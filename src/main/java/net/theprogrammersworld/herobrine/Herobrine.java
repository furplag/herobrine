package net.theprogrammersworld.herobrine;

import java.util.Arrays;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import jp.furplag.sandbox.reflect.SavageReflection;
import jp.furplag.sandbox.trebuchet.Trebuchet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityType.EntityFactory;
import net.minecraft.world.entity.MobCategory;
import net.theprogrammersworld.herobrine.AI.AICore;
import net.theprogrammersworld.herobrine.AI.Core;
import net.theprogrammersworld.herobrine.AI.extensions.GraveyardWorld;
import net.theprogrammersworld.herobrine.NPC.NPCCore;
import net.theprogrammersworld.herobrine.NPC.AI.Path;
import net.theprogrammersworld.herobrine.NPC.AI.PathManager;
import net.theprogrammersworld.herobrine.NPC.Entity.HumanNPC;
import net.theprogrammersworld.herobrine.commands.CmdExecutor;
import net.theprogrammersworld.herobrine.entity.CustomSkeleton;
import net.theprogrammersworld.herobrine.entity.CustomZombie;
import net.theprogrammersworld.herobrine.entity.EntityManager;
import net.theprogrammersworld.herobrine.listeners.BlockListener;
import net.theprogrammersworld.herobrine.listeners.EntityListener;
import net.theprogrammersworld.herobrine.listeners.InventoryListener;
import net.theprogrammersworld.herobrine.listeners.PlayerListener;
import net.theprogrammersworld.herobrine.listeners.WorldListener;

public final class Herobrine extends JavaPlugin implements Listener {

  /** Spigot version */
  private static final String challengeClassOfSpigotVersion = "org.bukkit.craftbukkit.v1_20_R4.CraftArt";

  private static final class _Container {
    private static final AtomicReference<Herobrine> origin = new AtomicReference<>();
  }

//  public static final List<Material> allowedBlocks;
//  static {/* @formatter:off */allowedBlocks = Collections.unmodifiableList(List.of(
//      Material.AIR
//    , Material.SNOW
//    , Material.ACACIA_PRESSURE_PLATE, Material.BIRCH_PRESSURE_PLATE, Material.CRIMSON_PRESSURE_PLATE, Material.DARK_OAK_PRESSURE_PLATE
//    , Material.JUNGLE_PRESSURE_PLATE, Material.MANGROVE_PRESSURE_PLATE, Material.OAK_PRESSURE_PLATE, Material.SPRUCE_PRESSURE_PLATE, Material.WARPED_PRESSURE_PLATE
//    , Material.HEAVY_WEIGHTED_PRESSURE_PLATE, Material.LIGHT_WEIGHTED_PRESSURE_PLATE, Material.STONE_PRESSURE_PLATE
//    , Material.RAIL, Material.ACTIVATOR_RAIL, Material.DETECTOR_RAIL, Material.POWERED_RAIL
//    , Material.DEAD_BUSH, Material.VINE
//    , Material.DANDELION, Material.POPPY
//    , Material.ACACIA_BUTTON, Material.BIRCH_BUTTON, Material.CRIMSON_BUTTON, Material.DARK_OAK_BUTTON
//    , Material.JUNGLE_BUTTON, Material.MANGROVE_BUTTON, Material.OAK_BUTTON, Material.SPRUCE_BUTTON, Material.WARPED_BUTTON
//    , Material.STONE_BUTTON
//    , Material.LADDER, Material.LEVER, Material.REDSTONE, Material.REDSTONE_TORCH, Material.TORCH
//  ));/* @formatter:on */}

  private static boolean isCompatibleServer() {
    return Trebuchet.Functions.orNot(challengeClassOfSpigotVersion, Class::forName) != null;
  }

  public static final Herobrine getInstance() {
    Optional.ofNullable(Herobrine._Container.origin.get()).ifPresentOrElse((herobrine) -> {}, () -> {
      Optional.ofNullable(Bukkit.getPluginManager().isPluginEnabled("herobrine") ? Bukkit.getPluginManager().getPlugin("herobrine") : null).ifPresent((herobrine) -> {
        Herobrine._Container.origin.set((Herobrine) herobrine);
      });
    });

    return Herobrine._Container.origin.get();
  }

