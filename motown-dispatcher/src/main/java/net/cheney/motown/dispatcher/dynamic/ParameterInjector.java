package net.cheney.motown.dispatcher.dynamic;

import net.cheney.motown.api.Message;

public interface ParameterInjector<V extends Message> {

	public Object injectParameter(V request);
}
