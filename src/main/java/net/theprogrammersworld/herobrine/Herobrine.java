package net.theprogrammersworld.herobrine;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

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

import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.theprogrammersworld.herobrine.AI.AICore;
import net.theprogrammersworld.herobrine.AI.Core.CoreType;
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

public class Herobrine extends JavaPlugin implements Listener {

	private static Herobrine pluginCore;
	private AICore aicore;
	private ConfigDB configdb;
	private Support support;
	private EntityManager entMng;
	private PathManager pathMng;
	private NPCCore NPCman;
	public HumanNPC HerobrineNPC;
	public long HerobrineEntityID;
	public boolean isInitDone = false;
	private int pathUpdateINT = 0;

	public static String versionStr = "UNDEFINED";
	public static boolean isNPCDisabled = false;
	public static int HerobrineHP = 200;
	public static int HerobrineMaxHP = 200;
	public static final boolean isDebugging = false;
	public static boolean AvailableWorld = false;

	public static List<Material> AllowedBlocks = new ArrayList<Material>();
	public Map<Player, Long> PlayerApple = new HashMap<Player, Long>();

	public static Logger log = Logger.getLogger("Minecraft");

	@Override
	public void onEnable() {
		boolean continueWithEnable = true;

		// Check a server class name to determine if the plugin is compatible with the Spigot server version.
		// If it is not, print an error message and disable the plugin.
		if (continueWithEnable) {
			try {
				Class.forName("org.bukkit.craftbukkit.v1_19_R2.CraftArt");
			} catch (ClassNotFoundException e) {
				Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "This version of Herobrine is not "
						+ "compatible with this server's Spigot version and will be disabled.");
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
				log.warning("[Herobrine] Custom NPCs have been disabled due to an incompatibility error.");
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

		this.configdb = new ConfigDB(log);

		this.NPCman = new NPCCore(this);

		// Initialize PathManager
		this.pathMng = new PathManager();

		// Initialize AICore
		this.aicore = new AICore();

		// Initialize EntityManager
		this.entMng = new EntityManager();

		// Config loading
		configdb.Startup();
		configdb.Reload();

		// Graveyard World
		if (this.configdb.UseGraveyardWorld == true && Bukkit.getServer().getWorld(configdb.HerobrineWorldName) == null) {
			WorldCreator wc = new WorldCreator(configdb.HerobrineWorldName);
			wc.generateStructures(false);
			org.bukkit.WorldType type = org.bukkit.WorldType.FLAT;
			wc.type(type);
			wc.createWorld();

			GraveyardWorld.Create();

			/* Starting with Minecraft 1.18, the Y coordinate used for the flat world generated for Herobrine's Graveyard is -61 instead of 3.
			 * This elevation change required graveyard generation as well as Herobrine and player location management to be shifted down to
			 * accommodate it. This change is only applied to graveyard worlds generated on 1.18 or newer, however, and would cause graveyards
			 * generated on older versions to not show Herobrine or the player in the right place. To work around this, we use this conditional
			 * to set a "graveyardYCoord" configuration variable. By default, the value is set for a graveyard generated by 1.18 or newer. This
			 * conditional will check the position of the graveyard to determine if the value should be set for a pre-1.18 graveyard. */
			if (Bukkit.getServer().getWorld(configdb.HerobrineWorldName).getBlockAt(0, 3, 0).getType() == Material.MYCELIUM) {
				configdb.graveyardYCoord = 4;
			}
		}

		// Spawn Herobrine
		Location nowloc = new Location((World) Bukkit.getServer().getWorlds().get(0), (float) 0, (float) -100,
				(float) 0);
		nowloc.setYaw((float) 1);
		nowloc.setPitch((float) 1);
		HerobrineSpawn(nowloc);

		HerobrineNPC.setItemInHand(configdb.ItemInHand.getItemStack());

		getServer().getPluginManager().registerEvents(new EntityListener(this), this);
		getServer().getPluginManager().registerEvents(new BlockListener(), this);
		getServer().getPluginManager().registerEvents(new InventoryListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
		getServer().getPluginManager().registerEvents(new WorldListener(), this);

		// Init Block Types
		AllowedBlocks.add(Material.AIR);
		AllowedBlocks.add(Material.SNOW);
		AllowedBlocks.add(Material.RAIL);
		AllowedBlocks.add(Material.ACTIVATOR_RAIL);
		AllowedBlocks.add(Material.DETECTOR_RAIL);
		AllowedBlocks.add(Material.POWERED_RAIL);
		AllowedBlocks.add(Material.DEAD_BUSH);
		AllowedBlocks.add(Material.DANDELION);
		AllowedBlocks.add(Material.POPPY);
		AllowedBlocks.add(Material.ACACIA_PRESSURE_PLATE);
		AllowedBlocks.add(Material.BIRCH_PRESSURE_PLATE);
		AllowedBlocks.add(Material.DARK_OAK_PRESSURE_PLATE);
		AllowedBlocks.add(Material.HEAVY_WEIGHTED_PRESSURE_PLATE);
		AllowedBlocks.add(Material.JUNGLE_PRESSURE_PLATE);
		AllowedBlocks.add(Material.LIGHT_WEIGHTED_PRESSURE_PLATE);
		AllowedBlocks.add(Material.OAK_PRESSURE_PLATE);
		AllowedBlocks.add(Material.SPRUCE_PRESSURE_PLATE);
		AllowedBlocks.add(Material.STONE_PRESSURE_PLATE);
		AllowedBlocks.add(Material.VINE);
		AllowedBlocks.add(Material.TORCH);
		AllowedBlocks.add(Material.REDSTONE);
		AllowedBlocks.add(Material.REDSTONE_TORCH);
		AllowedBlocks.add(Material.LEVER);
		AllowedBlocks.add(Material.STONE_BUTTON);
		AllowedBlocks.add(Material.LADDER);

		pathUpdateINT = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				if (Herobrine.getPluginCore().getAICore().getCoreTypeNow().equals(CoreType.ANY) ||
						Herobrine.getPluginCore().getAICore().getCoreTypeNow().equals(CoreType.RANDOM_POSITION)) {
					pathMng.setPath(new Path(Utils.getRandomGen().nextInt(15) - 7f, Utils.getRandomGen().nextInt(15) - 7f, Herobrine.getPluginCore()));
				}
			}
		}, 1 * 200L, 1 * 200L);

		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				pathMng.update();
			}
		}, 1 * 5L, 1 * 5L);

		// Command Executors
		this.getCommand("hb").setExecutor((CommandExecutor) new CmdExecutor(this));
		this.getCommand("hero").setExecutor((CommandExecutor) new CmdExecutor(this));

		// Support initialize
		this.support = new Support();

		// If the plugin configuration has updated checking turned on, start the thread responsible for performing the check.
		if(configdb.CheckForUpdates)
			new Thread(new UpdateScanner()).start();
	}

	@Override
	public void onDisable() {
		if (isInitDone) {
			this.entMng.killAllMobs();
			Bukkit.getServer().getScheduler().cancelTask(pathUpdateINT);
			NPCman.DisableTask();
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

	public static Herobrine getPluginCore() {
		return Herobrine.pluginCore;
	}

	public void HerobrineSpawn(Location loc) {
		HerobrineNPC = (HumanNPC) NPCman.spawnHumanNPC(ChatColor.WHITE + "Herobrine", loc);
		HerobrineNPC.getBukkitEntity().setMetadata("NPC", new FixedMetadataValue(this, true));
		HerobrineEntityID = HerobrineNPC.getBukkitEntity().getEntityId();
	}

	public void HerobrineRemove() {
		HerobrineEntityID = 0;
		HerobrineNPC = null;
		NPCman.removeAll();
	}

	public ConfigDB getConfigDB() {
		return this.configdb;
	}

	public String getVersionStr() {
		return versionStr;
	}

	public Support getSupport() {
		return this.support;
	}

	public PathManager getPathManager() {
		return this.pathMng;
	}

	public boolean canAttackPlayer(Player player, Player sender) {

		boolean opCheck = true;
		boolean creativeCheck = true;
		boolean ignoreCheck = true;

		if (!configdb.AttackOP && player.isOp()) {
			opCheck = false;

		}

		if (!configdb.AttackCreative && player.getGameMode() == GameMode.CREATIVE) {
			creativeCheck = false;
		}

		if (configdb.UseIgnorePermission && player.hasPermission("herobrine.ignore")) {
			ignoreCheck = false;
		}

		if (opCheck && creativeCheck && ignoreCheck) {
			return true;
		} else {

			if(sender == null){
				if (!opCheck)
					log.info("[Herobrine] " + player.getDisplayName() + " is an OP.");
				else if (!creativeCheck)
					log.info("[Herobrine] " + player.getDisplayName() + " is in creative mode.");
				else if (!ignoreCheck)
					log.info("[Herobrine] " + player.getDisplayName() + " has ignore permission.");
			}else{
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

		if (!configdb.AttackOP && player.isOp()) {
			opCheck = false;
		}

		if (!configdb.AttackCreative && player.getGameMode() == GameMode.CREATIVE) {
			creativeCheck = false;
		}

		if (configdb.UseIgnorePermission && player.hasPermission("herobrine.ignore")) {
			ignoreCheck = false;
		}

		if (opCheck && creativeCheck && ignoreCheck) {
			return true;
		} else {

			return false;
		}
	}

	public String getAvailableWorldString() {
		if (AvailableWorld) {
			return "Yes";
		} else {
			return "No";
		}
	}

	private static <T extends Entity> void addCustomEntity(String customName, EntityType.EntityFactory<T> _func, MobCategory enumCreatureType) {
		// Registers a custom entity. Adapted from https://www.spigotmc.org/threads/handling-custom-entity-registry-on-spigot-1-13.353426/#post-3447111
		// As of 1.18.2, this function was updated to unfreeze and freeze the entity registry when adding a new custom entity. Additionally, a conditional
		// check was added to prevent the plugin from crashing due to an attempt to add the same custom entity twice.
		if (!BuiltInRegistries.ENTITY_TYPE.getOptional(new ResourceLocation(customName)).isPresent()) {
			unfreezeRegistry();
			EntityType.Builder<?> entity = EntityType.Builder.of(_func, enumCreatureType);
			entity.noSummon();
			Registry.register(BuiltInRegistries.ENTITY_TYPE, customName, entity.build(customName));
			BuiltInRegistries.ENTITY_TYPE.freeze();
		}
	}

	private static void unfreezeRegistry() {
		// As of 1.18.2, registries are frozen once NMS is done adding to them, so we have to do some super hacky things to add custom entities now.
		// Adapted from https://github.com/iSach/UltraCosmetics/blob/7f8bbfd2a540559888b89dae7eee4dec482ab7c9/v1_18_R2/src/main/java/be/isach/ultracosmetics/
		//	v1_18_R2/customentities/CustomEntities.java#L75-L104
		// Obfuscated fields are from https://github.com/iSach/UltraCosmetics/blob/master/v1_19_R1/src/main/java/be/isach/ultracosmetics/v1_19_R1/ObfuscatedFields.java
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
