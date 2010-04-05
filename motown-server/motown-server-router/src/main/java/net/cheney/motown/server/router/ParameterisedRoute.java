package net.cheney.motown.server.router;

import java.util.List;

import com.google.common.collect.Lists;

import net.cheney.motown.server.api.Parameter;
import net.cheney.motown.server.api.Parameters;

public class ParameterisedRoute {

	private final List<Parameter<?>> params;

	public ParameterisedRoute(Parameter<?>... params) {
		this.params = Lists.newArrayList(params);
	}
	
	public Parameters convertArgsToParams(List<String> args) {
		if(args.size() != params.size()) {
			throw new IllegalArgumentException(String.format("Expected %d arguments, passed %d", params.size(), args.size()));
		}
		Parameters p = new Parameters();
		for(int i = 0 ; i < args.size() ; ++i) {
			// TODO unfortunate
			Parameter param = params.get(i);
			p.put(param, param.decode(args.get(i)));
		}
		return p;
	}
}
