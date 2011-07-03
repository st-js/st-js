<html>
<head>
<script src="${pageContext.request.contextPath}/js/stjs.js"" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/jquery/jquery-1.3.2.js"" type="text/javascript"></script>
<!--<script src="//ajax.googleapis.com/ajax/libs/jquery/1.6.1/jquery.min.js" type="text/javascript"></script>-->
<script src="${pageContext.request.contextPath}/generated-js/org/stjs/examples/stock/StockApplication.js"" type="text/javascript"></script>

<script language="javascript">
onload=function(){
	StockApplication.init();
}
</script>
</head>
<body>
<h1>ST-JS example: a stock watchlist manager</h1>
<form>
<table>
	<thead>
		<tr>
		<th>Stock</th><th>Last</th><th>Change</th><th>Remove</th>
		</tr>
	</thead>
	<tbody>
	</tbody>
</table>
<input type="text" id="newStock"><button id="addStock">Add</button>
<div>Last time: <span id="time"></span></div>
</form>
</body>
</html>