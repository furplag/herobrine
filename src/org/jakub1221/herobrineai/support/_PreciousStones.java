package org.jakub1221.herobrineai.support;

import net.sacredlabyrinth.Phaed.PreciousStones.FieldFlag;
import net.sacredlabyrinth.Phaed.PreciousStones.PreciousStones;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class _PreciousStones {
	
	public boolean Check(){
		return (Bukkit.getServer().getPluginManager().getPlugin("PreciousStones")!=null);
	}
	
	public boolean isSecuredArea(Location loc){
		PreciousStones preciousStones = (PreciousStones) Bukkit.getServer().getPluginManager().getPlugin("PreciousStones");
	
		return preciousStones.API().isFieldProtectingArea(FieldFlag.ALL, loc);
	}
}
