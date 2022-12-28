package net.theprogrammersworld.herobrine.NPC.NMS;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_19_R2.CraftServer;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.ServerLevel;

public class NMSServer {

  private final Server server;

	private static NMSServer ins;
	private MinecraftServer mcServer;
	private CraftServer cServer;
	private HashMap<String, NMSWorld> worlds = new HashMap<String, NMSWorld>();

	private NMSServer() {
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
	  return StreamSupport.stream(mcServer.getAllLevels().spliterator(), false).collect(Collectors.toList());
	}

	public Server getServer() {
		return server;
	}

	public NMSWorld getWorld(String worldName) {
		if (worlds.containsKey(worldName)) {
			return worlds.get(worldName);
		}
		NMSWorld w = new NMSWorld(ins.getServer().getWorld(worldName));
		worlds.put(worldName, w);
		return w;
	}

	public static NMSServer getInstance() {
		if (ins == null) {
			ins = new NMSServer();
		}
		return ins;
	}

	public MinecraftServer getMCServer() {
		return mcServer;
	}

}
