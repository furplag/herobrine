package net.theprogrammersworld.herobrine.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.theprogrammersworld.herobrine.Herobrine;
import net.theprogrammersworld.herobrine.AI.Core;

public class CmdCancel extends SubCommand {

  public CmdCancel(Herobrine plugin) {
    super(plugin);
  }

  @Override
  public boolean execute(Player player, String[] args) {

    plugin.getAICore().CancelTarget(Core.Type.ANY);
    sendMessage(player, ChatColor.RED + "[Herobrine] The current Herobrine victim has been saved.");

    return true;
  }

  @Override
  public String help() {
    return ChatColor.GREEN + "/herobrine cancel";
  }

  @Override
  public String helpDesc() {
    return ChatColor.GREEN + "Cancels Herobrine's current target";
  }

}
