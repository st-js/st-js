package org.stjs.javascript;

import org.stjs.javascript.annotation.DataType;

@DataType
public abstract class RegExpMatch implements Array<String> {
	public int index;
	public String input;
}
