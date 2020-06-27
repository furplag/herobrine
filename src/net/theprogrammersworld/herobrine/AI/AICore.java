package net.theprogrammersworld.herobrine.AI;

import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_16_R1.EntityPlayer;
import net.minecraft.server.v1_16_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_16_R1.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.theprogrammersworld.herobrine.Herobrine;
import net.theprogrammersworld.herobrine.Utils;
import net.theprogrammersworld.herobrine.AI.Core.CoreType;
import net.theprogrammersworld.herobrine.AI.cores.Attack;
import net.theprogrammersworld.herobrine.AI.cores.Book;
import net.theprogrammersworld.herobrine.AI.cores.BuildCave;
import net.theprogrammersworld.herobrine.AI.cores.Burn;
import net.theprogrammersworld.herobrine.AI.cores.BuryPlayer;
import net.theprogrammersworld.herobrine.AI.cores.Curse;
import net.theprogrammersworld.herobrine.AI.cores.DestroyTorches;
import net.theprogrammersworld.herobrine.AI.cores.Graveyard;
import net.theprogrammersworld.herobrine.AI.cores.Haunt;
import net.theprogrammersworld.herobrine.AI.cores.Heads;
import net.theprogrammersworld.herobrine.AI.cores.Pyramid;
import net.theprogrammersworld.herobrine.AI.cores.RandomExplosion;
import net.theprogrammersworld.herobrine.AI.cores.RandomPosition;
import net.theprogrammersworld.herobrine.AI.cores.RandomSound;
import net.theprogrammersworld.herobrine.AI.cores.Signs;
import net.theprogrammersworld.herobrine.AI.cores.SoundF;
import net.theprogrammersworld.herobrine.AI.cores.Temple;
import net.theprogrammersworld.herobrine.AI.cores.Totem;
import net.theprogrammersworld.herobrine.entity.MobType;
import net.theprogrammersworld.herobrine.misc.ItemName;

public class AICore {

	public static ConsoleLogger log = new ConsoleLogger();

	private ArrayList<Core> AllCores = new ArrayList<Core>();
	private CoreType CoreNow = CoreType.ANY;
	public static Herobrine plugin;
	public static Player PlayerTarget;
	public static boolean isTarget = false;
	public static int ticksToEnd = 0;
	public static boolean isDiscCalled = false;
	public static boolean isTotemCalled = false;
	public static int _ticks = 0;
	private ResetLimits resetLimits = null;
	private boolean BuildINT = false;
	private boolean MainINT = false;
	private boolean RandomPositionINT = false;
	private boolean RandomMoveINT = false;
	private boolean RandomSeeINT = false;
	private boolean CheckGravityINT = false;
	private boolean RandomCoreINT = false;
	private int RP_INT = 0;
	private int RM_INT = 0;
	private int RS_INT = 0;
	private int CG_INT = 0;
	private int MAIN_INT = 0;
	private int BD_INT = 0;
	private int RC_INT = 0;
	private HashSet<Player> visibilityList = new HashSet<>();

	public Core getCore(CoreType type) {
		for (Core c : AllCores) {
			if (c.getCoreType() == type) {
				return c;
			}
		}
		return null;
	}

	public AICore() {

		/* Cores init */
		AllCores.add(new Attack());
		AllCores.add(new Book());
		AllCores.add(new BuildCave());
		AllCores.add(new BuryPlayer());
		AllCores.add(new DestroyTorches());
		AllCores.add(new Graveyard());
		AllCores.add(new Haunt());
		AllCores.add(new Pyramid());
		AllCores.add(new RandomPosition());
		AllCores.add(new Signs());
		AllCores.add(new SoundF());
		AllCores.add(new Temple());
		AllCores.add(new Totem());
		AllCores.add(new Heads());
		AllCores.add(new RandomSound());
		AllCores.add(new RandomExplosion());
		AllCores.add(new Burn());
		AllCores.add(new Curse());

		resetLimits = new ResetLimits();

		plugin = Herobrine.getPluginCore();
		log.info("[Herobrine] Herobrine is now running in debug mode.");
		FindPlayer();
		StartIntervals();

	}

