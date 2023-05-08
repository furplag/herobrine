package net.theprogrammersworld.herobrine.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.theprogrammersworld.herobrine.Herobrine;
import net.theprogrammersworld.herobrine.AI.AICore;

public class CmdHaunt extends SubCommand {

  public CmdHaunt(Herobrine plugin) {
    super(plugin);
  }

  @Override
  public boolean execute(Player player, String[] args) {

    if (args.length > 1) {

      Player target = Bukkit.getServer().getPlayer(args[1]);

      if (target == null) {
        sendMessage(player, ChatColor.RED + "[Herobrine] " + args[1] + " cannot be haunted because they are offline.");
        return true;
      }

      if (!target.isOnline()) {
        sendMessage(player, ChatColor.RED + "[Herobrine] " + args[1] + " cannot be haunted because they are offline.");
        return true;
      }

      if (!plugin.getSupport().checkHaunt(target.getLocation())) {
        sendMessage(player,
            ChatColor.RED + "[Herobrine] " + args[1] + " cannot be haunted because they are in a secure area.");
        return true;
      }

      if (AICore.isTarget == false) {
        plugin.getAICore().setHauntTarget(target);
        sendMessage(player, ChatColor.RED + "[Herobrine] Herobrine is now haunting " + args[1] + ".");

      } else {
        sendMessage(player, ChatColor.RED + "[Herobrine] Herobrine is already haunting another player. Use "
            + ChatColor.GREEN + "/herobrine cancel" + ChatColor.RED + " to cancel the current haunting.");
      }

      return true;

    }

    return false;
  }

  @Override
  public String help() {
    return ChatColor.GREEN + "/herobrine haunt";
  }

  @Override
  public String helpDesc() {
    return ChatColor.GREEN + "Sends Herobrine to haunt the specified player";
  }

}
