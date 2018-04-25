package me.oska.ugui.obj;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.oska.ugui.abs.ClickAction;
import me.oska.ugui.abs.DynamicGUI;
import me.oska.ugui.abs.ShopRequirement;
import me.oska.ugui.abs.ShopReward;
import me.oska.ugui.abs.UIAction;
import me.oska.ugui.abs.UIElement;
import me.oska.ugui.manager.ModuleManager;
import me.oska.ugui.util.InventoryUtil;

public class Shop 
{
	private int npc_id;
	private File file;
	private String cmdshop = null;
	private FileConfiguration fcfg;
	private DynamicGUI dgui;
	
	public Shop(File f)
	{
		FileConfiguration fcfg = YamlConfiguration.loadConfiguration(f);
		this.npc_id = fcfg.getInt("npc_id");
		this.file = f;
		this.fcfg = fcfg;
		this.dgui = new DynamicGUI(fcfg.getString("gui_name").replaceAll("&", "§"), fcfg.getInt("gui_rows"));
		this.cmdshop = fcfg.getString("command");
		for (String i : InventoryUtil.getSlots(fcfg))
		{
			if (i.contains("-"))
			{
				int number1 = Integer.parseInt(i.split("\\-")[0]);
				int number2 = Integer.parseInt(i.split("\\-")[1]) + 1;
				for (int i2 = number1; i2 < number2 ;i2++)
				{
					this.dgui.addElement(new UIElement(i2, InventoryUtil.getDisplayItem(fcfg, i)));
				}
			}
			else
			{
				if (InventoryUtil.getEventSlots(fcfg).contains(i))
				{
					this.dgui.addElement(new UIElement(Integer.parseInt(i), InventoryUtil.getDisplayItem(fcfg, i)
							,new UIAction(ClickType.LEFT, new ClickAction()
									{
										@Override
										public void applyAction(InventoryClickEvent event)
										{
											checkCondition(event.getSlot(), (Player)event.getWhoClicked());
										}
									})));
				}
				else
				{
					this.dgui.addElement(new UIElement(Integer.parseInt(i), InventoryUtil.getDisplayItem(fcfg, i)));
				}
			}
		}
		ModuleManager.initializeShops(this);
	}
	
	public void checkCondition(int slot, Player p)
	{
		List<ShopRequirement> reqlist = new ArrayList<>();
		for (String key : this.getConfig().getConfigurationSection("event." + slot + ".requirement").getKeys(false))
		{
			for (ShopRequirement re : ModuleManager.getRequirements())
			{
				if (re.isConfigured(this.getConfig(), String.valueOf(slot), key))
				{
					re.loadConfigRecursive(this.getConfig(), String.valueOf(slot), key, "event." + slot + ".requirement." + key + "." + re.getName());
					if (!re.check(p))
					{
						if (InventoryUtil.getMessage(this.getConfig(), String.valueOf(slot), "fail") != null)
						{
							p.sendMessage(InventoryUtil.getMessage(this.getConfig(), String.valueOf(slot), "fail"));
						}
						return;
					}
					reqlist.add(re);
				}
				else
				{
					continue;
				}
			}
		}
		for (ShopRequirement sr : ModuleManager.getRequirements())
		{
			sr.remove(p);
		}
		for (String key : this.getConfig().getConfigurationSection("event." + slot + ".reward").getKeys(false))
		{
			for (ShopReward sr : ModuleManager.getRewards())
			{
				if (sr.isConfigured(this.getConfig(), String.valueOf(slot), key))
				{
					sr.loadConfigRecursive(this.getConfig(), String.valueOf(slot), key, "event." + slot + ".reward." + key + "." + sr.getName());
					sr.add(p);
				}
			}
		}
		if (InventoryUtil.getMessage(this.getConfig(), String.valueOf(slot), "sucess") != null)
		{
			p.sendMessage(InventoryUtil.getMessage(this.getConfig(), String.valueOf(slot), "sucess"));
		}
	}
	
	public void openInventory(Player p)
	{
		if (p.hasPermission("ugui.shop." + cmdshop))
		{
			dgui.open(p);
		}
		else
		{
			p.sendMessage(fcfg.getString("message.no_permission"));
		}
	}
	public FileConfiguration getConfig()
	{
		return this.fcfg;
	}
	public File getFile()
	{
		return this.file;
	}
	
	public int getNPC()
	{
		return this.npc_id;
	}
	public String getShopCommand()
	{
		return this.cmdshop;
	}
}
