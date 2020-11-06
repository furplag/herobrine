package net.theprogrammersworld.herobrine.NPC.Network;

import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class NullSocket extends Socket {

	@Override
	public InputStream getInputStream() {
		return new VoidInputStream();
	}

	@Override
	public OutputStream getOutputStream() {
		return new VoidOutputStream();
	}

	private class VoidInputStream extends InputStream {

		@Override
		public int read() throws IOException {
			return -1;
		}

	}

	private class VoidOutputStream extends OutputStream {

		@Override
		public void write(int b) throws IOException {
		}

	}

}
