package org.stjs.javascript.jquery;

public interface GlobalJQuery {

	public void ajax(AjaxParams params);

	public void get(String url, Object params, SuccessListener successListener, String mode);
}
