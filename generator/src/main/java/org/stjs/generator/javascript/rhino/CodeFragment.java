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

	public CodeFragment() {
		this(0, 0);
	}

	public CodeFragment(int pos) {
		this(pos, 0);
	}

	public CodeFragment(int pos, int len) {
		super(pos, len);
		this.type = Token.LAST_TOKEN + 2;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toSource(int depth) {
		StringBuilder sb = new StringBuilder();
		sb.append(makeIndent(depth));
		if (code != null) {
			sb.append(code);
		}
		return sb.toString();
	}

	@Override
	public void visit(NodeVisitor visitor) {
		visitor.visit(this);
	}
}
