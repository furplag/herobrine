package org.jakub1221.herobrineai.AI.cores;

import org.bukkit.entity.Player;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.AI.CoreResult;

public class Burn extends Core {

	public Burn() {
		super(CoreType.BURN, AppearType.NORMAL);
	}

	@Override
	public CoreResult CallCore(Object[] data) {
		Player player = (Player) data[0];
		player.setFireTicks(800);
		return new CoreResult(true,"Burn player!");
	}

}
