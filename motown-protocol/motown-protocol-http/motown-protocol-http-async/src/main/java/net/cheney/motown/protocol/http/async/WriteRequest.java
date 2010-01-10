package net.cheney.motown.protocol.http.async;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class WriteRequest extends net.cheney.rev.channel.AsyncSocketChannel.WriteRequest {

	private final ByteBuffer buffer;

	public WriteRequest(ByteBuffer buffer) {
		this.buffer = buffer;
	}
	
	@Override
	public boolean accept(SocketChannel channel) throws IOException {
		channel.write(buffer);
		return !buffer.hasRemaining();
	}

}
