package org.stjs.generator.writer.generics;

/**
 * A serie represents a set of points of a chart.
 */
public abstract class Serie {

	public abstract double getXValue(Integer i);

	/**
	 * Finds the index of the point in the [startIndex, endIndex] interval having the given x-value, or the point right after if no point with
	 * the given x-value exists.
	 *
	 * @param xValue The x-value that should correspond to the index searched.
	 * @param xScale The scale in which the search is done.
	 * @param startIndex First index in the list we look at for the search.
	 * @param endIndex Last index in the list we look at for the search.
	 * @return The index of the point at or after the given x-value.
	 */
	protected abstract Integer getIndexAtOrAfterXValue(Number xValue, double xScale, int startIndex, int endIndex);

};
