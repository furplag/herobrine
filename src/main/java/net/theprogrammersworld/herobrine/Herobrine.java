package net.theprogrammersworld.herobrine;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nonnull;

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
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.extern.slf4j.Slf4j;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
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

@Slf4j(topic = "Minecraft")
public final class Herobrine extends JavaPlugin implements Listener {

  public static final List<Material> allowedBlocks;
  static {/* @formatter:off */allowedBlocks = Collections.unmodifiableList(List.of(
      Material.AIR
    , Material.SNOW
    , Material.ACACIA_PRESSURE_PLATE, Material.BIRCH_PRESSURE_PLATE, Material.CRIMSON_PRESSURE_PLATE, Material.DARK_OAK_PRESSURE_PLATE
    , Material.JUNGLE_PRESSURE_PLATE, Material.MANGROVE_PRESSURE_PLATE, Material.OAK_PRESSURE_PLATE, Material.SPRUCE_PRESSURE_PLATE, Material.WARPED_PRESSURE_PLATE
    , Material.HEAVY_WEIGHTED_PRESSURE_PLATE, Material.LIGHT_WEIGHTED_PRESSURE_PLATE, Material.STONE_PRESSURE_PLATE
    , Material.RAIL, Material.ACTIVATOR_RAIL, Material.DETECTOR_RAIL, Material.POWERED_RAIL
    , Material.DEAD_BUSH, Material.VINE
    , Material.DANDELION, Material.POPPY
    , Material.ACACIA_BUTTON, Material.BIRCH_BUTTON, Material.CRIMSON_BUTTON, Material.DARK_OAK_BUTTON
    , Material.JUNGLE_BUTTON, Material.MANGROVE_BUTTON, Material.OAK_BUTTON, Material.SPRUCE_BUTTON, Material.WARPED_BUTTON
    , Material.STONE_BUTTON
    , Material.LADDER, Material.LEVER, Material.REDSTONE, Material.REDSTONE_TORCH, Material.TORCH
  ));/* @formatter:on */}

  

  private static Herobrine pluginCore;
  private AICore aicore;
  private ConfigDB configDB;
  private Support support;
  private EntityManager entMng;
  private PathManager pathManager;
  private NPCCore npcCore;
  public HumanNPC HerobrineNPC;
  public long entityId;
  public boolean isInitDone = false;
  private int pathUpdateINT = 0;

  public static String versionStr = "UNDEFINED";
  public static boolean isNPCDisabled = false;
  public static int HerobrineHP = 200;
  public static int HerobrineMaxHP = 200;
  public static final boolean isDebugging = false;
  public static boolean AvailableWorld = false;

  public Map<Player, Long> PlayerApple = new HashMap<Player, Long>();

  @Override
  public void onEnable() {
    boolean continueWithEnable = true;
    // Check a server class name to determine if the plugin is compatible with the Spigot server version.
    // If it is not, print an error message and disable the plugin.
    if (continueWithEnable) {
      try {
		Class.forName("org.bukkit.craftbukkit.v1_20_R1.CraftArt");
      } catch (ClassNotFoundException e) {
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "This version of Herobrine is not compatible with this server's Spigot version and will be disabled.");
        continueWithEnable = false;
        getServer().getPluginManager().disablePlugin(this);
      }
    }

