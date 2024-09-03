package net.theprogrammersworld.herobrine.AI.cores;

import java.util.Collection;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import lombok.extern.slf4j.Slf4j;
import net.theprogrammersworld.herobrine.ConfigDB;
import net.theprogrammersworld.herobrine.Herobrine;
import net.theprogrammersworld.herobrine.Utils;
import net.theprogrammersworld.herobrine.AI.AICore;
import net.theprogrammersworld.herobrine.AI.Core;
import net.theprogrammersworld.herobrine.AI.CoreResult;
import net.theprogrammersworld.herobrine.NPC.AI.Path;

@Slf4j(topic = "Minecraft")
public class RandomPosition extends Core {

  private static final Set<Material> unavailables = Set.of(/* @formatter:off */
    Material.AIR, Material.WATER, Material.LAVA, Material.GRASS_BLOCK, Material.SNOW
      , Material.WHEAT, Material.REDSTONE
      , Material.ACACIA_LEAVES, Material.AZALEA_LEAVES, Material.BIRCH_LEAVES, Material.CHERRY_LEAVES, Material.DARK_OAK_LEAVES
      , Material.FLOWERING_AZALEA_LEAVES, Material.JUNGLE_LEAVES, Material.MANGROVE_LEAVES, Material.OAK_LEAVES, Material.SPRUCE_LEAVES
      , Material.TORCH, Material.REDSTONE_TORCH, Material.SOUL_TORCH
  /* @formatter:on */);

  private int randomTicks = 0;
  private int randomMoveTicks = 0;
  private boolean RandomMoveIsPlayer = false;

  public RandomPosition() {
    super(Core.Type.RANDOM_POSITION, AppearType.APPEAR);
  }

  public int getRandomTicks() {
    return this.randomTicks;
  }

  public int getRandomMoveTicks() {
    return this.randomMoveTicks;
  }

  public void setRandomTicks(int i) {
    this.randomTicks = i;
  };

  public void setRandomMoveTicks(int i) {
    this.randomMoveTicks = i;
  };

  public CoreResult CallCore(Object[] data) {
    return setRandomPosition((World) data[0]);
  }

  public CoreResult setRandomPosition(World world) {
    if (plugin.getConfigDB().UseWalkingMode) {
      if (randomTicks != 3) {
        randomTicks++;
        if (plugin.getAICore().getCurrent() != Core.Type.RANDOM_POSITION && AICore.isTarget == false) {
          Location newloc = (Location) getRandomLocation(world);
          if (newloc != null) {

            plugin.HerobrineNPC.moveTo(newloc);
            newloc.setX(newloc.getX() + 2);
            newloc.setY(newloc.getY() + 1.5);
            plugin.HerobrineNPC.lookAtPoint(newloc);
            randomTicks = 0;
            log.info("[Herobrine] Herobrine is now in RandomLocation mode.");
            plugin.getAICore().Start_RM();
            plugin.getAICore().Start_RS();
            plugin.getAICore().Start_CG();
            RandomMoveIsPlayer = false;
            return new CoreResult(true, "Herobrine is now in WalkingMode.");
          } else {
            log.info("[Herobrine] RandomPosition Failed.");
            return setRandomPosition(world);
          }
        }
      } else {
        return new CoreResult(false, "WalkingMode - Find location failed.");
      }
    } else {
      return new CoreResult(false, "WalkingMode is disabled.");
    }
    return new CoreResult(false, "WalkingMode failed.");
  }

  public Location getRandomLocation(World world) {

    int r_nxtX = plugin.getConfigDB().WalkingModeXRadius;
    int nxtX = r_nxtX;
    if (nxtX < 0) {
      nxtX = -nxtX;
    }
    int r_nxtZ = plugin.getConfigDB().WalkingModeZRadius;
    int nxtZ = r_nxtZ;
    if (nxtZ < 0) {
      nxtZ = -nxtZ;
    }
    int randx = Utils.getRandom().nextInt(nxtX);

    int randy = 0;

    int randz = Utils.getRandom().nextInt(nxtZ);

    int randxp = Utils.getRandom().nextInt(1);

    int randzp = Utils.getRandom().nextInt(1);

    if (randxp == 0 && randx != 0) {
      randx = -(randx);
    }
    if (randzp == 0 && randz != 0) {
      randz = -(randz);
    }

    randx = randx + plugin.getConfigDB().WalkingModeFromXRadius;
    randz = randz + plugin.getConfigDB().WalkingModeFromZRadius;

    if (world != null) {
      randy = world.getHighestBlockYAt(randx, randz);
    } else {
      return null;
    }

    if (world.getBlockAt(randx, randy + 1, randz).getType().isAir() && world.getBlockAt(randx, randy + 2, randz).getType().isAir()) {
      if (!unavailables.contains(world.getBlockAt(randx, randy, randz).getType())) {

        randy++;

        log.info("[Herobrine] RandomLocation "
            + world.getBlockAt(randx, randy, randz).getType().toString() + " is X:" + randx + " Y:"
            + randy + " Z:" + randz);
        return new Location(world, (float) randx, (float) randy, (float) randz);
      }
    }

    return null;
  }

