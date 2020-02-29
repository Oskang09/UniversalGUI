package me.oska.module

import me.oska.UniversalGUI
import org.bukkit.entity.Player

abstract class Module {

    @Throws(ModuleNotSupported::class)
    abstract fun isSupported()

    @Throws(ModuleInvalidConfig::class)
    abstract fun isConfigured(type: ModuleType, config: Map<String, Any>)

    abstract fun check(player: Player, type: ModuleType): Boolean
    abstract fun action(player: Player, type: ModuleType)

    abstract fun getAuthor(): String
    abstract fun getName(): String
    abstract fun getVersion(): String
    abstract fun getIdentifier(): String

    fun log(message: String) {
        UniversalGUI.log(message, this);
    }
}