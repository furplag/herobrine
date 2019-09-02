package org.jakub1221.herobrineai.commands;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.AICore;

public class CmdPosition extends SubCommand {

	public CmdPosition(HerobrineAI plugin, Logger log) {
		super(plugin, log);
	}

	@Override
	public boolean execute(Player player, String[] args) {
		
		sendMessage(player, ChatColor.RED + "[HerobrineAI] Position");
		
		Location loc = plugin.HerobrineNPC.getBukkitEntity().getLocation();
		
		sendMessage(player, ChatColor.RED 
				+ "World: "+ loc.getWorld().getName()
				+ " X: " + ((int) loc.getX())
				+ " Y: " +((int) loc.getY())
				+ " Z: " +((int) loc.getZ())
				);
		
		sendMessage(player, ChatColor.RED + "InWalkingMode: " + AICore.getStringWalkingMode());
		sendMessage(player, ChatColor.RED + "Available World: "+ plugin.getAvailableWorldString());
		
		return true;
	}

	@Override
	public String help() {
		return ChatColor.GREEN + "/hb-ai position";
	}

}
