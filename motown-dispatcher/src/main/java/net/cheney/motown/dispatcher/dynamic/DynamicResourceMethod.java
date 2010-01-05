package net.cheney.motown.dispatcher.dynamic;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import net.cheney.motown.api.Message;
import net.cheney.motown.api.Response;
import net.cheney.motown.dispatcher.ResourceMethod;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.log4j.Logger;

public class DynamicResourceMethod implements ResourceMethod {
	private static final Logger LOG = Logger.getLogger(DynamicResourceMethod.class);

	private final Method method;
	private final ParameterInjector[] paramInjectors;

	public DynamicResourceMethod(Method method) {
		this.method = method;
		this.paramInjectors = buildParamInjectors();
	}
	
	private ParameterInjector[] buildParamInjectors() {
		final Class<?>[] params = method.getParameterTypes();
		final ParameterInjector[] args = new ParameterInjector[params.length];
		final Annotation[][] paramAnnotations = method.getParameterAnnotations();
		for(int i = 0 ; i < params.length ; ++i) {
			for(Annotation a : paramAnnotations[i]) {
				if(a.annotationType().equals(Context.class)) {
					args[i] = new ContextAnnotationParameterInjector(params[i]);
				}
			}
		}
		return args;
	}

	public Response invoke(Object resource, Message request) {
		final Object args[] = injectParameters(request);
		try {
			LOG.debug(String.format("Invoking %s(%s)",method.getName(),Arrays.asList(args).toString()));
			return (Response) method.invoke(resource, args);
		} catch (IllegalArgumentException e) {
			LOG.error(e);
			return Response.serverErrorInternal();
		} catch (IllegalAccessException e) {
			LOG.error(e);
			return Response.serverErrorInternal();
		} catch (InvocationTargetException e) {
			LOG.error(String.format("Failure Invoking %s(%s)",method.getName(),Arrays.asList(args).toString()),e);
			return Response.serverErrorInternal();
		}
	}

	private final Object[] injectParameters(final Message request) {
		final Object[] args = new Object[paramInjectors.length];
		for(int i = 0 ; i < paramInjectors.length ; ++i) {
			args[i] = paramInjectors[i].injectParameter(request);
		}
		return args;
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
	}
}
