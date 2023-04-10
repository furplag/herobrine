package net.theprogrammersworld.herobrine.NPC.Entity;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_19_R3.CraftServer;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;

import com.mojang.authlib.GameProfile;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;
import net.theprogrammersworld.herobrine.NPC.NPCCore;
import net.theprogrammersworld.herobrine.NPC.NMS.NMSWorld;

public class HumanEntity extends ServerPlayer {

  private CraftPlayer craftPlayer = null;

  public HumanEntity(final NPCCore npcCore, final NMSWorld world, final GameProfile gameProfile) {
    super(npcCore.getServer().getMinecraftServer(), world.getWorldServer(), gameProfile);
    setGameMode(GameType.SURVIVAL);
    connection = new ServerGamePacketListenerImpl(npcCore.getServer().getMinecraftServer(), npcCore.getNetworkmanager(), this);
    fauxSleeping = true;
  }

  @Override
  public void move(MoverType x, Vec3 vec3d) {
    setPos(vec3d);
    super.move(x, vec3d);
  }

  @Override
  public CraftPlayer getBukkitEntity() {
    if (craftPlayer == null) {
      craftPlayer = new CraftPlayer((CraftServer) Bukkit.getServer(), this);
    }

    return craftPlayer;
  }
}
