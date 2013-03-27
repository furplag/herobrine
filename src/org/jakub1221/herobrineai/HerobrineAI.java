package org.jakub1221.herobrineai;

import java.io.IOException;
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
import org.bukkit.plugin.java.JavaPlugin;

import org.jakub1221.herobrineai.NPC.AI.PathManager;
import org.jakub1221.herobrineai.NPC.Entity.HumanNPC;
import org.jakub1221.herobrineai.NPC.NPCCore;
import org.jakub1221.herobrineai.AI.AICore;
import org.jakub1221.herobrineai.AI.extensions.GraveyardWorld;
import org.jakub1221.herobrineai.commands.CmdExecutor;
import org.jakub1221.herobrineai.entity.CustomZombie;
import org.jakub1221.herobrineai.entity.EntityManager;
import org.jakub1221.herobrineai.listeners.BlockListener;
import org.jakub1221.herobrineai.listeners.EntityListener;
import org.jakub1221.herobrineai.listeners.InventoryListener;
import org.jakub1221.herobrineai.listeners.PlayerListener;
import org.jakub1221.herobrineai.listeners.WorldListener;



public class HerobrineAI extends JavaPlugin implements Listener{

	private static HerobrineAI pluginCore;
	private AICore aicore;
	private ConfigDB configdb;
	private Support support;
	private EntityManager entMng;
	private PathManager pathMng;
	public int build=3123;
	public String versionStr="3.0.1";
	public java.io.InputStream data_temple=HerobrineAI.class.getResourceAsStream("/res/temple.yml");
	public java.io.InputStream data_graveyard_world=HerobrineAI.class.getResourceAsStream("/res/graveyard_world.yml");
	
	public static int HerobrineHP=200;
	public static int HerobrineMaxHP=200;
	public static boolean isDebugging=false;
	
	public static NPCCore NPCman;
	public static HumanNPC HerobrineNPC;
	public static long HerobrineEntityID;
	
	public static boolean AvailableWorld = false;
	
	public static List<Material> AllowedBlocks = new ArrayList<Material>();
	public static List<Material> StandBlocks = new ArrayList<Material>();
	public static List<Material> NonStandBlocks = new ArrayList<Material>();
	public Map<Player,Long> PlayerApple = new HashMap<Player,Long>();
	
	public static Logger log = Logger.getLogger("Minecraft");
	
	public void onEnable(){
		
		NPCman = new NPCCore(this);
		HerobrineAI.pluginCore = this;
		this.configdb = new ConfigDB(log);
		
	
		getServer().getPluginManager().registerEvents(new EntityListener(), this);
		getServer().getPluginManager().registerEvents(new BlockListener(), this);
		getServer().getPluginManager().registerEvents(new InventoryListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		getServer().getPluginManager().registerEvents(new WorldListener(), this);
		
		// Metrics
		
		try {
		    Metrics metrics = new Metrics(this);
		    metrics.start();
		} catch (IOException e) {
		 
		}
		
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
		
		Location nowloc = new Location((World) Bukkit.getServer().getWorlds().get(0),(float) 0,(float) -20,(float) 0);
		   nowloc.setYaw((float) 1);
		    nowloc.setPitch((float) 1);
            HerobrineSpawn(nowloc);
			
            HerobrineNPC.setItemInHand(configdb.ItemInHand.getItemStack());
			
          //Graveyard World
            
			if (this.configdb.UseGraveyardWorld==true){
			if (Bukkit.getServer().getWorld("world_herobrineai_graveyard")==null){
				log.info("[HerobrineAI] Creating Graveyard world...");
				  WorldCreator wc = new WorldCreator("world_herobrineai_graveyard");
				  wc.generateStructures(false);
				org.bukkit.WorldType type = org.bukkit.WorldType.FLAT;
			       wc.type(type);
			        wc.createWorld();
			        GraveyardWorld.Create();
				}
			
			}
			log.info("[HerobrineAI] Plugin loaded! Version: "+versionStr+" / Build: "+build);
			
			// Init Block Types
		
			StandBlocks.add(Material.STONE);
			StandBlocks.add(Material.getMaterial(2));
			StandBlocks.add(Material.GRAVEL);
			StandBlocks.add(Material.SAND);
			StandBlocks.add(Material.DIRT);
			StandBlocks.add(Material.SANDSTONE);
			StandBlocks.add(Material.GLASS);
			StandBlocks.add(Material.CLAY);
			StandBlocks.add(Material.COBBLESTONE);
			StandBlocks.add(Material.ICE);
			StandBlocks.add(Material.getMaterial(33));
			StandBlocks.add(Material.getMaterial(29));
			StandBlocks.add(Material.getMaterial(35));
			StandBlocks.add(Material.getMaterial(57));
			StandBlocks.add(Material.getMaterial(41));
			StandBlocks.add(Material.getMaterial(42));
			
			NonStandBlocks.add(Material.AIR);
			NonStandBlocks.add(Material.GRASS);
			NonStandBlocks.add(Material.SNOW);
			
			AllowedBlocks.add(Material.AIR);
			AllowedBlocks.add(Material.SNOW);
			AllowedBlocks.add(Material.getMaterial(31));
			AllowedBlocks.add(Material.RAILS);
			AllowedBlocks.add(Material.getMaterial(32));
			AllowedBlocks.add(Material.getMaterial(37));
			AllowedBlocks.add(Material.getMaterial(38));
			AllowedBlocks.add(Material.getMaterial(70));
			AllowedBlocks.add(Material.getMaterial(72));
			AllowedBlocks.add(Material.getMaterial(106));
			AllowedBlocks.add(Material.TORCH);
			AllowedBlocks.add(Material.REDSTONE);
			AllowedBlocks.add(Material.REDSTONE_TORCH_ON);
			AllowedBlocks.add(Material.REDSTONE_TORCH_OFF);
			AllowedBlocks.add(Material.LEVER);
			AllowedBlocks.add(Material.getMaterial(77));
			AllowedBlocks.add(Material.LADDER);

	       for (int i = 0;i<=configdb.useWorlds.size()-1;i++){
	    	   if (Bukkit.getServer().getWorlds().contains(Bukkit.getServer().getWorld(configdb.useWorlds.get(i)))){AvailableWorld=true;}
	       }
			if (AvailableWorld==false){
				log.info("**********************************************************");
				log.info("[HerobrineAI] There are no available worlds for Herobrine!");
				log.info("**********************************************************");
				}
			
			
		
			Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(AICore.plugin, new Runnable() {
		        public void run() {
		        	
				    }
		        }, 1 * 35L, 1 * 35L);
			
			Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
		        public void run() {
		        	pathMng.update();
				    }
		        }, 1 * 5L, 1 * 5L);
			
		
			//Command Executors
			this.getCommand("hb").setExecutor((CommandExecutor) new CmdExecutor(this));
			this.getCommand("hb-ai").setExecutor((CommandExecutor) new CmdExecutor(this));
			
