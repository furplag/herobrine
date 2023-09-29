package net.theprogrammersworld.herobrine.NPC.Network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.theprogrammersworld.herobrine.NPC.NPCCore;

public class NetworkHandler extends ServerGamePacketListenerImpl {

	public NetworkHandler(final NPCCore npcCore, final ServerPlayer entityPlayer) {
		super(npcCore.getServer().getMCServer(), npcCore.getNetworkCore(), entityPlayer);
	}

	@Override
	public void send(final Packet<?> packet) {

	}

}