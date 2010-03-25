package net.cheney.motown.server.api;

import javax.annotation.Nonnull;

import net.cheney.motown.common.api.Response;

public interface Application {

	@Nonnull Response call(@Nonnull Environment env);
}
