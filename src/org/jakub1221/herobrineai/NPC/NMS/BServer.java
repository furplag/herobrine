package org.jakub1221.herobrineai.NPC.NMS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.server.v1_15_R1.DedicatedServer;
import net.minecraft.server.v1_15_R1.MinecraftServer;
import net.minecraft.server.v1_15_R1.WorldServer;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;

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
		mcServer.safeShutdown(true);
	}

	public void sendConsoleCommand(String cmd) {
		if (mcServer.isRunning()) {
			((DedicatedServer) mcServer).issueCommand(cmd, mcServer.getServerCommandListener());
		}
	}

	public Logger getLogger() {
		return cServer.getLogger();
	}

	public List<WorldServer> getWorldServers() {
		List<WorldServer> worlds = new ArrayList<>();
		for (WorldServer world:mcServer.getWorlds())
			worlds.add(world);
		return worlds;
	}

	public Server getServer() {
		return server;
	}

	public BWorld getWorld(String worldName) {
		if (worlds.containsKey(worldName)) {
			return worlds.get(worldName);
		}
		BWorld w = new BWorld(ins.getServer().getWorld(worldName));
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
