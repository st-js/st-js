package org.stjs.examples.stock;

import static org.junit.Assert.assertEquals;
import static org.stjs.javascript.JSCollections.$map;
import static org.stjs.testing.jquery.TestingGlobalJQuery.$;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.stjs.javascript.functions.Callback3;
import org.stjs.javascript.jquery.JQueryXHR;
import org.stjs.testing.annotation.HTMLFixture;
import org.stjs.testing.annotation.ScriptsAfter;
import org.stjs.testing.driver.STJSTestDriverRunner;
import org.stjs.testing.jquery.MockjaxOptions;

@RunWith(STJSTestDriverRunner.class)
@HTMLFixture(" <div id='fortune'></div>")
@ScriptsAfter({ "/jquery.mockjax.js", "/json2.js" })
public class MockjaxExampleTest {
	@Test
	public void myTest() {
		$.ajaxSetup($map("async", false));
		$.mockjax(new MockjaxOptions() {
			{
				url = "/restful/fortune";
				responseTime = 100;
				responseText = new Fortune() {
					{
						status = "success";
						fortune = "Are you a turtle?";
					}
				};
			}
		});

		$.getJSON("/restful/fortune", null, new Callback3<Fortune, String, JQueryXHR>() {
			@Override
			public void $invoke(Fortune response, String p2, JQueryXHR p3) {
				if (response.status.equals("success")) {
					$("#fortune").html("Your fortune is: " + response.fortune);
				} else {
					$("#fortune").html("Things do not look good, no fortune was told");
				}

			}
		});
		assertEquals("Your fortune is: Are you a turtle?", $("#fortune").html());
	}

	private static class Fortune {
		public String status;
		public String fortune;
	}
}
