package org.stjs.generator.utils;

import org.stjs.generator.ClassWithJavascript;
import org.stjs.generator.DefaultClassResolver;

/**
 * <p>LazyGenerationClassResolver class.</p>
 *
 * @author acraciun
 * @version $Id: $Id
 */
public class LazyGenerationClassResolver extends DefaultClassResolver {

	private final LazyGenerator generator;

	/**
	 * <p>Constructor for LazyGenerationClassResolver.</p>
	 *
	 * @param builtProjectClassLoader a {@link java.lang.ClassLoader} object.
	 * @param generator a {@link org.stjs.generator.utils.LazyGenerationClassResolver.LazyGenerator} object.
	 */
	public LazyGenerationClassResolver(ClassLoader builtProjectClassLoader, LazyGenerator generator) {
		super(builtProjectClassLoader);
		this.generator = generator;
	}

	/** {@inheritDoc} */
	@Override
	protected ClassWithJavascript doResolve(String className) {
		ClassWithJavascript stjsClass = super.doResolve(className);

		if (stjsClass.getJavascriptFiles().isEmpty()) {
			String parentClassName = getParentClassName(className);
			stjsClass = generator.generateJavaScript(parentClassName);
		}
		return stjsClass;
	}

	public interface LazyGenerator {
		ClassWithJavascript generateJavaScript(String className);
	}
}
