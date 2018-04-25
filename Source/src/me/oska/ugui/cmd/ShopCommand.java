package me.oska.ugui.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.oska.ugui.manager.ModuleManager;

public class ShopCommand implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) 
	{
		if (arg0 instanceof Player)
		{
			Player p = (Player)arg0;
			if (arg2.equalsIgnoreCase("ugui"))
			{
				if (arg3.length == 2)
				{
					if (arg3[0].equalsIgnoreCase("open"))
					{
						ModuleManager.getShops().stream()
							.filter( x -> x.getShopCommand().equalsIgnoreCase(arg3[1]))
							.findFirst().ifPresent(x -> x.openInventory(p));
					}
				}
			}
		}
		return true;
	}

}
