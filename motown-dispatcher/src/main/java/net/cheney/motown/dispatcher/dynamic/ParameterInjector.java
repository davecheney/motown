package net.cheney.motown.dispatcher.dynamic;

import net.cheney.motown.api.Request;

public interface ParameterInjector {

	public Object injectParameter(Request request);
}
