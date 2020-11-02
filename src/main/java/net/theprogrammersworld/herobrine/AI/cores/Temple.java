package net.theprogrammersworld.herobrine.AI.cores;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.theprogrammersworld.herobrine.Herobrine;
import net.theprogrammersworld.herobrine.Utils;
import net.theprogrammersworld.herobrine.AI.Core;
import net.theprogrammersworld.herobrine.AI.CoreResult;
import net.theprogrammersworld.herobrine.entity.MobType;
import net.theprogrammersworld.herobrine.misc.ItemName;
import net.theprogrammersworld.herobrine.misc.StructureLoader;

public class Temple extends Core {

	public Temple() {
		super(CoreType.TEMPLE, AppearType.NORMAL, Herobrine.getPluginCore());
	}

	public CoreResult CallCore(Object[] data) {
		if (data[0] instanceof Player) {
			return FindPlacePlayer((Player) data[0]);
		} else {
			return FindPlacePlayer((Chunk) data[0]);
		}
	}
	
	// TODO Change this nonsense
	public CoreResult FindPlacePlayer(Player player) {

		Location loc = player.getLocation();

		boolean canBuild = true;
		int i1 = 0;
		int i2 = 0;
		int i3 = 0;
		int i4 = 0;
		int i5 = 0;
		int i6 = 0;

		for (i1 = -5; i1 <= 5; i1++) {// Y
			for (i2 = -20; i2 <= 20; i2++) {// X
				for (i3 = -20; i3 <= 20; i3++) {// Z
					canBuild = true;

					for (i4 = -1; i4 <= 12; i4++) {// Y
						for (i5 = 0; i5 <= 11; i5++) {// X
							for (i6 = 0; i6 <= 10; i6++) {// Z

								if (player.getLocation().getBlockX() == i2 + i5 + loc.getBlockX()
										&& player.getLocation().getBlockY() == i1 + i4 + loc.getBlockY()
										&& player.getLocation().getBlockZ() == i3 + i6 + loc.getBlockZ()) {
									canBuild = false;
								}
								if (i4 == -1) {
									if (canBuild == true) {
										if (loc.getWorld().getBlockAt(i2 + i5 + loc.getBlockX(),i1 + i4 + loc.getBlockY(), i3 + i6 + loc.getBlockZ()).getType().isSolid()) {
											canBuild = true;
										} else {
											canBuild = false;
										}
									}
								} else {
									if (canBuild == true) {
										if (!loc.getWorld().getBlockAt(i2 + i5 + loc.getBlockX(), i1 + i4 + loc.getBlockY(), i3 + i6 + loc.getBlockZ()).getType().isSolid()) {
											canBuild = true;
										} else {
											canBuild = false;
										}
									}

								}

							}

						}
					}
					if (canBuild == true) {
						Create(loc.getWorld(), i2 + loc.getBlockX(), i1 + loc.getBlockY(), i3 + loc.getBlockZ());
						return new CoreResult(true, "Creating a temple near " + player.getDisplayName() + ".");
					}
				}

			}

		}

		return new CoreResult(false, "Cannot find a good place to create a temple.");

	}

	public CoreResult FindPlacePlayer(Chunk chunk) {

		Location loc = chunk.getBlock(2, 0, 2).getLocation();
		loc = loc.getWorld().getHighestBlockAt(loc).getLocation();

		boolean canBuild = true;
		int i1 = 0;
		int i2 = 0;
		int i3 = 0;
		int i4 = 0;
		int i5 = 0;
		int i6 = 0;

		i1 = 0;
		i2 = 0;
		i3 = 0;
		i4 = 0;
		i5 = 0;
		i6 = 0;

		for (i1 = -5; i1 <= 5; i1++) {// Y

			canBuild = true;

			for (i4 = -1; i4 <= 12; i4++) {// Y
				for (i5 = 0; i5 <= 11; i5++) {// X
					for (i6 = 0; i6 <= 10; i6++) {// Z

						if (loc.getBlockX() == i2 + i5 + loc.getBlockX() && loc.getBlockY() == i1 + i4 + loc.getBlockY()
								&& loc.getBlockZ() == i3 + i6 + loc.getBlockZ()) {
							canBuild = false;
						}
						if (i4 == -1) {
							if (canBuild == true) {
								if (loc.getWorld().getBlockAt(i2 + i5 + loc.getBlockX(), i1 + i4 + loc.getBlockY(), i3 + i6 + loc.getBlockZ()).getType().isSolid()) {
									canBuild = true;
								} else {
									canBuild = false;
								}
							}
						} else {
							if (canBuild == true) {
								if (!loc.getWorld().getBlockAt(i2 + i5 + loc.getBlockX(),i1 + i4 + loc.getBlockY(), i3 + i6 + loc.getBlockZ()).getType().isSolid()) {
									canBuild = true;
								} else {
									canBuild = false;
								}
							}

						}

					}

				}
			}
			if (canBuild == true) {
				Create(loc.getWorld(), i2 + loc.getBlockX(), i1 + loc.getBlockY(), i3 + loc.getBlockZ());
				return new CoreResult(true, "Creating temple.");
			}

		}

		return new CoreResult(false, "Cannot find a good place to create a temple.");

	}

