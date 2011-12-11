/*
 *  Copyright 2011 Alexandru Craciun, Eyal Kaspi
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
/**** Functionality in Java, but not in JS ********
 * methods added to JS prototypes
 */

/* String */
/*
codePointAt(int)
codePointBefore(int)
codePointCount(int, int)
compareTo(String)
compareToIgnoreCase(String)
contentEquals(StringBuffer)
endsWith(String)
equalsIgnoreCase(String)
getBytes()
getBytes(int, int, byte[], int)
getBytes(String)
getBytes(Charset)
getChars(char[], int)
getChars(int, int, char[], int)
matches(String)
regionMatches(boolean, int, String, int, int)
regionMatches(int, String, int, int)
replaceFirst(String, String)
replaceAll(String, String)
startsWith(String)
startsWith(String, int)
trim()
*/

/* Number */

/************* STJS helper functions ***************/
var stjs={};

stjs.skipCopy = {"prototype":true, "constructor": true, "$typeDescription":true};

stjs.copyProps=function(from, to){
	for(key in from){
		if (!stjs.skipCopy[key])
			to[key]	= from[key];
	}
	return to;
};

stjs.extend=function( _constructor,  _super, _implements){
	var key, a;
	var I = function(){};
	I.prototype	= _super.prototype;
	_constructor.prototype	= new I();

	//copy static properties for super and interfaces
	// assign every method from proto instance
	for(a = 1; a < arguments.length; ++a){
		stjs.copyProps(arguments[a], _constructor);
	}
	// remember the correct constructor
	_constructor.prototype.constructor	= _constructor;

	// build package and assign
	return	_constructor;
};

stjs.enumEntry=function(idx, name){
	this._name = name;
	this._ordinal = idx;
};

stjs.enumEntry.prototype.name=function(){
	return this._name;
};
stjs.enumEntry.prototype.ordinal=function(){
	return this._ordinal;
};
stjs.enumEntry.prototype.toString=function(){
	return this._name;
};

stjs.enumeration=function(){
	var i;
	var e = {};
	e._values = [];
	for(i = 0; i < arguments.length; ++i){
		e[arguments[i]] = new stjs.enumEntry(i, arguments[i]);
		e._values[i] = e[arguments[i]];
	}
	e.values = function(){return this._values;};
	return e;
};

/**
 * if true the execution of generated main methods is disabled.
 * this is useful when executing unit tests, to no have the main methods executing before the tests
 */
stjs.mainCallDisabled = false;

stjs.exception=function(err){
	return err;
}

stjs.isEnum=function(obj){
	return obj != null && obj.constructor == stjs.enumEntry;
}

stjs.converters = {
	Date : function(s, type) {
		var a = /^(\d{4})-(\d{2})-(\d{2}) (\d{2}):(\d{2}):(\d{2}(?:\.\d*)?)$/
				.exec(s);
		if (a) {
			return new Date(Date.UTC(+a[1], +a[2] - 1, +a[3], +a[4], +a[5],
					+a[6]));
		}
		return null;
	},

	Enum : function(s, type){
		return eval(type.arguments[0])[s];
	}
};
/** *********** global ************** */
function exception(err){
	return err;
}

function isEnum(obj){
	return obj != null && obj.constructor == stjs.enumEntry;
}

/******* parsing *************/

/**
 * parse a json string using the type definition to build a typed object hierarchy
 */
