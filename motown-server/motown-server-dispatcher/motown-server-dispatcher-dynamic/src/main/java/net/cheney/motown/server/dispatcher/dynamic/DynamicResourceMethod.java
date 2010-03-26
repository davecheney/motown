package net.cheney.motown.server.dispatcher.dynamic;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import net.cheney.motown.common.api.Response;
import net.cheney.motown.server.api.Environment;
import net.cheney.motown.server.dispatcher.ResourceMethod;

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
				if(a.annotationType().equals(PathTranslated.class)) {
					args[i] = new PathTranslatedParameterInjector();
				}
				if(a.annotationType().equals(Fragment.class)) {
					args[i] = new FragmentParameterInjector();
				}
				if(a.annotationType().equals(Depth.class)) {
					args[i] = new DepthParameterInjector(((Depth)a).value());
				}
			}
		}
		return args;
	}

	@Override
	public Response invoke(Object resource, Environment env) {
		LOG.info(String.format("%s %s %s %s", env.method(), env.uri(), env.version(), env.headers()));
		Response response = invoke0(resource, env);
		LOG.info(String.format("%s %s %s %s", response.version(), response.status().code(), response.status().reason(), response.headers()));
		return response;
	}

	private Response invoke0(Object resource, Environment env) {
		final Object args[] = injectParameters(env);
		try {
			LOG.debug(String.format("Invoking %s(%s)",method.getName(),Arrays.asList(args).toString()));
			return (Response) method.invoke(resource, args);
		} catch (IllegalArgumentException e) {
			LOG.error(String.format("Failure Invoking %s(%s)",method.getName(),Arrays.asList(args).toString()),e);
			return Response.serverErrorInternal();
		} catch (IllegalAccessException e) {
			LOG.error(String.format("Failure Invoking %s(%s)",method.getName(),Arrays.asList(args).toString()),e);
			return Response.serverErrorInternal();
		} catch (InvocationTargetException e) {
			LOG.error(String.format("Failure Invoking %s(%s)",method.getName(),Arrays.asList(args).toString()),e);
			return Response.serverErrorInternal();
		}		
	}

	private final Object[] injectParameters(final Environment env) {
		final Object[] args = new Object[paramInjectors.length];
		for(int i = 0 ; i < paramInjectors.length ; ++i) {
			args[i] = paramInjectors[i].injectParameter(env);
		}
		return args;
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
	}
}
