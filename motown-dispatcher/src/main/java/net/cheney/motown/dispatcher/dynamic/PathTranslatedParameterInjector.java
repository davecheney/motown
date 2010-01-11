package net.cheney.motown.dispatcher.dynamic;

import net.cheney.motown.api.Request;
import net.cheney.motown.uri.Path;

public class PathTranslatedParameterInjector extends MethodParameterInjector {

	@Override
	public Object injectParameter(Request request) {
		return Path.fromString(request.uri().getPath());
	}

}
