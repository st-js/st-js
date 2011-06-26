package org.stjs.examples.stock;

import static org.stjs.javascript.Global.$;
import static org.stjs.javascript.Global.alert;

import org.stjs.javascript.jquery.ChangeListener;
import org.stjs.javascript.jquery.Event;

public class JSTest {
	public String a;
	public String b;

	private static void xx(JSTest xx) {
		System.out.println(xx.a);
		System.out.println(xx.b);
	}

	public static void main(String[] args) {
		$("xxx").change(new ChangeListener() {
			public void onChange(Event ev) {
				String t = "a";
				t += "b";
				$(this).val(t);
				$(this).show();

				xx(new JSTest() {
					{
						a = "xxx";
						b = "yyy";
					}
				});
			}
		});

	}

	public void hello() {
		alert("Hello2");
	}
}
