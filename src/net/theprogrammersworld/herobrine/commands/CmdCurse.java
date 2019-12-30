package net.theprogrammersworld.herobrine.commands;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.theprogrammersworld.herobrine.Herobrine;
import net.theprogrammersworld.herobrine.AI.Core.CoreType;

public class CmdCurse extends SubCommand {

	public CmdCurse(Herobrine plugin, Logger log) {
		super(plugin, log);
	}

	@Override
	public boolean execute(Player player, String[] args) {
		
		if (args.length > 1) {

			
			Player target = Bukkit.getServer().getPlayer(args[1]);
			
			if (target == null) {
				sendMessage(player, ChatColor.RED + "[Herobrine] " + args[1] + " cannot be cursed because they are offline.");
				return true;
			}
			
			if (!target.isOnline()) {
				sendMessage(player, ChatColor.RED + "[Herobrine] " + args[1] + " cannot be cursed because they are offline.");
				return true;
			}

			Object[] data = { target };
			sendMessage(player, ChatColor.RED + "[Herobrine] " + plugin.getAICore().getCore(CoreType.CURSE).RunCore(data).getResultString());	
		
			return true;
		}
		
		return false;
	}

	@Override
	public String help() {
		return ChatColor.GREEN + "/herobrine curse <player>";
	}

	@Override
	public String helpDesc() {
		return ChatColor.GREEN + "Places a curse on the specified player";
	}

}
