package org.jakub1221.herobrineai.AI;

import org.bukkit.Bukkit;
import org.jakub1221.herobrineai.HerobrineAI;

public class ResetLimits {

	private int taskID = 0;
	private int books = 0;
	private int signs = 0;
	private int heads = 0;
	public int maxBooks = 1;
	public int maxSigns = 1;
	public int maxHeads = 1;

	public ResetLimits() {

		taskID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(HerobrineAI.getPluginCore(),
				new Runnable() {
					public void run() {
						resetAll();
					}
				}, 1 * 72000L, 1 * 72000L);
	}

	public void disable() {
		Bukkit.getServer().getScheduler().cancelTask(taskID);
	}

	public boolean isBook() {

		if (books < maxBooks) {
			books++;
			return true;
		}

		return false;
	}

	public boolean isSign() {

		if (signs < maxSigns) {
			signs++;
			return true;
		}

		return false;
	}

	public boolean isHead() {

		if (heads < maxHeads) {
			heads++;
			return true;
		}

		return false;
	}

	public void resetAll() {
		books = 0;
		signs = 0;
		heads = 0;
	}

	public void updateFromConfig() {
		maxBooks = HerobrineAI.getPluginCore().getConfigDB().maxBooks;
		maxSigns = HerobrineAI.getPluginCore().getConfigDB().maxSigns;
		maxHeads = HerobrineAI.getPluginCore().getConfigDB().maxHeads;
	}

}
