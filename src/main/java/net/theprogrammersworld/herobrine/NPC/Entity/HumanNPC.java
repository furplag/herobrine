package net.theprogrammersworld.herobrine.NPC.Entity;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import lombok.Getter;
import net.minecraft.world.InteractionHand;
import net.theprogrammersworld.herobrine.Herobrine;

public class HumanNPC {

    @Getter private HumanEntity entity;
	@Getter private final int id;

	public HumanNPC(HumanEntity humanEntity, int id) {
		this.entity = humanEntity;
		this.id = id;
	}

	public void armSwingAnimation() {
		entity.swing(InteractionHand.MAIN_HAND);
	}

	public void hurtAnimation() {
		LivingEntity ent = (LivingEntity) entity.getBukkitEntity();

		double healthBefore = ent.getHealth();

		ent.damage(0.5D);
		ent.setHealth(healthBefore);
	}

	public void setItemInHand(ItemStack item) {
		if (item != null) {
			getEntity().getBukkitEntity().getInventory().setItemInMainHand(item);
		}
	}

	public String getName() {
		return getEntity().getName().getString();
	}

	public void setPitch(float pitch) {
		((HumanEntity) getEntity()).setXRot(pitch);
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
			entity.getBukkitEntity().remove();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setYaw(float yaw) {
		getEntity().setYRot(yaw);
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
			entity.setXRot((float) newPitch);
			entity.setYRot((float) (newYaw - 90.0));
			entity.getBukkitEntity().setFlySpeed((float) (newYaw - 90.0) / 360);
		}

	}

	public void setYawA(float yaw) {
		getEntity().setYRot(yaw);
	}

	public org.bukkit.entity.Entity getBukkitEntity() {
		return entity.getBukkitEntity();
	}
}
