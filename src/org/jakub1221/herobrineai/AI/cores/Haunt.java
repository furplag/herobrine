package org.jakub1221.herobrineai.AI.cores;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.AICore;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.AI.CoreResult;

public class Haunt extends Core{

	private int _ticks = 0;
	private int ticksToEnd = 0;
	private int spawnedWolves = 0;
	private int spawnedBats = 0;
	private int KL_INT=0;
	private int PS_INT=0;
	private boolean isHandler=false;
	
	public Haunt(){
        super(CoreType.HAUNT,AppearType.APPEAR);
	}
	
	public CoreResult CallCore(Object[] data){
		return setHauntTarget((Player) data[0]);
	}
	
	public CoreResult setHauntTarget(Player player){
		if (HerobrineAI.getPluginCore().getSupport().checkHaunt(player.getLocation())){
	
		spawnedWolves=0;
		spawnedBats=0;
		 _ticks=0;
		 ticksToEnd=0;
		 AICore.isTarget=true;
		 AICore.PlayerTarget=player;
		 AICore.log.info("[HerobrineAI] Hauntig player!");
        Location loc = (Location) HerobrineAI.HerobrineNPC.getBukkitEntity().getLocation();
        loc.setY(-20);
        HerobrineAI.HerobrineNPC.moveTo(loc);
    
		StartHandler();
		 return new CoreResult(true,"Herobrine haunts "+player.getName()+"!");
	}
		return new CoreResult(false,"Player is in secure area!");
	}
	
	public void StartHandler(){
		isHandler=true;
		KL_INT=Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(AICore.plugin, new Runnable() {
   	        public void run() {
   	        	KeepLookingHaunt();
   			    }
   	        }, 1 * 5L,  1 * 5L);
		PS_INT=Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(AICore.plugin, new Runnable() {
   	        public void run() {
   	        	PlaySounds();
   			    }
   	        }, 1 * 35L,  1 * 35L);
	}
	
	public void StopHandler(){
		if(isHandler){
			isHandler=false;
			Bukkit.getScheduler().cancelTask(KL_INT);
			Bukkit.getScheduler().cancelTask(PS_INT);
		}
	}
	
	public void PlaySounds(){
		 if (AICore.PlayerTarget.isOnline() && AICore.isTarget && HerobrineAI.getPluginCore().getAICore().getCoreTypeNow()==CoreType.HAUNT){
		        if (AICore.PlayerTarget.isDead()==false){
		        	if (ticksToEnd==35){HerobrineAI.getPluginCore().getAICore().CancelTarget(CoreType.HAUNT);}else{
		        	ticksToEnd++;
		        	Object[] data = {AICore.PlayerTarget};
		        	HerobrineAI.getPluginCore().getAICore().getCore(CoreType.SOUNDF).RunCore(data);
		        	
		        	Location ploc = (Location) AICore.PlayerTarget.getLocation();
		        	
		        	Random randxgen = new Random();
		    		int randx= randxgen.nextInt(100);
		    		if (randx<70){}
		    		else if (randx<80 && spawnedBats<=3){
		    			if (HerobrineAI.getPluginCore().getConfigDB().SpawnBats){
		    			ploc.getWorld().spawnEntity(ploc, EntityType.BAT);
		    			spawnedBats++;
		    			}
		    		}else if (randx<90 && spawnedWolves<=2){
		    			if (HerobrineAI.getPluginCore().getConfigDB().SpawnWolves){
		    			 Wolf wolf = (Wolf) ploc.getWorld().spawnCreature(ploc, CreatureType.WOLF);
		    			 wolf.setAdult();
		    			 wolf.setAngry(true);
		    			 spawnedWolves++;
		    			}
		    		}
		    		
		        	if (HerobrineAI.getPluginCore().getConfigDB().Lighting==true){
		            	
		    			int lchance= new Random().nextInt(100);
		             
		            			if (lchance>75){
		            	Location newloc = (Location) ploc;
		            	
		    			int randz= new Random().nextInt(50);
		    			int randxp= new Random().nextInt(1);
		    			int randzp= new Random().nextInt(1);
		    				
		    			if (randxp==1){
		            	newloc.setX(newloc.getX()+randx);
		    			}else{
		    				newloc.setX(newloc.getX()-randx);	
		    			}
		    			if (randzp==1){
		    	        	newloc.setZ(newloc.getZ()+randz);
		    				}else{
		    					newloc.setZ(newloc.getZ()-randz);	
		    				}
		    			
		    			newloc.setY(250);
		            	newloc.getWorld().strikeLightning(newloc);
		            			
		            			}
		            		
		            			
		            	}	
		        	
		        	
		        	if (ticksToEnd==1){
		        	Object[] data2 = {AICore.PlayerTarget.getLocation()};
		        	HerobrineAI.getPluginCore().getAICore().getCore(CoreType.BUILD_STUFF).RunCore(data2);}
		        	
		   
		        	}
	}else{HerobrineAI.getPluginCore().getAICore().CancelTarget(CoreType.HAUNT);}
	}else{HerobrineAI.getPluginCore().getAICore().CancelTarget(CoreType.HAUNT);}
	}
	
