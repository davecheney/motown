package net.cheney.motown.api;

import static net.cheney.motown.api.Header.VIA;
import static org.junit.Assert.assertEquals;

import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

public class HeaderAccessorTestCase {

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
