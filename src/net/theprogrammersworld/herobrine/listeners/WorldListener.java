package net.theprogrammersworld.herobrine.listeners;

import java.util.Random;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

import net.theprogrammersworld.herobrine.Herobrine;
import net.theprogrammersworld.herobrine.AI.Core.CoreType;

public class WorldListener implements Listener{

	@EventHandler
	public void onChunkLoad(ChunkLoadEvent event){
		if (event.isNewChunk()){

			World world = event.getWorld();
			
			if (Herobrine.getPluginCore().getConfigDB().useWorlds.contains(world.getName())){

			 if (Herobrine.getPluginCore().getConfigDB().BuildTemples==true){
				 int templeChance = Herobrine.getPluginCore().getConfigDB().BuildTempleOnChunkPercentage;
				 if (new Random().nextInt(100) + 1 <= templeChance){
					 Object[] data = {event.getChunk()};
					 Herobrine.getPluginCore().getAICore().getCore(CoreType.TEMPLE).RunCore(data);
				 }
			 }
			 
			 if (Herobrine.getPluginCore().getConfigDB().BuildPyramids==true){
				 int pyramidChance = Herobrine.getPluginCore().getConfigDB().BuildPyramidOnChunkPercentage;
				 if (new Random().nextInt(100) + 1 <= pyramidChance){
					 Object[] data = {event.getChunk()};
				     Herobrine.getPluginCore().getAICore().getCore(CoreType.PYRAMID).RunCore(data);
				 }
			 }
		}
	}
}
	
}
