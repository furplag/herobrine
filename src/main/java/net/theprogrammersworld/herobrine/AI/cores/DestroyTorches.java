package net.theprogrammersworld.herobrine.AI.cores;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import net.theprogrammersworld.herobrine.Herobrine;
import net.theprogrammersworld.herobrine.AI.Core;
import net.theprogrammersworld.herobrine.AI.CoreResult;

public class DestroyTorches extends Core {

	public DestroyTorches() {
		super(CoreType.DESTROY_TORCHES, AppearType.NORMAL, Herobrine.getPluginCore());
	}

	public CoreResult CallCore(Object[] data) {
		return destroyTorches((Location) data[0]);
	}

	public CoreResult destroyTorches(Location loc) {
		if (Herobrine.getPluginCore().getConfigDB().DestroyTorches == true) {

			int x = loc.getBlockX();
			int y = loc.getBlockY();
			int z = loc.getBlockZ();
			World world = loc.getWorld();

			int i = -(Herobrine.getPluginCore().getConfigDB().DestroyTorchesRadius); // Y
			int ii = -(Herobrine.getPluginCore().getConfigDB().DestroyTorchesRadius); // X
			int iii = -(Herobrine.getPluginCore().getConfigDB().DestroyTorchesRadius); // Z

			for (i = -(Herobrine.getPluginCore().getConfigDB().DestroyTorchesRadius); i <= Herobrine.getPluginCore()
					.getConfigDB().DestroyTorchesRadius; i++) {
				for (ii = -(Herobrine.getPluginCore().getConfigDB().DestroyTorchesRadius); ii <= Herobrine
						.getPluginCore().getConfigDB().DestroyTorchesRadius; ii++) {
					for (iii = -(Herobrine.getPluginCore().getConfigDB().DestroyTorchesRadius); iii <= Herobrine
							.getPluginCore().getConfigDB().DestroyTorchesRadius; iii++) {
						if (world.getBlockAt(x + ii, y + i, z + iii).getType() == Material.TORCH) {
							world.getBlockAt(x + ii, y + i, z + iii).breakNaturally();
							return new CoreResult(true, "Torches successfully destroyed by Herobrine.");
						}
					}
				}
			}

		}
		return new CoreResult(false, "Herobrine could not destroy the torches.");
	}

}
