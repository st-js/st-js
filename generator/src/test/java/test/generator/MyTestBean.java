package test.generator;

import static org.stjs.javascript.jquery.GlobalJQuery.$;

public class MyTestBean {
	public static void main(String[] args) {
		$("#orders_and_positions .tabListView").width($("#orders_and_positions .tabList").width());
	}
}
