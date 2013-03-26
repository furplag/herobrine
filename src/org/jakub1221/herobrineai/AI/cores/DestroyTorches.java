package org.jakub1221.herobrineai.AI.cores;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.AI.CoreResult;

public class DestroyTorches extends Core{

	public DestroyTorches(){
		super(CoreType.DESTROY_TORCHES,AppearType.NORMAL);
	}
	
	public CoreResult CallCore(Object[] data){
		return destroyTorches((Location) data[0]);
	}
	
	public CoreResult destroyTorches(Location loc){
		if (HerobrineAI.getPluginCore().getConfigDB().DestroyTorches==true){
			
			
			int x=loc.getBlockX();
			int y=loc.getBlockY();
			int z=loc.getBlockZ();
			World world=loc.getWorld();
			
			int i=-(HerobrineAI.getPluginCore().getConfigDB().DestroyTorchesRadius); // Y
			int ii=-(HerobrineAI.getPluginCore().getConfigDB().DestroyTorchesRadius); // X
			int iii=-(HerobrineAI.getPluginCore().getConfigDB().DestroyTorchesRadius); // Z
			
			for(i=-(HerobrineAI.getPluginCore().getConfigDB().DestroyTorchesRadius);i<=HerobrineAI.getPluginCore().getConfigDB().DestroyTorchesRadius;i++){
				for(ii=-(HerobrineAI.getPluginCore().getConfigDB().DestroyTorchesRadius);ii<=HerobrineAI.getPluginCore().getConfigDB().DestroyTorchesRadius;ii++){
					for(iii=-(HerobrineAI.getPluginCore().getConfigDB().DestroyTorchesRadius);iii<=HerobrineAI.getPluginCore().getConfigDB().DestroyTorchesRadius;iii++){
						if(world.getBlockAt(x+ii, y+i, z+iii).getType() == Material.TORCH){
							world.getBlockAt(x+ii, y+i, z+iii).breakNaturally();
							return new CoreResult(true,"Torches destroyed!");
						}
					}
				}	
			}
			
		}
		return new CoreResult(false,"Cannot destroy torches.");
	}

	
}
