package me.oska.ugui;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.io.FilenameUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import me.oska.ugui.abs.ShopRequirement;
import me.oska.ugui.abs.ShopReward;
import me.oska.ugui.cmd.ShopCommand;
import me.oska.ugui.io.FileReader;
import me.oska.ugui.manager.ModuleManager;
import me.oska.ugui.obj.Shop;

public class UniversalGUI extends JavaPlugin
{
	private static UniversalGUI main;
	public UniversalGUI()
	{
		main = this;
	}
	public static UniversalGUI getInstance()
	{
		return main;
	}
	
	@Override
	public void onEnable()
	{
		FileReader.runFileInit("modules/example_module");
		FileReader.runFileInit("shops/example_shop");
		FileReader.runFileInit("message");
		
		File f = new File(this.getDataFolder() + "/modules");
		registerRecursive(f);
		
		f = new File(this.getDataFolder() + "/shops");
		registerShop(f);
		
		Bukkit.getServer().getPluginCommand("ugui").setExecutor(new ShopCommand());
	}
	
	@Override
	public void onDisable()
	{
		HandlerList.unregisterAll(this);
	}
	
	public void registerShop(File f)
	{
		for (File f2 : f.listFiles())
		{
			if (f2.isDirectory())
			{
				registerShop(f2);
			}
			else
			{
				if (f2.isFile())
				{
					new Shop(f2);
				}
			}
		}
	}
	
	public void registerRecursive(File f)
	{
		for (File f2 : f.listFiles())
		{
			if (f2.isDirectory())
			{
				registerRecursive(f2);
			}
			else
			{
				if (f2.isFile())
				{
					if (FilenameUtils.getExtension(f2.getName()).equalsIgnoreCase("jar"))
					{
						try
						{
							JarFile jarfile = new JarFile(f2);
							Enumeration<JarEntry> entry = jarfile.entries();
							URL[] urls = { new URL("jar:file:" + f2.getPath() + "!/") };
							URLClassLoader cl = URLClassLoader.newInstance(urls, this.getClassLoader());
							while (entry.hasMoreElements())
							{
								JarEntry je = (JarEntry)entry.nextElement();
								if (je.isDirectory() || !je.getName().endsWith(".class"))
								{
									continue;
								}
								String classname = je.getName().substring(0, je.getName().length() - 6);
								classname = classname.replace('/', '.');
								Class<?> clazz = Class.forName(classname, true, cl);
								if (ShopRequirement.class.isAssignableFrom(clazz))
								{
									Class<? extends ShopRequirement> pl = clazz.asSubclass(ShopRequirement.class);
									Constructor<? extends ShopRequirement> cst = pl.getConstructor();
									ShopRequirement pli = cst.newInstance();
									if (pli.checkHook())
									{
										ModuleManager.initializeCondition(pli);
										UniversalGUI.getInstance().getLogger().info("Loaded requirement module " +  pli.getDisplayName() + " by " + pli.getAuthor());
									}
								}
								if (ShopReward.class.isAssignableFrom(clazz))
								{
									Class<? extends ShopReward> pl = clazz.asSubclass(ShopReward.class);
									Constructor<? extends ShopReward> cst = pl.getConstructor();
									ShopReward pli = cst.newInstance();
									if (pli.checkHook())
									{
										ModuleManager.initializeReward(pli);
										UniversalGUI.getInstance().getLogger().info("Loaded reward module " +  pli.getDisplayName() + " by " + pli.getAuthor());
									}
								}
							}
							cl.close();
							jarfile.close();
						}
						catch (Exception e1)
						{
							UniversalGUI.getInstance().getLogger().severe("Load module failed from ( " + f2.getName() + " )");
							e1.printStackTrace();
						}
					}
				}
			}
		}
	}
}
