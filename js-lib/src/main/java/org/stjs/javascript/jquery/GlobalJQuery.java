package org.stjs.javascript.jquery;

abstract public class GlobalJQuery {
	public static GlobalJQuery $;

	/**
	 * jquery constructors
	 */
	public static <FullJQuery extends JQueryAndPlugins<?>> JQueryAndPlugins<FullJQuery> $(String path) {
		return null;
	}

	public static <FullJQuery extends JQueryAndPlugins<?>> JQueryAndPlugins<FullJQuery> $(Object path) {
		return null;
	}

	abstract public void ajax(AjaxParams params);

	abstract public void get(String url, Object params, SuccessListener successListener, String mode);
}
