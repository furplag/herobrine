package net.theprogrammersworld.herobrine.AI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket.Action;
import net.minecraft.server.level.ServerPlayer;
import net.theprogrammersworld.herobrine.Herobrine;
import net.theprogrammersworld.herobrine.Utils;
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
import net.theprogrammersworld.herobrine.AI.cores.SoundCore;
import net.theprogrammersworld.herobrine.AI.cores.Temple;
import net.theprogrammersworld.herobrine.AI.cores.Totem;
import net.theprogrammersworld.herobrine.entity.MobType;
import net.theprogrammersworld.herobrine.misc.ItemName;

public class AICore {

  public static ConsoleLogger log = new ConsoleLogger();

  private static final List<Core> cores;
  static {
    cores = Collections
        .unmodifiableList(List.of(new Attack(), new Book(), new BuildCave(), new Burn(), new BuryPlayer(), new Curse(),
            new DestroyTorches(), new Graveyard(), new Haunt(), new Heads(), new Pyramid(), new RandomExplosion(),
            new RandomPosition(), new RandomSound(), new Signs(), new SoundCore(), new Temple(), new Totem()));
  }

  @Getter
  @Setter
  private Core.Type current = Core.Type.ANY;

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

  public Core getCore(Core.Type type) {
    for (Core c : cores) {
      if (c.getCoreType() == type) {
        return c;
      }
    }
    return null;
  }

  public AICore() {

    /* Cores init */
    resetLimits = new ResetLimits();

    plugin = Herobrine.getPluginCore();
    log.info("[Herobrine] Herobrine is now running in debug mode.");
    FindPlayer();
    StartIntervals();

  }

  public Graveyard getGraveyard() {
    return ((Graveyard) getCore(Core.Type.GRAVEYARD));
  }

  public RandomPosition getRandomPosition() {
    return ((RandomPosition) getCore(Core.Type.RANDOM_POSITION));
  }

  public ResetLimits getResetLimits() {
    return resetLimits;
  }

  public void disableAll() {

    resetLimits.disable();

  }

  public static String getStringWalkingMode() {

    String result = "";

    if (Herobrine.getPluginCore().getAICore().getCurrent() == Core.Type.RANDOM_POSITION) {
      result = "Yes";
    } else {
      result = "No";
    }

    return result;

  }

  public void playerBedEnter(Player player) {
    int chance = Utils.getRandom().nextInt(100);
    if (chance < 25) {
      GraveyardTeleport(player);
    } else if (chance < 50) {
      setHauntTarget(player);
    } else if (Herobrine.getPluginCore().getConfigDB().SpawnDemonsOnPlayerBedEnter
        && Herobrine.getPluginCore().getConfigDB().UseNPC_Demon && !Herobrine.isNPCDisabled) {
      Herobrine.getPluginCore().getEntityManager().spawnCustomSkeleton(player.getLocation(), MobType.DEMON);
    }
  }

  public void FindPlayer() {
    if (Herobrine.getPluginCore().getConfigDB().OnlyWalkingMode == false) {

      if (isTarget == false) {

        int att_chance = Utils.getRandom().nextInt(100);
        log.info("[Herobrine] Generating find chance...");

        if (att_chance - (Herobrine.getPluginCore().getConfigDB().ShowRate * 4) < 55) {

          if (Bukkit.getServer().getOnlinePlayers().size() > 0) {

            log.info("[Herobrine] Finding target...");
            Player targetPlayer = Utils.getRandomPlayer();

            if (targetPlayer.getEntityId() != Herobrine.getPluginCore().entityId) {

              if (Herobrine.getPluginCore().getConfigDB().useWorlds
                  .contains(targetPlayer.getLocation().getWorld().getName())
                  && Herobrine.getPluginCore().canAttackPlayerNoMSG(targetPlayer)) {

                CancelTarget(Core.Type.ANY);
                isTarget = true;
                log.info("[Herobrine] Target found. Starting AI now. (" + targetPlayer.getName() + ")");
                setCurrent(Core.Type.START);
                StartAI();

              } else {
                log.info(
                    "[Herobrine] Target is in a safe world. (" + targetPlayer.getLocation().getWorld().getName() + ")");
                FindPlayer();
              }

            }

          }

        }

      }
    }
  }

