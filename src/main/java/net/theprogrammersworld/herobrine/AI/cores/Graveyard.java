package net.theprogrammersworld.herobrine.AI.cores;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
	private float savedPitch = 0;
	private float savedYaw = 0;
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
				LivingEntities = Bukkit.getServer().getWorld(Herobrine.getPluginCore().getConfigDB().HerobrineWorldName).getLivingEntities();
				for (int i = 0; i <= LivingEntities.size() - 1; i++) {

					if (LivingEntities.get(i) instanceof Player || LivingEntities.get(i).getEntityId() == PluginCore.HerobrineEntityID) {
					} else {

						LivingEntities.get(i).remove();

					}

				}

				Bukkit.getServer().getWorld(Herobrine.getPluginCore().getConfigDB().HerobrineWorldName).setTime(15000);
				AICore.PlayerTarget = player;
				Location loc = player.getLocation();
				savedX = loc.getX();
				savedY = loc.getY();
				savedZ = loc.getZ();
				savedPitch = loc.getPitch();
				savedYaw = loc.getYaw();
				savedWorld = loc.getWorld();
				savedPlayer = player;
				cachePreGraveyardPositionToDisk(loc, player);
				loc.setWorld(Bukkit.getServer().getWorld(Herobrine.getPluginCore().getConfigDB().HerobrineWorldName));
				loc.setX(-2.49);
				loc.setY(Herobrine.getPluginCore().getConfigDB().graveyardYCoord);
				loc.setZ(10.69);
				loc.setYaw(-179.85f);
				loc.setPitch(0.44999f);
				player.teleport(loc);
				
				Start();
				
				AICore.isTarget = true;
				Bukkit.getServer().getWorld(Herobrine.getPluginCore().getConfigDB().HerobrineWorldName).setStorm(false);
				
				return new CoreResult(true, player.getDisplayName() + " was successfully teleported to Herobrine's Graveyard.");
			} else {
				return new CoreResult(false, player.getDisplayName() + " cannot be attacked because they have an Ancient Sword.");
			}
		}
		return new CoreResult(false, "Herobrine's Graveyard is disabled.");
	}

	public void Start() {

		ticks = 0;
		PluginCore.HerobrineNPC.moveTo(new Location(Bukkit.getServer().getWorld(Herobrine.getPluginCore().getConfigDB().HerobrineWorldName),
				-2.49, Herobrine.getPluginCore().getConfigDB().graveyardYCoord, -4.12));
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

		LivingEntities = Bukkit.getServer().getWorld(Herobrine.getPluginCore().getConfigDB().HerobrineWorldName).getLivingEntities();
		for (int i = 0; i <= LivingEntities.size() - 1; i++) {

			if (LivingEntities.get(i) instanceof Player
					|| LivingEntities.get(i).getEntityId() == PluginCore.HerobrineEntityID) {
			} else {

				LivingEntities.get(i).remove();

			}

		}

		if (savedPlayer.isDead() == true 
			|| savedPlayer.isOnline() == false
			|| savedPlayer.getLocation().getWorld() != Bukkit.getServer().getWorld(Herobrine.getPluginCore().getConfigDB().HerobrineWorldName)
			|| this.ticks == 90 || AICore.isTarget == false) {
			
			if (AICore.PlayerTarget == savedPlayer) {
				Herobrine.getPluginCore().getAICore().CancelTarget(CoreType.GRAVEYARD);
			}
			
			// It looks like when the call to the teleport function is made here, pitch and yaw get reversed, so they are deliberately reversed here.
			// (Or maybe when the pitch and yaw values are extracted earlier, they are reversed? Not really sure.) This change was added v2.2.0 for
			// Spigot 1.17.
			savedPlayer.teleport(new Location(savedWorld, savedX, savedY, savedZ, savedYaw, savedPitch));
			deletePreGraveyardCache(savedPlayer);

		} else {
			Location ploc = (Location) savedPlayer.getLocation();
			ploc.setY(ploc.getY() + 1.5);
			PluginCore.HerobrineNPC.lookAtPoint(ploc);
			if (ticks == 1) {
				PluginCore.HerobrineNPC.moveTo(
						new Location(Bukkit.getServer().getWorld(Herobrine.getPluginCore().getConfigDB().HerobrineWorldName), -2.49,
								Herobrine.getPluginCore().getConfigDB().graveyardYCoord, -4.12));
			} else if (ticks == 40) {
				PluginCore.HerobrineNPC.moveTo(
						new Location(Bukkit.getServer().getWorld(Herobrine.getPluginCore().getConfigDB().HerobrineWorldName), -2.49,
								Herobrine.getPluginCore().getConfigDB().graveyardYCoord, -0.5));
			} else if (ticks == 60) {
				PluginCore.HerobrineNPC.moveTo(
						new Location(Bukkit.getServer().getWorld(Herobrine.getPluginCore().getConfigDB().HerobrineWorldName), -2.49,
								Herobrine.getPluginCore().getConfigDB().graveyardYCoord, 5.1));

			} else if (ticks == 84) {
				PluginCore.HerobrineNPC.moveTo(
						new Location(Bukkit.getServer().getWorld(Herobrine.getPluginCore().getConfigDB().HerobrineWorldName), -2.49,
								Herobrine.getPluginCore().getConfigDB().graveyardYCoord, 7.5));

			}

			Random randomGen = Utils.getRandomGen();
			
			if (randomGen.nextInt(4) == 1) {
				Location newloc = new Location(Bukkit.getServer().getWorld(Herobrine.getPluginCore().getConfigDB().HerobrineWorldName),
						(double) randomGen.nextInt(400), (double) Utils.getRandomGen().nextInt(20) + 20,
						(double) randomGen.nextInt(400));
				Bukkit.getServer().getWorld(Herobrine.getPluginCore().getConfigDB().HerobrineWorldName).strikeLightning(newloc);
			}
			ticks++;
			HandlerInterval();

		}

	}

	public Location getSavedLocation() {
		return new Location(savedWorld, savedX, savedY, savedZ, savedPitch, savedYaw);
	}

	private void cachePreGraveyardPositionToDisk(Location loc, Player player) {
		// Saves a cache of the given player's position prior to getting transported to the graveyard to the disk.
		try {
			FileWriter cache = new FileWriter("plugins/Herobrine/pregraveyard_caches/" + player.getUniqueId());
			cache.write(Double.toString(loc.getX()) + '\n');
			cache.write(Double.toString(loc.getY()) + '\n');
			cache.write(Double.toString(loc.getZ()) + '\n');
			cache.write(Double.toString(loc.getPitch()) + '\n');
			cache.write(Double.toString(loc.getYaw()) + '\n');
			cache.write(loc.getWorld().getName());
			cache.close();
		} catch (IOException e) {e.printStackTrace();}
	}
	
	private void deletePreGraveyardCache(Player player) {
		// Deletes the cache of the given player's position prior to getting teleported to the graveyard.
		new File("plugins/Herobrine/pregraveyard_caches/" + player.getUniqueId()).delete();
	}
	
}
