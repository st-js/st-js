var system = require('system');
var browserId = system.args[1];
var testServer = system.args[2];

var iframe = document.createElement('iframe');
document.body.appendChild(iframe);


function reportResultAndRunNextTest(result, stacktrace){
	console.error('reporting test result');
	iframe.src = testServer + '/getNextTest' + 
			'?browserId=' + browserId + 
			'&result=' + result + 
			'&location=' + stacktrace + 
			'&rand=' + Math.random();
}

function runFirstTest(){
	reportResultAndRunNextTest("OK");
}

// function sendError(method, ex){
// 	if (ex != "OK") {
// 		console.error(ex);
// 		//console.error(stacktrace());
// 	}
// 	console.error("" + ex);
// 	var xmlhttp = new XMLHttpRequest();
// 	try {
// 		var result = ex.message || ex;
// 		xmlhttp.open("POST", testServer + "/getNextTest?browserId=" + browserId + "&result=" + result + "&location=" + ex.location, true);
// 		xmlhttp.send();
// 	} catch (ex) {
// 		console.error("error:" + ex);
// 	}
// }
// 
// function sendOK(method){
// 	sendError(method, "OK");
// }

runFirstTest();

// phantom.exit();