package org.jakub1221.herobrineai.AI.cores;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.Utils;
import org.jakub1221.herobrineai.AI.AICore;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.AI.CoreResult;

public class Pyramid extends Core {

	public Pyramid() {
		super(CoreType.PYRAMID, AppearType.NORMAL, HerobrineAI.getPluginCore());
	}

	public CoreResult CallCore(Object[] data) {
		if (data[0] instanceof Player) {
			return FindPlace((Player) data[0]);
		} else {
			return FindPlace((Chunk) data[0]);
		}
	}

	// TODO Change this nonsense
	public CoreResult FindPlace(Chunk chunk) {
		if (PluginCore.getConfigDB().BuildPyramids) {

			Location loc = chunk.getBlock(2, 0, 2).getLocation();
			loc = loc.getWorld().getHighestBlockAt(loc).getLocation();

			boolean canBuild = true;
			int i1 = 0;
			int i2 = 5;
			int i3 = 5;
			int i4 = 0;
			int i5 = 0;
			int i6 = 0;

			for (i1 = -5; i1 <= 5; i1++) {// Y

				canBuild = true;

				for (i4 = -1; i4 <= 3; i4++) {// Y
					for (i5 = -2; i5 <= 2; i5++) {// X
						for (i6 = -2; i6 <= 2; i6++) {// Z

							if (loc.getBlockX() == i2 + i5 + loc.getBlockX()
									&& loc.getBlockY() == i1 + i4 + loc.getBlockY()
									&& loc.getBlockZ() == i3 + i6 + loc.getBlockZ()) {
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
					BuildPyramid(loc.getWorld(), i2 + loc.getBlockX(), i1 + loc.getBlockY(), i3 + loc.getBlockZ());
					return new CoreResult(true, "Creating a pyramid!");

				}

			}
		}
		return new CoreResult(false, "Cannot create a pyramid!");

	}

	public CoreResult FindPlace(Player player) {
		if (PluginCore.getConfigDB().BuildPyramids) {

			Location loc = (Location) player.getLocation();

			boolean canBuild = true;
			int i1 = 0;
			int i2 = 0;
			int i3 = 0;
			int i4 = 0;
			int i5 = 0;
			int i6 = 0;

			int xMax = Utils.getRandomGen().nextInt(15) - 10;
			int zMax = Utils.getRandomGen().nextInt(15) - 10;

			for (i1 = -5; i1 <= 5; i1++) {// Y
				for (i2 = xMax; i2 <= 15; i2++) {// X
					for (i3 = zMax; i3 <= 15; i3++) {// Z
						canBuild = true;

						for (i4 = -1; i4 <= 3; i4++) {// Y
							for (i5 = -2; i5 <= 2; i5++) {// X
								for (i6 = -2; i6 <= 2; i6++) {// Z

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
							BuildPyramid(
										loc.getWorld(), 
										i2 + loc.getBlockX(), 
										i1 + loc.getBlockY(), 
										i3 + loc.getBlockZ()
										);
							
							return new CoreResult(true, "Creating a pyramid!");
						}
					}

				}

			}

		}
		return new CoreResult(false, "Cannot create a pyramid!");

	}

	public void BuildPyramid(World world, int X, int Y, int Z) {

		if (HerobrineAI.getPluginCore().getSupport().checkBuild(new Location(world, X, Y, Z))) {

			AICore.log.info("Creating pyramid at " + X + "," + Y + "," + Z);

			Material mainMat = (Material) Material.SANDSTONE;

			
			// TODO CHANGE THIS
			// Level 1
			world.getBlockAt(X, Y, Z).setType(mainMat);
			world.getBlockAt(X - 2, Y, Z).setType(mainMat);
			world.getBlockAt(X - 1, Y, Z).setType(mainMat);
			world.getBlockAt(X + 1, Y, Z).setType(mainMat);
			world.getBlockAt(X + 2, Y, Z).setType(mainMat);
			world.getBlockAt(X - 2, Y, Z - 1).setType(mainMat);
			world.getBlockAt(X - 2, Y, Z + 1).setType(mainMat);
			world.getBlockAt(X - 1, Y, Z - 1).setType(mainMat);
			world.getBlockAt(X - 1, Y, Z + 1).setType(mainMat);
			world.getBlockAt(X, Y, Z - 1).setType(mainMat);
			world.getBlockAt(X, Y, Z + 1).setType(mainMat);
			world.getBlockAt(X, Y, Z - 2).setType(mainMat);
			world.getBlockAt(X, Y, Z + 2).setType(mainMat);
			world.getBlockAt(X - 1, Y, Z - 2).setType(mainMat);
			world.getBlockAt(X - 1, Y, Z + 2).setType(mainMat);
			world.getBlockAt(X + 1, Y, Z - 2).setType(mainMat);
			world.getBlockAt(X + 1, Y, Z + 2).setType(mainMat);
			world.getBlockAt(X + 1, Y, Z - 1).setType(mainMat);
			world.getBlockAt(X + 1, Y, Z + 1).setType(mainMat);
			world.getBlockAt(X + 2, Y, Z - 1).setType(mainMat);
			world.getBlockAt(X + 2, Y, Z + 1).setType(mainMat);
			world.getBlockAt(X + 1, Y, Z - 2).setType(mainMat);
			world.getBlockAt(X + 1, Y, Z + 2).setType(mainMat);
			// Level 2
			world.getBlockAt(X, Y + 1, Z).setType(mainMat);
			world.getBlockAt(X - 1, Y + 1, Z).setType(mainMat);
			world.getBlockAt(X + 1, Y + 1, Z).setType(mainMat);
			world.getBlockAt(X - 1, Y + 1, Z - 1).setType(mainMat);
			world.getBlockAt(X + 1, Y + 1, Z - 1).setType(mainMat);
			world.getBlockAt(X - 1, Y + 1, Z + 1).setType(mainMat);
			world.getBlockAt(X + 1, Y + 1, Z + 1).setType(mainMat);
			world.getBlockAt(X, Y + 1, Z + 1).setType(mainMat);
			world.getBlockAt(X, Y + 1, Z + 1).setType(mainMat);
			world.getBlockAt(X, Y + 1, Z - 1).setType(mainMat);
			world.getBlockAt(X, Y + 1, Z - 1).setType(mainMat);
			// Level 3
			world.getBlockAt(X, Y + 2, Z).setType(mainMat);
			// Level 4
			world.getBlockAt(X, Y + 3, Z).setType(Material.REDSTONE_TORCH_ON);

		}

	}

}
