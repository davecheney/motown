package net.cheney.motown.server.router;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import net.cheney.motown.server.middleware.Middleware;

public class Builder {

	private List<Middleware> middlewares = newArrayList();

	public Builder use(Middleware middleware) {
		this.middlewares.add(middleware);
		return this;
	}

	public void use(Class<? extends Middleware> klazz) {
		klazz.n
	}
}
