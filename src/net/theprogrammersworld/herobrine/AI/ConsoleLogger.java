package net.theprogrammersworld.herobrine.AI;

import java.util.logging.Logger;

import net.theprogrammersworld.herobrine.HerobrineAI;

public class ConsoleLogger {

	static Logger log = Logger.getLogger("Minecraft");
	
	public void info(String text){
		if (HerobrineAI.isDebugging){
			log.info(text);
		}
	}
	
}
