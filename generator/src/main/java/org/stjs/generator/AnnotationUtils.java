package org.stjs.generator;

import com.sun.tools.javac.code.Symbol;
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
				return generateMethodName(methodSymbolElement);
			} else {
				return value;
			}
		}

		private static String generateMethodName(Symbol.MethodSymbol methodSymbolElement) {
			String methodName = methodSymbolElement.getSimpleName().toString();
			List<Symbol.VarSymbol> params = methodSymbolElement.getParameters();

			StringBuilder methodNameBuilder = new StringBuilder(methodName);
			if (!params.isEmpty()) {
				methodNameBuilder.append(AnnotationConstants.JS_OVERLOAD_NAME_DEFAULT_VALUE);
			}
			for (int i = 0; i < params.size(); i++) {
				Symbol.VarSymbol param = params.get(i);
				methodNameBuilder.append(param.type.tsym.getSimpleName());
				if (i < params.size() - 1) {
					methodNameBuilder.append(AnnotationConstants.JS_OVERLOAD_NAME_METHOD_PARAMS_SEPARATOR);
				}
			}
			return methodNameBuilder.toString();
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
