package net.theprogrammersworld.herobrine.support;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import br.net.fabiozumbi12.RedProtect.Bukkit.RedProtect;

public class RedProtectHook {
	public boolean Check() {
		return Bukkit.getServer().getPluginManager().getPlugin("RedProtect") != null;
	}

	public boolean isSecuredArea(final Location loc) {
		return RedProtect.get().getAPI().getRegion(loc) != null;
	}
}
