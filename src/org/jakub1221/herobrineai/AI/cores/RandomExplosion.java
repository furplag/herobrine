package org.jakub1221.herobrineai.AI.cores;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.Utils;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.AI.CoreResult;

public class RandomExplosion extends Core {

	public RandomExplosion() {
		super(CoreType.RANDOM_EXPLOSION, AppearType.NORMAL, HerobrineAI.getPluginCore());
	}

	@Override
	public CoreResult CallCore(Object[] data) {

		Player player = (Player) data[0];
		if (PluginCore.getConfigDB().Explosions) {
			if (PluginCore.getSupport().checkBuild(player.getLocation())) {

				Location loc = player.getLocation();
				int x = loc.getBlockX() + (Utils.getRandomGen().nextInt(16) - 8);
				int y = loc.getBlockY();
				int z = loc.getBlockZ() + (Utils.getRandomGen().nextInt(16) - 8);
				loc.getWorld().createExplosion(new Location(loc.getWorld(), x, y, z), 1.0f);

			} else {
				return new CoreResult(true, "Player is in secure area!");
			}
		} else {
			return new CoreResult(true, "Explosions are not allowed!");
		}
		return new CoreResult(true, "Explosion near the player!");
	}

}
