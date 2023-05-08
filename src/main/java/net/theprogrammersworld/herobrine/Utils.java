package net.theprogrammersworld.herobrine;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Utils {
  public static Random getRandom() {
    return ThreadLocalRandom.current();
  }

  public static Player getRandomPlayer() {/* @formatter:off */
    return Optional.ofNullable(Bukkit.getServer().getOnlinePlayers()).orElseGet(ArrayList::new)
      .parallelStream().filter((_e) -> _e.getEntityId() != Herobrine.getPluginCore().entityId)
      .findAny().orElse(null);
  /* @formatter:on */}
}
