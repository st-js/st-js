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
import static org.stjs.javascript.Global.setInterval;
import static org.stjs.javascript.JSNumberAdapter.toFixed;
import static org.stjs.javascript.jquery.GlobalJQuery.$;

import org.stjs.javascript.Array;
import org.stjs.javascript.Date;
import org.stjs.javascript.dom.HTMLElement;
import org.stjs.javascript.jquery.AjaxParams;
import org.stjs.javascript.jquery.Event;
import org.stjs.javascript.jquery.EventHandler;
import org.stjs.javascript.jquery.JQuery;
import org.stjs.javascript.jquery.SuccessListener;

public class StockApplication {
	private Array<String> stocks = $array();

	public StockApplication(String test) {
		$("#test1").text(test);
	}

	public void init() {
		final StockApplication that = this;
		// add stock
		$("#form").submit(new EventHandler() {
			@Override
			public boolean onEvent(Event ev, final HTMLElement THIS) {
				that.updateStock($("#newStock").val(), new SuccessListener() {
					@Override
					public void onSuccess(Object data) {
						StockData stockData = (StockData) data;
						$(that.generateRow(stockData)).appendTo("table tbody");
						that.stocks.push(stockData.stock);
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
					that.updateStock(that.stocks.$get(i), new SuccessListener() {
						@Override
						public void onSuccess(Object data) {
							StockData stockData = (StockData) data;
							$("table tbody tr:nth(" + that.getRowForStock(stockData.stock) + ")").replaceWith(
									that.generateRow(stockData));
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

	private void updateStock(final Object stock, final SuccessListener listener) {
		$.ajax(new AjaxParams() {
			{
				url = "/stjs/stock/stock.jsp?stock=" + stock;
				dataType = "json";
				success = listener;
			}
		});
	}

	private String generateRow(StockData stockData) {
		String tr = "<tr>";
		tr += "<td>" + stockData.stock + "</td>";
		String color = stockData.last >= stockData.close ? "red" : "green";
		tr += "<td style='color:" + color + "'>" + toFixed(stockData.last, 2) + "</td>";
		double change = (stockData.last - stockData.close);
		double changePercent = change / stockData.close;
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
}
