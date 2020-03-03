package me.oska.module

abstract class ModuleInformation {
    abstract fun getAuthor(): String;
    abstract fun getName(): String;
    abstract fun getVersion(): String;
    abstract fun getIdentifier(): String;
    abstract fun supportParallel(): Boolean;

    @Throws(ModuleNotConfigured::class, ModuleNotSupported::class)
    abstract fun getModule(type: ModuleType, config: Map<*, *>): Module;

    @Throws(ModuleNotSupported::class)
    abstract fun isSupported();
}