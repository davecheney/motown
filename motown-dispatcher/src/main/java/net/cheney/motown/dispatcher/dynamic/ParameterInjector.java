package net.cheney.motown.dispatcher.dynamic;

import net.cheney.motown.server.api.Environment;

public interface ParameterInjector {

	public Object injectParameter(Environment env);
}
