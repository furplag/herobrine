package net.theprogrammersworld.herobrine.entity;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

import org.bukkit.Color;

import net.minecraft.server.v1_15_R1.ChatComponentText;
import net.minecraft.server.v1_15_R1.Entity;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.GenericAttributes;
import net.minecraft.server.v1_15_R1.World;
import net.theprogrammersworld.herobrine.Herobrine;
import net.theprogrammersworld.herobrine.misc.ItemName;

public class CustomSkeleton extends net.minecraft.server.v1_15_R1.EntitySkeleton implements CustomEntity {

	private MobType mobType = null;
	
	public CustomSkeleton(EntityTypes<? extends Entity> entitytypes, World world) {
		super(EntityTypes.SKELETON, world);
	}

	public CustomSkeleton(World world, Location loc, MobType mbt) {
		super(EntityTypes.SKELETON, world);
		this.mobType = mbt;
		if (mbt == MobType.DEMON) {
			spawnDemon(loc);
		}
	}

	public void spawnDemon(Location loc) {

		this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(Herobrine.getPluginCore().getConfigDB().npc.getDouble("npc.Demon.Speed"));
		this.getAttributeInstance(GenericAttributes.MAX_HEALTH).setValue(Herobrine.getPluginCore().getConfigDB().npc.getInt("npc.Demon.HP"));
		this.setHealth(Herobrine.getPluginCore().getConfigDB().npc.getInt("npc.Demon.HP"));
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
		Object[] items = Herobrine.getPluginCore().getConfigDB().npc.getConfigurationSection("npc.Demon.Drops")
				.getKeys(false).toArray();
		for (Object itemObj : items) {
			final String item = itemObj.toString();
			final int chance = new Random().nextInt(100);
			if (chance <= Herobrine.getPluginCore().getConfigDB().npc.getInt("npc.Demon.Drops." + item + ".Chance")) {
				getBukkitEntity().getLocation().getWorld().dropItemNaturally(getBukkitEntity().getLocation(),
						new ItemStack(Material.matchMaterial(item), Herobrine.getPluginCore().getConfigDB().npc
								.getInt("npc.Demon.Drops." + item + ".Count")));
			}
		}
		setHealth(0.0f);
	}

	@Override
	public MobType getMobType() {
		return this.mobType;
	}

}