    if (continueWithEnable) {
      // Custom Entity Injection
      if (!isNPCDisabled) {
        try {
          addCustomEntity("mzombie", CustomZombie::new, MobCategory.MONSTER);
          addCustomEntity("mskeleton", CustomSkeleton::new, MobCategory.MONSTER);
        } catch (Exception e) {
          e.printStackTrace();
          getServer().getPluginManager().disablePlugin(this);
        }
      } else {
        log.warn("[Herobrine] Custom NPCs have been disabled due to an incompatibility error.");
      }

      getServer().getPluginManager().registerEvents(this, this);
    }
  }

  @EventHandler
  public void onServerLoad(ServerLoadEvent event) {
    PluginDescriptionFile pdf = this.getDescription();
    versionStr = pdf.getVersion();

    isInitDone = true;

    Herobrine.pluginCore = this;

    this.configDB = new ConfigDB();

    this.npcCore = new NPCCore();

    // Initialize PathManager
    this.pathManager = new PathManager();

    // Initialize AICore
    this.aicore = new AICore();

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

      GraveyardWorld.Create();

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

    pathUpdateINT = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
      @Override
      public void run() {
        if (Set.of(Core.Type.ANY, Core.Type.RANDOM_POSITION).contains(Herobrine.getPluginCore().getAICore().getCurrent())) {
          pathManager.setPath(new Path(ThreadLocalRandom.current().nextInt(15) - 7f, ThreadLocalRandom.current().nextInt(15) - 7f, Herobrine.getPluginCore()));
        }
      }
    }, 1 * 200L, 1 * 200L);

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

  @Override
  public void onDisable() {
    if (isInitDone) {
      this.entMng.killAllMobs();
      Bukkit.getServer().getScheduler().cancelTask(pathUpdateINT);
      npcCore.cancelTask();
      aicore.Stop_BD();
      aicore.Stop_CG();
      aicore.Stop_MAIN();
      aicore.Stop_RC();
      aicore.Stop_RM();
      aicore.Stop_RP();
      aicore.Stop_RS();
      aicore.disableAll();
    }
  }

  public java.io.InputStream getInputStreamData(String src) {
    return Herobrine.class.getResourceAsStream(src);
  }

  public AICore getAICore() {
    return this.aicore;
  }

  public EntityManager getEntityManager() {
    return this.entMng;
  }

  public static boolean isHerobrine(final int entityId) {
    return entityId == Herobrine.getPluginCore().entityId;
  }

  public static boolean isGraveyard(@Nonnull World world) {
    return world.equals(Bukkit.getServer().getWorld(Herobrine.getPluginCore().getConfigDB().HerobrineWorldName));
  }

  public static Herobrine getPluginCore() {
    return Herobrine.pluginCore;
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

  public String getVersionStr() {
    return versionStr;
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
          log.info("[Herobrine] " + player.getDisplayName() + " has ignore permission.");
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

  private static <T extends Entity> void addCustomEntity(String customName, EntityType.EntityFactory<T> _func, MobCategory enumCreatureType) {
    // Registers a custom entity. Adapted from
    // https://www.spigotmc.org/threads/handling-custom-entity-registry-on-spigot-1-13.353426/#post-3447111
    // As of 1.18.2, this function was updated to unfreeze and freeze the entity
    // registry when adding a new custom entity. Additionally, a conditional
    // check was added to prevent the plugin from crashing due to an attempt to add
    // the same custom entity twice.
    if (!BuiltInRegistries.ENTITY_TYPE.getOptional(new ResourceLocation(customName)).isPresent()) {
      unfreezeRegistry();
      EntityType.Builder<?> entity = EntityType.Builder.of(_func, enumCreatureType);
      entity.noSummon();
      Registry.register(BuiltInRegistries.ENTITY_TYPE, customName, entity.build(customName));
      BuiltInRegistries.ENTITY_TYPE.freeze();
    }
  }

  private static void unfreezeRegistry() {
    // As of 1.18.2, registries are frozen once NMS is done adding to them, so we
    // have to do some super hacky things to add custom entities now.
    // Adapted from
    // https://github.com/iSach/UltraCosmetics/blob/7f8bbfd2a540559888b89dae7eee4dec482ab7c9/v1_18_R2/src/main/java/be/isach/ultracosmetics/v1_18_R2/customentities/CustomEntities.java#L75-L104
    // Obfuscated fields are from
    // https://github.com/iSach/UltraCosmetics/blob/master/v1_19_R1/src/main/java/be/isach/ultracosmetics/v1_19_R1/ObfuscatedFields.java
    final String INTRUSIVE_HOLDER_CACHE = "m";
    final String FROZEN = "l";
    Class<?> registryClass = MappedRegistry.class;
    try {
      Field intrusiveHolderCache = registryClass.getDeclaredField(INTRUSIVE_HOLDER_CACHE);
      intrusiveHolderCache.setAccessible(true);
      intrusiveHolderCache.set(BuiltInRegistries.ENTITY_TYPE, new IdentityHashMap<EntityType<?>, Holder.Reference<EntityType<?>>>());
      Field frozen = registryClass.getDeclaredField(FROZEN);
      frozen.setAccessible(true);
      frozen.set(BuiltInRegistries.ENTITY_TYPE, false);
    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
      e.printStackTrace();
      return;
    }
  }
}
