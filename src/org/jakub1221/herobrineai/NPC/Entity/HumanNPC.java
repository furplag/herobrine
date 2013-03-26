package org.jakub1221.herobrineai.NPC.Entity;

import net.minecraft.server.v1_5_R2.EntityPlayer;
import net.minecraft.server.v1_5_R2.Packet18ArmAnimation;
import net.minecraft.server.v1_5_R2.WorldServer;
import net.minecraft.server.v1_5_R2.Entity;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class HumanNPC {
	 
	private Entity entity;
	private final int id;

		public HumanNPC(HumanEntity humanEntity,int id) {
			this.entity=humanEntity;
			this.id=id;
		}

		public int getID(){
			return this.id;
		}
		
		public Entity getEntity(){
			return this.entity;
		}
		
		public void ArmSwingAnimation() {
			((WorldServer) getEntity().world).tracker.a(getEntity(), new Packet18ArmAnimation(getEntity(), 1));
		}

		public void HurtAnimation() {
			((WorldServer) getEntity().world).tracker.a(getEntity(), new Packet18ArmAnimation(getEntity(), 2));
		}

		public void setItemInHand(ItemStack item) {
			if (item!=null){
		 ((org.bukkit.entity.HumanEntity) getEntity().getBukkitEntity()).setItemInHand(item);
			}
		}

		public String getName() {
			return ((HumanEntity) getEntity()).name;
		}
		
		public void setPitch(float pitch){
			((HumanEntity) getEntity()).pitch=pitch;
		}
		
		public void moveTo(Location loc){
			Teleport(loc);
		}
		
		public void Teleport(Location loc){
			getEntity().getBukkitEntity().teleport(loc);
		}

		public PlayerInventory getInventory() {
			return ((org.bukkit.entity.HumanEntity) getEntity().getBukkitEntity()).getInventory();
		}

		public void removeFromWorld() {
			try {
				entity.world.removeEntity(entity);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	    
		public void setYaw(float yaw){
			((EntityPlayer) getEntity()).yaw=yaw;
			((EntityPlayer) getEntity()).aA=yaw;
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
			double newPitch = Math.acos(yDiff / DistanceY) * 180 / Math.PI - 90;
			
			if (zDiff < 0.0) {
				newYaw = newYaw + Math.abs(180 - newYaw) * 2;
			}
			
			((EntityPlayer) getEntity()).yaw = (float) (newYaw - 90);
			((EntityPlayer) getEntity()).pitch = (float) newPitch;
			((EntityPlayer) getEntity()).aA = (float) (newYaw - 90);


		}

		public org.bukkit.entity.Entity getBukkitEntity() {
			return entity.getBukkitEntity();
		}
}
