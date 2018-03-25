package org.stjs.javascript;

import org.stjs.javascript.annotation.SyntheticType;

/**
 * <p>Abstract RegExpMatch class.</p>
 *
 * @author acraciun
 * @version $Id: $Id
 */
@SyntheticType
public abstract class RegExpMatch extends Array<String> {
	public int index;
	public String input;
}