	public void KeepLookingHaunt(){
		 if (AICore.PlayerTarget.isOnline() && AICore.isTarget && HerobrineAI.getPluginCore().getAICore().getCoreTypeNow()==CoreType.HAUNT){
	       if (AICore.PlayerTarget.isDead()==false){
	    	   
	    	   Location loc = (Location) HerobrineAI.HerobrineNPC.getBukkitEntity().getLocation();
	    	   
	    	   Player [] AllOnPlayers = Bukkit.getServer().getOnlinePlayers();
			   if (Bukkit.getServer().getOnlinePlayers().length>0){
			   int i = 0;
			   for (i=0;i<=Bukkit.getServer().getOnlinePlayers().length-1;i++){
				   if (AllOnPlayers[i].getEntityId()!=HerobrineAI.HerobrineEntityID){
				Location ploc = (Location) AllOnPlayers[i].getLocation();
			
				   if (ploc.getWorld() == loc.getWorld() && ploc.getX()+5>loc.getX() && ploc.getX()-5<loc.getX() && ploc.getZ()+5>loc.getZ() && ploc.getZ()-5<loc.getZ() && ploc.getY()+5>loc.getY() && ploc.getY()-5<loc.getY()){
					   HerobrineAI.getPluginCore().getAICore().DisappearEffect();
					 
			
				   }
			   }
			   }
			   }
	            
	       	HerobrineAI.HerobrineHP=HerobrineAI.HerobrineMaxHP;
	        loc =  AICore.PlayerTarget.getLocation();
	       	loc.setY(loc.getY()+1.5);
	       	HerobrineAI.HerobrineNPC.lookAtPoint(loc);		
	       	
	       	_ticks++;
	  
	       	if (_ticks==0){HauntTP();}else if (_ticks==20){HerobrineAI.getPluginCore().getAICore().DisappearEffect();}else if (_ticks==30){HauntTP();}else if (_ticks==50){HerobrineAI.getPluginCore().getAICore().DisappearEffect();}else if (_ticks==60){HauntTP();}
	       	else if (_ticks==80){HerobrineAI.getPluginCore().getAICore().DisappearEffect();}else if (_ticks==90){HauntTP();}else if (_ticks==115){HerobrineAI.getPluginCore().getAICore().DisappearEffect();}else if (_ticks==120){HauntTP();}else if (_ticks==140){HerobrineAI.getPluginCore().getAICore().DisappearEffect();}
	       	else if (_ticks==145){HauntTP();}else if (_ticks==170){HerobrineAI.getPluginCore().getAICore().DisappearEffect();}else if (_ticks==175){HauntTP();}else if (_ticks==190){HerobrineAI.getPluginCore().getAICore().DisappearEffect();}
	       	
	       	
	       	}else{HerobrineAI.getPluginCore().getAICore().CancelTarget(CoreType.HAUNT);}
			}else{HerobrineAI.getPluginCore().getAICore().CancelTarget(CoreType.HAUNT);}
		}
	
	public void HauntTP(){
		 if (AICore.PlayerTarget.isOnline() && AICore.isTarget && HerobrineAI.getPluginCore().getAICore().getCoreTypeNow()==CoreType.HAUNT){
		        if (AICore.PlayerTarget.isDead()==false){
		if (HerobrineAI.getPluginCore().getConfigDB().useWorlds.contains(AICore.PlayerTarget.getWorld().getName())){
			
			FindPlace(AICore.PlayerTarget);
		        	Location ploc = (Location) AICore.PlayerTarget.getLocation();
		        	ploc.setY(ploc.getY()+1.5);
		        	HerobrineAI.HerobrineNPC.lookAtPoint(ploc);
		        	
		
		        	
		}else{HerobrineAI.getPluginCore().getAICore().CancelTarget(CoreType.HAUNT);}
		        }else{HerobrineAI.getPluginCore().getAICore().CancelTarget(CoreType.HAUNT);}
		 }else{HerobrineAI.getPluginCore().getAICore().CancelTarget(CoreType.HAUNT);}
		        
	}
	
public boolean FindPlace(Player player){
		
		Location loc = (Location) player.getLocation();
		
			int x=0;
			int z=0;
			int y=0;
			
			int xMax=new Random().nextInt(10)-7;
			int zMax=new Random().nextInt(10)-7;
			
		
				
			for(y=-5;y<=5;y++){
				for(x=xMax;x<=10;x++){
					for(z=zMax;z<=10;z++){
						
						if (HerobrineAI.StandBlocks.contains(loc.getWorld().getBlockAt(x+loc.getBlockX(), y+loc.getBlockY()-1, z+loc.getBlockZ()).getType())){
							if (HerobrineAI.AllowedBlocks.contains(loc.getWorld().getBlockAt(x+loc.getBlockX(), y+loc.getBlockY(), z+loc.getBlockZ()).getType())){
								if (HerobrineAI.AllowedBlocks.contains(loc.getWorld().getBlockAt(x+loc.getBlockX(), y+loc.getBlockY()+1, z+loc.getBlockZ()).getType())){
									Teleport(loc.getWorld(),x+loc.getBlockX(),y+loc.getBlockY(),z+loc.getBlockZ());
								}	
							}
						}
						
					}
					
				}
				
			}
				
			

			
			
			return false;
		
	}
	
	public void Teleport(World world,int X,int Y,int Z){
		
		Location loc = (Location) HerobrineAI.HerobrineNPC.getBukkitEntity().getLocation();
		loc.setWorld(world);
		loc.setX((double) X);
		loc.setY((double) Y);
		loc.setZ((double) Z);
		HerobrineAI.HerobrineNPC.moveTo(loc);
		
	}
	
}

	

