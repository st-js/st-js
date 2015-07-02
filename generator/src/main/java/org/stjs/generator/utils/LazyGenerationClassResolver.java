package org.stjs.generator.utils;

import org.stjs.generator.ClassWithJavascript;
import org.stjs.generator.DefaultClassResolver;

public class LazyGenerationClassResolver extends DefaultClassResolver {

	private final LazyGenerator generator;

	public LazyGenerationClassResolver(ClassLoader builtProjectClassLoader, LazyGenerator generator) {
		super(builtProjectClassLoader);
		this.generator = generator;
	}

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
