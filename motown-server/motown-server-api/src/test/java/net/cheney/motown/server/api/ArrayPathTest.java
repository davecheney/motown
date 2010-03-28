package net.cheney.motown.server.api;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;

import net.cheney.motown.server.api.ArrayPath;
import net.cheney.motown.server.api.Path;

import org.junit.Test;

public class ArrayPathTest {

	@Test public void testArrayPath() {
		assertEquals(new ArrayPath(asList("principals", "users")), Path.fromString("/principals/users"));
		assertEquals("principals/users",  Path.fromString("/principals/users").toString());
	}
}
