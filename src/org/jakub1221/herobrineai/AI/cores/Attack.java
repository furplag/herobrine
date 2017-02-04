package org.jakub1221.herobrineai.AI.cores;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.Utils;
import org.jakub1221.herobrineai.AI.AICore;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.AI.CoreResult;
import org.jakub1221.herobrineai.AI.Message;
import org.jakub1221.herobrineai.AI.extensions.Position;

public class Attack extends Core {

	private int ticksToEnd = 0;
	private int HandlerINT = 0;
	private boolean isHandler = false;

	public Attack() {
		super(CoreType.ATTACK, AppearType.APPEAR, HerobrineAI.getPluginCore());
	}

	public CoreResult CallCore(Object[] data) {
		return setAttackTarget((Player) data[0]);
	}

	public CoreResult setAttackTarget(Player player) {
		if (!PluginCore.getAICore().checkAncientSword(player.getInventory())) {
			if (PluginCore.getSupport().checkAttack(player.getLocation())) {
				if (!PluginCore.canAttackPlayerNoMSG(player)) {
					return new CoreResult(false, "This player is protected.");
				}

				HerobrineAI.HerobrineHP = HerobrineAI.HerobrineMaxHP;
				ticksToEnd = 0;
				AICore.PlayerTarget = player;
				AICore.isTarget = true;
				AICore.log.info("[HerobrineAI] Teleporting to target. (" + AICore.PlayerTarget.getName() + ")");
				Location ploc = (Location) AICore.PlayerTarget.getLocation();
				Object[] data = { ploc };
				PluginCore.getAICore().getCore(CoreType.DESTROY_TORCHES).RunCore(data);
				if (PluginCore.getConfigDB().UsePotionEffects) {
					AICore.PlayerTarget.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 1000, 1));
					AICore.PlayerTarget.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1000, 1));
					AICore.PlayerTarget.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1000, 1));
				}
				Location tploc = (Location) Position.getTeleportPosition(ploc);

				HerobrineAI.HerobrineNPC.moveTo(tploc);

				Message.SendMessage(AICore.PlayerTarget);

				StartHandler();

				return new CoreResult(true, "Herobrine attacks " + player.getName() + "!");
			} else {
				return new CoreResult(false, "Player is in secure area.");
			}
		} else {
			return new CoreResult(false, "Player has Ancient Sword.");
		}
	}

	public void StopHandler() {
		if (isHandler) {
			Bukkit.getScheduler().cancelTask(HandlerINT);
			isHandler = false;
		}
	}

	public void StartHandler() {
		KeepLooking();
		FollowHideRepeat();
		isHandler = true;
		HandlerINT = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(AICore.plugin, new Runnable() {
			public void run() {
				Handler();
			}
		}, 1 * 5L, 1 * 5L);
	}

	private void Handler() {
		KeepLooking();
		if (ticksToEnd == 1 || ticksToEnd % 16 == 0)
			FollowHideRepeat();
		
	}

	public void KeepLooking() {
		if (AICore.PlayerTarget.isOnline() && AICore.isTarget
				&& PluginCore.getAICore().getCoreTypeNow() == CoreType.ATTACK) {
			if (AICore.PlayerTarget.isDead() == false) {
				if (ticksToEnd == 160) {
					PluginCore.getAICore().CancelTarget(CoreType.ATTACK);
				} else {
					ticksToEnd++;

					Location ploc = (Location) AICore.PlayerTarget.getLocation();
					ploc.setY(ploc.getY() + 1.5);
					HerobrineAI.HerobrineNPC.lookAtPoint(ploc);
					if (PluginCore.getConfigDB().Lighting == true) {
						int lchance = Utils.getRandomGen().nextInt(100);

						if (lchance > 75) {
							Location newloc = (Location) ploc;
							int randx = Utils.getRandomGen().nextInt(50);
							int randz = Utils.getRandomGen().nextInt(50);
							if (Utils.getRandomGen().nextBoolean()) {
								newloc.setX(newloc.getX() + randx);
							} else {
								newloc.setX(newloc.getX() - randx);
							}
							if (Utils.getRandomGen().nextBoolean()) {
								newloc.setZ(newloc.getZ() + randz);
							} else {
								newloc.setZ(newloc.getZ() - randz);
							}
							newloc.setY(250);
							newloc.getWorld().strikeLightning(newloc);

						}

					}

				}
			} else {
				PluginCore.getAICore().CancelTarget(CoreType.ATTACK);
			}
		} else {
			PluginCore.getAICore().CancelTarget(CoreType.ATTACK);
		}
	}

	public void Follow() {
		if (AICore.PlayerTarget.isOnline() 
			&& AICore.isTarget
			&& PluginCore.getAICore().getCoreTypeNow() == CoreType.ATTACK) {
			
			if (AICore.PlayerTarget.isDead() == false) {
				
				if (PluginCore.getConfigDB().useWorlds.contains(AICore.PlayerTarget.getWorld().getName())
					&& PluginCore.getSupport().checkAttack(AICore.PlayerTarget.getLocation())) {
					
					HerobrineAI.HerobrineNPC.moveTo(Position.getTeleportPosition(AICore.PlayerTarget.getLocation()));
					Location ploc = (Location) AICore.PlayerTarget.getLocation();
					ploc.setY(ploc.getY() + 1.5);
					HerobrineAI.HerobrineNPC.lookAtPoint(ploc);
					AICore.PlayerTarget.playSound(AICore.PlayerTarget.getLocation(), Sound.ENTITY_PLAYER_BREATH, 0.75f, 0.75f);
					if (PluginCore.getConfigDB().HitPlayer == true) {
						int hitchance = Utils.getRandomGen().nextInt(100);
						if (hitchance < 55) {
							AICore.PlayerTarget.playSound(AICore.PlayerTarget.getLocation(), Sound.ENTITY_PLAYER_HURT, 0.75f, 0.75f);

							AICore.PlayerTarget.damage(4);

						}
					}
				} else {
					PluginCore.getAICore().CancelTarget(CoreType.ATTACK);
				}
			} else {
				PluginCore.getAICore().CancelTarget(CoreType.ATTACK);
			}
		} else {
			PluginCore.getAICore().CancelTarget(CoreType.ATTACK);
		}

	}

	public void Hide() {
		if (AICore.PlayerTarget.isOnline() && AICore.isTarget
				&& PluginCore.getAICore().getCoreTypeNow() == CoreType.ATTACK) {
			if (AICore.PlayerTarget.isDead() == false) {

				Location ploc = (Location) AICore.PlayerTarget.getLocation();

				ploc.setY(-20);

				for(int i=0; i < 5; i++){
					for(float j=0; j < 2; j+= 0.5f){
						Location hbloc = (Location) HerobrineAI.HerobrineNPC.getBukkitEntity().getLocation();
						hbloc.setY(hbloc.getY() + j);
						hbloc.getWorld().playEffect(hbloc, Effect.SMOKE, 80);
					}
				}

				if (PluginCore.getConfigDB().SpawnBats) {
					Location hbloc = (Location) HerobrineAI.HerobrineNPC.getBukkitEntity().getLocation();			
					ploc.getWorld().spawnEntity(hbloc, EntityType.BAT);
					ploc.getWorld().spawnEntity(hbloc, EntityType.BAT);			
				}

				HerobrineAI.HerobrineNPC.moveTo(ploc);

			} else {
				PluginCore.getAICore().CancelTarget(CoreType.ATTACK);
			}
		} else {
			PluginCore.getAICore().CancelTarget(CoreType.ATTACK);
		}

	}

	public void FollowHideRepeat() {
		if (AICore.PlayerTarget.isOnline() && AICore.isTarget
				&& PluginCore.getAICore().getCoreTypeNow() == CoreType.ATTACK) {
			if (AICore.PlayerTarget.isDead() == false) {
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(AICore.plugin, new Runnable() {
					public void run() {
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(AICore.plugin, new Runnable() {
							public void run() {
								Hide();
							}
						}, 1 * 30L);
						Follow();
					}
				}, 1 * 45L);
			} else {
				PluginCore.getAICore().CancelTarget(CoreType.ATTACK);
			}
		} else {
			PluginCore.getAICore().CancelTarget(CoreType.ATTACK);
		}
	}
}
