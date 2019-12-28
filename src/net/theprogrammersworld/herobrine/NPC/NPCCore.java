package net.theprogrammersworld.herobrine.NPC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.server.v1_15_R1.Entity;
import net.minecraft.server.v1_15_R1.PlayerInteractManager;
import net.theprogrammersworld.herobrine.HerobrineAI;
import net.theprogrammersworld.herobrine.NPC.Entity.HumanEntity;
import net.theprogrammersworld.herobrine.NPC.Entity.HumanNPC;
import net.theprogrammersworld.herobrine.NPC.NMS.BServer;
import net.theprogrammersworld.herobrine.NPC.NMS.BWorld;
import net.theprogrammersworld.herobrine.NPC.Network.NetworkCore;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

public class NPCCore {

	private ArrayList<HumanNPC> npcs = new ArrayList<HumanNPC>();
	private BServer server;
	private int taskid;
	private Map<World, BWorld> bworlds = new HashMap<World, BWorld>();
	private NetworkCore networkCore;
	public static JavaPlugin plugin;
	public boolean isInLoaded = false;
	private int lastID = 0;

	private GameProfile HerobrineGameProfile = getHerobrineGameProfile();

	private GameProfile getHerobrineGameProfile() {
		GameProfile profile = new GameProfile(
											  UUID.fromString(HerobrineAI.getPluginCore().getConfigDB().HerobrineUUID),
											  HerobrineAI.getPluginCore().getConfigDB().HerobrineName
											  );
		
		Property textures = new Property("textures",
				"eyJ0aW1lc3RhbXAiOjE0MjE0ODczMzk3MTMsInByb2ZpbGVJZCI6ImY4NGM2YTc5MGE0ZTQ1ZTA4NzliY2Q0OWViZDRjNGUyIiwicHJvZmlsZU5hbWUiOiJIZXJvYnJpbmUiLCJpc1B1YmxpYyI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzk4YjdjYTNjN2QzMTRhNjFhYmVkOGZjMThkNzk3ZmMzMGI2ZWZjODQ0NTQyNWM0ZTI1MDk5N2U1MmU2Y2IifX19",
				"Edb1R3vm2NHUGyTPaOdXNQY9p5/Ez4xButUGY3tNKIJAzjJM5nQNrq54qyFhSZFVwIP6aM4Ivqmdb2AamXNeN0KgaaU/C514N+cUZNWdW5iiycPytfh7a6EsWXV4hCC9B2FoLkbXuxs/KAbKORtwNfFhQupAsmn9yP00e2c3ZQmS18LWwFg0vzFqvp4HvzJHqY/cTqUxdlSFDrQe/4rATe6Yx6v4zbZN2sHbSL+8AwlDDuP2Xr4SS6f8nABOxjSTlWMn6bToAYiymD+KUPoO0kQJ0Uw/pVXgWHYjQeM4BYf/FAxe8Bf1cP8S7VKueULkOxqIjXAp85uqKkU7dR/s4M4yHm6fhCOCLSMv6hi5ewTaFNYyhK+NXPftFqHcOxA1LbrjOe6NyphF/2FI79n90hagxJpWwNPz3/8I5rnGbYwBZPTsTnD8PszgQTNuWSuvZwGIXPIp9zb90xuU7g7VNWjzPVoOHfRNExEs7Dn9pG8CIA/m/a8koWW3pkbP/AMMWnwgHCr/peGdvF5fN+hJwVdpbfC9sJfzGwA7AgXG/6yqhl1U7YAp/aCVM9bZ94sav+kQghvN41jqOwy4F4i/swc7R4Fx2w5HFxVY3j7FChG7iuhqjUclm79YNhTG0lBQLiZbN5FmC9QgrNHRKlzgSZrXHWoG3YXFSqfn4J+Om9w=");
		
		profile.getProperties().put(textures.getName(), textures);
		
		return profile;
	}

	public NPCCore(JavaPlugin plugin) {
		
		server = BServer.getInstance();
		
		networkCore = new NetworkCore();
		
		
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(HerobrineAI.getPluginCore(), new Runnable() {
			@Override
			public void run() {
				final ArrayList<HumanNPC> toRemove = new ArrayList<HumanNPC>();
				for (final HumanNPC humanNPC : npcs) {
					final Entity entity = humanNPC.getEntity();
					if (entity.dead) {
						toRemove.add(humanNPC);
					}
				}
				for (final HumanNPC n : toRemove) {
					npcs.remove(n);
				}
			}
		}, 1L, 1L);
		
		this.HerobrineGameProfile = getHerobrineGameProfile();
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

	public HumanNPC spawnHumanNPC(String name, Location l) {
		lastID++;
		int id = lastID;
		return spawnHumanNPC(name, l, id);
	}

	public HumanNPC spawnHumanNPC(String name, Location l, int id) {

		final BWorld world = server.getWorld(l.getWorld().getName());
		final HumanEntity humanEntity = new HumanEntity(this, world, HerobrineGameProfile, new PlayerInteractManager(world.getWorldServer()));		
		humanEntity.setLocation(l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
		world.getWorldServer().addEntity(humanEntity);
		final HumanNPC humannpc = new HumanNPC(humanEntity, id);
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