	public Graveyard getGraveyard() {
		return ((Graveyard) getCore(CoreType.GRAVEYARD));
	}

	public RandomPosition getRandomPosition() {
		return ((RandomPosition) getCore(CoreType.RANDOM_POSITION));
	}

	public void setCoreTypeNow(CoreType c) {
		CoreNow = c;
	}

	public CoreType getCoreTypeNow() {
		return CoreNow;
	}

	public ResetLimits getResetLimits() {
		return resetLimits;
	}

	public void disableAll() {

		resetLimits.disable();

	}

	public static String getStringWalkingMode() {

		String result = "";

		if (Herobrine.getPluginCore().getAICore().getCoreTypeNow() == CoreType.RANDOM_POSITION) {
			result = "Yes";
		} else {
			result = "No";
		}

		return result;

	}

	public void playerBedEnter(Player player) {
		int chance = Utils.getRandomGen().nextInt(100);
		if (chance < 25) {
			GraveyardTeleport(player);
		} else if (chance < 50) {
			setHauntTarget(player);
		} else if (Herobrine.getPluginCore().getConfigDB().SpawnDemonsOnPlayerBedEnter && Herobrine.getPluginCore().getConfigDB().UseNPC_Demon
				&& !Herobrine.isNPCDisabled) {
			Herobrine.getPluginCore().getEntityManager().spawnCustomSkeleton(player.getLocation(), MobType.DEMON);
		}
	}

	public void FindPlayer() {
		if (Herobrine.getPluginCore().getConfigDB().OnlyWalkingMode == false) {

			if (isTarget == false) {

				int att_chance = Utils.getRandomGen().nextInt(100);
				log.info("[Herobrine] Generating find chance...");

				if (att_chance - (Herobrine.getPluginCore().getConfigDB().ShowRate * 4) < 55) {

					if (Bukkit.getServer().getOnlinePlayers().size() > 0) {

						log.info("[Herobrine] Finding target...");
						Player targetPlayer = Utils.getRandomPlayer();

						if (targetPlayer.getEntityId() != Herobrine.getPluginCore().HerobrineEntityID) {

							if (Herobrine.getPluginCore().getConfigDB().useWorlds
									.contains(targetPlayer.getLocation().getWorld().getName())
									&& Herobrine.getPluginCore().canAttackPlayerNoMSG(targetPlayer)) {

								CancelTarget(CoreType.ANY);
								isTarget = true;
								log.info("[Herobrine] Target found. Starting AI now. (" + targetPlayer.getName()+ ")");
								setCoreTypeNow(CoreType.START);
								StartAI();

							} else {
								log.info("[Herobrine] Target is in a safe world. ("+ targetPlayer.getLocation().getWorld().getName() + ")");
								FindPlayer();
							}

						}

					}

				}

			}
		}
	}

	public void CancelTarget(CoreType coreType) {

		if (coreType == CoreNow || coreType == CoreType.ANY) {

			if (CoreNow == CoreType.RANDOM_POSITION) {
				Stop_RM();
				Stop_RS();
				Stop_CG();
				Location nowloc = new Location((World) Bukkit.getServer().getWorlds().get(0), 0, -20.f, 0);
				
				nowloc.setYaw(1.f);
				nowloc.setPitch(1.f);
				
				Herobrine.getPluginCore().HerobrineNPC.moveTo(nowloc);
				CoreNow = CoreType.ANY;
				Herobrine.getPluginCore().getPathManager().setPath(null);
			}

			if (isTarget == true) {
				if (CoreNow == CoreType.ATTACK) {
					((Attack) getCore(CoreType.ATTACK)).StopHandler();
				}
				if (CoreNow == CoreType.HAUNT) {
					((Haunt) getCore(CoreType.HAUNT)).StopHandler();
				}

				_ticks = 0;
				isTarget = false;
				Herobrine.HerobrineHP = Herobrine.HerobrineMaxHP;
				
				log.info("[Herobrine] Cancelled target.");
				Location nowloc = new Location((World) Bukkit.getServer().getWorlds().get(0), 0, -20.f, 0);
				
				nowloc.setYaw(1.f);
				nowloc.setPitch(1.f);
				
				Herobrine.getPluginCore().HerobrineNPC.moveTo(nowloc);
				CoreNow = CoreType.ANY;
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(AICore.plugin, new Runnable() {
					public void run() {
						FindPlayer();
					}
				}, (6 / Herobrine.getPluginCore().getConfigDB().ShowRate) * (Herobrine.getPluginCore().getConfigDB().ShowInterval * 1L));

			}
		}
	}

