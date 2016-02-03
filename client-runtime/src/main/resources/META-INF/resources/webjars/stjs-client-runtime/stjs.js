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

var __stjsGlobal__;
if (typeof Packages === "object" && String(Packages) === "[JavaPackage ]") {
  //Rhino
  __stjsGlobal__ = this;
} else if (typeof global !== 'undefined') {
  //node.js
  __stjsGlobal__ = global;
} else {
  __stjsGlobal__ = window;
}
__stjsGlobal__.stjs = {};

stjs.NOT_IMPLEMENTED = function(){
	throw "This method is not implemented in Javascript.";
};

stjs.JavalikeEquals = function(value){
	if (value == null)
		return false;
	if (value.valueOf)
		return this.valueOf() === value.valueOf();
	return this === value;
};

stjs.JavalikeGetClass = function(){
	return this.constructor;
};

/* String */
String.prototype.$java_equals=stjs.JavalikeEquals;

if (!String.prototype.getBytes) {
	String.prototype.getBytes=stjs.NOT_IMPLEMENTED;
}
if (!String.prototype.getChars) {
	String.prototype.getChars=stjs.NOT_IMPLEMENTED;
}
if (!String.prototype.contentEquals){
	String.prototype.contentEquals=stjs.NOT_IMPLEMENTED;
}

String.prototype.startsWith$String_int=function(start, from){
    return this.substring(from, from + start.length) == start;
}

String.prototype.startsWith$String=function(start) {
    return this.startsWith$String_int(start, 0);
}

if (!String.prototype.endsWith) {
	String.prototype.endsWith=function(end){
		if (end == null)
			return false;
		if (this.length < end.length)
			return false;
		return this.substring(this.length - end.length, this.length) == end;
	}
}
if (!String.prototype.trim) {
	stjs.trimLeftRegExp = /^\s+/;
	stjs.trimRightRegExp = /\s+$/;
	String.prototype.trim = function() {
		return this.replace(stjs.trimLeftRegExp, "").replace(stjs.trimRightRegExp, "");
	}
}
if (!String.prototype.matches){
	String.prototype.matches=function(regexp){
		return this.match("^" + regexp + "$") != null;
	}
}
if (!String.prototype.compareTo){
	String.prototype.compareTo=function(other){
		if (other == null)
			return 1;
		if (this < other)
			return -1;
		if (this == other)
			return 0;
		return 1;
	}
}

if (!String.prototype.compareToIgnoreCase){
	String.prototype.compareToIgnoreCase=function(other){
		if (other == null)
			return 1;
		return this.toLowerCase().compareTo(other.toLowerCase());
	}
}

if (!String.prototype.equalsIgnoreCase){
	String.prototype.equalsIgnoreCase=function(other){
		if (other == null)
			return false;
		return this.toLowerCase() === other.toLowerCase();
	}
}

if (!String.prototype.codePointAt){
	String.prototype.codePointAt=String.prototype.charCodeAt;
}

if (!String.prototype.codePointBefore){
	String.prototype.codePointBefore=stjs.NOT_IMPLEMENTED;
}
if (!String.prototype.codePointCount){
	String.prototype.codePointCount=stjs.NOT_IMPLEMENTED;
}

if (!String.prototype.replaceAll){
	String.prototype.replaceAll=function(regexp, replace){
		return this.replace(new RegExp(regexp, "g"), replace);
	}
}

if (!String.prototype.replaceFirst){
	String.prototype.replaceFirst=function(regexp, replace){
		return this.replace(new RegExp(regexp), replace);
	}
}

String.prototype.replace$CharSequence_CharSequence=function(target, replacement) {
  var splitedParts = this.split(target.toString());
  return splitedParts.join(replacement.toString())
}

String.prototype.indexOf$String=function(str) {
    return this.indexOf(str);
}

String.prototype.indexOf$String_int=function(str, pos) {
    return this.indexOf(str, pos);
}

String.prototype.indexOf$int=stjs.NOT_IMPLEMENTED;
String.prototype.indexOf$int_int=stjs.NOT_IMPLEMENTED;

String.prototype.substring$int=function(startIndex) {
    return this.substring(startIndex, this.length);
}

String.prototype.substring$int_int=function(startIndex, endIndex) {
    return this.substring(startIndex, endIndex);
}

String.prototype.regionMatches$boolean_int_String_int_int=function(ignoreCase, toffset, other, ooffset, len) {
    if (toffset < 0 || ooffset < 0 || other == null || toffset + len > this.length || ooffset + len > other.length)
        return false;
    var s1 = this.substring(toffset, toffset + len);
    var s2 = other.substring(ooffset, ooffset + len);
    return ignoreCase ? s1.equalsIgnoreCase(s2) : s1 === s2;
}

String.prototype.regionMatches$int_String_int_int=function(toffset, other, ooffset, len) {
    return this.regionMatches$boolean_int_String_int_int(true, toffset, other, ooffset, len);
}

if(!String.prototype.contains){
	String.prototype.contains=function(it){
		return this.indexOf(it)>=0;
	};
}

String.prototype.$java_getClass=stjs.JavalikeGetClass;

