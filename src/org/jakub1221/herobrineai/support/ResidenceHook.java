package org.jakub1221.herobrineai.support;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;

public class ResidenceHook {

	public boolean Check() {
		return (Bukkit.getServer().getPluginManager().getPlugin("Residence") != null);

	}

	public boolean isSecuredArea(Location loc) {
		Residence residence = (Residence) Bukkit.getServer().getPluginManager().getPlugin("Residence");
		ClaimedResidence res = residence.getResidenceManager().getByLoc(loc);
		return (res != null);
	}

}