stjs.parseJSON = (function () {
	  var number
	      = '(?:-?\\b(?:0|[1-9][0-9]*)(?:\\.[0-9]+)?(?:[eE][+-]?[0-9]+)?\\b)';
	  var oneChar = '(?:[^\\0-\\x08\\x0a-\\x1f\"\\\\]'
	      + '|\\\\(?:[\"/\\\\bfnrt]|u[0-9A-Fa-f]{4}))';
	  var string = '(?:\"' + oneChar + '*\")';

	  // Will match a value in a well-formed JSON file.
	  // If the input is not well-formed, may match strangely, but not in an unsafe
	  // way.
	  // Since this only matches value tokens, it does not match whitespace, colons,
	  // or commas.
	  var jsonToken = new RegExp(
	      '(?:false|true|null|[\\{\\}\\[\\]]'
	      + '|' + number
	      + '|' + string
	      + ')', 'g');

	  // Matches escape sequences in a string literal
	  var escapeSequence = new RegExp('\\\\(?:([^u])|u(.{4}))', 'g');

	  // Decodes escape sequences in object literals
	  var escapes = {
	    '"': '"',
	    '/': '/',
	    '\\': '\\',
	    'b': '\b',
	    'f': '\f',
	    'n': '\n',
	    'r': '\r',
	    't': '\t'
	  };
	  function unescapeOne(_, ch, hex) {
	    return ch ? escapes[ch] : String.fromCharCode(parseInt(hex, 16));
	  }

	  function convert(type, json){
		  if (!type)
			  return json;
		  var cv = stjs.converters[type.name || type];
		  if (cv)
			  return cv(json, type);
		  //hopefully the type has a string constructor
		 return eval("new " + type + "('" + json.replace("'", "\'") + "')");
	  }

	  function builder(type){
		  if (!type)
			  return {};
			if (typeof type == "function")
				return new type();
			if (type.name) {
				if (type.name == "Map")
					return {};
				if (type.name == "Map")
					return [];
				return eval("new " + type.name + "()");
			}
			return eval("new " + type + "()");
	  }

	  // A non-falsy value that coerces to the empty string when used as a key.
	  var EMPTY_STRING = new String('');
	  var SLASH = '\\';

	  // Constructor to use based on an open token.
	  var firstTokenCtors = { '{': Object, '[': Array };

	  var hop = Object.hasOwnProperty;

	  return function (json, type) {
	    // Split into tokens
	    var toks = json.match(jsonToken);
	    // Construct the object to return
	    var result;
	    var tok = toks[0];
	    var topLevelPrimitive = false;
	    if ('{' === tok) {
	      result = builder(type, null);
	    } else if ('[' === tok) {
	      result = [];
	    } else {
	      // The RFC only allows arrays or objects at the top level, but the JSON.parse
	      // defined by the EcmaScript 5 draft does allow strings, booleans, numbers, and null
	      // at the top level.
	      result = [];
	      topLevelPrimitive = true;
	    }

	    // If undefined, the key in an object key/value record to use for the next
	    // value parsed.
	    var key;
	    // Loop over remaining tokens maintaining a stack of uncompleted objects and
	    // arrays.
	    var stack = [result];
	    var stack2 = [type];
	    for (var i = 1 - topLevelPrimitive, n = toks.length; i < n; ++i) {
	      tok = toks[i];

	      var cont;
	      switch (tok.charCodeAt(0)) {
	        default:  // sign or digit
	          cont = stack[0];
	          cont[key || cont.length] = +(tok);
	          key = void 0;
	          break;
	        case 0x22:  // '"'
	          tok = tok.substring(1, tok.length - 1);
	          if (tok.indexOf(SLASH) !== -1) {
	            tok = tok.replace(escapeSequence, unescapeOne);
	          }
	          cont = stack[0];
	          if (!key) {
	            if (cont instanceof Array) {
	              key = cont.length;
	            } else {
	              key = tok || EMPTY_STRING;  // Use as key for next value seen.
	              stack2[0] = cont.constructor.$typeDescription ? cont.constructor.$typeDescription[key] : stack2[1].arguments[1];
	              break;
	            }
	          }
	          cont[key] = convert(stack2[0],tok);
	          key = void 0;
	          break;
	        case 0x5b:  // '['
	          cont = stack[0];
	          stack.unshift(cont[key || cont.length] = []);
	          stack2.unshift(stack2[0].arguments[0]);
	          //put the element type desc
	          key = void 0;
	          break;
	        case 0x5d:  // ']'
	          stack.shift();
	          stack2.shift();
	          break;
	        case 0x66:  // 'f'
	          cont = stack[0];
	          cont[key || cont.length] = false;
	          key = void 0;
	          break;
	        case 0x6e:  // 'n'
	          cont = stack[0];
	          cont[key || cont.length] = null;
	          key = void 0;
	          break;
	        case 0x74:  // 't'
	          cont = stack[0];
	          cont[key || cont.length] = true;
	          key = void 0;
	          break;
	        case 0x7b:  // '{'
	          cont = stack[0];
	          stack.unshift(cont[key || cont.length] = builder(stack2[0]));
	          stack2.unshift(null);
	          key = void 0;
	          break;
	        case 0x7d:  // '}'
	          stack.shift();
	          stack2.shift();
	          break;
	      }
	    }
	    // Fail if we've got an uncompleted object.
	    if (topLevelPrimitive) {
	      if (stack.length !== 1) { throw new Error(); }
	      result = result[0];
	    } else {
	      if (stack.length) { throw new Error(); }
	    }

	    return result;
	  };
})();

/************* STJS asserts ***************/
var stjsAssertHandler = function(position, code, msg) {
	throw msg + " at " + position;
}
function setAssertHandler(a) {
	stjsAssertHandler = a;
}

function assertArgEquals(position, code, expectedValue, testValue) {
	if (expepectedValue != testValue && stjsAssertHandler)
		stjsAssertHandler(position, code, "Wrong argument. Expected: " + expectedValue + ", got:" + testValue);
}

function assertArgNotNull(position, code, testValue) {
	if (testValue == null && stjsAssertHandler)
		stjsAssertHandler(position, code, "Wrong argument. Null value");
}

function assertArgTrue(position, code, condition) {
	if (!condition && stjsAssertHandler)
		stjsAssertHandler(position, code, "Wrong argument. Condition is false");
}

function assertStateEquals(position, code, expectedValue, testValue) {
	if (expepectedValue != testValue && stjsAssertHandler)
		stjsAssertHandler(position, code, "Wrong state. Expected: " + expectedValue + ", got:" + testValue);
}

function assertStateNotNull(position, code, testValue) {
	if (testValue == null && stjsAssertHandler)
		stjsAssertHandler("Wrong state. Null value");
}

function assertStateTrue(position, code, condition) {
	if (!condition && stjsAssertHandler)
		stjsAssertHandler(position, code, "Wrong state. Condition is false");
}
