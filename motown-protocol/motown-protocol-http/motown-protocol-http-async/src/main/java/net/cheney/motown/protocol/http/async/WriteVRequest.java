package net.cheney.motown.protocol.http.async;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class WriteVRequest extends
		net.cheney.rev.channel.AsyncSocketChannel.WriteRequest {

	private final ByteBuffer[] buffers;

	public WriteVRequest(ByteBuffer[] buffers) {
		this.buffers = buffers;
	}
	
	@Override
	public boolean writeTo(SocketChannel channel) throws IOException {
		channel.write(buffers);
		return !buffers[buffers.length - 1].hasRemaining();
	}

	@Override
	public void completed() {
		// TODO Auto-generated method stub
		
	}
	

}
