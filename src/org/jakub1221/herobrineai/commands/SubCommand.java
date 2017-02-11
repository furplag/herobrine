package org.jakub1221.herobrineai.commands;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jakub1221.herobrineai.HerobrineAI;

public abstract class SubCommand {
	
	protected HerobrineAI plugin;
	protected Logger logger;
	
	public SubCommand(HerobrineAI plugin, Logger log){
		this.plugin = plugin;
		this.logger = log;
	}
	
	public abstract boolean execute(Player player, String[] args);
	
	public abstract String help();
	
	protected void sendMessage(Player player, String message){
		if(player == null)
			logger.info(ChatColor.stripColor(message));
		else
			player.sendMessage(message);
	}
	

}
