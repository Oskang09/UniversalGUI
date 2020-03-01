package me.oska.config.shop

import de.leonhard.storage.sections.FlatFileSection
import me.clip.placeholderapi.PlaceholderAPI
import me.oska.manager.PluginManager
import org.bukkit.entity.Player

class MessageConfig(config: FlatFileSection) {

    companion object {
        const val KEY_CONST = "const";
        const val KEY_FULFILLED = "fulfilled";
        const val KEY_REJECTED = "rejected";
    }

    private var const: String = config.getString("message.$KEY_CONST");
    private var fulfilled: String = config.getString("message.$KEY_FULFILLED");
    private var rejected: String = config.getString("message.$KEY_REJECTED")

    fun requireCheck(): Boolean {
        return const == "";
    }

    fun getMessage(player: Player): String {
        if (PluginManager.isPlaceholderSupported) {
            return PlaceholderAPI.setPlaceholders(player, const);
        }
        return const;
    }

    fun getFulFilled(player: Player): String {
        if (PluginManager.isPlaceholderSupported) {
            return PlaceholderAPI.setPlaceholders(player, fulfilled);
        }
        return fulfilled;
    }

    fun getRejected(player: Player): String {
        if (PluginManager.isPlaceholderSupported) {
            return PlaceholderAPI.setPlaceholders(player, rejected);
        }
        return rejected;
    }
}