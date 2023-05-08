package net.theprogrammersworld.herobrine.AI.cores;

import org.bukkit.entity.Player;

import net.theprogrammersworld.herobrine.Herobrine;
import net.theprogrammersworld.herobrine.AI.Core;
import net.theprogrammersworld.herobrine.AI.CoreResult;

public class Burn extends Core {

	public Burn() {
		super(Core.Type.BURN, AppearType.NORMAL, Herobrine.getPluginCore());
	}

	@Override
	public CoreResult CallCore(Object[] data) {
		Player player = (Player) data[0];
		player.setFireTicks(800);
		return new CoreResult(true, player.getDisplayName() + " was burned by Herobrine.");
	}

}