  public void CancelTarget(Core.Type coreType) {

    if (coreType == current || coreType == Core.Type.ANY) {

      if (current == Core.Type.RANDOM_POSITION) {
        Stop_RM();
        Stop_RS();
        Stop_CG();
        Location nowloc = new Location(Bukkit.getServer().getWorlds().get(0), 0, -100.f, 0);

        nowloc.setYaw(1.f);
        nowloc.setPitch(1.f);

        Herobrine.getPluginCore().HerobrineNPC.moveTo(nowloc);
        current = Core.Type.ANY;
        Herobrine.getPluginCore().getPathManager().setPath(null);
      }

      if (isTarget == true) {
        if (current == Core.Type.ATTACK) {
          ((Attack) getCore(Core.Type.ATTACK)).StopHandler();
        }
        if (current == Core.Type.HAUNT) {
          ((Haunt) getCore(Core.Type.HAUNT)).StopHandler();
        }

        _ticks = 0;
        isTarget = false;
        Herobrine.HerobrineHP = Herobrine.HerobrineMaxHP;

        log.info("[Herobrine] Cancelled target.");
        Location nowloc = new Location(Bukkit.getServer().getWorlds().get(0), 0, -100.f, 0);

        nowloc.setYaw(1.f);
        nowloc.setPitch(1.f);

        Herobrine.getPluginCore().HerobrineNPC.moveTo(nowloc);
        current = Core.Type.ANY;
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(AICore.plugin, new Runnable() {
          public void run() {
            FindPlayer();
          }
        }, (6 / Herobrine.getPluginCore().getConfigDB().ShowRate)
            * (Herobrine.getPluginCore().getConfigDB().ShowInterval * 1L));

      }
    }
  }

  public void StartAI() {
    if (PlayerTarget.isOnline() && isTarget) {
      if (PlayerTarget.isDead() == false) {
        Object[] data = { PlayerTarget };
        int chance = Utils.getRandom().nextInt(100);
        if (chance <= 10) {
          if (Herobrine.getPluginCore().getConfigDB().UseGraveyardWorld == true) {
            log.info("[Herobrine] Teleporting " + PlayerTarget.getDisplayName() + " to Herobrine's Graveyard.");

            getCore(Core.Type.GRAVEYARD).RunCore(data);

          }
        } else if (chance <= 25) {

          getCore(Core.Type.ATTACK).RunCore(data);
        } else {
          getCore(Core.Type.HAUNT).RunCore(data);
        }
      } else {
        CancelTarget(Core.Type.START);
      }
    } else {
      CancelTarget(Core.Type.START);
    }
  }

  public CoreResult setAttackTarget(Player player) {
    Object[] data = { player };
    return getCore(Core.Type.ATTACK).RunCore(data);
  }

  public CoreResult setHauntTarget(Player player) {
    Object[] data = { player };
    return getCore(Core.Type.HAUNT).RunCore(data);
  }

  public void GraveyardTeleport(final Player player) {

    if (player.isOnline()) {
      CancelTarget(Core.Type.ANY);

      Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(AICore.plugin, new Runnable() {
        public void run() {
          Object[] data = { player };
          getCore(Core.Type.GRAVEYARD).RunCore(data);
        }
      }, 1 * 10L);

    }

  }

  public void PlayerCallTotem(Player player) {
    final String playername = player.getName();
    final Location loc = (Location) player.getLocation();
    isTotemCalled = true;
    CancelTarget(Core.Type.ANY);
    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(AICore.plugin, new Runnable() {
      public void run() {
        CancelTarget(Core.Type.ANY);
        Object[] data = { loc, playername };
        getCore(Core.Type.TOTEM).RunCore(data);
      }
    }, 1 * 40L);
  }

