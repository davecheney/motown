package net.cheney.motown.server.api;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

public class ParameterTest {

	Parameter<String> foo = new NamedParameter<String>(String.class) {

		@Override
		public String name() {
			return "foo";
		}

		@Override
		public String decode(String string) {
			return string;
		}
		
		@Override
		public String encode(String value) {
			return value.toString();
		}
	};

	NamedParameter<Integer> bar = new NamedParameter<Integer>(Integer.class) {

		@Override
		public String name() {
			return "bar";
		}

		@Override
		public Integer decode(String string) {
			return Integer.parseInt(string);
		}
		
		@Override
		public String encode(Integer value) {
			return value.toString();
		}
	};

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
