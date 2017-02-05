package org.jakub1221.herobrineai.NPC.Network;

import net.minecraft.server.v1_11_R1.EnumProtocolDirection;
import net.minecraft.server.v1_11_R1.NetworkManager;

public class NetworkCore extends NetworkManager {

	public NetworkCore() {
		super(EnumProtocolDirection.SERVERBOUND);
	}

	@Override
	public void a() {

	}

}
