var system = require('system');

var el = document.createElement('iframe');
el.src = 'http://localhost:8055/getNextTest?browserId=' + system.args[1] + '&rand=' + Math.random();
document.body.appendChild(el);


function sendError(method, ex){
	if (ex != "OK") {
		console.error(ex);
		//console.error(stacktrace());
	}
	log("" + ex);
	var xmlhttp = new XMLHttpRequest();
	try {
		var result = ex.message || ex;
		xmlhttp.open("POST", "/getNextTest?browserId=" + id + "&result=" + result + "&testId=" + testId + "&method=" + method + "&location=" + ex.location, true);
		xmlhttp.send();
	} catch (ex) {
		console.error("error:" + ex);
	}
	status(STATE.WAITING);
	setTimeout(retrieve, timeout());
}

function sendOK(method){
	sendError(method, "OK");
}

// phantom.exit();