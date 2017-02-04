package org.jakub1221.herobrineai.NPC.AI;

import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.Core.CoreType;

public class PathManager {

	Path pathNow = null;

	public void setPath(Path path) {
		pathNow = path;
	}

	public void update() {
		if (pathNow != null && HerobrineAI.getPluginCore().getAICore().getCoreTypeNow().equals(CoreType.RANDOM_POSITION)) {
			pathNow.update();
		}
	}

	public Path getPath() {
		return pathNow;
	}

}