if(!String.prototype.isEmpty){
	String.prototype.isEmpty=function(it){
		return this.length == 0;
	};
}

if(!String.prototype.toCharArray){
	String.prototype.toCharArray=function(){
		return this.split('');
	};
}

//force valueof to match the Java's behavior
String.valueOf=function(value){
	return new String(value);
};

String.valueOf$Object = function(o) {
    return o == null ? "null" : o.toString();
};
String.valueOf$boolean = function(b) {
    return "" + b;
};
String.valueOf$char = function(c) {
    return "" + c;
};
String.valueOf$int = function(i) {
    return "" + i;
};
String.valueOf$long = function(l) {
    return "" + l;
};
String.valueOf$float = function(f) {
    return "" + f;
};
String.valueOf$double = function(d) {
    return "" + d;
};

String.prototype.getBytes$Charset = function(charset) {
  if (charset != stjs.Java.StandardCharsets.UTF_8) {
    throw "String.prototype.getBytes$Charset: Only UTF-8 is supported";
  }

  var utf8 = [];
  for (var i=0; i < this.length; i++) {
    var charcode = this.charCodeAt(i);
    if (charcode < 0x80) utf8.push(charcode);
    else if (charcode < 0x800) {
      utf8.push(0xc0 | (charcode >> 6),
            0x80 | (charcode & 0x3f));
    }
    else if (charcode < 0xd800 || charcode >= 0xe000) {
      utf8.push(0xe0 | (charcode >> 12),
            0x80 | ((charcode>>6) & 0x3f),
            0x80 | (charcode & 0x3f));
    }
    // surrogate pair
    else {
      i++;
      // UTF-16 encodes 0x10000-0x10FFFF by
      // subtracting 0x10000 and splitting the
      // 20 bits of 0x0-0xFFFFF into two halves
      charcode = 0x10000 + (((charcode & 0x3ff)<<10)
            | (this.charCodeAt(i) & 0x3ff));
      utf8.push(0xf0 | (charcode >>18),
            0x80 | ((charcode>>12) & 0x3f),
            0x80 | ((charcode>>6) & 0x3f),
            0x80 | (charcode & 0x3f));
    }
  }
  return utf8;
}

/* String: as java.lang.CharSequence */
String.prototype.$java_length=function() {
  return this.length;
}

String.prototype.$java_subSequence$int_int=function(start, end) {
  return this.substring(start, end)
}

/* String: static methods */
String.format$String_Array$Object=function(format, argumentArray) {
  return stjs.sprintf.apply(stjs, [format].concat(argumentArray));
}

/* Number */
var Byte=Number;
var Double=Number;
var Float=Number;
var Integer=Number;
var Long=Number;
var Short=Number;

// Primitive hashCode
stjs.primitiveHashCode = function() {
  return this.valueOf();
}

Number.prototype.$java_hashCode = stjs.primitiveHashCode;
String.prototype.$java_hashCode = stjs.primitiveHashCode;
Boolean.prototype.$java_hashCode = function() { return this.valueOf() ? 1231 : 1237; }

Number.parseInt$String=function(str) {
    return parseInt(str);
}

Number.parseInt$String_int=function(str, radix) {
    return parseInt(str, radix);
}

Number.valueOf$String=function(str) {
    return Number.valueOf(Number.parseInt$String(str));
}

Number.valueOf$int=function(value) {
    return Number.valueOf(value);
}

Number.valueOf$String_int=function(str, radix) {
    return Number.valueOf(Number.parseInt$String_int(str, radix));
}

/* type conversion - approximative as Javascript only has integers and doubles */
if (!Number.prototype.intValue) {
	Number.prototype.intValue=function(){
    	return this.valueOf();
	}
}
if (!Number.prototype.shortValue) {
	Number.prototype.shortValue=function(){
		return this.valueOf();
	}
}
if (!Number.prototype.longValue) {
	Number.prototype.longValue=function(){
    	return this.valueOf();
	}
}
if (!Number.prototype.byteValue) {
	Number.prototype.byteValue=function(){
    	return this.valueOf();
	}
}

if (!Number.prototype.floatValue) {
	Number.prototype.floatValue=function(){
    	return this.valueOf();
	}
}

if (!Number.prototype.doubleValue) {
	Number.prototype.doubleValue=function(){
    	return this.valueOf();
	}
}

if (!Number.parseInt) {
	Number.parseInt = parseInt;
}
if (!Number.parseShort) {
	Number.parseShort = parseInt;
}
if (!Number.parseLong) {
	Number.parseLong = parseInt;
}
if (!Number.parseByte) {
	Number.parseByte = parseInt;
}

if (!Number.parseDouble) {
	Number.parseDouble = parseFloat;
}

if (!Number.parseFloat) {
	Number.parseFloat = parseFloat;
}

if (!Number.isNaN) {
	Number.isNaN = isNaN;
}

if (!Number.prototype.isNaN) {
	Number.prototype.isNaN = function() {
		return isNaN(this);
	}
}

Number.prototype.$java_equals=stjs.JavalikeEquals;

