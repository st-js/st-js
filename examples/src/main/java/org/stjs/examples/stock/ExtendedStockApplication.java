package org.stjs.examples.stock;

public class ExtendedStockApplication extends StockApplication {

	public ExtendedStockApplication(String test) {
		super(test);
	}

	@Override
	public void test3(String n) {
		super.test3(n + "-ext");
	}
}
