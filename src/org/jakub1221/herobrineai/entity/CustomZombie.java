package org.jakub1221.herobrineai.entity;

import org.bukkit.Location;

import net.minecraft.server.v1_5_R2.ItemStack;
import net.minecraft.server.v1_5_R2.World;

public class CustomZombie extends net.minecraft.server.v1_5_R2.EntityZombie implements CustomEntity{
	 
	private MobType mobType=null;
	
    public CustomZombie(World world,Location loc,MobType mbt) {
		super(world);
		this.mobType=mbt;
        if (mbt==MobType.ARTIFACT_GUARDIAN){
        	spawnArtifactGuardian(loc);
        }else if (mbt==MobType.HEROBRINE_WARRIOR){
        	spawnHerobrineWarrior(loc);
        }
	}

    private void spawnArtifactGuardian(Location loc){
    	
        this.health=50;
        this.setCustomName("Artifact Guardian");
        this.maxHealth=50;
        this.setEquipment(0, new ItemStack(net.minecraft.server.v1_5_R2.Item.GOLD_SWORD));
        this.setEquipment(1, new ItemStack(net.minecraft.server.v1_5_R2.Item.GOLD_BOOTS));
        this.setEquipment(2, new ItemStack(net.minecraft.server.v1_5_R2.Item.GOLD_LEGGINGS));
        this.setEquipment(3, new ItemStack(net.minecraft.server.v1_5_R2.Item.GOLD_CHESTPLATE));
        this.setEquipment(4, new ItemStack(net.minecraft.server.v1_5_R2.Item.GOLD_HELMET));
        this.getBukkitEntity().teleport(loc);
        
    
        
    }
    
    private void spawnHerobrineWarrior(Location loc){
    	
        this.health=25;
        this.setCustomName("Herobrine´s Warrior");
        this.maxHealth=25;
        this.setEquipment(0, new ItemStack(net.minecraft.server.v1_5_R2.Item.IRON_SWORD));
        this.setEquipment(1, new ItemStack(net.minecraft.server.v1_5_R2.Item.IRON_BOOTS));
        this.setEquipment(2, new ItemStack(net.minecraft.server.v1_5_R2.Item.IRON_LEGGINGS));
        this.setEquipment(3, new ItemStack(net.minecraft.server.v1_5_R2.Item.IRON_CHESTPLATE));
        this.setEquipment(4, new ItemStack(net.minecraft.server.v1_5_R2.Item.IRON_HELMET));
        this.getBukkitEntity().teleport(loc);
        
    
        
    }
    
    public CustomZombie(World world) {
        super(world);    
    }

	@Override
	public void Kill() {
      this.health=0;		
	}

	@Override
	public MobType getMobType() {
		return this.mobType;
	}
 
}
