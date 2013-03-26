package org.jakub1221.herobrineai.NPC.Network;

import java.io.IOException;

import net.minecraft.server.v1_5_R2.Connection;
import net.minecraft.server.v1_5_R2.NetworkManager;
import net.minecraft.server.v1_5_R2.Packet;

public class NetworkCore extends NetworkManager{
	
	public NetworkCore() throws IOException {
		super(null, new NullSocket(), "NPCCore", new Connection() {
			@Override
			public boolean a() {
				return true;
			}
		}, null);

	}

	@Override
	public void queue(Packet packet) {
	}
	
	@Override
	public void a() {
	}
	
	@Override
	public void a(String s, Object... aobject) {
	}
	
	@Override
	public void a(Connection nethandler) {
	}



}
