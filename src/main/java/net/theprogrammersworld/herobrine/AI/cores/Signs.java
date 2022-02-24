package net.theprogrammersworld.herobrine.AI.cores;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;

import net.kyori.adventure.text.Component;
import net.theprogrammersworld.herobrine.Herobrine;
import net.theprogrammersworld.herobrine.Utils;
import net.theprogrammersworld.herobrine.AI.ConsoleLogger;
import net.theprogrammersworld.herobrine.AI.Core;
import net.theprogrammersworld.herobrine.AI.CoreResult;
import net.theprogrammersworld.herobrine.misc.BlockChanger;

public class Signs extends Core {

	public Signs() {
		super(CoreType.SIGNS, AppearType.NORMAL, Herobrine.getPluginCore());
	}

	public CoreResult CallCore(Object[] data) {
		return placeSign((Location) data[0], (Location) data[1]);
	}

	static ConsoleLogger log = new ConsoleLogger();

	public CoreResult placeSign(Location loc, Location ploc) {
		boolean status = false;
		log.info("Generating sign...");

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
			return new CoreResult(true, "Sign generated successfully.");
		} else {
			return new CoreResult(false, "Sign generation failed.");
		}
	}

	public void createSign(Location loc, Location ploc) {

		Random randcgen = Utils.getRandomGen();
		int chance = randcgen.nextInt(100);
		if (chance > (100 - Herobrine.getPluginCore().getConfigDB().SignChance)) {
			Random randgen = Utils.getRandomGen();
			int count = Herobrine.getPluginCore().getConfigDB().useSignMessages.size();
			int randmsg = randgen.nextInt(count);

			Block signblock = loc.add(0, 0D, 0).getBlock();
			Block undersignblock = signblock.getLocation().subtract(0D, 1D, 0D).getBlock();
			if (!signblock.getType().isSolid() && undersignblock.getType().isSolid()) {
				
				signblock.setType(Material.OAK_SIGN);
				Sign sign = (Sign) signblock.getState();

				BlockData blockData = sign.getBlockData();
				((Directional) blockData).setFacing(BlockChanger.getPlayerBlockFace(ploc));
				sign.setBlockData(blockData);
				
				sign.line(1, Component.text(Herobrine.getPluginCore().getConfigDB().useSignMessages.get(randmsg)));
				sign.update();
			}
		}
	}
}
