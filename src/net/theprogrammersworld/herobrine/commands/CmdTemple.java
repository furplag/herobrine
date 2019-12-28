package net.theprogrammersworld.herobrine.commands;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.theprogrammersworld.herobrine.HerobrineAI;
import net.theprogrammersworld.herobrine.AI.Core.CoreType;

public class CmdTemple extends SubCommand {

	public CmdTemple(HerobrineAI plugin, Logger log) {
		super(plugin, log);
	}

	@Override
	public boolean execute(Player player, String[] args) {
		
		if (args.length > 1) {

			Player target = Bukkit.getServer().getPlayer(args[1]);
			
			if (target == null) {
				sendMessage(player, ChatColor.RED + "[HerobrineAI] Player is offline.");
				return true;
			}
			
			if (!target.isOnline()) {
				sendMessage(player, ChatColor.RED + "[HerobrineAI] Player is offline.");
				return true;
			}
			
			if (!plugin.getSupport().checkBuild(target.getLocation())) {
				sendMessage(player, ChatColor.RED + "[HerobrineAI] Player is in secure area.");
				return true;
			}	
			
			Object[] data = { target }; 

			if (plugin.getAICore().getCore(CoreType.TEMPLE).RunCore(data).getResult()) 
				sendMessage(player, ChatColor.RED + "[HerobrineAI] Creating temple near " + args[1] + "!");
			else
				sendMessage(player, ChatColor.RED + "[HerobrineAI] Cannot find good place for temple!");
				
			
			return true;

		}
		
		return false;
	}

	@Override
	public String help() {
		return ChatColor.GREEN + "/hb-ai temple <player name>";
	}

}