	public void StartAI() {
		if (PlayerTarget.isOnline() && isTarget) {
			if (PlayerTarget.isDead() == false) {
				Object[] data = { PlayerTarget };
				int chance = Utils.getRandomGen().nextInt(100);
				if (chance <= 10) {
					if (Herobrine.getPluginCore().getConfigDB().UseGraveyardWorld == true) {
						log.info("[Herobrine] Teleporting " + PlayerTarget.getDisplayName() + " to Herobrine's Graveyard.");

						getCore(CoreType.GRAVEYARD).RunCore(data);

					}
				} else if (chance <= 25) {

					getCore(CoreType.ATTACK).RunCore(data);
				} else {
					getCore(CoreType.HAUNT).RunCore(data);
				}
			} else {
				CancelTarget(CoreType.START);
			}
		} else {
			CancelTarget(CoreType.START);
		}
	}

	public CoreResult setAttackTarget(Player player) {
		Object[] data = { player };
		return getCore(CoreType.ATTACK).RunCore(data);
	}

	public CoreResult setHauntTarget(Player player) {
		Object[] data = { player };
		return getCore(CoreType.HAUNT).RunCore(data);
	}

	public void GraveyardTeleport(final Player player) {

		if (player.isOnline()) {
			CancelTarget(CoreType.ANY);

			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(AICore.plugin, new Runnable() {
				public void run() {
					Object[] data = { player };
					getCore(CoreType.GRAVEYARD).RunCore(data);
				}
			}, 1 * 10L);

		}

	}

	public void PlayerCallTotem(Player player) {
		final String playername = player.getName();
		final Location loc = (Location) player.getLocation();
		isTotemCalled = true;
		CancelTarget(CoreType.ANY);
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(AICore.plugin, new Runnable() {
			public void run() {
				CancelTarget(CoreType.ANY);
				Object[] data = { loc, playername };
				getCore(CoreType.TOTEM).RunCore(data);
			}
		}, 1 * 40L);
	}

	private void RandomPositionInterval() {
		if (CoreNow == CoreType.ANY) {
			((RandomPosition) getCore(CoreType.RANDOM_POSITION)).setRandomTicks(0);
			int count = Herobrine.getPluginCore().getConfigDB().useWorlds.size();
			int chance = Utils.getRandomGen().nextInt(count);
			Object[] data = {
					Bukkit.getServer().getWorld(Herobrine.getPluginCore().getConfigDB().useWorlds.get(chance)) };
			getCore(CoreType.RANDOM_POSITION).RunCore(data);

		}
	}

	private void CheckGravityInterval() {
		if (this.CoreNow == CoreType.RANDOM_POSITION) {
			((RandomPosition) getCore(CoreType.RANDOM_POSITION)).CheckGravity();
		}

	}

	private void RandomMoveInterval() {
		((RandomPosition) getCore(CoreType.RANDOM_POSITION)).RandomMove();

	}

	private void RandomSeeInterval() {
		if (CoreNow == CoreType.RANDOM_POSITION) {
			((RandomPosition) getCore(CoreType.RANDOM_POSITION)).CheckPlayerPosition();
		}

	}

