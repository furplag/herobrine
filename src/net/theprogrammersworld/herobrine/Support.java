package net.theprogrammersworld.herobrine;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import net.theprogrammersworld.herobrine.support.FactionsHook;
import net.theprogrammersworld.herobrine.support.GriefPreventionHook;
import net.theprogrammersworld.herobrine.support.PreciousStonesHook;
import net.theprogrammersworld.herobrine.support.RedProtectHook;
import net.theprogrammersworld.herobrine.support.ResidenceHook;
import net.theprogrammersworld.herobrine.support.TownyHook;
import net.theprogrammersworld.herobrine.support.WorldGuardHook;

public class Support {

	private boolean B_Residence = false;
	private boolean B_GriefPrevention = false;
	private boolean B_Towny = false;
	private boolean B_WorldGuard = false;
	private boolean B_PreciousStones = false;
	private boolean B_Factions = false;
	private boolean B_RedProtect = false;
	private ResidenceHook ResidenceCore = null;
	private GriefPreventionHook GriefPreventionCore = null;
	private TownyHook TownyCore = null;
	private WorldGuardHook WorldGuard = null;
	private PreciousStonesHook PreciousStones = null;
	private FactionsHook Factions = null;
	private RedProtectHook RedProtect = null;

	public Support() {
		ResidenceCore = new ResidenceHook();
		GriefPreventionCore = new GriefPreventionHook();
		TownyCore = new TownyHook();
		WorldGuard = new WorldGuardHook();
		PreciousStones = new PreciousStonesHook();
		Factions = new FactionsHook();
		RedProtect = new RedProtectHook();
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Herobrine.getPluginCore(), new Runnable() {
			public void run() {
				CheckForPlugins();
			}
		}, 1 * 2L);
	}

	public boolean isPreciousStones() {
		return B_PreciousStones;
	}

	public boolean isWorldGuard() {
		return B_WorldGuard;
	}

	public boolean isResidence() {
		return B_Residence;
	}

	public boolean isGriefPrevention() {
		return B_GriefPrevention;
	}

	public boolean isTowny() {
		return B_Towny;
	}

	public boolean isFactions() {
		return B_Factions;
	}
	
	public boolean isRedProtect() {
		return B_RedProtect;
	}

	public void CheckForPlugins() {
		if (ResidenceCore.Check()) {
			B_Residence = true;
			Herobrine.log.info("[Herobrine] Residence plugin detected on server");
		}
		if (GriefPreventionCore.Check()) {
			B_GriefPrevention = true;
			Herobrine.log.info("[Herobrine] GriefPrevention plugin detected on server");
		}
		if (TownyCore.Check()) {
			B_Towny = true;
			Herobrine.log.info("[Herobrine] Towny plugin detected on server");
		}
		if (WorldGuard.Check()) {
			B_WorldGuard = true;
			Herobrine.log.info("[Herobrine] WorldGuard plugin detected on server");
		}
		if (PreciousStones.Check()) {
			B_PreciousStones = true;
			Herobrine.log.info("[Herobrine] PreciousStones plugin detected on server");
		}
		if (Factions.Check()) {
			B_Factions = true;
			Herobrine.log.info("[Herobrine] Factions plugin detected on server");
		}
	}

	public boolean isSecuredArea(Location loc) {		
		if (B_Residence) 
			return ResidenceCore.isSecuredArea(loc);		
		if (B_GriefPrevention) 
			return GriefPreventionCore.isSecuredArea(loc);
		if (B_Towny)
			return TownyCore.isSecuredArea(loc);
		if (B_WorldGuard)
			return WorldGuard.isSecuredArea(loc);
		if (B_PreciousStones) 
			return PreciousStones.isSecuredArea(loc);
		if (B_Factions) 
			return Factions.isSecuredArea(loc);
		if (B_RedProtect)
			return RedProtect.isSecuredArea(loc);
		return false;
	}

	public boolean checkBuild(final Location loc) {
		return Herobrine.getPluginCore().getConfigDB().SecuredArea_Build || !isSecuredArea(loc);
	}

	public boolean checkAttack(final Location loc) {
		return Herobrine.getPluginCore().getConfigDB().SecuredArea_Attack || !isSecuredArea(loc);
	}

	public boolean checkHaunt(final Location loc) {
		return Herobrine.getPluginCore().getConfigDB().SecuredArea_Haunt || !isSecuredArea(loc);
	}

	public boolean checkSigns(final Location loc) {
		return Herobrine.getPluginCore().getConfigDB().SecuredArea_Signs || !isSecuredArea(loc);
	}

	public boolean checkBooks(final Location loc) {
		return Herobrine.getPluginCore().getConfigDB().SecuredArea_Books || !isSecuredArea(loc);
	}

}
