package net.theprogrammersworld.herobrine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import lombok.extern.slf4j.Slf4j;
import net.theprogrammersworld.herobrine.misc.CustomID;

@Slf4j(topic = "Minecraft")
public class ConfigDB {

	public YamlConfiguration config;
	public YamlConfiguration npc;
	public int ShowRate = 2;
	public boolean HitPlayer = true;
	public boolean SendMessages = true;
	public boolean Lightning = true;
	public boolean DestroyTorches = true;
	public int DestroyTorchesRadius = 5;
	public int ShowInterval = 144000;
	public boolean TotemExplodes = true;
	public boolean OnlyWalkingMode = false;
	public boolean BuildStuff = true;
	public boolean PlaceSigns = true;
	public boolean UseTotem = true;
	public boolean WriteBooks = true;
	public boolean Killable = false;
	public boolean UsePotionEffects = true;
	public int CaveChance = 40;
	public int BookChance = 5;
	public int SignChance = 5;
	public String DeathMessage = "You cannot kill me!";
	public List<String> useWorlds = new ArrayList<String>();;
	public List<String> useMessages = new ArrayList<String>();;
	public List<String> useSignMessages = new ArrayList<String>();;
	public List<String> useBookMessages = new ArrayList<String>();;
	public boolean BuildPyramids = true;
	public int BuildPyramidOnChunkPercentage = 5;
	public boolean UseGraveyardWorld = true;
	public boolean BuryPlayers = true;
	public boolean SpawnWolves = true;
	public boolean SpawnBats = true;
	public boolean UseWalkingMode = true;
	public int WalkingModeXRadius = 1000;
	public int WalkingModeZRadius = 1000;
	public int WalkingModeFromXRadius = 0;
	public int WalkingModeFromZRadius = 0;
	public boolean BuildTemples = true;
	public int BuildTempleOnChunkPercentage = 5;
	public boolean UseArtifactBow = true;
	public boolean UseArtifactSword = true;
	public boolean UseArtifactApple = true;
	public boolean AttackCreative = true;
	public boolean AttackOP = true;
	public boolean SecuredArea_Build = true;
	public boolean SecuredArea_Attack = true;
	public boolean SecuredArea_Haunt = true;
	public boolean SecuredArea_Signs = true;
	public boolean SecuredArea_Books = true;
	public int HerobrineHP = 150;
	public int BuildInterval = 72000;
	public boolean UseHeads = true;
	public boolean UseAncientSword = true;
	public boolean UseNPC_Guardian = true;
	public boolean UseNPC_Warrior = true;
	public boolean UseNPC_Demon = true;
	public boolean SpawnDemonsOnPlayerBedEnter = true;
	public CustomID ItemInHand = null;
	public boolean Explosions = true;
	public boolean Burn = true;
	public boolean Curse = true;
	public int maxBooks = 1;
	public int maxSigns = 1;
	public int maxHeads = 1;
	public boolean UseIgnorePermission = true;
	public String HerobrineUUID = "f84c6a79-0a4e-45e0-879b-cd49ebd4c4e2";
	public String HerobrineName = "Herobrine";
	public String HerobrineWorldName = "herobrine";
	public boolean ShowInTabList = false;
	public boolean CheckForUpdates = true;

	public boolean newVersionFound = false;

	/* Starting with Minecraft 1.18, the Y coordinate used for the flat world generated for Herobrine's Graveyard is -61 instead of 3.
	 * This elevation change required graveyard generation as well as Herobrine and player location management to be shifted down to
	 * accommodate it. This change is only applied to graveyard worlds generated on 1.18 or newer, however, and would cause graveyards
	 * generated on older versions to not show Herobrine or the player in the right place. To work around this, we now use the
	 * "graveyardYCoord" variable to indicate the Y coordinate that should be used for Herobrine and the player when in the graveyard
	 * world. The default stored value is the one used for graveyard generated on 1.18 or newer. A conditional check will be used
	 * to change this value to 4 as part of the server launch process if doing so is appropriate. */
	public double graveyardYCoord = -60;

	private boolean isStartupDone = false;

	final public String pluginVersionString = Bukkit.getServer().getPluginManager().getPlugin("Herobrine")
			.getDescription().getVersion();

	public File configF = new File("plugins/Herobrine/config.yml");
	public File npcF = new File("plugins/Herobrine/npc.yml");