Number.prototype.$java_getClass=stjs.JavalikeGetClass;

//force valueof to match approximately the Java's behavior (for Integer.valueOf it returns in fact a double)
Number.valueOf=function(value){
	return new Number(value);
}

Integer.valueOf$int=function(value){
	return new Number(value);
}

Integer.valueOf$String=function(value){
	return new Number(value);
}

Long.valueOf$long=function(value){
	return new Number(value);
}

Long.valueOf$String=function(value){
	return new Number(value);
}

/* Boolean */
Boolean.prototype.$java_equals=stjs.JavalikeEquals;

Boolean.prototype.$java_getClass=stjs.JavalikeGetClass;

if (!Boolean.TRUE) {
    Boolean.TRUE = new Boolean(true);
}
if (!Boolean.FALSE) {
    Boolean.FALSE = new Boolean(false);
}

if (!Boolean.prototype.booleanValue) {
    Boolean.prototype.booleanValue=function(){
        return this.valueOf();
    }
}

//force valueof to match the Java's behavior
Boolean.valueOf$boolean=function(value){
    if (value) {
	    return Boolean.TRUE;
	} else {
	    return Boolean.FALSE;
	}
}
Boolean.valueOf$String=function(value){
    if (value != null && value.toLowerCase() == 'true') {
	    return Boolean.TRUE;
	} else {
	    return Boolean.FALSE;
	}
}

/* Array */
Array.prototype._clone = function() {
  return this.slice();
}

Array.prototype.$java_equals = function(other) {
  if (other == null) {
    return false;
  }

  if (this.length != other.length) {
    return false;
  }

  for (var i = 0; i < this.length; i++) {
    if (this[i] !== other[i]) {
      return false;
    }
  }

  return true;
}

// createJavaArray
// Supports:
//   createJavaArray(3) --> creates a new array of 3 elements initialized with 'null' values
//   createJavaArray(null, "A", "B", "C") --> creates a new array of 3 elements initialized with specified values
stjs.createJavaArray = function() {
    var argsArray = Array.prototype.slice.call(arguments);
    var arraySize = argsArray[0];

    if (argsArray.length == 1) {
        var theArray = new Array(arraySize);
        for (var i = 0; i < arraySize; i++) {
            theArray[i] = null;
        }
        return theArray;
    }
    else {
        var argsWithoutFirst = argsArray.slice(1);

        var theArray = new Array(arraySize);
        for (var i = 0; i < arraySize; i++) {
            theArray[i] = stjs.createJavaArray.apply(null, argsWithoutFirst);
        }

        return theArray;
    }
}

/************* STJS helper functions ***************/
stjs.skipCopy = {"prototype":true, "constructor": true, "$typeDescription":true, "$inherit" : true};

stjs.ns=function(path){
	var p = path.split(".");
	var obj = __stjsGlobal__;
	for(var i = 0; i < p.length; ++i){
		var part = p[i];
		obj = obj[part] = obj[part] || {};
	}
	return obj;
};

stjs.ns("stjs.Java");

stjs.copyProps=function(from, to){
	for(var key in from){
		if (!stjs.skipCopy.hasOwnProperty(key))
			to[key]	= from[key];
	}
	return to;
};

stjs.copyInexistentProps=function(from, to){
	for(var key in from){
		if (!stjs.skipCopy.hasOwnProperty(key) && !to[key])
			to[key]	= from[key];
	}
	return to;
};

stjs.extend=function(_constructor, _super, _implements, _initializer, _typeDescription, _annotations, _className){
	_constructor.$inherit=[];

	if(_super != null){
		// I is used as a no-op constructor that has the same prototype as _super
		// we do this because we cannot predict the result of calling new _super()
		// without parameters (it might throw an exception).
		// Basically, the following 3 lines are a safe equivalent for
		// _constructor.prototype = new _super();
		var I = function(){};
		I.prototype	= _super.prototype;
		_constructor.prototype	= new I();

		// copy static properties for super
		// assign every method from proto instance
		stjs.copyProps(_super, _constructor);
		stjs.copyProps(_super.$typeDescription, _typeDescription);
		stjs.copyProps(_super.$annotations, _annotations);

		//add the super class to inherit array
		_constructor.$inherit.push(_super);
	}

	// copy static properties and default methods from interfaces
	for(var a = 0; a < _implements.length; ++a){
		if (!_implements[a]) continue;
		stjs.copyProps(_implements[a], _constructor);
		stjs.copyInexistentProps(_implements[a].prototype, _constructor.prototype);
		_constructor.$inherit.push(_implements[a]);
	}

	// remember the correct constructor
	_constructor.prototype.constructor	= _constructor;

	// run the initializer to assign all static and instance variables/functions
	if(_initializer != null){
		_initializer(_constructor, _constructor.prototype);
	}

	_constructor.$typeDescription = _typeDescription;
	_constructor.$annotations = _annotations;
	_constructor.$className = _className;

    _constructor._$stjs_objectUid = stjs._nextObjectUid++;
    _constructor.$java_hashCode = function() {
        return this._$stjs_objectUid;
    }

    _constructor.$java_hashCode = function() {
        return this._$stjs_objectUid;
    }

    _constructor.$java_equals = stjs.JavalikeEquals;

    _constructor.$java_getSimpleName = function() {
        var nameParts = this.$className.split(".");
        return nameParts[nameParts.length - 1]
    }

    _constructor.$java_getCanonicalName = function() {
        return this.$className;
    }

    _constructor.$java_getName = function() {
        return this.$className;
    }

	// build package and assign
	return	_constructor;
};


