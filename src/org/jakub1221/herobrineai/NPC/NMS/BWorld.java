package org.jakub1221.herobrineai.NPC.NMS;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.server.v1_14_R1.PlayerChunkMap;
import net.minecraft.server.v1_14_R1.WorldServer;

import org.bukkit.World;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;

public class BWorld {
	
	private CraftWorld cWorld;
	private WorldServer wServer;

	public BWorld(final World world) {
		try {
			cWorld = (CraftWorld) world;
			wServer = cWorld.getHandle();
		} catch (Exception ex) {
			Logger.getLogger("Minecraft").log(Level.SEVERE, null, ex);
		}
	}

	public PlayerChunkMap getPlayerManager() {
		return wServer.getChunkProvider().playerChunkMap;
	}

	public CraftWorld getCraftWorld() {
		return cWorld;
	}

	public WorldServer getWorldServer() {
		return wServer;
	}
}
