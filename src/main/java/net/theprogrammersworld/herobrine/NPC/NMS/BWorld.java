package net.theprogrammersworld.herobrine.NPC.NMS;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;

import org.bukkit.World;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;

public class BWorld {
	
	private CraftWorld cWorld;
	private ServerLevel wServer;

	public BWorld(final World world) {
		try {
			cWorld = (CraftWorld) world;
			wServer = cWorld.getHandle();
		} catch (Exception ex) {
			Logger.getLogger("Minecraft").log(Level.SEVERE, null, ex);
		}
	}

	public ChunkMap getPlayerManager() {
		ServerChunkCache chunkProvider = ((ServerLevel) wServer).getChunkProvider();
		return chunkProvider.chunkMap;
	}

	public CraftWorld getCraftWorld() {
		return cWorld;
	}

	public ServerLevel getWorldServer() {
		return wServer;
	}
}
