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


var fail = function fail(msg) {
  var err = new Error(msg );
  err.name = 'AssertError';
  err.location="";

  if (!err.message) {
    err.message = msg;
  }

  throw err;
};


function isBoolean_(bool) {
  if (typeof(bool) != 'boolean') {
    fail('Not a boolean: ' + prettyPrintEntity_(bool));
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
  for(var i = 0; i < args.length; i++) {
    copyOfArgs.push(args[i]);
  }
  var argsLength = args.length; //remove location and statement
  var min = length - 1;

  if (argsLength < min) {
    fail(args[0], 'expected at least ' + min + ' arguments, got ' + argsLength);
  } else if (argsLength >= length && typeof copyOfArgs[0] === "string") {
    copyOfArgs[0] += ' ';
  } else {
    copyOfArgs.unshift('');
  }
  return copyOfArgs;
}


function assertTrue(msg, actual) {
  var args = argsWithOptionalMsg_(arguments, 2);
  junit.assertCount++;

  isBoolean_(args[1]);
  if (args[1] != true) {
    fail(args[0] + 'expected true but was ' + prettyPrintEntity_(args[1]));
  }
  return true;
}


function assertFalse(msg, actual) {
  var args = argsWithOptionalMsg_(arguments, 2);
  junit.assertCount++;

  isBoolean_(args[1]);
  if (args[1] != false) {
    fail(args[0] + 'expected false but was ' + prettyPrintEntity_(args[1]));
  }
  return true;
}


function assertEquals(msg, expected, actual, delta) {
  var args = argsWithOptionalMsg_(arguments, 3);
  junit.assertCount++;
  msg = args[0];
  expected = args[1];
  actual = args[2];
  delta = args[3];


  if (!compare_(expected, actual, delta)) {
    fail(msg + 'expected ' + prettyPrintEntity_(expected) + ' but was ' +
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


function assertNotEquals(msg, expected, actual) {
  try {
    assertEquals.apply(this, arguments);
  } catch (e) {
    if (e.name == 'AssertError') {
      return true;
    }

    throw e;
  }

  var args = argsWithOptionalMsg_(arguments, 3);

  fail(args[0] + 'expected ' + prettyPrintEntity_(args[1]) +
      ' not to be equal to ' + prettyPrintEntity_(args[2]));
}


function assertSame(msg, expected, actual) {
  var args = argsWithOptionalMsg_(arguments, 3);
  junit.assertCount++;

  if (!isSame_(args[2], args[1])) {
    fail(args[0] + 'expected ' + prettyPrintEntity_(args[1]) + ' but was ' +
        prettyPrintEntity_(args[2]));
  }
  return true;
}


function assertNotSame(msg, expected, actual) {
  var args = argsWithOptionalMsg_(arguments, 3);
  junit.assertCount++;

  if (isSame_(args[2], args[1])) {
    fail(args[0] + 'expected not same as ' + prettyPrintEntity_(args[1]) +
        ' but was ' + prettyPrintEntity_(args[2]));
  }
  return true;
}


function isSame_(expected, actual) {
  return actual === expected;
}


function assertNull(msg, actual) {
  var args = argsWithOptionalMsg_(arguments, 2);
  junit.assertCount++;

  if (args[1] !== null) {
    fail(args[0] + 'expected null but was ' + prettyPrintEntity_(args[1]));
  }
  return true;
}


function assertNotNull(msg, actual) {
  var args = argsWithOptionalMsg_(arguments, 2);
  junit.assertCount++;

  if (args[1] === null) {
    fail(args[0] + 'expected not null but was null');
  }

  return true;
}


function assertUndefined(msg, actual) {
  var args = argsWithOptionalMsg_(arguments, 2);
  junit.assertCount++;

  if (typeof args[1] != 'undefined') {
    fail(args[2] + 'expected undefined but was ' + prettyPrintEntity_(args[1]));
  }
  return true;
}


function assertNotUndefined(msg, actual) {
  var args = argsWithOptionalMsg_(arguments, 2);
  junit.assertCount++;

  if (typeof args[1] == 'undefined') {
    fail(args[0] + 'expected not undefined but was undefined');
  }
  return true;
}


function assertNaN(msg, actual) {
  var args = argsWithOptionalMsg_(arguments, 2);
  junit.assertCount++;

  if (!isNaN(args[1])) {
    fail(args[0] + 'expected to be NaN but was ' + args[1]);
  }

  return true;
}


function assertNotNaN(msg, actual) {
  var args = argsWithOptionalMsg_(arguments, 2);
  junit.assertCount++;

  if (isNaN(args[1])) {
    fail(args[0] + 'expected not to be NaN');
  }

  return true;
}


function assertException(msg, callback, error) {
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
      fail(msg + 'expected to throw ' + error + ' but threw ' + e.name);
    }

    return true;
  }

  fail(msg + 'expected to throw exception');
}


function assertNoException(msg, callback) {
  var args = argsWithOptionalMsg_(arguments, 2);
  junit.assertCount++;

  try {
    args[1]();
  } catch(e) {
    fail(args[0] + 'expected not to throw exception, but threw ' + e.name +
        ' (' + e.message + ')');
  }
}


function assertArray(msg, actual) {
  var args = argsWithOptionalMsg_(arguments, 2);
  junit.assertCount++;

  if (!junit.isArray(args[1])) {
    fail(args[0] + 'expected to be array, but was ' +
        prettyPrintEntity_(args[1]));
  }
}


function assertTypeOf(msg, expected, value) {
  var args = argsWithOptionalMsg_(arguments, 3);
  junit.assertCount++;
  var actual = typeof args[2];

  if (actual != args[1]) {
    fail(args[0] + 'expected to be ' + args[1] + ' but was ' + actual);
  }

  return true;
}


function assertBoolean(msg, actual) {
  var args = argsWithOptionalMsg_(arguments, 2);
  return assertTypeOf(args[0], 'boolean', args[1]);
}


function assertFunction(msg, actual) {
  var args = argsWithOptionalMsg_(arguments, 2);
  return assertTypeOf(args[0], 'function', args[1]);
}


function assertObject(msg, actual) {
  var args = argsWithOptionalMsg_(arguments, 2);
  return assertTypeOf(args[0], 'object', args[1]);
}


function assertNumber(msg, actual) {
  var args = argsWithOptionalMsg_(arguments, 2);
  return assertTypeOf(args[0], 'number', args[1]);
}


function assertString(msg, actual) {
  var args = argsWithOptionalMsg_(arguments, 2);
  return assertTypeOf(args[0], 'string', args[1]);
}


function assertMatch(msg, regexp, actual) {
  var args = argsWithOptionalMsg_(arguments, 3);
  var isUndef = typeof args[2] == 'undefined';
  junit.assertCount++;
  var _undef;

  if (isUndef || !args[1].test(args[2])) {
    actual = (isUndef ? _undef : prettyPrintEntity_(args[2]));
    fail(args[0] + 'expected ' + actual + ' to match ' + args[1]);
  }

  return true;
}


function assertNoMatch(msg, regexp, actual) {
  var args = argsWithOptionalMsg_(arguments, 3);
  junit.assertCount++;

  if (args[1].test(args[2])) {
    fail(args[0] + 'expected ' + prettyPrintEntity_(args[2]) +
        ' not to match ' + args[1]);
  }

  return true;
}


function assertTagName(msg, tagName, element) {
  var args = argsWithOptionalMsg_(arguments, 3);
  var actual = args[2] && args[2].tagName;

  if (String(actual).toUpperCase() != args[1].toUpperCase()) {
    fail(args[0] + 'expected tagName to be ' + args[1] + ' but was ' + actual);
  }
  return true;
}


function assertClassName(msg, className, element) {
  var args = argsWithOptionalMsg_(arguments, 3);
  var actual = args[2] && args[2].className;
  var regexp = new RegExp('(^|\\s)' + args[1] + '(\\s|$)');

  try {
    assertMatch(args[0], regexp, actual);
  } catch (e) {
    actual = prettyPrintEntity_(actual);
    fail(args[0] + 'expected class name to include ' +
        prettyPrintEntity_(args[1]) + ' but was ' + actual);
  }

  return true;
}


function assertElementId(msg, id, element) {
  var args = argsWithOptionalMsg_(arguments, 3);
  var actual = args[2] && args[2].id;
  junit.assertCount++;

  if (actual !== args[1]) {
    fail(args[0] + 'expected id to be ' + args[1] + ' but was ' + actual);
  }

  return true;
}


function assertInstanceOf(msg, constructor, actual) {
  junit.assertCount++;
  var args = argsWithOptionalMsg_(arguments, 3);
  var pretty = prettyPrintEntity_(args[2]);
  var expected = args[1] && args[1].name || args[1];

  if (args[2] == null) {
    fail(args[0] + 'expected ' + pretty + ' to be instance of ' + expected);
  }

  if (!(Object(args[2]) instanceof args[1])) {
    fail(args[0] + 'expected ' + pretty + ' to be instance of ' + expected);
  }

  return true;
}


function assertNotInstanceOf(msg, constructor, actual) {
  var args = argsWithOptionalMsg_(arguments, 3);
  junit.assertCount++;

  if (Object(args[2]) instanceof args[1]) {
    var expected = args[1] && args[1].name || args[1];
    var pretty = prettyPrintEntity_(args[2]);
    fail(args[0] + 'expected ' + pretty + ' not to be instance of ' + expected);
  }

  return true;
}

/**
 * Asserts that two doubles, or the elements of two arrays of doubles,
 * are equal to within a positive delta.
 */
function assertEqualsDelta(msg, expected, actual, epsilon) {
  var args = this.argsWithOptionalMsg_(arguments, 4);
  junit.assertCount++;
  msg = args[0];
  expected = args[1];
  actual = args[2];
  epsilon = args[3];

  if (!compareDelta_(expected, actual, epsilon)) {
    this.fail(msg + 'expected ' + epsilon + ' within ' +
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