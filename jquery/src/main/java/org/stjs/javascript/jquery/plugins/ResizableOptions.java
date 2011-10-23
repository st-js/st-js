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

import org.stjs.javascript.jquery.JQuery;

public class ResizableOptions<FullJQuery extends JQuery<?>> {
	public boolean disabled = false;

	public Object alsoResize = false;

	public boolean animate = false;

	public Object animateDuration = "slow";

	public String animateEasing = "swing";

	public Object aspectRatio = false;

	public boolean autoHide = false;

	public String cancel = ":input,option";

	public Object containment = false;

	public int delay = 0;

	public int distance = 1;

	public boolean ghost = false;

	public Object grid = false;

	public String handles = "e, s, se";

	public String helper = "false";

	public Integer maxHeight = null;

	public Integer maxWidth = null;

	public int minHeight = 10;

	public int minWidth = 10;

	public UIEventHandler<ResizeableUI<FullJQuery>> create;
	public UIEventHandler<ResizeableUI<FullJQuery>> start;

	public UIEventHandler<ResizeableUI<FullJQuery>> resize;
	public UIEventHandler<ResizeableUI<FullJQuery>> stop;
}
