package me.oska.module

import org.bukkit.inventory.ItemStack

abstract class ItemProvider {
    abstract fun getAuthor(): String;
    abstract fun getName(): String;
    abstract fun getVersion(): String;

    @Throws(ModuleNotSupported::class)
    abstract fun isSupported();

    abstract fun key(): String
    abstract fun get(config: Map<*, *>): ItemStack
}