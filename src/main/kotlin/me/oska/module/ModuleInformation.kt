package me.oska.module

abstract class ModuleInformation {
    abstract fun getAuthor(): String;
    abstract fun getName(): String;
    abstract fun getVersion(): String;
    abstract fun getIdentifier(): String;

    @Throws(ModuleNotConfigured::class, ModuleNotSupported::class)
    abstract fun getModule(type: ModuleType, config: Map<*, *>): Module;

    @Throws(ModuleNotSupported::class)
    abstract fun isSupported();
}