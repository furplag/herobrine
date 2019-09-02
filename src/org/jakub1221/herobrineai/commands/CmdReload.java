package org.jakub1221.herobrineai.commands;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jakub1221.herobrineai.HerobrineAI;

public class CmdReload extends SubCommand {

	public CmdReload(HerobrineAI plugin, Logger log) {
		super(plugin, log);
	}

	@Override
	public boolean execute(Player player, String[] args) {
		
		plugin.getConfigDB().Reload();
		sendMessage(player, ChatColor.RED + "[HerobrineAI] Config reloaded!");
		
		return true;
	}

	@Override
	public String help() {
		return ChatColor.GREEN + "/hb-ai reload";
	}

}
