package me.oska.module

import kotlin.reflect.KClass

abstract class ModuleInformation {
    abstract fun getAuthor(): String;
    abstract fun getName(): String;
    abstract fun getVersion(): String;
    abstract fun getIdentifier(): String;
    abstract fun getModule(type: ModuleType, config: Map<*, *>): Module;

    @Throws(ModuleNotSupported::class)
    abstract fun isSupported();
}