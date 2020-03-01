package me.oska.manager

import me.oska.config.shop.ShopConfig
import org.bukkit.inventory.Inventory

object InventoryManager {

    data class PlayerState(
            val page: Int,
            val inventory: Inventory,
            val currentShop: ShopConfig,
            val thread: List<Thread>
    );

    private var states: MutableMap<String, PlayerState> = mutableMapOf()

    fun loopPlayer(func: (String, PlayerState) -> Unit) {
        states.forEach(func);
    }

    fun removePlayer(uuid: String): PlayerState {
        return states.remove(uuid)!!;
    }

    fun getPlayer(uuid: String): PlayerState {
        return states[uuid]!!;
    }

    fun hasPlayer(uuid: String) :Boolean {
        return states.containsKey(uuid);
    }

    fun addPlayer(uuid: String, page: Int, inventory: Inventory, shop: ShopConfig, threads: List<Thread>) {
        states[uuid] = PlayerState(page, inventory, shop, threads);
    }
}