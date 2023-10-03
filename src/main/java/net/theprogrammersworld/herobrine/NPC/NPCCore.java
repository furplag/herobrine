package net.theprogrammersworld.herobrine.NPC;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.plugin.java.JavaPlugin;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import io.netty.channel.embedded.EmbeddedChannel;
import lombok.Getter;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.world.entity.Entity;
import net.theprogrammersworld.herobrine.Herobrine;
import net.theprogrammersworld.herobrine.NPC.Entity.HumanEntity;
import net.theprogrammersworld.herobrine.NPC.Entity.HumanNPC;
import net.theprogrammersworld.herobrine.NPC.NMS.NMSServer;

public class NPCCore {

  private static GameProfile getHerobrineGameProfile() {
    return new GameProfile(UUID.fromString(Herobrine.getPluginCore().getConfigDB().HerobrineUUID), Herobrine.getPluginCore().getConfigDB().HerobrineName) {{
      getProperties().put("textures", new Property("textures",
        "eyJ0aW1lc3RhbXAiOjE0MjE0ODczMzk3MTMsInByb2ZpbGVJZCI6ImY4NGM2YTc5MGE0ZTQ1ZTA4NzliY2Q0OWViZDRjNGUyIiwicHJvZmlsZU5hbWUiOiJIZXJvYnJpbmUiLCJpc1B1YmxpYyI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzk4YjdjYTNjN2QzMTRhNjFhYmVkOGZjMThkNzk3ZmMzMGI2ZWZjODQ0NTQyNWM0ZTI1MDk5N2U1MmU2Y2IifX19",
        "Edb1R3vm2NHUGyTPaOdXNQY9p5/Ez4xButUGY3tNKIJAzjJM5nQNrq54qyFhSZFVwIP6aM4Ivqmdb2AamXNeN0KgaaU/C514N+cUZNWdW5iiycPytfh7a6EsWXV4hCC9B2FoLkbXuxs/KAbKORtwNfFhQupAsmn9yP00e2c3ZQmS18LWwFg0vzFqvp4HvzJHqY/cTqUxdlSFDrQe/4rATe6Yx6v4zbZN2sHbSL+8AwlDDuP2Xr4SS6f8nABOxjSTlWMn6bToAYiymD+KUPoO0kQJ0Uw/pVXgWHYjQeM4BYf/FAxe8Bf1cP8S7VKueULkOxqIjXAp85uqKkU7dR/s4M4yHm6fhCOCLSMv6hi5ewTaFNYyhK+NXPftFqHcOxA1LbrjOe6NyphF/2FI79n90hagxJpWwNPz3/8I5rnGbYwBZPTsTnD8PszgQTNuWSuvZwGIXPIp9zb90xuU7g7VNWjzPVoOHfRNExEs7Dn9pG8CIA/m/a8koWW3pkbP/AMMWnwgHCr/peGdvF5fN+hJwVdpbfC9sJfzGwA7AgXG/6yqhl1U7YAp/aCVM9bZ94sav+kQghvN41jqOwy4F4i/swc7R4Fx2w5HFxVY3j7FChG7iuhqjUclm79YNhTG0lBQLiZbN5FmC9QgrNHRKlzgSZrXHWoG3YXFSqfn4J+Om9w="));
    }};
  }
  @Getter private final NMSServer server;
  @Getter private final Connection networkmanager;
  private final GameProfile herobrineProfile;
  private final int taskId;

  private final List<HumanNPC> npcs = new ArrayList<HumanNPC>();
  private final AtomicInteger lastId = new AtomicInteger();

  public NPCCore(JavaPlugin plugin) {
    
    server = NMSServer.getInstance();
    
    networkmanager = new Connection(PacketFlow.SERVERBOUND) {
      { channel = new EmbeddedChannel(); }
      @Override public void setListener(PacketListener packetListener) {}
      @Override public void tick() {}
    };
    
    taskId = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Herobrine.getPluginCore(), new Runnable() {
        @Override public void run() { npcs.removeIf((npc) -> !npc.getEntity().isAlive()); }
    }, 1L, 1L);
    
    this.herobrineProfile = getHerobrineGameProfile();
  }

  public void cancelTask() {
    Bukkit.getServer().getScheduler().cancelTask(taskId);
  }

  public HumanNPC getHumanNPC(final int id) {
    return npcs.parallelStream().filter((npc) -> npc.getId() == id).findFirst().orElse(null);
  }

  public void removeAll() {
    npcs.stream().filter(Objects::nonNull).forEach(HumanNPC::removeFromWorld);
    npcs.clear();
  }

  public HumanNPC spawnHumanNPC(String name, Location l) {
    return spawnHumanNPC(name, l, lastId.incrementAndGet());
  }

  private HumanNPC spawnHumanNPC(String name, Location l, int id) {
    final NMSServer.World world = server.getWorld(l.getWorld().getName());
    world.getCraftWorld().addEntity(new HumanEntity(this, world, herobrineProfile) {{
      forceSetPositionRotation(l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
      npcs.add(new HumanNPC(this, id));
    }}, SpawnReason.CUSTOM);

    return getHumanNPC(id);
  }
}
