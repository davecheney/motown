package net.cheney.motown.protocol.http.common;

import static org.junit.Assert.assertEquals;

import java.net.URI;

import net.cheney.motown.api.Method;
import net.cheney.motown.api.RequestLine;
import net.cheney.motown.api.Version;

import org.junit.Before;
import org.junit.Test;

public class RequestLineParserTest {

	private RequestLineParser parser;

	@Before
	public void setup() {
		this.parser = new RequestLineParser();
	}

	@Test
	public void testGETRequest() {
		RequestLine r = parser.parse("GET /foo HTTP/1.0\r\n\r\n");
		assertEquals(r.method(), Method.GET);
		assertEquals(r.uri(), URI.create("/foo"));
		assertEquals(r.version(), Version.HTTP_1_0);
	}
	
	@Test
	public void testGETRequestWithTrailingData() {
		RequestLine r = parser.parse("GET /foo HTTP/1.0\r\nHost: localhost\r\n\r\n");
		assertEquals(r.method(), Method.GET);
		assertEquals(r.uri(), URI.create("/foo"));
		assertEquals(r.version(), Version.HTTP_1_0);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testUnknowMethod() {
		parser.parse("BRICK /foo HTTP/1.0\r\n");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testUnknownHttpVersion() {
		parser.parse("POST /foo HTTP/2.1\r\n");
	}

}
