package net.cheney.motown.api;

import java.nio.ByteBuffer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;

public abstract class Message {

	private final Multimap<Header, String> headers;
	private final ByteBuffer body;
	
	public enum TransferCoding { NONE, CHUNKED };
	
	public interface HttpHeader {

		Header.Type type();
		
		String value();
	}

	Message(@Nonnull Multimap<Header, String> headers, @Nullable ByteBuffer body) {
		this.headers = headers;
		this.body = body;
	}
	
	public abstract Version version();

	public final Multimap<Header, String> headers() {
		return headers;
	}
	
	public abstract Message setBody(ByteBuffer body);

	public final ByteBuffer body() {
		return body;
	}
	
	public final boolean hasBody() {
		return body != null ? body.hasRemaining() : false;
	}
	
	public TransferCoding transferCoding() {
		return "chunked".equalsIgnoreCase(getOnlyElement(Header.TRANSFER_ENCODING)) ? TransferCoding.CHUNKED : TransferCoding.NONE;
	}
	
	private String getOnlyElement(Header header) {
		return getOnlyElement(header, null);
	}

	public final int contentLength() {
		return Integer.parseInt(Iterables.getOnlyElement(headers().get(Header.CONTENT_LENGTH), "0"));
	}
	
	public boolean closeRequested() {
		return "close".equals(Iterables.getOnlyElement(headers().get(Header.CONNECTION), ""));
	}

	public final boolean containsHeader(Header header) {
		return headers().containsKey(header);
	}
	
	protected String getOnlyElement(Header header, String defaultValue) {
		return Iterables.getOnlyElement(headers().get(header), defaultValue);
	}

	public abstract static class Builder {
		
		public abstract Message build();
		
		abstract Version version();
		
		abstract Multimap<Header, String> headers();
		
		abstract ByteBuffer body();

		public abstract Builder setBody(ByteBuffer buffer);

	}
}
