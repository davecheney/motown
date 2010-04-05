package net.cheney.motown.server.api;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

public class ParameterTest {

	Parameter<String> foo = new NamedStringParameter("foo");

	Parameter<Integer> bar = new NamedIntegerParameter("bar");

	@Test
	public void testValidDecode() {
		assertEquals("Hello world!", foo.decode("Hello world!"));
		assertEquals((Integer) 1331, bar.decode("1331"));
	}

	@Test(expected = NumberFormatException.class)
	public void testInvalidDecode() {
		assertEquals((Integer) 1331, bar.decode("spum"));
	}
	
	@Test public void testValidEncode() {
		assertEquals("Hello world!", foo.encode("Hello world!"));
		assertEquals("1331", bar.encode(1331));
	}
}
