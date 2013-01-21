var system = require('system');
var browserId = system.args[1];
var testServer = system.args[2];

var iframe = document.createElement('iframe');
document.body.appendChild(iframe);


function reportResultAndRunNextTest(result, stacktrace){
	console.error('reporting test result');
	iframe.src = testServer + 'getNextTest' + 
			'?browserId=' + browserId + 
			'&result=' + result + 
			'&location=' + stacktrace + 
			'&rand=' + Math.random();
}

function runFirstTest(){
	reportResultAndRunNextTest("OK");
}

function startingTest(){
	// empty for phantomjs
}

runFirstTest();