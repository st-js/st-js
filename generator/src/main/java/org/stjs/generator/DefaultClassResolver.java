package org.stjs.generator;

import java.util.HashMap;
import java.util.Map;

import org.stjs.generator.utils.ClassUtils;

/**
 * this class lazily generates the dependencies
 *
 * @author acraciun
 * @version $Id: $Id
 */
public class DefaultClassResolver implements ClassResolver {

	private final ClassLoader classLoader;
	private final Map<String, ClassWithJavascript> cache = new HashMap<>();

	/**
	 * <p>Constructor for DefaultClassResolver.</p>
	 *
	 * @param classLoader a {@link java.lang.ClassLoader} object.
	 */
	public DefaultClassResolver(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	/** {@inheritDoc} */
	@Override
	public ClassWithJavascript resolve(String className) {
		ClassWithJavascript clazz = this.cache.get(className);
		if (clazz == null) {
			clazz = doResolve(className);
			if (clazz != null) {
				cache.put(className, clazz);
			}
		}
		return clazz;
	}

	/** {@inheritDoc} */
	public Class<?> resolveJavaClass(String className) {
		try {
			return classLoader.loadClass(className);
		}
		catch (ClassNotFoundException e) {
			throw new STJSRuntimeException(e);
		}
	}

	/**
	 * <p>getParentClassName.</p>
	 *
	 * @param className a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected String getParentClassName(String className) {
		String parentClassName = className;
		int pos = parentClassName.indexOf('$');
		if (pos > 0 && parentClassName.charAt(pos - 1) != '.') {
			// avoid classes like angularjs.$Timeout
			parentClassName = parentClassName.substring(0, pos);
		}
		return parentClassName;
	}

	/**
	 * <p>doResolve.</p>
	 *
	 * @param className a {@link java.lang.String} object.
	 * @return a {@link org.stjs.generator.ClassWithJavascript} object.
	 */
	protected ClassWithJavascript doResolve(String className) {
		String parentClassName = getParentClassName(className);
		// try first if to see if it's a bridge class
		Class<?> clazz = resolveJavaClass(parentClassName);

		if (ClassUtils.isBridge(classLoader, clazz)) {
			return new BridgeClass(this, clazz);
		}

		return new STJSClass(this, classLoader, clazz);
	}
}
