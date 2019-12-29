package net.theprogrammersworld.herobrine.AI.cores;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

import net.theprogrammersworld.herobrine.Herobrine;
import net.theprogrammersworld.herobrine.Utils;
import net.theprogrammersworld.herobrine.AI.AICore;
import net.theprogrammersworld.herobrine.AI.Core;
import net.theprogrammersworld.herobrine.AI.CoreResult;

public class Haunt extends Core {

	private int _ticks = 0;
	private int spawnedWolves = 0;
	private int spawnedBats = 0;
	private int KL_INT = 0;
	private int PS_INT = 0;
	private boolean isHandler = false;
	private boolean isFirst = true;

	public Haunt() {
		super(CoreType.HAUNT, AppearType.APPEAR, Herobrine.getPluginCore());
	}

	public CoreResult CallCore(Object[] data) {
		return setHauntTarget((Player) data[0]);
	}

	public CoreResult setHauntTarget(Player player) {
		if (PluginCore.getSupport().checkHaunt(player.getLocation())) {
			if (!PluginCore.canAttackPlayerNoMSG(player)) {
				return new CoreResult(false, player.getDisplayName() + " cannot be attacked because they are protected.");
			}
			spawnedWolves = 0;
			spawnedBats = 0;
			_ticks = 0;
			isFirst = true;
			AICore.isTarget = true;
			AICore.PlayerTarget = player;
			AICore.log.info("[Herobrine] " + player.getDisplayName() + " is now being haunted by Herobrine.");
			Location loc = (Location) PluginCore.HerobrineNPC.getBukkitEntity().getLocation();
			loc.setY(-20);
			PluginCore.HerobrineNPC.moveTo(loc);

			StartHandler();
			return new CoreResult(true, player.getDisplayName() + " is now being haunted by Herobrine.");
		}
		return new CoreResult(false, player.getDisplayName() + " cannot be haunted because they are in a secure area.");
	}

	public void StartHandler() {
		isHandler = true;
		KL_INT = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(AICore.plugin, new Runnable() {
			public void run() {
				KeepLookingHaunt();
			}
		}, 1 * 5L, 1 * 5L);
		PS_INT = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(AICore.plugin, new Runnable() {
			public void run() {
				PlaySounds();
			}
		}, 1 * 35L, 1 * 35L);
	}

	public void StopHandler() {
		if (isHandler) {
			isHandler = false;
			Bukkit.getScheduler().cancelTask(KL_INT);
			Bukkit.getScheduler().cancelTask(PS_INT);
		}
	}

	public void PlaySounds() {
		if (AICore.PlayerTarget.isOnline() && AICore.isTarget
				&& PluginCore.getAICore().getCoreTypeNow() == CoreType.HAUNT) {
			if (AICore.PlayerTarget.isDead() == false) {
				if (_ticks > 290) {
					PluginCore.getAICore().CancelTarget(CoreType.HAUNT);
				} else {

					Object[] data = { AICore.PlayerTarget };
					PluginCore.getAICore().getCore(CoreType.SOUNDF).RunCore(data);

					Location ploc = (Location) AICore.PlayerTarget.getLocation();

					Random randxgen = Utils.getRandomGen();
					int randx = randxgen.nextInt(100);
					if (randx < 70) {
					} else if (randx < 80 && spawnedBats < 2) {
						if (PluginCore.getConfigDB().SpawnBats) {
							ploc.getWorld().spawnEntity(ploc, EntityType.BAT);
							spawnedBats++;
						}
					} else if (randx < 90 && spawnedWolves < 1) {
						if (PluginCore.getConfigDB().SpawnWolves) {
							Wolf wolf = (Wolf) ploc.getWorld().spawnEntity(ploc, EntityType.WOLF);
							wolf.setAdult();
							wolf.setAngry(true);
							spawnedWolves++;
						}
					}

					if (PluginCore.getConfigDB().Lightning == true) {

						int lchance = randxgen.nextInt(100);

						if (lchance > 75) {
							Location newloc = (Location) ploc;

							int randz = randxgen.nextInt(50);
							int randxp = randxgen.nextInt(1);
							int randzp = randxgen.nextInt(1);

							if (randxp == 1) {
								newloc.setX(newloc.getX() + randx);
							} else {
								newloc.setX(newloc.getX() - randx);
							}
							if (randzp == 1) {
								newloc.setZ(newloc.getZ() + randz);
							} else {
								newloc.setZ(newloc.getZ() - randz);
							}

							newloc.setY(250);
							newloc.getWorld().strikeLightning(newloc);

						}

					}

					if (isFirst) {
						Object[] data2 = { AICore.PlayerTarget.getLocation() };
						PluginCore.getAICore().getCore(CoreType.BUILD_CAVE).RunCore(data2);
					}
					isFirst = false;
				}
			} else {
				PluginCore.getAICore().CancelTarget(CoreType.HAUNT);
			}
		} else {
			PluginCore.getAICore().CancelTarget(CoreType.HAUNT);
		}
	}

