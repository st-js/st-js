package org.stjs.examples.stock;

import static org.stjs.examples.stock.Application.server;
import static org.stjs.examples.stock.Application.transaction;
import static org.stjs.javascript.Global.$;
import static org.stjs.javascript.Global.eval;
import static org.stjs.javascript.Global.nvl;
import static org.stjs.javascript.Global.parseFloat;
import static org.stjs.javascript.Global.parseInt;
import static org.stjs.javascript.jquery.JSNumberAdapter.toFixed;

import java.util.Map;

import org.stjs.javascript.jquery.AjaxParams;
import org.stjs.javascript.jquery.ChangeListener;
import org.stjs.javascript.jquery.ClickListener;
import org.stjs.javascript.jquery.Event;
import org.stjs.javascript.jquery.FocusListener;
import org.stjs.javascript.jquery.JQuery;
import org.stjs.javascript.jquery.KeyupListener;
import org.stjs.javascript.jquery.SuccessListener;
import org.stjs.javascript.jquery.plugins.DatePickerOptions;
import org.stjs.javascript.jquery.plugins.SliderOptions;

public class Transaction {
	public StockHistoData stockHistoData = null;
	public String autoComment = null;
	public Map<String, Double> rates;

	public void init() {
		$(".datePicker").datepicker(new DatePickerOptions() {
			{
				dateFormat = "dd.mm.yy";
				onSelect = transaction.changeDate;
				nextText = "";
				prevText = "";
			}
		});

		if (transaction.stockHistoData != null) {
			transaction.paintCandle();
		}

		$("#buy").click(new ClickListener() {
			@Override
			public boolean onClick(Event ev) {
				if ($(this).hasClass("enabled")) {
					$("#transactionType").val("BUY");
					$("#addStockForm").submit();
				}
				return false;
			}
		});

		$("#sell").click(new ClickListener() {
			@Override
			public boolean onClick(Event ev) {
				if ($(this).hasClass("enabled")) {
					$("#transactionType").val("SELL");
					$("#addStockForm").submit();
				}
				return false;
			}
		});

		$("#addTransactionBtn").click(new ClickListener() {
			@Override
			public boolean onClick(Event ev) {
				if ($(this).hasClass("enabled")) {
					$("#addStockForm").submit();
				}
				return false;
			}
		});

		$(".textWithExplanation").focus(new FocusListener() {
			@Override
			public void onFocus(Event ev) {
				$("#transactionData").show();
			}
		});

		$("#transaction_quantity,#transaction_price,#transaction_commission,#transaction_commissionCurrency").change(
				new ChangeListener() {
					@Override
					public void onChange(Event ev) {
						transaction.updateTotal();
						transaction.updateButtonStatus();
						transaction.updateComment();
					}
				});

		$("#transaction_quantity,#transaction_price,#transaction_commission").keyup(new KeyupListener() {
			@Override
			public void onKeyup(Event ev) {
				transaction.updateTotal();
				transaction.updateButtonStatus();
				transaction.updateComment();
			}
		});

		$("#transaction_price").change(new ChangeListener() {
			@Override
			public void onChange(Event ev) {
				double p = parseFloat(nvl($("#transaction_price").val(), "0"));
				$("#candle").slider("option", "value", p);
				$("#priceHigh").hide();
				$("#priceLow").hide();
				if ((transaction.stockHistoData != null) && (p < transaction.stockHistoData.low)) {
					$("#priceLow").show();
				} else if ((transaction.stockHistoData != null) && (p > transaction.stockHistoData.high)) {
					$("#priceHigh").show();
				}
			}
		});
	}

	public void setStockKey(final String key, String name) {
		$("#transaction_stockKey").val(key);
		$("#ss2").val(name);
		transaction.updateStockInfo();
		transaction.updateRates();
		transaction.updateButtonStatus();

		$.ajax(new AjaxParams() {
			{
				url = server.context + "/views/ViewTransactions.action";
				dataType = "html";
				data = "searchTransactions=&stockKeyVars=" + key + "&pageId=" + $("#pageId").val();
				success = new SuccessListener() {
					@Override
					public void onSuccess(Object ret) {
						$("#similarTransactions").html(ret);
						$("#similar_transaction_area").attr("style", "");
					}
				};
			}
		});
	}

