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

import org.stjs.javascript.annotation.Adapter;
import org.stjs.javascript.annotation.Template;
import org.stjs.javascript.functions.Callback0;
import org.stjs.javascript.functions.Callback1;
import org.stjs.javascript.functions.Callback2;
import org.stjs.javascript.functions.Callback3;
import org.stjs.javascript.functions.Callback4;
import org.stjs.javascript.functions.Function0;
import org.stjs.javascript.functions.Function1;
import org.stjs.javascript.functions.Function2;
import org.stjs.javascript.functions.Function3;
import org.stjs.javascript.functions.Function4;

/**
 * this adapter provides you the missing functionalities in Java for Javascript functions. It provides you the way to
 * use the "call" and "apply" methods on a function (in Java a single-method interface).
 * 
 * @author acraciun
 * 
 */
@Adapter
public final class JSFunctionAdapter {
	private JSFunctionAdapter() {
		//
	}

	@Template("adapter")
	public native static void call(Callback0 function, Object receiver);

	@Template("adapter")
	public native static <P1> void call(Callback1<P1> function, Object receiver, P1 p1);

	@Template("adapter")
	public native static <P1, P2> void call(Callback2<P1, P2> function, Object receiver, P1 p1, P2 p2);

	@Template("adapter")
	public native static <P1, P2, P3> void call(Callback3<P1, P2, P3> function, Object receiver, P1 p1, P2 p2, P3 p3);

	@Template("adapter")
	public native static <P1, P2, P3, P4> void call(Callback4<P1, P2, P3, P4> function, Object receiver, P1 p1, P2 p2, P3 p3, P4 p4);

	@Template("adapter")
	public native static <R> R call(Function0<R> function, Object receiver);

	@Template("adapter")
	public native static <P1, R> void call(Function1<P1, R> function, Object receiver, P1 p1);

	@Template("adapter")
	public native static <P1, P2, R> void call(Function2<P1, P2, R> function, Object receiver, P1 p1, P2 p2);

	@Template("adapter")
	public native static <P1, P2, P3, R> void call(Function3<P1, P2, P3, R> function, Object receiver, P1 p1, P2 p2, P3 p3);

	@Template("adapter")
	public native static <P1, P2, P3, P4, R> void call(Function4<P1, P2, P3, P4, R> function, Object receiver, P1 p1, P2 p2, P3 p3, P4 p4);

	@Template("adapter")
	public native static <T> T call(Object function, Object receiver, Object... args);

	@Template("adapter")
	public native static <T> T apply(Object function, Object receiver, Array<?> args);
}
