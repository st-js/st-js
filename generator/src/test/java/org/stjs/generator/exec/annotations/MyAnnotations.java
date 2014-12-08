package org.stjs.generator.exec.annotations;

public class MyAnnotations {
	public @interface WithMultipleValues {
		int n() default 0;

		String m() default "";
	}

	public @interface WithSingleValue {
		int value() default 0;
	}

	public @interface WithArrayValue {
		String[] value();
	}
}
