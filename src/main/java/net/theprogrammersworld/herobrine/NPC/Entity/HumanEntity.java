package net.theprogrammersworld.herobrine.NPC.Entity;

import net.minecraft.world.level.GameType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.theprogrammersworld.herobrine.NPC.NPCCore;
import net.theprogrammersworld.herobrine.NPC.NMS.BWorld;
import net.theprogrammersworld.herobrine.NPC.Network.NetworkHandler;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;

import com.mojang.authlib.GameProfile;

public class HumanEntity extends ServerPlayer {

	private CraftPlayer cplayer = null;

	public HumanEntity(final NPCCore npcCore, final BWorld world, final GameProfile s, final ServerPlayerGameMode playerInteractManager) {
		super(npcCore.getServer().getMCServer(), world.getWorldServer(), s);

		playerInteractManager.changeGameModeForPlayer(GameType.SURVIVAL);

		connection = new NetworkHandler(npcCore, this);
		fauxSleeping = true;
	}

	@Override
	public void move(MoverType x, Vec3 vec3d) {
		setPos(vec3d);
	}

	@Override
	public CraftPlayer getBukkitEntity() {
		if (cplayer == null) {
			cplayer = new CraftPlayer((CraftServer) Bukkit.getServer(), this);
		}

		return cplayer;
	}

}
