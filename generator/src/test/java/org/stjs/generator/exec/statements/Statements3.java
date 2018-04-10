package org.stjs.generator.exec.statements;

import static org.stjs.javascript.JSCollections.$array;
import static org.stjs.javascript.JSGlobal.parseInt;
import static org.stjs.javascript.JSObjectAdapter.$js;

import org.stjs.javascript.Array;

/**
 * (c) Swissquote 10.04.18
 *
 * @author sgoetz
 */
public class Statements3 {

	public static int main(String[] args) {
		$js("Array.prototype.something = function() {};");
		Array<Integer> a = $array(1, 2);
		int count = 0;
		for (String i : a) {
			int x = a.$get(i);
			count++;
		}

		$js("console.log(count)");
		return count;
	}

}
