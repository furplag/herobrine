package net.theprogrammersworld.herobrine.support;

import java.util.Objects;
import java.util.Set;

public interface PluginSupport {

  static final Set<PluginSupport> supportPlugins = Set.of(/* @formatter:off */
    new PluginSupport() { @Override public String getPluginName() { return "Factions"; }
      @Override public boolean isProtected(org.bukkit.Location location) {
        return !com.massivecraft.factions.entity.BoardColl.get().getFactionAt(com.massivecraft.massivecore.ps.PS.valueOf(location)).getComparisonName().equalsIgnoreCase("Wilderness");
      }
    },
    new PluginSupport() { @Override public String getPluginName() { return "GriefPrevention"; }
      @Override public boolean isProtected(org.bukkit.Location location) {
        return Objects.nonNull(me.ryanhamshire.GriefPrevention.GriefPrevention.instance.dataStore.getClaimAt(location, false, null));
      }
    },
    new PluginSupport() { @Override public String getPluginName() { return "PreciousStones"; }
      @Override public boolean isProtected(org.bukkit.Location location) {
        return net.sacredlabyrinth.Phaed.PreciousStones.PreciousStones.API().isFieldProtectingArea(net.sacredlabyrinth.Phaed.PreciousStones.field.FieldFlag.ALL, location);
      }
    },
    new PluginSupport() { @Override public String getPluginName() { return "RedProtect"; }
      @Override public boolean isProtected(org.bukkit.Location location) {
        return Objects.nonNull(br.net.fabiozumbi12.RedProtect.Bukkit.RedProtect.get().getAPI().getRegion(location));
      }
    },
    new PluginSupport() { @Override public String getPluginName() { return "Residence"; }
      @Override public boolean isProtected(org.bukkit.Location location) {
        return Objects.nonNull(((com.bekvon.bukkit.residence.Residence) getPlugin()).getResidenceManager().getByLoc(location));
      }
    },
    new PluginSupport() { @Override public String getPluginName() { return "Towny"; }
      @Override public boolean isProtected(org.bukkit.Location location) {
        return Objects.nonNull(com.palmergames.bukkit.towny.TownyAPI.getInstance().getTownBlock(location));
      }
    },
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
