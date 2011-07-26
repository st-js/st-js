package org.stjs.examples.stock;

import static org.stjs.javascript.JSNumberAdapter.toFixed;

public class ExtendedStockApplication extends StockApplication {

	public ExtendedStockApplication(String test) {
		super(test);
	}

	@Override
	public void test3(String n) {
		super.test3(n + "-ext");
		toFixed(100, 2);
	}
}
