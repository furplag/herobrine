package org.jakub1221.herobrineai.support;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.jakub1221.customitems.API;
import org.jakub1221.customitems.CustomItems;

public class CustomItemsHook {

	private CustomItems ci = null;
	private API api = null;

	public void init() {
		ci = (CustomItems) Bukkit.getServer().getPluginManager().getPlugin("CustomItems");
		api = ci.getAPI();
	}

	public boolean Check() {
		return (Bukkit.getServer().getPluginManager().getPlugin("CustomItems") != null);
	}

	public boolean checkItem(String name) {
		if (ci != null) {
			return api.itemExist(name);
		}
		return false;

	}

	public ItemStack getItem(String name) {
		return api.createItem(name);
	}

}
