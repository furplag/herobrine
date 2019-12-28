package net.theprogrammersworld.herobrine.commands;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.theprogrammersworld.herobrine.Herobrine;
import net.theprogrammersworld.herobrine.AI.AICore;

public class CmdAttack extends SubCommand {

	public CmdAttack(Herobrine plugin, Logger log) {
		super(plugin, log);
	}

	@Override
	public boolean execute(Player player, String[] args) {
		
		if (args.length > 1) {
			
			Player target = Bukkit.getServer().getPlayer(args[1]);
			
			if (target == null) {
				sendMessage(player, ChatColor.RED + "[Herobrine] Player is offline.");
				return true;
			}
			
			if (!target.isOnline()) {
				sendMessage(player, ChatColor.RED + "[Herobrine] Player is offline.");
				return true;
			}
			
			if (!plugin.canAttackPlayer(target, player)) {
				return true;
			}
			
			if (!plugin.getSupport().checkAttack(target.getLocation())) {
				sendMessage(player, ChatColor.RED + "[Herobrine] Player is in secure area.");
				return true;
			}
			
			if (AICore.isTarget == false) {
				
				plugin.getAICore().setAttackTarget(target);		
				sendMessage(player, ChatColor.RED + "[Herobrine] Herobrine is now attacking the " + args[1] + "!");
				
			} else {
				sendMessage(player,ChatColor.RED
								   + "[Herobrine] Herobrine already has target! Use "
								   + ChatColor.GREEN + "/hb-ai cancel" + ChatColor.RED
								   + " to cancel current target");
			}
			
			return true;
		} 
		
		return false;
	}

	@Override
	public String help() {
		return ChatColor.GREEN + "/hb-ai attack <player name>";
	}

}
