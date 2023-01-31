package net.theprogrammersworld.herobrine.AI.cores;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.theprogrammersworld.herobrine.Herobrine;
import net.theprogrammersworld.herobrine.Utils;
import net.theprogrammersworld.herobrine.AI.Core;
import net.theprogrammersworld.herobrine.AI.CoreResult;
import net.theprogrammersworld.herobrine.entity.MobType;
import net.theprogrammersworld.herobrine.misc.ItemName;
import net.theprogrammersworld.herobrine.misc.StructureLoader;

public class Temple extends Core {

  public Temple() {
    super(CoreType.TEMPLE, AppearType.NORMAL, Herobrine.getPluginCore());
  }

  @Override
  public CoreResult CallCore(Object[] data) {
    if (data[0] instanceof Player player) {
      return FindPlacePlayer(player);
    } else if (data[0] instanceof Chunk chunk) {
      return FindPlaceInChunk(chunk);
    }

    return new CoreResult(false, "Cannot find a good place to create a temple.");
  }

  // TODO Change this nonsense
  private CoreResult FindPlacePlayer(Player player) {
    final Location location = player.getLocation();
    for (int y1 = -5; y1 <= 5; y1++) {// Y
      for (int x1 = -20; x1 <= 20; x1++) {// X
        for (int z1 = -20; z1 <= 20; z1++) {// Z
          boolean canBuild = true;
          for (int y2 = -1; y2 <= 12; y2++) {// Y
            for (int x2 = 0; x2 <= 11; x2++) {// X
              for (int z2 = 0; z2 <= 10; z2++) {// Z
                if ((x1 + x2) == 0 && (y1 + y2) == 0 && (z1 + z2) == 0) {
                  canBuild = false;
                }
                if (y2 == -1) {
                  canBuild = canBuild && location.getWorld().getBlockAt(x1 + x2 + location.getBlockX(), y1 + y2 + location.getBlockY(), z1 + z2 + location.getBlockZ()).getType().isSolid();
                } else {
                  canBuild = canBuild && !location.getWorld().getBlockAt(x1 + x2 + location.getBlockX(), y1 + y2 + location.getBlockY(), z1 + z2 + location.getBlockZ()).getType().isSolid();
                }
              }
            }
          }
          if (canBuild) {
            Create(location.getWorld(), x1 + location.getBlockX(), y1 + location.getBlockY(), z1 + location.getBlockZ());

            return new CoreResult(true, "Creating a temple near %s.".formatted(player.getDisplayName()));
          }
        }
      }
    }

    return new CoreResult(false, "Cannot find a good place to create a temple.");
  }

  private CoreResult FindPlaceInChunk(Chunk chunk) {

    final Location location = chunk.getWorld().getHighestBlockAt(chunk.getBlock(2, 0, 2).getLocation()).getLocation();
    for (int y1 = -5; y1 <= 5; y1++) {// Y
      boolean canBuild = true;
      for (int y2 = -1; y2 <= 12; y2++) {// Y
        for (int x = 0; x <= 11; x++) {// X
          for (int z = 0; z <= 10; z++) {// Z
            if (x == 0 && (y1 + y2) == 0 && z == 0) {
              canBuild = false;
            }
            if (y2 == -1) {
              canBuild = canBuild && location.getWorld().getBlockAt(x + location.getBlockX(), y1 + y2 + location.getBlockY(), z + location.getBlockZ()).getType().isSolid();
            } else {
              canBuild = canBuild && !location.getWorld().getBlockAt(x + location.getBlockX(), y1 + y2 + location.getBlockY(), z + location.getBlockZ()).getType().isSolid();
            }
          }
        }
      }
      if (canBuild) {
        Create(location.getWorld(), location.getBlockX(), y1 + location.getBlockY(), location.getBlockZ());

        return new CoreResult(true, "Creating temple.");
      }

    }

    return new CoreResult(false, "Cannot find a good place to create a temple.");
  }

  public void Create(World world, int X, int Y, int Z) {

    Location loc = new Location(world, X, Y, Z);

    if (Herobrine.getPluginCore().getSupport().checkBuild(new Location(world, X, Y, Z))) {

      int MainX = loc.getBlockX();
      int MainY = loc.getBlockY();
      int MainZ = loc.getBlockZ();

      // Main blocks

      new StructureLoader(Herobrine.getPluginCore().getInputStreamData("/res/temple.yml")).Build(loc.getWorld(), MainX, MainY, MainZ);
      loc.getWorld().getBlockAt(MainX + 6, MainY + 0, MainZ + 2).setType(Material.CHEST);
      // Mob spawn
      if (!Herobrine.isNPCDisabled) {
        if (Herobrine.getPluginCore().getConfigDB().UseNPC_Guardian) {
          Location mobloc = new Location(loc.getWorld(), MainX + 6, MainY + 0, MainZ + 4);
          for (int i = 1; i <= Herobrine.getPluginCore().getConfigDB().npc.getInt("npc.Guardian.SpawnCount"); i++) {
            Herobrine.getPluginCore().getEntityManager().spawnCustomZombie(mobloc, MobType.ARTIFACT_GUARDIAN);
          }
        }
      }
      // Chest
      Random generator = Utils.getRandomGen();
      int chance = generator.nextInt(15);
      ItemStack item = null;
      ArrayList<String> newLore = new ArrayList<String>();

      if (chance < 4 && Herobrine.getPluginCore().getConfigDB().UseArtifactBow) {

        item = new ItemStack(Material.BOW);
        newLore.add("Herobrine artifact");
        newLore.add("Bow of Teleporting");
        item = ItemName.setNameAndLore(item, "Bow of Teleporting", newLore);
        item.addEnchantment(Enchantment.ARROW_FIRE, 1);
        item.addEnchantment(Enchantment.ARROW_KNOCKBACK, 1);

      } else if (chance < 8 && Herobrine.getPluginCore().getConfigDB().UseArtifactSword) {

        item = new ItemStack(Material.DIAMOND_SWORD);
        newLore.add("Herobrine artifact");
        newLore.add("Sword of Lightning");
        item = ItemName.setNameAndLore(item, "Sword of Lightning", newLore);
        item.addEnchantment(Enchantment.KNOCKBACK, 2);
        item.addEnchantment(Enchantment.DAMAGE_ALL, 2);
        item.addEnchantment(Enchantment.DURABILITY, 3);

      } else if (chance < 12 && Herobrine.getPluginCore().getConfigDB().UseArtifactApple) {

        item = new ItemStack(Material.GOLDEN_APPLE);
        newLore.add("Herobrine artifact");
        newLore.add("Apple of Death");
        item = ItemName.setNameAndLore(item, "Apple of Death", newLore);

      } else {
        if (Herobrine.getPluginCore().getConfigDB().UseAncientSword) {
          item = Herobrine.getPluginCore().getAICore().createAncientSword();
          item.addEnchantment(Enchantment.KNOCKBACK, 2);
          item.addEnchantment(Enchantment.DAMAGE_ALL, 2);
        }
      }

      Chest chest = (Chest) loc.getWorld().getBlockAt(MainX + 6, MainY + 0, MainZ + 2).getState();
      chest.getBlockInventory().setItem(chest.getInventory().firstEmpty(), item);
    }
  }

}
