package net.theprogrammersworld.herobrine.listeners;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;

import net.theprogrammersworld.herobrine.HerobrineAI;
import net.theprogrammersworld.herobrine.Utils;
import net.theprogrammersworld.herobrine.AI.*;
import net.theprogrammersworld.herobrine.AI.Core.CoreType;
import net.theprogrammersworld.herobrine.entity.MobType;
import net.theprogrammersworld.herobrine.misc.ItemName;

import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

public class EntityListener implements Listener {

	private ItemStack itemInHand = null;
	private ArrayList<String> equalsLore = new ArrayList<String>();
	private ArrayList<String> equalsLoreS = new ArrayList<String>();
	private ArrayList<String> getLore = new ArrayList<String>();
	private HerobrineAI PluginCore = null;

	public EntityListener(HerobrineAI plugin) {
		equalsLore.add("Herobrine artifact");
		equalsLore.add("Bow of Teleporting");
		equalsLoreS.add("Herobrine artifact");
		equalsLoreS.add("Sword of Lighting");
		PluginCore = plugin;
	}

	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if (!HerobrineAI.isNPCDisabled) {
			if (PluginCore.getConfigDB().useWorlds.contains(event.getEntity().getLocation().getWorld().getName())) {
				
				Entity entity = event.getEntity();
				EntityType creatureType = event.getEntityType();
				
				if (event.isCancelled())
					return;

				if (creatureType == EntityType.ZOMBIE) {
					if (PluginCore.getConfigDB().UseNPC_Warrior) {
						if (Utils.getRandomGen().nextInt(100) < PluginCore.getConfigDB().npc.getInt("npc.Warrior.SpawnChance")) {

							if (PluginCore.getEntityManager().isCustomMob(entity.getEntityId()) == false) {
								LivingEntity ent = (LivingEntity) entity;

								ent.setHealth(0);
								PluginCore.getEntityManager().spawnCustomZombie(event.getLocation(), MobType.HEROBRINE_WARRIOR);

								return;
							}
						}
					}
				} else if (creatureType == EntityType.SKELETON) {
					if (PluginCore.getConfigDB().UseNPC_Demon) {
						if (Utils.getRandomGen().nextInt(100) < PluginCore.getConfigDB().npc.getInt("npc.Demon.SpawnChance")) {

							if (PluginCore.getEntityManager().isCustomMob(entity.getEntityId()) == false) {
								LivingEntity ent = (LivingEntity) entity;

								ent.setHealth(0);
								PluginCore.getEntityManager().spawnCustomSkeleton(event.getLocation(), MobType.DEMON);

								return;
							}
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onEntityDeathEvent(EntityDeathEvent event) {
		if (PluginCore.getEntityManager().isCustomMob(event.getEntity().getEntityId())) {
			PluginCore.getEntityManager().removeMob(event.getEntity().getEntityId());
		}
	}

	@EventHandler
	public void EntityTargetEvent(EntityTargetLivingEntityEvent e) {
		LivingEntity lv = e.getTarget();
		
		if(lv == null)
			return;
		
		if (lv.getEntityId() == PluginCore.HerobrineEntityID) {
			e.setCancelled(true);
			return;
		}
	}

	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event) {
		if (event.getEntity() instanceof Arrow) {
			
			Arrow arrow = (Arrow) event.getEntity();
			if (arrow.getShooter() instanceof Player) {
				
				Player player = (Player) arrow.getShooter();
				if (player.getInventory().getItemInMainHand() != null) {
					
					itemInHand = player.getInventory().getItemInMainHand();
					if (itemInHand.getType() != null) {
						
						if (itemInHand.getType() == Material.BOW) {
							getLore = ItemName.getLore(itemInHand);
							if (getLore != null) {
								
								if (getLore.containsAll(equalsLore)) {
									
									if (PluginCore.getConfigDB().UseArtifactBow) {

										player.teleport(arrow.getLocation());
									}
								}
							}
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onEntityDamageByBlock(EntityDamageByBlockEvent event) {

		if (event.getEntity().getEntityId() == PluginCore.HerobrineEntityID) {
			event.setCancelled(true);
			event.setDamage(0);
			return;
		}

	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.getEntity().getEntityId() == PluginCore.HerobrineEntityID) {
			if (event instanceof EntityDamageByEntityEvent) {

				EntityDamageByEntityEvent dEvent = (EntityDamageByEntityEvent) event;
				if (PluginCore.getConfigDB().Killable == true
					&& PluginCore.getAICore().getCoreTypeNow() != CoreType.GRAVEYARD) {
					
					if (dEvent.getDamager() instanceof Player) {
						if (event.getDamage() >= HerobrineAI.HerobrineHP) {

							HerobrineDropItems();

							PluginCore.getAICore().CancelTarget(CoreType.ANY);
							HerobrineAI.HerobrineHP = HerobrineAI.HerobrineMaxHP;
							Player player = (Player) dEvent.getDamager();
							player.sendMessage("<Herobrine> " + PluginCore.getConfigDB().DeathMessage);

						} else {
							HerobrineAI.HerobrineHP -= event.getDamage();
							PluginCore.HerobrineNPC.HurtAnimation();
							AICore.log.info("HIT: " + event.getDamage());
						}
					} else if (dEvent.getDamager() instanceof Projectile) {

						Arrow arrow = (Arrow) dEvent.getDamager();
						if (arrow.getShooter() instanceof Player) {
							if (PluginCore.getAICore().getCoreTypeNow() == CoreType.RANDOM_POSITION) {
								PluginCore.getAICore().CancelTarget(CoreType.ANY);
								PluginCore.getAICore().setAttackTarget((Player) arrow.getShooter());
							} else {

								if (event.getDamage() >= HerobrineAI.HerobrineHP) {

									HerobrineDropItems();

									PluginCore.getAICore().CancelTarget(CoreType.ANY);
									HerobrineAI.HerobrineHP = HerobrineAI.HerobrineMaxHP;
									Player player = (Player) arrow.getShooter();
									player.sendMessage("<Herobrine> " + PluginCore.getConfigDB().DeathMessage);

								} else {
									HerobrineAI.HerobrineHP -= event.getDamage();
									PluginCore.HerobrineNPC.HurtAnimation();
									AICore.log.info("HIT: " + event.getDamage());
								}

							}
						} else {
							if (PluginCore.getAICore().getCoreTypeNow() == CoreType.RANDOM_POSITION) {
								Location newloc = (Location) PluginCore.HerobrineNPC.getBukkitEntity().getLocation();
								newloc.setY(-20);
								PluginCore.HerobrineNPC.moveTo(newloc);
								PluginCore.getAICore().CancelTarget(CoreType.ANY);
							}
						}
					} else {
						if (PluginCore.getAICore().getCoreTypeNow() == CoreType.RANDOM_POSITION) {
							Location newloc = (Location) PluginCore.HerobrineNPC.getBukkitEntity().getLocation();
							newloc.setY(-20);
							PluginCore.HerobrineNPC.moveTo(newloc);
							PluginCore.getAICore().CancelTarget(CoreType.ANY);
						}
					}
				}

			}

			event.setCancelled(true);
			event.setDamage(0);
			return;

		} else {
			if (event instanceof EntityDamageByEntityEvent) {
				EntityDamageByEntityEvent dEvent = (EntityDamageByEntityEvent) event;
				if (dEvent.getDamager() instanceof Player) {
					Player player = (Player) dEvent.getDamager();
					if (player.getInventory().getItemInMainHand() != null) {
						if (player.getInventory().getItemInMainHand().getType() == Material.DIAMOND_SWORD) {
							if (ItemName.getLore(player.getInventory().getItemInMainHand()) != null) {
								itemInHand = player.getInventory().getItemInMainHand();
								getLore = ItemName.getLore(itemInHand);
								if (getLore.containsAll(equalsLoreS)) {
									if (PluginCore.getConfigDB().UseArtifactSword) {
										if (Utils.getRandomGen().nextBoolean()) {
											player.getLocation().getWorld().strikeLightning(event.getEntity().getLocation());
										}
									}
								}
							}
						}
					}
				} else if (dEvent.getDamager() instanceof Zombie) {
					Zombie zmb = (Zombie) dEvent.getDamager();
					if (zmb.getCustomName() == "Artifact Guardian" || zmb.getCustomName() == "Herobrine�s Warrior") {

						dEvent.setDamage(dEvent.getDamage() * 3);
					}

				} else if (dEvent.getDamager() instanceof Skeleton) {
					Skeleton zmb = (Skeleton) dEvent.getDamager();
					if (zmb.getCustomName() == "Demon") {

						dEvent.setDamage(dEvent.getDamage() * 3);
					}

				}
			}
			if (event.getCause() != null) {
				if (event.getCause() == DamageCause.LIGHTNING) {
					if (event.getEntity() instanceof Player) {
						if (event.getEntity().getEntityId() != PluginCore.HerobrineEntityID) {
							Player player = (Player) event.getEntity();
							if (player.getInventory().getItemInMainHand() != null) {
								if (player.getInventory().getItemInMainHand().getType() == Material.DIAMOND_SWORD) {
									if (ItemName.getLore(player.getInventory().getItemInMainHand()) != null) {
										itemInHand = player.getInventory().getItemInMainHand();
										getLore = ItemName.getLore(itemInHand);
										if (getLore.containsAll(equalsLoreS)) {
											if (PluginCore.getConfigDB().UseArtifactSword) {
												event.setDamage(0);
												event.setCancelled(true);
												return;
											}
										}
									}
								}
							}

						}
					}
				}

			}
		}
	}
	
	protected void HerobrineDropItems(){
		Object[] items = PluginCore.getConfigDB().config.getConfigurationSection("config.Drops").getKeys(false).toArray();
		for (Object itemObj : items) {
			final String item = itemObj.toString();
			final int chance = new Random().nextInt(100);
			if (chance <= PluginCore.getConfigDB().config.getInt("config.Drops." + item + ".chance")) {
				PluginCore.HerobrineNPC.getBukkitEntity().getLocation().getWorld().dropItemNaturally(PluginCore.HerobrineNPC.getBukkitEntity()
						.getLocation(), new ItemStack(Material.matchMaterial(item), PluginCore.getConfigDB().config
								.getInt("config.Drops." + item + ".count")));
			}
		}
	}

}
