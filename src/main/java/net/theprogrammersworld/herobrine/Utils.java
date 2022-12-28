package net.theprogrammersworld.herobrine;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Utils {

  private static Random randomGen = new Random();

  public static Random getRandomGen() {
    return randomGen;
  }

  public static Player getRandomPlayer() {/* @formatter:off */
    return Optional.ofNullable(Bukkit.getServer().getOnlinePlayers()).orElseGet(ArrayList::new)
      .parallelStream().filter((_e) -> _e.getEntityId() != Herobrine.getPluginCore().HerobrineEntityID)
      .findAny().orElse(null);
  /* @formatter:on */}
}
