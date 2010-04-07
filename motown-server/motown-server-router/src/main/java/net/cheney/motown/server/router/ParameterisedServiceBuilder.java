package net.cheney.motown.server.router;

import net.cheney.motown.server.api.Parameter;

public interface ParameterisedServiceBuilder {

	ContextBuilder with(Class<?> clazz, Parameter<?>... params);
}
