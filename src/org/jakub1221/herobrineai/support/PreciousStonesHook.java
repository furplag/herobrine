package org.jakub1221.herobrineai.support;

import net.sacredlabyrinth.Phaed.PreciousStones.field.FieldFlag;
import net.sacredlabyrinth.Phaed.PreciousStones.PreciousStones;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class PreciousStonesHook {

	public boolean Check() {
		return (Bukkit.getServer().getPluginManager().getPlugin("PreciousStones") != null);
	}

	public boolean isSecuredArea(Location loc) {
		return PreciousStones.API().isFieldProtectingArea(FieldFlag.ALL, loc);
	}
}
