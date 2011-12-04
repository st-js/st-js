/**
 *  Copyright 2011 Alexandru Craciun, Eyal Kaspi
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.stjs.examples.stock;

import static org.stjs.javascript.Global.parseFloat;
import static org.stjs.javascript.Global.parseInt;
import static org.stjs.javascript.Global.setInterval;
import static org.stjs.javascript.Global.window;
import static org.stjs.javascript.JSCollections.$array;
import static org.stjs.javascript.JSNumberAdapter.toFixed;
import static org.stjs.javascript.jquery.GlobalJQuery.$;

import org.stjs.javascript.Array;
import org.stjs.javascript.Date;
import org.stjs.javascript.dom.Element;
import org.stjs.javascript.functions.Callback0;
import org.stjs.javascript.functions.Callback3;
import org.stjs.javascript.jquery.Event;
import org.stjs.javascript.jquery.EventHandler;
import org.stjs.javascript.jquery.JQueryCore;
import org.stjs.javascript.jquery.JQueryXHR;

public class StockApplication {
	private Array<String> stocks;
	private QuoteProvider quoteProvider;

	public StockApplication(QuoteProvider quoteProvider) {
		this.quoteProvider = quoteProvider;
		stocks = $array();
	}

	public static void main(String[] args) {
		$(window).ready(new EventHandler() {
			@Override
			public boolean onEvent(Event ev, Element THIS) {
				StockApplication a = new StockApplication(new YahooQuoteProvider());
				a.init();
				a.startUpdate(5000);
				return false;
			}
		});
	}

	public void init() {
		final StockApplication that = this;
		// add stock
		$("#form").submit(new EventHandler() {
			@Override
			public boolean onEvent(Event ev, final Element THIS) {
				that.quoteProvider.updateStock($("#newStock").val(), new Callback3<Object, String, JQueryXHR>() {
					@Override
					public void $invoke(Object data, String status, JQueryXHR xhr) {
						Response response = (Response) data;
						Quote quote = response.query.results.quote;
						$(that.generateRow(quote)).appendTo("table tbody");
						that.stocks.push(quote.symbol);
					}
				});
				return false;

			}
		});

		// the remove stock listener
		$(".removeStock").live("click", new EventHandler() {
			@Override
			public boolean onEvent(Event ev, final Element THIS) {
				JQueryCore<?> $tr = $(THIS).parents("tr");
				int index = $tr.parent().find("tr").index($tr);
				that.stocks.splice(index);
				$tr.remove();
				return false;
			}
		});

	}

	/**
	 * starts the regular update from the quote provider
	 * 
	 * @param interval
	 */
	public void startUpdate(int interval) {
		final StockApplication that = this;
		// automatic update
		setInterval(new Callback0() {
			@Override
			public void $invoke() {
				for (String i : that.stocks) {
					that.quoteProvider.updateStock(that.stocks.$get(i), new Callback3<Object, String, JQueryXHR>() {
						@Override
						public void $invoke(Object data, String status, JQueryXHR xhr) {
							Response response = (Response) data;
							Quote quote = response.query.results.quote;
							$("table tbody tr:nth(" + that.getRowForStock(quote.symbol) + ")").replaceWith(
									that.generateRow(quote));
							$("#timestamp").text(new Date().toString());
						}
					});
				}
			}
		}, interval);
	}

	private int getRowForStock(String stock) {
		for (String i : stocks) {
			if (stocks.$get(i) == stock) {
				return parseInt(i);
			}
		}
		return -1;
	}

	private String generateRow(Quote quote) {
		double last = parseFloat(quote.LastTradePriceOnly);
		double close = parseFloat(quote.PreviousClose);
		String tr = "<tr>";
		tr += "<td>" + quote.symbol + "</td>";
		String color = last >= close ? "red" : "green";
		tr += "<td style='color:" + color + "'>" + toFixed(last, 2) + "</td>";
		double change = (last - close);
		double changePercent = change / close;
		tr += "<td style='color:" + color + "'>" + toFixed(change, 2) + "(" + toFixed(changePercent, 2) + "%)</td>";
		tr += "<td><button class='removeStock'>Remove</button></td>";
		tr += "</tr>";
		return tr;
	}

	static class Response {
		Query query;
	}

	static class Query {
		Results results;
	}

	static class Results {
		Quote quote;
	}

	static class Quote {
		String LastTradePriceOnly;
		String PreviousClose;
		String symbol;
	}

}