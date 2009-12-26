package net.cheney.motown.protocol.http.async;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.TimeZone;

import net.cheney.motown.api.Message;
import net.cheney.motown.protocol.http.common.BodyHandler;
import net.cheney.motown.protocol.http.common.HttpParser;
import net.cheney.rev.channel.AsyncSocketChannel;
import net.cheney.rev.protocol.Protocol;

import org.apache.commons.lang.time.FastDateFormat;

abstract class HttpProtocol<V extends Message> extends Protocol {
	
	private static final int DEFAULT_BUFFER_SIZE = 8192;

	protected static final FastDateFormat RFC1123_DATE_FORMAT = FastDateFormat.getInstance("EEE, dd MMM yyyy HH:mm:ss zzz", TimeZone.getTimeZone("GMT"), Locale.US);
	
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
	

	private BodyHandler bodyHandler = FAILSAFE_BODY_HANDLER;
	
	private V message;

	private final HttpParser<V> parser;
	
	protected HttpProtocol(final AsyncSocketChannel channel, HttpParser<V> parser) {
		super(channel);
		this.parser = parser;
		reset();
	}

	void headerReceived(final ByteBuffer buffer) {
		buffer.flip();
		message = parseBuffer(buffer);
		if(message == null) {
			readHeader(buffer);
		} else {
			handleBody(buffer);
		}
	}
	
	private void readHeader(final ByteBuffer buffer) {
		channel().send(new ReadRequest(buffer) {
			
			@Override
			public void completed() {
				headerReceived(buffer);
			}
			
		});
	}

	void handleBody(final ByteBuffer buffer) {
		// handle body
		switch(message.transferCoding()) {
		case NONE:
			final int contentLength = message.contentLength();
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
		if(close) writeAndClose(buffer); else write(buffer);
	}
	
	protected final void write(ByteBuffer[] buffer, boolean close) {
		if(close) writeAndClose(buffer); else write(buffer);
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
				
			});
		}
		
	}
	
}