package net.theprogrammersworld.herobrine.AI.cores;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import net.kyori.adventure.text.Component;
import net.theprogrammersworld.herobrine.Herobrine;
import net.theprogrammersworld.herobrine.AI.Core;
import net.theprogrammersworld.herobrine.AI.CoreResult;

public class BuryPlayer extends Core {

	public Block savedBlock1 = null;
	public Block savedBlock2 = null;

	public BuryPlayer() {
		super(CoreType.BURY_PLAYER, AppearType.NORMAL, Herobrine.getPluginCore());
	}

	public CoreResult CallCore(Object[] data) {
		return FindPlace((Player) data[0]);
	}
	
	public CoreResult FindPlace(final Player player) {
		if (Herobrine.getPluginCore().getSupport().checkBuild(player.getLocation())) {
			final Location loc = player.getLocation();
			if (isSolidBlock(
					loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY() - 1, loc.getBlockZ()).getType())
					&& isSolidBlock(
							loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY() - 2, loc.getBlockZ()).getType())
					&& isSolidBlock(loc.getWorld()
							.getBlockAt(loc.getBlockX(), loc.getBlockY() - 1, loc.getBlockZ() - 1).getType())
					&& isSolidBlock(loc.getWorld()
							.getBlockAt(loc.getBlockX(), loc.getBlockY() - 2, loc.getBlockZ() - 1).getType())
					&& isSolidBlock(
							loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY() - 3, loc.getBlockZ()).getType())
					&& isSolidBlock(loc.getWorld()
							.getBlockAt(loc.getBlockX(), loc.getBlockY() - 3, loc.getBlockZ() - 1).getType())
					&& isSolidBlock(loc.getWorld()
							.getBlockAt(loc.getBlockX(), loc.getBlockY() - 1, loc.getBlockZ() - 1).getType())
					&& isSolidBlock(loc.getWorld()
							.getBlockAt(loc.getBlockX(), loc.getBlockY() - 2, loc.getBlockZ() - 1).getType())
					&& isSolidBlock(loc.getWorld()
							.getBlockAt(loc.getBlockX(), loc.getBlockY() - 1, loc.getBlockZ() - 2).getType())) {
				if (Herobrine.getPluginCore().getSupport().checkBuild(
						loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY() - 1, loc.getBlockZ()).getLocation())
						&& Herobrine.getPluginCore().getSupport().checkBuild(loc.getWorld()
								.getBlockAt(loc.getBlockX(), loc.getBlockY() - 2, loc.getBlockZ()).getLocation())
						&& Herobrine.getPluginCore().getSupport().checkBuild(loc.getWorld()
								.getBlockAt(loc.getBlockX(), loc.getBlockY() - 1, loc.getBlockZ() - 1).getLocation())
						&& Herobrine.getPluginCore().getSupport().checkBuild(loc.getWorld()
								.getBlockAt(loc.getBlockX(), loc.getBlockY() - 2, loc.getBlockZ() - 1).getLocation())
						&& Herobrine.getPluginCore().getSupport().checkBuild(loc.getWorld()
								.getBlockAt(loc.getBlockX(), loc.getBlockY() - 3, loc.getBlockZ()).getLocation())
						&& Herobrine.getPluginCore().getSupport().checkBuild(loc.getWorld()
								.getBlockAt(loc.getBlockX(), loc.getBlockY() - 3, loc.getBlockZ() - 1).getLocation())
						&& Herobrine.getPluginCore().getSupport().checkBuild(loc.getWorld()
								.getBlockAt(loc.getBlockX(), loc.getBlockY() - 1, loc.getBlockZ() - 1).getLocation())
						&& Herobrine.getPluginCore().getSupport().checkBuild(loc.getWorld()
								.getBlockAt(loc.getBlockX(), loc.getBlockY() - 2, loc.getBlockZ() - 1).getLocation())
						&& Herobrine.getPluginCore().getSupport().checkBuild(loc.getWorld()
								.getBlockAt(loc.getBlockX(), loc.getBlockY() - 1, loc.getBlockZ() - 2).getLocation())) {
					Bury(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), player);
					return new CoreResult(true, player.displayName() + " was buried by Herobrine.");
				} else {
					return new CoreResult(false, player.displayName() + " is in a protected area and cannot be buried.");
				}
			}
		} else {
			return new CoreResult(false, player.displayName()
					+ " could not be buried because a good burial location could not be found.");
		}
		return new CoreResult(false,
				player.displayName() + " could not be buried because a good burial location could not be found.");
	}

	public void Bury(World world, int X, int Y, int Z, Player player) {

		Location loc = new Location(world, X, Y, Z);

		loc.getWorld().getBlockAt(X, Y - 1, Z).breakNaturally();
		loc.getWorld().getBlockAt(X, Y - 2, Z).breakNaturally();
		loc.getWorld().getBlockAt(X, Y - 3, Z).breakNaturally();
		loc.getWorld().getBlockAt(X, Y - 1, Z - 1).breakNaturally();
		loc.getWorld().getBlockAt(X, Y - 2, Z - 1).breakNaturally();
		loc.getWorld().getBlockAt(X, Y - 3, Z - 1).breakNaturally();
		player.teleport(new Location(world, X, Y - 3, Z));
		RegenBlocks(world, X, Y, Z, player.getName());

	}

	public void RegenBlocks(World world, int X, int Y, int Z, String playername) {
		Location loc = new Location(world, X, Y, Z);
		Location signloc = new Location(world, X, Y, Z - 2);
		Block signblock = signloc.add(0, 0D, 0).getBlock();
		signblock.setType(Material.OAK_SIGN);
		Sign sign = (Sign) signblock.getState();
		sign.line(1, Component.text(playername));
		sign.update();
		loc.getWorld().getBlockAt(X, Y - 1, Z).setType(Material.STONE_BRICKS, false);
		loc.getWorld().getBlockAt(X, Y - 1, Z - 1).setType(Material.STONE_BRICKS, false);
	}
	
	private boolean isSolidBlock(Material m) {
		return m.isSolid();
	}

}
