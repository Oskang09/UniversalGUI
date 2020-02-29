package me.oska.manager

import me.oska.UniversalGUI
import me.oska.listener.CitizenListener
import me.oska.listener.CommandListener
import me.oska.listener.InventoryListener
import me.oska.listener.ItemListener
import org.bukkit.Bukkit

object PluginManager {
    var isPlaceholderSupported: Boolean = false;
    var isCitizensSupported: Boolean = false;

    fun onStart() {
        isCitizensSupported = UniversalGUI.getPluginManager().getPlugin("Citizens") != null;
        isPlaceholderSupported = UniversalGUI.getPluginManager().getPlugin("PlaceholderAPI") != null;
        ModuleManager.initialize();
        ShopManager.initialize();

        UniversalGUI.getInstance().getCommand("ugui")?.setExecutor(CommandListener());
        UniversalGUI.getPluginManager().registerEvents(InventoryListener(), UniversalGUI.getInstance());
        UniversalGUI.getPluginManager().registerEvents(ItemListener(), UniversalGUI.getInstance());
        if (isCitizensSupported) {
            UniversalGUI.getPluginManager().registerEvents(CitizenListener(), UniversalGUI.getInstance());
        }
    }

    fun onClose() {
        InventoryManager.loopPlayer { uuid, _ ->
            run {
                Bukkit.getPlayer(uuid)?.closeInventory();
            }
        }
    }
}