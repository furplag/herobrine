package net.theprogrammersworld.herobrine.AI.extensions;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import net.theprogrammersworld.herobrine.Herobrine;
import net.theprogrammersworld.herobrine.misc.StructureLoader;

public class GraveyardWorld {

	public static void Create() {

		Location loc = new Location(Bukkit.getServer().getWorld("world_herobrine_graveyard"), 0, 3, 0);

		for (int x = -50; x <= 50; x++) {
			for (int z = -50; z <= 50; z++) {

				loc.getWorld().getBlockAt(x, 3, z).setType(Material.MYCELIUM);
			}

		}

		int MainX = -10;
		int MainY = 3;
		int MainZ = -10;

		StructureLoader structLoader = new StructureLoader(Herobrine.getPluginCore().getInputStreamData("/res/graveyard_world.yml"));
		structLoader.Build(loc.getWorld(), MainX, MainY, MainZ);

	}

}
