package net.cheney.motown.dispatcher.dynamic;

import net.cheney.motown.common.api.Request;

public class PathTranslatedParameterInjector extends MethodParameterInjector {

	@Override
	public Object injectParameter(Request request) {
		return Path.fromString(request.uri().getPath());
	}

}
