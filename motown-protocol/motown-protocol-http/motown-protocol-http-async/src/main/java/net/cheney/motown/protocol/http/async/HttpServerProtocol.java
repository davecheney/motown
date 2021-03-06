package net.cheney.motown.protocol.http.async;

import static java.lang.String.format;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

import net.cheney.motown.common.api.Header;
import net.cheney.motown.common.api.Request;
import net.cheney.motown.common.api.Response;
import net.cheney.motown.common.parser.RequestParser;
import net.cheney.motown.server.api.Application;
//import net.cheney.motown.protocol.common.HttpRequestHandler;
//import net.cheney.motown.protocol.common.HttpResponseHandler;
import net.cheney.rev.channel.AsyncSocketChannel;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


public class HttpServerProtocol extends HttpProtocol<Request>  {
	private static final Logger LOG = Logger.getLogger(HttpServerProtocol.class);
	
	private final Adapter handler;
	
	HttpServerProtocol(final AsyncSocketChannel channel, final Application app) {
		super(channel, new RequestParser());
		this.handler = new Adapter(app, this);
	}

	@Override
	final void onMessage(final Request request) {
		handler.handleRequest(request);
	}
	
	public final void sendResponse(final Response response, boolean requestClose) {
		try {
			sendResponse0(response, requestClose);
		} catch (IOException e) {
			LOG.error(e);
			shutdown();
		}
	}

	private void sendResponse0(Response response, boolean requestClose) throws IOException {
		ByteBuffer header = buildHeaderBuffer(response, requestClose);
		if(response.hasBody()) {
			if(response.buffer() != null) {
				write(new ByteBuffer[] { header, response.buffer() }, requestClose);
			} else { 
				write(header, response.channel(), requestClose);
			}
		} else {
			write(header, requestClose);
		}
		reset();
		readRequest();
	}

	private final ByteBuffer buildHeaderBuffer(Response response, boolean requestClose) throws IOException {
		CharBuffer buffer = CharBuffer.allocate(8192);
		buffer.append(format("%s %s %s\r\n", response.version(), response.status().code(), response.status().reason()));
		
		// http://tools.ietf.org/html/draft-ietf-httpbis-p1-messaging-08#section-3.4
		// elide Content-Length header where not permitted
		// TODO: needs unit test
		if (response.mayContainBody()) {
			if (response.hasBody()) {
				buffer.append(format("Content-Length: %d\r\n", response.contentLength()));
			} else {
				buffer.append("Content-Length: 0\r\n");
			}
		}
		if (requestClose) {
			buffer.append("Connection: close\r\n");
		}
		for(Header header : response.headers().keySet()) {
			buffer.append(format("%s: %s\r\n", header.value(), StringUtils.join(response.header(header).iterator(), ',')));
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
