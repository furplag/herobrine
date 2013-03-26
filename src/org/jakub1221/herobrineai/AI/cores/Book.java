package org.jakub1221.herobrineai.AI.cores;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.AI.CoreResult;

public class Book extends Core{
	
	public Book(){
		super(CoreType.BOOK,AppearType.NORMAL);
	}
	
	public CoreResult CallCore(Object[] data){
		Player player = (Player) data[0];
	
		if (HerobrineAI.getPluginCore().getConfigDB().useWorlds.contains(player.getLocation().getWorld().getName())){
			if (HerobrineAI.getPluginCore().getConfigDB().WriteBooks==true && HerobrineAI.getPluginCore().getSupport().checkBooks(player.getLocation())){
			Random randgen = new Random();
			int chance=randgen.nextInt(100);
			if (chance>(100-HerobrineAI.getPluginCore().getConfigDB().BookChance)){
			Inventory chest = (Inventory) data[1];
			if (chest.firstEmpty()!=-1){
			chest.setItem(chest.firstEmpty(),newBook());
			return new CoreResult(true,"Book created!");
			}else{
				return new CoreResult(false,"Book create failed!");
			}
			}else{
				return new CoreResult(false,"Books are not allowed!");
			}
			}else{
				return new CoreResult(false,"Player is not in allowed world!");
			}
		}
		return new CoreResult(false,"Book create failed!");
	}
	public ItemStack newBook(){
		
		int count = HerobrineAI.getPluginCore().getConfigDB().useBookMessages.size();
		
		Random randgen = new Random();
		int chance=randgen.nextInt(count);
		
		ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
		BookMeta meta = (BookMeta) book.getItemMeta();
		
	
		ArrayList<String> list = new ArrayList<String>();

		meta.setTitle("");
		meta.setAuthor("");

		
		list.add(0, (String) HerobrineAI.getPluginCore().getConfigDB().useBookMessages.get(chance));
		
		meta.setPages(list);

		book.setItemMeta(meta);

	return (ItemStack) book;
	}
	
}
