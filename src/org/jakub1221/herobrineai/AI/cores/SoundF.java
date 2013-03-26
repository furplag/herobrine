package org.jakub1221.herobrineai.AI.cores;

import java.util.Random;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.AI.CoreResult;

public class SoundF extends Core{

	public SoundF(){
		super(CoreType.SOUNDF,AppearType.NORMAL);
	}
	
	public CoreResult CallCore(Object[] data){
        return playRandom((Player)data[0]);
	}
	
	public CoreResult playRandom(Player player){
		
		Sound sound = Sound.BREATH;
		
		Random generator = new Random();
		int chance= generator.nextInt(20);
		
		if (chance<5){
		sound = Sound.STEP_STONE;
		}else if (chance<10){
			sound = Sound.STEP_SAND;
			}else if (chance<14){
				sound = Sound.STEP_GRASS;
				}else if (chance<18){
			sound = Sound.STEP_GRAVEL;
			}else{sound = Sound.BREATH;}
		
		Random randxgen = new Random();
		int randx= randxgen.nextInt(3);
		
		Random randzgen = new Random();
		int randz= randzgen.nextInt(3);
		
		Random randxgenp = new Random();
		int randxp= randxgenp.nextInt(1);
		Random randzgenp = new Random();
		int randzp= randzgenp.nextInt(1);
		if (randxp==0 && randx!=0){randx=(-(randx));}
		if (randzp==0 && randz!=0){randz=(-(randz));}
		
		
		
       player.playSound(player.getLocation(),sound,(float) 0.75,(float) 0.75);
       
       return new CoreResult(true,"SoundF is starting!");
	}
	
}
