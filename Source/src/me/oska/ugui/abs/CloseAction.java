package me.oska.ugui.abs;

import org.bukkit.event.inventory.InventoryCloseEvent;

@FunctionalInterface
public interface CloseAction
{
	public void applyAction(InventoryCloseEvent event);
}
