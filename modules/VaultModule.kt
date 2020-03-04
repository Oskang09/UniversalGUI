import me.oska.UniversalGUI
import me.oska.module.*
import net.milkbowl.vault.economy.Economy
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class VaultModule: ModuleInformation() {

    private var economy: Economy? = null;

    override fun isSupported() {
        val isVaultSupported = UniversalGUI.getPluginManager().getPlugin("Vault") != null;
        if (!isVaultSupported) {
            throw ModuleNotSupported("vault not found unable enable economy feature")
        }

        val registration = UniversalGUI.getInstance().server.servicesManager.getRegistration(Economy::class.java)
                ?: throw ModuleNotSupported("vault registration not found unable enable economy feature")
        economy = registration!!.provider
    }

    override fun getAuthor(): String {
        return "Oska"
    }

    override fun getName(): String {
        return "VaultModule"
    }

    override fun getVersion(): String {
        return "0.0.1"
    }

    override fun getIdentifier(): String {
        return "vault"
    }

    override fun supportParallel(): Boolean {
        return true
    }

    override fun getModule(type: ModuleType, config: Map<*, *>): Module {
        return ActionModule(economy!!, type, config);
    }

    internal class ActionModule constructor(private val economy: Economy, private val type: ModuleType, config: Map<*, *>): Module() {

        private var money: Double;

        init {
            val money = config["money"] ?: throw ModuleNotConfigured("missing 'money' from configuration")

            try {
                this.money = money as Double
            } catch (ex: Throwable) {
                throw ModuleNotConfigured("money is not a decimal value, received $money")
            }
        }

        override fun check(player: Player): Boolean {
            if (type == ModuleType.REWARD) {
                return true;
            }
            return economy.hasAccount(player) && economy.has(player, this.money);
        }

        override fun action(player: Player) {
            if (type == ModuleType.REWARD) {
                economy.depositPlayer(player, this.money)
            } else {
                economy.withdrawPlayer(player, this.money)
            }
        }
    }
}