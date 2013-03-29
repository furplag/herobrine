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
		
		Sound[] sounds = new Sound[15];
		sounds[0]=Sound.STEP_STONE;
		sounds[1]=Sound.STEP_WOOD;
		sounds[2]=Sound.STEP_GRASS;
		sounds[3]=Sound.STEP_SAND;
		sounds[4]=Sound.STEP_GRAVEL;
		sounds[5]=Sound.BREATH;
		sounds[6]=Sound.BREATH;
		sounds[7]=Sound.BREATH;
		sounds[8]=Sound.BREATH;
		sounds[9]=Sound.DOOR_OPEN;
		sounds[10]=Sound.DOOR_CLOSE;
		sounds[11]=Sound.GHAST_SCREAM;
		sounds[12]=Sound.GHAST_SCREAM2;
		sounds[13]=Sound.WITHER_DEATH;
		sounds[14]=Sound.WITHER_HURT;
		
		int chance=new Random().nextInt(14);
		int randx=new Random().nextInt(3);
		int randz=new Random().nextInt(3);
		int randxp=new Random().nextInt(1);
		int randzp=new Random().nextInt(1);
		if (randxp==0 && randx!=0){randx=(-(randx));}
		if (randzp==0 && randz!=0){randz=(-(randz));}
		
       player.playSound(player.getLocation(),sounds[chance],(float) 0.75,(float) 0.75);
       
       return new CoreResult(true,"SoundF is starting!");
	}
	
}
