package org.stjs.generator.javascript.rhino;

import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.NodeVisitor;

/**
 * 
 * this node keeps a code AS-IS and it will dump it directly in the source file
 * 
 * @author acraciun
 */
class CodeFragment extends AstNode {
	private String code;

	/**
	 * <p>Constructor for CodeFragment.</p>
	 */
	public CodeFragment() {
		this(0, 0);
	}

	/**
	 * <p>Constructor for CodeFragment.</p>
	 *
	 * @param pos a int.
	 */
	public CodeFragment(int pos) {
		this(pos, 0);
	}

	/**
	 * <p>Constructor for CodeFragment.</p>
	 *
	 * @param pos a int.
	 * @param len a int.
	 */
	public CodeFragment(int pos, int len) {
		super(pos, len);
		this.type = Token.LAST_TOKEN + 2;
	}

	/**
	 * <p>Getter for the field <code>code</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getCode() {
		return code;
	}

	/**
	 * <p>Setter for the field <code>code</code>.</p>
	 *
	 * @param code a {@link java.lang.String} object.
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/** {@inheritDoc} */
	@Override
	public String toSource(int depth) {
		StringBuilder sb = new StringBuilder();
		sb.append(makeIndent(depth));
		if (code != null) {
			sb.append(code);
		}
		return sb.toString();
	}

	/** {@inheritDoc} */
	@Override
	public void visit(NodeVisitor visitor) {
		visitor.visit(this);
	}
}
