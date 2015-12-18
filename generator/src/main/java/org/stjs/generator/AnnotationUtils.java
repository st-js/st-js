package org.stjs.generator;

import org.stjs.generator.javac.InternalUtils;
import org.stjs.javascript.annotation.AnnotationConstants;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class AnnotationUtils {

    /**
     * Annotation @JSOverloadName specific utils
     */
	public static class JSOverloadName {

		public static String getAnnotationValue(GenerationContext<?> context, ExecutableElement element) {
			String value = null;
			try {
				Annotation annotation = element.getAnnotation(org.stjs.javascript.annotation.JSOverloadName.class);
				if (annotation != null) {
					value = (String) annotation.getClass().getMethod("value").invoke(annotation);

					if (AnnotationConstants.JS_OVERLOAD_NAME_DEFAULT_VALUE.equals(value)) {
						// generate method name with argument types
						List<? extends VariableElement> params = element.getParameters();
						value = InternalUtils.generateOverloadedMethodName(context, element.getSimpleName().toString(), params);
					}
				}

				return value;
			}
			catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
				throw new STJSRuntimeException(e);
			}
		}
	}
}
