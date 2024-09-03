package net.theprogrammersworld.herobrine.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import com.google.common.base.CaseFormat;

import jp.furplag.sandbox.reflect.Reflections;
import jp.furplag.sandbox.reflect.SavageReflection;
import jp.furplag.sandbox.trebuchet.Trebuchet;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

public interface Config extends ConfigurationSerializable {

  @SerializableAs("jp.furplag.spigotmc.dynmap.extension.config.Config.HerobrineConfig")
  @RequiredArgsConstructor
  @Getter
  @EqualsAndHashCode
  @ToString
  public static class HerobrineConfig implements Config {

    /** whether available this plugin or not ( default: true ) */
    private final boolean enabled;

    /** enable to logging more details ( default: false ) */
    private final boolean debugEnabled;

    /** if true, Herobrine do nothing to players who has "herobrine.ignore" permission ( default: false ) */
    private final boolean ignorePermissionEnabled;

    /** notification when the newer version is available if true ( default: false ) */
    private final boolean updateCheck;

    /** whether or not Herobrine attack players ( default: true ) */
    private final boolean attackPlayer;

    /** whether or not Herobrine attack players in creative mode ( default: true, also needs enable to {@link #attackPlayer} ) */
    private final boolean attackInCreative;

    /** whether or not Herobrine attack players even towards OP ( default: true, also needs enable to {@link #attackPlayer} ) */
    private final boolean attackOperator;


    @SerializableAs("jp.furplag.spigotmc.dynmap.extension.config.Config.HerobrineConfig")
    @RequiredArgsConstructor
    @Getter
    @EqualsAndHashCode
    @ToString
    public static class Profile implements Config {

      /** UUID ( default: f84c6a790a4e45e0879bcd49ebd4c4e2 ) */
      private final String UUID;

      /** name ( default: Herobrine ) */
      private final String name;

      /** health ( default: 200 ) */
      private final int HP;

      /** graveyard world name ( default: herobrine_graveyard ) */
      private final String graveyardWorld;

      /** if true, Herobrine appears players list in the player who targetted by Herobrine ( default: true ) */
      private final boolean showInTabList;
    }

    @SerializableAs("jp.furplag.spigotmc.dynmap.extension.config.Config.HerobrineConfig")
    @RequiredArgsConstructor
    @Getter
    @EqualsAndHashCode
    @ToString
    public static class Build implements Config {
      /** identifier of structure */
      private final String id;

      /** whether or not Herobrine build this structure ( default: false ) */
      private final boolean enabled;

      /** interval to evaluate that whether or not Herobrine do building */
      private final int interval;

      /** percentage which Herobrine do building */
      private final int percentage;
    }
    private final Map<String, Build> builds;

    /** whether Herobrine summon lightnings or not ( default: true ) */
    private final boolean summonLightNing;

    /** whether Herobrine summon bats or not ( default: true ) */
    private final boolean summonBats;

    /** whether Herobrine summon wolves or not ( default: true ) */
    private final boolean summonWolves;

    /** whether or not players enable to kill Herobrine ( default: true, also needs enable to PVP in server config )  ) */
    private final boolean killable;

    /** random messages when Herobrine killed, empty ( "{}" ) means silence */
    private Set<String> dyingMessages;

    /** whether or not Herobrine bury players ( default: true ) */
    private final boolean buryPlayer;

    @SerializableAs("jp.furplag.spigotmc.dynmap.extension.config.Config.HerobrineConfig")
    @RequiredArgsConstructor
    @Getter
    @EqualsAndHashCode
    @ToString
    public static class Message implements Config {
      /** identifier of structure */
      private final String id;

      /** whether or not Herobrine tells (in chat, put signs, write books ) a message ( default: true ) */
      private final boolean enabled;

      /** maximum number of messages per hour ( default: 1 ) */
      private final int limit;

      /** percentage which Herobrine telling a message */
      private final int percentage;

      /** message ( s ), empty ( "[]" ) means silence */
      private final List<String> messages;
    }
    private final Map<String, Message> messages;

    @SerializableAs("jp.furplag.spigotmc.dynmap.extension.config.Config.HerobrineConfig")
    @RequiredArgsConstructor
    @Getter
    @EqualsAndHashCode
    @ToString
    public static class DropItem implements Config {
      /** item ID */
      private final String itemId;

      /** percentage of loot drops ( default: 25 ) */
      private final int percentage;

      /** number of drops ( default: 1 ) */
      private final int amount;
    }
    private final Map<String, DropItem> dropItems;

    /**  Sets whether or not Herobrine can destory torches when he attacks */
    private final boolean destoryTorches;

    /**  Sets the torch destruction radius */
    private final int destroyTorchesRadius;

