package net.cheney.motown.common.parser;

import static junit.framework.Assert.assertEquals;

import java.net.URI;

import net.cheney.motown.common.api.RequestLine;
import net.cheney.motown.common.api.Version;
import net.cheney.motown.common.api.Request.Method;

import org.junit.Test;

public class RequestLineTest {

	@Test public void testGetRequest() {
		String req = "GET /foo HTTP/1.1\r\n";
		RequestLineParser parser = new RequestLineParser();
		RequestLine line = parser.parse(req);
		assertEquals(line.method(), Method.GET);
		assertEquals(line.uri(), URI.create("/foo"));
		assertEquals(line.version(), Version.HTTP_1_1);
	}
	
}
