package net.theprogrammersworld.herobrine.listeners;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Jukebox;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket.Action;
import net.theprogrammersworld.herobrine.Herobrine;
import net.theprogrammersworld.herobrine.Utils;
import net.theprogrammersworld.herobrine.AI.AICore;
import net.theprogrammersworld.herobrine.AI.Core.CoreType;
import net.theprogrammersworld.herobrine.misc.ItemName;

public class PlayerListener implements Listener {

	private ArrayList<String> equalsLoreS = new ArrayList<String>();
	private ArrayList<String> equalsLoreA = new ArrayList<String>();
	private ArrayList<LivingEntity> LivingEntities = new ArrayList<LivingEntity>();
	private Location le_loc = null;
	private Location p_loc = null;
	private long timestamp = 0;
	private boolean canUse = false;
	private Herobrine PluginCore = null;

	public PlayerListener(Herobrine plugin) {
		equalsLoreS.add("Herobrine artifact");
		equalsLoreS.add("Sword of Lightning");
		equalsLoreA.add("Herobrine artifact");
		equalsLoreA.add("Apple of Death");
		PluginCore = plugin;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		// If the persistent tab list entry for Herobrine is enabled, send an "add player" packet to the user on login.
		if(Herobrine.getPluginCore().getConfigDB().ShowInTabList)
			((CraftPlayer) event.getPlayer()).getHandle().connection.send(
					new ClientboundPlayerInfoUpdatePacket(Action.ADD_PLAYER, Herobrine.getPluginCore().HerobrineNPC.getEntity()));

		// Check if the user has a Graveyard cache. If they do, this means they are stuck in the Graveyard and
		// need teleported out.
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(PluginCore, new Runnable() {
			@Override
			public void run() {
				String graveyardCachePath = "plugins/Herobrine/pregraveyard_caches/" + event.getPlayer().getUniqueId();
				if(new File(graveyardCachePath).exists()) {
					try {
						FileReader cache = new FileReader(graveyardCachePath);
						String cacheDataString = "";
						int charVal = cache.read();
						while(charVal != -1) {
							cacheDataString += (char) charVal;
							charVal = cache.read();
						}
						cache.close();
						String[] cacheData = cacheDataString.split("\n");

						// If the cacheData length is 4, then the cache is from a version of the plugin prior to 2.2.0 and only contains the
						// player's (X, Y, Z) coordinates and the world. Otherwise, the cache also contains the player's pitch and yaw. Parse
						// the data appropriately and teleport them.
						Location tpDest = new Location(null, Double.parseDouble(cacheData[0]), Double.parseDouble(cacheData[1]), Double.parseDouble(cacheData[2]));
						if (cacheData.length == 4) {
							tpDest.setWorld(Bukkit.getServer().getWorld(cacheData[3]));
						}
						else {
							tpDest.setPitch(Float.parseFloat(cacheData[3]));
							tpDest.setYaw(Float.parseFloat(cacheData[4]));
							tpDest.setWorld(Bukkit.getServer().getWorld(cacheData[5]));
						}
						event.getPlayer().teleport(tpDest);

						new File(graveyardCachePath).delete();
					} catch (FileNotFoundException e) {e.printStackTrace();}
					catch (IOException e) {e.printStackTrace();}
				}
			}
		}, 20L); // 20L = 1 sec

		// If a newer version of Herobrine is available and the player is an OP, display a message to the OP stating that a new version is available.
		if(Herobrine.getPluginCore().getConfigDB().newVersionFound && event.getPlayer().isOp())
			event.getPlayer().sendMessage(ChatColor.RED + "A new version of Herobrine is available. To "
					+ "get it, go to www.theprogrammersworld.net/Herobrine and click \"Download\".");
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {

		if (event.getAction() == org.bukkit.event.block.Action.LEFT_CLICK_BLOCK || event.getAction() == org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK) {
			if (event.getClickedBlock() != null && event.getPlayer().getInventory().getItemInMainHand() != null) {

				ItemStack itemInHand = event.getPlayer().getInventory().getItemInMainHand();
				if (event.getPlayer().getInventory().getItemInMainHand().getType() != null) {

					if (itemInHand.getType() == Material.DIAMOND_SWORD
							|| itemInHand.getType() == Material.GOLDEN_APPLE) {

						if (ItemName.getLore(itemInHand) != null) {

							if (ItemName.getLore(itemInHand).containsAll(equalsLoreS)
								&& PluginCore.getConfigDB().UseArtifactSword) {

								if (Utils.getRandomGen().nextBoolean()) {
									event.getPlayer().getLocation().getWorld()
											.strikeLightning(event.getClickedBlock().getLocation());
								}

							} else if (ItemName.getLore(itemInHand).containsAll(equalsLoreA)
									   && PluginCore.getConfigDB().UseArtifactApple) {

								timestamp = System.currentTimeMillis() / 1000;
								canUse = false;

								if (PluginCore.PlayerApple.containsKey(event.getPlayer())) {
									if (PluginCore.PlayerApple.get(event.getPlayer()) < timestamp) {
										PluginCore.PlayerApple.remove(event.getPlayer());
										canUse = true;
									} else {
										canUse = false;
									}
								} else {
									canUse = true;
								}

								if (canUse == true) {

									event.getPlayer().getWorld().createExplosion(event.getPlayer().getLocation(), 0F);
									LivingEntities = (ArrayList<LivingEntity>) event.getPlayer().getLocation()
											.getWorld().getLivingEntities();
									PluginCore.PlayerApple.put(event.getPlayer(), timestamp + 60);

									for (int i = 0; i <= LivingEntities.size() - 1; i++) {

										if (LivingEntities.get(i) instanceof Player || LivingEntities.get(i)
												.getEntityId() == PluginCore.HerobrineEntityID) {
										} else {
											le_loc = LivingEntities.get(i).getLocation();
											p_loc = event.getPlayer().getLocation();

											if (le_loc.getBlockX() < p_loc.getBlockX() + 20
												&& le_loc.getBlockX() > p_loc.getBlockX() - 20) {

												if (le_loc.getBlockY() < p_loc.getBlockY() + 10
													&& le_loc.getBlockY() > p_loc.getBlockY() - 10) {

													if (le_loc.getBlockZ() < p_loc.getBlockZ() + 20
														&& le_loc.getBlockZ() > p_loc.getBlockZ() - 20) {

														event.getPlayer().getWorld().createExplosion(LivingEntities.get(i).getLocation(), 0F);
														LivingEntities.get(i).damage(10000);
													}
												}
											}
										}
									}
								} else {
									event.getPlayer().sendMessage(ChatColor.RED + "Apple of Death is recharging.");
								}
							}
						}
					}
				}
			}
		}

		if (event.getClickedBlock() != null) {
			if (event.getPlayer().getInventory().getItemInMainHand() != null) {
				if (event.getClickedBlock().getType() == Material.JUKEBOX) {

					ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
					Jukebox block = (Jukebox) event.getClickedBlock().getState();

					if (!block.isPlaying()) {
						if (item.getType() == Material.MUSIC_DISC_11) {

							PluginCore.getAICore();

							if (!AICore.isDiscCalled) {

								final Player player = event.getPlayer();
								PluginCore.getAICore();
								AICore.isDiscCalled = true;
								PluginCore.getAICore().CancelTarget(CoreType.ANY);

								Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(AICore.plugin,
										new Runnable() {
											public void run() {
												PluginCore.getAICore().callByDisc(player);
											}
										}, 1 * 50L);
							}
						}
					}
				}
			}
		}

	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onPlayerEnterBed(PlayerBedEnterEvent event) {
		PlayerBedEnterEvent.BedEnterResult result = event.getBedEnterResult();
		if (!result.equals(PlayerBedEnterEvent.BedEnterResult.OK)) {
			return;
		}

		if (Utils.getRandomGen().nextInt(100) > 75) {
			Player player = event.getPlayer();
			event.setCancelled(true);
			PluginCore.getAICore().playerBedEnter(player);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerQuit(PlayerQuitEvent event) {
		if (event.getPlayer().getEntityId() != PluginCore.HerobrineEntityID) {

			if (AICore.PlayerTarget == event.getPlayer()
					&& PluginCore.getAICore().getCoreTypeNow() == CoreType.GRAVEYARD
					&& event.getPlayer().getLocation().getWorld() == Bukkit.getServer()
							.getWorld(Herobrine.getPluginCore().getConfigDB().HerobrineWorldName)
					&& AICore.isTarget) {

				if (Utils.getRandomGen().nextBoolean()) {
					event.getPlayer()
							.teleport(PluginCore.getAICore().getGraveyard().getSavedLocation());
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerKick(PlayerKickEvent event) {
		if (event.getPlayer().getEntityId() == PluginCore.HerobrineEntityID) {
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerTeleport(PlayerTeleportEvent event) {

		if (event.getPlayer().getEntityId() == PluginCore.HerobrineEntityID) {
			if (event.getFrom().getWorld() != event.getTo().getWorld()) {
				PluginCore.HerobrineRemove();
				PluginCore.HerobrineSpawn(event.getTo());
				event.setCancelled(true);
				return;
			}

			if (PluginCore.getAICore().getCoreTypeNow() == CoreType.RANDOM_POSITION) {

				Location herobrineLocation = PluginCore.HerobrineNPC.getEntity().getBukkitEntity().getLocation();

				if (herobrineLocation.getBlockX() > PluginCore.getConfigDB().WalkingModeXRadius
					&& herobrineLocation.getBlockX() < -PluginCore.getConfigDB().WalkingModeXRadius
					&& herobrineLocation.getBlockZ() > PluginCore.getConfigDB().WalkingModeZRadius
					&& herobrineLocation.getBlockZ() < -PluginCore.getConfigDB().WalkingModeZRadius) {

					PluginCore.getAICore().CancelTarget(CoreType.RANDOM_POSITION);
					PluginCore.HerobrineNPC.moveTo(new Location(Bukkit.getServer().getWorlds().get(0), 0, -20, 0));

				}
			}
		}
	}

	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent event) {
		if (event.getEntity().getEntityId() == PluginCore.HerobrineEntityID) {
			event.setDeathMessage("");

			PluginCore.HerobrineRemove();

			Location nowloc = new Location((World) Bukkit.getServer().getWorlds().get(0), 0, -20.f, 0);
			nowloc.setYaw(1.f);
			nowloc.setPitch(1.f);
			PluginCore.HerobrineSpawn(nowloc);
		}
	}

	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent event) {
		// Dynamically toggle Herobrine's visibility to players as a workaround to the persistent tab list entry if the persistent entry is disabled.
		if(!Herobrine.getPluginCore().getConfigDB().ShowInTabList)
			PluginCore.getAICore().toggleHerobrinePlayerVisibility(event.getPlayer());

		// Prevent player from moving when in Herobrine's Graveyard.
		if (event.getPlayer().getEntityId() != PluginCore.HerobrineEntityID) {
			if (event.getPlayer().getWorld() == Bukkit.getServer().getWorld(Herobrine.getPluginCore().getConfigDB().HerobrineWorldName)) {
				Player player = (Player) event.getPlayer();
				player.teleport(new Location(Bukkit.getServer().getWorld(Herobrine.getPluginCore().getConfigDB().HerobrineWorldName), -2.49f,
						Herobrine.getPluginCore().getConfigDB().graveyardYCoord, 10.69f, -179.85f, 0.44999f));
			}
		}

	}

}
