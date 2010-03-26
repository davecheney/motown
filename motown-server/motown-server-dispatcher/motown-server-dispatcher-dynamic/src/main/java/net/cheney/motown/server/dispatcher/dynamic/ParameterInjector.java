package net.cheney.motown.server.dispatcher.dynamic;

import net.cheney.motown.server.api.Environment;

public interface ParameterInjector {

	public Object injectParameter(Environment env);
}