	public void Create(World world, int X, int Y, int Z) {

		Location loc = new Location(world, X, Y, Z);

		if (Herobrine.getPluginCore().getSupport().checkBuild(new Location(world, X, Y, Z))) {

			int MainX = loc.getBlockX();
			int MainY = loc.getBlockY();
			int MainZ = loc.getBlockZ();

			// Main blocks

			new StructureLoader(Herobrine.getPluginCore().getInputStreamData("/res/temple.yml")).Build(loc.getWorld(),
					MainX, MainY, MainZ);
			loc.getWorld().getBlockAt(MainX + 6, MainY + 0, MainZ + 2).setType(Material.CHEST);
			// Mob spawn
			if (!Herobrine.isNPCDisabled) {
				if (Herobrine.getPluginCore().getConfigDB().UseNPC_Guardian) {
					Location mobloc = new Location(loc.getWorld(), MainX + 6, MainY + 0, MainZ + 4);
					for (int i = 1; i <= Herobrine.getPluginCore().getConfigDB().npc.getInt("npc.Guardian.SpawnCount"); i++) {
						Herobrine.getPluginCore().getEntityManager().spawnCustomZombie(mobloc, MobType.ARTIFACT_GUARDIAN);
					}
				}
			}
			// Chest			
			Random generator = Utils.getRandomGen();
			int chance = generator.nextInt(15);
			ItemStack item = null;
			ArrayList<String> newLore = new ArrayList<String>();
			
			if (chance < 4 && Herobrine.getPluginCore().getConfigDB().UseArtifactBow) {
				
				item = new ItemStack(Material.BOW);
				newLore.add("Herobrine artifact");
				newLore.add("Bow of Teleporting");
				item = ItemName.setNameAndLore(item, "Bow of Teleporting", newLore);
				item.addEnchantment(Enchantment.ARROW_FIRE, 1);
				item.addEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
				
			} else if (chance < 8 && Herobrine.getPluginCore().getConfigDB().UseArtifactSword) {
				
				item = new ItemStack(Material.DIAMOND_SWORD);
				newLore.add("Herobrine artifact");
				newLore.add("Sword of Lightning");
				item = ItemName.setNameAndLore(item, "Sword of Lightning", newLore);
				item.addEnchantment(Enchantment.KNOCKBACK, 2);
				item.addEnchantment(Enchantment.DAMAGE_ALL, 2);
				item.addEnchantment(Enchantment.DURABILITY, 3);
				
			} else if (chance < 12 && Herobrine.getPluginCore().getConfigDB().UseArtifactApple) {
				
				item = new ItemStack(Material.GOLDEN_APPLE);
				newLore.add("Herobrine artifact");
				newLore.add("Apple of Death");
				item = ItemName.setNameAndLore(item, "Apple of Death", newLore);

			} else {
				if (Herobrine.getPluginCore().getConfigDB().UseAncientSword) {
					item = Herobrine.getPluginCore().getAICore().createAncientSword();
					item.addEnchantment(Enchantment.KNOCKBACK, 2);
					item.addEnchantment(Enchantment.DAMAGE_ALL, 2);
				}
			}

			Chest chest = (Chest) loc.getWorld().getBlockAt(MainX + 6, MainY + 0, MainZ + 2).getState();
			chest.getBlockInventory().setItem(chest.getInventory().firstEmpty(), item);
		}
	}

}
