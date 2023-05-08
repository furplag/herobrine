package net.theprogrammersworld.herobrine.NPC.NMS;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_19_R3.CraftServer;
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;

public class NMSServer {

  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  @Getter
  public static final class World {

    private final CraftWorld craftWorld;
    private final ServerLevel worldServer;

    public ChunkMap getPlayerManager() {
      try (ServerChunkCache chunkProvider = worldServer.getChunkSource()) {
        return chunkProvider.chunkMap;
      } catch (IOException e) {}

      return null;
    }

    public static final World of(final org.bukkit.World world) {
      return new World((CraftWorld) world, ((CraftWorld) world).getHandle());
    }
  }

  private static final class Singleton {
    private static final NMSServer nmsServer = new NMSServer();
  }

  private final Server server;
  @Getter private final MinecraftServer minecraftServer;
  private Map<String, World> worlds = new HashMap<String, World>();

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

  public World getWorld(String worldName) {
    worlds.putIfAbsent(worldName, World.of(getInstance().getServer().getWorld(worldName)));

    return worlds.get(worldName);
  }

  public static NMSServer getInstance() {
    return Singleton.nmsServer;
  }
}
