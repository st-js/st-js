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
package org.stjs.generator.handlers.utils;

import static org.stjs.generator.handlers.utils.PreConditions.checkNotNull;
import japa.parser.ast.Node;

import java.util.Iterator;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.handlers.RuleBasedVisitor;

public class Joiner {

	private final String separator;
	private final RuleBasedVisitor visitor;

	private Joiner(String separator, RuleBasedVisitor visitor) {
		this.visitor = checkNotNull(visitor);
		this.separator = checkNotNull(separator);
	}

	public void join(Iterable<? extends Node> nodes, GenerationContext arg) {
		if (nodes != null) {
			Iterator<? extends Node> iterator = nodes.iterator();
			if (iterator.hasNext()) {
				iterator.next().accept(visitor, arg);
				while (iterator.hasNext()) {
					visitor.getPrinter().print(separator);
					iterator.next().accept(visitor, arg);
				}
			}
		}
	}

	public static JoinerBuilder joiner(RuleBasedVisitor visitor) {
		return new JoinerBuilder(visitor);
	}

	public static class JoinerBuilder {
		private final RuleBasedVisitor visitor;

		public JoinerBuilder(RuleBasedVisitor visitor) {
			this.visitor = checkNotNull(visitor);
		}

		public Joiner on(String separator) {
			return new Joiner(separator, visitor);
		}
	}

}
