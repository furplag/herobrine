package net.theprogrammersworld.herobrine.listeners;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.util.Vector;

import net.theprogrammersworld.herobrine.HerobrineAI;
import net.theprogrammersworld.herobrine.AI.*;
import net.theprogrammersworld.herobrine.AI.Core.CoreType;
import net.theprogrammersworld.herobrine.AI.cores.Heads;

public class BlockListener implements Listener {

	Logger log = Logger.getLogger("Minecraft");

	@EventHandler
	public void onBlockIgnite(BlockIgniteEvent event) {
		if (event.getBlock() != null) {
			Block blockt = (Block) event.getBlock();
			Location blockloc = (Location) blockt.getLocation();

			if (event.getPlayer() != null) {
				blockloc.setY(blockloc.getY() - 1);
				Block block = (Block) blockloc.getWorld().getBlockAt(blockloc);
				if (block.getType() == Material.NETHERRACK) {

					Object[][] checkList = { 
							{ new Vector(0, -1, 0), Material.NETHERRACK },
							{ new Vector(-1, -1, 0), Material.GOLD_BLOCK },
							{ new Vector(-1, -1, -1), Material.GOLD_BLOCK },
							{ new Vector(-1, -1, 1), Material.GOLD_BLOCK },
							{ new Vector(1, -1, 0), Material.GOLD_BLOCK },
							{ new Vector(1, -1, -1), Material.GOLD_BLOCK },
							{ new Vector(1, -1, 1), Material.GOLD_BLOCK },
							{ new Vector(0, -1, -1), Material.GOLD_BLOCK },
							{ new Vector(0, -1, +1), Material.GOLD_BLOCK },
							{ new Vector(0, 0, 1), Material.REDSTONE_TORCH },
							{ new Vector(0, 0, -1), Material.REDSTONE_TORCH },
							{ new Vector(1, 0, 0), Material.REDSTONE_TORCH },
							{ new Vector(-1, 0, 0), Material.REDSTONE_TORCH } 
							};

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

					if (checkListCorrect && HerobrineAI.getPluginCore().getConfigDB().UseTotem && !AICore.isTotemCalled) {
						HerobrineAI.getPluginCore().getAICore().PlayerCallTotem(event.getPlayer());
					}

				}
			}
		}

		if (event.getBlock().getWorld() == Bukkit.getServer().getWorld("world_herobrineai_graveyard")) {
			event.setCancelled(true);
			return;
		}

	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.getBlock().getWorld() == Bukkit.getServer().getWorld("world_herobrineai_graveyard")) {
			event.setCancelled(true);
			return;
		} else {
			Heads h = (Heads) HerobrineAI.getPluginCore().getAICore().getCore(CoreType.HEADS);
			ArrayList<Block> list = h.getHeadList();
			if (list.contains(event.getBlock())) {
				event.setCancelled(true);
				return;
			}
		}

	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if (event.getBlock().getWorld() == Bukkit.getServer().getWorld("world_herobrineai_graveyard")) {
			event.setCancelled(true);
			return;
		}

	}
}
