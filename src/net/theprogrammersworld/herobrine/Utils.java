package net.theprogrammersworld.herobrine;

import java.util.Collection;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Utils {

	private static Random randomGen = new Random();
	
	public static Random getRandomGen(){
		return randomGen;
	}
	
	public static Player getRandomPlayer() {
		Collection<? extends Player> playersOnline = Bukkit.getServer().getOnlinePlayers();

		if(playersOnline.size() == 1 && ((Player)playersOnline.toArray()[0]).getEntityId() == Herobrine.getPluginCore().HerobrineEntityID)
			return null;
		
		int player_rolled = new Random().nextInt(playersOnline.size());

		Player p = (Player) playersOnline.toArray()[player_rolled];
		
		if (p.getEntityId() == Herobrine.getPluginCore().HerobrineEntityID)
			return getRandomPlayer();
		
		return p;

	}

	public static int getRandomPlayerNum() {
		Collection<? extends Player> playersOnline = Bukkit.getServer().getOnlinePlayers();
		
		return new Random().nextInt(playersOnline.size());
	}

}
