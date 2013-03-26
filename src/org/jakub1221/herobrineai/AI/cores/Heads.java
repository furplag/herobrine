package org.jakub1221.herobrineai.AI.cores;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.AICore;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.AI.Core.AppearType;
import org.jakub1221.herobrineai.AI.CoreResult;
import org.jakub1221.herobrineai.misc.BlockChanger;
import org.jakub1221.herobrineai.misc.ItemName;

public class Heads extends Core{

	private boolean isCalled=false;
	private List<Block> headList = new ArrayList<Block>();
	
	public Heads(){
		super(CoreType.HEADS,AppearType.NORMAL);
	}
	
	public CoreResult CallCore(Object[] data){
	if (isCalled==false){
		if (Bukkit.getPlayer((String) data[0]).isOnline()){
		Player player = (Player) Bukkit.getServer().getPlayer((String) data[0]);
		if(HerobrineAI.getPluginCore().getSupport().checkBuild(player.getLocation())){
			if (HerobrineAI.getPluginCore().getConfigDB().UseHeads){
			
				Location loc = player.getLocation();
				int px=loc.getBlockX();
				int pz=loc.getBlockZ();
				int y=0;
				int x=-7;
				int z=-7;
				for (x=-7;x<=7;x++){
					for (z=-7;z<=7;z++){
						if(new Random().nextInt(7) == new Random().nextInt(7)){
							
							if (HerobrineAI.NonStandBlocks.contains(loc.getWorld().getHighestBlockAt(px+x, pz+z).getType())){
								y=loc.getWorld().getHighestBlockYAt(px+x, pz+z);
							}else{
								y=loc.getWorld().getHighestBlockYAt(px+x, pz+z)+1;
							}
								
							Block block = loc.getWorld().getBlockAt(px+x, y, pz+z);
						     BlockChanger.PlaceSkull(block.getLocation(),"Herobrine");
							
						    headList.add(block);
						   
						}
					}
				}
				
				 isCalled=true;
				Bukkit.getScheduler().scheduleSyncDelayedTask(AICore.plugin, new Runnable() {
   	        public void run() {
   	        	RemoveHeads();
   			    }
   	        }, 1 * 100L);
				
		return new CoreResult(true,"Spawned some heads near "+player.getName()+"!");
		
			}else{return new CoreResult(false,"Heads are disabled!");}
		}else{return new CoreResult(false,"Player is in secure area!");}
		}else{return new CoreResult(false,"Player is offline.");}
	}else{
		return new CoreResult(false,"There are already heads! Wait until they disappear.");
	}
	}
	
	public void RemoveHeads(){
		for (Block h : headList){
			h.setType(Material.AIR);
		}
		headList.clear();
		 isCalled=false;
	}
	public ArrayList<Block> getHeadList(){
		return (ArrayList<Block>) headList;
	}
}
