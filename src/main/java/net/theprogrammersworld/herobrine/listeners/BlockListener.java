package net.theprogrammersworld.herobrine.listeners;

import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.util.Vector;

import net.theprogrammersworld.herobrine.Herobrine;
import net.theprogrammersworld.herobrine.AI.AICore;
import net.theprogrammersworld.herobrine.AI.Core.CoreType;
import net.theprogrammersworld.herobrine.AI.cores.Heads;

public class BlockListener implements Listener {

  Logger log = Logger.getLogger("Minecraft");

  @EventHandler
  public void onBlockIgnite(BlockIgniteEvent event) {
    if (event.getBlock() != null) {
      Block blockt = (Block) event.getBlock();
      Location blockloc = (Location) blockt.getLocation();

      if (Herobrine.getPluginCore().getConfigDB().UseTotem && event.getPlayer() != null) {
        blockloc.setY(blockloc.getY() - 1);
        Block block = (Block) blockloc.getWorld().getBlockAt(blockloc);
        if (block.getType() == Material.NETHERRACK) {

          Object[][] checkList = { { new Vector(0, -1, 0), Material.NETHERRACK },
              { new Vector(-1, -1, 0), Material.GOLD_BLOCK }, { new Vector(-1, -1, -1), Material.GOLD_BLOCK },
              { new Vector(-1, -1, 1), Material.GOLD_BLOCK }, { new Vector(1, -1, 0), Material.GOLD_BLOCK },
              { new Vector(1, -1, -1), Material.GOLD_BLOCK }, { new Vector(1, -1, 1), Material.GOLD_BLOCK },
              { new Vector(0, -1, -1), Material.GOLD_BLOCK }, { new Vector(0, -1, +1), Material.GOLD_BLOCK },
              { new Vector(0, 0, 1), Material.REDSTONE_TORCH }, { new Vector(0, 0, -1), Material.REDSTONE_TORCH },
              { new Vector(1, 0, 0), Material.REDSTONE_TORCH }, { new Vector(-1, 0, 0), Material.REDSTONE_TORCH } };

          boolean checkListCorrect = true;

          for (int i = 0; i < checkList.length; i++) {

            Vector v = (Vector) checkList[i][0];

            Block checkBlock = block.getWorld().getBlockAt(blockloc.getBlockX() + v.getBlockX(),
                blockloc.getBlockY() + v.getBlockY(), blockloc.getBlockZ() + v.getBlockZ());

            if (checkBlock.getType() != (Material) checkList[i][1]) {
              checkListCorrect = false;
              break;
            }
          }

          if (checkListCorrect && !AICore.isTotemCalled) {
            Herobrine.getPluginCore().getAICore().PlayerCallTotem(event.getPlayer());
          }

        }
      }
    }

    if (Herobrine.isGraveyard(event.getBlock().getWorld())) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onBlockBreak(BlockBreakEvent event) {
    if (Herobrine.isGraveyard(event.getBlock().getWorld())
        || ((Heads) Herobrine.getPluginCore().getAICore().getCore(CoreType.HEADS)).getHeadList().contains(event.getBlock())) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onBlockPlace(BlockPlaceEvent event) {
    if (Herobrine.isGraveyard(event.getBlock().getWorld())) {
      event.setCancelled(true);
    }
  }
}
