package net.cheney.motown.server.dispatcher.dynamic;

import java.net.URI;

import net.cheney.motown.common.api.Header;
import net.cheney.motown.server.api.Environment;

public class DestinationParameterInjector extends MethodParameterInjector {

	@Override
	public URI injectParameter(Environment env) {
		String destination = env.header(Header.DESTINATION).getOnlyElementWithDefault("");
		return URI.create(destination);
	}

}
