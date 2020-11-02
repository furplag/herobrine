package net.theprogrammersworld.herobrine.AI.cores;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import net.theprogrammersworld.herobrine.Herobrine;
import net.theprogrammersworld.herobrine.Utils;
import net.theprogrammersworld.herobrine.AI.Core;
import net.theprogrammersworld.herobrine.AI.CoreResult;

public class SoundF extends Core{

	public SoundF(){
		super(CoreType.SOUNDF,AppearType.NORMAL, Herobrine.getPluginCore());
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
       
       return new CoreResult(true,"SoundF is starting.");
	}
	
}
