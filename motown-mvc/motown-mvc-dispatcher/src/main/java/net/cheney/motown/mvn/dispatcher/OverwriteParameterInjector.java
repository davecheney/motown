package net.cheney.motown.mvn.dispatcher;

import net.cheney.motown.common.api.Header;
import net.cheney.motown.server.api.Environment;

public class OverwriteParameterInjector extends MethodParameterInjector {

	@Override
	public Boolean injectParameter(Environment env) {
		String overwrite = env.header(Header.OVERWRITE).getOnlyElementWithDefault("T");
		return overwrite.equals("T");
	}

}
