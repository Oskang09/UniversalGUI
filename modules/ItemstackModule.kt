package modules

import me.oska.module.Module
import me.oska.module.ModuleType
import org.bukkit.entity.Player

class ItemstackModule: Module() {

    override fun isSupported() {

    }

    override fun isConfigured(type: ModuleType, config: Map<String, Any>) {

    }

    override fun check(player: Player, type: ModuleType): Boolean {
        return true;
    }

    override fun action(player: Player, type: ModuleType) {

    }

    override fun getAuthor(): String {
        return "Oska";
    }

    override fun getName(): String {
        return "ItemstackModule"
    }

    override fun getVersion(): String {
        return "0.0.1"
    }

    override fun getIdentifier(): String {
        return "item";
    }

}