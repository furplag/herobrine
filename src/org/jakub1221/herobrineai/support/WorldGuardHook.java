package org.jakub1221.herobrineai.support;

import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class WorldGuardHook {
	public boolean Check() {

		return (Bukkit.getServer().getPluginManager().getPlugin("WorldGuard") != null);
	}

	public boolean isSecuredArea(Location loc) {

		WorldGuardPlugin worldGuard = (WorldGuardPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
		RegionManager rm = worldGuard.getRegionManager(loc.getWorld());
		if (rm != null) {
			Map<String, ProtectedRegion> mp = rm.getRegions();
			if (mp != null) {
				for (Entry<String, ProtectedRegion> s : mp.entrySet()) {
					if (s.getValue().contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())) {
						return true;
					}
				}
			}
		}

		return false;
	}
}
