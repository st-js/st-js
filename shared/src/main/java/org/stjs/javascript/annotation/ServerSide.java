package org.stjs.javascript.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to annotated elements of a class (methods, fields, constructors) that are intented to be used on the server side only.
 * This allows you to add supplementary functionality for the server side in the shared classes. The ST-JS generator will detect if you try to
 * use such code in the client code. Also it will not generate the corresponding JavaScript code, allowing you to use classes or constructions
 * normally forbidden for the client side.
 * @author acraciun
 */
@Target({ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ServerSide {

}
