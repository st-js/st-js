package org.stjs.examples.stock;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(STJSTestDriverRunner.class)
@HTMLFixture("<form id='form'>" + //
		"			<table>" + //
		"				<tbody>" + //
		"				</tbody>" + //
		"			</table>" + //
		"			<input type='text' id='newStock'><button id='addStock' type='submit'>Add</button>\n" + //
		"			</form>")
@Scripts("classpath://jquery.js")
public class StockApplicationTest {

	@Test
	@SuppressWarnings("rawtypes")
	public void shouldAddAndRemoveStockInTheList() throws Exception {
		new StockApplication(new QuoteProviderStub()).init();
		$("#newStock").val("goog");
		$("#form").submit();
		assertEquals(1, $("#form tr").size());
		JQuery row = $("#form tr:nth-child(1)");
		assertEquals("goog", row.find("td:nth-child(1)").text());
		assertEquals("3.10", row.find("td:nth-child(2)").text());
		assertEquals("0.10(0.03%)", row.find("td:nth-child(3)").text());
		assertEquals("Remove", row.find("td:nth-child(4) button.removeStock").text());

		$(".removeStock").click();
		assertEquals(0, $("#form tr").size());
	}

}
