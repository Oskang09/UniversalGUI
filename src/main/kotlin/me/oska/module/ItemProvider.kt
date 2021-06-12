package me.oska.module

import org.bukkit.inventory.ItemStack

abstract class ItemProvider {
    abstract fun key(): String
    abstract fun get(config: Map<*, *>): ItemStack
}