    @SerializableAs("jp.furplag.spigotmc.dynmap.extension.config.Config.HerobrineConfig")
    @RequiredArgsConstructor
    @Getter
    @EqualsAndHashCode
    @ToString
    public static class Protection implements Config {
      private final String id;
      private final boolean enabled;
    }
    /** sets plugin name and toggles, Herobrine can perform in protection areas that specified by supported plugins .
     * <p>Supported plugins</p>
     * <ul>
     * <li>Factions</li>
     * <li>GriefPrevention</li>
     * <li>MassiveCore</li>
     * <li>PreciousStones</li>
     * <li>RedProtect</li>
     * <li>Residence</li>
     * <li>Towny</li>
     * <li>WorldGuard</li>
     * </ul>
     */
    private final Map<String, Protection> protections;

    /**  Sets the item that Herobrine is holding in his hand (this setting is based on item data values) */
    private final Object ItemInHand;

    /**  Sets whether or not Herobrine can fly or if he can only walk */
    private final Object OnlyWalkingMode;

    /**  Set the interval in milliseconds at which Herobrine will select a target (this setting also depends on ShowRate) */
    private final Object ShowInterval;

    /**  Set the chance of Herobrine's target seeing Herobrine, there are three levels (1 = sometimes, 2 = often, 3 = very often) */
    private final Object ShowRate;

    /**  Sets whether or not Herobrine's Totem explodes after he is summoned */
    private final Object TotemExplodes;

    /**  Sets whether Herobrine's Ancient Sword can be found in Herobrine's Temples */
    private final Object UseAncientSword;

    /**  Sets what artifacts Herobrine can place in temples */
    private final Object UseArtifacts;

    /**  Sets whether or not Herobrine can teleport players to Herobrine's Graveyard */
    private final Object UseGraveyardWorld;

    /**  Sets whether or not Herobrine can spawn heads */
    private final Object UseHeads;

    /**  Sets whether or not the ignore Herobrine permission can be used (hb-ai.ignore) */
    private final Object UseIgnorePermission;

    /**  Sets whether the plugin will spawn Herobrine's Demons */
    private final Object UseNPCDemon;

    /**  Sets whether the plugin will spawn Herobrine's Guardians */
    private final Object UseNPCGuardian;

    /**  Sets whether the plugin will spawn Herobrine's Warriors */
    private final Object UseNPCWarrior;

    /**  Sets whether or not Herobrine can use potions on players */
    private final Object UsePotionEffects;

    /**  Sets whether or not Herobrine can be summoned by building Herobrine's Totem */
    private final Object UseTotem;

    /**  Sets the X radius where Herobrine can be in WalkingMode */
    private final Object WalkingModeRadiusX;

    /**  Sets the Z radius where Herobrine can be in WalkingMode */
    private final Object WalkingModeRadiusZ;

    /**
     * world name(s) that Herobrine has access to,
     * empty means "Herobrine comes everywhere"
     */
    private final Set<String> worlds = new HashSet<>();

    @SerializableAs("jp.furplag.spigotmc.dynmap.extension.config.Config.HerobrineConfig.HerobrineProfile")
    @RequiredArgsConstructor
    @Getter
    @EqualsAndHashCode
    @ToString
    public static class HerobrineProfile implements Config {
      private final String name;
      private final String uuid;
      private final String key;
    }
    private final HerobrineProfile profile;

    public static class Messages {

    }
  }

  default boolean relaxingBinding(final ConfigurationSection config, final String... exculudes) {
    final Set<String> _exculudes = Arrays.stream(Optional.ofNullable(exculudes).orElse(new String[] {})).filter(StringUtils::isNotBlank).distinct().collect(Collectors.toUnmodifiableSet());

    return config != null && Arrays.stream(Reflections.getFields(this))
      .filter((f) -> !Reflections.isStatic(f)).filter((f) -> !_exculudes.contains(f.getName()))
      .map((f) -> {
        final Object newValue = config.get(f.getName(), config.get(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_HYPHEN, f.getName())));

        return newValue != null && Reflections.isAssignable(this, f, newValue) ? Map.entry(f, newValue) : null;
      }).filter(Objects::nonNull)
      .map((e) -> Trebuchet.Predicates.orNot(this, e.getKey(), e.getValue(), SavageReflection::set))
      .allMatch((r) -> r);
  }

  private static <T extends Config> Stream<Map.Entry<String, Object>> _serialize(final T config) {
    return SavageReflection.read(config).entrySet().stream().filter(Objects::nonNull).filter((e) -> e != null && StringUtils.isNotBlank(e.getKey()) && e.getValue() != null);
  }

  /** {@inheritDoc} */ @Override default Map<String, Object> serialize() {
    return _serialize(this).map((entry) -> {
      if (entry.getValue() instanceof Config _value) { return Map.entry(entry.getKey(), _value.serialize()); }
      if (entry.getValue() instanceof Collection<?> _value && _value.isEmpty()) { return null; }

      return entry;
    }).filter(Objects::nonNull).collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue, (_current, _new) -> _new));
  }
}
