package org.stjs.javascript.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this interface to activate some generation plugins for your type. The plugins that are activated this way have to
 * be listed in the META-INF/stjs.plugins. The format of this file is the following:
 * 
 * <pre>
 * java.version=1.8
 * template_name1=template_class1
 * template_name2=template_class2
 * ...
 * </pre>
 * 
 * The java.version entry is optional and represents the minimum version of the JVM for which your plugin is activated.
 * For example your plugin can only deal with LamdbaExpression in which case you put 1.8 as this feature only appeared
 * in Java 8. If you don't put it, Java 6 is assumed (1.6).
 * 
 * The plugins that are activated this way, have to return false from the method {STJSGenerationPlugin.loadByDefault}.
 * 
 * The artifact containing your plugin(s) must be in the dependency of the STJS maven plugin in your pom, so that the
 * stjs.plugins file could be found at runtime.
 * 
 * 
 * @author acraciun
 * 
 */
@Target({ ElementType.TYPE, ElementType.PACKAGE })
@Retention(RetentionPolicy.RUNTIME)
public @interface UsePlugin {
	String[] value();
}
