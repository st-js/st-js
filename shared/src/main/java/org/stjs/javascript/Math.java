/**
 *  Copyright 2011 Alexandru Craciun, Eyal Kaspi
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.stjs.javascript;

/**
 * <p>Math class.</p>
 *
 * @author acraciun
 * @version $Id: $Id
 */
public class Math {
	/** Constant <code>E=java.lang.Math.E</code> */
	public static final double E = java.lang.Math.E;
	/** Constant <code>LN2=java.lang.Math.log(2)</code> */
	public static final double LN2 = java.lang.Math.log(2);
	/** Constant <code>LN10=java.lang.Math.log(10)</code> */
	public static final double LN10 = java.lang.Math.log(10);
	/** Constant <code>LOG2E=1 / LN2</code> */
	public static final double LOG2E = 1 / LN2;
	/** Constant <code>LOG10E=LN10 / LN2</code> */
	public static final double LOG10E = LN10 / LN2;
	/** Constant <code>PI=java.lang.Math.PI</code> */
	public static final double PI = java.lang.Math.PI;
	/** Constant <code>SQRT1_2=java.lang.Math.sqrt(1 / 2)</code> */
	public static final double SQRT1_2 = java.lang.Math.sqrt(1 / 2);
	/** Constant <code>SQRT2=java.lang.Math.sqrt(2)</code> */
	public static final double SQRT2 = java.lang.Math.sqrt(2);

	private Math() {//
	}

	/**
	 * <p>abs.</p>
	 *
	 * @param x a double.
	 * @return a double.
	 */
	public static final double abs(double x) {
		return java.lang.Math.abs(x);
	}

	/**
	 * <p>acos.</p>
	 *
	 * @param x a double.
	 * @return a double.
	 */
	public static final double acos(double x) {
		return java.lang.Math.acos(x);
	}

	/**
	 * <p>asin.</p>
	 *
	 * @param x a double.
	 * @return a double.
	 */
	public static final double asin(double x) {
		return java.lang.Math.asin(x);
	}

	/**
	 * <p>atan.</p>
	 *
	 * @param x a double.
	 * @return a double.
	 */
	public static final double atan(double x) {
		return java.lang.Math.atan(x);
	}

	/**
	 * <p>atan2.</p>
	 *
	 * @param y a double.
	 * @param x a double.
	 * @return a double.
	 */
	public static final double atan2(double y, double x) {
		return java.lang.Math.atan2(y, x);
	}

	/**
	 * <p>ceil.</p>
	 *
	 * @param x a double.
	 * @return a double.
	 */
	public static final double ceil(double x) {
		return java.lang.Math.ceil(x);
	}

	/**
	 * <p>cos.</p>
	 *
	 * @param x a double.
	 * @return a double.
	 */
	public static final double cos(double x) {
		return java.lang.Math.cos(x);
	}

	/**
	 * <p>exp.</p>
	 *
	 * @param x a double.
	 * @return a double.
	 */
	public static final double exp(double x) {
		return java.lang.Math.exp(x);
	}

	/**
	 * <p>floor.</p>
	 *
	 * @param x a double.
	 * @return a double.
	 */
	public static final double floor(double x) {
		return java.lang.Math.floor(x);
	}

	/**
	 * <p>log.</p>
	 *
	 * @param x a double.
	 * @return a double.
	 */
	public static final double log(double x) {
		return java.lang.Math.log(x);
	}

	/**
	 * <p>min.</p>
	 *
	 * @param x a double.
	 * @return a double.
	 */
	public static final double min(double... x) {
		double m = Double.POSITIVE_INFINITY;
		for (double xx : x) {
			m = java.lang.Math.min(xx, m);
		}
		return m == Double.POSITIVE_INFINITY ? Double.NaN : m;
	}

	/**
	 * <p>max.</p>
	 *
	 * @param x a double.
	 * @return a double.
	 */
	public static final double max(double... x) {
		double m = Double.NEGATIVE_INFINITY;
		for (double xx : x) {
			m = java.lang.Math.max(xx, m);
		}
		return m == Double.NEGATIVE_INFINITY ? Double.NaN : m;
	}

	/**
	 * <p>pow.</p>
	 *
	 * @param x a double.
	 * @param y a double.
	 * @return a double.
	 */
	public static final double pow(double x, double y) {
		return java.lang.Math.pow(x, y);
	}

	/**
	 * <p>random.</p>
	 *
	 * @return a double.
	 */
	public static final double random() {
		return java.lang.Math.random();
	}

	/**
	 * <p>round.</p>
	 *
	 * @param x a double.
	 * @return a double.
	 */
	public static final double round(double x) {
		return java.lang.Math.round(x);
	}

	/**
	 * <p>sin.</p>
	 *
	 * @param x a double.
	 * @return a double.
	 */
	public static final double sin(double x) {
		return java.lang.Math.sin(x);
	}

	/**
	 * <p>sqrt.</p>
	 *
	 * @param x a double.
	 * @return a double.
	 */
	public static final double sqrt(double x) {
		return java.lang.Math.sqrt(x);
	}

	/**
	 * <p>tan.</p>
	 *
	 * @param x a double.
	 * @return a double.
	 */
	public static final double tan(double x) {
		return java.lang.Math.tan(x);
	}

}
