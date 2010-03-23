package net.cheney.motown.dispatcher.dynamic;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.cheney.motown.common.api.MimeType;

@Inherited
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Consumes {

	MimeType[] value() default MimeType.ANY_ANY;    
}
