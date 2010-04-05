package net.cheney.motown.mvn.dispatcher;

import net.cheney.motown.server.api.Environment;

public class PathTranslatedParameterInjector extends MethodParameterInjector {

	@Override
	public Object injectParameter(Environment env) {
		return env.pathInfo();
	}

}
