package net.cheney.motown.dispatcher.dynamic;

import net.cheney.motown.api.Request;
import net.cheney.motown.api.Depth;

public class DepthParameterInjector extends MethodParameterInjector {

	private final Depth defaultDepth;

	public DepthParameterInjector(Depth defaultDepth) {
		this.defaultDepth = defaultDepth;
	}
	
	@Override
	public Depth injectParameter(Request request) {
		return request.getDepth(defaultDepth);
	}

}
