package net.theprogrammersworld.herobrine.NPC.Network;

import net.minecraft.network.protocol.Packet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class NetworkUtils {
	public static void sendPacketNearby(Location location, Packet<?> packet) {
		sendPacketNearby(location, packet, 64);
	}

	public static void sendPacketNearby(Location location, Packet<?> packet, double radius) {
		radius *= radius;
		final World world = location.getWorld();
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (world == player.getWorld()) {
				if (location.distanceSquared(player.getLocation()) <= radius) {
					((CraftPlayer) player).getHandle().connection.send(packet);
				}
			}
		}
	}

	public static ItemStack[] combineItemStackArrays(Object[] a, Object[] b) {
		ItemStack[] c = new ItemStack[a.length + b.length];
		System.arraycopy(a, 0, c, 0, a.length);
		System.arraycopy(b, 0, c, a.length, b.length);
		return c;
	}
}
