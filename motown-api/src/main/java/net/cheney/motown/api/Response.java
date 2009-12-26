package net.cheney.motown.api;

import java.nio.ByteBuffer;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class Response extends Message {
	
	private final StatusLine statusLine;

	private Response(StatusLine statusLine, Multimap<Header, String> headers, ByteBuffer body) {
		super(headers, body);
		this.statusLine = statusLine;
	}

	public Response(Status status) {
		this(new StatusLine(Version.HTTP_1_1, status), ArrayListMultimap.<Header, String>create(), ByteBuffer.allocate(0));
	}
	
	public Response(Status status, Multimap<Header, String> headers, ByteBuffer body) {
		this(new StatusLine(Version.HTTP_1_1, status), headers, body);
	}

	public Status status() {
		return this.statusLine.status();
	}
	
	@Override
	public Version version() {
		return this.statusLine.version();
	}
	
	public static class Builder extends Message.Builder {

		private Multimap<Header, String> headers = ArrayListMultimap.create();
		private ByteBuffer body = ByteBuffer.allocate(0);
		private final Status status;

		public Builder(Status status) {
			this.status = status;
		}
		
		Status status() {
			return status;
		}
		
		@Override
		Version version() {
			return Version.HTTP_1_1;
		}
		
		@Override
		Multimap<Header, String> headers() {
			return headers;
		}
		
		@Override
		ByteBuffer body() {
			return body;
		}

		@Override
		public Response build() {
			return new Response(status(), headers(), body());
		}
		
	}
	
	@Override
	public Response setBody(final ByteBuffer body) {
		return new Builder(status()) {
			@Override
			ByteBuffer body() {
				return body;
			}
		}.build();
	}
}
