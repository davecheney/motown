package net.cheney.motown.common.api;

import static net.cheney.motown.common.api.Header.DEPTH;
import static net.cheney.motown.common.api.Header.DESTINATION;
import static net.cheney.motown.common.api.Header.OVERWRITE;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public final class Request extends Message {

	protected final RequestLine requestLine;

	private Request(RequestLine requestLine, Multimap<Header, String> headers, ByteBuffer body) {
		super(headers, body);
		this.requestLine = requestLine;
	}
	
	private Request(RequestLine requestLine, Multimap<Header, String> headers, FileChannel body) {
		super(headers, body);
		this.requestLine = requestLine;
	}
	
	public Request(Method method, String uri, Version version) throws URISyntaxException {
		this(new RequestLine(method, uri, version));
	}
	
	public Request(RequestLine requestLine) {
		this(requestLine, ArrayListMultimap.<Header, String>create(), (ByteBuffer) null);	
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
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
	@Override
	public boolean equals(Object that) {
		return EqualsBuilder.reflectionEquals(this, that);
	}
	
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
	
	public Depth getDepth(Depth defaultDepth) {
		return Depth.parse(header(DEPTH).getOnlyElementWithDefault(defaultDepth.toString()), defaultDepth);
	}

	public URI getDestination() {
		return URI.create(header(DESTINATION).getOnlyElementWithDefault(""));
	}

	public boolean isOverwrite() {
		return header(OVERWRITE).getOnlyElementWithDefault("T").equals("T");
	}
	
}
