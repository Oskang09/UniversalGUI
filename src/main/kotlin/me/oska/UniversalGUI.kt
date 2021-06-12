package me.oska

import me.oska.manager.ModuleManager
import me.oska.module.ItemProvider
import me.oska.module.ModuleInformation
import org.bukkit.Bukkit
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitScheduler
import java.io.File
import java.util.logging.Logger

class UniversalGUI: JavaPlugin() {

    companion object {
        private lateinit var instance: UniversalGUI;
        private lateinit var shopPath: File;
        private lateinit var apiPath: File;
        private lateinit var configPath: File;
        private lateinit var manager: PluginManager;
        private lateinit var scheduler: BukkitScheduler;
        private lateinit var console: Logger;

        fun getInstance(): UniversalGUI {
            return instance;
        }

        fun getConfigFile(): File {
            return configPath;
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

        fun log(message: String) {
            console.info(message);
        }
    }

    fun registerProvider(module: ItemProvider) {
        ModuleManager.registerItemProvider(module)
    }

    fun registerModule(module: ModuleInformation) {
        ModuleManager.registerPluginModule(module)
    }

    init {
        instance = this
        shopPath = File(dataFolder, "shops");
        apiPath = File(dataFolder, "_api");
        configPath = File(dataFolder, "config.json");
        manager = server.pluginManager;
        scheduler = Bukkit.getScheduler();
        console = logger;
    }

    override fun onEnable() {
        if (!apiPath.exists()) {
            apiPath.mkdirs();
        }

        if (!shopPath.exists()) {
            shopPath.mkdirs();
        }

        if (!configPath.exists()) {
            saveResource("config.json", false)
        }

        me.oska.manager.PluginManager.onStart();
    }

    override fun onDisable() {
        me.oska.manager.PluginManager.onClose();
    }
}