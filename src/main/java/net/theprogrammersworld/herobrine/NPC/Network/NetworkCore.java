package net.theprogrammersworld.herobrine.NPC.Network;

import net.minecraft.network.protocol.EnumProtocolDirection;
import net.minecraft.network.NetworkManager;

public class NetworkCore extends NetworkManager {

	public NetworkCore() {
		super(EnumProtocolDirection.SERVERBOUND);
	}

	@Override
	public void a() {

	}

}
