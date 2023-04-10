package net.theprogrammersworld.herobrine.NPC.AI;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import net.theprogrammersworld.herobrine.Herobrine;
import net.theprogrammersworld.herobrine.AI.Core.CoreType;

public class PathManager {

  @Getter
  @Setter
  private Path path;

  public void update() {
    if (path != null && Set.of(CoreType.ANY, CoreType.RANDOM_POSITION).contains(Herobrine.getPluginCore().getAICore().getCoreTypeNow())) {
      path.update();
    }
  }
}
