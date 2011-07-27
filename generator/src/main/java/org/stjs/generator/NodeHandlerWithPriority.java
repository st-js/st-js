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
package org.stjs.generator;

import org.stjs.generator.handlers.DefaultHandler;

public class NodeHandlerWithPriority {
	private final DefaultHandler handler;
	private final int priority;

	public NodeHandlerWithPriority(DefaultHandler handler, int priority) {
		this.handler = handler;
		this.priority = priority;
	}

	public DefaultHandler getHandler() {
		return handler;
	}

	public int getPriority() {
		return priority;
	}

}
