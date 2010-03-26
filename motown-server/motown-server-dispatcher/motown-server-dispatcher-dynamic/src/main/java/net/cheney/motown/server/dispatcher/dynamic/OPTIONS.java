package net.cheney.motown.server.dispatcher.dynamic;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.cheney.motown.common.api.Message.Method;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@HttpMethod(Method.OPTIONS)
public @interface OPTIONS {

}
