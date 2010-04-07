package net.cheney.motown.server.router;

import java.util.regex.Pattern;

import net.cheney.motown.server.api.Path;

public interface ContextBuilder extends RouteBuilder {

	ServiceBuilder serve(Path path);
	
	ParameterisedServiceBuilder serve(Pattern pattern);

	RootContextBuilder done();
}