  public void RandomMove() {
    if (plugin.getAICore().getCurrent() == Core.Type.RANDOM_POSITION && AICore.isTarget == false
        && RandomMoveIsPlayer == false) {
      Herobrine.HerobrineHP = Herobrine.HerobrineMaxHP;

      if (Utils.getRandom().nextInt(5) == 3) {
        Location loc = plugin.HerobrineNPC.getBukkitEntity().getLocation();
        Path path = new Path((float) loc.getX() + Utils.getRandom().nextInt(30) - 15,
                   (float) loc.getZ() + Utils.getRandom().nextInt(30) - 15,
                   plugin);
        plugin.getPathManager().setPath(path);
      }

    }

  }

  public void CheckGravity() {

    if (plugin.getAICore().getCurrent() == Core.Type.RANDOM_POSITION && AICore.isTarget == false) {

      Location hbloc = (Location) plugin.HerobrineNPC.getBukkitEntity().getLocation();
      World w = (World) hbloc.getWorld();
      ConfigDB config = plugin.getConfigDB();

      if (hbloc.getBlockX() < config.WalkingModeXRadius + config.WalkingModeFromXRadius
        && hbloc.getBlockX() > (-config.WalkingModeXRadius) + config.WalkingModeFromXRadius
        && hbloc.getBlockZ() < config.WalkingModeZRadius + config.WalkingModeFromZRadius
        && hbloc.getBlockZ() > (-config.WalkingModeZRadius) + config.WalkingModeFromZRadius) {

        if (!w.getBlockAt(hbloc.getBlockX(), hbloc.getBlockY() - 1, hbloc.getBlockZ()).getType().isSolid()) {

          hbloc.setY(hbloc.getY() - 1);

          plugin.HerobrineNPC.moveTo(hbloc);

        }
      } else {
        plugin.getAICore().CancelTarget(Core.Type.RANDOM_POSITION);
      }
    }
  }

  public void CheckPlayerPosition() {
    boolean isThere = false;
    Location loc = (Location) plugin.HerobrineNPC.getBukkitEntity().getLocation();
    Collection<? extends Player> onlinePlayers = Bukkit.getServer().getOnlinePlayers();

    if (Bukkit.getServer().getOnlinePlayers().size() > 0) {

      for (Player player : onlinePlayers) {

        if (plugin.entityId != player.getEntityId()) {
          Location ploc = (Location) player.getLocation();

          if (ploc.getWorld() == loc.getWorld()
            && ploc.getX() + 7 > loc.getX()
            && ploc.getX() - 7 < loc.getX()
            && ploc.getZ() + 7 > loc.getZ()
            && ploc.getZ() - 7 < loc.getZ()
            && ploc.getY() + 7 > loc.getY()
            && ploc.getY() - 7 < loc.getY()) {

            loc.setY(-100);
            plugin.HerobrineNPC.moveTo(loc);
            plugin.getAICore().CancelTarget(Core.Type.RANDOM_POSITION);
            RandomMoveIsPlayer = false;
            plugin.getAICore().setAttackTarget(player);
            break;
          } else {

            if (ploc.getWorld() == loc.getWorld()
              && ploc.getX() + 15 > loc.getX()
              && ploc.getX() - 15 < loc.getX()
              && ploc.getZ() + 15 > loc.getZ()
              && ploc.getZ() - 15 < loc.getZ()
              && ploc.getY() + 15 > loc.getY()
              && ploc.getY() - 15 < loc.getY()) {

              ploc.setY(ploc.getY() + 1.5);
              plugin.HerobrineNPC.lookAtPoint(ploc);
              plugin.getPathManager().setPath(null);
              isThere = true;
              break;
            }
          }
        }
      }
    }

    if (isThere) {
      RandomMoveIsPlayer = true;
    } else {
      RandomMoveIsPlayer = false;
    }

  }

}
