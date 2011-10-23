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
package org.stjs.javascript.jquery;

import org.stjs.javascript.JsFunction;
import org.stjs.shared.Array;

public abstract class SuccessListener implements JsFunction<Void> {
	public abstract void onSuccess(Object data);
	@Override
	public Void $invoke(Object... args) {
		return null;
	}
	@Override
	public Void apply(Object receiver, Array<?> args) {
		return null;
	}
	@Override
	public Void call(Object receiver, Object... args) {
		return null;
	}
}
