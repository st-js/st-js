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

import org.stjs.javascript.annotation.JavascriptFunction;
import org.stjs.javascript.annotation.Template;

/**
 * <p>SortFunction interface.</p>
 *
 * @author acraciun
 * @version $Id: $Id
 */
@JavascriptFunction
public interface SortFunction<V> {

	/**
	 * Compares its two arguments for order. Returns a negative integer, zero, or a positive integer as the first
	 * argument is less than, equal to, or greater than the second.
	 *
	 * <p>
	 * The notation:
	 * <ul>
	 * <li><em>a &lt;<sub>CF</sub> b</em> means <tt>comparefn(a,b) &lt; 0</tt>
	 * <li><em>a =<sub>CF</sub> b</em> means <tt>comparefn(a,b) = 0</tt> (of either sign)
	 * <li><em>a &gt;<sub>CF</sub> b</em> means <tt>comparefn(a,b) &gt; 0</tt>
	 * </ul>
	 *
	 * <p>
	 * A function <tt>comparefn</tt> is a consistent comparison function for a set of values <em>S</em> if all of the
	 * requirements below are met for all values <em>a</em>, <em>b</em>, and <em>c</em> (possibly the same value) in the
	 * set <em>S</em>:
	 *
	 * <ul>
	 * <li>Calling <tt>comparefn(a,b)</tt> always returns the same value <tt>v</tt> when given a specific pair of values
	 * <em>a</em> and <em>b</em> as its two arguments. Furthermore, <tt>Type(v)</tt> is <tt>Number</tt>, and <tt>v</tt>
	 * is not <tt>NaN</tt>. Note that this implies that exactly one of <em>a &lt;<sub>CF</sub> b</em>, <em>a
	 * =<sub>CF</sub> b</em>, and <em>a &gt;<sub>CF</sub> b</em> will be true for a given pair of <em>a</em> and
	 * <em>b</em>.
	 * <li>Calling <tt>comparefn(a,b)</tt> does not modify the this object
	 * <li><em>a =<sub>CF</sub> a</em> (reflexivity)
	 * <li>If <em>a =<sub>CF</sub> b</em>, then <em>b =<sub>CF</sub> a</em> (symmetry)
	 * <li>If <em>a =<sub>CF</sub> b</em> and <em>b =<sub>CF</sub> c</em>, then <em>a =<sub>CF</sub> c</em>
	 * (transitivity of =<sub>CF</sub>)
	 * <li>If <em>a &lt;<sub>CF</sub> b</em> and <em>b &lt;<sub>CF</sub> c</em>, then <em>a &lt;<sub>CF</sub> c</em>
	 * (transitivity of &lt;<sub>CF</sub>)
	 * <li>If <em>a &gt;<sub>CF</sub> b</em> and <em>b &gt;<sub>CF</sub> c</em>, then <em>a &gt;<sub>CF</sub> c</em>
	 * (transitivity of <sub>CF</sub>)
	 * </ul>
	 *
	 * @param a
	 *            the first object to be compared
	 * @param b
	 *            the second object to be compared
	 * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater
	 *         than the second.
	 */
	@Template("invoke")
	public int $invoke(V a, V b);
}
