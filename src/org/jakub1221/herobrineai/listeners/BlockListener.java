package org.jakub1221.herobrineai.listeners;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jakub1221.herobrineai.AI.*;
import org.jakub1221.herobrineai.AI.Core.CoreType;
import org.jakub1221.herobrineai.AI.cores.Heads;
import org.jakub1221.herobrineai.HerobrineAI;

public class BlockListener implements Listener{
	
	Logger log = Logger.getLogger("Minecraft");
	
	@EventHandler
	public void onBlockIgnite(BlockIgniteEvent event){
		if (event.getBlock()!=null){
		Block blockt = (Block) event.getBlock();
		Location blockloc = (Location) blockt.getLocation();
		
		if (event.getPlayer()!=null){
		blockloc.setY(blockloc.getY()-1);
		Block block = (Block) blockloc.getWorld().getBlockAt(blockloc);
		if (block.getType() == Material.NETHERRACK){
			
		if (block.getWorld().getBlockAt(blockloc.getBlockX(), blockloc.getBlockY()-1, blockloc.getBlockZ()).getType() == Material.NETHERRACK){
		if (block.getWorld().getBlockAt(blockloc.getBlockX()-1, blockloc.getBlockY()-1, blockloc.getBlockZ()).getType() == Material.GOLD_BLOCK){
		if (block.getWorld().getBlockAt(blockloc.getBlockX()-1, blockloc.getBlockY()-1, blockloc.getBlockZ()-1).getType() == Material.GOLD_BLOCK){
		if (block.getWorld().getBlockAt(blockloc.getBlockX()-1, blockloc.getBlockY()-1, blockloc.getBlockZ()+1).getType() == Material.GOLD_BLOCK){
		if (block.getWorld().getBlockAt(blockloc.getBlockX()+1, blockloc.getBlockY()-1, blockloc.getBlockZ()).getType() == Material.GOLD_BLOCK){
		if (block.getWorld().getBlockAt(blockloc.getBlockX()+1, blockloc.getBlockY()-1, blockloc.getBlockZ()-1).getType() == Material.GOLD_BLOCK){
		if (block.getWorld().getBlockAt(blockloc.getBlockX()+1, blockloc.getBlockY()-1, blockloc.getBlockZ()+1).getType() == Material.GOLD_BLOCK){
		if (block.getWorld().getBlockAt(blockloc.getBlockX(), blockloc.getBlockY()-1, blockloc.getBlockZ()-1).getType() == Material.GOLD_BLOCK){
		if (block.getWorld().getBlockAt(blockloc.getBlockX(), blockloc.getBlockY()-1, blockloc.getBlockZ()+1).getType() == Material.GOLD_BLOCK){
		if (block.getWorld().getBlockAt(blockloc.getBlockX(), blockloc.getBlockY(), blockloc.getBlockZ()+1).getType() == Material.REDSTONE_TORCH_ON){
		if (block.getWorld().getBlockAt(blockloc.getBlockX(), blockloc.getBlockY(), blockloc.getBlockZ()-1).getType() == Material.REDSTONE_TORCH_ON){
		if (block.getWorld().getBlockAt(blockloc.getBlockX()+1, blockloc.getBlockY(), blockloc.getBlockZ()).getType() == Material.REDSTONE_TORCH_ON){
		if (block.getWorld().getBlockAt(blockloc.getBlockX()-1, blockloc.getBlockY(), blockloc.getBlockZ()).getType() == Material.REDSTONE_TORCH_ON){
		
		if (HerobrineAI.getPluginCore().getConfigDB().UseTotem==true && AICore.isTotemCalled==false){
			
		HerobrineAI.getPluginCore().getAICore().PlayerCallTotem(event.getPlayer());
		
		}
			
		}	
		}	
		}
		}
		}
		}
		}
		}
		}
		}
		}
		}
		}
		}
		}
		}
		
		if (event.getBlock().getWorld()==Bukkit.getServer().getWorld("world_herobrineai_graveyard")){
			event.setCancelled(true);
			return;
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event){
		if (event.getBlock().getWorld()==Bukkit.getServer().getWorld("world_herobrineai_graveyard")){
			event.setCancelled(true);
			return;
		}else{
			Heads h = (Heads) HerobrineAI.getPluginCore().getAICore().getCore(CoreType.HEADS);
			ArrayList<Block> list = h.getHeadList();
			if (list.contains(event.getBlock())){
				event.setCancelled(true);
				return;	
			}
		}
		
	
	}
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event){
		if (event.getBlock().getWorld()==Bukkit.getServer().getWorld("world_herobrineai_graveyard")){
			event.setCancelled(true);
			return;
		}
		
	
	}
}
