package net.theprogrammersworld.herobrine.commands;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.theprogrammersworld.herobrine.Herobrine;
import net.theprogrammersworld.herobrine.AI.Core.CoreType;

public class CmdBury extends SubCommand {

	public CmdBury(Herobrine plugin, Logger log) {
		super(plugin, log);
	}

	@Override
	public boolean execute(Player player, String[] args) {
		
		if (args.length > 1) {	
			
			Player target = Bukkit.getServer().getPlayer(args[1]);
			
			if (target == null) {
				sendMessage(player, ChatColor.RED + "[Herobrine] " + args[1] + " cannot be buried because they are offline.");
				return true;
			}
			
			if (!target.isOnline()) {
				sendMessage(player, ChatColor.RED + "[Herobrine] " + args[1] + " cannot be buried because they are offline.");
				return true;
			}
			
			if (!plugin.getSupport().checkHaunt(target.getLocation())) {
				sendMessage(player, ChatColor.RED + "[Herobrine] " + args[1] + " cannot be buried because they are in a secure area.");
				return true;
			}
			
			
			Object[] data = { target };
			
			if (plugin.getAICore().getCore(CoreType.BURY_PLAYER).RunCore(data).getResult()) 
				sendMessage(player, ChatColor.RED + "[Herobrine] Herobrine has buried " + args[1] + ".");
			else
				sendMessage(player, ChatColor.RED + "[Herobrine] " + args[1] + " could not be buried because there is no good place to bury them.");
					
			return true;
		}
		
		return false;
	}

	@Override
	public String help() {
		return ChatColor.GREEN + "/herobrine bury <player>";
	}

	@Override
	public String helpDesc() {
		return ChatColor.GREEN + "Buries the specified player";
	}

}
