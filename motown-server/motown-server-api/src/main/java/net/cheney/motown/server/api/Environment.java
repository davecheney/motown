package net.cheney.motown.server.api;

import net.cheney.motown.common.api.Request;

public abstract class Environment {

	public static Environment fromRequest(Request req) {
		return new Environment() {
			
		};
	}
	
}

