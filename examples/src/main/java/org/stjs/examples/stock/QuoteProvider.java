package org.stjs.examples.stock;

import static org.stjs.javascript.jquery.GlobalJQuery.$;

import org.stjs.javascript.jquery.AjaxParams;
import org.stjs.javascript.jquery.SuccessListener;

public interface QuoteProvider {

	void updateStock(final Object stock, final SuccessListener listener);
}
