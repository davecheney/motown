package net.cheney.motown.protocol.http.async;

import java.io.IOException;

import javax.annotation.Nonnull;

import net.cheney.motown.common.api.Request;
import net.cheney.motown.protocol.common.HttpRequestHandler;
import net.cheney.rev.channel.AsyncSocketChannel;
import net.cheney.rev.protocol.ServerProtocolFactory;

public class HttpServerProtocolFactory extends ServerProtocolFactory {
	
	private final HttpRequestHandler handler;

	public HttpServerProtocolFactory(@Nonnull HttpRequestHandler handler) {
		this.handler = handler;
	}

	@Override
	public void onAccept(final AsyncSocketChannel channel) {
		final HttpProtocol<Request> protocol = new HttpServerProtocol(channel, handler);
		try {
			protocol.onConnect();
		} catch (IOException e) {
			closeQuietly(channel);
		}
	}

	private void closeQuietly(AsyncSocketChannel channel) {
		try {
			channel.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
