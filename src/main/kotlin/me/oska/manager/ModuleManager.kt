package me.oska.manager

import me.oska.UniversalGUI
import me.oska.extension.CommandModule
import me.oska.extension.ItemstackModule
import me.oska.extension.VaultModule
import me.oska.module.*

internal object ModuleManager {
    private var modules: MutableMap<String, ModuleInformation> = mutableMapOf();
    private var providers: MutableMap<String, ItemProvider> = mutableMapOf()

    fun initialize() {
        registerPluginModule(VaultModule())
        registerPluginModule(ItemstackModule())
        registerPluginModule(CommandModule())
    }

    fun isSupportParallel(name: String): Boolean {
        return modules[name]!!.supportParallel();
    }

    fun getProvider(name: String): ItemProvider? {
        return providers[name]
    }

    fun getModule(name: String, type: ModuleType, setting: Map<*, *>): Module? {
        return modules[name]?.getModule(type, setting);
    }

    fun registerItemProvider(provider: ItemProvider) {
        try {
            provider.isSupported()
            providers[provider.key()] = provider
            UniversalGUI.log("Registered new module ${provider.getName()} (${provider.getVersion()}) by ${provider.getAuthor()}")
        } catch (ex: ModuleNotSupported) {
            UniversalGUI.log("Fail to register module ${provider.getName()} due to " + ex.message);
        }
    }

    fun registerPluginModule(module: ModuleInformation) {
        try {
            module.isSupported()
            modules[module.getIdentifier()] = module
            UniversalGUI.log("Registered new module ${module.getName()} (${module.getVersion()}) by ${module.getAuthor()}")
        } catch (ex: ModuleNotSupported) {
            UniversalGUI.log("Fail to register module ${module.getName()} due to " + ex.message);
        }
    }
}