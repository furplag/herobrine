package net.theprogrammersworld.herobrine.support;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.palmergames.bukkit.towny.Towny;
import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.object.TownyUniverse;

public class TownyHook {
	public boolean Check() {
		return (Bukkit.getServer().getPluginManager().getPlugin("Towny") != null);

	}

	public boolean isSecuredArea(Location loc) {

		Towny towny = (Towny) Bukkit.getServer().getPluginManager().getPlugin("Towny");
		towny.getTownyUniverse();
		TownBlock block = TownyUniverse.getTownBlock(loc);
		return (block != null);
	}
}
