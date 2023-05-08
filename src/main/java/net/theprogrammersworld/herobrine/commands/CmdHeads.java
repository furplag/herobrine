package net.theprogrammersworld.herobrine.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.theprogrammersworld.herobrine.Herobrine;
import net.theprogrammersworld.herobrine.AI.Core;

public class CmdHeads extends SubCommand {

  public CmdHeads(Herobrine plugin) {
    super(plugin);
  }

  @Override
  public boolean execute(Player player, String[] args) {

    if (args.length > 1) {

      Player target = Bukkit.getServer().getPlayer(args[1]);

      if (target == null) {
        sendMessage(player,
            ChatColor.RED + "[Herobrine] " + args[1] + " cannot be haunted with heads because they are offline.");
        return true;
      }

      if (!target.isOnline()) {
        sendMessage(player,
            ChatColor.RED + "[Herobrine] " + args[1] + " cannot be haunted with heads because they are offline.");
        return true;
      }

      if (!plugin.getSupport().checkBuild(target.getLocation())) {
        sendMessage(player, ChatColor.RED + "[Herobrine] " + args[1]
            + " cannot be haunted with heads because they are in a secure area.");
        return true;
      }

      Object[] data = { args[1] };
      sendMessage(player,
          ChatColor.RED + "[Herobrine] " + plugin.getAICore().getCore(Core.Type.HEADS).RunCore(data).getResultString());

      return true;
    }

    return false;
  }

  @Override
  public String help() {
    return ChatColor.GREEN + "/herobrine heads <player>";
  }

  @Override
  public String helpDesc() {
    return ChatColor.GREEN + "Spawns the specified player's heads near the specified player";
  }

}
