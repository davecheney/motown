package net.cheney.motown.protocol.http.async;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;

public abstract class ReadRequest extends net.cheney.rev.channel.AsyncSocketChannel.ReadRequest {

	private final ByteBuffer buffer;

	public ReadRequest(ByteBuffer buffer) {
		this.buffer = buffer;
	}

	@Override
	public boolean accept(SocketChannel channel) throws IOException {
		switch(channel.read(buffer)) {
		case -1:
			throw new ClosedChannelException();
			
		case 0:
			return false;
			
		default:
			return true;
		}
	}

}
