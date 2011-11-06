package org.stjs.examples.stock;

import static org.stjs.javascript.jquery.GlobalJQuery.$;

import org.stjs.javascript.functions.Callback3;
import org.stjs.javascript.jquery.AjaxParams;
import org.stjs.javascript.jquery.JQueryXHR;

public class YahooQuoteProvider implements QuoteProvider {

	@Override
	public void updateStock(final Object stock, final Callback3<Object, String, JQueryXHR> listener) {
		$.ajax(new AjaxParams() {
			{
				url = "http://query.yahooapis.com/v1/public/yql?q=select%20symbol,%20LastTradePriceOnly,PreviousClose%20from%20yahoo.finance.quotes%20where%20symbol%20in%20(%22"
						+ stock
						+ "%22)%0A%09%09&format=json&env=http%3A%2F%2Fdatatables.org%2Falltables.env&callback=?";
				dataType = "jsonp";
				success = listener;
			}
		});
	}

}
