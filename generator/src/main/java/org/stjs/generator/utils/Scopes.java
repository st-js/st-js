package org.stjs.generator.utils;

import javax.lang.model.element.Element;

import com.sun.source.tree.Scope;

public class Scopes {
	public static Element findElement(Scope currentScope, String name) {
		for (Element element : currentScope.getLocalElements()) {
			if (name.equals(element.getSimpleName().toString())) {
				return element;
			}
		}
		if (currentScope.getEnclosingScope() != null) {
			return findElement(currentScope.getEnclosingScope(), name);
		}
		return null;
	}
}
