package org.jakub1221.herobrineai.AI.cores;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.AICore;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.AI.CoreResult;
import org.jakub1221.herobrineai.AI.Message;
import org.jakub1221.herobrineai.AI.extensions.Position;

public class Attack extends Core{
	
	private int ticksToEnd = 0;
	private int HandlerINT=0;
	private boolean isHandler=false;
	
	public Attack(){
		super(CoreType.ATTACK,AppearType.APPEAR);
	}
	
	public CoreResult CallCore(Object[] data){	
		return setAttackTarget((Player)data[0]);
	}

	public CoreResult setAttackTarget(Player player){
		if (!HerobrineAI.getPluginCore().getAICore().checkAncientSword(player.getInventory())){
		if (HerobrineAI.getPluginCore().getSupport().checkAttack(player.getLocation())){
		
		HerobrineAI.HerobrineHP=HerobrineAI.HerobrineMaxHP;
		 ticksToEnd=0;
		 AICore.PlayerTarget=player;
		 AICore.isTarget=true;
		 AICore.log.info("[HerobrineAI] Teleporting to target. ("+ AICore.PlayerTarget.getName()+")");
		 Location ploc = (Location)  AICore.PlayerTarget.getLocation();
		 Object[] data = {ploc};
		 HerobrineAI.getPluginCore().getAICore().getCore(CoreType.DESTROY_TORCHES).RunCore(data);
		 if (HerobrineAI.getPluginCore().getConfigDB().UsePotionEffects){
			 AICore.PlayerTarget.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 1000, 1));
			 AICore.PlayerTarget.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1000, 1));
			 AICore.PlayerTarget.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1000, 1));
		 }
	     Location tploc = (Location) Position.getTeleportPosition(ploc);
		 
		HerobrineAI.HerobrineNPC.moveTo(tploc);
		
		Message.SendMessage(AICore.PlayerTarget);
		
		StartHandler();
       
		return new CoreResult(true,"Herobrine attacks "+player.getName()+"!");
	}else{
		return new CoreResult(false,"Player is in secure area.");
	}
	}else{
		return new CoreResult(false,"Player has Ancient Sword.");
	}
	}
	
	public void StopHandler(){
		if (isHandler){
		Bukkit.getScheduler().cancelTask(HandlerINT);
	isHandler=false;
		}
	}
	
	public void StartHandler(){
		KeepLooking();
		FollowHideRepeat();
		isHandler=true;
     	HandlerINT=Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(AICore.plugin, new Runnable() {
	        public void run() {
	        	Handler();
			    }
	        }, 1 * 5L,1 * 5L);
	}
	
	private void Handler(){
		KeepLooking();
		if (ticksToEnd==1 || ticksToEnd==16 || ticksToEnd==32 || ticksToEnd==48 || ticksToEnd==64 || ticksToEnd==80 || ticksToEnd==96 || ticksToEnd==112 || ticksToEnd==128 || ticksToEnd==144){
			FollowHideRepeat();
		}
	}
	
	public void KeepLooking(){
        if (AICore.PlayerTarget.isOnline() && AICore.isTarget && HerobrineAI.getPluginCore().getAICore().getCoreTypeNow()==CoreType.ATTACK){
        if (AICore.PlayerTarget.isDead()==false){
        	if (ticksToEnd==160){HerobrineAI.getPluginCore().getAICore().CancelTarget(CoreType.ATTACK);}else{
        	ticksToEnd++;
        	
        	Location ploc = (Location) AICore.PlayerTarget.getLocation();
        	ploc.setY(ploc.getY()+1.5);
        	HerobrineAI.HerobrineNPC.lookAtPoint(ploc);
        	if (HerobrineAI.getPluginCore().getConfigDB().Lighting==true){
			int lchance= new Random().nextInt(100);
         
        			if (lchance>75){
        	Location newloc = (Location) ploc;
			int randx= new Random().nextInt(50);
			int randz= new Random().nextInt(50);
			if (new Random().nextBoolean()){
        	newloc.setX(newloc.getX()+randx);
			}else{
				newloc.setX(newloc.getX()-randx);	
			}
			if (new Random().nextBoolean()){
	        	newloc.setZ(newloc.getZ()+randz);
				}else{
					newloc.setZ(newloc.getZ()-randz);	
				}
			newloc.setY(250);
        	newloc.getWorld().strikeLightning(newloc);
        			
        			}
        		
        			
        	}		
        	
   
        	}
        }else{HerobrineAI.getPluginCore().getAICore().CancelTarget(CoreType.ATTACK);}
		}else{HerobrineAI.getPluginCore().getAICore().CancelTarget(CoreType.ATTACK);}
	}
	
	public void Follow(){
		 if (AICore.PlayerTarget.isOnline() && AICore.isTarget && HerobrineAI.getPluginCore().getAICore().getCoreTypeNow()==CoreType.ATTACK){
		        if (AICore.PlayerTarget.isDead()==false){
		if (HerobrineAI.getPluginCore().getConfigDB().useWorlds.contains(AICore.PlayerTarget.getWorld().getName()) && HerobrineAI.getPluginCore().getSupport().checkAttack(AICore.PlayerTarget.getLocation())){
		        	HerobrineAI.HerobrineNPC.moveTo(Position.getTeleportPosition(AICore.PlayerTarget.getLocation()));
		        	Location ploc = (Location) AICore.PlayerTarget.getLocation();
		        	ploc.setY(ploc.getY()+1.5);
		        	HerobrineAI.HerobrineNPC.lookAtPoint(ploc);
		        	AICore.PlayerTarget.playSound(AICore.PlayerTarget.getLocation(),Sound.BREATH,(float) 0.75,(float) 0.75);
		        	if (HerobrineAI.getPluginCore().getConfigDB().HitPlayer==true){
		    			int hitchance= new Random().nextInt(100);
		    			if (hitchance<55){
		    				AICore.PlayerTarget.playSound(AICore.PlayerTarget.getLocation(), Sound.HURT,(float) 0.75,(float) 0.75);
		    				if (AICore.PlayerTarget.getHealth()>=4){
		    					AICore.PlayerTarget.setHealth(AICore.PlayerTarget.getHealth()-4);
		    				}else{AICore.PlayerTarget.setHealth(0);}
		
		        	
		    			}
		        	}
		}else{HerobrineAI.getPluginCore().getAICore().CancelTarget(CoreType.ATTACK);}
		        }else{HerobrineAI.getPluginCore().getAICore().CancelTarget(CoreType.ATTACK);}
			}else{HerobrineAI.getPluginCore().getAICore().CancelTarget(CoreType.ATTACK);}
		     
	}
	
	public void Hide(){
		if (AICore.PlayerTarget.isOnline() && AICore.isTarget && HerobrineAI.getPluginCore().getAICore().getCoreTypeNow()==CoreType.ATTACK){
		        if (AICore.PlayerTarget.isDead()==false){
		        	
		        	Location ploc = (Location) AICore.PlayerTarget.getLocation();
		        
		        	ploc.setY(-20);
		        	
		        	Location hbloc1 = (Location) HerobrineAI.HerobrineNPC.getBukkitEntity().getLocation();
		        	hbloc1.setY(hbloc1.getY()+1);
		        	Location hbloc2 = (Location) HerobrineAI.HerobrineNPC.getBukkitEntity().getLocation();
		        	hbloc2.setY(hbloc2.getY()+0);
		        	Location hbloc3 = (Location) HerobrineAI.HerobrineNPC.getBukkitEntity().getLocation();
		        	hbloc3.setY(hbloc3.getY()+0.5);
		        	Location hbloc4 = (Location) HerobrineAI.HerobrineNPC.getBukkitEntity().getLocation();
		        	hbloc4.setY(hbloc4.getY()+1.5);
		        
		        	
		        	ploc.getWorld().playEffect(hbloc1, Effect.SMOKE, 80);
		        	ploc.getWorld().playEffect(hbloc2, Effect.SMOKE, 80);
		        	ploc.getWorld().playEffect(hbloc3, Effect.SMOKE, 80);
		        	ploc.getWorld().playEffect(hbloc4, Effect.SMOKE, 80);
		        	ploc.getWorld().playEffect(hbloc1, Effect.SMOKE, 80);
		        	ploc.getWorld().playEffect(hbloc2, Effect.SMOKE, 80);
		        	ploc.getWorld().playEffect(hbloc3, Effect.SMOKE, 80);
		        	ploc.getWorld().playEffect(hbloc4, Effect.SMOKE, 80);
		        	ploc.getWorld().playEffect(hbloc1, Effect.SMOKE, 80);
		        	ploc.getWorld().playEffect(hbloc2, Effect.SMOKE, 80);
		        	ploc.getWorld().playEffect(hbloc3, Effect.SMOKE, 80);
		        	ploc.getWorld().playEffect(hbloc4, Effect.SMOKE, 80);
		        	ploc.getWorld().playEffect(hbloc1, Effect.SMOKE, 80);
		        	ploc.getWorld().playEffect(hbloc2, Effect.SMOKE, 80);
		        	ploc.getWorld().playEffect(hbloc3, Effect.SMOKE, 80);
		        	ploc.getWorld().playEffect(hbloc4, Effect.SMOKE, 80);
		        	ploc.getWorld().playEffect(hbloc1, Effect.SMOKE, 80);
		        	ploc.getWorld().playEffect(hbloc2, Effect.SMOKE, 80);
		        	ploc.getWorld().playEffect(hbloc3, Effect.SMOKE, 80);
		        	ploc.getWorld().playEffect(hbloc4, Effect.SMOKE, 80);
		        	ploc.getWorld().playEffect(hbloc1, Effect.SMOKE, 80);
		        	ploc.getWorld().playEffect(hbloc2, Effect.SMOKE, 80);
		        	ploc.getWorld().playEffect(hbloc3, Effect.SMOKE, 80);
		        	ploc.getWorld().playEffect(hbloc4, Effect.SMOKE, 80);
		        	ploc.getWorld().playEffect(hbloc1, Effect.SMOKE, 80);
		        	ploc.getWorld().playEffect(hbloc2, Effect.SMOKE, 80);
		        	ploc.getWorld().playEffect(hbloc3, Effect.SMOKE, 80);
		        	ploc.getWorld().playEffect(hbloc4, Effect.SMOKE, 80);
		        	
		        	if (HerobrineAI.getPluginCore().getConfigDB().SpawnBats){
		        		int cc = new Random().nextInt(3);
		        		if (cc==0){
		        	ploc.getWorld().spawnEntity(hbloc1, EntityType.BAT);
		        	ploc.getWorld().spawnEntity(hbloc1, EntityType.BAT);
		        		}else if (cc==1){
				        	ploc.getWorld().spawnEntity(hbloc1, EntityType.BAT);
				        		}
		       
		        	}
		        	
		        	HerobrineAI.HerobrineNPC.moveTo(ploc);
		        	
		        }else{HerobrineAI.getPluginCore().getAICore().CancelTarget(CoreType.ATTACK);}
			}else{HerobrineAI.getPluginCore().getAICore().CancelTarget(CoreType.ATTACK);}
		        
	}
	
	public void FollowHideRepeat(){
		if (AICore.PlayerTarget.isOnline() && AICore.isTarget && HerobrineAI.getPluginCore().getAICore().getCoreTypeNow()==CoreType.ATTACK){
		        if (AICore.PlayerTarget.isDead()==false){
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(AICore.plugin, new Runnable() {
	        public void run() {
	           	Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(AICore.plugin, new Runnable() {
	    	        public void run() {
	    	        	Hide();
	    			    }
	    	        }, 1 * 30L);
	        	Follow();
			    }
	        }, 1 * 45L);
		        }else{HerobrineAI.getPluginCore().getAICore().CancelTarget(CoreType.ATTACK);}
		 			}else{HerobrineAI.getPluginCore().getAICore().CancelTarget(CoreType.ATTACK);}
	}
}