	public void updateStockInfo() {
		// Load data from server with ajax call
		String url = server.context + "/transaction/Transaction.action";
		StockHistoParams params = new StockHistoParams() {
			{
				stockKey = $("#transaction_stockKey").val();
				date = $("#transaction_date").val();
				getStockInfo = "getStockInfo";
			}
		};

		$.get(url, params, new SuccessListener() {
			@Override
			public void onSuccess(Object payloadText) {
				StockHistoData payload = (StockHistoData) eval((String) payloadText);
				// Update interface
				if (payload != null) {
					transaction.stockHistoData = payload;
					if (transaction.stockHistoData.percentQuoted) {
						$("#transaction_factor").val(0.01);
						$("#transaction_nominal_label").show();
						$("#transaction_quantity_label").hide();
					} else {
						$("#transaction_factor").val(1.0);
						$("#transaction_quantity_label").show();
						$("#transaction_nominal_label").hide();
					}
					$("#transaction_currency").html(
							(transaction.stockHistoData.percentQuoted ? "% " : "")
									+ transaction.stockHistoData.currency);
					$("#transaction_currency_total").html(transaction.stockHistoData.currency);
					if (transaction.stockHistoData.close != 0) {
						$("#transaction_price").val(transaction.stockHistoData.close);
					}
					$("#transaction_commissionCurrency").val(transaction.stockHistoData.currency);
					$("#priceHigh").hide();
					$("#priceLow").hide();
					transaction.paintCandle();
					transaction.updateQuantity();
					$("#transaction_comment").trigger("focus");
					transaction.updateComment();
					transaction.updateButtonStatus();
				}
			}
		}, "text");
	}

	public void updateRates() {
		// Load data from server with ajax call
		String url = server.context + "/transaction/Transaction.action";
		StockHistoParams params = new StockHistoParams() {
			{
				stockKey = $("#transaction_stockKey").val();
				date = $("#transaction_date").val();
				getRates = "getRates";
			}
		};

		$.get(url, params, new SuccessListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(Object payloadText) {
				Object payload = eval((String) payloadText);
				// Update interface
				if (payload != null) {
					transaction.rates = (Map<String, Double>) payload;
					transaction.updateQuantity();
				}
			}
		}, "text");
	}

	public void updateComment() {
		if (($("#transaction_comment").val() == "")
				|| ($("#transaction_comment").val() == $("#transaction_comment").attr("explanation"))
				|| ($("#transaction_comment").val() == transaction.autoComment)) {
			String d = $("#transaction_date").val();
			$("#transaction_comment").val(d + "@" + $("#transaction_price").val());
			transaction.autoComment = $("#transaction_comment").val();
		}

	}

	public ChangeListener changeDate = new ChangeListener() {
		@Override
		public void onChange(Event ev) {
			String stockKey = $("#transaction_stockKey").val();
			if ((stockKey != null) && (stockKey != "")) {
				transaction.updateStockInfo();
				transaction.updateRates();
			}
		}
	};

	public void updateTotal() {
		double q = parseFloat(nvl($("#transaction_quantity").val(), "0"));
		double p = parseFloat(nvl($("#transaction_price").val(), "0"))
				* parseFloat(nvl($("#transaction_factor").val(), "0"));
		double c = parseFloat(nvl($("#transaction_commission").val(), "0"));

		// Get rate
		double rate = 1;
		String transactionCommissionCurrency = $("#transaction_commissionCurrency").val();
		if ((transaction.rates != null) && (transaction.rates.get(transactionCommissionCurrency) != null)) {
			rate = transaction.rates.get(transactionCommissionCurrency);
		}

		// Update total
		if ($("#transactionType").val() == "BUY") {
			$("#transaction_total").val(toFixed(q * p + c * rate, 2));
		} else {
			$("#transaction_total").val(toFixed(q * p - c * rate, 2));
		}

	}

	public void updateQuantity() {
		// do not automatically calculate when for example the user closes a position
		if ($("#calculate_transaction_quantity").val() != "true") {
			return;
		}
		double t = parseFloat($("#transaction_total").val());
		double p = parseFloat($("#transaction_price").val()) * parseFloat($("#transaction_factor").val());
		double c = parseFloat($("#transaction_commission").val());
		if (t != 0) {
			return;
		}
		double rate = transaction.rates != null ? transaction.rates.get($("#transaction_commissionCurrency").val()) : 1;
		$("#transaction_quantity").val(parseInt((t - c * rate) / p));

	}

	public void updateButtonStatus() {
		boolean enabled = !$("#transaction_stockKey").val().equals("") && !$("#transaction_date").val().equals("")
				&& !$("#transaction_price").val().equals("") && !$("#transaction_quantity").val().equals("");
		if (enabled) {
			$(".transactionButton").addClass("enabled");
			$(".transactionButton").removeClass("disabled");
		} else {
			$(".transactionButton").removeClass("enabled");
			$(".transactionButton").addClass("disabled");
		}

	}

	public ChangeListener updateSliderPrice = new ChangeListener() {
		@Override
		public void onChange(Event ev) {
			String price = $("#candle").slider("option", "value");
			$("#transaction_price").val(toFixed(price, 2));
			transaction.updateQuantity();
		}
	};

	public void paintCandle() {
		JQuery $candleBar = $("#candle");
		if (this.stockHistoData != null) {
			$candleBar.slider(new SliderOptions() {
				{
					min = Transaction.this.stockHistoData.low;
					max = Transaction.this.stockHistoData.high;
					step = (Transaction.this.stockHistoData.high - Transaction.this.stockHistoData.low) / 10;
					value = Transaction.this.stockHistoData.close;
					change = Transaction.this.updateSliderPrice;
					orientation = "vertical";
				}
			});

			$("#candlePane .jsHigh").text(this.stockHistoData.high);
			$("#candlePane .jsLow").text(this.stockHistoData.low);
		}
	}
}
