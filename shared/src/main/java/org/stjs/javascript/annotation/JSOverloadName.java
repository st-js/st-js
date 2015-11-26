package org.stjs.javascript.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation added on a method changes how the name will be generated. This is used when having multiple overload of the
 * same method to provide clear and concise name based on the given parameters.
 *
 * @author boubalou
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JSOverloadName {
    String value() default AnnotationConstants.JS_OVERLOAD_NAME_DEFAULT_VALUE;
}
