package net.cheney.motown.server.middleware;

import net.cheney.motown.server.api.Application;

public abstract class Middleware implements Application {

	/**
	 * Bind this middleware to a new Application instance
	 * @param app The application to bind to
	 * @return An instance of this class bound to the @Application passed
	 */
	public abstract Middleware bind(Application app);
	
}
