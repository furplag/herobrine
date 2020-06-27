package net.theprogrammersworld.herobrine.NPC.Entity;

import net.minecraft.server.v1_16_R1.Entity;
import net.minecraft.server.v1_16_R1.EntityHuman;
import net.minecraft.server.v1_16_R1.EntityPlayer;
import net.minecraft.server.v1_16_R1.EnumGamemode;
import net.minecraft.server.v1_16_R1.EnumMoveType;
import net.minecraft.server.v1_16_R1.PlayerInteractManager;
import net.minecraft.server.v1_16_R1.Vec3D;
import net.theprogrammersworld.herobrine.NPC.NPCCore;
import net.theprogrammersworld.herobrine.NPC.NMS.BWorld;
import net.theprogrammersworld.herobrine.NPC.Network.NetworkHandler;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R1.CraftServer;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;

import com.mojang.authlib.GameProfile;

public class HumanEntity extends EntityPlayer {

	private CraftPlayer cplayer = null;

	public HumanEntity(final NPCCore npcCore, final BWorld world, final GameProfile s, final PlayerInteractManager playerInteractManager) {
		super(npcCore.getServer().getMCServer(), world.getWorldServer(), s, playerInteractManager);

		playerInteractManager.b(EnumGamemode.SURVIVAL);

		playerConnection = new NetworkHandler(npcCore, this);
		fauxSleeping = true;
	}

	@Override
	public void move(EnumMoveType x, Vec3D vec3d) {
		setPosition(vec3d.x, vec3d.y, vec3d.z);
	}

	@Override
	public boolean a(EntityHuman entity) {
		return super.a(entity);
	}

	@Override
	public void c(Entity entity) {
		super.c(entity);
	}

	@Override
	public CraftPlayer getBukkitEntity() {
		if (cplayer == null) {
			cplayer = new CraftPlayer((CraftServer) Bukkit.getServer(), this);
		}

		return cplayer;
	}

}
