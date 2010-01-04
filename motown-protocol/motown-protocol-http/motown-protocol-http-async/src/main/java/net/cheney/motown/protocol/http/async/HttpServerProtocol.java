package net.cheney.motown.protocol.http.async;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Collection;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import net.cheney.motown.api.Header;
import net.cheney.motown.api.Request;
import net.cheney.motown.api.Response;
import net.cheney.motown.protocol.common.HttpRequestHandler;
import net.cheney.motown.protocol.common.HttpResponseHandler;
import net.cheney.motown.protocol.http.common.RequestParser;
import net.cheney.rev.channel.AsyncSocketChannel;


public class HttpServerProtocol extends HttpProtocol<Request> implements HttpResponseHandler {

	private final HttpRequestHandler handler;
	
	public HttpServerProtocol(final AsyncSocketChannel channel, final HttpRequestHandler handler) {
		super(channel, new RequestParser());
		this.handler = handler; 
	}

	@Override
	final void onMessage(final Request request) {
		handler.handleRequest(request, this);
	}
	
	public final void sendResponse(final Response response, final boolean requestClose) {
		ByteBuffer header = buildHeaderBuffer(response, requestClose);
		ByteBuffer body = response.body();
		if(body == null) {
			write(header, requestClose);
		} else {
			write(new ByteBuffer[] { header, body }, requestClose);
		}
		reset();
		readRequest();
	}
	
	private final ByteBuffer buildHeaderBuffer(Response response, boolean requestClose) {
		CharBuffer buffer = CharBuffer.allocate(8192);
		buffer.append(String.format("%s %s %s\r\n", response.version(), response.status().code(), response.status().reason()));
		buffer.append(String.format("Date: %s\r\n", RFC1123_DATE_FORMAT.format(System.currentTimeMillis())));
		if (response.hasBody()) {
			buffer.append(String.format("Content-Length: %d\r\n", response.body().remaining()));
		}
		if (requestClose) {
			buffer.append("Connection: close\r\n");
		}
		for(Entry<Header, Collection<String>> header : response.headers().asMap().entrySet()) {
			buffer.append(String.format("%s: %s\r\n", header.getKey(), StringUtils.join(header.getValue(), ',')));
		}
		buffer.append("\r\n");
		return US_ASCII.encode((CharBuffer)buffer.flip());
	}

	@Override
	public final void onConnect() {
		readRequest();
	}

	@Override
	public final void onDisconnect() {
		
	}

}
