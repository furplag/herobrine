package org.jakub1221.herobrineai.NPC.Entity;

import net.minecraft.server.v1_5_R2.Entity;
import net.minecraft.server.v1_5_R2.EntityHuman;
import net.minecraft.server.v1_5_R2.EntityPlayer;
import net.minecraft.server.v1_5_R2.EnumGamemode;
import net.minecraft.server.v1_5_R2.PlayerInteractManager;

import org.bukkit.craftbukkit.v1_5_R2.entity.CraftEntity;
import org.jakub1221.herobrineai.NPC.NPCCore;
import org.jakub1221.herobrineai.NPC.NMS.BWorld;
import org.jakub1221.herobrineai.NPC.Network.NetworkHandler;

public class HumanEntity extends EntityPlayer {


	public HumanEntity(NPCCore npcCore, BWorld world, String s, PlayerInteractManager playerInteractManager) {
		super(npcCore.getServer().getMCServer(), world.getWorldServer(), s, playerInteractManager);

		playerInteractManager.b(EnumGamemode.SURVIVAL);

		playerConnection = new NetworkHandler(npcCore, this);
		fauxSleeping = true;
	}

	public void setBukkitEntity(org.bukkit.entity.Entity entity) {
		bukkitEntity = (CraftEntity) entity;
	}

	@Override
	public void move(double arg0, double arg1, double arg2) {
		setPosition(arg0, arg1, arg2);
	}

	@Override
	public boolean a(EntityHuman entity) {
		return super.a(entity);
	}
	
	@Override
	public void c(Entity entity) {
		super.c(entity);
	}

}
