package org.jakub1221.herobrineai.AI.cores;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jakub1221.herobrineai.ConfigDB;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.AICore;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.AI.CoreResult;
import org.jakub1221.herobrineai.NPC.AI.Path;

public class RandomPosition extends Core{

	private int randomTicks=0;
	private int randomMoveTicks=0;
	private boolean RandomMoveIsPlayer=false;
	
	public RandomPosition(){
      super(CoreType.RANDOM_POSITION,AppearType.APPEAR);
	}
	
	public int getRandomTicks(){return this.randomTicks;}
	public int getRandomMoveTicks(){return this.randomMoveTicks;}
	public void setRandomTicks(int i){this.randomTicks=i;};
	public void setRandomMoveTicks(int i){this.randomMoveTicks=i;};
	
	public CoreResult CallCore(Object[] data){
		return setRandomPosition((World)data[0]);
	}
	
	public CoreResult setRandomPosition(World world){
		if (HerobrineAI.getPluginCore().getConfigDB().UseWalkingMode){
		if (randomTicks!=3){
			randomTicks++;
		if (HerobrineAI.getPluginCore().getAICore().getCoreTypeNow() != CoreType.RANDOM_POSITION && AICore.isTarget==false){
		Location newloc = (Location) getRandomLocation(world);
		if (newloc!=null){
	
		HerobrineAI.HerobrineNPC.moveTo(newloc);
		newloc.setX(newloc.getX()+2);
		newloc.setY(newloc.getY()+1.5);
		HerobrineAI.HerobrineNPC.lookAtPoint(newloc);
		randomTicks=0;
		AICore.log.info("[HerobrineAI] Herobrine is now in RandomLocation mode.");
		HerobrineAI.getPluginCore().getAICore().Start_RM();
		HerobrineAI.getPluginCore().getAICore().Start_RS();
		HerobrineAI.getPluginCore().getAICore().Start_CG();
		RandomMoveIsPlayer=false;
        return new CoreResult(true,"Herobrine is now in WalkingMode.");
		}else{AICore.log.info("[HerobrineAI] RandomPosition Failed!");return setRandomPosition(world);}
		}
		}else{
			return new CoreResult(false,"WalkingMode - Find location failed!");
		}
		}else{
			return new CoreResult(false,"WalkingMode is disabled!");
		}
		return new CoreResult(false,"WalkingMode failed!");
	}
	
	public Location getRandomLocation(World world){
		
		int i = 0;
		for (i=0;i<=300;i++){
			
			int r_nxtX=HerobrineAI.getPluginCore().getConfigDB().WalkingModeXRadius;
			int nxtX=r_nxtX;
			if (nxtX<0){
				nxtX=-nxtX;
			}
			int r_nxtZ=HerobrineAI.getPluginCore().getConfigDB().WalkingModeZRadius;
			int nxtZ=r_nxtZ;
			if (nxtZ<0){
				nxtZ=-nxtZ;
			}
			int randx= new Random().nextInt(nxtX);
			
			int randy=0;
		
			int randz= new Random().nextInt(nxtZ);
			
			int randxp= new Random().nextInt(1);
		
			int randzp= new Random().nextInt(1);
			
			if (randxp==0 && randx!=0){randx=-(randx);}
			if (randzp==0 && randz!=0){randz=-(randz);}
			
			randx=randx+HerobrineAI.getPluginCore().getConfigDB().WalkingModeFromXRadius;
			randz=randz+HerobrineAI.getPluginCore().getConfigDB().WalkingModeFromZRadius;
			
			if (world != null){
			randy=world.getHighestBlockYAt(randx,randz);
			}else{
				return null;
			}
		
			if (world.getBlockAt(randx, randy, randz).getType() == Material.AIR && world.getBlockAt(randx, randy+1, randz).getType() == Material.AIR){
				if (world.getBlockAt(randx, randy-1, randz).getType() != Material.AIR &&
						world.getBlockAt(randx, randy-1, randz).getType() != Material.WATER && 
						world.getBlockAt(randx, randy-1, randz).getType() != Material.LAVA &&
						world.getBlockAt(randx, randy-1, randz).getType() != Material.GRASS &&
						world.getBlockAt(randx, randy-1, randz).getType() != Material.SNOW &&
						world.getBlockAt(randx, randy-1, randz).getType() != Material.LEAVES &&
						world.getBlockAt(randx, randy-1, randz).getType() != Material.WHEAT &&
						world.getBlockAt(randx, randy-1, randz).getType() != Material.TORCH &&
						world.getBlockAt(randx, randy-1, randz).getType() != Material.REDSTONE_TORCH_OFF &&
						world.getBlockAt(randx, randy-1, randz).getType() != Material.REDSTONE_TORCH_ON &&
						world.getBlockAt(randx, randy-1, randz).getType() != Material.REDSTONE &&
						world.getBlockAt(randx, randy-1, randz).getType() != Material.STATIONARY_WATER &&
						world.getBlockAt(randx, randy-1, randz).getType() != Material.STATIONARY_LAVA){

			AICore.log.info("[HerobrineAI] RandomLocation "+world.getBlockAt(randx, randy-1, randz).getType().toString()+" is X:"+randx+" Y:"+randy+" Z:"+randz);
			return new Location(world,(float) randx + 0.5,(float) randy,(float) randz);	
			
			
			}
			}
		}
		
		return null;
		
	}
	
