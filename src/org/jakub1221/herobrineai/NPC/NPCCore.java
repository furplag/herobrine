package org.jakub1221.herobrineai.NPC;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import net.minecraft.server.v1_11_R1.Entity;
import net.minecraft.server.v1_11_R1.PlayerInteractManager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jakub1221.herobrineai.NPC.Entity.HumanEntity;
import org.jakub1221.herobrineai.NPC.Entity.HumanNPC;
import org.jakub1221.herobrineai.NPC.NMS.BServer;
import org.jakub1221.herobrineai.NPC.NMS.BWorld;
import org.jakub1221.herobrineai.NPC.Network.NetworkCore;

public class NPCCore {

	private ArrayList<HumanNPC> npcs = new ArrayList<HumanNPC>();
	private BServer server;
	private int taskid;
	private Map<World, BWorld> bworlds = new HashMap<World, BWorld>();
	private NetworkCore networkCore;
	public static JavaPlugin plugin;
	public boolean isInLoaded = false;
	private int lastID = 0;

	public NPCCore(JavaPlugin plugin) {
		server = BServer.getInstance();

		try {
			networkCore = new NetworkCore();
		} catch (IOException e) {
			e.printStackTrace();
		}

		taskid = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			@Override
			public void run() {
				ArrayList<HumanNPC> toRemove = new ArrayList<HumanNPC>();
				for (HumanNPC i : npcs) {
					Entity j = i.getEntity();

					if (j.dead) {
						toRemove.add(i);
					}
				}
				for (HumanNPC n : toRemove) {
					npcs.remove(n);
				}
			}
		}, 1L, 1L);
		Bukkit.getServer().getPluginManager().registerEvents(new WorldL(), plugin);
	}

	public void removeAll() {
		for (HumanNPC humannpc : npcs) {
			if (humannpc != null) {
				humannpc.removeFromWorld();
			}
		}
		npcs.clear();
	}

	public BWorld getBWorld(World world) {
		BWorld bworld = bworlds.get(world);
		if (bworld != null) {
			return bworld;
		}
		bworld = new BWorld(world);
		bworlds.put(world, bworld);
		return bworld;
	}

	public void DisableTask() {
		Bukkit.getServer().getScheduler().cancelTask(taskid);
	}

	private class WorldL implements Listener {
		@SuppressWarnings("unused")
		@EventHandler
		public void onChunkLoad(ChunkLoadEvent event) throws EventException {
			for (HumanNPC humannpc : npcs) {
				if (humannpc != null
						&& event.getChunk() == humannpc.getBukkitEntity().getLocation().getBlock().getChunk()) {

					if (isInLoaded == false) {
						BWorld world = getBWorld(event.getWorld());

						isInLoaded = true;
					}
				}
			}

		}

		@EventHandler
		public void onChunkUnload(ChunkUnloadEvent event) {
			for (HumanNPC humannpc : npcs) {
				if (humannpc != null
						&& event.getChunk() == humannpc.getBukkitEntity().getLocation().getBlock().getChunk()) {

				}
			}
		}
	}

	public HumanNPC spawnHumanNPC(String name, Location l) {
		lastID++;
		int id = lastID;
		return spawnHumanNPC(name, l, id);
	}

	public HumanNPC spawnHumanNPC(String name, Location l, int id) {

		BWorld world = getBWorld(l.getWorld());
		HumanEntity humanEntity = new HumanEntity(this, world, name, new PlayerInteractManager(world.getWorldServer()));
		((Entity) humanEntity).setLocation(l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
		world.getWorldServer().addEntity(humanEntity);
		HumanNPC humannpc = new HumanNPC(humanEntity, id);
		npcs.add(humannpc);
		return humannpc;
	}

	public HumanNPC getHumanNPC(int id) {

		for (HumanNPC n : npcs) {
			if (n.getID() == id) {
				return n;
			}
		}

		return null;
	}

	public BServer getServer() {
		return server;
	}

	public NetworkCore getNetworkCore() {
		return networkCore;
	}

}
