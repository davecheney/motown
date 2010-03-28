package net.cheney.motown.mvn.dispatcher;

import javax.annotation.Nonnull;

import net.cheney.motown.server.api.Environment;

public class FragmentParameterInjector extends MethodParameterInjector {

	@Override
	public String injectParameter(@Nonnull Environment env) {
		return env.uri().getFragment();
	}

}
