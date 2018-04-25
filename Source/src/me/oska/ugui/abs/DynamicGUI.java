package me.oska.ugui.abs;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import me.oska.ugui.UniversalGUI;

public class DynamicGUI implements Listener
{
	private Inventory inv;
	private List<Player> players;
	private UIElement[] ui;
	private int page;
	private CloseAction ca;

	public DynamicGUI(String title, int height)
	{
		Bukkit.getServer().getPluginManager().registerEvents(this, UniversalGUI.getInstance());
		this.inv = Bukkit.createInventory(null, height * 9, title);
		this.ui = new UIElement[height * 9];
		this.page = 1;
		this.players = new ArrayList<Player>();
	}

	public void setCloseAction(CloseAction cat)
	{
		ca = cat;
	}
	
	public void setPage(int page)
	{
		this.page = page;
	}
	
	public int getPage()
	{
		return this.page;
	}
	
	public void addElement(UIElement uie)
	{
		this.inv.setItem(uie.getSlot(), uie.getItem());
		ui[uie.getSlot()] = uie;
	}

	public void removeElement(int slot)
	{
		ui[slot] = null;
	}

	public void open(Player player)
	{
		player.openInventory(inv);
		players.add(player);
	}

	public void close(Player player)
	{
		player.closeInventory();
		players.remove(player);
	}

	@EventHandler
	public void onInvClose(InventoryCloseEvent event)
	{
		if (players.contains(event.getPlayer()))
		{
			players.remove(event.getPlayer());
			if (ca != null)
			{
				ca.applyAction(event);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onInvClick(InventoryClickEvent event)
	{
		if (players.contains(event.getWhoClicked()))
		if (event.getClickedInventory() != null && event.getClickedInventory().equals(inv))
		{
			event.setCancelled(true);
			if (event.getSlot() > -1 && event.getSlot() < event.getInventory().getSize() && ui[event.getSlot()] != null)
			{
				ui[event.getSlot()].Action(event.getClick(), event);
			}	
		}
	}
}
