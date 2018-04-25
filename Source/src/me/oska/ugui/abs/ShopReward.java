package me.oska.ugui.abs;

import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public abstract class ShopReward 
{
	private String name;
	private String displayname;
	public HashMap<String, Object> configmap = new HashMap<>();
	private String author;
	
	public abstract void add(Player p);
	public abstract boolean checkHook();
	
	public String getAuthor()
	{
		return this.author;
	}
	public void setAuthor(String author)
	{
		this.author = author;
	}
	public void setConfigName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return this.name;
	}
	public void setDisplayName(String name)
	{
		this.displayname = name;
	}
	public String getDisplayName()
	{
		return this.displayname;
	}
	
	public void loadConfigRecursive(FileConfiguration cs, String slot, String key, String lopkey)
	{
		for (String key2 : cs.getConfigurationSection(lopkey).getKeys(false))
		{
			if (cs.isConfigurationSection(lopkey + "." + key2))
			{
				loadConfigRecursive(cs, slot, key, lopkey + "." + key2);
			}
			else
			{
				this.configmap.put((lopkey + "." + key2).replaceAll("event." + slot + ".reward." + key + "." + this.getName() + ".", ""), cs.get(lopkey + "." + key2));
			}
		}
	}
	
	public boolean isConfigured(FileConfiguration fcfg, String slot, String key)
	{
		for (String key2 : fcfg.getConfigurationSection("event." + slot + ".reward." + key).getKeys(false))
		{
			if (key2.equals(name))
			{
				return true;
			}
		}
		return false;
	}
}
