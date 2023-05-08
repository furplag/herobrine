package net.theprogrammersworld.herobrine.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import lombok.extern.slf4j.Slf4j;
import net.theprogrammersworld.herobrine.Herobrine;

@Slf4j(topic = "Minecraft")
public abstract class SubCommand {

	protected Herobrine plugin;

	public SubCommand(Herobrine plugin){
		this.plugin = plugin;
	}

	public abstract boolean execute(Player player, String[] args);

	public abstract String help();

	public abstract String helpDesc();

	protected void sendMessage(Player player, String message){
		if(player == null)
			log.info(ChatColor.stripColor(message));
		else
			player.sendMessage(message);
	}


}
