package net.cheney.motown.server.router;

import static junit.framework.Assert.assertEquals;
import net.cheney.motown.server.api.NamedIntegerParameter;
import net.cheney.motown.server.api.NamedStringParameter;
import net.cheney.motown.server.api.Parameters;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.google.common.collect.Lists;

public class ParameterisedRouteTest {

	static final NamedStringParameter FOO = new NamedStringParameter("foo"), BAZ = new NamedStringParameter("baz");
	static final NamedIntegerParameter BAR = new NamedIntegerParameter("bar");
	
	@Test public void testParamRoute() {
		ParameterisedRoute route = new ParameterisedRoute(FOO, BAR, BAZ);
		Parameters p = route.convertArgsToParams(Lists.newArrayList(StringUtils.split("spum/54/frogger", "/")));
		assertEquals(p.get(FOO), "spum");
		assertEquals(p.get(BAR), (Integer)54);
		assertEquals(p.get(BAZ), "frogger");
	}
}
