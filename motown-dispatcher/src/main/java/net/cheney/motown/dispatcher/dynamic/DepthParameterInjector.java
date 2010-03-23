package net.cheney.motown.dispatcher.dynamic;

import net.cheney.motown.common.api.Request;

public class DepthParameterInjector extends MethodParameterInjector {

	private final net.cheney.motown.common.api.Depth defaultDepth;

	public DepthParameterInjector(net.cheney.motown.common.api.Depth defaultDepth) {
		this.defaultDepth = defaultDepth;
	}
	
	@Override
	public net.cheney.motown.common.api.Depth injectParameter(Request request) {
		return request.getDepth(defaultDepth);
	}

}
