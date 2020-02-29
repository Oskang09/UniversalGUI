package me.oska.config

import me.oska.manager.ModuleManager
import me.oska.module.Module
import me.oska.module.ModuleNotExists
import me.oska.module.ModuleType
import org.bukkit.entity.Player

class ModuleConfig(private var type: ModuleType, setting: Map<String, Any>) {

    var module: Module
    var name: String
    var display: String

    init {
        this.name = setting[KEY_MODULE] as String
        this.display = setting[KEY_DISPLAY] as String
        this.module = ModuleManager.getModule(this.name) ?: throw ModuleNotExists("Module \"${this.name}\" doesn't exists.");
        this.module.isConfigured(type, setting);
    }

    companion object {
        const val KEY_MODULE = "module";
        const val KEY_DISPLAY = "display";
    }

    fun check(player: Player): Boolean {
        return this.module.check(player, this.type);
    }

    fun action(player: Player) {
        return this.module.action(player, this.type);
    }
}
