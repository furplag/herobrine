package org.jakub1221.herobrineai.NPC.NMS;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.server.v1_5_R2.DedicatedServer;
import net.minecraft.server.v1_5_R2.MinecraftServer;
import net.minecraft.server.v1_5_R2.WorldServer;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_5_R2.CraftServer;

public class BServer { 
	private static BServer ins;
	private MinecraftServer mcServer;
	private CraftServer cServer;
	private Server server;
	private HashMap<String, BWorld> worlds = new HashMap<String, BWorld>();

	private BServer() {
		server = Bukkit.getServer();
		try {
			cServer = (CraftServer) server;
			mcServer = cServer.getServer();
		} catch (Exception ex) {
			Logger.getLogger("Minecraft").log(Level.SEVERE, null, ex);
		}
	}

	public void stop() {
		mcServer.safeShutdown();
	}

	public void sendConsoleCommand(String cmd) {
		if (mcServer.isRunning()) {
			((DedicatedServer) mcServer).issueCommand(cmd, mcServer);
		}
	}

	public Logger getLogger() {
		return cServer.getLogger();
	}

	public List<WorldServer> getWorldServers() {
		return mcServer.worlds;
	}

	public Server getServer() {
		return server;
	}

	public BWorld getWorld(String worldName) {
		if (worlds.containsKey(worldName)) {
			return worlds.get(worldName);
		}
		BWorld w = new BWorld(this, worldName);
		worlds.put(worldName, w);
		return w;
	}

	public static BServer getInstance() {
		if (ins == null) {
			ins = new BServer();
		}
		return ins;
	}

	public MinecraftServer getMCServer() {
		return mcServer;
	}

}
