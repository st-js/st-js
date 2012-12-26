package org.stjs.examples.stock;

import static org.junit.Assert.assertEquals;
import static org.stjs.javascript.JSCollections.$map;
import static org.stjs.testing.jquery.TestingGlobalJQuery.$;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.stjs.javascript.jquery.JQueryCore;
import org.stjs.testing.annotation.HTMLFixture;
import org.stjs.testing.annotation.ScriptsAfter;
import org.stjs.testing.driver.STJSTestDriverRunner;
import org.stjs.testing.jquery.MockjaxOptions;

@RunWith(STJSTestDriverRunner.class)
@HTMLFixture("<form id='form'>" + //
		"			<table>" + //
		"				<tbody>" + //
		"				</tbody>" + //
		"			</table>" + //
		"			<input type='text' id='newStock'><button id='addStock' type='submit'>Add</button>\n" + //
		"			</form>")
@ScriptsAfter({ "/jquery.mockjax.js", "/json2.js" })
public class StockApplicationTest {

	@Test
	@SuppressWarnings("rawtypes")
	public void shouldAddAndRemoveStockInTheList() throws Exception {
		new StockApplication(new QuoteProviderStub()).init();
		$("#newStock").val("goog");
		$("#form").submit();
		assertEquals(1, $("#form tr").size());
		JQueryCore row = $("#form tr:nth-child(1)");
		assertEquals("goog", row.find("td:nth-child(1)").text());
		assertEquals("3.10", row.find("td:nth-child(2)").text());
		assertEquals("0.10(0.03%)", row.find("td:nth-child(3)").text());
		assertEquals("Remove", row.find("td:nth-child(4) button.removeStock").text());

		$(".removeStock").click();
		assertEquals(0, $("#form tr").size());
	}

	private static StockApplication.Response answer(String symbol) {
		StockApplication.Quote quote = new StockApplication.Quote();
		quote.symbol = symbol;
		quote.LastTradePriceOnly = "3.10";
		quote.PreviousClose = "3.00";
		StockApplication.Response response = new StockApplication.Response();
		response.query = new StockApplication.Query();
		response.query.results = new StockApplication.Results();
		response.query.results.quote = quote;
		return response;
	}

	@Test
	@SuppressWarnings("rawtypes")
	public void shouldAddAndRemoveStockInTheListWithMockjax() throws Exception {
		$.ajaxSetup($map("async", false));

		$.mockjax(new MockjaxOptions() {
			{
				url = "*";
				responseTime = 100;
				responseText = answer("goog");
			}
		});

		new StockApplication(new YahooQuoteProvider()).init();
		$("#newStock").val("goog");
		$("#form").submit();
		assertEquals(1, $("#form tr").size());
		JQueryCore row = $("#form tr:nth-child(1)");
		assertEquals("goog", row.find("td:nth-child(1)").text());
		assertEquals("3.10", row.find("td:nth-child(2)").text());
		assertEquals("0.10(0.03%)", row.find("td:nth-child(3)").text());
		assertEquals("Remove", row.find("td:nth-child(4) button.removeStock").text());

		$(".removeStock").click();
		assertEquals(0, $("#form tr").size());
	}

}
