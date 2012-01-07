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

import org.stjs.javascript.annotation.SyntheticType;
import org.stjs.javascript.functions.Callback1;
import org.stjs.javascript.functions.Callback2;
import org.stjs.javascript.functions.Callback3;

@SyntheticType
public class AjaxParams {
	public boolean async;
	public String url;
	public String dataType;
	public Object data;
	public Callback1<JQueryXHR> beforeSend;
	public Callback3<? extends Object, String, JQueryXHR> success;
	public Callback2<JQueryXHR, String> complete;
	public Callback3<JQueryXHR, String, String> error;

	public boolean cache;
	public String type;
	public Object context;
}
