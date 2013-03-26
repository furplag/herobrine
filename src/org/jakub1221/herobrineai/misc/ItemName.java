package org.jakub1221.herobrineai.misc;


import java.util.ArrayList;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemName {

	public static ItemMeta meta=null;
	public static SkullMeta skullmeta=null;
	
	public static ItemStack setName(ItemStack item, String name){
		meta=item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return item;
	}
	public static ItemStack setLore(ItemStack item, ArrayList<String> lore){
		meta=item.getItemMeta();
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	public static ItemStack setNameAndLore(ItemStack item, String name,ArrayList<String> lore){
		meta=item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	public static ArrayList<String> getLore(ItemStack item){
		return (ArrayList<String>) item.getItemMeta().getLore();
	}
	public static String getName(ItemStack item){
		return item.getItemMeta().getDisplayName();
	}
	public static ItemStack CreateSkull(String data){
		ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short)SkullType.PLAYER.ordinal());
		SkullMeta skullmeta = (SkullMeta)skull.getItemMeta();
		skullmeta.setOwner(data);
	
			skullmeta.setDisplayName(ChatColor.RESET+data);
		skull.setItemMeta(skullmeta);
		return skull;
	}
	
}