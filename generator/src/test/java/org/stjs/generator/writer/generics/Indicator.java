package org.stjs.generator.writer.generics;

import org.stjs.javascript.Array;

public interface Indicator<M extends Serie & FinancialSerie, S extends Serie & IndicatorSerie<M>> {
	public Array<S> getSeries();

	public Integer getSignal(M mainSerie, int index);

	public S getSignals();
}
