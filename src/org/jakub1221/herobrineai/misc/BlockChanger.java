package org.jakub1221.herobrineai.misc;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;

public class BlockChanger {

	 public static BlockFace getPlayerBlockFace(Location loc)
	    {
	 
	        BlockFace dir = null;
	        float y = loc.getYaw();
	        if( y < 0 ){y += 360;}
	        y %= 360;
	        int i = (int)((y+8) / 22.5);
	        switch(i){
	        case 0: dir = BlockFace.WEST; break;
	        case 1: dir = BlockFace.WEST_NORTH_WEST; break;
	        case 2: dir = BlockFace.NORTH_WEST; break;
	        case 3: dir = BlockFace.NORTH_NORTH_WEST; break;
	        case 4: dir = BlockFace.NORTH; break;
	        case 5: dir = BlockFace.NORTH_NORTH_EAST; break;
	        case 6: dir = BlockFace.NORTH_EAST; break;
	        case 7: dir = BlockFace.EAST_NORTH_EAST; break;
	        case 8: dir = BlockFace.EAST; break;
	        case 9: dir = BlockFace.EAST_SOUTH_EAST; break;
	        case 10: dir = BlockFace.SOUTH_EAST; break;
	        case 11: dir = BlockFace.SOUTH_SOUTH_EAST; break;
	        case 12: dir = BlockFace.SOUTH; break;
	        case 13: dir = BlockFace.SOUTH_SOUTH_WEST; break;
	        case 14: dir = BlockFace.SOUTH_WEST; break;
	        case 15: dir = BlockFace.WEST_SOUTH_WEST; break;
	        default: dir = BlockFace.WEST; break;
	        }
	     
	        return dir;
	 
	    }
	 
	 public static void PlaceSkull(Location loc, String name){
	        int chance = new Random().nextInt(7);
			Block b = loc.getBlock();
			b.setType(Material.SKULL);
			
			    Skull skull = (Skull)b.getState();
			    skull.setSkullType(SkullType.PLAYER);
			    skull.setOwner(name);

			    BlockFace bface = BlockFace.EAST;
							
			    if (chance==0){bface=BlockFace.WEST;}
			    else if (chance==1){bface=BlockFace.EAST;}
			    else if (chance==2){bface=BlockFace.SOUTH;}
			    else if (chance==3){bface=BlockFace.NORTH;}
			    
				skull.setRawData((byte) bface.ordinal());
     		    skull.update(true);
	
			
		}
}
