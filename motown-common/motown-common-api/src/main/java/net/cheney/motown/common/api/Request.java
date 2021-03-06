package net.cheney.motown.common.api;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.annotation.Nonnull;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public final class Request extends Message {

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
		this(requestLine, emptyMultiMap(), (ByteBuffer) null);	
	}
	
	private static Multimap<Header, String> emptyMultiMap() {
		return ArrayListMultimap.create();
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
	
}
