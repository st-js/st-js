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

public class Math {
	public static final double E = java.lang.Math.E;
	public static final double LN2 = java.lang.Math.log(2);
	public static final double LN10 = java.lang.Math.log(10);
	public static final double LOG2E = 1 / LN2;
	public static final double LOG10E = LN10 / LN2;
	public static final double PI = java.lang.Math.PI;
	public static final double SQRT1_2 = java.lang.Math.sqrt(1 / 2);
	public static final double SQRT2 = java.lang.Math.sqrt(2);

	private Math() {//
	}

	public static final double abs(double x) {
		return java.lang.Math.abs(x);
	}

	public static final double acos(double x) {
		return java.lang.Math.acos(x);
	}

	public static final double asin(double x) {
		return java.lang.Math.asin(x);
	}

	public static final double atan(double x) {
		return java.lang.Math.atan(x);
	}

	public static final double atan2(double y, double x) {
		return java.lang.Math.atan2(y, x);
	}

	public static final double ceil(double x) {
		return java.lang.Math.ceil(x);
	}

	public static final double cos(double x) {
		return java.lang.Math.cos(x);
	}

	public static final double exp(double x) {
		return java.lang.Math.exp(x);
	}

	public static final double floor(double x) {
		return java.lang.Math.floor(x);
	}

	public static final double log(double x) {
		return java.lang.Math.log(x);
	}

	public static final double min(double... x) {
		double m = Double.POSITIVE_INFINITY;
		for (double xx : x) {
			m = java.lang.Math.min(xx, m);
		}
		return m == Double.POSITIVE_INFINITY ? Double.NaN : m;
	}

	public static final double max(double... x) {
		double m = Double.NEGATIVE_INFINITY;
		for (double xx : x) {
			m = java.lang.Math.max(xx, m);
		}
		return m == Double.NEGATIVE_INFINITY ? Double.NaN : m;
	}

	public static final double pow(double x, double y) {
		return java.lang.Math.pow(x, y);
	}

	public static final double random() {
		return java.lang.Math.random();
	}

	public static final double round(double x) {
		return java.lang.Math.round(x);
	}

	public static final double sin(double x) {
		return java.lang.Math.sin(x);
	}

	public static final double sqrt(double x) {
		return java.lang.Math.sqrt(x);
	}

	public static final double tan(double x) {
		return java.lang.Math.tan(x);
	}

}
