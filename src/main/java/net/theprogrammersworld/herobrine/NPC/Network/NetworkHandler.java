package net.theprogrammersworld.herobrine.NPC.Network;

import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.Packet;
import net.minecraft.server.v1_16_R3.PlayerConnection;
import net.theprogrammersworld.herobrine.NPC.NPCCore;

public class NetworkHandler extends PlayerConnection {

	public NetworkHandler(final NPCCore npcCore, final EntityPlayer entityPlayer) {
		super(npcCore.getServer().getMCServer(), npcCore.getNetworkCore(), entityPlayer);
	}

	@Override
	public void a(final double d0, final double d1, final double d2, final float f, final float f1) {
		
	}

	@Override
	public void sendPacket(final Packet<?> packet) {
		
	}

}