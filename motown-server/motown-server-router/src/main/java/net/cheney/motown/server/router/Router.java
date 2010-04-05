package net.cheney.motown.server.router;

import static net.cheney.motown.common.api.Response.clientErrorNotFound;
import net.cheney.motown.common.api.Response;
import net.cheney.motown.server.api.Application;
import net.cheney.motown.server.api.Environment;

public class Router implements Application {

	private final RouteSet routes = new RouteSet();
	
	@Override
	public Response call(Environment env) {
		Response resp = clientErrorNotFound();
		for(Router.Route r : routes) {
			resp = r.call(env);
			resp.status().isClientError() ? break : continue;
		}
	}
	
	static abstract class Route implements Application {
		
		
	}

}
