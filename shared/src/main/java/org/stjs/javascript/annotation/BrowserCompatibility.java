package org.stjs.javascript.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>BrowserCompatibility class.</p>
 *
 * @author acraciun
 * @version $Id: $Id
 */
@Target({ ElementType.PACKAGE, ElementType.TYPE, ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface BrowserCompatibility {

	String value();
}
