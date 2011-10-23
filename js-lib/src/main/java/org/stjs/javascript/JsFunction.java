package org.stjs.javascript;

import org.stjs.shared.Array;

public interface JsFunction<T> {

	T $invoke(Object... args);

	T call(Object receiver, Object... args);

	T apply(Object receiver, Array<?> args);
}
