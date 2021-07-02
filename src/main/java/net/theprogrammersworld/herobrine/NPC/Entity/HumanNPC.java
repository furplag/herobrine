package net.theprogrammersworld.herobrine.NPC.Entity;

import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.protocol.game.PacketPlayInArmAnimation;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ChunkMap.TrackedEntity;
import net.minecraft.server.level.ServerLevel;
import net.theprogrammersworld.herobrine.Herobrine;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class HumanNPC {

	private ServerPlayer entity;
	private final int id;

	public HumanNPC(HumanEntity humanEntity, int id) {
		this.entity = humanEntity;
		this.id = id;
	}

	public int getID() {
		return this.id;
	}

	public ServerPlayer getEntity() {
		return this.entity;
	}

	public void ArmSwingAnimation() {
		ServerChunkCache chunkProvider = this.entity.getLevel().getChunkProvider();
		ChunkMap playerChunkMap = chunkProvider.chunkMap;
		ChunkMap.TrackedEntity playerchunkmap_entitytracker = playerChunkMap.trackedEntities.get(this.entity.getId());
		
		if(playerchunkmap_entitytracker != null) {
			playerchunkmap_entitytracker.broadcast(new PacketPlayInArmAnimation(InteractionHand.MAIN_HAND));
		}
	}

	public void HurtAnimation() {
		LivingEntity ent = (LivingEntity) entity.getBukkitEntity();

		double healthBefore = ent.getHealth();

		ent.damage(0.5D);
		ent.setHealth(healthBefore);
	}

	public void setItemInHand(ItemStack item) {
		if (item != null) {
			((org.bukkit.entity.HumanEntity) getEntity().getBukkitEntity()).getInventory().setItemInMainHand(item);
		}
	}

	public String getName() {
		return ((HumanEntity) getEntity()).getName();
	}

	public void setPitch(float pitch) {
		((HumanEntity) getEntity()).pitch = pitch;
	}

	public void moveTo(Location loc) {
		Teleport(loc);
	}

	public void Teleport(Location loc) {
		getEntity().getBukkitEntity().teleport(loc);
		
		// After Herobrine moves, check if any players are in Herobrine's line of sight if the persistent tab list entry is disabled.
		if(!Herobrine.getPluginCore().getConfigDB().ShowInTabList) {
			boolean doActivationTeleport = false;
			for(Player p : Bukkit.getOnlinePlayers())
				doActivationTeleport = doActivationTeleport || Herobrine.getPluginCore().getAICore().toggleHerobrinePlayerVisibilityNoTeleport(p);
			if(doActivationTeleport)
				Herobrine.getPluginCore().getAICore().visibilityActivationTeleport();
		}
	}

	public PlayerInventory getInventory() {
		return ((org.bukkit.entity.HumanEntity) getEntity().getBukkitEntity()).getInventory();
	}

	public void removeFromWorld() {
		try {
			entity.getWorldServer().removeEntity(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setYaw(float yaw) {
		((EntityPlayer) getEntity()).yaw = yaw;
	}

	public void lookAtPoint(Location point) {

		if (getEntity().getBukkitEntity().getWorld() != point.getWorld()) {
			return;
		}

		Location npcLoc = ((LivingEntity) getEntity().getBukkitEntity()).getEyeLocation();
		double xDiff = point.getX() - npcLoc.getX();
		double yDiff = point.getY() - npcLoc.getY();
		double zDiff = point.getZ() - npcLoc.getZ();
		double DistanceXZ = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
		double DistanceY = Math.sqrt(DistanceXZ * DistanceXZ + yDiff * yDiff);

		double newYaw = Math.acos(xDiff / DistanceXZ) * 180 / Math.PI;
		double newPitch = (Math.acos(yDiff / DistanceY) * 180 / Math.PI) - 90;

		if (zDiff < 0.0) {
			newYaw = newYaw + Math.abs(180 - newYaw) * 2.0D;
		}

		if (newYaw > 0.0D || newYaw < 180.0D) {
			entity.yaw = (float) (newYaw - 90.0);
			entity.pitch = (float) newPitch;
			entity.aE = (float) (newYaw - 90.0);
			entity.aC = (float) (newYaw - 90.0);
		}

	}

	public void setYawA(float yaw) {
		((EntityPlayer) getEntity()).yaw = yaw;
	}

	public org.bukkit.entity.Entity getBukkitEntity() {
		return entity.getBukkitEntity();
	}
}
