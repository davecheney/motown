package net.cheney.motown.mvn.dispatcher;

import net.cheney.motown.server.api.Environment;
import net.cheney.motown.uri.Path;

public class PathTranslatedParameterInjector extends MethodParameterInjector {

	@Override
	public Object injectParameter(Environment env) {
		return Path.fromString(env.uri().getPath());
	}

}
