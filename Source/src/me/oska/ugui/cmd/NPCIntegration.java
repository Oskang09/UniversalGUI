package me.oska.ugui.cmd;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.oska.ugui.manager.ModuleManager;
import net.citizensnpcs.api.event.NPCRightClickEvent;

public class NPCIntegration implements Listener
{
	@EventHandler
	public void onNpcClick(NPCRightClickEvent event)
	{
		ModuleManager.getShops().stream()
			.filter(x -> x.getNPC() == event.getNPC().getId())
			.findFirst().ifPresent(x -> x.openInventory(event.getClicker()));
	}
}
