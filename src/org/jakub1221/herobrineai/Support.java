package org.jakub1221.herobrineai;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jakub1221.herobrineai.support._CustomItems;
import org.jakub1221.herobrineai.support._GriefPrevention;
import org.jakub1221.herobrineai.support._PreciousStones;
import org.jakub1221.herobrineai.support._Residence;
import org.jakub1221.herobrineai.support._Towny;
import org.jakub1221.herobrineai.support._WorldGuard;

public class Support {
	
	private boolean B_Residence=false;
	private boolean B_GriefPrevention=false;
	private boolean B_Towny=false;
	private boolean B_CustomItems=false;
	private boolean B_WorldGuard=false;
	private boolean B_PreciousStones=false;
	private _Residence ResidenceCore=null; 
	private _GriefPrevention GriefPreventionCore=null;
	private _Towny TownyCore=null;
	private _CustomItems CustomItems=null;
	private _WorldGuard WorldGuard=null;
	private _PreciousStones PreciousStones=null;
	
	public Support(){
		ResidenceCore=new _Residence();
		GriefPreventionCore=new _GriefPrevention();
		TownyCore=new _Towny();
		CustomItems=new _CustomItems();
		WorldGuard=new _WorldGuard();
		PreciousStones=new _PreciousStones();
		   Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(HerobrineAI.getPluginCore(), new Runnable() {
		        public void run() {
		        	CheckForPlugins();
		        }
	        }, 1 * 10L);
	}
	
	public boolean isPreciousStones(){
		return B_PreciousStones;
	}
	public boolean isWorldGuard(){
		return B_WorldGuard;
	}
	public boolean isResidence(){
		return B_Residence;
	}
	public boolean isGriefPrevention(){
		return B_GriefPrevention;
	}
	public boolean isTowny(){
		return B_Towny;
	}
	public void CheckForPlugins(){
		if (ResidenceCore.Check()){
			B_Residence=true;
			HerobrineAI.log.info("[HerobrineAI] Residence plugin detected!");
		}
		if (GriefPreventionCore.Check()){
			B_GriefPrevention=true;
			HerobrineAI.log.info("[HerobrineAI] GriefPrevention plugin detected!");
		}
		if (TownyCore.Check()){
			B_Towny=true;
			HerobrineAI.log.info("[HerobrineAI] Towny plugin detected!");
		}
		if (CustomItems.Check()){
			B_CustomItems=true;
			HerobrineAI.log.info("[HerobrineAI] CustomItems plugin detected!");
			CustomItems.init();
		}
		if (WorldGuard.Check()){
			B_WorldGuard=true;
			HerobrineAI.log.info("[HerobrineAI] WorldGuard plugin detected!");
		}
		if (PreciousStones.Check()){
			B_PreciousStones=true;
			HerobrineAI.log.info("[HerobrineAI] PreciousStones plugin detected!");
		}
	}
	public boolean isSecuredArea(Location loc){
		if (B_Residence){
			if (ResidenceCore.isSecuredArea(loc)){
				return true;
			}else{return false;}
		}else if (B_GriefPrevention){
			if (GriefPreventionCore.isSecuredArea(loc)){
				return true;
			}else{return false;}
		}else if (B_Towny){
			if (TownyCore.isSecuredArea(loc)){
				return true;
			}else{return false;}
		}else if (B_WorldGuard){
			if (WorldGuard.isSecuredArea(loc)){
				return true;
			}else{return false;}
		}else if (B_PreciousStones){
			if (PreciousStones.isSecuredArea(loc)){
				return true;
			}else{return false;}
		}
		return false;
	}
	public boolean checkBuild(Location loc){
		if (HerobrineAI.getPluginCore().getConfigDB().SecuredArea_Build){
			return true;
		}else{
			if (isSecuredArea(loc)){
				return false;
			}else{return true;}
		}
	}
	
	public boolean checkAttack(Location loc){
		if (HerobrineAI.getPluginCore().getConfigDB().SecuredArea_Attack){
			return true;
		}else{
			if (isSecuredArea(loc)){
				return false;
			}else{return true;}
		}
	}
	
	public boolean checkHaunt(Location loc){
		if (HerobrineAI.getPluginCore().getConfigDB().SecuredArea_Haunt){
			return true;
		}else{
			if (isSecuredArea(loc)){
				return false;
			}else{return true;}
		}
	}
	
	public boolean checkSigns(Location loc){
		if (HerobrineAI.getPluginCore().getConfigDB().SecuredArea_Signs){
			return true;
		}else{
			if (isSecuredArea(loc)){
				return false;
			}else{return true;}
		}
	}
	
	public boolean checkBooks(Location loc){
		if (HerobrineAI.getPluginCore().getConfigDB().SecuredArea_Books){
			return true;
		}else{
			if (isSecuredArea(loc)){
				return false;
			}else{return true;}
		}
	}
	
    public boolean isCustomItems(){
    return B_CustomItems;
    }
    
    public _CustomItems getCustomItems(){
    	if (B_CustomItems){
    		return CustomItems;
    	}
    	return null;
    }

}