  private void RandomPositionInterval() {
    if (current == Core.Type.ANY) {
      ((RandomPosition) getCore(Core.Type.RANDOM_POSITION)).setRandomTicks(0);
      int count = Herobrine.getPluginCore().getConfigDB().useWorlds.size();
      int chance = Utils.getRandom().nextInt(count);
      Object[] data = { Bukkit.getServer().getWorld(Herobrine.getPluginCore().getConfigDB().useWorlds.get(chance)) };
      getCore(Core.Type.RANDOM_POSITION).RunCore(data);

    }
  }

  private void CheckGravityInterval() {
    if (this.current == Core.Type.RANDOM_POSITION) {
      ((RandomPosition) getCore(Core.Type.RANDOM_POSITION)).CheckGravity();
    }

  }

  private void RandomMoveInterval() {
    ((RandomPosition) getCore(Core.Type.RANDOM_POSITION)).RandomMove();

  }

  private void RandomSeeInterval() {
    if (current == Core.Type.RANDOM_POSITION) {
      ((RandomPosition) getCore(Core.Type.RANDOM_POSITION)).CheckPlayerPosition();
    }

  }

  private void PyramidInterval() {

    if (Utils.getRandom().nextBoolean()) {
      if (Bukkit.getServer().getOnlinePlayers().size() > 0) {
        log.info("[Herobrine] Finding pyramid target...");

        Player player = Utils.getRandomPlayer();
        if (Herobrine.getPluginCore().getConfigDB().useWorlds.contains(player.getLocation().getWorld().getName())) {

          int chance2 = Utils.getRandom().nextInt(100);
          if (chance2 < 30) {
            if (Herobrine.getPluginCore().getConfigDB().BuildPyramids == true) {
              Object[] data = { player };
              getCore(Core.Type.PYRAMID).RunCore(data);
            }
          } else if (chance2 < 70) {
            if (Herobrine.getPluginCore().getConfigDB().BuryPlayers) {
              Object[] data = { player };
              getCore(Core.Type.BURY_PLAYER).RunCore(data);
            }
          } else {
            if (Herobrine.getPluginCore().getConfigDB().UseHeads) {
              Object[] data = { player.getName() };
              getCore(Core.Type.HEADS).RunCore(data);
            }
          }
        }
      }

    }

  }

  private void TempleInterval() {
    if (Herobrine.getPluginCore().getConfigDB().BuildTemples == true) {
      if (Utils.getRandom().nextBoolean()) {
        if (Bukkit.getServer().getOnlinePlayers().size() > 0) {
          log.info("[Herobrine] Finding temple target...");

          Player player = Utils.getRandomPlayer();

          if (Herobrine.getPluginCore().getConfigDB().useWorlds.contains(player.getLocation().getWorld().getName())) {
            if (Utils.getRandom().nextBoolean()) {
              Object[] data = { player };
              getCore(Core.Type.TEMPLE).RunCore(data);

            }
          }
        }
      }
    }

  }

  private void BuildCave() {
    if (Herobrine.getPluginCore().getConfigDB().BuildStuff == true) {
      if (Utils.getRandom().nextBoolean()) {
        if (Bukkit.getServer().getOnlinePlayers().size() > 0) {

          Player player = Utils.getRandomPlayer();

          if (Herobrine.getPluginCore().getConfigDB().useWorlds.contains(player.getLocation().getWorld().getName())) {

            if (Utils.getRandom().nextBoolean()) {
              Object[] data = { player.getLocation() };
              getCore(Core.Type.BUILD_CAVE).RunCore(data);

            }
          }
        }
      }
    }
  }

  public void callByDisc(Player player) {
    isDiscCalled = false;
    if (player.isOnline()) {
      CancelTarget(Core.Type.ANY);
      setHauntTarget(player);
    }
  }

