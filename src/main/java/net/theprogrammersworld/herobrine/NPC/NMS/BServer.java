package net.theprogrammersworld.herobrine.NPC.NMS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.MinecraftServer;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;

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
		mcServer.halt(true);
	}

	public void sendConsoleCommand(String cmd) {
		if (mcServer.isRunning()) {
			((DedicatedServer) mcServer).handleConsoleInput(cmd, mcServer.createCommandSourceStack());
		}
	}

	public Logger getLogger() {
		return cServer.getLogger();
	}

	public List<ServerLevel> getWorldServers() {
		List<ServerLevel> worlds = new ArrayList<>();
		for (ServerLevel world:mcServer.getAllLevels())
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