	private void PyramidInterval() {

		if (Utils.getRandomGen().nextBoolean()) {
			if (Bukkit.getServer().getOnlinePlayers().size() > 0) {
				log.info("[Herobrine] Finding pyramid target...");
				
				Player player = Utils.getRandomPlayer();
				if (Herobrine.getPluginCore().getConfigDB().useWorlds.contains(player.getLocation().getWorld().getName())) {

					int chance2 = Utils.getRandomGen().nextInt(100);
					if (chance2 < 30) {
						if (Herobrine.getPluginCore().getConfigDB().BuildPyramids == true) {
							Object[] data = { player };
							getCore(CoreType.PYRAMID).RunCore(data);
						}
					} else if (chance2 < 70) {
						if (Herobrine.getPluginCore().getConfigDB().BuryPlayers) {
							Object[] data = { player };
							getCore(CoreType.BURY_PLAYER).RunCore(data);
						}
					} else {
						if (Herobrine.getPluginCore().getConfigDB().UseHeads) {
							Object[] data = { player.getName() };
							getCore(CoreType.HEADS).RunCore(data);
						}
					}
				}
			}

		}

	}

	private void TempleInterval() {
		if (Herobrine.getPluginCore().getConfigDB().BuildTemples == true) {
			if (Utils.getRandomGen().nextBoolean()) {
				if (Bukkit.getServer().getOnlinePlayers().size() > 0) {
					log.info("[Herobrine] Finding temple target...");
					
					Player player = Utils.getRandomPlayer();
					
					if (Herobrine.getPluginCore().getConfigDB().useWorlds.contains(player.getLocation().getWorld().getName())) {
						if (Utils.getRandomGen().nextBoolean()) {
							Object[] data = { player };
							getCore(CoreType.TEMPLE).RunCore(data);

						}
					}
				}
			}
		}

	}

	private void BuildCave() {
		if (Herobrine.getPluginCore().getConfigDB().BuildStuff == true) {
			if (Utils.getRandomGen().nextBoolean()) {
				if (Bukkit.getServer().getOnlinePlayers().size() > 0) {
					
					Player player = Utils.getRandomPlayer();
					
					if (Herobrine.getPluginCore().getConfigDB().useWorlds
							.contains(player.getLocation().getWorld().getName())) {

						if (Utils.getRandomGen().nextBoolean()) {
							Object[] data = { player.getLocation() };
							getCore(CoreType.BUILD_CAVE).RunCore(data);

						}
					}
				}
			}
		}
	}

	public void callByDisc(Player player) {
		isDiscCalled = false;
		if (player.isOnline()) {
			CancelTarget(CoreType.ANY);
			setHauntTarget(player);
		}
	}

	public void RandomCoreINT() {

		if (Utils.getRandomGen().nextBoolean()) {
			if (Bukkit.getServer().getOnlinePlayers().size() > 0) {

				Player player = Utils.getRandomPlayer();

				if (player.getEntityId() != Herobrine.getPluginCore().HerobrineEntityID) {
					if (Herobrine.getPluginCore().getConfigDB().useWorlds
							.contains(player.getLocation().getWorld().getName())) {
						Object[] data = { player };
						if (Herobrine.getPluginCore().canAttackPlayerNoMSG(player)) {
							if (Utils.getRandomGen().nextInt(100) < 30) {

								getCore(CoreType.RANDOM_SOUND).RunCore(data);
							} else if (Utils.getRandomGen().nextInt(100) < 60) {
								if (Herobrine.getPluginCore().getConfigDB().Burn) {
									getCore(CoreType.BURN).RunCore(data);
								}
							} else if (Utils.getRandomGen().nextInt(100) < 80) {
								if (Herobrine.getPluginCore().getConfigDB().Curse) {
									getCore(CoreType.CURSE).RunCore(data);
								}
							} else {

								getCore(CoreType.RANDOM_EXPLOSION).RunCore(data);
							}
						}
					}
				}
			}
		}
	}

