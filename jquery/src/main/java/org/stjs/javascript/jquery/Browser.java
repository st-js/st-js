package org.stjs.javascript.jquery;

import org.stjs.javascript.annotation.DataType;

@DataType
public class Browser {
	public boolean webkit;
	@Deprecated
	public boolean safari;
	public boolean opera;
	public boolean msie;
	public boolean mozilla;

	public String version;
}
