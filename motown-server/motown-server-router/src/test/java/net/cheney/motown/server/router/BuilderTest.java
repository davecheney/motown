package net.cheney.motown.server.router;

import java.io.OutputStreamWriter;

import net.cheney.motown.server.middleware.CommonLogger;

import org.junit.Test;

public class BuilderTest {

	@Test public void testBuilderWithUse() {
		Builder builder = new Builder();
		builder.use(new CommonLogger(null, new OutputStreamWriter(System.out)));
		builder.run(new )
	}
}
