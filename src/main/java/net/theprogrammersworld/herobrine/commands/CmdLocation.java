package net.theprogrammersworld.herobrine.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.theprogrammersworld.herobrine.Herobrine;

public class CmdLocation extends SubCommand {

  public CmdLocation(Herobrine plugin) {
    super(plugin);
  }

  @Override
  public boolean execute(Player player, String[] args) {
    Location loc = plugin.HerobrineNPC.getBukkitEntity().getLocation();

    sendMessage(player, ChatColor.GREEN + "[Herobrine] Location - " + "World: " + loc.getWorld().getName()
        + ", Coordinates: (" + (int) loc.getX() + ", " + (int) loc.getY() + ", " + (int) loc.getZ() + ")");

    return true;
  }

  @Override
  public String help() {
    return ChatColor.GREEN + "/herobrine location";
  }

  @Override
  public String helpDesc() {
    return ChatColor.GREEN + "Displays Herobrine's location";
  }

}