stjs._nextObjectUid = 1;

stjs.Java.Object = function() {
};

stjs.Java.Object = stjs.extend(stjs.Java.Object, null, [], function(constructor, prototype) {

  prototype._constructor = function() {
      return this;
  };

  prototype._$stjs_objectUid = 0;

  prototype.$java_equals = stjs.JavalikeEquals;

  prototype.$java_getClass = stjs.JavalikeGetClass;

  prototype.$java_hashCode = function() {
    if (this._$stjs_objectUid == 0) {
        this._$stjs_objectUid = stjs._nextObjectUid++;
    }
    return this._$stjs_objectUid;
  };

}, {}, {}, "stjs.Java.Object");

/**
 * return type's annotations
 */
stjs.getAnnotations = function(clz) {
	return clz.$annotations;
};

stjs.getTypeAnnotation = function(clz, annType) {
	var ann = clz.$annotations._;
	return ann ? ann[annType]: null;
};

stjs.getMemberAnnotation = function(clz, memberName, annType) {
	var ann = clz.$annotations.memberName;
	return ann ? ann[annType]: null;
};

stjs.getParameterAnnotation = function(clz, methodName, idx, annType) {
	var ann = clz.$annotations[methodName + "$" + idx];
	return ann ? ann[annType]: null;
};

/**
 * checks if an object is an instanceof parentClass.
 */
stjs.isInstanceOf=function(instance, parentClass){
	if (instance == null)
		return false;
	var result = stjs.classInheritsFrom(instance.constructor, parentClass);

	if (!result && parentClass === stjs.Java.CharSequence && typeof(instance) == 'string') {
	    result = true;
	}
    return result;
}

/**
 * checks if a class inherits from parent class. it checks recursively if "parent" is the child itself or it's found somewhere in the $inherit array
 */
stjs.classInheritsFrom=function(childClass, parentClass){
	if (childClass == null)
		return false;
	if (childClass === parentClass)
		return true;
	if (!childClass.$inherit)
		return false;
	for(var i = 0; i < childClass.$inherit.length; ++i){
		if (stjs.classInheritsFrom(childClass.$inherit[i], parentClass)) {
			return true;
		}
	}
	return false;
}

stjs.enumEntry=function(){
};

