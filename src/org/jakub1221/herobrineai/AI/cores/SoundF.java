package org.jakub1221.herobrineai.AI.cores;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.Utils;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.AI.CoreResult;

public class SoundF extends Core{

	public SoundF(){
		super(CoreType.SOUNDF,AppearType.NORMAL, HerobrineAI.getPluginCore());
	}
	
	public CoreResult CallCore(Object[] data){
        return playRandom((Player)data[0]);
	}
	
	public CoreResult playRandom(Player player){
		
		Sound[] sounds = {
				Sound.ENTITY_GHAST_SCREAM,
				Sound.ENTITY_WITHER_DEATH,
				Sound.ENTITY_WITHER_HURT,
				Sound.ENTITY_BAT_HURT,
				Sound.ENTITY_PLAYER_BREATH,
				Sound.ENTITY_PLAYER_HURT,
				Sound.BLOCK_IRON_DOOR_OPEN,
				Sound.BLOCK_IRON_DOOR_CLOSE
				};
		
		
       player.playSound(player.getLocation(),sounds[Utils.getRandomGen().nextInt(sounds.length)],(float) 0.75,(float) 0.75);
       
       return new CoreResult(true,"SoundF is starting!");
	}
	
}
