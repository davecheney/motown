package net.cheney.motown.server.dispatcher.dynamic;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.cheney.motown.common.api.Response;
import net.cheney.motown.common.api.Request.Method;
import net.cheney.motown.mvn.dispatcher.ResourceFactory;
import net.cheney.motown.mvn.dispatcher.ResourceMethod;
import net.cheney.motown.server.api.Application;
import net.cheney.motown.server.api.Environment;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

public class DynamicResourceHandler implements Application {

	private final ResourceFactory factory;
	private final Map<Method, ResourceMethod> resourceMethods;
	
	public DynamicResourceHandler(ResourceFactory factory) {
		this.factory = factory;
		this.resourceMethods = buildResourceMethods();
	}
	
	@Override
	public Response call(Environment env) {
		final ResourceMethod resourceMethod = resourceMethods.get(env.method());
		return resourceMethod == null ? Response.serverErrorNotImplemented() : resourceMethod.invoke(factory.resource(), env);
	}

	private Map<Method, ResourceMethod> buildResourceMethods() {
		final HashMap<Method, ResourceMethod> r = new HashMap<Method, ResourceMethod>();
		for(final java.lang.reflect.Method m : findMethodsWithMetaAnnotation(factory.resourceClass(), HttpMethod.class)) {
			for(final Annotation a : m.getAnnotations()) {
				final Method httpMethod = a.annotationType().getAnnotation(HttpMethod.class).value();
				r.put(httpMethod, new DynamicResourceMethod(m));
			}
		}
		return r;
	}

	private <T extends Annotation> Iterable<java.lang.reflect.Method> findMethodsWithMetaAnnotation(final Class<?> clazz, final Class<T> metaAnnotation) {
		return Iterables.filter(Arrays.asList(clazz.getMethods()), new Predicate<java.lang.reflect.Method>() {

			@Override
			public boolean apply(java.lang.reflect.Method method) {
				return Iterables.any(Arrays.asList(method.getDeclaredAnnotations()), new Predicate<Annotation>() {

					@Override
					public boolean apply(Annotation a) {
						return a.annotationType().isAnnotationPresent(metaAnnotation);
					}
				});
			}
			
		});
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
