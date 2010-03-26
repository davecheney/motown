package net.cheney.motown.server.dispatcher.dynamic;

import net.cheney.motown.server.api.Environment;

public class DepthParameterInjector extends MethodParameterInjector {

	private final net.cheney.motown.common.api.Depth defaultDepth;

	public DepthParameterInjector(net.cheney.motown.common.api.Depth defaultDepth) {
		this.defaultDepth = defaultDepth;
	}
	
	@Override
	public net.cheney.motown.common.api.Depth injectParameter(Environment env) {
		return env.getDepth(defaultDepth);
	}

}
