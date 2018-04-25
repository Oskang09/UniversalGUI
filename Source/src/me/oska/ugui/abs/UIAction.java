package me.oska.ugui.abs;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

public class UIAction
{
	private ClickType click_type;
	private ClickAction action;
	public UIAction(ClickType ct, ClickAction at)
	{
		click_type = ct;
		action = at;
	}

	public void RunAction(ClickType ct, InventoryClickEvent event)
	{
		if (click_type == ct)
		{
			action.applyAction(event);
		}
	}
	public ClickType getClick()
	{
		return this.click_type;
	}
}
