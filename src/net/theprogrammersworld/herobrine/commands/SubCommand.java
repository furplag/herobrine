package net.theprogrammersworld.herobrine.commands;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.theprogrammersworld.herobrine.Herobrine;

public abstract class SubCommand {
	
	protected Herobrine plugin;
	protected Logger logger;
	
	public SubCommand(Herobrine plugin, Logger log){
		this.plugin = plugin;
		this.logger = log;
	}
	
	public abstract boolean execute(Player player, String[] args);
	
	public abstract String help();
	
	public abstract String helpDesc();
	
	protected void sendMessage(Player player, String message){
		if(player == null)
			logger.info(ChatColor.stripColor(message));
		else
			player.sendMessage(message);
	}
	

}
