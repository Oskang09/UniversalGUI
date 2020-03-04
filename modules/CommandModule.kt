import me.oska.module.Module
import me.oska.module.ModuleInformation
import me.oska.module.ModuleNotSupported
import me.oska.module.ModuleType
import org.bukkit.entity.Player

class CommandModule: ModuleInformation() {

    override fun isSupported() {

    }

    override fun getAuthor(): String {
        return "Oska";
    }

    override fun getName(): String {
        return "CommandModule"
    }

    override fun getVersion(): String {
        return "0.0.1"
    }

    override fun getIdentifier(): String {
        return "command";
    }

    override fun supportParallel(): Boolean {
        return true;
    }

    override fun getModule(type: ModuleType, config: Map<*, *>): Module {
        if (type == ModuleType.REQUIREMENT) {
            throw ModuleNotSupported("command module doesn't support requirement option");
        }
        return ActionModule(type, config)
    }

    internal class ActionModule constructor(private val type: ModuleType, config: Map<*, *>): Module() {
        override fun check(player: Player): Boolean {
            return true;
        }

        override fun action(player: Player) {

        }
    }
}