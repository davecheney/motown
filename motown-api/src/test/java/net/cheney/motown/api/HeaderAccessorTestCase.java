package net.cheney.motown.api;

import static net.cheney.motown.api.Header.HOST;
import static org.junit.Assert.assertEquals;

import java.nio.ByteBuffer;

import net.cheney.motown.protocol.http.common.RequestParser;

import org.junit.Test;


public class HeaderAccessorTestCase {

	@Test
	public void testRequestHeaderAccessor() {
		String s = "PROPFIND /Software%20Update%20Enabler.app HTTP/1.1\r\nHost: localhost:8080\r\nUser-Agent: WebDAVFS/1.8 (01808000) Darwin/10.2.0 (i386)\r\nAccept: */*\r\nContent-Type: text/xml\r\nDepth: 0\r\nContent-Length: 161\r\nConnection: keep-alive\r\n\r\n";
		Request request = new RequestParser().parse(ByteBuffer.wrap(s.getBytes()));
		
		assertEquals(request.header(HOST).getOnlyElement(), "localhost:8080");
	}
}