	public void RandomMove(){
		if (HerobrineAI.getPluginCore().getAICore().getCoreTypeNow()==CoreType.RANDOM_POSITION  && AICore.isTarget==false &&  RandomMoveIsPlayer==false){
			HerobrineAI.HerobrineHP=HerobrineAI.HerobrineMaxHP;
			
	if (new Random().nextInt(4)==1){
				Location loc = HerobrineAI.HerobrineNPC.getBukkitEntity().getLocation();
				Path path = new Path((float)loc.getX()+new Random().nextInt(12)-6,(float)loc.getZ()+new Random().nextInt(12)-6);
				HerobrineAI.getPluginCore().getPathManager().setPath(path);
	}
				
		}
		
	}
	
	public void CheckGravity(){
		
		if (HerobrineAI.getPluginCore().getAICore().getCoreTypeNow()==CoreType.RANDOM_POSITION  && AICore.isTarget==false){
			
			Location hbloc = (Location) HerobrineAI.HerobrineNPC.getBukkitEntity().getLocation();
			World w = (World) hbloc.getWorld();
			ConfigDB config = HerobrineAI.getPluginCore().getConfigDB();
			if (hbloc.getBlockX()<config.WalkingModeXRadius+config.WalkingModeFromXRadius && hbloc.getBlockX()>(-config.WalkingModeXRadius)+config.WalkingModeFromXRadius && hbloc.getBlockZ()<config.WalkingModeZRadius+config.WalkingModeFromZRadius && hbloc.getBlockZ()>(-config.WalkingModeZRadius)+config.WalkingModeFromZRadius){
			if (HerobrineAI.NonStandBlocks.contains(w.getBlockAt(hbloc.getBlockX(), hbloc.getBlockY()-1, hbloc.getBlockZ()).getType())){
				
				hbloc.setY(hbloc.getY()-1);
				
				HerobrineAI.HerobrineNPC.moveTo(hbloc);
				
			}
			}else{	
				HerobrineAI.getPluginCore().getAICore().CancelTarget(CoreType.RANDOM_POSITION);
			}
		}
	}
	
	public void CheckPlayerPosition(){
	boolean isThere=false;
    Location loc = (Location) HerobrineAI.HerobrineNPC.getBukkitEntity().getLocation();
		Player [] AllOnPlayers = Bukkit.getServer().getOnlinePlayers();
		   if (Bukkit.getServer().getOnlinePlayers().length>0){
		   int i = 0;
		   for (i=0;i<=Bukkit.getServer().getOnlinePlayers().length-1;i++){
			   if (HerobrineAI.HerobrineEntityID!=AllOnPlayers[i].getEntityId()){
			Location ploc = (Location) AllOnPlayers[i].getLocation();
			   if (ploc.getWorld() == loc.getWorld() && ploc.getX()+7>loc.getX() && ploc.getX()-7<loc.getX() && ploc.getZ()+7>loc.getZ() && ploc.getZ()-7<loc.getZ() && ploc.getY()+7>loc.getY() && ploc.getY()-7<loc.getY()){
			loc.setY(-20);
			HerobrineAI.HerobrineNPC.moveTo(loc);
			HerobrineAI.getPluginCore().getAICore().CancelTarget(CoreType.RANDOM_POSITION);
            RandomMoveIsPlayer=false;
            HerobrineAI.getPluginCore().getAICore().setAttackTarget(AllOnPlayers[i]);
			   }else{
				   if (ploc.getWorld() == loc.getWorld() && ploc.getX()+15>loc.getX() && ploc.getX()-15<loc.getX() && ploc.getZ()+15>loc.getZ() && ploc.getZ()-15<loc.getZ() && ploc.getY()+15>loc.getY() && ploc.getY()-15<loc.getY()){
					  ploc.setY(ploc.getY()+1.5);
					   HerobrineAI.HerobrineNPC.lookAtPoint(ploc);
					   HerobrineAI.getPluginCore().getPathManager().setPath(null);
					   isThere=true;
				   }
				   }
		   }
		   }
		   }
		   
		   if (isThere){
			  RandomMoveIsPlayer=true;
		   }else{
			  RandomMoveIsPlayer=false;	   
		   }
		
	}
	
}
