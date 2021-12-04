package net.theprogrammersworld.herobrine.misc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

import net.theprogrammersworld.herobrine.Herobrine;

public class UpdateScanner implements Runnable {

	@Override
	public void run() {
		// Check for a newer version of the plugin, and put the thread to sleep for 24 hours
		// before the check is performed again. If a newer version is found, begin reporting
		// the discovery of a newer version once every hour.
		final String pluginVersionCount = "36";
		ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
		
		while(!Herobrine.getPluginCore().getConfigDB().newVersionFound) {
			// Check for updates once every 24 hours.
			try {
				URL versionCheckURL = new URL("https://www.theprogrammersworld.net/Herobrine/latestVersion.html");
				BufferedReader remoteNumberReader = new BufferedReader(new InputStreamReader(versionCheckURL.openStream()));
				String remoteVersionNumber = remoteNumberReader.readLine();
				if(!remoteVersionNumber.equals(pluginVersionCount)) {
					// A newer version was found. Change the value of "newVersionFound", and break
					// from this loop in to a loop that will display a "new version" report in the
					// console once every hour.
					Herobrine.getPluginCore().getConfigDB().newVersionFound = true;
					break;
				}
			} catch (Exception e) {
				console.sendMessage(ChatColor.RED + "Herobrine was unable to connect to the internet to check\n" +
						"for a new version.");
			}
			try {
				Thread.sleep(86400000);
			} catch (InterruptedException e) {}
		}
		
		while(true) {
			// Display a "new version" message in the console once every hour.
			console.sendMessage(ChatColor.RED + "A new version of Herobrine is available.\nTo get it, " +
					"go to www.theprogrammersworld.net/Herobrine and click \"Download\".");
			try {
				Thread.sleep(3600000);
			} catch (InterruptedException e) {}
		}
	}

}
