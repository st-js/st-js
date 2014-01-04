package org.stjs.javascript.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation added on a method changes how the call to this method is generated. This is used when building
 * bridges if the regular Java-to-JavaScript translation is not possible. For example in Java you can only use brackets
 * ([]) with arrays, but in JavaScript you may also use the to access objects (maps) properties.
 * 
 * The templates are added via generation plugins {@link org.stjs.generator.plugin.STJSGenerationPlugin}. You may add
 * you own by creating a plugin and use it with you class. See {@link UsePlugin}.
 * 
 * @author acraciun
 * 
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Template {
	String value();
}
