package net.theprogrammersworld.herobrine.AI.cores;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import net.theprogrammersworld.herobrine.AI.Core;
import net.theprogrammersworld.herobrine.AI.CoreResult;

public class DestroyTorches extends Core {

  public DestroyTorches() {
    super(Core.Type.DESTROY_TORCHES, AppearType.NORMAL);
  }

  public CoreResult CallCore(Object[] data) {
    return destroyTorches((Location) data[0]);
  }

  public CoreResult destroyTorches(Location loc) {
    if (plugin.getConfigDB().DestroyTorches == true) {

      int x = loc.getBlockX();
      int y = loc.getBlockY();
      int z = loc.getBlockZ();
      World world = loc.getWorld();

      int i = -(plugin.getConfigDB().DestroyTorchesRadius); // Y
      int ii = -(plugin.getConfigDB().DestroyTorchesRadius); // X
      int iii = -(plugin.getConfigDB().DestroyTorchesRadius); // Z

      for (i = -(plugin.getConfigDB().DestroyTorchesRadius); i <= plugin
          .getConfigDB().DestroyTorchesRadius; i++) {
        for (ii = -(plugin.getConfigDB().DestroyTorchesRadius); ii <= plugin.getConfigDB().DestroyTorchesRadius; ii++) {
          for (iii = -(plugin.getConfigDB().DestroyTorchesRadius); iii <= plugin.getConfigDB().DestroyTorchesRadius; iii++) {
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
