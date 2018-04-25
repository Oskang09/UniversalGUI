package me.oska.ugui.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryUtil 
{
	public static List<String> getSlots(FileConfiguration fcfg)
	{
		List<String> list = new ArrayList<String>();
		for (String s : fcfg.getConfigurationSection("gui").getKeys(false))
		{
			list.add(s);
		}
		return list;
	}
	public static List<String> getEventSlots(FileConfiguration fcfg)
	{
		List<String> list = new ArrayList<String>();
		for (String s : fcfg.getConfigurationSection("event").getKeys(false))
		{
			list.add(s);
		}
		return list;
	}
	public static String getMessage(FileConfiguration fcfg, String slot, String type)
	{
		if (fcfg.isSet("event." + slot+ ".msg." + type))
		{
			return fcfg.getString("event." + slot+ ".msg." + type).replaceAll("&", "§");
		}
		if (fcfg.isSet("msg." + type))
		{
			return fcfg.getString("msg." + type).replaceAll("&", "§");
		}
		return null;
	}
	@SuppressWarnings("deprecation")
	public static ItemStack getDisplayItem(FileConfiguration fcfg, String slot)
	{
		ItemStack item = new ItemStack(Material.getMaterial(fcfg.getInt("gui." + slot + ".id")));
		ItemMeta im = item.getItemMeta();
		List<String> lores = new ArrayList<String>();
		if (fcfg.isInt("gui." + slot + ".data"))
		{
			item.setDurability((short)fcfg.getInt("gui." + slot + ".data"));
		}
		if (fcfg.isString("gui." + slot + ".name"))
		{
			im.setDisplayName(fcfg.getString("gui." + slot + ".name").replaceAll("&", "§"));
		}
		if (fcfg.isList("gui." + slot + ".lore"))
		{
			for (String s : fcfg.getStringList("gui." + slot + ".lore"))
			{
				lores.add(s.replaceAll("&", "§"));
			}
			im.setLore(lores);
		}
		item.setItemMeta(im);
		return item;
	}
}
