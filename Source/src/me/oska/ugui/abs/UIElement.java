package me.oska.ugui.abs;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class UIElement
{
	private int slot;
	private ItemStack item;
	private List<UIAction> uia;
	
	public UIElement(int slot, ItemStack item, UIAction uia)
	{
		this.slot = slot;
		this.item = item;
		this.uia = new ArrayList<UIAction>();
		this.uia.add(uia);
	}
	public UIElement(int slot, ItemStack item, List<UIAction> uia)
	{
		this.slot = slot;
		this.item = item;
		this.uia = uia;
	}
	public UIElement(int slot, ItemStack item)
	{
		this.slot = slot;
		this.item = item;
		this.uia = new ArrayList<UIAction>();
	}
	
	public int getSlot()
	{
		return this.slot;
	}
	public ItemStack getItem()
	{
		return this.item;
	}
	
	public void Action(ClickType ct, InventoryClickEvent event)
	{
		if (uia.size() > 0)
		{
			uia.forEach(x -> x.RunAction(ct, event));
		}
	}
	
	// Static Element
	public static UIElement previousElement(int slot, List<UIAction> uia)
	{
		ItemStack item = new ItemStack(Material.SLIME_BALL);
		ItemMeta im = item.getItemMeta();;
        im.setDisplayName("Â§fç‚¹å‡»è¿›å…¥ä¸Šä¸€é¡µ");
		item.setItemMeta(im);
		return new UIElement(slot, item, uia);
	}
	public static UIElement pageElement(int slot, int page, List<UIAction> uia)
	{
		ItemStack item = new ItemStack(Material.CHEST);
		ItemMeta im = item.getItemMeta();
		List<String> list = new ArrayList<String>();
        list.add("Â§aå½“å‰�é¡µæ•° - Â§f" + page);
        im.setDisplayName("Â§fé¡µæ•°è®¾ç½®");
		im.setLore(list);
		item.setItemMeta(im);
		return new UIElement(slot, item, uia);
	}
	public static UIElement nextElement(int slot, List<UIAction> uia)
	{
		ItemStack item = new ItemStack(Material.FEATHER);
		ItemMeta im = item.getItemMeta();;
        im.setDisplayName("Â§fç‚¹å‡»è¿›å…¥ä¸‹ä¸€é¡µ");
		item.setItemMeta(im);
		return new UIElement(slot, item, uia);
	}
}
