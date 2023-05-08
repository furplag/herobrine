package net.theprogrammersworld.herobrine.misc;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.block.data.Rotatable;

public class BlockChanger {

  private static final Map<Integer, BlockFace> directions;
  static {/* @formatter:off */
    directions = Map.ofEntries(
        Map.entry(0, BlockFace.WEST)
      , Map.entry(1, BlockFace.WEST_NORTH_WEST), Map.entry(2, BlockFace.NORTH_WEST), Map.entry(3, BlockFace.NORTH_NORTH_WEST)
      , Map.entry(4, BlockFace.NORTH)
      , Map.entry(5, BlockFace.NORTH_NORTH_EAST), Map.entry(6, BlockFace.NORTH_EAST), Map.entry(7, BlockFace.EAST_NORTH_EAST)
      , Map.entry(8, BlockFace.EAST)
      , Map.entry(9, BlockFace.EAST_SOUTH_EAST), Map.entry(10, BlockFace.SOUTH_EAST), Map.entry(11, BlockFace.SOUTH_SOUTH_EAST)
      , Map.entry(12, BlockFace.SOUTH)
      , Map.entry(13, BlockFace.SOUTH_SOUTH_WEST), Map.entry(14, BlockFace.SOUTH_WEST), Map.entry(15, BlockFace.WEST_SOUTH_WEST)
  );/* @formatter:on */}

  public static BlockFace getPlayerBlockFace(final Location location) {
    final float y = location.getYaw();

    return directions.getOrDefault(( int ) ((((y + (y < 0 ? 360 : 0)) % 360) + 8 ) / 22.5), BlockFace.WEST);
  }

  public static void PlaceSkull(final Location location, final UUID uuid) {
    Optional.ofNullable(location.getBlock()).ifPresent((b) -> {
      b.setType(Material.PLAYER_HEAD);
      final Rotatable blockData = (Rotatable) b.getBlockData();
      blockData.setRotation(Set.of(BlockFace.WEST, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH).stream().findFirst().orElse(BlockFace.WEST));
      b.setBlockData(blockData);
      final Skull skull = (Skull) b.getState();
      skull.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
      skull.update(true);
    });
  }
}
