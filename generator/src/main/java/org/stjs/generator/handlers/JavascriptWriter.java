package org.stjs.generator.handlers;

public class JavascriptWriter {
	private int level = 0;

	private boolean indented = false;

	private final StringBuilder buf = new StringBuilder();

	public JavascriptWriter indent() {
		level++;
		return this;
	}

	public JavascriptWriter unindent() {
		level--;
		return this;
	}

	private void makeIndent() {
		for (int i = 0; i < level; i++) {
			buf.append("    ");
		}
	}

	public JavascriptWriter printLiteral(String value) {
		//
		print(value);
		return this;
	}

	public JavascriptWriter printStringLiteral(String value) {
		print("\"");
		print(value);
		print("\"");
		return this;
	}

	public JavascriptWriter print(String arg) {
		if (!indented) {
			makeIndent();
			indented = true;
		}
		buf.append(arg);
		return this;
	}

	public JavascriptWriter printLn(String arg) {
		print(arg);
		printLn();
		return this;
	}

	public JavascriptWriter printLn() {
		buf.append("\n");
		indented = false;
		return this;
	}

	public String getSource() {
		return buf.toString();
	}

	@Override
	public String toString() {
		return getSource();
	}

}