  @SuppressWarnings({ "unused" })
  private static interface _Log {

    boolean isDebugEnabled();

    /** just a wrapper {@code JavaPlugin#getLogger()} . */
    Logger getLogger_();

    /* @formatter:off */
    default void config(final String templatedMessage, final Object... args) { getLogger_().config(format(templatedMessage, args)); }
    default void debug(final String templatedMessage, final Object... args) { if (isDebugEnabled()) { getLogger_().info(format("[DEBUG] {}", format(templatedMessage, args))); } }
    default void fine(final String templatedMessage, final Object... args) { getLogger_().fine(format(templatedMessage, args)); }
    default void finer(final String templatedMessage, final Object... args) { getLogger_().finer(format(templatedMessage, args)); }
    default void finest(final String templatedMessage, final Object... args) { getLogger_().finest(format(templatedMessage, args)); }
    default void info(final String templatedMessage, final Object... args) { getLogger_().info(format(templatedMessage, args)); }
    default void severe(final String templatedMessage, final Object... args) { getLogger_().severe(format(templatedMessage, args)); }
    default void warning(final String templatedMessage, final Object... args) { getLogger_().warning(format(templatedMessage, args)); }
    /* @formatter:on */

    private static String format(final String templatedMessage, final Object... args) {
      return StringUtils.defaultIfBlank(templatedMessage, "").replaceAll("\\{\\}", "%s").formatted(Arrays.stream(Optional.ofNullable(args).orElse(new Object[] { "" })).map((arg) -> Objects.toString(arg, "")).toArray());
    }
  }
  private final _Log log = new _Log() { @Getter private final boolean debugEnabled = true; @Getter private final Logger logger_ = getLogger(); };



  private AICore aICore;
  private ConfigDB configDB;
  private Support support;
  private EntityManager entMng;
  private PathManager pathManager;
  private NPCCore npcCore;
  public HumanNPC HerobrineNPC;
  public long entityId;
  private int pathUpdateINT = 0;
  private boolean isInitDone;

  public static int HerobrineHP = 200;
  public static int HerobrineMaxHP = 200;
  public static boolean AvailableWorld = false;
  public boolean isNPCDisabled;

  private boolean registerEntities() {
    return addCustomEntity("mzombie", CustomZombie::new, MobCategory.MONSTER) && addCustomEntity("mskeleton", CustomSkeleton::new, MobCategory.MONSTER);
  }

  public Map<Player, Long> PlayerApple = new HashMap<Player, Long>();

  /** {@inheritDoc} */ @Override public void onEnable() {

    if (!isCompatibleServer()) {
      Bukkit.getServer().getConsoleSender().sendMessage("%sThis version of Herobrine is not compatible with this server's Spigot version .".formatted(ChatColor.RED.toString()));
      getServer().getPluginManager().disablePlugin(this);

      return;
    }

    getServer().getPluginManager().registerEvents(this, this);
  }

