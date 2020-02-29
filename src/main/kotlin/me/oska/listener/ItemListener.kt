package me.oska.listener

import me.oska.manager.ShopManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot

class ItemListener : Listener {

    @EventHandler
    fun interact(event: PlayerInteractEvent) {
        if (event.hand == EquipmentSlot.HAND) {
            return;
        }
        event.item?.let {
            ShopManager.displayShop(event.player, it)
        };
    }

}