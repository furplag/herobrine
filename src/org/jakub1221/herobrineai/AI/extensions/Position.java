package org.jakub1221.herobrineai.AI.extensions;

import org.bukkit.Location;
import org.bukkit.Material;

public class Position {

	public static Location getTeleportPosition(Location ploc) {

		for(int y = -2; y < 2; y++)
		{		
			for(int x = -2; x < 2; x++)
			{
				for(int z = -2; z < 2; z++)
				{
					Material bottomBlock = ploc.clone().add(new Location(ploc.getWorld(), x, y - 1 ,z)).getBlock().getType();
					Material middleBlock = ploc.clone().add(new Location(ploc.getWorld(), x, y ,z)).getBlock().getType();
					Material topBlock = ploc.clone().add(new Location(ploc.getWorld(), x, y + 1 ,z)).getBlock().getType();
					
					if (bottomBlock.isSolid() && !middleBlock.isSolid() && !topBlock.isSolid())
						return ploc.clone().add(new Location(ploc.getWorld(), x, y - 1 ,z));
				}
			}
		}
		
		return ploc;

	}

}