	public void DisappearEffect() {

		Location ploc = (Location) PlayerTarget.getLocation();

		for(int i=0; i < 5; i++){
			for(float j=0; j < 2; j+= 0.5f){
				Location hbloc = (Location) Herobrine.getPluginCore().HerobrineNPC.getBukkitEntity().getLocation();
				hbloc.setY(hbloc.getY() + j);
				hbloc.getWorld().playEffect(hbloc, Effect.SMOKE, 80);
			}
		}
		
		ploc.setY(-20);
		Herobrine.getPluginCore().HerobrineNPC.moveTo(ploc);

	}

	private void BuildInterval() {
		if (Utils.getRandomGen().nextInt(100) < 75) {
			PyramidInterval();
		} else {
			TempleInterval();
		}

		if (Utils.getRandomGen().nextBoolean()) {
			BuildCave();
		}
	}

	private void StartIntervals() {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(AICore.plugin, new Runnable() {
			public void run() {
				Start_RP();
				Start_MAIN();
				Start_BD();
				Start_RC();
			}
		}, 1 * 5L);

	}

	public void Start_RP() {
		RandomPositionINT = true;
		RP_INT = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(AICore.plugin, new Runnable() {
			public void run() {
				RandomPositionInterval();
			}
		}, 1 * 300L, 1 * 300L);
	}

	public void Start_BD() {
		BuildINT = true;
		BD_INT = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(AICore.plugin, new Runnable() {
			public void run() {
				BuildInterval();
			}
		}, 1 * 1L * Herobrine.getPluginCore().getConfigDB().BuildInterval,
		   1 * 1L * Herobrine.getPluginCore().getConfigDB().BuildInterval);
	}

	public void Start_MAIN() {
		MainINT = true;
		MAIN_INT = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(AICore.plugin, new Runnable() {
			public void run() {
				FindPlayer();

			}
		}, (6 / Herobrine.getPluginCore().getConfigDB().ShowRate)
				* (Herobrine.getPluginCore().getConfigDB().ShowInterval * 1L),
				(6 / Herobrine.getPluginCore().getConfigDB().ShowRate)
						* (Herobrine.getPluginCore().getConfigDB().ShowInterval * 1L));
	}

	public void Start_RM() {
		RandomMoveINT = true;

		RM_INT = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(AICore.plugin, new Runnable() {
			public void run() {
				RandomMoveInterval();
			}
		}, 1 * 50L, 1 * 50L);

	}

	public void Start_RS() {
		RandomSeeINT = true;
		RS_INT = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(AICore.plugin, new Runnable() {
			public void run() {
				RandomSeeInterval();
			}
		}, 1 * 15L, 1 * 15L);
	}

	public void Start_RC() {
		RandomCoreINT = true;
		RC_INT = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(AICore.plugin, new Runnable() {
			public void run() {
				RandomCoreINT();
			}
		}, (long) (Herobrine.getPluginCore().getConfigDB().ShowInterval / 1.5),
				(long) (Herobrine.getPluginCore().getConfigDB().ShowInterval / 1.5));
	}

	public void Start_CG() {
		CheckGravityINT = true;
		CG_INT = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(AICore.plugin, new Runnable() {
			public void run() {
				CheckGravityInterval();
			}
		}, 1 * 10L, 1 * 10L);
	}

	public void Stop_RP() {
		if (RandomPositionINT) {
			RandomPositionINT = false;
			Bukkit.getServer().getScheduler().cancelTask(RP_INT);
		}
	}

	public void Stop_BD() {
		if (BuildINT) {
			BuildINT = false;
			Bukkit.getServer().getScheduler().cancelTask(BD_INT);
		}
	}

	public void Stop_RS() {
		if (RandomSeeINT) {
			RandomSeeINT = false;
			Bukkit.getServer().getScheduler().cancelTask(RS_INT);
		}
	}

	public void Stop_RM() {
		if (RandomMoveINT) {
			RandomMoveINT = false;
			Bukkit.getServer().getScheduler().cancelTask(RM_INT);
		}
	}

