package org.jakub1221.herobrineai;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.bukkit.event.Listener;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.jakub1221.herobrineai.NPC.AI.Path;
import org.jakub1221.herobrineai.NPC.AI.PathManager;
import org.jakub1221.herobrineai.NPC.Entity.HumanNPC;
import org.jakub1221.herobrineai.NPC.NPCCore;
import org.jakub1221.herobrineai.AI.AICore;
import org.jakub1221.herobrineai.AI.Core.CoreType;
import org.jakub1221.herobrineai.AI.extensions.GraveyardWorld;
import org.jakub1221.herobrineai.commands.CmdExecutor;
import org.jakub1221.herobrineai.entity.CustomSkeleton;
import org.jakub1221.herobrineai.entity.CustomZombie;
import org.jakub1221.herobrineai.entity.EntityManager;
import org.jakub1221.herobrineai.listeners.BlockListener;
import org.jakub1221.herobrineai.listeners.EntityListener;
import org.jakub1221.herobrineai.listeners.InventoryListener;
import org.jakub1221.herobrineai.listeners.PlayerListener;
import org.jakub1221.herobrineai.listeners.WorldListener;

public class HerobrineAI extends JavaPlugin implements Listener {

	private static HerobrineAI pluginCore;
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
	public static String bukkit_ver_string = "1.15.1";
	public static int HerobrineHP = 200;
	public static int HerobrineMaxHP = 200;
	public static final boolean isDebugging = false;
	public static boolean AvailableWorld = false;

	public static List<Material> AllowedBlocks = new ArrayList<Material>();
	public Map<Player, Long> PlayerApple = new HashMap<Player, Long>();

	public static Logger log = Logger.getLogger("Minecraft");

