package org.stjs.generator.writer.generics;

import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;
import org.stjs.javascript.Array;

/**
 * (c) Swissquote 19.04.18
 *
 * @author sgoetz
 */
public class GenericsTest extends AbstractStjsTest {

	@Test
	public void testInterfaceGenerics() {
		String source = generate(Indicator.class);

		assertCodeContains(source, "getSeries(): Array<S>;");
		assertCodeContains(source, "getSignal(mainSerie: M, index: number): number;");
		assertCodeContains(source, "getSignals(): S;");
		assertCodeContains(source, "interface Indicator<M extends Serie & FinancialSerie, S extends Serie & IndicatorSerie<M>> {");
	}

	@Test
	public void testComplexGenerics() {
		String source = generate(DefaultIndicator.class);

		assertCodeContains(source, "series: Array<S> = null;");
		assertCodeContains(source, "signals: S = null;");
		assertCodeContains(source, "private isDifferentReference(computedSeries: Array<S>, mainSerie: M): boolean {");
		assertCodeContains(source, "abstract class DefaultIndicator<\n" +
				"    M extends Serie & FinancialSerie,\n" +
				"    S extends Serie & IndicatorSerie<M>\n" +
				"    > implements Indicator<M, S> {");
	}
}
