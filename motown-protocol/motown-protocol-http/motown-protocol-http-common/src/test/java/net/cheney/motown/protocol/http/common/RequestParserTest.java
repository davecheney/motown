package net.cheney.motown.protocol.http.common;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.ByteBuffer;

import net.cheney.motown.api.Header;
import net.cheney.motown.api.Method;
import net.cheney.motown.api.Request;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class RequestParserTest {

	private RequestParser parser;

	@Before
	public void setup() {
		this.parser = new RequestParser();
	}
	
	@Test
	public void testGETRequest() {
		Request r = parser.parse("GET / HTTP/1.1\r\nHost: localhost:8080\r\nConnection: keep-alive\r\n\r\n");
		assertEquals(r.method(), Method.GET);
		assertEquals(r.uri(), URI.create("/"));
		assertFalse(r.closeRequested());
	}
	
	@Test 
	public void testTwoRequests() throws UnsupportedEncodingException {
		String requests = "OPTIONS / HTTP/1.1\r\nHost: 192.168.1.102:8080\r\nUser-Agent: " +
				"WebDAVFS/1.2.7 (01278000) Transmit/3.7 neon/0.25.4\r\nKeep-Alive: \r\nConnection: TE, Keep-Alive\r\nTE: trailers\r\n\r\n"+
				"PROPFIND / HTTP/1.1\r\nHost: 192.168.1.102:8080\r\nUser-Agent: WebDAVFS/1.2.7 (01278000) " +
				"Transmit/3.7 neon/0.25.4\r\nConnection: TE\r\nTE: trailers\r\nDepth: 0\r\nContent-Length: 121\r\nContent-Type: application/xml\r\n\r\n" +               
				"<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n<propfind xmlns=\"DAV:\"><prop>\r\n<resourcetype xmlns=\"DAV:\"/>\r\n</prop></propfind>\r\n";
		
		ByteBuffer buffer = ByteBuffer.wrap(requests.getBytes("US-ASCII"));
		Request options = parser.parse(buffer);
		parser.reset();
		Request propfind = parser.parse(buffer);
		
		assertTrue(options.method().equals(Method.OPTIONS));
		assertTrue(options.headers().containsKey(Header.HOST));
		assertTrue(options.headers().containsKey(Header.USER_AGENT));
		assertTrue(options.headers().containsKey(Header.KEEP_ALIVE));
		assertTrue(options.headers().containsKey(Header.CONNECTION));
		
		assertTrue(propfind.method().equals(Method.PROPFIND));
		assertTrue(propfind.headers().containsKey(Header.HOST));
		assertTrue(propfind.headers().containsKey(Header.USER_AGENT));
		assertTrue(propfind.headers().containsKey(Header.CONNECTION));
//		assertTrue(propfind.hasBody());
//		assertTrue(propfind.contentLength() == 121);

	}
}