	public void onEnable() {
		
		PluginDescriptionFile pdf = this.getDescription();
		versionStr = pdf.getVersion();

		boolean errorCheck = true;

		try {
			Class.forName("net.minecraft.server.v1_15_R1.EntityTypes");
		} catch (ClassNotFoundException e) {
			errorCheck = false;
			isInitDone = false;
		}
		if (errorCheck) {
			
			isInitDone = true;
			
			HerobrineAI.pluginCore = this;
			
			this.configdb = new ConfigDB(log);

			this.NPCman = new NPCCore(this);

			getServer().getPluginManager().registerEvents(new EntityListener(this), this);
			getServer().getPluginManager().registerEvents(new BlockListener(), this);
			getServer().getPluginManager().registerEvents(new InventoryListener(), this);
			getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
			getServer().getPluginManager().registerEvents(new WorldListener(), this);

			// Initialize PathManager

			this.pathMng = new PathManager();

			// Initialize AICore

			this.aicore = new AICore();

			// Initialize EntityManager

			this.entMng = new EntityManager();

			// Config loading

			configdb.Startup();
			configdb.Reload();

			// Spawn Herobrine

			Location nowloc = new Location((World) Bukkit.getServer().getWorlds().get(0), (float) 0, (float) -20,
					(float) 0);
			nowloc.setYaw((float) 1);
			nowloc.setPitch((float) 1);
			HerobrineSpawn(nowloc);

			HerobrineNPC.setItemInHand(configdb.ItemInHand.getItemStack());

			// Graveyard World

			if (this.configdb.UseGraveyardWorld == true && Bukkit.getServer().getWorld("world_herobrineai_graveyard") == null) {
				log.info("[HerobrineAI] Creating Graveyard world...");
				
				WorldCreator wc = new WorldCreator("world_herobrineai_graveyard");
				wc.generateStructures(false);
				org.bukkit.WorldType type = org.bukkit.WorldType.FLAT;
				wc.type(type);
				wc.createWorld();
				
				GraveyardWorld.Create();
			}
			log.info("[HerobrineAI] Plugin loaded! Version: ");

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

			/*
			 * Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this,
			 * new Runnable() { public void run() {
			 * 
			 * for (int i = 0;i<=configdb.useWorlds.size()-1;i++){ if
			 * (Bukkit.getServer().getWorlds().contains(Bukkit.getServer().
			 * getWorld(configdb.useWorlds.get(i)))){AvailableWorld=true;} } if
			 * (AvailableWorld==false){ log.warning(
			 * "**********************************************************");
			 * log.
			 * warning("[HerobrineAI] There are no available worlds for Herobrine!"
			 * ); log.warning(
			 * "**********************************************************");
			 * }else{ log.info(
			 * "**********************************************************");
			 * log.info("[HerobrineAI] No problems detected."); log.info(
			 * "**********************************************************"); }
			 * 
			 * 
			 * } }, 1 * 1L);
			 */

			pathUpdateINT = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
				public void run() {
					if (Utils.getRandomGen().nextInt(4) == 2 && HerobrineAI.getPluginCore().getAICore().getCoreTypeNow()
							.equals(CoreType.RANDOM_POSITION)) {
						pathMng.setPath(new Path(Utils.getRandomGen().nextInt(15) - 7f, Utils.getRandomGen().nextInt(15) - 7f, HerobrineAI.getPluginCore()));
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
			this.getCommand("hb-ai").setExecutor((CommandExecutor) new CmdExecutor(this));

			// Support initialize
			this.support = new Support();

			Class<?>[] argst = new Class[3];
			argst[0] = Class.class;
			argst[1] = String.class;
			argst[2] = int.class;

			if (!isNPCDisabled) {
				try {
					@SuppressWarnings("rawtypes")
					Class[] args = new Class[3];
					args[0] = Class.class;
					args[1] = String.class;
					args[2] = int.class;

					Method a = net.minecraft.server.v1_15_R1.EntityTypes.class.getDeclaredMethod("a", args);
					a.setAccessible(true);

					a.invoke(a, CustomZombie.class, "Zombie", 54);
					a.invoke(a, CustomSkeleton.class, "Skeleton", 51);
				} catch (Exception e) {
					e.printStackTrace();
					this.setEnabled(false);
				}
			} else {
				log.warning("[HerobrineAI] Custom NPCs have been disabled. (Incompatibility error!)");
			}
		} else {
			log.warning("[HerobrineAI] ******************ERROR******************");
			log.warning("[HerobrineAI] This version is only compatible with bukkit version " + bukkit_ver_string);
			log.warning("[HerobrineAI] *****************************************");
			this.setEnabled(false);
		}
	}

	public void onDisable() {

		if (isInitDone) {
			this.entMng.killAllMobs();
			Bukkit.getServer().getScheduler().cancelTask(pathUpdateINT);
			NPCman.DisableTask();
			aicore.CancelTarget(CoreType.ANY);
			aicore.Stop_BD();
			aicore.Stop_CG();
			aicore.Stop_MAIN();
			aicore.Stop_RC();
			aicore.Stop_RM();
			aicore.Stop_RP();
			aicore.Stop_RS();
			aicore.disableAll();
			log.info("[HerobrineAI] Plugin disabled!");
		}

	}

	public java.io.InputStream getInputStreamData(String src) {
		return HerobrineAI.class.getResourceAsStream(src);
	}

	public AICore getAICore() {

		return this.aicore;

	}

	public EntityManager getEntityManager() {
		return this.entMng;
	}

	public static HerobrineAI getPluginCore() {

		return HerobrineAI.pluginCore;

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
		
		if (configdb.UseIgnorePermission && player.hasPermission("hb-ai.ignore")) {
			ignoreCheck = false;
		}

		if (opCheck && creativeCheck && ignoreCheck) {
			return true;
		} else {
		
			if(sender == null){			
				if (!opCheck)
					log.info("[HerobrineAI] Player is an OP.");
				else if (!creativeCheck)
					log.info("[HerobrineAI] Player is in creative mode.");
				else if (!ignoreCheck)
					log.info("[HerobrineAI] Player has ignore permission.");			
			}else{
				if (!opCheck)
					sender.sendMessage(ChatColor.RED + "[HerobrineAI] Player is an OP.");
				else if (!creativeCheck)
					sender.sendMessage(ChatColor.RED + "[HerobrineAI] Player is in creative mode.");
				else if (!ignoreCheck)
					sender.sendMessage(ChatColor.RED + "[HerobrineAI] Player has ignore permission.");
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
		
		if (configdb.UseIgnorePermission && player.hasPermission("hb-ai.ignore")) {
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

}
