package net.theprogrammersworld.herobrine.commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.theprogrammersworld.herobrine.Herobrine;

public class CmdPluginReport extends SubCommand {

	public CmdPluginReport(Herobrine plugin) {
		super(plugin);
	}

	@Override
	public boolean execute(Player player, String[] args) {
		if (args.length > 1) {
			new Thread() {
				public void run() {
					String response = sendBugReport(player, args);
					if(response.equals("0"))
						sendMessage(player, ChatColor.RED + "Your report was not was submitted because the report server could not " +
								"be reached. Please try again later.");
					else if(response.equals("1"))
						sendMessage(player, ChatColor.RED + "Your report was not submitted because this version of Herobrine does not " +
								"appear to be an official release. Please download an official version of the " +
								"plugin at: https://www.theprogrammersworld.net/Herobrine/download.php");
					else if(response.equals("2"))
						sendMessage(player, ChatColor.RED + "Your report was not submitted because this version of Herobrine is no " +
								"longer supported. Please download a newer version of the plugin at: " +
								"https://www.theprogrammersworld.net/Herobrine/download.php");
					else if(response.equals("4"))
						sendMessage(player, ChatColor.RED + "Your report was not submitted because this Minecraft server is banned " +
								"from submitting reports.");
					else if(response.equals("5"))
						sendMessage(player, ChatColor.RED + "Your report was not submitted because you are banned from submitting " +
								"reports.");
					else
						sendMessage(player, ChatColor.GREEN + "Your report was successfully submitted. For further assistance, please " +
								"post on our forum at https://www.theprogrammersworld.net/forum/ and " +
								"reference this report in your post with the following ID: " +
								response);
				}
			}.start();
			return true;
		}
		return false;
	}

	@Override
	public String help() {
		return ChatColor.GREEN + "/herobrine pluginreport <message>";
	}

	@Override
	public String helpDesc() {
		return ChatColor.GREEN + "See theprogrammersworld.net/Herobrine/internalBugReporting.php for documentation on this command";
	}

	private String sendBugReport(Player player, String[] messageWords) {
		// Collect the necessary information and POST it to the server where bug reports will be
		// collected. Based on the result of the submission, return a string.
		// (0 = report failed, 1 = unofficial version, 2 = unsupported version, Report ID => success)
		String report;
		String checksum;

		// Collect basic information including the UUID of the player who made the submission, the
		// IP address of the server, the port on which the server is running, and the plugin version.
		String serverIP = getServerIP();
		String playerUUID;
		try {
			playerUUID = player.getUniqueId().toString();
		} catch (Exception e) {
			playerUUID = "CONSOLE";
		}

		try {
			// Get the contents of the configuration file.
			File configFile = new File("plugins" + File.separator + "Herobrine" + File.separator + "config.yml");
			FileInputStream configFileInputStream = new FileInputStream(configFile);
			byte[] configFileBytes = new byte[(int) configFile.length()];
			configFileInputStream.read(configFileBytes);
			configFileInputStream.close();
			String configFileString = new String(configFileBytes, "UTF-8");

			report = "Server IP Address: " + serverIP + "\r\n" +
					"Submission UUID: " + playerUUID + "\r\n" +
					"Server Port: " + Bukkit.getServer().getPort() + "\r\n" +
					"Version: " + Herobrine.getPluginCore().getConfigDB().pluginVersionString + "\r\n\r\n" +
					"Plugin Configuration File:\r\n" + configFileString + " \r\n\r\n";

			// Assemble the array of words in to a single message.
			for(int x = 1; x < messageWords.length; x++) {
				report += messageWords[x] + " ";
			}

			// Determine the MD5 hash of the plugin.
			checksum = computeMD5();

			// After all of the necessary information has been acquired, submit the report.
			try {
				String postData = URLEncoder.encode("report", "UTF-8") + "=" + URLEncoder.encode(report, "UTF-8") +
						"&" + URLEncoder.encode("checksum", "UTF-8") + "=" + URLEncoder.encode(checksum, "UTF-8") +
						"&" + URLEncoder.encode("ip", "UTF-8") + "=" + URLEncoder.encode(serverIP, "UTF-8") +
						"&" + URLEncoder.encode("uuid", "UTF-8") + "=" + URLEncoder.encode(playerUUID, "UTF-8");
				URL submitURL = new URL("https://www.theprogrammersworld.net/Herobrine/pluginBugReporter.php");
				URLConnection urlConn = submitURL.openConnection();
				urlConn.setDoOutput(true);
				OutputStreamWriter wr = new OutputStreamWriter(urlConn.getOutputStream());
				wr.write(postData);
				wr.flush();

				BufferedReader rd = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
				String serverResponse = rd.readLine();
				rd.close();
				wr.close();
				return serverResponse;
			} catch (Exception e){}
		} catch (Exception e){}
		// The report failed because for some reason, the server could not be reached.
		return "0";
	}

	private String getServerIP() {
		// Get the IP address of the server by calling a page on the website that will return the IP.
		URL ipFetchURL;
		try {
			ipFetchURL = new URL("https://www.theprogrammersworld.net/Herobrine/pluginIPFetcher.php");
			InputStreamReader ipFetchISR = new InputStreamReader(ipFetchURL.openStream());
			BufferedReader ipFetchBR = new BufferedReader(ipFetchISR);
			String ipAddress = ipFetchBR.readLine();
			ipFetchBR.close();
			ipFetchISR.close();
			return ipAddress;
		} catch (Exception e) {
			return "";
		}
	}

	private String computeMD5() {
		// Determine the MD5 hash of the plugin.
		File pluginFile = new File(CmdPluginReport.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        try
        {
        	String filepath = URLDecoder.decode(pluginFile.getAbsolutePath(), StandardCharsets.UTF_8.toString());
            StringBuilder hash = new StringBuilder();
            MessageDigest md = MessageDigest.getInstance("MD5");
            FileInputStream fis = new FileInputStream(filepath);
            byte[] dataBytes = new byte[1024];
            int nread = 0;

            while((nread = fis.read(dataBytes)) != -1)
                 md.update(dataBytes, 0, nread);

            byte[] mdbytes = md.digest();

            for(int i=0; i<mdbytes.length; i++)
            hash.append(Integer.toString((mdbytes[i] & 0xff) + 0x100 , 16).substring(1));
            fis.close();
            return hash.toString();
        }
        catch(Exception e)
        {
        	return "";
        }
	}

}