	public void KeepLookingHaunt() {
		if (AICore.PlayerTarget.isOnline() && AICore.isTarget
				&& PluginCore.getAICore().getCoreTypeNow() == CoreType.HAUNT) {
			if (AICore.PlayerTarget.isDead() == false) {

				Location loc = (Location) PluginCore.HerobrineNPC.getBukkitEntity().getLocation();

				if (Bukkit.getServer().getOnlinePlayers().size() > 0) {
					
					
					Player player = Utils.getRandomPlayer();
					
					if(player == null)
						return;
					
					Location ploc = (Location) player.getLocation();

					if (ploc.getWorld() == loc.getWorld() 
						&& ploc.getX() + 5 > loc.getX()
						&& ploc.getX() - 5 < loc.getX() 
						&& ploc.getZ() + 5 > loc.getZ()
						&& ploc.getZ() - 5 < loc.getZ() 
						&& ploc.getY() + 5 > loc.getY()
						&& ploc.getY() - 5 < loc.getY()) {
				
						PluginCore.getAICore().DisappearEffect();

					}
					
				}

				Herobrine.HerobrineHP = Herobrine.HerobrineMaxHP;
				loc = AICore.PlayerTarget.getLocation();
				loc.setY(loc.getY() + 1.5);
				PluginCore.HerobrineNPC.lookAtPoint(loc);

				_ticks++;

				AICore _aicore = PluginCore.getAICore();

				if(_ticks % 30 == 0)
					HauntTP();
				else if(_ticks % 20 == 0)
					_aicore.DisappearEffect();
				

			} else {
				PluginCore.getAICore().CancelTarget(CoreType.HAUNT);
			}
		} else {
			PluginCore.getAICore().CancelTarget(CoreType.HAUNT);
		}
	}

	public void HauntTP() {
		if (AICore.PlayerTarget.isOnline() && AICore.isTarget
				&& PluginCore.getAICore().getCoreTypeNow() == CoreType.HAUNT) {
			if (AICore.PlayerTarget.isDead() == false) {
				if (PluginCore.getConfigDB().useWorlds
						.contains(AICore.PlayerTarget.getWorld().getName())) {

					FindAndTeleport(AICore.PlayerTarget);
					Location ploc = (Location) AICore.PlayerTarget.getLocation();
					ploc.setY(ploc.getY() + 1.5);
					PluginCore.HerobrineNPC.lookAtPoint(ploc);

				} else {
					PluginCore.getAICore().CancelTarget(CoreType.HAUNT);
				}
			} else {
				PluginCore.getAICore().CancelTarget(CoreType.HAUNT);
			}
		} else {
			PluginCore.getAICore().CancelTarget(CoreType.HAUNT);
		}

	}

	public boolean FindAndTeleport(Player player) {

		Location loc = (Location) player.getLocation();

		int x = 0;
		int z = 0;
		int y = 0;

		Random randGen = Utils.getRandomGen();
		
		int xMax = randGen.nextInt(10) + 10;
		int zMax = randGen.nextInt(10) + 10;
		int randY = randGen.nextInt(5) + 5;
		xMax = randGen.nextBoolean() ? -xMax : xMax;
		zMax = randGen.nextBoolean() ? -zMax : zMax;

		for (y = -randY; y <= randY; y++) {

			for (x = -xMax; xMax > 0 ? x <= xMax : x >= xMax; x += xMax > 0 ? 1 : -1) {
				for (z = -zMax; zMax > 0 ? z <= zMax : z >= zMax; z += zMax > 0 ? 1 : -1) {
					if (!(x >= -4 && x <= 4 && z >= -4 && z <= 4)) {
						
						Material blockBottom =  loc.getWorld().getBlockAt(
								x + loc.getBlockX(), 
								y + loc.getBlockY() - 1, 
								z + loc.getBlockZ()).getType();
						
						Material blockMiddle =  loc.getWorld().getBlockAt(
								x + loc.getBlockX(), 
								y + loc.getBlockY(), 
								z + loc.getBlockZ()).getType();
						
						Material blockTop =  loc.getWorld().getBlockAt(
								x + loc.getBlockX(), 
								y + loc.getBlockY() + 1, 
								z + loc.getBlockZ()).getType();
						
						if (blockBottom.isSolid() && blockMiddle.isSolid() && blockTop.isSolid()){
							Teleport(loc.getWorld(), x + loc.getBlockX(), y + loc.getBlockY(), z + loc.getBlockZ());								
							return true;
						}
					}
				}

			}

		}

		return false;

	}

	public void Teleport(World world, int X, int Y, int Z) {

		Location loc = (Location) PluginCore.HerobrineNPC.getBukkitEntity().getLocation();
		loc.setWorld(world);
		loc.setX((double) X);
		loc.setY((double) Y);
		loc.setZ((double) Z);
		PluginCore.HerobrineNPC.moveTo(loc);

	}

}
