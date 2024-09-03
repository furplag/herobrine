package net.theprogrammersworld.herobrine.AI;

import java.util.Random;

import org.bukkit.entity.Player;

import net.theprogrammersworld.herobrine.Herobrine;

public class Message {

  public static void sendRandomMessage(Player player) {
    if (Herobrine.getInstance().getConfigDB().SendMessages == true) {

      int count = Herobrine.getInstance().getConfigDB().useMessages.size();

      Random randgen = new Random();
      int randmsg = randgen.nextInt(count);

      player.sendMessage("<Herobrine> " + Herobrine.getInstance().getConfigDB().useMessages.get(randmsg));

    }
  }

}
