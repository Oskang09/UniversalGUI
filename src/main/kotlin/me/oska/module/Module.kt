package me.oska.module

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

abstract class Module {
    abstract fun check(player: Player): Boolean;
    abstract fun action(player: Player);
    abstract fun get(config: Map<*, *>): ItemStack;
}