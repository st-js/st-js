package org.stjs.generator.exec.annotations;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;
import org.stjs.generator.GeneratorConfigurationBuilder;

public class AnnotationsTest extends AbstractStjsTest {
	@Test
	public void testAnnotationClass() {
		assertEquals(
				10.0,
				executeAndReturnNumber(Annotation1.class,
						new GeneratorConfigurationBuilder().annotations("MyAnnotations.WithMultipleValues").build()), 0);
	}

	@Test
	public void testAnnotationParam() {
		assertEquals(
				5.0,
				executeAndReturnNumber(Annotation5.class,
						new GeneratorConfigurationBuilder().annotations("MyAnnotations.WithMultipleValues").build()), 0);
	}
}
