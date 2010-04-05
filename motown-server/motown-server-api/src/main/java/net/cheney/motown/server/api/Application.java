package net.cheney.motown.server.api;

import javax.annotation.Nonnull;

import net.cheney.motown.common.api.Response;

public interface Application {

	/**
	 * 
	 * @param env The environment for this request
	 * @return a @Response object, this cannot be null
	 */
	@Nonnull Response call(@Nonnull Environment env);
}
