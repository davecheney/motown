package net.cheney.motown.uri;

import net.cheney.motown.uri.URI;

import org.junit.Test;

//@RunWith(Parameterized.class)
public class URITest {

//	@Parameters public static 
	
	@Test public void testURI() {
		URI.parse("http://www.ics.uci.edu/pub/ietf/uri/#Related");
		URI.parse("ftp://cnn.example.com&story=breaking_news@10.0.0.1/top_story.htm");
	}
	
	
}
