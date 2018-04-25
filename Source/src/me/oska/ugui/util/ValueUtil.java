package me.oska.ugui.util;

public class ValueUtil 
{
	public static boolean isInteger(String args)
	{
		try
		{
			Integer.parseInt(args);
		}
		catch (NumberFormatException e)
		{
			return false;
		}
		return true;
	}
}
