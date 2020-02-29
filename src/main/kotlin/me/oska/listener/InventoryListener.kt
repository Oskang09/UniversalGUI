package me.oska.listener

import me.oska.manager.InventoryManager
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class InventoryListener : Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    fun close(event: InventoryCloseEvent) {
        print("close");
        if (event.player !is Player) {
            return;
        }

        val uuid: String = event.player.uniqueId.toString();
        if (InventoryManager.hasPlayer(uuid)) {
            val state = InventoryManager.removePlayer(uuid);
            state.thread.parallelStream().filter { thread -> thread.isAlive }.forEach { thread -> thread.interrupt() }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun click(event: InventoryClickEvent) {
        print("click");
        if (event.whoClicked !is Player) {
            return;
        }

        val uuid: String = event.whoClicked.uniqueId.toString();
        if (InventoryManager.hasPlayer(uuid)) {
            event.isCancelled = true;

            val state = InventoryManager.getPlayer(uuid);
            if (event.clickedInventory == state.inventory) {
                state.currentShop.action(state.page, event.slot, event.whoClicked as Player);
            }
        }
    }
}