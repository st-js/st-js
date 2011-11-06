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
package org.stjs.javascript.jquery.plugins;

import org.stjs.javascript.annotation.DataType;
import org.stjs.javascript.jquery.JQuery;

@DataType
public class ButtonOptions<FullJQuery extends JQuery<?>> {
	public boolean disabled = false;

	public boolean text = true;

	public Object icons;// = { primary: null, secondary: null }

	public String label;

	public UIEventHandler<ButtonUI<FullJQuery>> create;

}
