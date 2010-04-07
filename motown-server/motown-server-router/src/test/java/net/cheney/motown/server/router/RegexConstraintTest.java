package net.cheney.motown.server.router;

import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.Assert;

import net.cheney.motown.common.api.Request;
import net.cheney.motown.common.api.Response;
import net.cheney.motown.common.api.Status;
import net.cheney.motown.common.api.Version;
import net.cheney.motown.common.api.Request.Method;
import net.cheney.motown.server.api.Application;
import net.cheney.motown.server.api.Environment;

import org.junit.Test;

public class RegexConstraintTest {

	@Test public void testRegex1() throws URISyntaxException {
		Application app = new Application() {
			
			@Override
			public Response call(Environment env) {
				return Response.success("hello world!");
			}
		};
		
		Pattern p = Pattern.compile("/archive/(\\d+)/$");
		app = new RegexConstraint(p, app) {
			@Override
			public Response match(Matcher matcher, Environment env) {
				String year = matcher.group();
				Assert.assertEquals("2004", year);
				return super.match(matcher, env);
			}
		};
		
		Request req = new Request(Method.GET, "/archive/2004/", Version.HTTP_1_1);
		Response r = app.call(Environment.fromRequest(req));
//		Assert.assertEquals(Status.SUCCESS_OK, r.status());
		
	}
}
