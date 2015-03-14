package org.stjs.generator.writer.types;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE, ElementType.PACKAGE })
@Retention(RetentionPolicy.RUNTIME)
public @interface TestAnnotation {//
	String[] value() default "";
}
