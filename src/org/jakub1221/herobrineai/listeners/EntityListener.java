package org.jakub1221.herobrineai.listeners;

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
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.Utils;
import org.jakub1221.herobrineai.AI.*;
import org.jakub1221.herobrineai.AI.Core.CoreType;
import org.jakub1221.herobrineai.entity.MobType;
import org.jakub1221.herobrineai.misc.ItemName;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

public class EntityListener implements Listener {

	private ItemStack itemInHand = null;
	private ArrayList<String> equalsLore = new ArrayList<String>();
	private ArrayList<String> equalsLoreS = new ArrayList<String>();
	private ArrayList<String> getLore = new ArrayList<String>();

	public EntityListener() {
		equalsLore.add("Herobrine artifact");
		equalsLore.add("Bow of Teleporting");
		equalsLoreS.add("Herobrine artifact");
		equalsLoreS.add("Sword of Lighting");
	}

	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if (!HerobrineAI.isNPCDisabled) {
			if (HerobrineAI.getPluginCore().getConfigDB().useWorlds.contains(event.getEntity().getLocation().getWorld().getName())) {
				
				Entity entity = event.getEntity();
				EntityType creatureType = event.getEntityType();
				
				if (event.isCancelled())
					return;

				if (creatureType == EntityType.ZOMBIE) {
					if (HerobrineAI.getPluginCore().getConfigDB().UseNPC_Warrior) {
						if (Utils.getRandomGen().nextInt(100) < HerobrineAI.getPluginCore().getConfigDB().npc.getInt("npc.Warrior.SpawnChance")) {

							if (HerobrineAI.getPluginCore().getEntityManager().isCustomMob(entity.getEntityId()) == false) {
								LivingEntity ent = (LivingEntity) entity;

								ent.setHealth(0);
								HerobrineAI.getPluginCore().getEntityManager().spawnCustomZombie(event.getLocation(), MobType.HEROBRINE_WARRIOR);

								return;
							}
						}
					}
				} else if (creatureType == EntityType.SKELETON) {
					if (HerobrineAI.getPluginCore().getConfigDB().UseNPC_Demon) {
						if (Utils.getRandomGen().nextInt(100) < HerobrineAI.getPluginCore().getConfigDB().npc.getInt("npc.Demon.SpawnChance")) {

							if (HerobrineAI.getPluginCore().getEntityManager().isCustomMob(entity.getEntityId()) == false) {
								LivingEntity ent = (LivingEntity) entity;

								ent.setHealth(0);
								HerobrineAI.getPluginCore().getEntityManager().spawnCustomSkeleton(event.getLocation(), MobType.DEMON);

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
		if (HerobrineAI.getPluginCore().getEntityManager().isCustomMob(event.getEntity().getEntityId())) {
			HerobrineAI.getPluginCore().getEntityManager().removeMob(event.getEntity().getEntityId());
		}
	}

	@EventHandler
	public void EntityTargetEvent(EntityTargetLivingEntityEvent e) {
		LivingEntity lv = e.getTarget();
		if (lv.getEntityId() == HerobrineAI.HerobrineEntityID) {
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
				if (player.getItemInHand() != null) {
					
					itemInHand = player.getItemInHand();
					if (itemInHand.getType() != null) {
						
						if (itemInHand.getType() == Material.BOW) {
							getLore = ItemName.getLore(itemInHand);
							if (getLore != null) {
								
								if (getLore.containsAll(equalsLore)) {
									
									if (HerobrineAI.getPluginCore().getConfigDB().UseArtifactBow) {

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

		if (event.getEntity().getEntityId() == HerobrineAI.HerobrineEntityID) {
			event.setCancelled(true);
			event.setDamage(0);
			return;
		}

	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.getEntity().getEntityId() == HerobrineAI.HerobrineEntityID) {
			if (event instanceof EntityDamageByEntityEvent) {

				EntityDamageByEntityEvent dEvent = (EntityDamageByEntityEvent) event;
				if (HerobrineAI.getPluginCore().getConfigDB().Killable == true
					&& HerobrineAI.getPluginCore().getAICore().getCoreTypeNow() != CoreType.GRAVEYARD) {
					
					if (dEvent.getDamager() instanceof Player) {
						if (event.getDamage() >= HerobrineAI.HerobrineHP) {

							HerobrineDropItems();

							HerobrineAI.getPluginCore().getAICore().CancelTarget(CoreType.ANY);
							HerobrineAI.HerobrineHP = HerobrineAI.HerobrineMaxHP;
							Player player = (Player) dEvent.getDamager();
							player.sendMessage("<Herobrine> " + HerobrineAI.getPluginCore().getConfigDB().DeathMessage);

						} else {
							HerobrineAI.HerobrineHP -= event.getDamage();
							HerobrineAI.HerobrineNPC.HurtAnimation();
							AICore.log.info("HIT: " + event.getDamage());
						}
					} else if (dEvent.getDamager() instanceof Projectile) {

						Arrow arrow = (Arrow) dEvent.getDamager();
						if (arrow.getShooter() instanceof Player) {
							if (HerobrineAI.getPluginCore().getAICore().getCoreTypeNow() == CoreType.RANDOM_POSITION) {
								HerobrineAI.getPluginCore().getAICore().CancelTarget(CoreType.ANY);
								HerobrineAI.getPluginCore().getAICore().setAttackTarget((Player) arrow.getShooter());
							} else {

								if (event.getDamage() >= HerobrineAI.HerobrineHP) {

									HerobrineDropItems();

									HerobrineAI.getPluginCore().getAICore().CancelTarget(CoreType.ANY);
									HerobrineAI.HerobrineHP = HerobrineAI.HerobrineMaxHP;
									Player player = (Player) arrow.getShooter();
									player.sendMessage("<Herobrine> " + HerobrineAI.getPluginCore().getConfigDB().DeathMessage);

								} else {
									HerobrineAI.HerobrineHP -= event.getDamage();
									HerobrineAI.HerobrineNPC.HurtAnimation();
									AICore.log.info("HIT: " + event.getDamage());
								}

							}
						} else {
							if (HerobrineAI.getPluginCore().getAICore().getCoreTypeNow() == CoreType.RANDOM_POSITION) {
								Location newloc = (Location) HerobrineAI.HerobrineNPC.getBukkitEntity().getLocation();
								newloc.setY(-20);
								HerobrineAI.HerobrineNPC.moveTo(newloc);
								HerobrineAI.getPluginCore().getAICore().CancelTarget(CoreType.ANY);
							}
						}
					} else {
						if (HerobrineAI.getPluginCore().getAICore().getCoreTypeNow() == CoreType.RANDOM_POSITION) {
							Location newloc = (Location) HerobrineAI.HerobrineNPC.getBukkitEntity().getLocation();
							newloc.setY(-20);
							HerobrineAI.HerobrineNPC.moveTo(newloc);
							HerobrineAI.getPluginCore().getAICore().CancelTarget(CoreType.ANY);
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
					if (player.getItemInHand() != null) {
						if (player.getItemInHand().getType() == Material.DIAMOND_SWORD) {
							if (ItemName.getLore(player.getItemInHand()) != null) {
								itemInHand = player.getItemInHand();
								getLore = ItemName.getLore(itemInHand);
								if (getLore.containsAll(equalsLoreS)) {
									if (HerobrineAI.getPluginCore().getConfigDB().UseArtifactSword) {
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
						if (event.getEntity().getEntityId() != HerobrineAI.HerobrineEntityID) {
							Player player = (Player) event.getEntity();
							if (player.getItemInHand() != null) {
								if (player.getItemInHand().getType() == Material.DIAMOND_SWORD) {
									if (ItemName.getLore(player.getItemInHand()) != null) {
										itemInHand = player.getItemInHand();
										getLore = ItemName.getLore(itemInHand);
										if (getLore.containsAll(equalsLoreS)) {
											if (HerobrineAI.getPluginCore().getConfigDB().UseArtifactSword) {
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
		for (int i = 1; i <= 2500; i++) {
			if (HerobrineAI.getPluginCore().getConfigDB().config.contains("config.Drops." + Integer.toString(i)) == true) {
				
				Random randgen = Utils.getRandomGen();
				
				int chance = randgen.nextInt(100);
				
				int requiredRoll = HerobrineAI.getPluginCore().getConfigDB().config.getInt("config.Drops." + Integer.toString(i) + ".chance");
				
				if (chance <= requiredRoll) {
						
					int itsAmount = HerobrineAI.getPluginCore().getConfigDB().config.getInt("config.Drops." + Integer.toString(i)+ ".count");
					
					ItemStack its = new ItemStack(Material.getMaterial(i), itsAmount);
					
					HerobrineAI.HerobrineNPC.getBukkitEntity().getLocation().getWorld().dropItemNaturally(
									HerobrineAI.HerobrineNPC.getBukkitEntity().getLocation(),
									its);
				}
			}
		}
	}

}