stjs.enumEntry.prototype._constructor$String_int=function(idx, name){
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
stjs.enumEntry.prototype.$java_equals=stjs.JavalikeEquals;

stjs.enumeration=function(){
	var i;
	var e = {};
	e._values = [];
	for(i = 0; i < arguments.length; ++i){
		e[arguments[i]] = new stjs.enumEntry(i, arguments[i]);
		e._values[i] = e[arguments[i]];
	}
	e.values = function(){return this._values;};
	e.valueOf=function(label){
		return this[label];
	}
	return e;
};

stjs.Java.Enum = function() {};
stjs.extend(stjs.Java.Enum, stjs.Java.Object, [], function(constructor, prototype) {
  prototype._name = null;
  prototype._ordinal = null;
  constructor._values = [];

  constructor.values = function() {
    return this._values;
  };
  constructor.getEnumConstants = function() {
    return this._values;
  }
  constructor.valueOf$String = function() {
    var value = arguments[0];
    // No values to compare with, assume the caller want the value of ourself
    if (!value) {
      return this;
    }
    var matchedValue = null;
    for(var key in this._values){
      var obj = this._values[key];
      if (value === obj.name()) {
        return obj;
      }
    }
    throw new Error("Specified Java.Enum value not found in the enumeration: " + value);
  };

  prototype._constructor$String_int=function(idx, name){
  }

  prototype.name = function() {
    return this._name;
  };
  prototype.ordinal = function() {
    return this._ordinal;
  };
  prototype.toString = function() {
    return this._name;
  };
}, {});

/**
 * if true the execution of generated main methods is disabled.
 * this is useful when executing unit tests, to no have the main methods executing before the tests
 */
stjs.mainCallDisabled = false;

stjs.exception=function(err){
	return err;
};

stjs.isEnum=function(obj){
	return obj != null && obj.constructor == stjs.enumEntry;
};

if (typeof Math.trunc === "function") {
	stjs.trunc = Math.trunc;
} else {
	stjs.trunc=function(n) {
		if (n == null)
			return null;
		return n >= 0 ? Math.floor(n) : Math.ceil(n);
	}
}

stjs.converters = {
	Date : function(s, type) {
		var a = /^(\d{4})-(\d{2})-(\d{2}) (\d{2}):(\d{2}):(\d{2}(?:\.\d*)?)$/
				.exec(s);
		if (a) {
			return new Date(+a[1], +a[2] - 1, +a[3], +a[4], +a[5], +a[6]);
		}
		return null;
	},

	Enum : function(s, type){
		return eval(type.arguments[0])[s];
	}
};


stjs.serializers = {
	Date : function(d, type) {
		function pad(n){
			return n < 10 ? "0" + n : "" + n;
		}
		if (d) {
			return "" + d.getFullYear() + "-" + pad(d.getMonth()+1) + "-" + pad(d.getDate()) + " " + pad(d.getHours()) + ":" + pad(d.getMinutes()) + ":" + pad(d.getSeconds());
		}
		return null;
	},

	Enum : function(e, type){
		return e != null ? e.toString() : null;
	}
};

/**
 * Used to be able to send method references and lambdas that capture 'this' as callbacks.
 * This method has a bunch of different usage patterns:
 *
 * bind(instance, "methodName"):
 *     Used when translating a capturing method reference (eg: instance::methodName), or when translating
 *
 * bind(this, function):
 *     Used when translating a lambda that uses 'this' explicitly or implicitly (eg: () -> this.doSomething(3))
 *
 * bind(this, function, specialTHISparamPosition)
 *     Used when translating a lambda that uses the special all-caps 'THIS' parameter
 *
 * bind("methodName")
 *     Used when translating a non-static method reference (eg: TypeName::methodName, where methodName is non-static)
 */
stjs.bind=function(obj, method, thisParamPos) {
	var useFirstParamAsContext = false;
	if(method == null) {
		// the bind("methodName") is the only usage where the method argument can be null
		method = obj;
		obj = null;
		useFirstParamAsContext = true;
	}
	var addThisToParameters = thisParamPos != null;

	var f = function(){
		var args = arguments;
		if (addThisToParameters) {
			Array.prototype.splice.call(args, thisParamPos, 0, this);
		}
		if(useFirstParamAsContext){
			obj = Array.prototype.shift.call(args);
		}

		if (typeof method === "string") {
			return obj[method].apply(obj, args);

		} else {
			return method.apply(obj, args);
		}
	};
	return f;
};


/** *********** global ************** */
function exception(err){
	return err;
}

function isEnum(obj){
	return obj != null && obj.constructor == stjs.enumEntry;
}

/******* parsing *************/

stjs.parseJSON = stjs.NOT_IMPLEMENTED;

stjs.isArray=function( obj ) {
    return stjs.toString.call(obj) === "[object Array]";
};
stjs.isArray = Array.isArray;

/**
 * cls can by the type of the return.
 * If it's an array it can be either the type of an element or the type definition of the field.
 * TODO - for other collections and classes is not done yet
 */
stjs.typefy=function(obj, cls){
	if (stjs.isArray(obj)){
		var result = [];
		for(var idx = 0; idx < obj.length; idx++){
			result.push(stjs.typefy(obj[idx], elementType(cls)));
		}
		return result;
	}
	 var constructors = {};
	 function constr(name, param){
		  var c = constructors[name];
		  if (!c)
			  constructors[name] = c = eval(name);
		  return new c(param);
	  }

	 function elementType(type){
		 if (typeof type == "function")
			 return type;
		 if (type.arguments) {
			 return eval(type.arguments[0]);
		 }
		 if (typeof type == "string")
			 return eval(type);
		 return Object;
	  }


	function convert(type, json){
		  if (!type)
			  return json;
		  var cv = stjs.converters[type.name || type];
		  if (cv)
			  return cv(json, type);
		  //hopefully the type has a string constructor
		 return constr(type, json);
	  }

	 function builder(type){
		  if (!type)
			  return {};
			if (typeof type == "function")
				return new type();
			if (type.name) {
				if (type.name == "Map")
					return {};
				if (type.name == "Array")
					return [];
				return constr(type.name);
			}
			return constr(type);
	  }

	  if (obj == null)
		  return null;

	  var ret = new cls();
	  for(var key in obj){
		  var prop = obj[key];
		  if (prop == null)
			  continue;
		  var td = cls.$typeDescription[key];
		  if (!td) {
			  ret[key] = prop;
			  continue;
		  }
		  if (typeof prop == "string")
			  ret[key] = convert(td, prop);
		  else if (typeof prop == "object")
			  ret[key] = stjs.typefy(prop, td);
	  }
	  return ret;
};
stjs.hydrate=stjs.typefy

stjs.stringify=function(obj, cls){
	 if (obj == null)
		  return null;

	 var ret = {};
	  for(var key in obj){
		  var td = cls.$typeDescription[key];
		  var prop = obj[key];
		  var ser = td != null ? stjs.serializers[td.name || td] : null;

		  if (typeof prop == "function")
			  continue;

		  if (!td || !ser) {
			  ret[key] = prop;
			  continue;
		  }
		  if (typeof prop != "string")
			  if (ser)
				  ret[key] = ser(prop, td);
			  else
				  ret[key] = stjs.typefy(prop, td);
	  }
	  return ret;
};
/************* STJS asserts ***************/
stjs.assertHandler = function(position, code, msg) {
	throw msg + " at " + position;
};

stjs.STJSAssert = {};

stjs.STJSAssert.setAssertHandler = function(a) {
	stjs.assertHandler = a;
}

stjs.STJSAssert.assertArgEquals = function(position, code, expectedValue, testValue) {
	if (expectedValue != testValue && stjs.assertHandler)
		stjs.assertHandler(position, code, "Wrong argument. Expected: " + expectedValue + ", got:" + testValue);
}

stjs.STJSAssert.assertArgNotNull = function(position, code, testValue) {
	if (testValue == null && stjs.assertHandler)
		stjs.assertHandler(position, code, "Wrong argument. Null value");
}

stjs.STJSAssert.assertArgTrue = function(position, code, condition) {
	if (!condition && stjs.assertHandler)
		stjs.assertHandler(position, code, "Wrong argument. Condition is false");
}

stjs.STJSAssert.assertStateEquals = function(position, code, expectedValue, testValue) {
	if (expectedValue != testValue && stjs.assertHandler)
		stjs.assertHandler(position, code, "Wrong state. Expected: " + expectedValue + ", got:" + testValue);
}

stjs.STJSAssert.assertStateNotNull = function(position, code, testValue) {
	if (testValue == null && stjs.assertHandler)
		stjs.assertHandler(position, code, "Wrong state. Null value");
}

stjs.STJSAssert.assertStateTrue = function(position, code, condition) {
	if (!condition && stjs.assertHandler)
		stjs.assertHandler(position, code, "Wrong state. Condition is false");
}
/** exceptions **/
stjs.Java.Throwable = function(message, cause){
	Error.call(this);
	if(typeof Error.captureStackTrace === 'function'){
		// nice way to capture the stack trace for chrome
		Error.captureStackTrace(this, arguments.callee);
	} else {
		// alternate way to capture the stack trace for other browsers
		try{
			throw new Error();
		}catch(e){
			this.stack = e.stack;
		}
	}
	if (typeof message === "string"){
		this.detailMessage  = message;
		this.message = message;
		this.cause = cause;
	} else {
		this.cause = message;
	}
};
stjs.extend(stjs.Java.Throwable, Error, [], function(constructor, prototype){
	prototype.detailMessage = null;
	prototype.cause = null;

    prototype._constructor = function() {
        return this;
    };
    prototype._constructor$String = function(message) {
        this.detailMessage = message;
        return this;
    };
    prototype._constructor$String_Throwable = function(message, cause) {
        this.detailMessage = message;
        this.cause = cause;
        return this;
    };
    prototype._constructor$Throwable = function(cause) {
        this.cause = cause;
        return this;
    };
	prototype.getMessage = function() {
        return this.detailMessage;
    };

	prototype.getLocalizedMessage = function() {
        return this.getMessage();
    };

	prototype.getCause = function() {
        return (this.cause==this ? null : this.cause);
    };

	prototype.toString = function() {
	        var s = "Exception";//TODO should get the exception's type name here
	        var message = this.getLocalizedMessage();
	        return (message != null) ? (s + ": " + message) : s;
	 };

	 prototype.getStackTrace = function() {
		 return this.stack;
	 };

	 prototype.printStackTrace = function(){
		 console.error(this.getStackTrace());
	 };
}, {});

stjs.Java.Exception = function(message, cause){
	stjs.Java.Throwable.call(this, message, cause);
};
stjs.extend(stjs.Java.Exception, stjs.Java.Throwable, [], function(constructor, prototype){
    prototype._constructor = function() {
        stjs.Java.Throwable.call(this);
        return this;
    };
    prototype._constructor$String = function(detailMessage) {
        stjs.Java.Throwable.call(this, detailMessage);
        return this;
    };
    prototype._constructor$String_Throwable = function(detailMessage, throwable) {
        stjs.Java.Throwable.call(this, detailMessage, throwable);
        return this;
    };
    prototype._constructor$Throwable = function(throwable) {
        stjs.Java.Throwable.call(this, throwable);
        return this;
    };
}, {});

stjs.Java.RuntimeException = function(message, cause){
	stjs.Java.Exception.call(this, message, cause);
};
stjs.extend(stjs.Java.RuntimeException, stjs.Java.Exception, [], function(constructor, prototype){
    prototype._constructor = function() {
        stjs.Java.Exception.call(this);
        return this;
    };
    prototype._constructor$String = function(detailMessage) {
        stjs.Java.Exception.call(this, detailMessage);
        return this;
    };
    prototype._constructor$String_Throwable = function(detailMessage, throwable) {
        stjs.Java.Exception.call(this, detailMessage, throwable);
        return this;
    };
    prototype._constructor$Throwable = function(throwable) {
        stjs.Java.Exception.call(this, throwable);
        return this;
    };
    prototype._constructor$String_Throwable_boolean_boolean = function(message, cause, enableSuppression, writableStackTrace) {
        stjs.Java.Exception.call(this, message, cause, enableSuppression, writableStackTrace);
        return this;
    };
}, {});

stjs.Java.Iterator = function() {};
stjs.Java.Iterator = stjs.extend(stjs.Java.Iterator, null, [], function(constructor, prototype) {
    prototype.hasNext = function() {};
    prototype.next = function() {};
    prototype.remove = function() {};
}, {}, {});

stjs.Java.Iterable = function() {};
stjs.Java.Iterable = stjs.extend(stjs.Java.Iterable, null, [], function(constructor, prototype) {
    prototype.iterator = function() {};
}, {}, {});

/** stjs field manipulation */
stjs.setField=function(obj, field, value, returnOldValue){
	if (stjs.setFieldHandler)
		return stjs.setFieldHandler(obj, field, value, returnOldValue);
	var toReturn = returnOldValue ? obj[field] : value;
	obj[field] = value;
	return toReturn;
};

stjs.getField=function(obj, field){
	if (stjs.getFieldHandler)
		return stjs.getFieldHandler(obj, field);
	return obj[field];
};

/* java.lang.CharSequence */
stjs.Java.CharSequence = function () {};
stjs.Java.CharSequence = stjs.extend(stjs.Java.CharSequence, stjs.Java.Object, [], function(constructor, prototype) {
    prototype.$java_length = function() {};
    prototype.$java_charAt = function(index) {};
    prototype.$java_subSequence = function(start, end) {};
    prototype.toString = function() {};
}, {}, {}, "stjs.Java.CharSequence");

/* java.nio.Charset */
stjs.Java.Charset = function() {};
stjs.Java.Charset = stjs.extend(stjs.Java.Charset, null, [], function(constructor, prototype) {
}, {}, {});

/* java.nio.StandardCharsets */
stjs.Java.StandardCharsets = function() {};
stjs.Java.StandardCharsets = stjs.extend(stjs.Java.StandardCharsets, null, [], function(constructor, prototype) {
  constructor.UTF_8 = new stjs.Java.Charset();
}, {UTF_8: 'Charset'}, {});


/* ********************************************************************************************
   sprintf-js
       Alexandru Marasteanu <hello@alexei.ro> (http://alexei.ro/)
   Licence:
       BSD-3-Clause
   Source:
        https://github.com/alexei/sprintf.js/blob/master/src/sprintf.js
******************************************************************************************** */
(function(window) {
    var re = {
        not_string: /[^s]/,
        not_bool: /[^t]/,
        not_type: /[^T]/,
        not_primitive: /[^v]/,
        number: /[diefg]/,
        numeric_arg: /bcdiefguxX/,
        json: /[j]/,
        not_json: /[^j]/,
        text: /^[^\x25]+/,
        modulo: /^\x25{2}/,
        placeholder: /^\x25(?:([1-9]\d*)\$|\(([^\)]+)\))?(\+)?(0|'[^$])?(-)?(\d+)?(?:\.(\d+))?([b-gijostTuvxX])/,
        key: /^([a-z_][a-z_\d]*)/i,
        key_access: /^\.([a-z_][a-z_\d]*)/i,
        index_access: /^\[(\d+)\]/,
        sign: /^[\+\-]/
    }

    function sprintf() {
        var key = arguments[0], cache = sprintf.cache
        if (!(cache[key] && cache.hasOwnProperty(key))) {
            cache[key] = sprintf.parse(key)
        }
        return sprintf.format.call(null, cache[key], arguments)
    }

    sprintf.format = function(parse_tree, argv) {
        var cursor = 1, tree_length = parse_tree.length, node_type = '', arg, output = [], i, k, match, pad, pad_character, pad_length, is_positive = true, sign = ''
        for (i = 0; i < tree_length; i++) {
            node_type = get_type(parse_tree[i])
            if (node_type === 'string') {
                output[output.length] = parse_tree[i]
            }
            else if (node_type === 'array') {
                match = parse_tree[i] // convenience purposes only
                if (match[2]) { // keyword argument
                    arg = argv[cursor]
                    for (k = 0; k < match[2].length; k++) {
                        if (!arg.hasOwnProperty(match[2][k])) {
                            throw new Error(sprintf('[sprintf] property "%s" does not exist', match[2][k]))
                        }
                        arg = arg[match[2][k]]
                    }
                }
                else if (match[1]) { // positional argument (explicit)
                    arg = argv[match[1]]
                }
                else { // positional argument (implicit)
                    arg = argv[cursor++]
                }

                if (re.not_type.test(match[8]) && re.not_primitive.test(match[8]) && get_type(arg) == 'function') {
                    arg = arg()
                }

                if (re.numeric_arg.test(match[8]) && (get_type(arg) != 'number' && isNaN(arg))) {
                    throw new TypeError(sprintf("[sprintf] expecting number but found %s", get_type(arg)))
                }

                if (re.number.test(match[8])) {
                    is_positive = arg >= 0
                }

                switch (match[8]) {
                    case 'b':
                        arg = parseInt(arg, 10).toString(2)
                    break
                    case 'c':
                        arg = String.fromCharCode(parseInt(arg, 10))
                    break
                    case 'd':
                    case 'i':
                        arg = parseInt(arg, 10)
                    break
                    case 'j':
                        arg = JSON.stringify(arg, null, match[6] ? parseInt(match[6]) : 0)
                    break
                    case 'e':
                        arg = match[7] ? parseFloat(arg).toExponential(match[7]) : parseFloat(arg).toExponential()
                    break
                    case 'f':
                        arg = match[7] ? parseFloat(arg).toFixed(match[7]) : parseFloat(arg)
                    break
                    case 'g':
                        arg = match[7] ? parseFloat(arg).toPrecision(match[7]) : parseFloat(arg)
                    break
                    case 'o':
                        arg = arg.toString(8)
                    break
                    case 's':
                        arg = String(arg)
                        arg = (match[7] ? arg.substring(0, match[7]) : arg)
                    break
                    case 't':
                        arg = String(!!arg)
                        arg = (match[7] ? arg.substring(0, match[7]) : arg)
                    break
                    case 'T':
                        arg = get_type(arg)
                        arg = (match[7] ? arg.substring(0, match[7]) : arg)
                    break
                    case 'u':
                        arg = parseInt(arg, 10) >>> 0
                    break
                    case 'v':
                        arg = arg.valueOf()
                        arg = (match[7] ? arg.substring(0, match[7]) : arg)
                    break
                    case 'x':
                        arg = parseInt(arg, 10).toString(16)
                    break
                    case 'X':
                        arg = parseInt(arg, 10).toString(16).toUpperCase()
                    break
                }
                if (re.json.test(match[8])) {
                    output[output.length] = arg
                }
                else {
                    if (re.number.test(match[8]) && (!is_positive || match[3])) {
                        sign = is_positive ? '+' : '-'
                        arg = arg.toString().replace(re.sign, '')
                    }
                    else {
                        sign = ''
                    }
                    pad_character = match[4] ? match[4] === '0' ? '0' : match[4].charAt(1) : ' '
                    pad_length = match[6] - (sign + arg).length
                    pad = match[6] ? (pad_length > 0 ? str_repeat(pad_character, pad_length) : '') : ''
                    output[output.length] = match[5] ? sign + arg + pad : (pad_character === '0' ? sign + pad + arg : pad + sign + arg)
                }
            }
        }
        return output.join('')
    }

    sprintf.cache = {}

    sprintf.parse = function(fmt) {
        var _fmt = fmt, match = [], parse_tree = [], arg_names = 0
        while (_fmt) {
            if ((match = re.text.exec(_fmt)) !== null) {
                parse_tree[parse_tree.length] = match[0]
            }
            else if ((match = re.modulo.exec(_fmt)) !== null) {
                parse_tree[parse_tree.length] = '%'
            }
            else if ((match = re.placeholder.exec(_fmt)) !== null) {
                if (match[2]) {
                    arg_names |= 1
                    var field_list = [], replacement_field = match[2], field_match = []
                    if ((field_match = re.key.exec(replacement_field)) !== null) {
                        field_list[field_list.length] = field_match[1]
                        while ((replacement_field = replacement_field.substring(field_match[0].length)) !== '') {
                            if ((field_match = re.key_access.exec(replacement_field)) !== null) {
                                field_list[field_list.length] = field_match[1]
                            }
                            else if ((field_match = re.index_access.exec(replacement_field)) !== null) {
                                field_list[field_list.length] = field_match[1]
                            }
                            else {
                                throw new SyntaxError("[sprintf] failed to parse named argument key")
                            }
                        }
                    }
                    else {
                        throw new SyntaxError("[sprintf] failed to parse named argument key")
                    }
                    match[2] = field_list
                }
                else {
                    arg_names |= 2
                }
                if (arg_names === 3) {
                    throw new Error("[sprintf] mixing positional and named placeholders is not (yet) supported")
                }
                parse_tree[parse_tree.length] = match
            }
            else {
                throw new SyntaxError("[sprintf] unexpected placeholder")
            }
            _fmt = _fmt.substring(match[0].length)
        }
        return parse_tree
    }

    var vsprintf = function(fmt, argv, _argv) {
        _argv = (argv || []).slice(0)
        _argv.splice(0, 0, fmt)
        return sprintf.apply(null, _argv)
    }

    /**
     * helpers
     */
    function get_type(variable) {
        if (typeof variable === 'number') {
            return 'number'
        }
        else if (typeof variable === 'string') {
            return 'string'
        }
        else {
            return Object.prototype.toString.call(variable).slice(8, -1).toLowerCase()
        }
    }

    var preformattedPadding = {
        '0': ['', '0', '00', '000', '0000', '00000', '000000', '0000000'],
        ' ': ['', ' ', '  ', '   ', '    ', '     ', '      ', '       '],
        '_': ['', '_', '__', '___', '____', '_____', '______', '_______'],
    }
    function str_repeat(input, multiplier) {
        if (multiplier >= 0 && multiplier <= 7 && preformattedPadding[input]) {
            return preformattedPadding[input][multiplier]
        }
        return Array(multiplier + 1).join(input)
    }

    window.sprintf = sprintf
    window.vsprintf = vsprintf

    if (typeof define === 'function' && define.amd) {
        define(function() {
            return {
                sprintf: sprintf,
                vsprintf: vsprintf
            }
        })
    }
})(stjs);
