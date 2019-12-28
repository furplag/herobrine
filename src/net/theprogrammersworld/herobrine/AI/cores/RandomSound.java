package net.theprogrammersworld.herobrine.AI.cores;

import org.bukkit.Bukkit;

import net.theprogrammersworld.herobrine.Herobrine;
import net.theprogrammersworld.herobrine.AI.Core;
import net.theprogrammersworld.herobrine.AI.CoreResult;

public class RandomSound extends Core {

	public RandomSound() {
		super(CoreType.RANDOM_SOUND, AppearType.NORMAL, Herobrine.getPluginCore());
	}

	@Override
	public CoreResult CallCore(final Object[] data) {

		int multip = 1;

		while (multip != 7) {
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Herobrine.getPluginCore(), new Runnable() {

				@Override
				public void run() {

					Herobrine.getPluginCore().getAICore().getCore(CoreType.SOUNDF).RunCore(data);

				}

			}, multip * 30L);
			multip++;
		}

		return new CoreResult(true, "Starting sound play to target!");
	}

}
