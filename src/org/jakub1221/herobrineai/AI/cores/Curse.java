package org.jakub1221.herobrineai.AI.cores;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.AI.CoreResult;

public class Curse extends Core {

	public Curse() {
		super(CoreType.CURSE, AppearType.NORMAL);
	}

	@Override
	public CoreResult CallCore(Object[] data) {
             
			 final Player player = (Player) data[0];
			 
			 player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 1000, 1));
			 player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1000, 1));
			 player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1000, 1));
			 player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 50, 1));
			 player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 1000, 1));
			 
			 int i = 0;
			 while(i!=3){
				 i++;
				 Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(HerobrineAI.getPluginCore(), new Runnable(){

					@Override
					public void run() {
							player.getLocation().getWorld().strikeLightning(new Location(player.getLocation().getWorld(),player.getLocation().getX(),player.getLocation().getY()+1,player.getLocation().getZ()));	
							player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 50, 1));
					}
					 
				 }, i*150L);
			 }
			 
		 
		return new CoreResult(true,"Player cursed!");
	}

}
