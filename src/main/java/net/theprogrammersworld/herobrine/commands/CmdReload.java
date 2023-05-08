package net.theprogrammersworld.herobrine.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.theprogrammersworld.herobrine.Herobrine;

public class CmdReload extends SubCommand {

  public CmdReload(Herobrine plugin) {
    super(plugin);
  }

  @Override
  public boolean execute(Player player, String[] args) {

    plugin.getConfigDB().Reload();
    sendMessage(player, ChatColor.RED + "[Herobrine] Herobrine configuration file reloaded.");

    return true;
  }

  @Override
  public String help() {
    return ChatColor.GREEN + "/herobrine reload";
  }

  @Override
  public String helpDesc() {
    return ChatColor.GREEN + "Reloads the Herobrine configuration file";
  }

}
