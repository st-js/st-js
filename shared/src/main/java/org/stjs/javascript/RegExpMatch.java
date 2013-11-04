package org.stjs.javascript;

import org.stjs.javascript.annotation.SyntheticType;

@SyntheticType
public abstract class RegExpMatch implements Array<String> {
	public int index;
	public String input;
}
