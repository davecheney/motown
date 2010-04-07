package net.cheney.motown.server.router;

import java.util.regex.Pattern;

import net.cheney.motown.common.api.Response;
import net.cheney.motown.server.api.Application;
import net.cheney.motown.server.api.Environment;
import net.cheney.motown.server.api.Parameter;
import net.cheney.motown.server.api.Path;

public class Router implements Application {

	private Router() {
		// TODO Auto-generated constructor stub
	}
	
	public static RootContextBuilder builder() {
		return new Builder();
	}

	public static class Builder implements RootContextBuilder, ServiceBuilder, ParameterisedServiceBuilder {

		@Override
		public Router build() {
			// TODO
			return new Router();
		}
		
		@Override
		public ContextBuilder rootContent() {
			return this;
		}
		
		@Override
		public ContextBuilder context(Path path) {
			return this;
		}
		
		@Override
		public ServiceBuilder serve(Path path) {
			return this;
		}

		@Override
		public ContextBuilder with(Class<?> clazz) {
			return this;
		}

		@Override
		public ParameterisedServiceBuilder serve(Pattern pattern) {
			return this;
		}

		@Override
		public ContextBuilder with(Class<?> clazz, Parameter<?>... params) {
			return this;
		}

		@Override
		public RootContextBuilder done() {
			return this;
		}
	}

	@Override
	public Response call(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}
}
