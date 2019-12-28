package net.theprogrammersworld.herobrine.AI.cores;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import net.theprogrammersworld.herobrine.HerobrineAI;
import net.theprogrammersworld.herobrine.Utils;
import net.theprogrammersworld.herobrine.AI.Core;
import net.theprogrammersworld.herobrine.AI.CoreResult;

public class Book extends Core {

	public Book() {
		super(CoreType.BOOK, AppearType.NORMAL, HerobrineAI.getPluginCore());
	}

	public CoreResult CallCore(Object[] data) {
		Player player = (Player) data[0];

		if (HerobrineAI.getPluginCore().getConfigDB().useWorlds.contains(player.getLocation().getWorld().getName())) {
			
			if (HerobrineAI.getPluginCore().getConfigDB().WriteBooks == true
				&& HerobrineAI.getPluginCore().getSupport().checkBooks(player.getLocation())) {
				
				int chance = Utils.getRandomGen().nextInt(100);
				if (chance > (100 - HerobrineAI.getPluginCore().getConfigDB().BookChance)) {
					Inventory chest = (Inventory) data[1];
					if (chest.firstEmpty() != -1) {
						if (HerobrineAI.getPluginCore().getAICore().getResetLimits().isBook()) {
							chest.setItem(chest.firstEmpty(), newBook());
							return new CoreResult(true, "Book created!");
						}
					} else {
						return new CoreResult(false, "Book create failed!");
					}
				} else {
					return new CoreResult(false, "Books are not allowed!");
				}
			} else {
				return new CoreResult(false, "Player is not in allowed world!");
			}
		}
		return new CoreResult(false, "Book create failed!");
	}

	public ItemStack newBook() {

		int count = HerobrineAI.getPluginCore().getConfigDB().useBookMessages.size();

		int chance = Utils.getRandomGen().nextInt(count);

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
