package net.cheney.motown.server.router;

import net.cheney.motown.server.api.Path;

public interface RouteBuilder {

	/**
	 * Close builder and return a {@link Router} 
	 * @return
	 */
	Router build();
	
	ContextBuilder context(Path path);
	
}
