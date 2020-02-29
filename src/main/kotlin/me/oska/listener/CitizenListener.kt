package me.oska.listener

import me.oska.manager.ShopManager
import net.citizensnpcs.api.event.NPCClickEvent
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class CitizenListener: Listener {

    @EventHandler
    fun interact(event: NPCClickEvent) {
        if (event.clicker !is Player) {
            return;
        }
        ShopManager.displayShop(event.clicker, event.npc.id);
    }
}