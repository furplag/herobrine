package net.theprogrammersworld.herobrine.AI.cores;

import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import net.theprogrammersworld.herobrine.Herobrine;
import net.theprogrammersworld.herobrine.Utils;
import net.theprogrammersworld.herobrine.AI.AICore;
import net.theprogrammersworld.herobrine.AI.Core;
import net.theprogrammersworld.herobrine.AI.CoreResult;

public class Graveyard extends Core {

	private List<LivingEntity> LivingEntities;
	private int ticks = 0;
	private double savedX = 0;
	private double savedY = 0;
	private double savedZ = 0;
	private World savedWorld = null;
	private Player savedPlayer = null;

	public Graveyard() {
		super(CoreType.GRAVEYARD, AppearType.APPEAR, Herobrine.getPluginCore());
	}

	public CoreResult CallCore(Object[] data) {
		return Teleport((Player) data[0]);
	}

	public CoreResult Teleport(Player player) {
		if (Herobrine.getPluginCore().getConfigDB().UseGraveyardWorld == true) {
			if (!Herobrine.getPluginCore().getAICore().checkAncientSword(player.getInventory())) {
				LivingEntities = Bukkit.getServer().getWorld("world_herobrine_graveyard").getLivingEntities();
				for (int i = 0; i <= LivingEntities.size() - 1; i++) {

					if (LivingEntities.get(i) instanceof Player || LivingEntities.get(i).getEntityId() == PluginCore.HerobrineEntityID) {
					} else {

						LivingEntities.get(i).remove();

					}

				}

				Bukkit.getServer().getWorld("world_herobrine_graveyard").setTime(15000);
				AICore.PlayerTarget = player;
				Location loc = (Location) player.getLocation();
				savedX = loc.getX();
				savedY = loc.getY();
				savedZ = loc.getZ();
				savedWorld = loc.getWorld();
				savedPlayer = player;
				loc.setWorld(Bukkit.getServer().getWorld("world_herobrine_graveyard"));
				loc.setX(-2.49);
				loc.setY(4);
				loc.setZ(10.69);
				loc.setYaw(-179.85f);
				loc.setPitch(0.44999f);
				player.teleport(loc);
				
				Start();
				
				AICore.isTarget = true;
				Bukkit.getServer().getWorld("world_herobrine_graveyard").setStorm(false);
				
				return new CoreResult(true, "Player successfully teleported!");
			} else {
				return new CoreResult(false, "Player has Ancient Sword.");
			}
		}
		return new CoreResult(false, "Graveyard world is not allowed!");
	}

	public void Start() {

		ticks = 0;
		PluginCore.HerobrineNPC.moveTo(new Location(Bukkit.getServer().getWorld("world_herobrine_graveyard"), -2.49, 4, -4.12));
		HandlerInterval();

	}

	public void HandlerInterval() {

		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(AICore.plugin, new Runnable() {
			public void run() {
				Handler();
			}
		}, 1 * 5L);
	}

	public void Handler() {

		LivingEntities = Bukkit.getServer().getWorld("world_herobrine_graveyard").getLivingEntities();
		for (int i = 0; i <= LivingEntities.size() - 1; i++) {

			if (LivingEntities.get(i) instanceof Player
					|| LivingEntities.get(i).getEntityId() == PluginCore.HerobrineEntityID) {
			} else {

				LivingEntities.get(i).remove();

			}

		}

		if (savedPlayer.isDead() == true 
			|| savedPlayer.isOnline() == false
			|| savedPlayer.getLocation().getWorld() != Bukkit.getServer().getWorld("world_herobrine_graveyard")
			|| this.ticks == 90 || AICore.isTarget == false) {
			
			if (AICore.PlayerTarget == savedPlayer) {
				Herobrine.getPluginCore().getAICore().CancelTarget(CoreType.GRAVEYARD);
			}
			
			savedPlayer.teleport(new Location(savedWorld, savedX, savedY, savedZ));

		} else {
			Location ploc = (Location) savedPlayer.getLocation();
			ploc.setY(ploc.getY() + 1.5);
			PluginCore.HerobrineNPC.lookAtPoint(ploc);
			if (ticks == 1) {
				PluginCore.HerobrineNPC.moveTo(
						new Location(Bukkit.getServer().getWorld("world_herobrine_graveyard"), -2.49, 4, -4.12));
			} else if (ticks == 40) {
				PluginCore.HerobrineNPC.moveTo(
						new Location(Bukkit.getServer().getWorld("world_herobrine_graveyard"), -2.49, 4, -0.5));
			} else if (ticks == 60) {
				PluginCore.HerobrineNPC.moveTo(
						new Location(Bukkit.getServer().getWorld("world_herobrine_graveyard"), -2.49, 4, 5.1));

			} else if (ticks == 84) {
				PluginCore.HerobrineNPC.moveTo(
						new Location(Bukkit.getServer().getWorld("world_herobrine_graveyard"), -2.49, 4, 7.5));

			}

			Random randomGen = Utils.getRandomGen();
			
			if (randomGen.nextInt(4) == 1) {
				Location newloc = new Location(Bukkit.getServer().getWorld("world_herobrine_graveyard"),
						(double) randomGen.nextInt(400), (double) Utils.getRandomGen().nextInt(20) + 20,
						(double) randomGen.nextInt(400));
				Bukkit.getServer().getWorld("world_herobrine_graveyard").strikeLightning(newloc);
			}
			ticks++;
			HandlerInterval();

		}

	}

	public Location getSavedLocation() {
		return new Location(savedWorld, savedX, savedY, savedZ);
	}

}
