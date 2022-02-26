package net.theprogrammersworld.herobrine.AI.cores;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.theprogrammersworld.herobrine.Herobrine;
import net.theprogrammersworld.herobrine.Utils;
import net.theprogrammersworld.herobrine.AI.AICore;
import net.theprogrammersworld.herobrine.AI.Core;
import net.theprogrammersworld.herobrine.AI.CoreResult;
import net.theprogrammersworld.herobrine.AI.Message;
import net.theprogrammersworld.herobrine.AI.extensions.Position;

public class Attack extends Core {

	private int ticksToEnd = 0;
	private int HandlerINT = 0;
	private boolean isHandler = false;

	public Attack() {
		super(CoreType.ATTACK, AppearType.APPEAR, Herobrine.getPluginCore());
	}

	public CoreResult CallCore(Object[] data) {
		return setAttackTarget((Player) data[0]);
	}

	@SuppressWarnings("deprecation")
	public CoreResult setAttackTarget(Player player) {
		if (!PluginCore.getAICore().checkAncientSword(player.getInventory())) {
			if (PluginCore.getSupport().checkAttack(player.getLocation())) {
				if (!PluginCore.canAttackPlayerNoMSG(player)) {
					return new CoreResult(false, player.getDisplayName() + " cannot be attacked because they are protected.");
				}

				Herobrine.HerobrineHP = Herobrine.HerobrineMaxHP;
				ticksToEnd = 0;
				AICore.PlayerTarget = player;
				AICore.isTarget = true;
				AICore.log.info("[Herobrine] Teleporting Herobrine to " + AICore.PlayerTarget.getName() + ".");
				Location ploc = (Location) AICore.PlayerTarget.getLocation();
				Object[] data = { ploc };
				PluginCore.getAICore().getCore(CoreType.DESTROY_TORCHES).RunCore(data);
				if (PluginCore.getConfigDB().UsePotionEffects) {
					AICore.PlayerTarget.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 1000, 1));
					AICore.PlayerTarget.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1000, 1));
					AICore.PlayerTarget.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1000, 1));
				}
				Location tploc = (Location) Position.getTeleportPosition(ploc);

				PluginCore.HerobrineNPC.moveTo(tploc);

				Message.sendRandomMessage(AICore.PlayerTarget);

				StartHandler();

				return new CoreResult(true, "Herobrine is currently attacking " + player.getName() + ".");
			} else {
				return new CoreResult(false, AICore.PlayerTarget.getDisplayName() + " cannot be attacked because they are in a protected area.");
			}
		} else {
			return new CoreResult(false, player.getDisplayName() + " cannot be attacked because they have an Ancient Sword.");
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
					PluginCore.HerobrineNPC.lookAtPoint(ploc);
					if (PluginCore.getConfigDB().Lightning == true) {
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
					
					PluginCore.HerobrineNPC.moveTo(Position.getTeleportPosition(AICore.PlayerTarget.getLocation()));
					Location ploc = (Location) AICore.PlayerTarget.getLocation();
					ploc.setY(ploc.getY() + 1.5);
					PluginCore.HerobrineNPC.lookAtPoint(ploc);
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
						Location hbloc = (Location) PluginCore.HerobrineNPC.getBukkitEntity().getLocation();
						hbloc.setY(hbloc.getY() + j);
						hbloc.getWorld().playEffect(hbloc, Effect.SMOKE, 80);
					}
				}

				if (PluginCore.getConfigDB().SpawnBats) {
					Location hbloc = (Location) PluginCore.HerobrineNPC.getBukkitEntity().getLocation();			
					ploc.getWorld().spawnEntity(hbloc, EntityType.BAT);
					ploc.getWorld().spawnEntity(hbloc, EntityType.BAT);			
				}

				PluginCore.HerobrineNPC.moveTo(ploc);

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
