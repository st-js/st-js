package org.stjs.examples.stock;

import static org.stjs.javascript.Global.$;
import static org.stjs.javascript.Global.eval;
import static org.stjs.javascript.JSNumberAdapter.toFixed;

import org.stjs.javascript.jquery.AjaxParams;
import org.stjs.javascript.jquery.Event;
import org.stjs.javascript.jquery.EventHandler;
import org.stjs.javascript.jquery.SuccessListener;

public class StockApplication {
	public void init() {
		final StockApplication that = this;

		// add stock
		$("#addStock").click(new EventHandler() {
			@Override
			public boolean onEvent(Event ev) {
				$.ajax(new AjaxParams() {
					{
						url = "/stjs/stock/stock.jsp?stock=" + $("#newStock").val();
						success = new SuccessListener() {
							@Override
							public void onSuccess(Object data) {
								StockData stockData = (StockData) eval("(" + data + ")");
								String tr = "<tr>";
								tr += "<td>" + stockData.stock + "</td>";
								String color = stockData.last >= stockData.close ? "red" : "green";
								tr += "<td style='color:" + color + "'>" + toFixed(stockData.last, 2) + "</td>";
								double change = (stockData.last - stockData.close);
								double changePercent = change / stockData.close;
								tr += "<td style='color:" + color + "'>" + toFixed(change, 2) + "("
										+ toFixed(changePercent, 2) + "%)</td>";
								tr += "<td><button class='removeStock'>Remove</button></td>";
								tr += "</tr>";
								$(tr).appendTo("table tbody").find(".removeStock").click(that.removeListener);
							}
						};
					}
				});
				return false;
			}
		});

	}

	private EventHandler removeListener = new EventHandler() {
		@Override
		public boolean onEvent(Event ev) {
			$(this).parents("tr").remove();
			return false;
		}
	};
}
