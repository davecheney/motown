package net.cheney.motown.mvn.dispatcher;

import net.cheney.motown.server.api.Environment;

public interface ParameterInjector {

	public Object injectParameter(Environment env);
}
