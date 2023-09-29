package net.theprogrammersworld.herobrine.NPC.Network;

import io.netty.channel.embedded.EmbeddedChannel;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.PacketFlow;

public class NetworkCore extends Connection {

	public NetworkCore() {
		super(PacketFlow.SERVERBOUND);
		channel = new EmbeddedChannel();
	}

	@Override public void setListener(PacketListener packetListener) {}

	@Override
	public void tick() {

	}

}
