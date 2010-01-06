package net.cheney.motown.protocol.http.common;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.ByteBuffer;

import net.cheney.motown.api.Header;
import net.cheney.motown.api.Method;
import net.cheney.motown.api.Request;

import static net.cheney.motown.api.Header.CONNECTION;
import static net.cheney.motown.api.Header.HOST;
import static net.cheney.motown.api.Method.OPTIONS;
import static net.cheney.motown.api.Method.PROPFIND;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

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
		assertEquals(r.header(CONNECTION), Lists.newArrayList("keep-alive"));
		assertEquals(r.header(HOST), Lists.newArrayList("localhost:8080"));
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
		
		assertEquals(options.method(), OPTIONS);
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
	
	@Test 
	public void testRequestWithEncodedSpaces() {
		String request = "PROPFIND /Puppet%20&%20Atlassian.key HTTP/1.1\r\nHost: localhost:8080\r\nUser-Agent: WebDAVFS/1.8 (01808000) Darwin/10.2.0 (i386)\r\nAccept: */*\r\nContent-Type: text/xml\r\nDepth: 0\r\nContent-Length: 161\r\nConnection: keep-alive\r\n\r\n";
		Request propfind = parser.parse(request);
		
		assertEquals(propfind.method(), PROPFIND);
		assertEquals(propfind.uri().getPath(), "/Puppet & Atlassian.key");
	}
}
