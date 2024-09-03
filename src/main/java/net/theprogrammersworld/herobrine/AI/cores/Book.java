package net.theprogrammersworld.herobrine.AI.cores;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import net.kyori.adventure.text.Component;
import net.theprogrammersworld.herobrine.Utils;
import net.theprogrammersworld.herobrine.AI.Core;
import net.theprogrammersworld.herobrine.AI.CoreResult;

public class Book extends Core {

  public Book() {
    super(Core.Type.BOOK, AppearType.NORMAL);
  }

  public CoreResult CallCore(Object[] data) {
    Player player = (Player) data[0];

    if (plugin.getConfigDB().useWorlds.contains(player.getLocation().getWorld().getName())) {

      if (plugin.getConfigDB().WriteBooks == true
        && plugin.getSupport().checkBooks(player.getLocation())) {

        int chance = Utils.getRandom().nextInt(100);
        if (chance > (100 - plugin.getConfigDB().BookChance)) {
          Inventory chest = (Inventory) data[1];
          if (chest.firstEmpty() != -1) {
            if (plugin.getAICore().getResetLimits().isBook()) {
              chest.setItem(chest.firstEmpty(), newBook());
              return new CoreResult(true, "Herobrine's book created.");
            }
          } else {
            return new CoreResult(false, "Herobrine's book creation failed.");
          }
        } else {
          return new CoreResult(false, "Herobrine's books are prohibited.");
        }
      } else {
        return new CoreResult(false, player.displayName() + " is in a world that Herobrine is not allowed in.");
      }
    }
    return new CoreResult(false, "Herobrine's book creation failed.");
  }

  public ItemStack newBook() {

    ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
    BookMeta meta = (BookMeta) book.getItemMeta();

    ArrayList<String> list = new ArrayList<String>();

    meta.setTitle("");
    meta.setAuthor(plugin.getConfigDB().HerobrineName);

    list.add(0, (String) plugin.getConfigDB().useBookMessages.get(Utils.getRandom().nextInt(plugin.getConfigDB().useBookMessages.size())));

    meta.pages(Component.text(plugin.getConfigDB().useBookMessages.get(Utils.getRandom().nextInt(plugin.getConfigDB().useBookMessages.size()))));

    book.setItemMeta(meta);

    return book;
  }

}
