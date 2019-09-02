package org.jakub1221.herobrineai.entity;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Color;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.Utils;
import org.jakub1221.herobrineai.misc.ItemName;

import net.minecraft.server.v1_14_R1.ChatComponentText;
import net.minecraft.server.v1_14_R1.EntityTypes;
import net.minecraft.server.v1_14_R1.GenericAttributes;
import net.minecraft.server.v1_14_R1.World;

public class CustomSkeleton extends net.minecraft.server.v1_14_R1.EntitySkeleton implements CustomEntity {

	private MobType mobType = null;

	public CustomSkeleton(World world, Location loc, MobType mbt) {
		super(EntityTypes.SKELETON, world);
		this.mobType = mbt;
		if (mbt == MobType.DEMON) {
			spawnDemon(loc);
		}
	}

	public void spawnDemon(Location loc) {

		this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(HerobrineAI.getPluginCore().getConfigDB().npc.getDouble("npc.Demon.Speed"));
		this.getAttributeInstance(GenericAttributes.MAX_HEALTH).setValue(HerobrineAI.getPluginCore().getConfigDB().npc.getInt("npc.Demon.HP"));
		this.setHealth(HerobrineAI.getPluginCore().getConfigDB().npc.getInt("npc.Demon.HP"));
		this.setCustomName(new ChatComponentText("Demon"));

		Skeleton entityCast = (Skeleton) this.getBukkitEntity();
		
		entityCast.getEquipment().setItemInMainHand(new ItemStack(Material.GOLDEN_APPLE, 1));
		entityCast.getEquipment().setHelmet(ItemName.colorLeatherArmor(new ItemStack(Material.LEATHER_HELMET, 1), Color.RED));
		entityCast.getEquipment().setChestplate(ItemName.colorLeatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE, 1), Color.RED));
		entityCast.getEquipment().setLeggings(ItemName.colorLeatherArmor(new ItemStack(Material.LEATHER_LEGGINGS, 1), Color.RED));
		entityCast.getEquipment().setBoots(ItemName.colorLeatherArmor(new ItemStack(Material.LEATHER_BOOTS, 1), Color.RED));
		this.getBukkitEntity().teleport(loc);

	}

	public CustomSkeleton(World world) {
		super(EntityTypes.SKELETON, world);
	}

	@Override
	public void Kill() {
		for (int i = 1; i <= 2500; i++) {
			if (HerobrineAI.getPluginCore().getConfigDB().npc.contains("npc.Demon.Drops." + Integer.toString(i)) == true) {
				int chance = Utils.getRandomGen().nextInt(100);
				
				int requiredRoll = HerobrineAI.getPluginCore().getConfigDB().npc.getInt("npc.Demon.Drops." + Integer.toString(i) + ".Chance");
				
				if (chance <= requiredRoll) {
				
					ItemStack its = new ItemStack(Material.getMaterial(i), HerobrineAI.getPluginCore().getConfigDB().npc.getInt("npc.Demon.Drops." + Integer.toString(i) + ".Count"));
					
					this.getBukkitEntity().getLocation().getWorld().dropItemNaturally(this.getBukkitEntity().getLocation(),its);
				}
			}
		}
		this.setHealth(0);
	}

	@Override
	public MobType getMobType() {
		return this.mobType;
	}

}
