package net.cheney.motown.dispatcher.dynamic;

import net.cheney.motown.common.api.Request;

public class FragmentParameterInjector extends MethodParameterInjector {

	@Override
	public String injectParameter(Request request) {
		return request.uri().getFragment();
	}

}
