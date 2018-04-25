package me.oska.ugui.manager;

import java.util.ArrayList;
import java.util.List;

import me.oska.ugui.abs.ShopRequirement;
import me.oska.ugui.abs.ShopReward;
import me.oska.ugui.obj.Shop;

public class ModuleManager 
{
	private static List<ShopRequirement> req = new ArrayList<>();
	private static List<ShopReward> rew = new ArrayList<>();
	private static List<Shop> shops = new ArrayList<>();
	
	public static List<Shop> getShops()
	{
		return shops;
	}
	public static List<ShopRequirement> getRequirements()
	{
		return req;
	}
	public static List<ShopReward> getRewards()
	{
		return rew;
	}
 	public static void initializeReward(ShopReward sr)
	{
		rew.add(sr);
	}
	public static void initializeCondition(ShopRequirement sr)
	{
		req.add(sr);
	}
	public static void initializeShops(Shop sp)
	{
		shops.add(sp);
	}
}
