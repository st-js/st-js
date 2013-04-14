package org.stjs.javascript.annotation;

public @interface Compatibility {

	public enum Browser {
		MSIE, FIREFOX, CHROME, SAFARI, OPERA
	}

	Browser browser();

	double from();

	double to();
}
