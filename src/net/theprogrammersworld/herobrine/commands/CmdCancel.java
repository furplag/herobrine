package net.theprogrammersworld.herobrine.commands;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.theprogrammersworld.herobrine.HerobrineAI;
import net.theprogrammersworld.herobrine.AI.Core.CoreType;

public class CmdCancel extends SubCommand {

	public CmdCancel(HerobrineAI plugin, Logger log) {
		super(plugin, log);
	}

	@Override
	public boolean execute(Player player, String[] args) {
		
		plugin.getAICore().CancelTarget(CoreType.ANY);
		sendMessage(player, ChatColor.RED + "[HerobrineAI] Target cancelled!");
		
		return true;
	}

	@Override
	public String help() {
		return ChatColor.GREEN + "/hb-ai cancel";
	}

}
