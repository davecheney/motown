package net.cheney.motown.dispatcher.dynamic;

import net.cheney.motown.api.Request;

public class ContextAnnotationParameterInjector extends MethodParameterInjector {

	private final Class<?> paramType;

	public ContextAnnotationParameterInjector(Class<?> paramType) {
		this.paramType = paramType;
	}
	
	public Object injectParameter(Request request) {
		if(paramType.equals(Request.class)) {
			return request;
		} else {
			return null;
		}
	}
}
