package net.cheney.motown.dispatcher.dynamic;

import net.cheney.motown.server.api.Environment;

public class ContextAnnotationParameterInjector extends MethodParameterInjector {

	private final Class<?> paramType;

	public ContextAnnotationParameterInjector(Class<?> paramType) {
		this.paramType = paramType;
	}
	
	@Override
	public Object injectParameter(Environment env) {
		return null; // TODO
	}
}
