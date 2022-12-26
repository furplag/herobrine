package net.theprogrammersworld.herobrine;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import net.theprogrammersworld.herobrine.support.PluginSupport;

public final class Support {

  private final Set<PluginSupport> availablePlugins = new HashSet<>();

  public Support() {/* @formatter:off */
    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Herobrine.getPluginCore(), new Runnable() {
      @Override public void run() {
        availablePlugins.addAll(PluginSupport.supportPlugins.parallelStream().filter(PluginSupport::isAvailable).peek((_p) -> Herobrine.log.info("[Herobrine] %s plugin detected on server".formatted(_p.getPluginName()))).collect(Collectors.toSet()));
      }
    }, 1 * 2L);
  /* @formatter:on */}

  public boolean isSecuredArea(final Location location) {
    return availablePlugins.parallelStream().allMatch((_p) -> _p.isProtected(location));
  }

  public boolean checkBuild(final Location location) {
    return isEnable(Herobrine.getPluginCore().getConfigDB().SecuredArea_Build, location);
  }

  public boolean checkAttack(final Location location) {
    return isEnable(Herobrine.getPluginCore().getConfigDB().SecuredArea_Attack, location);
  }

  public boolean checkHaunt(final Location location) {
    return isEnable(Herobrine.getPluginCore().getConfigDB().SecuredArea_Haunt, location);
  }

  public boolean checkSigns(final Location location) {
    return isEnable(Herobrine.getPluginCore().getConfigDB().SecuredArea_Signs, location);
  }

  public boolean checkBooks(final Location location) {
    return isEnable(Herobrine.getPluginCore().getConfigDB().SecuredArea_Books, location);
  }

  private boolean isEnable(final boolean configValue, final Location location) {
    return configValue || !isSecuredArea(location);
  }
}
