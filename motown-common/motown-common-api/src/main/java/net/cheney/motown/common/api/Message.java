package net.cheney.motown.common.api;

import static net.cheney.motown.common.api.Header.CONNECTION;
import static net.cheney.motown.common.api.Header.TRANSFER_ENCODING;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Collection;
import java.util.Iterator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

public abstract class Message {

	private final Multimap<Header, String> headers;
	private ByteBuffer body;
	private FileChannel channel;
	
	public enum TransferCoding { NONE, CHUNKED };
	
	public enum Method {
		// RFC 2516

		OPTIONS, GET, DELETE, HEAD, PUT, POST, TRACE,

		// RFC 2518

		// DAV Level 1

		COPY, MOVE, MKCOL, PROPFIND, PROPPATCH,

		// DAV Level 2

		LOCK, UNLOCK,

		// RFC 3744

		ACL,

		// DeltaV RFC 3253

		REPORT, MKACTIVITY, MERGE, CHECKIN, UNCHECKOUT, UPDATE, LABEL, MKWORKSPACE, VERSION_CONTROL, CHECKOUT, SEARCH,

		// CALDAV RFC 4791

		MKCALENDAR;

		public static Method parse(@Nonnull CharSequence method) {
			return valueOf(method.toString());
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
		closeQuietly(channel);
		this.channel = null;
		return (V) this;
	}
	
	private void closeQuietly(@Nullable Closeable channel) {
		try {
			if (channel != null) {
				channel.close();
			}
		} catch (IOException ignored) { }
	}

	@SuppressWarnings("unchecked")
	public <V extends Message> V setBody(FileChannel channel) {
		this.channel = channel;
		this.body = null;
		return (V) this;
	}

	public final ByteBuffer buffer() {
		return body;
	}
	
	public final FileChannel channel() {
		return channel;
	}
	
	public final boolean hasBody() {
		if (body != null ) {
			return body.hasRemaining();
		} else {
			return channel != null;
		}
	}
	
	public TransferCoding transferCoding() {
		return "chunked".equalsIgnoreCase(header(TRANSFER_ENCODING).getOnlyElementWithDefault(null)) ? TransferCoding.CHUNKED : TransferCoding.NONE;
	}
	
	public final long contentLength() throws IOException {
		if (body != null ) {
			return body.remaining();
		} else {
			return channel.size();
		}
	}
	
	public boolean closeRequested() {
		return "close".equals(header(CONNECTION).getOnlyElementWithDefault(""));
	}

	public final boolean containsHeader(Header header) {
		return headers().containsKey(header);
	}
	
	public <V extends Message> HeaderAccessor<V> header(final Header header) {
		return new HeaderAccessor<V>(header);
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
		
		public void setAll(Iterable<String> values) {
			headers().putAll(header, values);
		}

		public Iterator<String> iterator() {
			return get().iterator();
		}
		
		@Override
		public String toString() {
			return String.format("%s=%s", header, get()); 
		}
		
		@Override
		public boolean equals(Object obj) {
			return obj.equals(get());
		}

		private Collection<String> get() {
			return headers().get(header);
		}
		
		public String getOnlyElement() {
			return Iterables.getOnlyElement(get());
		}
		
		public String getOnlyElementWithDefault(String defaultValue) {
			return Iterables.getOnlyElement(get(), defaultValue);
		}

	}
	
}