	public void Stop_RC() {
		if (RandomCoreINT) {
			RandomCoreINT = false;
			Bukkit.getServer().getScheduler().cancelTask(RC_INT);
		}
	}

	public void Stop_CG() {
		if (CheckGravityINT) {
			CheckGravityINT = false;
			Bukkit.getServer().getScheduler().cancelTask(CG_INT);
		}
	}

	public void Stop_MAIN() {
		if (MainINT) {
			MainINT = false;
			Bukkit.getServer().getScheduler().cancelTask(MAIN_INT);
		}
	}

	public ItemStack createAncientSword() {
		ItemStack item = new ItemStack(Material.GOLDEN_SWORD);
		String name = "Ancient Sword";
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("Ancient Sword");
		lore.add("A very old and mysterious sword");
		lore.add("that protects aganist Herobrine.");
		item = ItemName.setNameAndLore(item, name, lore);
		return item;
	}

	public boolean isAncientSword(ItemStack item) {
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("Ancient Sword");
		lore.add("A very old and mysterious sword");
		lore.add("that protects aganist Herobrine.");
		if (item != null) {
			if (item.getItemMeta() != null) {
				if (item.getItemMeta().getLore() != null) {
					ArrayList<String> ilore = (ArrayList<String>) item.getItemMeta().getLore();
					if (ilore.containsAll(lore)) {
						return true;

					}
				}
			}
		}

		return false;
	}

	public boolean checkAncientSword(Inventory inv) {
		ItemStack[] itemlist = inv.getContents();
		ItemStack item = null;
		int i = 0;
		for (i = 0; i <= itemlist.length - 1; i++) {
			item = itemlist[i];
			if (isAncientSword(item)) {
				return true;
			}
		}

		return false;
	}
	
	public boolean toggleHerobrinePlayerVisibilityNoTeleport(Player p) {
		// Toggles the visibility of Herobrine for the given player. This function does not perform the "visibility activation teleport".
		// If an activiation teleport should be performed, returns true, otherwise, false.
		boolean playerCanSeeHerobrine = p.hasLineOfSight(Herobrine.getPluginCore().HerobrineNPC.getBukkitEntity());
		if(playerCanSeeHerobrine && !visibilityList.contains(p)) {
			// If player p can see Herobrine but visibilty is not already enabled, then enable it.
			EntityPlayer pcon = ((CraftPlayer) p).getHandle();
			pcon.playerConnection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, Herobrine.getPluginCore().HerobrineNPC.getEntity()));
			visibilityList.add(p);
			return true;
		}
		else if(!playerCanSeeHerobrine && visibilityList.contains(p)) {
			// If player p cannot see Herobrine but visibility is still enabled, then disable it.
			EntityPlayer pcon = ((CraftPlayer) p).getHandle();
			pcon.playerConnection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, Herobrine.getPluginCore().HerobrineNPC.getEntity()));
			visibilityList.remove(p);
		}
		return false;
	}
	
	public void visibilityActivationTeleport() {
		// Makes Herobrine visible to players that should be able to see him by quickly teleporting him out of the map and back to where he previously was.
		Location original = Herobrine.getPluginCore().HerobrineNPC.getBukkitEntity().getLocation();
		Herobrine.getPluginCore().HerobrineNPC.getBukkitEntity().teleport(new Location(Bukkit.getServer().getWorlds().get(0), 0, -20, 0));
		Herobrine.getPluginCore().HerobrineNPC.getBukkitEntity().teleport(original);
	}
	
	public void toggleHerobrinePlayerVisibility(Player p) {
		// Toggles the visibility of Herobrine for the given player. Most of the work is passed off to toggleHerobrinePlayerVisibilityNoTeleport().
		if(toggleHerobrinePlayerVisibilityNoTeleport(p)) {
			// If toggleHerobrinePlayerVisibilityNoTeleport() retured true, Herobrine will appear in the tab list, but to appear to the player, he cannot
			// already be in the line of sight. To work around this, teleport Herobrine out of the line of sight and then teleport him back.
			visibilityActivationTeleport();
		}
	}
}
