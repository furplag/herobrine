package org.jakub1221.herobrineai.AI.cores;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.AICore;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.AI.CoreResult;
import org.jakub1221.herobrineai.AI.Message;

public class Totem extends Core{
	
	public Totem(){
	super(CoreType.TOTEM,AppearType.APPEAR);
	}

	public CoreResult CallCore(Object[] data){
		return TotemCall((Location)data[0],(String)data[1]);
	}
	
	public CoreResult TotemCall(Location loc,String caller){
		
		AICore.isTotemCalled=false;
		loc.getWorld().strikeLightning(loc);
		if (HerobrineAI.getPluginCore().getConfigDB().TotemExplodes==true){
		loc.getWorld().createExplosion(loc, 5);
		}
		if (Bukkit.getServer().getPlayer(caller) != null){
			if (Bukkit.getServer().getPlayer(caller).isOnline()){
			     HerobrineAI.getPluginCore().getAICore().setCoreTypeNow(CoreType.TOTEM);
				HerobrineAI.getPluginCore().getAICore().setAttackTarget(Bukkit.getServer().getPlayer(caller));
				Player player = (Player) Bukkit.getServer().getPlayer(caller);
				
				Player [] AllOnPlayers = Bukkit.getServer().getOnlinePlayers();
				   if (Bukkit.getServer().getOnlinePlayers().length>0){
				   int i = 0;
				   for (i=0;i<=Bukkit.getServer().getOnlinePlayers().length-1;i++){
					Location ploc = (Location) AllOnPlayers[i].getLocation();
					   if (AllOnPlayers[i].getName() != player.getName() && ploc.getX()+10>loc.getX() && ploc.getX()-10<loc.getX() && ploc.getZ()+10>loc.getZ() && ploc.getZ()-10<loc.getZ()){
					
					Message.SendMessage(AllOnPlayers[i]);
					 if (HerobrineAI.getPluginCore().getConfigDB().UsePotionEffects){
					AllOnPlayers[i].addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 1000, 1));
					AllOnPlayers[i].addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1000, 1));
					AllOnPlayers[i].addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1000, 1));
					 }
					   }
				   }
				
				   }
			}else{
				boolean hasTarget=false;
				Player target = null;
				
				Player [] AllOnPlayers = Bukkit.getServer().getOnlinePlayers();
				   if (Bukkit.getServer().getOnlinePlayers().length>0){
				   int i = 0;
				   for (i=0;i<=Bukkit.getServer().getOnlinePlayers().length-1;i++){
					   if (hasTarget==false){
					Location ploc = (Location) AllOnPlayers[i].getLocation();
					   if (ploc.getX()+10>loc.getX() && ploc.getX()-10<loc.getX() && ploc.getZ()+10>loc.getZ() && ploc.getZ()-10<loc.getZ()){
					
					hasTarget=true;
					target=AllOnPlayers[i];
						   
					   }
					   }
				   }
				
				   }
				   if (hasTarget==true){
					   
					   HerobrineAI.getPluginCore().getAICore().CancelTarget(CoreType.TOTEM);
					   HerobrineAI.getPluginCore().getAICore().setAttackTarget(target);
					   Player player = (Player) target;
					   if (Bukkit.getServer().getOnlinePlayers().length>0){
						   int i = 0;
						   for (i=0;i<=Bukkit.getServer().getOnlinePlayers().length-1;i++){
							Location ploc = (Location) AllOnPlayers[i].getLocation();
							   if (AllOnPlayers[i].getName() != player.getName() && ploc.getX()+20>loc.getX() && ploc.getX()-20<loc.getX() && ploc.getZ()+20>loc.getZ() && ploc.getZ()-20<loc.getZ()){
							
							Message.SendMessage(AllOnPlayers[i]);
							AllOnPlayers[i].addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 1000, 1));
							AllOnPlayers[i].addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1000, 1));
							AllOnPlayers[i].addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1000, 1));
							   
							   }
						   }
						
						   }
					 
					   
				   }
				
				
			}
		
		
		}else{
			boolean hasTarget=false;
			Player target = null;
			
			Player [] AllOnPlayers = Bukkit.getServer().getOnlinePlayers();
			   if (Bukkit.getServer().getOnlinePlayers().length>0){
			   int i = 0;
			   for (i=0;i<=Bukkit.getServer().getOnlinePlayers().length-1;i++){
				   if (hasTarget==false){
				Location ploc = (Location) AllOnPlayers[i].getLocation();
				   if (ploc.getX()+20>loc.getX() && ploc.getX()-20<loc.getX() && ploc.getZ()+20>loc.getZ() && ploc.getZ()-20<loc.getZ()){
				
				hasTarget=true;
				target=AllOnPlayers[i];
					   
				   }
				   }
			   }
			
			   }
			   if (hasTarget==true){
				   
				   HerobrineAI.getPluginCore().getAICore().CancelTarget(CoreType.TOTEM);
				   HerobrineAI.getPluginCore().getAICore().setAttackTarget(target);
				   Player player = (Player) target;
				   if (Bukkit.getServer().getOnlinePlayers().length>0){
					   int i = 0;
					   for (i=0;i<=Bukkit.getServer().getOnlinePlayers().length-1;i++){
						   if (AllOnPlayers[i].getEntityId()!=HerobrineAI.HerobrineEntityID){
						Location ploc = (Location) AllOnPlayers[i].getLocation();
						   if (AllOnPlayers[i].getName() != player.getName() && ploc.getX()+20>loc.getX() && ploc.getX()-20<loc.getX() && ploc.getZ()+20>loc.getZ() && ploc.getZ()-20<loc.getZ()){
						
						Message.SendMessage(AllOnPlayers[i]);
						AllOnPlayers[i].addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 1000, 1));
						AllOnPlayers[i].addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1000, 1));
						AllOnPlayers[i].addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1000, 1));
						   }
					   }
					   }
					   }
				   
			   }
			
			
		}
		return new CoreResult(false,"Totem called!");
	}
}
