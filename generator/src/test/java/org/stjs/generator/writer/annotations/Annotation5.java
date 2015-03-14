package org.stjs.generator.writer.annotations;

public class Annotation5 {
	public int method(int p1, @MyAnnotations.WithMultipleValues(n = 2 + 3) int p2) {
		return 0;
	}
}
