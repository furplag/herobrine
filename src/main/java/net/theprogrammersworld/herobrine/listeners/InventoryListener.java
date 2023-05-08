package net.theprogrammersworld.herobrine.listeners;

import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;

import net.theprogrammersworld.herobrine.Herobrine;
import net.theprogrammersworld.herobrine.Utils;
import net.theprogrammersworld.herobrine.AI.Core;
import net.theprogrammersworld.herobrine.misc.ItemName;

public class InventoryListener implements Listener {

  Logger log = Logger.getLogger("Minecraft");

  @EventHandler
  public void onInventoryClose(InventoryCloseEvent event) {
    if (Herobrine.getPluginCore().getConfigDB().UseHeads && InventoryType.CHEST == event.getInventory().getType()) {
      Herobrine.getPluginCore().getAICore().getCore(Core.Type.BOOK).RunCore(new Object[] { event.getPlayer(), event.getInventory() });
      if (Utils.getRandom().nextInt(100) > 97 && event.getInventory().firstEmpty() != -1) {
        if (Herobrine.getPluginCore().getAICore().getResetLimits().isHead()) {
          event.getInventory().setItem(event.getInventory().firstEmpty(), ItemName.CreateSkull(event.getPlayer().getUniqueId(), event.getPlayer().getName()));
        }
      }
    }
  }

  @EventHandler
  public void onInventoryOpen(InventoryOpenEvent event) {
    if (Herobrine.getPluginCore().getConfigDB().PlaceSigns && Herobrine.getPluginCore().getConfigDB().useWorlds.contains(event.getPlayer().getLocation().getWorld().getName())) {
      if (Herobrine.getPluginCore().getAICore().getResetLimits().isSign() && Herobrine.getPluginCore().getSupport().checkSigns(event.getPlayer().getLocation())) {
        if (Set.of(InventoryType.CHEST, InventoryType.FURNACE, InventoryType.WORKBENCH).contains(event.getInventory().getType())) {
          Herobrine.getPluginCore().getAICore().getCore(Core.Type.SIGNS).RunCore(new Object[] { event.getPlayer().getLocation(), event.getPlayer().getLocation() });
        }
      }
    }
  }
}
