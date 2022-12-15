package net.theprogrammersworld.herobrine.entity;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public class EntityManager {

	private HashMap<Integer,CustomEntity> mobList = new HashMap<Integer,CustomEntity>();

	public void spawnCustomZombie(Location loc,MobType mbt){

		World world = loc.getWorld();
		net.minecraft.world.level.Level mcWorld = ((org.bukkit.craftbukkit.v1_19_R2.CraftWorld) world).getHandle();
		CustomZombie zmb = new CustomZombie(mcWorld,loc,mbt);
		mcWorld.addFreshEntity(zmb, SpawnReason.CUSTOM);
		mobList.put(Integer.valueOf(zmb.getBukkitEntity().getEntityId()),zmb);

	}

   public void spawnCustomSkeleton(Location loc,MobType mbt){

		World world = loc.getWorld();
		net.minecraft.world.level.Level mcWorld = ((org.bukkit.craftbukkit.v1_19_R2.CraftWorld) world).getHandle();
		CustomSkeleton zmb = new CustomSkeleton(mcWorld,loc,mbt);
		mcWorld.addFreshEntity(zmb, SpawnReason.CUSTOM);
		mobList.put(Integer.valueOf(zmb.getBukkitEntity().getEntityId()), zmb);
	}

	public boolean isCustomMob(int id){
		return mobList.containsKey(Integer.valueOf(id));
	}

	public CustomEntity getHerobrineMobType(int id){
		return mobList.get(Integer.valueOf(id));
	}

	public void removeMob(int id){
		mobList.get(Integer.valueOf(id)).Kill();
		mobList.remove(Integer.valueOf(id));
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
