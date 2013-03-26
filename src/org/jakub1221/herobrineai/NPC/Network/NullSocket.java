package org.jakub1221.herobrineai.NPC.Network;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.Socket;

public class NullSocket extends Socket {

	@Override
	public InputStream getInputStream() {
		byte[] buffer = new byte[5];
		return new ByteArrayInputStream(buffer);
	}

	@Override
	public OutputStream getOutputStream() {
		return new ByteArrayOutputStream();
	}

}
