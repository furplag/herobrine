package net.theprogrammersworld.herobrine.commands;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.theprogrammersworld.herobrine.Herobrine;

public class CmdAllWorlds extends SubCommand {

	public CmdAllWorlds(Herobrine plugin, Logger log) {
		super(plugin, log);
	}

	@Override
	public boolean execute(Player player, String[] args) {
		
		plugin.getConfigDB().addAllWorlds();
		sendMessage(player, ChatColor.GREEN + "[Herobrine] All worlds have been added to config.");
		sendMessage(player, ChatColor.YELLOW + "[Herobrine] Note: Worlds with blank spaces can cause problems!");
		
		return true;
	}

	@Override
	public String help() {
		return ChatColor.GREEN + "/hb-ai allworlds";
	}

}