	public void Startup() {
		boolean configUpdated = false;
		new File("plugins/Herobrine/pregraveyard_caches").mkdirs();

		if (!configF.exists())
			try {
				configF.createNewFile();

			} catch (IOException e) {
				e.printStackTrace();
			}

		config = new YamlConfiguration();

		if (!npcF.exists())
			try {
				npcF.createNewFile();

			} catch (IOException e) {
				e.printStackTrace();
			}

		npc = new YamlConfiguration();

		try {
			config.load(configF);
			npc.load(npcF);
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (InvalidConfigurationException e) {

			e.printStackTrace();
		}

		if (!npc.contains("npc.Guardian")) {
			npc.set("npc.Guardian.SpawnCount", 1);
			npc.set("npc.Guardian.HP", 40);
			npc.set("npc.Guardian.Speed", 0.3);
			npc.set("npc.Guardian.Drops.GOLDEN_SWORD.Chance", 40);
			npc.set("npc.Guardian.Drops.GOLDEN_SWORD.Count", 1);
			npc.set("npc.Guardian.Drops.GOLDEN_AXE.Chance", 30);
			npc.set("npc.Guardian.Drops.GOLDEN_AXE.Count", 1);
			npc.set("npc.Warrior.SpawnChance", 4);
			npc.set("npc.Warrior.HP", 40);
			npc.set("npc.Warrior.Speed", 0.3);
			npc.set("npc.Warrior.Drops.IRON_CHESTPLATE.Chance", 25);
			npc.set("npc.Warrior.Drops.IRON_CHESTPLATE.Count", 1);
			npc.set("npc.Warrior.Drops.IRON_HELMET.Chance", 20);
			npc.set("npc.Warrior.Drops.IRON_HELMET.Count", 1);
			npc.set("npc.Demon.SpawnChance", 4);
			npc.set("npc.Demon.HP", 40);
			npc.set("npc.Demon.Speed", 0.3);
			npc.set("npc.Demon.Drops.GOLDEN_APPLE.Chance", 40);
			npc.set("npc.Demon.Drops.GOLDEN_APPLE.Count", 1);
			npc.set("npc.Demon.Drops.SKELETON_SKULL.Chance", 20);
			npc.set("npc.Demon.Drops.SKELETON_SKULL.Count", 1);

			try {
				npc.save(npcF);
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

		if (!config.contains("config.ShowRate")) {

			useWorlds.add("world");
			useMessages.add("Even Notch can't save you now!");
			useMessages.add("Fear me!");
			useMessages.add("Welcome to my world!");
			useMessages.add("I am your death!");
			useMessages.add("Grave awaits you!");
			useSignMessages.add("I'm watching.");
			useSignMessages.add("Death...");
			useSignMessages.add("Eyes in dark...");
			useBookMessages.add("White eyes in dark...");
			useBookMessages.add("... was last what I saw ...");
			useBookMessages.add("... before i was dead.");

			config.set("config.ShowInterval", 144000);
			config.set("config.ShowRate", 2);
			config.set("config.HitPlayer", true);
			config.set("config.SendMessages", true);
			config.set("config.Lightning", false);
			config.set("config.DestroyTorches", true);
			config.set("config.DestroyTorchesRadius", 5);
			config.set("config.Worlds", useWorlds);
			config.set("config.TotemExplodes", true);
			config.set("config.OnlyWalkingMode", false);
			config.set("config.BuildStuff", true);
			config.set("config.PlaceSigns", true);
			config.set("config.UseTotem", true);
			config.set("config.WriteBooks", true);
			config.set("config.Killable", false);
			config.set("config.UsePotionEffects", true);
			config.set("config.CaveChance", 40);
			config.set("config.BookChance", 5);
			config.set("config.SignChance", 5);
			config.set("config.DeathMessage", "You cannot kill me!");
			config.set("config.Messages", useMessages);
			config.set("config.SignMessages", useSignMessages);
			config.set("config.BookMessages", useBookMessages);
			config.set("config.Drops.DIAMOND.count", 1);
			config.set("config.Drops.DIAMOND.chance", 20);
			config.set("config.BuildPyramids", true);
			config.set("config.BuildPyramidOnChunkPercentage", 5);
			config.set("config.UseGraveyardWorld", true);
			config.set("config.BuryPlayers", true);
			config.set("config.SpawnWolves", true);
			config.set("config.SpawnBats", true);
			config.set("config.UseWalkingMode", true);
			config.set("config.WalkingModeRadius.X", 1000);
			config.set("config.WalkingModeRadius.Z", 1000);
			config.set("config.WalkingModeRadius.FromX", 0);
			config.set("config.WalkingModeRadius.FromZ", 0);
			config.set("config.BuildInterval", 72000);
			config.set("config.BuildTemples", true);
			config.set("config.BuildTempleOnChunkPercentage", 5);
			config.set("config.UseArtifacts.Bow", true);
			config.set("config.UseArtifacts.Sword", true);
			config.set("config.UseArtifacts.Apple", true);
			config.set("config.HerobrineHP", 300);
			config.set("config.AttackCreative", true);
			config.set("config.AttackOP", true);
			config.set("config.SecuredArea.Build", true);
			config.set("config.SecuredArea.Attack", true);
			config.set("config.SecuredArea.Haunt", true);
			config.set("config.SecuredArea.Signs", true);
			config.set("config.SecuredArea.Books", true);
			config.set("config.UseHeads", true);
			config.set("config.UseAncientSword", true);
			config.set("config.UseNPC.Guardian", true);
			config.set("config.UseNPC.Warrior", true);
			config.set("config.UseNPC.Demon", true);
			config.set("config.SpawnDemonsOnPlayerBedEnter", true);
			config.set("config.ItemInHand", "AIR");
			config.set("config.Explosions", true);
			config.set("config.Burn", true);
			config.set("config.Curse", true);
			config.set("config.Limit.Books", 1);
			config.set("config.Limit.Signs", 1);
			config.set("config.Limit.Heads", 1);
			config.set("config.UseIgnorePermission", true);
			config.set("config.HerobrineUUID", "f84c6a79-0a4e-45e0-879b-cd49ebd4c4e2");
			config.set("config.HerobrineName", "Herobrine");
			config.set("config.HerobrineWorldName", "world_herobrine_graveyard");
			config.set("config.ShowInTabList", false);
			config.set("config.CheckForUpdates", true);

			try {
				config.save(configF);
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		else if(!config.contains("config.SpawnDemonsOnPlayerBedEnter")) {
			config.set("config.SpawnDemonsOnPlayerBedEnter", true);
			configUpdated = true;
		}

		if(configUpdated) {
			try {
				config.save(configF);
				log.info("[Herobrine] The Herobrine configuration file was updated with new parameters for Herobrine v" + pluginVersionString + ".");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Reload();
	}

	public void Reload() {

		try {
			config.load(configF);
			npc.load(npcF);
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (InvalidConfigurationException e) {

			e.printStackTrace();
		}

		ShowInterval = config.getInt("config.ShowInterval");
		ShowRate = config.getInt("config.ShowRate");
		HitPlayer = config.getBoolean("config.HitPlayer");
		SendMessages = config.getBoolean("config.SendMessages");
		Lightning = config.getBoolean("config.Lightning");
		DestroyTorches = config.getBoolean("config.DestroyTorches");
		DestroyTorchesRadius = config.getInt("config.DestroyTorchesRadius");
		useWorlds = config.getStringList("config.Worlds");
		TotemExplodes = config.getBoolean("config.TotemExplodes");
		OnlyWalkingMode = config.getBoolean("config.OnlyWalkingMode");
		BuildStuff = config.getBoolean("config.BuildStuff");
		PlaceSigns = config.getBoolean("config.PlaceSigns");
		UseTotem = config.getBoolean("config.UseTotem");
		WriteBooks = config.getBoolean("config.WriteBooks");
		Killable = config.getBoolean("config.Killable");
		UsePotionEffects = config.getBoolean("config.UsePotionEffects");
		CaveChance = config.getInt("config.CaveChance");
		BookChance = config.getInt("config.BookChance");
		SignChance = config.getInt("config.SignChance");
		DeathMessage = config.getString("config.DeathMessage");
		useMessages = config.getStringList("config.Messages");
		useSignMessages = config.getStringList("config.SignMessages");
		useBookMessages = config.getStringList("config.BookMessages");
		BuildPyramids = config.getBoolean("config.BuildPyramids");
		BuildPyramidOnChunkPercentage = config.getInt("config.BuildPyramidOnChunkPercentage");
		UseGraveyardWorld = config.getBoolean("config.UseGraveyardWorld");
		BuryPlayers = config.getBoolean("config.BuryPlayers");
		SpawnWolves = config.getBoolean("config.SpawnWolves");
		SpawnBats = config.getBoolean("config.SpawnBats");
		UseWalkingMode = config.getBoolean("config.UseWalkingMode");
		WalkingModeXRadius = config.getInt("config.WalkingModeRadius.X");
		WalkingModeZRadius = config.getInt("config.WalkingModeRadius.Z");
		WalkingModeFromXRadius = config.getInt("config.WalkingModeRadius.FromX");
		WalkingModeFromZRadius = config.getInt("config.WalkingModeRadius.FromZ");
		BuildInterval = config.getInt("config.BuildInterval");
		BuildTemples = config.getBoolean("config.BuildTemples");
		BuildTempleOnChunkPercentage = config.getInt("config.BuildTempleOnChunkPercentage");
		UseArtifactBow = config.getBoolean("config.UseArtifacts.Bow");
		UseArtifactSword = config.getBoolean("config.UseArtifacts.Sword");
		UseArtifactApple = config.getBoolean("config.UseArtifacts.Apple");
		HerobrineHP = config.getInt("config.HerobrineHP");
		AttackCreative = config.getBoolean("config.AttackCreative");
		AttackOP = config.getBoolean("config.AttackOP");
		SecuredArea_Build = config.getBoolean("config.SecuredArea.Build");
		SecuredArea_Attack = config.getBoolean("config.SecuredArea.Attack");
		SecuredArea_Haunt = config.getBoolean("config.SecuredArea.Haunt");
		SecuredArea_Signs = config.getBoolean("config.SecuredArea.Signs");
		SecuredArea_Books = config.getBoolean("config.SecuredArea.Books");
		UseHeads = config.getBoolean("config.UseHeads");
		UseAncientSword = config.getBoolean("config.UseAncientSword");
		UseNPC_Guardian = config.getBoolean("config.UseNPC.Guardian");
		UseNPC_Warrior = config.getBoolean("config.UseNPC.Warrior");
		UseNPC_Demon = config.getBoolean("config.UseNPC.Demon");
		SpawnDemonsOnPlayerBedEnter = config.getBoolean("config.SpawnDemonsOnPlayerBedEnter");
		ItemInHand = new CustomID(config.getString("config.ItemInHand"));
		Explosions = config.getBoolean("config.Explosions");
		Burn = config.getBoolean("config.Burn");
		Curse = config.getBoolean("config.Curse");
		maxBooks = config.getInt("config.Limit.Books");
		maxSigns = config.getInt("config.Limit.Signs");
		maxHeads = config.getInt("config.Limit.Heads");
		UseIgnorePermission = config.getBoolean("config.UseIgnorePermission");
		HerobrineUUID = config.getString("config.HerobrineUUID");
		HerobrineName = config.getString("config.HerobrineName");
		HerobrineWorldName = config.getString("config.HerobrineWorldName");
		ShowInTabList = config.getBoolean("config.ShowInTabList");
		CheckForUpdates = this.config.getBoolean("config.CheckForUpdates");

		Herobrine.HerobrineMaxHP = HerobrineHP;
		Herobrine.getPluginCore().getAICore().Stop_MAIN();
		Herobrine.getPluginCore().getAICore().Start_MAIN();
		Herobrine.getPluginCore().getAICore().Stop_BD();
		Herobrine.getPluginCore().getAICore().Start_BD();
		Herobrine.getPluginCore().getAICore().Stop_RC();
		Herobrine.getPluginCore().getAICore().Start_RC();
		Herobrine.AvailableWorld = false;
		Herobrine.getPluginCore().getAICore().getResetLimits().updateFromConfig();

		if (Herobrine.getPluginCore().HerobrineNPC != null)
			Herobrine.getPluginCore().HerobrineNPC.setItemInHand(ItemInHand.getItemStack());

		if (isStartupDone) {

			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Herobrine.getPluginCore(), new Runnable() {
				public void run() {
					for (int i = 0; i <= useWorlds.size() - 1; i++)
						if (Bukkit.getServer().getWorlds().contains(Bukkit.getServer().getWorld(useWorlds.get(i))))
							Herobrine.AvailableWorld = true;

					if (Herobrine.AvailableWorld == false)
						log.warn("[Herobrine] There are no worlds available for Herobrine to spawn in.");
				}
			}, 1L);

		}
		isStartupDone = true;
	}

	public void addAllWorlds() {

		ArrayList<String> allWorlds = new ArrayList<String>();
		List<World> worlds_ = Bukkit.getWorlds();
		for (int i = 0; i <= worlds_.size() - 1; i++) {
			if (!worlds_.get(i).getName().equalsIgnoreCase(HerobrineWorldName)) {
				allWorlds.add(worlds_.get(i).getName());
			}
		}

		try {
			config.load(configF);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}

		config.set("config.Worlds", allWorlds);

		try {
			config.save(configF);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Reload();

	}

}
