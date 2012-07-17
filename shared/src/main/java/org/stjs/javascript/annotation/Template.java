package org.stjs.javascript.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation added on a method changes how the call to this method is generated. The only template supported for
 * the moment is "none", that leaves the method call as-is, i.e. if it's a special method (object.$get(i) for example
 * will generate object.$get(i) instead of object[i]). The next major version will list more templates.
 * 
 * @author acraciun
 * 
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Template {
	String value();
}
