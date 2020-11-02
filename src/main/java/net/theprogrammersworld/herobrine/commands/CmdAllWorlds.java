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
		sendMessage(player, ChatColor.GREEN + "[Herobrine] All worlds have been added to the configuration file. Herobrine can now access all of the server's worlds.");
		
		return true;
	}

	@Override
	public String help() {
		return ChatColor.GREEN + "/herobrine allworlds";
	}

	@Override
	public String helpDesc() {
		return ChatColor.GREEN + "Grants Herobrine access to all worlds";
	}

}
