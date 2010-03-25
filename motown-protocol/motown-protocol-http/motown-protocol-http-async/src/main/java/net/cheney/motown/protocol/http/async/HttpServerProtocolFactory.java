package net.cheney.motown.protocol.http.async;

import java.io.IOException;

import javax.annotation.Nonnull;

import net.cheney.motown.common.api.Request;
import net.cheney.motown.server.api.Application;
import net.cheney.rev.channel.AsyncSocketChannel;
import net.cheney.rev.protocol.ServerProtocolFactory;

public class HttpServerProtocolFactory extends ServerProtocolFactory {
	
	private final Application application;

	public HttpServerProtocolFactory(@Nonnull Application app) {
		this.application = app;
	}

	@Override
	public void onAccept(final AsyncSocketChannel channel) {
		final HttpProtocol<Request> protocol = new HttpServerProtocol(channel, application);
		try {
			protocol.onConnect();
		} catch (IOException e) {
			closeQuietly(channel);
		}
	}

	private void closeQuietly(AsyncSocketChannel channel) {
		try {
			channel.close();
		} catch (IOException ignored) { 
			
		}
	}

}
