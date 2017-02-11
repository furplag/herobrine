package org.jakub1221.herobrineai.commands;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.AICore;

public class CmdHaunt extends SubCommand {

	public CmdHaunt(HerobrineAI plugin, Logger log) {
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
			
			if (AICore.isTarget == false) {			
				plugin.getAICore().setHauntTarget(target);
				sendMessage(player, ChatColor.RED + "[HerobrineAI] Herobrine is now haunting " + args[1] + "!");
				
			} else {
				sendMessage(player,ChatColor.RED + "[HerobrineAI] Herobrine already has target! Use "
								  + ChatColor.GREEN + "/hb-ai cancel" + ChatColor.RED
                                  + " to cancel current target.");
			}
			
			return true;
			
		}
		
		return false;
	}

	@Override
	public String help() {	
		return ChatColor.GREEN + "/hb-ai haunt <player name>";
	}

}
