package org.jakub1221.herobrineai.AI.cores;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.Utils;
import org.jakub1221.herobrineai.AI.ConsoleLogger;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.AI.CoreResult;
import org.jakub1221.herobrineai.misc.BlockChanger;

public class Signs extends Core {

	public Signs() {
		super(CoreType.SIGNS, AppearType.NORMAL, HerobrineAI.getPluginCore());
	}

	public CoreResult CallCore(Object[] data) {
		return placeSign((Location) data[0], (Location) data[1]);
	}

	static ConsoleLogger log = new ConsoleLogger();

	public CoreResult placeSign(Location loc, Location ploc) {
		boolean status = false;
		log.info("Generating sign location...");

		if (loc.getWorld().getBlockAt(loc.getBlockX() + 2, loc.getBlockY(), loc.getBlockZ()).getType() == Material.AIR
				&& loc.getWorld().getBlockAt(loc.getBlockX() + 2, loc.getBlockY() - 1, loc.getBlockZ())
						.getType() != Material.AIR) {
			loc.setX(loc.getBlockX() + 2);
			createSign(loc, ploc);
			status = true;
		} else if (loc.getWorld().getBlockAt(loc.getBlockX() - 2, loc.getBlockY(), loc.getBlockZ())
				.getType() == Material.AIR
				&& loc.getWorld().getBlockAt(loc.getBlockX() - 2, loc.getBlockY() - 1, loc.getBlockZ())
						.getType() != Material.AIR) {
			loc.setX(loc.getBlockX() - 2);
			createSign(loc, ploc);
			status = true;
		} else if (loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ() + 2)
				.getType() == Material.AIR
				&& loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY() - 1, loc.getBlockZ() + 2)
						.getType() != Material.AIR) {
			loc.setZ(loc.getBlockZ() + 2);
			createSign(loc, ploc);
			status = true;
		} else if (loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ() - 2)
				.getType() == Material.AIR
				&& loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY() - 1, loc.getBlockZ() - 2)
						.getType() != Material.AIR) {
			loc.setZ(loc.getBlockZ() - 2);
			createSign(loc, ploc);
			status = true;
		}

		if (status) {
			return new CoreResult(true, "Sign placed!");
		} else {
			return new CoreResult(false, "Cannot place a sign!");
		}
	}

	public void createSign(Location loc, Location ploc) {

		Random randcgen = Utils.getRandomGen();
		int chance = randcgen.nextInt(100);
		if (chance > (100 - HerobrineAI.getPluginCore().getConfigDB().SignChance)) {
			Random randgen = Utils.getRandomGen();
			int count = HerobrineAI.getPluginCore().getConfigDB().useSignMessages.size();
			int randmsg = randgen.nextInt(count);

			Block signblock = loc.add(0, 0D, 0).getBlock();
			Block undersignblock = signblock.getLocation().subtract(0D, 1D, 0D).getBlock();
			if (HerobrineAI.NonStandBlocks.contains(signblock.getType())
					&& !HerobrineAI.NonStandBlocks.contains(undersignblock.getType())) {
				signblock.setType(Material.SIGN_POST);
				Sign sign = (Sign) signblock.getState();
				sign.setLine(1, HerobrineAI.getPluginCore().getConfigDB().useSignMessages.get(randmsg));

				sign.setRawData((byte) BlockChanger.getPlayerBlockFace(ploc).ordinal());
				sign.update();
			}
		}
	}
}
