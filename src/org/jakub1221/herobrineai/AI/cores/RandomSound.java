package org.jakub1221.herobrineai.AI.cores;

import org.bukkit.Bukkit;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.AI.CoreResult;

public class RandomSound extends Core {

	public RandomSound() {
		super(CoreType.RANDOM_SOUND, AppearType.NORMAL);
	}

	@Override
	public CoreResult CallCore(final Object[] data) {
		
		int multip=1;

		while(multip!=7){
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(HerobrineAI.getPluginCore(), new Runnable(){

			@Override
			public void run() {
				
				HerobrineAI.getPluginCore().getAICore().getCore(CoreType.SOUNDF).RunCore(data);
				
			}
			
		},multip*30L);
		multip++;
		}
		
		return new CoreResult(true,"Starting sound play to target!");
	}

}
