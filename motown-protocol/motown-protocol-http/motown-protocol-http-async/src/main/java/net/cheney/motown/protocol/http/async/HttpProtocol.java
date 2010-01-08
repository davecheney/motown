package net.cheney.motown.protocol.http.async;

import static java.lang.Integer.parseInt;
import static net.cheney.motown.api.Header.CONTENT_LENGTH;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import net.cheney.motown.api.Header;
import net.cheney.motown.api.Message;
import net.cheney.motown.protocol.http.common.BodyHandler;
import net.cheney.motown.protocol.http.common.HttpParser;
import net.cheney.rev.channel.AsyncSocketChannel;
import net.cheney.rev.protocol.Protocol;

import org.apache.log4j.Logger;

abstract class HttpProtocol<V extends Message> extends Protocol {
	private static final Logger LOG = Logger.getLogger(HttpProtocol.class);
	
	private static final int DEFAULT_BUFFER_SIZE = 8192;
	
	protected static final Charset US_ASCII = Charset.forName("US-ASCII");
	
	protected static final byte[] CRLF = "\r\n".getBytes();
	protected static final byte[] CONNECTION_CLOSE = "Connection: close\r\n\r\n".getBytes();
	protected static final byte[] SPACE = " ".getBytes();
	protected static final byte[] COLON_SPACE = ": ".getBytes();
	
	private static class FailsafeBodyHandler implements BodyHandler {
		public void bodyReceived(final ByteBuffer buffer) {
			throw new RuntimeException("FAILSAFE BODY HANDLER called");
		}		
	}
	
	private static final BodyHandler FAILSAFE_BODY_HANDLER = new FailsafeBodyHandler();

	private BodyHandler bodyHandler;
	
	private V message;

	private final HttpParser<V> parser;
	
	protected HttpProtocol(final AsyncSocketChannel channel, HttpParser<V> parser) {
		super(channel);
		this.parser = parser;
		reset();
	}

	void headerReceived(final ByteBuffer buffer) {
		message = parseBuffer(buffer);
		if(message == null) {
			readHeader(buffer.compact());
		} else {
			handleBody(buffer);
		}
	}
	
	private void readHeader(final ByteBuffer buffer) {
		channel().send(new ReadRequest(buffer) {
			
			@Override
			public void completed() {
				headerReceived((ByteBuffer) buffer.flip());
			}
			
			@Override
			public void failed(Throwable t) {
				LOG.fatal("Unable to read header due to unhandled exception", t);
				shutdown();
			}
			
		});
	}

	protected void shutdown() {
		try {
			channel().close();
		} catch (IOException ignored) {		}
	}

	void handleBody(final ByteBuffer buffer) {
		// handle body
		switch(message.transferCoding()) {
		case NONE:
			// does the message header indicate an entity body ?
			final int contentLength = parseInt(message.header(CONTENT_LENGTH).getOnlyElementWithDefault("0"));
			if (contentLength > 0) {
				final ByteBuffer bodyBuffer = ByteBuffer.allocate(contentLength).put(buffer);
				bodyHandler = new IdentityBodyHandler();
				bodyHandler.bodyReceived(bodyBuffer);
			} else {
				onMessage(message);
			}
			break;
		
		default:
			throw new IllegalArgumentException();
		}
	}

	private V parseBuffer(final ByteBuffer buffer) {
		return parser.parse(buffer);
	}

	abstract void onMessage(final V message);

	void reset() {
		parser.reset(); // TODO - suspect
		bodyHandler = FAILSAFE_BODY_HANDLER;
	}
	
	protected final void write(ByteBuffer buffer, boolean close) {
		if (close)
			writeAndClose(buffer);
		else
			write(buffer);
	}
	
	protected final void write(ByteBuffer[] buffer, boolean close) {
		if (close)
			writeAndClose(buffer);
		else
			write(buffer);
	}
	
	protected final void write(final ByteBuffer buffer) {
		channel().send(new WriteRequest(buffer));
	}
	
	protected final void writeAndClose(ByteBuffer buffer) {
		channel().send(new WriteRequest(buffer) {
			@Override
			public void completed() {
				channel().shutdownOutput();
			}
			
			@Override
			public void failed(Throwable t) {
				LOG.fatal("Unable to write response due to unhandled exception", t);
				shutdown();
			}
		});
	}
	
	protected final void write(final ByteBuffer[] buffers) {
		channel().send(new WriteVRequest(buffers));
	}
	
	protected final void writeAndClose(final ByteBuffer[] buffers) {
		channel().send(new WriteVRequest(buffers) { 
			@Override
			public void completed() {
				channel().shutdownOutput();
			}
			
			@Override
			public void failed(Throwable t) {
				LOG.fatal("Unable to write response due to unhandled exception", t);
				shutdown();
			}
		});
	}
	
	protected final void readRequest() {
		readHeader(ByteBuffer.allocate(DEFAULT_BUFFER_SIZE));
	}

	protected final class IdentityBodyHandler implements BodyHandler {
		
		@SuppressWarnings("unchecked")
		public void bodyReceived(final ByteBuffer buffer) {
			if(buffer.hasRemaining()) {
				readBody(buffer);
			} else {
				message = (V) message.setBody((ByteBuffer)buffer.flip());
				onMessage(message);
			}
		}

		private void readBody(final ByteBuffer buffer) {
			channel().send(new ReadRequest(buffer) {
				
				@Override
				public void completed() {
					bodyReceived(buffer);
				}
				
				@Override
				public void failed(Throwable t) {
					LOG.fatal("Unable to read request body due to unhandled exception", t);
					shutdown();
				}
				
			});
		}
		
	}
	
}