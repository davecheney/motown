package net.cheney.motown.protocol.http.common;

import java.net.URI;

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
}
