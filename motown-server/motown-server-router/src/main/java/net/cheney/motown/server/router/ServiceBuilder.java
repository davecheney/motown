package net.cheney.motown.server.router;

public interface ServiceBuilder {

	ContextBuilder with(Class<?> clazz);
}
