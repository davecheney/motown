package net.cheney.motown.protocol.http.async;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class WriteChannelRequest extends net.cheney.rev.channel.AsyncSocketChannel.WriteRequest {

	private final FileChannel fc;
	private long position, count;

	public WriteChannelRequest(FileChannel fc) throws IOException {
		this.fc = fc;
		this.position = fc.position();
		this.count = fc.size();
	}

	@Override
	public void completed() {
		closeQuietly(fc);
	}
	
	@Override
	public boolean accept(SocketChannel channel) throws IOException {
		long c = fc.transferTo(position, count, channel);
		position += c;
		count -= c;
		return count == 0;
	}
	
}
