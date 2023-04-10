package net.theprogrammersworld.herobrine.NPC.NMS;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_19_R3.CraftServer;

import lombok.Getter;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.ServerLevel;

public class NMSServer {

  private static final class Singleton {
    private static final NMSServer nmsServer = new NMSServer();
  }


  private final Server server;
  @Getter private final MinecraftServer minecraftServer;
  private Map<String, NMSWorld> worlds = new HashMap<String, NMSWorld>();

  private NMSServer() {
    server = Bukkit.getServer();
    minecraftServer = ((CraftServer) server).getServer();
  }

  public void stop() {
    minecraftServer.halt(true);
  }

  public void sendConsoleCommand(String cmd) {
    if (minecraftServer.isRunning()) {
      ((DedicatedServer) minecraftServer).handleConsoleInput(cmd, minecraftServer.createCommandSourceStack());
    }
  }

  public List<ServerLevel> getWorldServers() {
    return StreamSupport.stream(minecraftServer.getAllLevels().spliterator(), false).collect(Collectors.toList());
  }

  public Server getServer() {
    return server;
  }

  public NMSWorld getWorld(String worldName) {
    worlds.putIfAbsent(worldName, NMSWorld.of(getInstance().getServer().getWorld(worldName)));

    return worlds.get(worldName);
  }

  public static NMSServer getInstance() {
    return Singleton.nmsServer;
  }
}
