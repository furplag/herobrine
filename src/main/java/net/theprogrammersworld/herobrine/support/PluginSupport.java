package net.theprogrammersworld.herobrine.support;

import java.util.Objects;
import java.util.Set;

public interface PluginSupport {

  static final Set<PluginSupport> supportPlugins = Set.of(/* @formatter:off */
    new PluginSupport() { @Override public String getPluginName() { return "WorldGuard"; }
      @Override public boolean isProtected(org.bukkit.Location location) {
        final com.sk89q.worldguard.protection.ApplicableRegionSet _applicableRegionSet = com.sk89q.worldguard.WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery().getApplicableRegions(com.sk89q.worldedit.bukkit.BukkitAdapter.adapt(location));

        return _applicableRegionSet != null && _applicableRegionSet.size() > 0;
      }
    }/* @formatter:on */);

  default org.bukkit.plugin.Plugin getPlugin() {
    return org.bukkit.Bukkit.getServer().getPluginManager().getPlugin(getPluginName());
  }

  String getPluginName();

  default boolean isAvailable() {
    return Objects.nonNull(getPlugin());
  }

  boolean isProtected(final org.bukkit.Location location);
}
