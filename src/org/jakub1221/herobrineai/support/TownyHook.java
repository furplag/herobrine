package org.jakub1221.herobrineai.support;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jakub1221.customitems.API;
import org.jakub1221.customitems.CustomItems;

import com.palmergames.bukkit.towny.Towny;
import com.palmergames.bukkit.towny.object.TownBlock;

public class TownyHook {
	public boolean Check() {
		return (Bukkit.getServer().getPluginManager().getPlugin("Towny") != null);

	}

	public boolean isSecuredArea(Location loc) {

		Towny towny = (Towny) Bukkit.getServer().getPluginManager().getPlugin("Towny");
		TownBlock block = towny.getTownyUniverse().getTownBlock(loc);
		return (block != null);
	}
}
