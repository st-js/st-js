var junit = {
	expectedAssertCount: 0,
	assertCount : 0,
	isArray: Array.isArray || function( obj ) {
		if (obj == null)
			return false;
		return toString.call(obj) === "[object Array]";
	}
}

function expectAsserts(count) {
  junit.expectedAssertCount = count;
}


var fail = function fail(location, msg) {
  var err = new Error(arguments.length == 2 ? msg : arguments[0]);
  err.name = 'AssertError';
  err.location=arguments.length == 2 ? location : "";

  if (!err.message) {
    err.message = msg;
  }

  throw err;
};


function isBoolean_(bool) {
  if (typeof(bool) != 'boolean') {
    fail(location, 'Not a boolean: ' + prettyPrintEntity_(bool));
  }
}


var isElement_ = (function () {
  var div = document.createElement('div');

  function isNode(obj) {
    try {
      div.appendChild(obj);
      div.removeChild(obj);
    } catch (e) {
      return false;
    }

    return true;
  }

  return function isElement(obj) {
    return obj && obj.nodeType === 1 && isNode(obj);
  };
}());


function formatElement_(el) {
  var tagName;

  try {
    tagName = el.tagName.toLowerCase();
    var str = '<' + tagName;
    var attrs = el.attributes, attribute;

    for (var i = 0, l = attrs.length; i < l; i++) {
      attribute = attrs.item(i);

      if (!!attribute.nodeValue) {
        str += ' ' + attribute.nodeName + '=\"' + attribute.nodeValue + '\"';
      }
    }

    return str + '>...</' + tagName + '>';
  } catch (e) {
    return '[Element]' + (!!tagName ? ' ' + tagName : '');
  }
}


