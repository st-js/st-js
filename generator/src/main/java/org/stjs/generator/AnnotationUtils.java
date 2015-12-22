package org.stjs.generator;

import org.stjs.generator.javac.ElementUtils;
import org.stjs.generator.javac.InternalUtils;
import org.stjs.javascript.annotation.AnnotationConstants;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

public class AnnotationUtils {

    /**
     * Annotation @JSOverloadName specific utils
     */
	public static class JSOverloadName {

		public static String getAnnotationValue(GenerationContext<?> context, ExecutableElement element) {
			String value = null;
			try {
				List<? extends VariableElement> params = element.getParameters();
				String defaultOverloadedName = InternalUtils.generateOverloadedMethodName(context, element.getSimpleName().toString(), params);

				Annotation annotation = element.getAnnotation(org.stjs.javascript.annotation.JSOverloadName.class);
				if (annotation == null) {
					Map<String, String> renamedMethodSignatures = context.getConfiguration().getRenamedMethodSignatures();

					String fullMethodSignature = ElementUtils.enclosingClass(element).getQualifiedName() + "." + defaultOverloadedName;
					String configOverloadedName = renamedMethodSignatures.get(fullMethodSignature);
					if (configOverloadedName != null) {
						value = configOverloadedName;
					}
				} else {
					value = (String) annotation.getClass().getMethod("value").invoke(annotation);
					if (AnnotationConstants.JS_OVERLOAD_NAME_DEFAULT_VALUE.equals(value)) {
						// generate method name with argument types
						value = defaultOverloadedName;
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
