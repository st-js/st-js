package org.stjs.generator;

import japa.parser.ast.Node;

/**
 * This class indicated a position in a source file (line, column) where the given identifier starts.
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 */
public class SourcePosition {
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
