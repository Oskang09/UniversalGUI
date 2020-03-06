import me.clip.placeholderapi.PlaceholderAPI
import me.oska.UniversalGUI
import me.oska.module.*
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class CommandModule: ModuleInformation() {

    private var isPlaceholderSupported: Boolean = false;

    override fun isSupported() {
        isPlaceholderSupported = UniversalGUI.getPluginManager().getPlugin("PlaceholderAPI") != null
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

        val commands = config["commands"] ?: throw ModuleNotConfigured("missing 'commands' from configuration.");
        val executeBy = config["executeBy"] ?: "SERVER"

        @Suppress("UNCHECKED_CAST")
        val cmd: List<String> = commands as? List<String> ?: throw ModuleNotConfigured("commands is not a string list, received $commands")
        val exec: String = executeBy as? String ?: throw ModuleNotConfigured("executeBy is not a string, received $executeBy")
        return ActionModule(exec, cmd, isPlaceholderSupported);
    }

    internal class ActionModule constructor(
        private val executeBy: String,
        private val commands: List<String>,
        private val isPlaceholderSupported: Boolean
    ): Module() {

        private fun getCommands(player: Player): List<String> {
            if (isPlaceholderSupported) {
                return PlaceholderAPI.setPlaceholders(player, commands)
            }
            return commands.map {
                command -> command.
                    replace("%player_name%", player.name).
                    replace("%player_uuid%", player.uniqueId.toString()).
                    replace("%player_level%", player.level.toString())
            }
        }

        override fun check(player: Player): Boolean {
            return true;
        }

        override fun action(player: Player) = if (executeBy == "SERVER")
            getCommands(player).forEach {command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command)}
        else
            getCommands(player).forEach {command -> player.performCommand(command)}
    }
}