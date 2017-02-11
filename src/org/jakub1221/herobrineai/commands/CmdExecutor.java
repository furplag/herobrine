package org.jakub1221.herobrineai.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.AICore;
import org.jakub1221.herobrineai.AI.Core.CoreType;

public class CmdExecutor implements CommandExecutor {

	@Deprecated
	private HerobrineAI P_Core = null;

	private Logger log = null;
	private HashMap<String, SubCommand> subCommands = new HashMap<String, SubCommand>();
	private String[] helpCommandOrder = {
			"reload", "cancel", "allworlds", "position", "attack", "haunt", "heads", 
			"bury", "curse", "burn", "pyramid", "cave", "temple", "graveyard" };

	public CmdExecutor(HerobrineAI p) {
		log = p.log;

		subCommands.put("reload", new CmdReload(p, log));
		subCommands.put("cancel", new CmdCancel(p, log));
		subCommands.put("attack", new CmdAttack(p, log));
		subCommands.put("haunt", new CmdHaunt(p, log));
		subCommands.put("bury", new CmdBury(p, log));
		subCommands.put("pyramid", new CmdPyramid(p, log));
		subCommands.put("temple", new CmdTemple(p, log));
		subCommands.put("curse", new CmdCurse(p, log));
		subCommands.put("burn", new CmdBurn(p, log));
		subCommands.put("cave", new CmdCave(p, log));
		subCommands.put("graveyard", new CmdGraveyard(p, log));
		subCommands.put("allworlds", new CmdAllWorlds(p, log));
		subCommands.put("position", new CmdPosition(p, log));
		subCommands.put("heads", new CmdHeads(p, log));
	}

	public void ShowHelp(Player player) {

		ArrayList<String> helpMessage = new ArrayList<String>();

		helpMessage.add(ChatColor.GREEN + "[HerobrineAI] Command list");
		helpMessage.add(ChatColor.GREEN + "/hb-ai help - shows all commands");

		for (String v : helpCommandOrder)
			helpMessage.add(((SubCommand) subCommands.get(v)).help());

		if (player == null)
			for (String v : helpMessage)
				log.info(ChatColor.stripColor(v));
		else
			for (String v : helpMessage)
				player.sendMessage(v);

	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		if (args.length == 0)
			return false;

		Player player = null;

		if (sender instanceof Player)
			player = (Player) sender;

		SubCommand subcmd = subCommands.get(args[0]);

		if (subcmd == null && args[0].equals("help")) {
			ShowHelp(player);
			return true;
		} else if (subcmd == null)
			return false;

		if (player != null && !player.hasPermission("hb-ai." + args[0])) {
			player.sendMessage(ChatColor.RED + "Insufficient permission!");
			return true;
		}

		if (!subcmd.execute(player, args))
			if (player == null)
				log.info("Usage: " + subcmd.help());
			else
				player.sendMessage("Usage: " + subcmd.help());

		return true;

	}

}
