package org.stjs.generator.writer.generics;

import org.stjs.javascript.Array;

public interface IndicatorSerie<S extends Serie & FinancialSerie> {

	public void update(S mainSerie, Array<Double> range);

	public S getMainSerie();
}
