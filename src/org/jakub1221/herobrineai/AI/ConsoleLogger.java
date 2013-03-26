package org.jakub1221.herobrineai.AI;

import java.util.logging.Logger;

import org.jakub1221.herobrineai.HerobrineAI;

public class ConsoleLogger {

	static Logger log = Logger.getLogger("Minecraft");
	
	public void info(String text){
		if (HerobrineAI.isDebugging){
		log.info(text);
		}
	}
	
}
