package net.theprogrammersworld.herobrine.AI.cores;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import net.theprogrammersworld.herobrine.Herobrine;
import net.theprogrammersworld.herobrine.Utils;
import net.theprogrammersworld.herobrine.AI.AICore;
import net.theprogrammersworld.herobrine.AI.Core;
import net.theprogrammersworld.herobrine.AI.CoreResult;
import net.theprogrammersworld.herobrine.misc.BlockChanger;

public class Heads extends Core {

	private boolean isCalled = false;
	private List<Block> headList = new ArrayList<Block>();

	public Heads() {
		super(CoreType.HEADS, AppearType.NORMAL, Herobrine.getPluginCore());
	}

	public CoreResult CallCore(Object[] data) {
		if (isCalled == false) {
			if (Bukkit.getPlayer((String) data[0]).isOnline()) {
				Player player = (Player) Bukkit.getServer().getPlayer((String) data[0]);
				if (PluginCore.getSupport().checkBuild(player.getLocation())) {
					if (PluginCore.getConfigDB().UseHeads) {

						Location loc = player.getLocation();
						int px = loc.getBlockX();
						int pz = loc.getBlockZ();
						int y = 0;
						int x = -7;
						int z = -7;
						
						Random randomGen = Utils.getRandomGen();
						
						for (x = -7; x <= 7; x++) {
							for (z = -7; z <= 7; z++) {
								if (randomGen.nextInt(7) == randomGen.nextInt(7)) {

									if (!loc.getWorld().getHighestBlockAt(px + x, pz + z).getType().isSolid()) {
										y = loc.getWorld().getHighestBlockYAt(px + x, pz + z);
									} else {
										y = loc.getWorld().getHighestBlockYAt(px + x, pz + z) + 1;
									}

									Block block = loc.getWorld().getBlockAt(px + x, y, pz + z);
									BlockChanger.PlaceSkull(block.getLocation(), player.getUniqueId());

									headList.add(block);

								}
							}
						}

						isCalled = true;
						Bukkit.getScheduler().scheduleSyncDelayedTask(AICore.plugin, new Runnable() {
							public void run() {
								RemoveHeads();
							}
						}, 1 * 100L);

						return new CoreResult(true, "Herobrine spawned heads near " + player.displayName() + ".");

					} else {
						return new CoreResult(false, "Herobrine head-spawning is disabled.");
					}
				} else {
					return new CoreResult(false, player.displayName() + " cannot be haunted with heads because they are in a secure area.");
				}
			} else {
				return new CoreResult(false, "Player cannot be haunted with heads because they are offline.");
			}
		} else {
			return new CoreResult(false, "Herobrine head-spawning is on a cooldown period.");
		}
	}

	public void RemoveHeads() {
		for (Block h : headList) {
			h.setType(Material.AIR);
		}
		headList.clear();
		isCalled = false;
	}

	public ArrayList<Block> getHeadList() {
		return (ArrayList<Block>) headList;
	}
}
