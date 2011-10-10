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

import static org.stjs.javascript.Global.$array;
import static org.stjs.javascript.Global.parseFloat;
import static org.stjs.javascript.Global.setInterval;
import static org.stjs.javascript.Global.window;
import static org.stjs.javascript.JSNumberAdapter.toFixed;
import static org.stjs.javascript.jquery.GlobalJQuery.$;

import org.stjs.javascript.Array;
import org.stjs.javascript.Date;
import org.stjs.javascript.dom.HTMLElement;
import org.stjs.javascript.jquery.Event;
import org.stjs.javascript.jquery.EventHandler;
import org.stjs.javascript.jquery.EvtHandler;
import org.stjs.javascript.jquery.JQuery;
import org.stjs.javascript.jquery.SuccessListener;

public class StockApplication {
	private Array<String> stocks;
	private QuoteProvider quoteProvider;

	public StockApplication(QuoteProvider quoteProvider) {
		this.quoteProvider = quoteProvider;
		this.stocks = $array();
	}

	public static void main(String[] args) {
		$(window).ready(new EvtHandler() {
			@Override
			public void onEvent(Event ev) {
				StockApplication a = new StockApplication(new YahooQuoteProvider());
				a.init();
			}
		});
	}

	public void init() {
		final StockApplication that = this;
		// add stock
		$("#form").submit(new EventHandler() {
			@Override
			public boolean onEvent(Event ev, final HTMLElement THIS) {
				that.quoteProvider.updateStock($("#newStock").val(), new SuccessListener() {
					@Override
					public void onSuccess(Object data) {
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
			public boolean onEvent(Event ev, final HTMLElement THIS) {
				JQuery<?> $tr = $(THIS).parents("tr");
				int index = $tr.parent().find("tr").index($tr);
				that.stocks.splice(index);
				$tr.remove();
				return false;
			}
		});

		// automatic update
		setInterval(new Runnable() {
			@Override
			public void run() {
				for (int i : that.stocks) {
					that.quoteProvider.updateStock(that.stocks.$get(i), new SuccessListener() {
						@Override
						public void onSuccess(Object data) {
							Response response = (Response) data;
							Quote quote = response.query.results.quote;
							$("table tbody tr:nth(" + that.getRowForStock(quote.symbol) + ")").replaceWith(
									that.generateRow(quote));
							$("#timestamp").text(new Date().toString());
						}
					});
				}
			}
		}, 5000);
	}

	private int getRowForStock(String stock) {
		for (int i : stocks) {
			if (stocks.$get(i) == stock) {
				return i;
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

	public static void test2() {
		$("#test2").text("check static");
	}

	public void test3(String n) {
		$("#test3").text(n);
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
