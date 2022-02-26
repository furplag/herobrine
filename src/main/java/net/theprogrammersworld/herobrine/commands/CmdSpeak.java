package net.theprogrammersworld.herobrine.commands;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.theprogrammersworld.herobrine.Herobrine;

public class CmdSpeak extends SubCommand {

	public CmdSpeak(Herobrine plugin, Logger log) {
		super(plugin, log);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean execute(Player player, String[] args) {
		if (args.length > 1) {
			String message = "<Herobrine>";
			for(int x = 1; x < args.length; x++)
				message += " " + args[x];
			Bukkit.broadcastMessage(message);
			return true;
		}
		return false;
	}

	@Override
	public String help() {
		return ChatColor.GREEN + "/herobrine speak <message>";
	}

	@Override
	public String helpDesc() {
		return ChatColor.GREEN + "Sends a chat message on Herobrine's behalf";
	}

}