  @EventHandler
  public void onServerLoad(ServerLoadEvent event) {
    this.configDB = new ConfigDB();

    this.npcCore = new NPCCore();

    // Initialize PathManager
    this.pathManager = new PathManager();

    // Initialize AICore
    this.aICore = new AICore();

    // Initialize EntityManager
    this.entMng = new EntityManager();

    // Config loading
    configDB.Startup();
    configDB.Reload();

    // Graveyard World
    if (this.configDB.UseGraveyardWorld == true && Bukkit.getServer().getWorld(configDB.HerobrineWorldName) == null) {
      WorldCreator wc = new WorldCreator(configDB.HerobrineWorldName);
      wc.generateStructures(false);
      org.bukkit.WorldType type = org.bukkit.WorldType.FLAT;
      wc.type(type);
      wc.createWorld();

      GraveyardWorld.Create(this);

      /*
       * Starting with Minecraft 1.18, the Y coordinate used for the flat world
       * generated for Herobrine's Graveyard is -61 instead of 3. This elevation
       * change required graveyard generation as well as Herobrine and player location
       * management to be shifted down to accommodate it. This change is only applied
       * to graveyard worlds generated on 1.18 or newer, however, and would cause
       * graveyards generated on older versions to not show Herobrine or the player in
       * the right place. To work around this, we use this conditional to set a
       * "graveyardYCoord" configuration variable. By default, the value is set for a
       * graveyard generated by 1.18 or newer. This conditional will check the
       * position of the graveyard to determine if the value should be set for a
       * pre-1.18 graveyard.
       */
      if (Bukkit.getServer().getWorld(configDB.HerobrineWorldName).getBlockAt(0, 3, 0).getType() == Material.MYCELIUM) {
        configDB.graveyardYCoord = 4;
      }
    }

    // Spawn Herobrine
    Location nowloc = new Location(Bukkit.getServer().getWorlds().get(0), (float) 0, (float) -100, (float) 0);
    nowloc.setYaw(1f);
    nowloc.setPitch(1f);
    HerobrineSpawn(nowloc);

    HerobrineNPC.setItemInHand(configDB.ItemInHand.getItemStack());

    getServer().getPluginManager().registerEvents(new EntityListener(this), this);
    getServer().getPluginManager().registerEvents(new BlockListener(), this);
    getServer().getPluginManager().registerEvents(new InventoryListener(), this);
    getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    getServer().getPluginManager().registerEvents(new WorldListener(), this);
    // Init Block Types
    pathUpdateINT = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Pathfinder(this), 1 * 200L, 1 * 200L);

    Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {/* @formatter:off */
      @Override public void run() { pathManager.update(); }
    /* @formatter:on */}, 1 * 5L, 1 * 5L);

    // Command Executors
    this.getCommand("hb").setExecutor((CommandExecutor) new CmdExecutor(this));
    this.getCommand("hero").setExecutor((CommandExecutor) new CmdExecutor(this));

    // Support initialize
    this.support = new Support();

    // If the plugin configuration has updated checking turned on, start the thread
    // responsible for performing the check.
    if (configDB.CheckForUpdates)
      new Thread(new UpdateScanner()).start();
  }

  @RequiredArgsConstructor
  private static class Pathfinder implements Runnable {
    private final Herobrine herobrine;
    @Override
    public void run() {
      if (Set.of(Core.Type.ANY, Core.Type.RANDOM_POSITION).contains(herobrine.getAICore().getCurrent())) {
        herobrine.pathManager.setPath(new Path(ThreadLocalRandom.current().nextInt(15) - 7f, ThreadLocalRandom.current().nextInt(15) - 7f, herobrine));
      }
    }
  }

  @Override
  public void onDisable() {
    if (isInitDone) {
      this.entMng.killAllMobs();
      Bukkit.getServer().getScheduler().cancelTask(pathUpdateINT);
      npcCore.cancelTask();
      aICore.Stop_BD();
      aICore.Stop_CG();
      aICore.Stop_MAIN();
      aICore.Stop_RC();
      aICore.Stop_RM();
      aICore.Stop_RP();
      aICore.Stop_RS();
      aICore.disableAll();
    }
  }

  public AICore getAICore() {
    return this.aICore;
  }

  public EntityManager getEntityManager() {
    return this.entMng;
  }

  public boolean isHerobrine(final int entityId) {
    return this.entityId == entityId;
  }

  public boolean isGraveyard(@Nonnull World world) {
    return world.equals(Bukkit.getServer().getWorld(getConfigDB().HerobrineWorldName));
  }

  public void HerobrineSpawn(Location loc) {
    HerobrineNPC = (HumanNPC) npcCore.spawnHumanNPC(ChatColor.WHITE + "Herobrine", loc);
    HerobrineNPC.getBukkitEntity().setMetadata("NPC", new FixedMetadataValue(this, true));
    entityId = HerobrineNPC.getBukkitEntity().getEntityId();
  }

  public void HerobrineRemove() {
    entityId = 0;
    HerobrineNPC = null;
    npcCore.removeAll();
  }

  public ConfigDB getConfigDB() {
    return this.configDB;
  }

  public Support getSupport() {
    return this.support;
  }

  public PathManager getPathManager() {
    return this.pathManager;
  }



  public boolean canAttackPlayer(Player player, Player sender) {

    boolean opCheck = true;
    boolean creativeCheck = true;
    boolean ignoreCheck = true;

    if (!configDB.AttackOP && player.isOp()) {
      opCheck = false;

    }

    if (!configDB.AttackCreative && player.getGameMode() == GameMode.CREATIVE) {
      creativeCheck = false;
    }

    if (configDB.UseIgnorePermission && player.hasPermission("herobrine.ignore")) {
      ignoreCheck = false;
    }

    if (opCheck && creativeCheck && ignoreCheck) {
      return true;
    } else {

      if (sender == null) {
        if (!opCheck)
          log.info("[Herobrine] " + player.getDisplayName() + " is an OP.");
        else if (!creativeCheck)
          log.info("[Herobrine] " + player.getDisplayName() + " is in creative mode.");
        else if (!ignoreCheck)
          log.info("[Herobrine] " + player.displayName() + " has ignore permission.");
      } else {
        if (!opCheck)
          sender.sendMessage(ChatColor.RED + "[Herobrine] " + player.getDisplayName() + " is an OP.");
        else if (!creativeCheck)
          sender.sendMessage(ChatColor.RED + "[Herobrine] " + player.getDisplayName() + " is in creative mode.");
        else if (!ignoreCheck)
          sender.sendMessage(ChatColor.RED + "[Herobrine] " + player.getDisplayName() + " has ignore permission.");
      }

      return false;
    }

  }

  public boolean canAttackPlayerNoMSG(Player player) {

    boolean opCheck = true;
    boolean creativeCheck = true;
    boolean ignoreCheck = true;

    if (!configDB.AttackOP && player.isOp()) {
      opCheck = false;
    }

    if (!configDB.AttackCreative && player.getGameMode() == GameMode.CREATIVE) {
      creativeCheck = false;
    }

    if (configDB.UseIgnorePermission && player.hasPermission("herobrine.ignore")) {
      ignoreCheck = false;
    }

    return opCheck && creativeCheck && ignoreCheck;
  }

  private static <T extends Entity> boolean addCustomEntity(String customName, EntityFactory<T> constructor, MobCategory enumCreatureType) {
    // Registers a custom entity. Adapted from
    // https://www.spigotmc.org/threads/handling-custom-entity-registry-on-spigot-1-13.353426/#post-3447111
    // As of 1.18.2, this function was updated to unfreeze and freeze the entity
    // registry when adding a new custom entity. Additionally, a conditional
    // check was added to prevent the plugin from crashing due to an attempt to add
    // the same custom entity twice.
    final String unregisteredIntrusiveHoldersObfuscated = "m";
    final String frozenObfuscated = "l";

    BuiltInRegistries.ENTITY_TYPE.getOptional(new ResourceLocation(customName)).ifPresentOrElse((entityType) -> {}, () -> {
      if (Stream.of(Map.entry(unregisteredIntrusiveHoldersObfuscated, new IdentityHashMap<EntityType<?>, Holder.Reference<EntityType<?>>>()) , Map.entry(frozenObfuscated, false))
      .map((e) -> SavageReflection.set(BuiltInRegistries.ENTITY_TYPE, e.getKey(), e.getValue()))
      .allMatch((r) -> r)) {
        Registry.register(BuiltInRegistries.ENTITY_TYPE, customName, EntityType.Builder.of(constructor, enumCreatureType).noSummon().build(customName));
        BuiltInRegistries.ENTITY_TYPE.freeze();
      }
    });

    return BuiltInRegistries.ENTITY_TYPE.getOptional(new ResourceLocation(customName)).isPresent();
  }
}
