package net.theprogrammersworld.herobrine.support;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.palmergames.bukkit.towny.TownyAPI;

public class TownyHook {
	public boolean Check() {
		return (Bukkit.getServer().getPluginManager().getPlugin("Towny") != null);

	}

	public boolean isSecuredArea(Location loc) {
		return (TownyAPI.getInstance().getTownBlock(loc) != null);
	}
}
