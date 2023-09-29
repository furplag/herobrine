package net.theprogrammersworld.herobrine.NPC.Network;

import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.Connection;

public class NetworkCore extends Connection {

	public NetworkCore() {
		super(PacketFlow.SERVERBOUND);
	}

	@Override
	public void tick() {

	}

}