			//Support initialize
			this.support = new Support();
			
			
			//Register mobs
			try{
		        @SuppressWarnings("rawtypes")
		        Class[] args = new Class[3];
		        args[0] = Class.class;
		        args[1] = String.class;
		        args[2] = int.class;

		        Method a = net.minecraft.server.v1_5_R2.EntityTypes.class.getDeclaredMethod("a", args);
		        a.setAccessible(true);

		        a.invoke(a, CustomZombie.class, "Zombie", 54);
		    }catch (Exception e){
		        e.printStackTrace();
		        this.setEnabled(false);
		    }
		

			
	}	
	
	
	public void onDisable(){
		this.entMng.killAllMobs();
		NPCman.DisableTask();
		log.info("[HerobrineAI] Plugin disabled!");
		
		
	}
	
	public AICore getAICore(){
		
		return this.aicore;
		
	}
	
	public EntityManager getEntityManager(){
		return this.entMng;
	}
	
	public static HerobrineAI getPluginCore(){
		
		return HerobrineAI.pluginCore;
		
	}
	
	public void HerobrineSpawn(Location loc){
	     HerobrineNPC = (HumanNPC) NPCman.spawnHumanNPC(ChatColor.WHITE+"Herobrine", loc);
	     HerobrineEntityID=HerobrineNPC.getBukkitEntity().getEntityId();
	   
	}
	public void HerobrineRemove(){
		
		HerobrineEntityID=0;
		HerobrineNPC=null;
		NPCman.removeAll();
		
	}
    public ConfigDB getConfigDB(){
    	return this.configdb;
    }
    public String getVersionStr(){
    	return versionStr;
    }
    public Support getSupport(){
    	return this.support;
    }
	public PathManager getPathManager(){
		return this.pathMng;
	}
    public boolean canAttackPlayer(Player player,Player sender){
 
    	if (player.isOp()){
    		if (configdb.AttackOP){
    			 if (player.getGameMode()==GameMode.CREATIVE){
    				 if (configdb.AttackCreative){
    					 return true;
    				 }else{sender.sendMessage(ChatColor.RED+"[HerobrineAI] Player is in Creative mode.");return false;}
    			 }
    			return true;
    		}else{sender.sendMessage(ChatColor.RED+"[HerobrineAI] Player is OP.");return false;}
    	}else if (player.hasPermission("hb-ai.ignore")){
    		sender.sendMessage(ChatColor.RED+"[HerobrineAI] Player has ignore permission.");
    		return false;
    	}else if (player.getGameMode()==GameMode.CREATIVE){
    		if (configdb.AttackCreative){
    			return true;
    		}else{sender.sendMessage(ChatColor.RED+"[HerobrineAI] Player is in Creative mode.");return false;}
    	}else{return true;}
    	
    }
    public boolean canAttackPlayerConsole(Player player){
    	
    	if (player.isOp()){
    		if (configdb.AttackOP){
    			 if (player.getGameMode()==GameMode.CREATIVE){
    				 if (configdb.AttackCreative){
    					 return true;
    				 }else{log.info("[HerobrineAI] Player is in Creative mode.");return false;}
    			 }
    			return true;
    		}else{log.info("[HerobrineAI] Player is OP.");return false;}
    	}else if (player.hasPermission("hb-ai.ignore")){
    		log.info("[HerobrineAI] Player has ignore permission.");
    		return false;
    	}else if (player.getGameMode()==GameMode.CREATIVE){
    		if (configdb.AttackCreative){
    			return true;
    		}else{log.info("[HerobrineAI] Player is in Creative mode.");return false;}
    	}else{return true;}
    	
    }
	
    public boolean canAttackPlayerNoMSG(Player player){
    	if (player.isOp()){
    		if (configdb.AttackOP){
    			 if (player.getGameMode()==GameMode.CREATIVE){
    				 if (configdb.AttackCreative){
    					 return true;
    				 }else{return false;}
    			 }
    			return true;
    		}else{return false;}
    	}else if (player.hasPermission("hb-ai.ignore")){
    		return false;
    	}else if (player.getGameMode()==GameMode.CREATIVE){
    		if (configdb.AttackCreative){
    			return true;
    		}else{return false;}
    	}else{return true;}
    	
    }
    public String getAvailableWorldString(){
    	if (AvailableWorld){
    		return "Yes";
    	}else{
    		return "No";
    	}
    }

}

