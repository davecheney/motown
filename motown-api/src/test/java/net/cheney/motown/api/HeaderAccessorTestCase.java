package net.cheney.motown.api;

import static net.cheney.motown.api.Header.HOST;
import static net.cheney.motown.api.Header.VIA;
import static org.junit.Assert.assertEquals;

import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.List;

import net.cheney.motown.protocol.http.common.RequestParser;

import org.junit.Test;

import com.google.common.collect.Lists;

public class HeaderAccessorTestCase {

	@Test
	public void testRequestHeaderAccessor() {
		String s = "PROPFIND /Software%20Update%20Enabler.app HTTP/1.1\r\nHost: localhost:8080\r\nUser-Agent: WebDAVFS/1.8 (01808000) Darwin/10.2.0 (i386)\r\nAccept: */*\r\nContent-Type: text/xml\r\nDepth: 0\r\nContent-Length: 161\r\nConnection: keep-alive\r\n\r\n";
		Request request = new RequestParser().parse(ByteBuffer.wrap(s.getBytes()));
		
		assertEquals(request.header(HOST).getOnlyElement(), "localhost:8080");
	}
	
	@Test 
	public void testHeaderSet() throws URISyntaxException {
		Request r = new Request(Method.GET, "/", Version.HTTP_1_1);
		r.headers().get(Header.VIA).addAll(Lists.newArrayList("foo", "bar"));
		
		assertEquals(r.header(VIA), Lists.newArrayList("foo", "bar"));
		
		r.header(VIA).set("baz");
		
		assertEquals(r.header(VIA).getOnlyElement(), "baz");
	}
	
	@Test 
	public void testHeaderAccessorIterator() throws URISyntaxException {
		Request r = new Request(Method.GET, "/", Version.HTTP_1_1);
		List<String> l =  Lists.newArrayList("foo", "bar", "baz");
		
		r.headers().get(Header.VIA).addAll(l);
		
		for(Iterator<String> expected = l.iterator(), actual = r.header(VIA).iterator() ; expected.hasNext() ; ) {
			assertEquals(expected.next(), actual.next());
		}
		
		// TODO, should check that expected and actual !hasNext()
	}
}
