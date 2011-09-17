package org.stjs.examples.stock;

import org.stjs.javascript.jquery.SuccessListener;

public interface QuoteProvider {

	void updateStock(final Object stock, final SuccessListener listener);
}
