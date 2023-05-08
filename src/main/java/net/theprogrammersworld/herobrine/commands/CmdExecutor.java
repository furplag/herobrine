package net.theprogrammersworld.herobrine.commands;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import lombok.extern.slf4j.Slf4j;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.theprogrammersworld.herobrine.Herobrine;

@Slf4j(topic = "Minecraft")
public class CmdExecutor implements CommandExecutor {

	private HashMap<String, SubCommand> subCommands = new HashMap<String, SubCommand>();
	private String[] helpCommandOrder = {
			"reload", "pluginreport", "cancel", "allworlds", "location", "attack", "haunt", "heads",
			"bury", "curse", "burn", "pyramid", "cave", "temple", "graveyard", "speakrandom", "speak" };

	public CmdExecutor(Herobrine p) {

		subCommands.put("reload", new CmdReload(p));
		subCommands.put("cancel", new CmdCancel(p));
		subCommands.put("attack", new CmdAttack(p));
		subCommands.put("haunt", new CmdHaunt(p));
		subCommands.put("bury", new CmdBury(p));
		subCommands.put("pyramid", new CmdPyramid(p));
		subCommands.put("temple", new CmdTemple(p));
		subCommands.put("curse", new CmdCurse(p));
		subCommands.put("burn", new CmdBurn(p));
		subCommands.put("cave", new CmdCave(p));
		subCommands.put("graveyard", new CmdGraveyard(p));
		subCommands.put("allworlds", new CmdAllWorlds(p));
		subCommands.put("location", new CmdLocation(p));
		subCommands.put("heads", new CmdHeads(p));
		subCommands.put("speakrandom", new CmdSpeakRandom(p));
		subCommands.put("speak", new CmdSpeak(p));
		subCommands.put("pluginreport", new CmdPluginReport(p));
	}

	public void ShowHelp(Player player) {

		ArrayList<String> helpMessage = new ArrayList<String>();
		HashMap<String, String> helpMessageDesc = new HashMap<>();
		HashMap<String, String> permissionNode = new HashMap<>();

		helpMessage.add(ChatColor.GREEN + "/herobrine help");
		helpMessageDesc.put(helpMessage.get(0), ChatColor.GREEN + "Shows this list of Herobrine commands");

		for (String v : helpCommandOrder) {
			helpMessage.add(((SubCommand) subCommands.get(v)).help());
			helpMessageDesc.put(((SubCommand) subCommands.get(v)).help(), ((SubCommand) subCommands.get(v)).helpDesc());
			permissionNode.put(((SubCommand) subCommands.get(v)).help(), v);
		}

		if (player == null) {
			log.info("[Herobrine] Command List");
			for (String v : helpMessage)
				log.info(ChatColor.stripColor(v + " - " + helpMessageDesc.get(v)));
		}
		else {
			player.sendMessage(ChatColor.RED + "[Herobrine] Command List (hover over commands for more info)");
			for (String v : helpMessage) {
				if(player.hasPermission("herobrine." + permissionNode.get(v))) {
					TextComponent help = new TextComponent(v);
					help.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(helpMessageDesc.get(v))));
					player.spigot().sendMessage(help);
				}
			}
		}

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

		if (player != null && !player.hasPermission("herobrine." + args[0])) {
			player.sendMessage(ChatColor.RED + "Error: You do not have permission to use this command.");
			return true;
		}

		if (!subcmd.execute(player, args))
			if (player == null)
				log.info(ChatColor.stripColor("Usage: " + subcmd.help() + " - " + subcmd.helpDesc()));
			else
				player.sendMessage("Usage: " + subcmd.help() + " - " + subcmd.helpDesc());

		return true;

	}

}
