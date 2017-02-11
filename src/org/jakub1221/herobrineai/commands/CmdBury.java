package org.jakub1221.herobrineai.commands;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.Core.CoreType;

public class CmdBury extends SubCommand {

	public CmdBury(HerobrineAI plugin, Logger log) {
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
			
			if (!plugin.getSupport().checkHaunt(target.getLocation())) {
				sendMessage(player, ChatColor.RED + "[HerobrineAI] Player is in secure area.");
				return true;
			}
			
			
			Object[] data = { target };
			
			if (plugin.getAICore().getCore(CoreType.BURY_PLAYER).RunCore(data).getResult()) 
				sendMessage(player, ChatColor.RED + "[HerobrineAI] Buried " + args[1] + "!");
			else
				sendMessage(player, ChatColor.RED + "[HerobrineAI] Cannot find good place!");
					
			return true;
		}
		
		return false;
	}

	@Override
	public String help() {
		return ChatColor.GREEN + "/hb-ai bury <player name>";
	}

}
