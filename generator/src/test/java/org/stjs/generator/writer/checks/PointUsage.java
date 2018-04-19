package org.stjs.generator.writer.checks;

import static org.stjs.javascript.JSObjectAdapter.$js;

/**
 * (c) Swissquote 19.04.18
 *
 * @author sgoetz
 */
public class PointUsage {

	public boolean main(int[] args) {
		Point2D first = new Point2D(2,2);
		Point2D second = new Point2D(2,2);

		Boolean result = first.equals(second);
		$js("console.log(result)");
		return result;
	}
}
