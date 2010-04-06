package net.cheney.motown.mvn.controller;

import static net.cheney.motown.common.api.Response.clientErrorNotFound;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.cheney.motown.common.api.Response;
import net.cheney.motown.mvn.dispatcher.action.CrudAction;
import net.cheney.motown.mvn.dispatcher.action.CrudAction.Action;
import net.cheney.motown.server.api.Application;
import net.cheney.motown.server.api.Environment;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

public abstract class Controller {

	public Application action(CrudAction.Action action) {
		Map<Action, java.lang.reflect.Method> actionMap = findActionMethods();
		return actionMap.containsKey(action) ? createActionApplication(actionMap.get(action)) : new Application() {
			
			@Override
			public Response call(Environment env) {
				return clientErrorNotFound();
			}
		};
	}

	private Application createActionApplication(final java.lang.reflect.Method method) {
		final ArgumentInjector injector = createArgumentInjector(method);
		// there is a way to get the outer class of an anon inner class, but I don't know it ATM
		final Controller controller = this;
		return new Application() {

			@Override
			public Response call(Environment env) {
				Object[] args = injector.inject(env);
				return invoke(args);
			}

			private Response invoke(Object[] args) {
				try {
					return (Response) method.invoke(controller, args);
				} catch (IllegalArgumentException e) {
					return Response.serverErrorInternal();
				} catch (IllegalAccessException e) {
					return Response.serverErrorInternal();
				} catch (InvocationTargetException e) {
					return Response.serverErrorInternal();
				}
			}	
			
		};
	}

	private ArgumentInjector createArgumentInjector(
			java.lang.reflect.Method method) {
		// TODO Auto-generated method stub
		return null;
	}

	private Map<Action,java.lang.reflect.Method> findActionMethods() {
		final HashMap<CrudAction.Action, java.lang.reflect.Method> r = new HashMap<CrudAction.Action, java.lang.reflect.Method>();
			for(final java.lang.reflect.Method m : findMethodsWithMetaAnnotation(this.getClass(), CrudAction.class)) {
				for(final Annotation a : m.getAnnotations()) {
					final Action action = a.annotationType().getAnnotation(CrudAction.class).value();
					r.put(action, m);
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
		
}
