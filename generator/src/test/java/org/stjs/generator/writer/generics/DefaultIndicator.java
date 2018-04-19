package org.stjs.generator.writer.generics;

import org.stjs.javascript.Array;

public abstract class DefaultIndicator<M extends Serie & FinancialSerie, S extends Serie & IndicatorSerie<M>> implements Indicator<M, S> {

	protected Array<S> series;
	protected S signals;

	/**
	 * If the referenced series are not the same for historical data, when zoomed in or in a different period, the reference
	 * values between indicators and the main serie may differ. For intraday data this may not happen as reference value is
	 * yesterday close point.
	 *
	 * @param computedSeries Indicators associated with the serie
	 * @param mainSerie      The main serie currently being displayed
	 * @return True if there is a discrepancy between the serie being drawn and the indicator reference serie for histo data
	 */
	private boolean isDifferentReference(Array<S> computedSeries, M mainSerie) {
		S computedIndicator = computedSeries != null ? computedSeries.$get(0) : null;
		return  (computedIndicator == null || computedIndicator.getMainSerie() == null || mainSerie == null);
	}

	@Override
	public Array<S> getSeries() {
		return series;
	}

	@Override
	public S getSignals() {
		return signals;
	}
}
