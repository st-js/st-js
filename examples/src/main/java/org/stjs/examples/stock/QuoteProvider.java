package org.stjs.examples.stock;

import org.stjs.javascript.functions.Callback3;
import org.stjs.javascript.jquery.JQueryXHR;

public interface QuoteProvider {

	void updateStock(final Object stock, final Callback3<Object, String, JQueryXHR> listener);
}
