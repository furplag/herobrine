package org.jakub1221.herobrineai.entity;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;

public class EntityManager {
	
	private HashMap<Integer,CustomEntity> mobList = new HashMap<Integer,CustomEntity>();
	
	public void spawnCustomZombie(Location loc,MobType mbt){
		
		World world = loc.getWorld();
		net.minecraft.server.v1_5_R2.World mcWorld = ((org.bukkit.craftbukkit.v1_5_R2.CraftWorld) world).getHandle();
		CustomZombie zmb = new CustomZombie(mcWorld,loc,mbt);
		mcWorld.addEntity(zmb);
		mobList.put(new Integer(zmb.getBukkitEntity().getEntityId()),zmb);
		
	}
	
	public boolean isCustomMob(int id){
		return mobList.containsKey(new Integer(id));
	}
	
	public CustomEntity getMobType(int id){
		return mobList.get(new Integer(id));
	}
	
	public void removeMob(int id){
		mobList.remove(new Integer(id));
	}
	
	public void removeAllMobs(){
		mobList.clear();
	}
	
	public void killAllMobs(){
		for(Map.Entry<Integer, CustomEntity> s : mobList.entrySet()){
			s.getValue().Kill();
		}
		removeAllMobs();
	}

}
