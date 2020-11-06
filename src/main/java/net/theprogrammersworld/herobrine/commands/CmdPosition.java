package net.theprogrammersworld.herobrine.commands;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.theprogrammersworld.herobrine.Herobrine;

public class CmdPosition extends SubCommand {

	public CmdPosition(Herobrine plugin, Logger log) {
		super(plugin, log);
	}

	@Override
	public boolean execute(Player player, String[] args) {		
		Location loc = plugin.HerobrineNPC.getBukkitEntity().getLocation();
		
		sendMessage(player, ChatColor.GREEN + "[Herobrine] Position - "
				+ "World: "+ loc.getWorld().getName()
				+ ", Coordinates: (" + (int) loc.getX() + ", " + (int) loc.getY() + ", " + (int) loc.getZ() + ")");
		
		return true;
	}

	@Override
	public String help() {
		return ChatColor.GREEN + "/herobrine position";
	}

	@Override
	public String helpDesc() {
		return ChatColor.GREEN + "Displays Herobrine's coordinates";
	}

}
