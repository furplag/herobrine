package org.jakub1221.herobrineai.misc;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemName {

	public static ItemMeta meta = null;
	public static SkullMeta skullmeta = null;

	public static ItemStack colorLeatherArmor(ItemStack i, Color color) {

		LeatherArmorMeta la_meta = (LeatherArmorMeta) i.getItemMeta();
		la_meta.setColor(color);
		i.setItemMeta(la_meta);

		return i;
	}

	public static ItemStack setName(ItemStack item, String name) {
		meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack setLore(ItemStack item, ArrayList<String> lore) {
		meta = item.getItemMeta();
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack setNameAndLore(ItemStack item, String name, ArrayList<String> lore) {
		meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public static ArrayList<String> getLore(ItemStack item) {
		return (ArrayList<String>) item.getItemMeta().getLore();
	}

	public static String getName(ItemStack item) {
		return item.getItemMeta().getDisplayName();
	}

	public static ItemStack CreateSkull(final UUID uuid, final String data) {
		final ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
		final SkullMeta skullmeta = (SkullMeta) skull.getItemMeta();

		skullmeta.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
		skullmeta.setDisplayName(ChatColor.RESET + data);

		skull.setItemMeta(skullmeta);
		
		return skull;
	}

}