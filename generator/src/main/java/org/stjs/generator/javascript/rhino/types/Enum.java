package org.stjs.generator.javascript.rhino.types;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.NodeVisitor;

import java.util.Iterator;

public class Enum extends AstNode {
	private String name;
	private Iterable<String> values;

	{
		type = TSToken.ENUM;
	}

	public Enum(String name, Iterable<String> values) {
		this.name = name;
		this.values = values;
	}

	@Override
	public String toSource(int depth) {
		StringBuilder sb = new StringBuilder();
		sb.append(makeIndent(depth));
		sb.append("enum ");
		sb.append(name);
		sb.append(" {\n");

		if (values != null) {
			Iterator<String> iter = values.iterator();
			while (iter.hasNext()) {
				String t = iter.next();

				sb.append(makeIndent(depth + 1));
				sb.append(t);

				sb.append(iter.hasNext() ? ", \n" : "\n");
			}
		}

		sb.append(makeIndent(depth));
		sb.append("}\n");
		return sb.toString();
	}

	@Override
	public void visit(NodeVisitor visitor) {
		//TODO :: no idea when this thing's used
	}
}
