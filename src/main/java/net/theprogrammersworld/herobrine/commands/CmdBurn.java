package net.theprogrammersworld.herobrine.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.theprogrammersworld.herobrine.Herobrine;
import net.theprogrammersworld.herobrine.AI.Core;

public class CmdBurn extends SubCommand {

	public CmdBurn(Herobrine plugin) {
		super(plugin);
	}

	@Override
	public boolean execute(Player player, String[] args) {

		if (args.length > 1) {


			Player target = Bukkit.getServer().getPlayer(args[1]);

			if (target == null) {
				sendMessage(player, ChatColor.RED + "[Herobrine] " + args[1] + " cannot be attacked because they are offline.");
				return true;
			}

			if (!target.isOnline()) {
				sendMessage(player, ChatColor.RED + "[Herobrine] " + args[1] + " cannot be attacked because they are offline.");
				return true;
			}

			Object[] data = { target };
			sendMessage(player, ChatColor.RED + "[Herobrine] " + plugin.getAICore().getCore(Core.Type.BURN).RunCore(data).getResultString());

			return true;
		}

		return false;
	}

	@Override
	public String help() {
		return ChatColor.GREEN + "/herobrine burn <player>";
	}

	@Override
	public String helpDesc() {
		return ChatColor.GREEN + "Burns the specified player";
	}

}
