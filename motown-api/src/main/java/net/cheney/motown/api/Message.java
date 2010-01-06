package net.cheney.motown.api;

import static com.google.common.collect.Iterables.getOnlyElement;
import static java.lang.Integer.parseInt;
import static net.cheney.motown.api.Header.CONNECTION;
import static net.cheney.motown.api.Header.CONTENT_LENGTH;
import static net.cheney.motown.api.Header.TRANSFER_ENCODING;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Iterator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

public abstract class Message {

	private final Multimap<Header, String> headers;
	private ByteBuffer body;
	private FileChannel channel;
	
	public enum TransferCoding { NONE, CHUNKED };
	
	public interface HttpHeader {

		Header.Type type();
		
		String value();
	}
	
	public class HeaderAccessor<V extends Message> implements Iterable<String> {

		private final Header header;

		public HeaderAccessor(Header header) {
			this.header = header;
		}

		@SuppressWarnings("unchecked")
		public V set(String value) {
			headers().replaceValues(header, Lists.newArrayList(value));
			return (V) Message.this;
		}

		public Iterator<String> iterator() {
			return headers().values().iterator();
		}

	}

	Message(@Nonnull Multimap<Header, String> headers, @Nullable ByteBuffer body) {
		this.headers = headers;
		this.body = body;
		this.channel = null;
	}
	
	Message(@Nonnull Multimap<Header, String> headers, @Nullable FileChannel channel) {
		this.headers = headers;
		this.body = null;
		this.channel = channel;
	}
	
	public abstract Version version();

	public final Multimap<Header, String> headers() {
		return headers;
	}
	
	@SuppressWarnings("unchecked")
	public <V extends Message> V setBody(ByteBuffer body) {
		this.body = body;
		if(this.channel != null) {
			try {
				channel.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.channel = null;
		return (V) this;
	}
	
	@SuppressWarnings("unchecked")
	public <V extends Message> V setBody(FileChannel channel) {
		this.channel = channel;
		this.body = null;
		return (V) this;
	}

	public final ByteBuffer body() {
		return body;
	}
	
	public final boolean hasBody() {
		if(body != null) {
			return body.hasRemaining();
		} else if (hasChannel()) {
			return true;
		} else { 
			return false;
		}
	}
	
	public final boolean hasChannel() {
		return channel != null;
	}

	public TransferCoding transferCoding() {
		return "chunked".equalsIgnoreCase(getOnlyElement(header(TRANSFER_ENCODING), null)) ? TransferCoding.CHUNKED : TransferCoding.NONE;
	}
	
	public final int contentLength() {
		return parseInt(getOnlyElement(header(CONTENT_LENGTH), "0"));
	}
	
	public boolean closeRequested() {
		return "close".equals(getOnlyElement(header(CONNECTION), ""));
	}

	public final boolean containsHeader(Header header) {
		return headers().containsKey(header);
	}
	
	public <V extends Message> HeaderAccessor<V> header(final Header header) {
		return new HeaderAccessor<V>(header);
	}

}
