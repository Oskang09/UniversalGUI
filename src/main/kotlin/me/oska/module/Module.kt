package me.oska.module

import org.bukkit.entity.Player

abstract class Module {
    abstract fun check(player: Player): Boolean;
    abstract fun action(player: Player);
}