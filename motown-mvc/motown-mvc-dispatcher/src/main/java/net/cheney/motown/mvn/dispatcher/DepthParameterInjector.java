package net.cheney.motown.mvn.dispatcher;

import net.cheney.motown.common.api.Depth;
import net.cheney.motown.common.api.Header;
import net.cheney.motown.server.api.Environment;

public class DepthParameterInjector extends MethodParameterInjector {

	private final net.cheney.motown.common.api.Depth defaultDepth;

	public DepthParameterInjector(net.cheney.motown.common.api.Depth defaultDepth) {
		this.defaultDepth = defaultDepth;
	}
	
	@Override
	public net.cheney.motown.common.api.Depth injectParameter(Environment env) {
		String depth = env.header(Header.DEPTH).getOnlyElementWithDefault(defaultDepth.toString());
		return Depth.parse(depth, defaultDepth);
	}

}
