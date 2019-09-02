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

public class BuryPlayer extends Core {

	public Block savedBlock1 = null;
	public Block savedBlock2 = null;

	public BuryPlayer() {
		super(CoreType.BURY_PLAYER, AppearType.NORMAL, HerobrineAI.getPluginCore());
	}

	public CoreResult CallCore(Object[] data) {
		return FindPlace((Player) data[0]);
	}

	public CoreResult FindPlace(Player player){
        if(HerobrineAI.getPluginCore().getSupport().checkBuild(player.getLocation())){	
			Location loc = (Location) player.getLocation();
			
			int[][] blocks = {
								{-1, 0},
								{-2, 0},
								{-1,-1},
								{-2,-1},
								{-3, 0},
								{-3,-1},
								{-1,-1},
								{-2,-1},
								{-1,-2},
								{ 0,-2},
								
							};
			
			for (int i = 0; i < blocks.length; i++){
				
				Material mat = loc.getWorld().getBlockAt(
						  	  							loc.getBlockX(), 
														loc.getBlockY() + blocks[i][0], 
														loc.getBlockZ() + blocks[i][1]
														).getType();
				
				if(!mat.isSolid())
					return new CoreResult(false,"Cannot find suitable location!");
					
			}
			
		  
			Bury(loc.getWorld(),loc.getBlockX(),loc.getBlockY(),loc.getBlockZ(),player);
			return new CoreResult(true,"Player buried!");
        }
        
        return new CoreResult(false,"Cannot find suitable location!");
	}

	public void Bury(World world, int X, int Y, int Z, Player player) {

		Location loc = new Location(world, X, Y, Z);

		loc.getWorld().getBlockAt(X, Y - 1, Z).breakNaturally();
		loc.getWorld().getBlockAt(X, Y - 2, Z).breakNaturally();
		loc.getWorld().getBlockAt(X, Y - 3, Z).breakNaturally();
		loc.getWorld().getBlockAt(X, Y - 1, Z - 1).breakNaturally();
		loc.getWorld().getBlockAt(X, Y - 2, Z - 1).breakNaturally();
		loc.getWorld().getBlockAt(X, Y - 3, Z - 1).breakNaturally();
		player.teleport(new Location(world, X, Y - 3, Z));
		RegenBlocks(world, X, Y, Z, player.getName());

	}

	public void RegenBlocks(World world, int X, int Y, int Z, String playername) {
		Location loc = new Location(world, X, Y, Z);
		Location signloc = new Location(world, X, Y, Z - 2);
		Block signblock = signloc.add(0, 0D, 0).getBlock();
		signblock.setType(Material.OAK_SIGN);
		Sign sign = (Sign) signblock.getState();
		sign.setLine(1, playername);
		sign.update();
		loc.getWorld().getBlockAt(X, Y - 1, Z).setType(Material.STONE_BRICKS, false);
		loc.getWorld().getBlockAt(X, Y - 1, Z - 1).setType(Material.STONE_BRICKS, false);
	}

}
