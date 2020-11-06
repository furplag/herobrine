package net.theprogrammersworld.herobrine.NPC.AI;

import net.theprogrammersworld.herobrine.Herobrine;
import net.theprogrammersworld.herobrine.AI.Core.CoreType;

public class PathManager {

	Path pathNow = null;

	public void setPath(Path path) {
		pathNow = path;
	}

	public void update() {
		if (pathNow != null && (Herobrine.getPluginCore().getAICore().getCoreTypeNow().equals(CoreType.ANY) ||
				Herobrine.getPluginCore().getAICore().getCoreTypeNow().equals(CoreType.RANDOM_POSITION))) {
			pathNow.update();
		}
	}

	public Path getPath() {
		return pathNow;
	}

}
