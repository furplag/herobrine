package org.jakub1221.herobrineai.AI.cores;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.AI.CoreResult;

public class BuryPlayer extends Core{

	public Block savedBlock1=null;
	public Block savedBlock2=null;
	
	public BuryPlayer(){
		super(CoreType.BURY_PLAYER,AppearType.NORMAL);
	}
	
	public CoreResult CallCore(Object[] data){
		return FindPlace((Player)data[0]);
	}
	
	public CoreResult FindPlace(Player player){
        if(HerobrineAI.getPluginCore().getSupport().checkBuild(player.getLocation())){
	
		
		Location loc = (Location) player.getLocation();
		
	  if (HerobrineAI.StandBlocks.contains(loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY()-1, loc.getBlockZ()).getType())){
		  if (HerobrineAI.StandBlocks.contains(loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY()-2, loc.getBlockZ()).getType())){
			  if (HerobrineAI.StandBlocks.contains(loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY()-1, loc.getBlockZ()-1).getType())){
				  if (HerobrineAI.StandBlocks.contains(loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY()-2, loc.getBlockZ()-1).getType())){
					  if (HerobrineAI.StandBlocks.contains(loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY()-3, loc.getBlockZ()).getType())){
						  if (HerobrineAI.StandBlocks.contains(loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY()-3, loc.getBlockZ()-1).getType())){
							  if (HerobrineAI.StandBlocks.contains(loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY()-1, loc.getBlockZ()-1).getType())){
								  if (HerobrineAI.StandBlocks.contains(loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY()-2, loc.getBlockZ()-1).getType())){
									  if (HerobrineAI.StandBlocks.contains(loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY()-1, loc.getBlockZ()-2).getType())){
									  if (HerobrineAI.NonStandBlocks.contains(loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()-2).getType())){
										  Bury(loc.getWorld(),loc.getBlockX(),loc.getBlockY(),loc.getBlockZ(),player);
										 return new CoreResult(true,"Player buried!");
									  }   
								  } 
							  } 
						  } 
					  }
					  }
				  } 
			  } 
		  } 
	  }
        }
		
        return new CoreResult(false,"Cannot find a good location!");
	}
	
	public void Bury(World world,int X,int Y,int Z,Player player){
		
		Location loc = new Location(world,X,Y,Z);
		
		loc.getWorld().getBlockAt(X, Y-1, Z).breakNaturally();
		loc.getWorld().getBlockAt(X, Y-2, Z).breakNaturally();
		loc.getWorld().getBlockAt(X, Y-3, Z).breakNaturally();
		loc.getWorld().getBlockAt(X, Y-1, Z-1).breakNaturally();
		loc.getWorld().getBlockAt(X, Y-2, Z-1).breakNaturally();
		loc.getWorld().getBlockAt(X, Y-3, Z-1).breakNaturally();
		player.teleport(new Location(world,X,Y-3,Z));
	RegenBlocks(world,X,Y,Z,player.getName());
		
	}
	
	public void RegenBlocks(World world,int X,int Y,int Z,String playername){
		Location loc = new Location(world,X,Y,Z);
		Location signloc = new Location(world,X,Y,Z-2);
		Block signblock = signloc.add(0, 0D ,0).getBlock();
		signblock.setType(Material.SIGN_POST);
		Sign sign = (Sign) signblock.getState();
		sign.setLine(1, playername);
		sign.update();
		loc.getWorld().getBlockAt(X, Y-1, Z).setTypeIdAndData(98,(byte)2,false);
		loc.getWorld().getBlockAt(X, Y-1, Z-1).setTypeIdAndData(98,(byte)2,false);
	}
	
}
