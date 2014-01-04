package org.stjs.generator.visitor;

import com.sun.source.util.TreePath;

public interface TreePathHolder {
	void setCurrentPath(TreePath path);

	TreePath getCurrentPath();
}
