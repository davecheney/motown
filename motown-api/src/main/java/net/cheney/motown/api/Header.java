package net.cheney.motown.api;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

public enum Header {
	
	ACCEPT("Accept", Type.REQUEST),
	ACCEPT_RANGES("Accept-Ranges", Type.RESPONSE),
	ACCEPT_CHARSET("Accept-Charset", Type.REQUEST),
	ACCEPT_ENCODING("Accept-Encoding", Type.REQUEST),
	ACCEPT_LANGUAGE("Accept-Language", Type.REQUEST),
	AGE("Age", Type.RESPONSE),
	ALLOW("Allow", Type.ENTITY),
	AUTHORIZATION("Authorization", Type.REQUEST),
	CACHE_CONTROL("Cache-Control", Type.GENERAL),
	COOKIE("Cookie", Type.REQUEST),
	COOKIE2("Cookie2", Type.REQUEST),
	CONNECTION("Connection", Type.GENERAL),
	CONTENT_ENCODING("Content-Encoding", Type.ENTITY), 
	CONTENT_LANGUAGE("Content-Language", Type.ENTITY),
	CONTENT_LENGTH("Content-Length", Type.ENTITY),
	CONTENT_LOCATION("Content-Location", Type.ENTITY),
	CONTENT_MD5("Content-MD5", Type.ENTITY),
	CONTENT_RANGE("Content-Range", Type.ENTITY),
	CONTENT_TYPE("Content-Type", Type.ENTITY),
	DATE("Date", Type.GENERAL), 
	DAV("DAV", Type.RESPONSE), 
	DEPTH("Depth", Type.REQUEST), 
	DESTINATION("Destination", Type.REQUEST),
	ETAG("ETag", Type.RESPONSE),
	EXPECT("Expect", Type.REQUEST),
	EXPIRES("Expires", Type.ENTITY),
	FROM("From", Type.REQUEST),
	HOST("Host", Type.REQUEST),
	IF("If", Type.REQUEST),
	IF_MATCH("If-Match", Type.REQUEST), 
	IF_MODIFIED_SINCE("If-Modified-Since", Type.REQUEST),
	IF_NONE_MATCH("If-None-Match", Type.REQUEST), 
	IF_RANGE("If-Range", Type.REQUEST),
	IF_UNMODIFIED_SINCE("If-Unmodified-Since", Type.REQUEST), 
	LAST_MODIFIED("Last-Modified", Type.ENTITY), 
	LOCATION("Location", Type.RESPONSE),
	LOCK_TOKEN("Lock-Token", Type.REQUEST),
	MAX_FORWARDS("Max-Forwards", Type.REQUEST),
	MS_AUTHOR_BY("MS-Author-By", Type.RESPONSE),
	OVERWRITE("Overwrite", Type.REQUEST),
	PRAGMA("Pragma", Type.GENERAL),
	PROXY_AUTHORIZATION("Proxy-Authorization", Type.REQUEST),
	PROXY_AUTHENTICATE("Proxy-Authenticate", Type.RESPONSE),
	RANGE("Range", Type.REQUEST),
	REFERER("Referer", Type.REQUEST),
	RETRY_AFTER("Retry-After", Type.RESPONSE),
	SERVER("Server", Type.RESPONSE),
	SET_COOKIE("Set-Cookie", Type.RESPONSE),
	SET_COOKIE2("Set-Cookie2", Type.RESPONSE),
	TE("TE", Type.REQUEST),
	TIMEOUT("Timeout", Type.REQUEST),
	TRAILER("Trailer", Type.GENERAL),	
	TRANSFER_ENCODING("Transfer-Encoding", Type.GENERAL),
	UPGRADE("Upgrade", Type.GENERAL),
	VARY("Vary", Type.RESPONSE),
	VIA("Via", Type.GENERAL),
	USER_AGENT("User-Agent", Type.REQUEST),
	WARNING("Warning", Type.GENERAL),
	WWW_AUTHENTICATE("WWW-Authenticate", Type.RESPONSE),
	
	KEEP_ALIVE("Keep-Alive", Type.GENERAL); 
	
	public enum Type {
		GENERAL,
		REQUEST,
		RESPONSE,
		ENTITY
	}
	
	private static final Map<String, Header> HTTP_HEADER_MAP = new HashMap<String, Header>();
	
	static {
		for(Header header : Header.values()) {
			HTTP_HEADER_MAP.put(header.value, header);
		}
	}
	
	private final String value;
	private final Header.Type type;
	
	private Header(@Nonnull String name, @Nonnull Header.Type type) {
		this.value = name;
		this.type = type;
	}
	
	public static final Header parse(@Nonnull CharSequence c) {
		return HTTP_HEADER_MAP.get(c.toString());
	}
	
	public final Header.Type type() {
		return this.type;
	}
	
	@Override
	public String toString() {
		return value();
	}

	public String value() {
		return value;
	}
	
}