package me.oska.ugui.abs;

import org.bukkit.event.inventory.InventoryClickEvent;

@FunctionalInterface
public interface ClickAction
{
	public void applyAction(InventoryClickEvent event);
}
