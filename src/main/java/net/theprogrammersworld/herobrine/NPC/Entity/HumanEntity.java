package net.theprogrammersworld.herobrine.NPC.Entity;

import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R2.CraftServer;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer;

import com.mojang.authlib.GameProfile;

import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;
import net.theprogrammersworld.herobrine.NPC.NPCCore;
import net.theprogrammersworld.herobrine.NPC.NMS.NMSServer;

public class HumanEntity extends ServerPlayer {

  private CraftPlayer craftPlayer;

  public HumanEntity(final NPCCore npcCore, final NMSServer.World world, final GameProfile gameProfile) {
    super(npcCore.getServer().getMinecraftServer(), world.getWorldServer(), gameProfile, ClientInformation.createDefault());
    setGameMode(GameType.SURVIVAL);
    craftPlayer = null;
    fauxSleeping = true;
    connection = new ServerGamePacketListenerImpl(npcCore.getServer().getMinecraftServer(), npcCore.getNetworkmanager(), this, CommonListenerCookie.createInitial(getGameProfile())) {
      @Override public void send(final Packet<?> packet) {}
    };
  }

  @Override
  public void move(MoverType x, Vec3 vec3d) {
    setPos(vec3d);
  }

  @Override
  public CraftPlayer getBukkitEntity() {
    return Optional.ofNullable(craftPlayer).orElseGet(() -> { craftPlayer = new CraftPlayer((CraftServer) Bukkit.getServer(), this); return craftPlayer; });
  }
}
