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
        if (event.player !is Player) {
            return;
        }

        val uuid: String = event.player.uniqueId.toString();
        if (InventoryManager.hasPlayer(uuid)) {
            InventoryManager.removePlayer(uuid);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun click(event: InventoryClickEvent) {
        if (event.whoClicked !is Player) {
            return;
        }

        val uuid: String = event.whoClicked.uniqueId.toString();
        if (InventoryManager.hasPlayer(uuid)) {
            val state = InventoryManager.getPlayer(uuid);
            if (event.clickedInventory != state.inventory) {
                return;
            }

            event.isCancelled = true;
            state.currentShop.action(state, event.slot, event.whoClicked as Player);
        }
    }
}