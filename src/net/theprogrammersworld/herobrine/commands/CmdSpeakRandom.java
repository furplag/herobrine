package net.theprogrammersworld.herobrine.commands;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.theprogrammersworld.herobrine.Herobrine;
import net.theprogrammersworld.herobrine.AI.Message;

public class CmdSpeakRandom extends SubCommand {

	public CmdSpeakRandom(Herobrine plugin, Logger log) {
		super(plugin, log);
	}

	@Override
	public boolean execute(Player player, String[] args) {
		if (args.length > 1) {
			Player target = Bukkit.getServer().getPlayer(args[1]);
			
			if (target == null) {
				sendMessage(player, ChatColor.RED + "[Herobrine] Herobrine cannot send a random message to " + args[1] + " because they are offline.");
				return true;
			}
			
			if (!target.isOnline()) {
				sendMessage(player, ChatColor.RED + "[Herobrine] Herobrine cannot send a random message to " + args[1] + " because they are offline.");
				return true;
			}
			
			Message.sendRandomMessage(target);
			sendMessage(player, ChatColor.RED + "[Herobrine] Herobrine sent a random message to " + args[1] + ".");
			return true;
		}
		return false;
	}

	@Override
	public String help() {
		return ChatColor.GREEN + "/herobrine speakrandom <player>";
	}

	@Override
	public String helpDesc() {
		return ChatColor.GREEN + "Sends a random message from Herobrine defined in the configuration file to the player";
	}

}
