package me.oska

import de.leonhard.storage.LightningFile
import me.oska.listener.CitizenListener
import me.oska.listener.CommandListener
import me.oska.listener.InventoryListener
import me.oska.listener.ItemListener
import me.oska.manager.ModuleManager
import me.oska.manager.ShopManager
import me.oska.module.Module
import org.bukkit.Bukkit
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitScheduler
import java.io.File
import java.util.logging.Logger

class UniversalGUI: JavaPlugin() {

    companion object {
        private lateinit var instance: UniversalGUI;
        private lateinit var modulePath: File;
        private lateinit var shopPath: File;
        private lateinit var apiPath: File;
        private lateinit var manager: PluginManager;
        private lateinit var scheduler: BukkitScheduler;

        fun getInstance(): UniversalGUI {
            return instance;
        }

        fun getModuleFolder(): File {
            return modulePath;
        }

        fun getShopFolder(): File {
            return shopPath;
        }

        fun getApiPath(): File {
            return apiPath;
        }

        fun getPluginManager(): PluginManager {
            return manager;
        }

        fun getScheduler(): BukkitScheduler {
            return scheduler;
        }

        fun log(message: String, module: Module? = null) {
            Bukkit.getLogger().info(
                if (module != null) "[Module@${module.getName()}] $message" else message
            );
        }
    }

    init {
        instance = this
        modulePath = File(dataFolder, "modules");
        shopPath = File(dataFolder, "shops");
        apiPath = File(dataFolder, "_api");
        manager = server.pluginManager;
        scheduler = Bukkit.getScheduler();
    }

    override fun onEnable() {
        if (!apiPath.exists()) {
            apiPath.mkdirs();
        }

        if(!modulePath.exists()) {
            modulePath.mkdirs();
        }

        if (!shopPath.exists()) {
            shopPath.mkdirs();
        }
        me.oska.manager.PluginManager.onStart();
    }

    override fun onDisable() {
        me.oska.manager.PluginManager.onClose();
    }
}