package net.cheney.motown.server.router;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.cheney.motown.common.api.Response;
import net.cheney.motown.server.api.Application;
import net.cheney.motown.server.api.Environment;

public class RegexConstraint implements Application {

	private final Pattern pattern;
	private final Application app;

	public RegexConstraint(Pattern p, Application app) {
		this.pattern = p;
		this.app = app;
	}
	
	@Override
	public final Response call(Environment env) {
		Matcher matcher = pattern.matcher(env.pathInfo().toString());
		return matcher.matches() ? match(matcher, env) : Response.clientErrorNotFound();
	}

	public Response match(Matcher matcher, Environment env) {
		return app.call(env);
	}

}
