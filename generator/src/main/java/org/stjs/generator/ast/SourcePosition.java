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
package org.stjs.generator.ast;

import japa.parser.ast.Node;

import java.io.Serializable;

import javax.annotation.concurrent.Immutable;

/**
 * This class indicated a position in a source file (line, column) where the given identifier starts.
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 */
@Immutable
//PMD cannot correctly handle final fields
@SuppressWarnings("PMD.BeanMembersShouldSerialize")
public final class SourcePosition implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private final int line;
	private final int column;

	public SourcePosition(int beginLine, int beginColumn) {
		this.line = beginLine;
		this.column = beginColumn;
	}

	public SourcePosition(Node node) {
		this(node.getBeginLine(), node.getBeginColumn());
	}

	public int getLine() {
		return line;
	}

	public int getColumn() {
		return column;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + column;
		result = prime * result + line;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		SourcePosition other = (SourcePosition) obj;
		if (column != other.column) {
			return false;
		}
		if (line != other.line) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return line + ":" + column;
	}

}
