package org.jakub1221.herobrineai.entity;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.Utils;

import net.minecraft.server.v1_11_R1.GenericAttributes;
import net.minecraft.server.v1_11_R1.World;

public class CustomZombie extends net.minecraft.server.v1_11_R1.EntityZombie implements CustomEntity {

	private MobType mobType = null;

	public CustomZombie(World world, Location loc, MobType mbt) {
		super(world);
		this.mobType = mbt;
		if (mbt == MobType.ARTIFACT_GUARDIAN) {
			spawnArtifactGuardian(loc);
		} else if (mbt == MobType.HEROBRINE_WARRIOR) {
			spawnHerobrineWarrior(loc);
		}
	}

	private void spawnArtifactGuardian(Location loc) {

		this.getAttributeInstance(GenericAttributes.c).setValue(HerobrineAI.getPluginCore().getConfigDB().npc.getDouble("npc.Guardian.Speed"));
		this.getAttributeInstance(GenericAttributes.maxHealth).setValue(HerobrineAI.getPluginCore().getConfigDB().npc.getInt("npc.Guardian.HP"));
		this.setHealth(HerobrineAI.getPluginCore().getConfigDB().npc.getInt("npc.Guardian.HP"));

		this.setCustomName("Artifact Guardian");

		Zombie entityCast = (Zombie) this.getBukkitEntity();

		entityCast.getEquipment().setItemInMainHand(new ItemStack(Material.GOLD_SWORD, 1));
		entityCast.getEquipment().setHelmet(new ItemStack(Material.GOLD_HELMET, 1));
		entityCast.getEquipment().setChestplate(new ItemStack(Material.GOLD_CHESTPLATE, 1));
		entityCast.getEquipment().setLeggings(new ItemStack(Material.GOLD_LEGGINGS, 1));
		entityCast.getEquipment().setBoots(new ItemStack(Material.GOLD_BOOTS, 1));

		this.getBukkitEntity().teleport(loc);

	}

	private void spawnHerobrineWarrior(Location loc) {

		this.getAttributeInstance(GenericAttributes.c).setValue(HerobrineAI.getPluginCore().getConfigDB().npc.getDouble("npc.Warrior.Speed"));
		this.getAttributeInstance(GenericAttributes.maxHealth).setValue(HerobrineAI.getPluginCore().getConfigDB().npc.getInt("npc.Warrior.HP"));
		this.setHealth(HerobrineAI.getPluginCore().getConfigDB().npc.getInt("npc.Warrior.HP"));

		this.setCustomName("Herobrine Warrior");

		Zombie entityCast = (Zombie) this.getBukkitEntity();

		entityCast.getEquipment().setItemInMainHand(new ItemStack(Material.IRON_SWORD, 1));
		entityCast.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET, 1));
		entityCast.getEquipment().setChestplate(new ItemStack(Material.IRON_CHESTPLATE, 1));
		entityCast.getEquipment().setLeggings(new ItemStack(Material.IRON_LEGGINGS, 1));
		entityCast.getEquipment().setBoots(new ItemStack(Material.IRON_BOOTS, 1));

		this.getBukkitEntity().teleport(loc);

	}

	public CustomZombie(World world) {
		super(world);
	}

	@Override
	public void Kill() {

		String mobS = "";
		if (this.mobType == MobType.ARTIFACT_GUARDIAN) {
			mobS = "Guardian";
		} else {
			mobS = "Warrior";
		}
		
		for (int i = 1; i <= 2500; i++) {
			if (HerobrineAI.getPluginCore().getConfigDB().npc.contains("npc." + mobS + ".Drops." + Integer.toString(i)) == true) {
				int chance = Utils.getRandomGen().nextInt(100);
				
				int requiredRoll = HerobrineAI.getPluginCore().getConfigDB().npc.getInt("npc." + mobS + ".Drops." + Integer.toString(i) + ".Chance");
				
				if (chance <= requiredRoll) {
				
					ItemStack its = new ItemStack(Material.getMaterial(i), HerobrineAI.getPluginCore().getConfigDB().npc.getInt("npc." + mobS + ".Drops." + Integer.toString(i) + ".Count"));
					
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
