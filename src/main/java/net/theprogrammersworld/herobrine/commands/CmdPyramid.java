package net.theprogrammersworld.herobrine.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.theprogrammersworld.herobrine.Herobrine;
import net.theprogrammersworld.herobrine.AI.Core;

public class CmdPyramid extends SubCommand {

  public CmdPyramid(Herobrine plugin) {
    super(plugin);
  }

  @Override
  public boolean execute(Player player, String[] args) {

    if (args.length > 1) {

      Player target = Bukkit.getServer().getPlayer(args[1]);

      if (target == null) {
        sendMessage(player,
            ChatColor.RED + "[Herobrine] A pyramid cannot be built near " + args[1] + " because they are offline.");
        return true;
      }

      if (!target.isOnline()) {
        sendMessage(player,
            ChatColor.RED + "[Herobrine] A pyramid cannot be built near " + args[1] + " because they are offline.");
        return true;
      }

      if (!plugin.getSupport().checkBuild(target.getLocation())) {
        sendMessage(player, ChatColor.RED + "[Herobrine] A pyramid cannot be built near " + args[1]
            + " because they are in a secure area.");
        return true;
      }

      Object[] data = { target };

      if (plugin.getAICore().getCore(Core.Type.PYRAMID).RunCore(data).getResult())
        sendMessage(player, ChatColor.RED + "[Herobrine] Generating a pyramid near " + args[1] + ".");
      else
        sendMessage(player, ChatColor.RED + "[Herobrine] A pyramid could not be generated near " + args[1]
            + " because there is no good place for it near them.");

      return true;
    }

    return false;
  }

  @Override
  public String help() {
    return ChatColor.GREEN + "/herobrine pyramid <player>";
  }

  @Override
  public String helpDesc() {
    return ChatColor.GREEN + "Builds a pyramid near the specified player";
  }

}
