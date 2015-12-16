package org.stjs.generator;

import com.sun.tools.javac.code.Symbol;
import org.stjs.generator.javac.InternalUtils;
import org.stjs.javascript.annotation.AnnotationConstants;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class AnnotationUtils {
    /**
     * Annotation @JSOverloadName specific utils
     */
	public static class JSOverloadName {
		public static boolean isPresent(Symbol.MethodSymbol methodSymbolElement) {
			return methodSymbolElement.getAnnotation(org.stjs.javascript.annotation.JSOverloadName.class) != null;
		}

		public static String decorate(Symbol.MethodSymbol methodSymbolElement) {
			String value = getAnnotationValue(methodSymbolElement);

			if (value == null || AnnotationConstants.JS_OVERLOAD_NAME_DEFAULT_VALUE.equals(value)) {
				String methodName = methodSymbolElement.getSimpleName().toString();
				List<Symbol.VarSymbol> params = methodSymbolElement.getParameters();

				return InternalUtils.generateOverloadedMethodName(methodName, params);
			} else {
				return value;
			}
		}

		private static String getAnnotationValue(Element element) {
			try {
				Annotation annotation = element.getAnnotation(org.stjs.javascript.annotation.JSOverloadName.class);
				if (annotation != null) {
					return (String) annotation.getClass().getMethod("value").invoke(annotation);
				}
				return null;
			}
			catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
				throw new STJSRuntimeException(e);
			}
		}
	}
}
