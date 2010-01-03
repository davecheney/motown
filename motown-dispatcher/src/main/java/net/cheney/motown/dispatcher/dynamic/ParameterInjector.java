package net.cheney.motown.dispatcher.dynamic;

import net.cheney.motown.api.Message;

public interface ParameterInjector {

	public Object injectParameter(Message request);
}
