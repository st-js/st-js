<%@ page language="java" contentType="text/json"%>
{
	stock: "${param.stock}",
	last: <%= 1 + 100 * java.lang.Math.random()%>,
	close: <%= 1 + 100 * java.lang.Math.random()%>
}