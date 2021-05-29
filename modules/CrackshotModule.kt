import com.shampaggon.crackshot.CSUtility
import me.oska.module.*
import org.bukkit.entity.Player

class CrackshotModule: ModuleInformation() {

    private lateinit var crackshot: CSUtility;

    override fun isSupported() {
        try {
            crackshot = CSUtility()
        } catch (ex: Throwable) {
            throw ModuleNotSupported("unable to enable crackshot feature due to " + ex.message)
        }
    }

    override fun getAuthor(): String {
        return "Oska"
    }

    override fun getName(): String {
        return "CrackshotModule"
    }

    override fun getVersion(): String {
        return "0.0.1";
    }

    override fun getIdentifier(): String {
        return "crackshot";
    }

    override fun supportParallel(): Boolean {
        return false;
    }

    override fun getModule(type: ModuleType, config: Map<*, *>): Module {
        val configName = config["name"] ?: throw ModuleNotConfigured("missing 'name' from configuration.");
        val configAmount = config["amount"] ?: 1;

        val name: String = configName as? String ?: throw ModuleNotConfigured("name is not a string, received $configName")
        val amount: Int = configAmount as? Int ?: throw ModuleNotConfigured("amount is not a int, received $configAmount")
        return ActionModule(crackshot, type, name, amount);
    }

    internal class ActionModule constructor(
        private val crackshot: CSUtility,
        private val type: ModuleType,
        private val weaponName: String,
        private val amount: Int
    ): Module() {

        override fun check(player: Player): Boolean {
            if (type == ModuleType.REQUIREMENT) {
                val item = crackshot.generateWeapon(weaponName)
                return player.inventory.contains(item);
            }
            return player.inventory.firstEmpty() == -1;
        }

        override fun action(player: Player) {
            if (type == ModuleType.REQUIREMENT) {
                val item = crackshot.generateWeapon(weaponName);
                player.inventory.removeItem(item);
            } else {
                crackshot.giveWeapon(player, weaponName, amount);
            }
        }
    }
}