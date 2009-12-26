package net.cheney.motown.api;

import java.net.URI;
import java.nio.ByteBuffer;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class Request extends Message {

	private final RequestLine requestLine;

	private Request(RequestLine requestLine, Multimap<Header, String> headers, ByteBuffer body) {
		super(headers, body);
		this.requestLine = requestLine;
	}
	
	public Request(Method method, String uri, Version version) {
		this(new RequestLine(method, uri, version));
	}
	
	public Request(RequestLine requestLine) {
		this(requestLine, ArrayListMultimap.<Header, String>create(), null);	
	}
	
	public Method method() {
		return this.requestLine.method();	
	}
	
	public URI uri() {
		return this.requestLine.uri();
	}
	
	@Override
	public Version version() {
		return this.requestLine.version();
	}
	
	public static Request.Builder builder(Method method, String uri) {
		return new Builder(method, uri);
	}
	
	public static class Builder extends Message.Builder {

		private Method method;
		private URI uri;
		private Multimap<Header, String> headers = ArrayListMultimap.create();
		private ByteBuffer body = ByteBuffer.allocate(0);

		public Builder(Method method, String uri) {
			this(method, URI.create(uri));
		}
		
		public Builder(Method method, URI uri) {
			this.method = method;
			this.uri = uri;
		}

		Method method() {
			return method;
		}
		
		URI uri() {
			return uri;
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
		public Request build() {
			return new Request(new RequestLine(method(), uri(), version()), headers(), body());
		}
		
	}

	@Override
	public Request setBody(final ByteBuffer body) {
		return new Builder(method(), uri()) {
			@Override
			ByteBuffer body() {
				return body;
			}
		}.build();
	}
}
