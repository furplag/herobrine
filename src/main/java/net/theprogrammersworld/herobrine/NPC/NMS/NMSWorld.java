package net.theprogrammersworld.herobrine.NPC.NMS;

import java.io.IOException;

import org.bukkit.World;
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class NMSWorld {

  private final CraftWorld craftWorld;
  private final ServerLevel worldServer;

  public ChunkMap getPlayerManager() {
    try (ServerChunkCache chunkProvider = worldServer.getChunkSource()) {
      return chunkProvider.chunkMap;
    } catch (IOException e) {}

    return null;
  }

  public static NMSWorld of(final World world) {
    return new NMSWorld((CraftWorld) world, ((CraftWorld) world).getHandle());
  }
}