  public void RandomCoreINT() {

    if (Utils.getRandom().nextBoolean()) {
      if (Bukkit.getServer().getOnlinePlayers().size() > 0) {

        Player player = Utils.getRandomPlayer();

        if (player.getEntityId() != Herobrine.getPluginCore().entityId) {
          if (Herobrine.getPluginCore().getConfigDB().useWorlds.contains(player.getLocation().getWorld().getName())) {
            Object[] data = { player };
            if (Herobrine.getPluginCore().canAttackPlayerNoMSG(player)) {
              if (Utils.getRandom().nextInt(100) < 30) {

                getCore(Core.Type.RANDOM_SOUND).RunCore(data);
              } else if (Utils.getRandom().nextInt(100) < 60) {
                if (Herobrine.getPluginCore().getConfigDB().Burn) {
                  getCore(Core.Type.BURN).RunCore(data);
                }
              } else if (Utils.getRandom().nextInt(100) < 80) {
                if (Herobrine.getPluginCore().getConfigDB().Curse) {
                  getCore(Core.Type.CURSE).RunCore(data);
                }
              } else {

                getCore(Core.Type.RANDOM_EXPLOSION).RunCore(data);
              }
            }
          }
        }
      }
    }
  }

  public void DisappearEffect() {

    Location ploc = (Location) PlayerTarget.getLocation();

    for (int i = 0; i < 5; i++) {
      for (float j = 0; j < 2; j += 0.5f) {
        Location hbloc = (Location) Herobrine.getPluginCore().HerobrineNPC.getBukkitEntity().getLocation();
        hbloc.setY(hbloc.getY() + j);
        hbloc.getWorld().playEffect(hbloc, Effect.SMOKE, 80);
      }
    }

    ploc.setY(-100);
    Herobrine.getPluginCore().HerobrineNPC.moveTo(ploc);

  }

  private void BuildInterval() {
    if (Utils.getRandom().nextInt(100) < 75) {
      PyramidInterval();
    } else {
      TempleInterval();
    }

    if (Utils.getRandom().nextBoolean()) {
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
    // Toggles the visibility of Herobrine for the given player. This function does
    // not perform the "visibility activation teleport".
    // If an activiation teleport should be performed, returns true, otherwise,
    // false.
    boolean playerCanSeeHerobrine = p.hasLineOfSight(Herobrine.getPluginCore().HerobrineNPC.getBukkitEntity());
    if (playerCanSeeHerobrine && !visibilityList.contains(p)) {
      // If player p can see Herobrine but visibilty is not already enabled, then
      // enable it.
      ServerPlayer pcon = ((CraftPlayer) p).getHandle();
      pcon.connection.send(
          new ClientboundPlayerInfoUpdatePacket(Action.ADD_PLAYER, Herobrine.getPluginCore().HerobrineNPC.getEntity()));
      visibilityList.add(p);
      return true;
    } else if (!playerCanSeeHerobrine && visibilityList.contains(p)) {
      // If player p cannot see Herobrine but visibility is still enabled, then
      // disable it.
      ServerPlayer pcon = ((CraftPlayer) p).getHandle();
      pcon.connection.send(
          new ClientboundPlayerInfoRemovePacket(List.of(Herobrine.getPluginCore().HerobrineNPC.getEntity().getUUID())));
      visibilityList.remove(p);
    }
    return false;
  }

  public void visibilityActivationTeleport() {
    // Makes Herobrine visible to players that should be able to see him by quickly
    // teleporting him out of the map and back to where he previously was.
    Location original = Herobrine.getPluginCore().HerobrineNPC.getBukkitEntity().getLocation();
    Herobrine.getPluginCore().HerobrineNPC.getBukkitEntity()
        .teleport(new Location(Bukkit.getServer().getWorlds().get(0), 0, -100, 0));
    Herobrine.getPluginCore().HerobrineNPC.getBukkitEntity().teleport(original);
  }

  public void toggleHerobrinePlayerVisibility(Player p) {
    // Toggles the visibility of Herobrine for the given player. Most of the work is
    // passed off to toggleHerobrinePlayerVisibilityNoTeleport().
    if (toggleHerobrinePlayerVisibilityNoTeleport(p)) {
      // If toggleHerobrinePlayerVisibilityNoTeleport() retured true, Herobrine will
      // appear in the tab list, but to appear to the player, he cannot
      // already be in the line of sight. To work around this, teleport Herobrine out
      // of the line of sight and then teleport him back.
      visibilityActivationTeleport();
    }
  }
}