function prettyPrintEntity_(entity) {
  if (isElement_(entity)) {
    return formatElement_(entity);
  }

  var str;

  if (typeof entity == 'function') {
    try {
      str = entity.toString().match(/(function [^\(]+\(\))/)[1];
    } catch (e) {}

    return str || '[function]';
  }

  try {
    str = JSON.stringify(entity);
  } catch (e) {}

  return str || '[' + typeof entity + ']';
}


function argsWithOptionalMsg_(args, length) {
  var copyOfArgs = [];
  // make copy because it's bad practice to change a passed in mutable
  // And to ensure we aren't working with an arguments array. IE gets bitchy.
  for(var i = 2; i < args.length; i++) {
    copyOfArgs.push(args[i]);
  }
  var argsLength = args.length - 2; //remove location and statement
  var min = length - 1;

  if (argsLength < min) {
    fail(location, args[0], 'expected at least ' + min + ' arguments, got ' + argsLength);
  } else if (argsLength >= length && typeof copyOfArgs[0] === "string") {
    copyOfArgs[0] += ' ';
  } else {
    copyOfArgs.unshift('');
  }
  return copyOfArgs;
}


function assertTrue(location, statement, msg, actual) {
  var args = argsWithOptionalMsg_(arguments, 2);
  junit.assertCount++;

  isBoolean_(args[1]);
  if (args[1] != true) {
    fail(location, args[0] + 'expected true but was ' + prettyPrintEntity_(args[1]));
  }
  return true;
}


function assertFalse(location, statement, msg, actual) {
  var args = argsWithOptionalMsg_(arguments, 2);
  junit.assertCount++;

  isBoolean_(args[1]);
  if (args[1] != false) {
    fail(location, args[0] + 'expected false but was ' + prettyPrintEntity_(args[1]));
  }
  return true;
}


function assertEquals(location, statement, msg, expected, actual, delta) {
  var args = argsWithOptionalMsg_(arguments, 3);
  junit.assertCount++;
  msg = args[0];
  expected = args[1];
  actual = args[2];
  delta = args[3];


  if (!compare_(expected, actual, delta)) {
    fail(location, msg + 'expected ' + prettyPrintEntity_(expected) + ' but was ' +
        prettyPrintEntity_(actual) + '');
  }
  return true;
}


function compare_(expected, actual, delta) {
  if (expected === actual) {
    return true;
  }
  if (delta != null){
	  return Math.abs(expected - actual) <= delta;
  }

  if (typeof expected != 'object' ||
      typeof actual != 'object' ||
      !expected || !actual) {
    return expected == actual;
  }

  if (isElement_(expected) || isElement_(actual)) {
    return false;
  }

  var key = null;
  var actualLength   = 0;
  var expectedLength = 0;

  try {
    // If an array is expected the length of actual should be simple to
    // determine. If it is not it is undefined.
    if (junit.isArray(actual)) {
      actualLength = actual.length;
    } else {
      // In case it is an object it is a little bit more complicated to
      // get the length.
      for (key in actual) {
        if (actual.hasOwnProperty(key)) {
          ++actualLength;
        }
      }
    }

    // Arguments object
    if (actualLength == 0 && typeof actual.length == 'number') {
      actualLength = actual.length;

      for (var i = 0, l = actualLength; i < l; i++) {
        if (!(i in actual)) {
          actualLength = 0;
          break;
        }
      }
    }

    for (key in expected) {
      if (expected.hasOwnProperty(key)) {
        if (!compare_(expected[key], actual[key])) {
          return false;
        }

        ++expectedLength;
      }
    }

    if (expectedLength != actualLength) {
      return false;
    }

    return expectedLength == 0 ? expected.toString() == actual.toString() : true;
  } catch (e) {
    return false;
  }
}


function assertNotEquals(location, statement, msg, expected, actual) {
  try {
    assertEquals.apply(this, arguments);
  } catch (e) {
    if (e.name == 'AssertError') {
      return true;
    }

    throw e;
  }

  var args = argsWithOptionalMsg_(arguments, 3);

  fail(location, args[0] + 'expected ' + prettyPrintEntity_(args[1]) +
      ' not to be equal to ' + prettyPrintEntity_(args[2]));
}


function assertSame(location, statement, msg, expected, actual) {
  var args = argsWithOptionalMsg_(arguments, 3);
  junit.assertCount++;

  if (!isSame_(args[2], args[1])) {
    fail(location, args[0] + 'expected ' + prettyPrintEntity_(args[1]) + ' but was ' +
        prettyPrintEntity_(args[2]));
  }
  return true;
}


function assertNotSame(location, statement, msg, expected, actual) {
  var args = argsWithOptionalMsg_(arguments, 3);
  junit.assertCount++;

  if (isSame_(args[2], args[1])) {
    fail(location, args[0] + 'expected not same as ' + prettyPrintEntity_(args[1]) +
        ' but was ' + prettyPrintEntity_(args[2]));
  }
  return true;
}


function isSame_(expected, actual) {
  return actual === expected;
}


function assertNull(location, statement, msg, actual) {
  var args = argsWithOptionalMsg_(arguments, 2);
  junit.assertCount++;

  if (args[1] !== null) {
    fail(location, args[0] + 'expected null but was ' + prettyPrintEntity_(args[1]));
  }
  return true;
}


function assertNotNull(location, statement, msg, actual) {
  var args = argsWithOptionalMsg_(arguments, 2);
  junit.assertCount++;

  if (args[1] === null) {
    fail(location, args[0] + 'expected not null but was null');
  }

  return true;
}


function assertUndefined(location, statement, msg, actual) {
  var args = argsWithOptionalMsg_(arguments, 2);
  junit.assertCount++;

  if (typeof args[1] != 'undefined') {
    fail(location, args[2] + 'expected undefined but was ' + prettyPrintEntity_(args[1]));
  }
  return true;
}


function assertNotUndefined(location, statement, msg, actual) {
  var args = argsWithOptionalMsg_(arguments, 2);
  junit.assertCount++;

  if (typeof args[1] == 'undefined') {
    fail(location, args[0] + 'expected not undefined but was undefined');
  }
  return true;
}


function assertNaN(location, statement, msg, actual) {
  var args = argsWithOptionalMsg_(arguments, 2);
  junit.assertCount++;

  if (!isNaN(args[1])) {
    fail(location, args[0] + 'expected to be NaN but was ' + args[1]);
  }

  return true;
}


function assertNotNaN(location, statement, msg, actual) {
  var args = argsWithOptionalMsg_(arguments, 2);
  junit.assertCount++;

  if (isNaN(args[1])) {
    fail(location, args[0] + 'expected not to be NaN');
  }

  return true;
}


function assertException(location, statement, msg, callback, error) {
  if (arguments.length == 1) {
    // assertThrows(callback)
    callback = msg;
    msg = '';
  } else if (arguments.length == 2) {
    if (typeof callback != 'function') {
      // assertThrows(callback, type)
      error = callback;
      callback = msg;
      msg = '';
    } else {
      // assertThrows(msg, callback)
      msg += ' ';
    }
  } else {
    // assertThrows(msg, callback, type)
    msg += ' ';
  }

  junit.assertCount++;

  try {
    callback();
  } catch(e) {
    if (e.name == 'AssertError') {
      throw e;
    }

    if (error && e.name != error) {
      fail(location, msg + 'expected to throw ' + error + ' but threw ' + e.name);
    }

    return true;
  }

  fail(location, msg + 'expected to throw exception');
}


function assertNoException(location, statement, msg, callback) {
  var args = argsWithOptionalMsg_(arguments, 2);
  junit.assertCount++;

  try {
    args[1]();
  } catch(e) {
    fail(location, args[0] + 'expected not to throw exception, but threw ' + e.name +
        ' (' + e.message + ')');
  }
}


function assertArray(location, statement, msg, actual) {
  var args = argsWithOptionalMsg_(arguments, 2);
  junit.assertCount++;

  if (!junit.isArray(args[1])) {
    fail(location, args[0] + 'expected to be array, but was ' +
        prettyPrintEntity_(args[1]));
  }
}


function assertTypeOf(location, statement, msg, expected, value) {
  var args = argsWithOptionalMsg_(arguments, 3);
  junit.assertCount++;
  var actual = typeof args[2];

  if (actual != args[1]) {
    fail(location, args[0] + 'expected to be ' + args[1] + ' but was ' + actual);
  }

  return true;
}


function assertBoolean(location, statement, msg, actual) {
  var args = argsWithOptionalMsg_(arguments, 2);
  return assertTypeOf(args[0], 'boolean', args[1]);
}


function assertFunction(location, statement, msg, actual) {
  var args = argsWithOptionalMsg_(arguments, 2);
  return assertTypeOf(args[0], 'function', args[1]);
}


function assertObject(location, statement, msg, actual) {
  var args = argsWithOptionalMsg_(arguments, 2);
  return assertTypeOf(args[0], 'object', args[1]);
}


function assertNumber(location, statement, msg, actual) {
  var args = argsWithOptionalMsg_(arguments, 2);
  return assertTypeOf(args[0], 'number', args[1]);
}


function assertString(location, statement, msg, actual) {
  var args = argsWithOptionalMsg_(arguments, 2);
  return assertTypeOf(args[0], 'string', args[1]);
}


function assertMatch(location, statement, msg, regexp, actual) {
  var args = argsWithOptionalMsg_(arguments, 3);
  var isUndef = typeof args[2] == 'undefined';
  junit.assertCount++;
  var _undef;

  if (isUndef || !args[1].test(args[2])) {
    actual = (isUndef ? _undef : prettyPrintEntity_(args[2]));
    fail(location, args[0] + 'expected ' + actual + ' to match ' + args[1]);
  }

  return true;
}


function assertNoMatch(location, statement, msg, regexp, actual) {
  var args = argsWithOptionalMsg_(arguments, 3);
  junit.assertCount++;

  if (args[1].test(args[2])) {
    fail(location, args[0] + 'expected ' + prettyPrintEntity_(args[2]) +
        ' not to match ' + args[1]);
  }

  return true;
}


function assertTagName(location, statement, msg, tagName, element) {
  var args = argsWithOptionalMsg_(arguments, 3);
  var actual = args[2] && args[2].tagName;

  if (String(actual).toUpperCase() != args[1].toUpperCase()) {
    fail(location, args[0] + 'expected tagName to be ' + args[1] + ' but was ' + actual);
  }
  return true;
}


function assertClassName(location, statement, msg, className, element) {
  var args = argsWithOptionalMsg_(arguments, 3);
  var actual = args[2] && args[2].className;
  var regexp = new RegExp('(^|\\s)' + args[1] + '(\\s|$)');

  try {
    assertMatch(args[0], regexp, actual);
  } catch (e) {
    actual = prettyPrintEntity_(actual);
    fail(location, args[0] + 'expected class name to include ' +
        prettyPrintEntity_(args[1]) + ' but was ' + actual);
  }

  return true;
}


function assertElementId(location, statement, msg, id, element) {
  var args = argsWithOptionalMsg_(arguments, 3);
  var actual = args[2] && args[2].id;
  junit.assertCount++;

  if (actual !== args[1]) {
    fail(location, args[0] + 'expected id to be ' + args[1] + ' but was ' + actual);
  }

  return true;
}


function assertInstanceOf(location, statement, msg, constructor, actual) {
  junit.assertCount++;
  var args = argsWithOptionalMsg_(arguments, 3);
  var pretty = prettyPrintEntity_(args[2]);
  var expected = args[1] && args[1].name || args[1];

  if (args[2] == null) {
    fail(location, args[0] + 'expected ' + pretty + ' to be instance of ' + expected);
  }

  if (!(Object(args[2]) instanceof args[1])) {
    fail(location, args[0] + 'expected ' + pretty + ' to be instance of ' + expected);
  }

  return true;
}


function assertNotInstanceOf(location, statement, msg, constructor, actual) {
  var args = argsWithOptionalMsg_(arguments, 3);
  junit.assertCount++;

  if (Object(args[2]) instanceof args[1]) {
    var expected = args[1] && args[1].name || args[1];
    var pretty = prettyPrintEntity_(args[2]);
    fail(location, args[0] + 'expected ' + pretty + ' not to be instance of ' + expected);
  }

  return true;
}

/**
 * Asserts that two doubles, or the elements of two arrays of doubles,
 * are equal to within a positive delta.
 */
function assertEqualsDelta(location, statement, msg, expected, actual, epsilon) {
  var args = this.argsWithOptionalMsg_(arguments, 4);
  junit.assertCount++;
  msg = args[0];
  expected = args[1];
  actual = args[2];
  epsilon = args[3];

  if (!compareDelta_(expected, actual, epsilon)) {
    this.fail(location, msg + 'expected ' + epsilon + ' within ' +
              this.prettyPrintEntity_(expected) +
              ' but was ' + this.prettyPrintEntity_(actual) + '');
  }
  return true;
};

function compareDelta_(expected, actual, epsilon) {
  var compareDouble = function(e,a,d) {
    return Math.abs(e - a) <= d;
  }
  if (expected === actual || (expected != null && expected.equals(actual))) {
    return true;
  }

  if (typeof expected == "number" ||
      typeof actual == "number" ||
      !expected || !actual) {
    return compareDouble(expected, actual, epsilon);
  }

  if (isElement_(expected) || isElement_(actual)) {
    return false;
  }

  var key = null;
  var actualLength   = 0;
  var expectedLength = 0;

  try {
    // If an array is expected the length of actual should be simple to
    // determine. If it is not it is undefined.
    if (junit.isArray(actual)) {
      actualLength = actual.length;
    } else {
      // In case it is an object it is a little bit more complicated to
      // get the length.
      for (key in actual) {
        if (actual.hasOwnProperty(key)) {
          ++actualLength;
        }
      }
    }

    // Arguments object
    if (actualLength == 0 && typeof actual.length == "number") {
      actualLength = actual.length;

      for (var i = 0, l = actualLength; i < l; i++) {
        if (!(i in actual)) {
          actualLength = 0;
          break;
        }
      }
    }

    for (key in expected) {
      if (expected.hasOwnProperty(key)) {
        if (!compareDelta_(expected[key], actual[key], epsilon)) {
          return false;
        }

        ++expectedLength;
      }
    }

    if (expectedLength != actualLength) {
      return false;
    }

    return expectedLength == 0 ? expected.toString() == actual.toString() : true;
  } catch (e) {
    return false;
  }
};

var assert = assertTrue;