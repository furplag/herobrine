package net.theprogrammersworld.herobrine.commands;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.theprogrammersworld.herobrine.Herobrine;
import net.theprogrammersworld.herobrine.AI.Core.CoreType;

public class CmdCave extends SubCommand {

	public CmdCave(Herobrine plugin, Logger log) {
		super(plugin, log);
	}

	@Override
	public boolean execute(Player player, String[] args) {
		
		if (args.length > 1) {

			Player target = Bukkit.getServer().getPlayer(args[1]);
			
			if (target == null) {
				sendMessage(player, ChatColor.RED + "[Herobrine] A cave could not be created near " + args[1] + " because they are offline.");
				return true;
			}
			
			if (!target.isOnline()) {
				sendMessage(player, ChatColor.RED + "[Herobrine] A cave could not be created near " + args[1] + " because they are offline.");
				return true;
			}
			
			if (!plugin.getSupport().checkBuild(target.getLocation())) {
				sendMessage(player, ChatColor.RED + "[Herobrine] A cave could not be created near " + args[1] + " because they are in a secure area.");
				return true;
			}
			
			Object[] data = { target.getLocation(), true };
			sendMessage(player, ChatColor.RED + "[Herobrine] " + plugin.getAICore().getCore(CoreType.BUILD_CAVE).RunCore(data).getResultString());
			
			return true;
		}
		
		return false;
	}

	@Override
	public String help() {
		return ChatColor.GREEN + "/herobrine cave <player>";
	}

	@Override
	public String helpDesc() {
		return ChatColor.GREEN + "Creates a cave near the specified player";
	}

}
