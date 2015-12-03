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

// we use this format to explicitly "export" global variables in the eyes of closure compiler

/* Number */
window["stjs"] = (function (global) {

  var _NOT_IMPLEMENTED = function () {
    throw "This method is not implemented in Javascript.";
  };

  /** @return boolean */
  var _JavalikeEquals = function (value) {
    if (value == null)
      return false;
    if (value.valueOf)
      return this.valueOf() === value.valueOf();
    return this === value;
  };

  var _JavalikeGetClass = function () {
    return this.constructor;
  };

  var _copyInexistentProps = function (from, to) {
    for (var key in from) {
      //noinspection JSUnfilteredForInLoop
      if (!_skipCopy[key] && !to[key]) {
        //noinspection JSUnfilteredForInLoop
        to[key] = from[key];
      }
    }
    return to;
  };

  var _INHERIT = "$inherit";
  var _TYPEDESCRIPTION = "$typeDescription";
  var _ANNOTATIONS = "$annotations";

  /* String */
  var _augmentedStringPrototype = {
    "equals": _JavalikeEquals,
    "getClass": _JavalikeGetClass,
    "getBytes": _NOT_IMPLEMENTED,
    "getChars": _NOT_IMPLEMENTED,
    "contentEquals": _NOT_IMPLEMENTED,
    "codePointBefore": _NOT_IMPLEMENTED,
    "codePointCount": _NOT_IMPLEMENTED,
    "codePointAt": String.prototype.charCodeAt,

    "startsWith": function (start, from) {
      var f = from != null ? from : 0;
      return this.substring(f, f + start.length) == start;
    },

    "endsWith": function (end) {
      if (end == null)
        return false;
      if (this.length < end.length)
        return false;
      return this.substring(this.length - end.length, this.length) == end;
    },

    "trim": function () {
      return this.replace(/^\s+/, "").replace(/\s+$/, "");
    },

    "matches": function (regexp) {
      return this.match(new RegExp("^" + regexp + "$")) != null;
    },

    "compareTo": function (other) {
      if (other == null)
        return 1;
      if (this < other)
        return -1;
      if (this == other)
        return 0;
      return 1;
    },

    "compareToIgnoreCase": function (other) {
      if (other == null)
        return 1;
      return this.toLowerCase().compareTo(other.toLowerCase());
    },

    "equalsIgnoreCase": function (other) {
      if (other == null)
        return false;
      return this.toLowerCase() === other.toLowerCase();
    },

    "replaceAll": function (regexp, replace) {
      return this.replace(new RegExp(regexp, "g"), replace);
    },

    "replaceFirst": function (regexp, replace) {
      return this.replace(new RegExp(regexp), replace);
    },

    "regionMatches": function (ignoreCase, toffset, other, ooffset, len) {
      if (arguments.length == 4) {
        len = arguments[3];
        ooffset = arguments[2];
        other = arguments[1];
        toffset = arguments[0];
        ignoreCase = false;
      }
      if (toffset < 0 || ooffset < 0 || other == null || toffset + len > this.length || ooffset + len > other.length)
        return false;
      var s1 = this.substring(toffset, toffset + len);
      var s2 = other.substring(ooffset, ooffset + len);
      return ignoreCase ? s1.equalsIgnoreCase(s2) : s1 === s2;
    },

    "contains": function (it) {
      return this.indexOf(it) >= 0;
    }

  };
  _copyInexistentProps(_augmentedStringPrototype, String.prototype);

  /**
   * force valueof to match the Java's behavior
   */
  String["valueOf"] = function (value) {
    return String(value);
  };

  var _parseInt = function () {
    return parseInt(this);
  };

  var _parseFloat = function () {
    return parseFloat(this);
  };

  var _augmentedNumberPrototype = {
    /* type conversion - approximative as Javascript only has integers and doubles */
    "equals": _JavalikeEquals,
    "getClass": _JavalikeGetClass,
    "intValue": _parseInt,
    "shortValue": _parseInt,
    "longValue": _parseInt,
    "byteValue": _parseInt,
    "floatValue": _parseFloat,
    "doubleValue": _parseFloat,
    "isNaN": function () {
      return isNaN(this);
    }
  };
  _copyInexistentProps(_augmentedNumberPrototype, Number.prototype);

  var _augmentedNumberConstructor = {
    "parseInt": parseInt,
    "parseShort": parseInt,
    "parseLong": parseInt,
    "parseByte": parseInt,
    "parseDouble": parseFloat,
    "parseFloat": parseFloat,
    "isNaN": isNaN
  };
  _copyInexistentProps(_augmentedNumberConstructor, Number);

  /** force valueof to match approximately the Java's behavior (for Integer.valueOf it returns in fact a double) */
  Number["valueOf"] = function (value) {
    return Number(value).valueOf();
  };


  /* Boolean */
  var _augmentedBooleanPrototype = {
    "equals": _JavalikeEquals,
    "getClass": _JavalikeGetClass
  };
  _copyInexistentProps(_augmentedBooleanPrototype, Boolean.prototype);

  /** force valueof to match the Java's behavior */
  Boolean["valueOf"] = function (value) {
    return Boolean(value).valueOf();
  };


  /************* STJS helper functions ***************/
  var _skipCopy = {"prototype": true, "constructor": true, "$typeDescription": true, "$inherit": true};

  var ns = function (path) {
    var p = path.split(".");
    var obj = global;
    for (var i = 0; i < p.length; ++i) {
      var part = p[i];
      obj = obj[part] = obj[part] || {};
    }
    return obj;
  };

  var _copyProps = function (from, to) {
    for (var key in from) {
      //noinspection JSUnfilteredForInLoop
      if (!_skipCopy[key]) {
        //noinspection JSUnfilteredForInLoop
        to[key] = from[key];
      }
    }
    return to;
  };

  /**
   * 1.2 and earlier version of stjs.extend. Included for backwards compatibility
   */
  var _extend12 = function (_constructor, _super, _implements) {
    var I = function () {
    };
    I.prototype = _super.prototype;
    _constructor.prototype = new I();

    //copy static properties for super and interfaces
    // assign every method from proto instance
    for (var a = 1; a < arguments.length; ++a) {
      _copyProps(arguments[a], _constructor);
    }
    // remember the correct constructor
    _constructor.prototype.constructor = _constructor;

    // add the default equals method if we don't have a superclass. Code generated with version 1.2 will
    // override this method is equals() is present in the original java code.
    // this was not part of the original 1.2 version of extends, however forward compatibility
    // with 1.3 requires it
    if (_super == null) {
      _constructor.prototype.equals = _JavalikeEquals;
      _constructor.prototype.getClass = _JavalikeGetClass;
    }

    // build package and assign
    return _constructor;
  };

  var extend = function (_constructor, _super, _implements, _initializer, _typeDescription, _annotations) {
    if (typeof(_typeDescription) !== "object") {
      // stjs 1.3+ always passes an non-null object to _typeDescription => The code calling stjs.extend
      // was generated with version 1.2 or earlier, so let's call the 1.2 version of stjs.extend
      return _extend12.apply(this, arguments);
    }

    _constructor[_INHERIT] = [];

    if (_super != null) {
      // I is used as a no-op constructor that has the same prototype as _super
      // we do this because we cannot predict the result of calling new _super()
      // without parameters (it might throw an exception).
      // Basically, the following 3 lines are a safe equivalent for
      // _constructor.prototype = new _super();
      var I = function () {
      };
      I.prototype = _super.prototype;
      _constructor.prototype = new I();

      // copy static properties for super
      // assign every method from proto instance
      _copyProps(_super, _constructor);
      _copyProps(_super[_TYPEDESCRIPTION], _typeDescription);
      _copyProps(_super[_ANNOTATIONS], _annotations);

      //add the super class to inherit array
      _constructor[_INHERIT].push(_super);
    }

    // copy static properties and default methods from interfaces
    for (var a = 0; a < _implements.length; ++a) {
      if (!_implements[a]) continue;
      _copyProps(_implements[a], _constructor);
      _copyInexistentProps(_implements[a].prototype, _constructor.prototype);
      _constructor[_INHERIT].push(_implements[a]);
    }

    // remember the correct constructor
    _constructor.prototype.constructor = _constructor;

    // run the initializer to assign all static and instance variables/functions
    if (_initializer != null) {
      _initializer(_constructor, _constructor.prototype);
    }

    _constructor[_TYPEDESCRIPTION] = _typeDescription;
    _constructor[_ANNOTATIONS] = _annotations;

    // add the default equals method if it is not present yet, and we don't have a superclass
    if (_super == null) {
      if (!_constructor.prototype.equals) {
        _constructor.prototype.equals = _JavalikeEquals;
      }
      if (!_constructor.prototype.getClass) {
        _constructor.prototype.getClass = _JavalikeGetClass;
      }
    }

    // build package and assign
    return _constructor;
  };

  /**
   * return type's annotations
   */
  var getAnnotations = function (clz) {
    return clz[_ANNOTATIONS];
  };

  var getTypeAnnotation = function (clz, annType) {
    //noinspection JSUnresolvedVariable
    var ann = clz[_ANNOTATIONS]["_"];
    return ann ? ann[annType] : null;
  };

  var getMemberAnnotation = function (clz, memberName, annType) {
    var ann = clz[_ANNOTATIONS][memberName];
    return ann ? ann[annType] : null;
  };

  var getParameterAnnotation = function (clz, methodName, idx, annType) {
    var ann = clz[_ANNOTATIONS][methodName + "$" + idx];
    return ann ? ann[annType] : null;
  };

  /**
   * checks if the child is an instanceof parent. it checks recursively if "parent" is the child itself or it's found somewhere in the $inherit array
   */
  var isInstanceOf = function (child, parent) {
    if (child == null)
      return false;
    if (child === parent)
      return true;
    if (!child[_INHERIT])
      return false;
    for (var i = 0; i < child[_INHERIT].length; ++i) {
      if (isInstanceOf(child[_INHERIT][i], parent)) {
        return true;
      }
    }
    return false;
  };

  var _enumEntry = function (idx, name) {
    this._name = name;
    this._ordinal = idx;
  };
  var _enumEntryPrototype = {
    "equals": _JavalikeEquals,
    "name": function () {
      return this._name;
    },
    "ordinal": function () {
      return this._ordinal;
    },
    "toString": function () {
      return this._name;
    }
  };
  _copyInexistentProps(_enumEntryPrototype, _enumEntry.prototype);

  var enumeration = function () {
    var enumClass = {}; // the Enum class has no constructors
    enumClass._values = [];
    for (var i = 0; i < arguments.length; ++i) {
      enumClass[arguments[i]] = new _enumEntry(i, arguments[i]);
      enumClass._values[i] = enumClass[arguments[i]];
    }

    enumClass["values"] = function () {
      return enumClass._values;
    };

    enumClass["valueOf"] = function (label) {
      return enumClass[label];
    };
    return enumClass;
  };

  /**
   * if true the execution of generated main methods is disabled.
   * this is useful when executing unit tests, to no have the main methods executing before the tests
   */
  var mainCallDisabled = false;

  var trunc;
  if (typeof Math.trunc === "function") {
    trunc = Math.trunc;
  } else {
    trunc = function (n) {
      if (n == null)
        return null;
      return n >= 0 ? Math.floor(n) : Math.ceil(n);
    }
  }

  //noinspection JSUnusedLocalSymbols
  var converters = {
    /** @return {Date} */
    Date: function (s, type) {
      var a = /^(\d{4})-(\d{2})-(\d{2}) (\d{2}):(\d{2}):(\d{2}(?:\.\d*)?)$/
        .exec(s);
      if (a) {
        return new Date(+a[1], +a[2] - 1, +a[3], +a[4], +a[5], +a[6]);
      }
      return null;
    },

    Enum: function (s, type) {
      return eval(type.arguments[0])[s];
    }
  };

  //noinspection JSUnusedLocalSymbols
  var serializers = {
    /** @return {String} */
    Date: function (d, type) {
      function pad(n) {
        return n < 10 ? "0" + n : "" + n;
      }

      if (d) {
        return "" + d.getFullYear() + "-" + pad(d.getMonth() + 1) + "-" + pad(d.getDate()) + " " + pad(d.getHours()) + ":" + pad(d.getMinutes()) + ":" + pad(d.getSeconds());
      }
      return null;
    },

    Enum: function (e, type) {
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
  var bind = function (obj, method, thisParamPos) {
    var useFirstParamAsContext = false;
    if (method == null) {
      // the bind("methodName") is the only usage where the method argument can be null
      method = obj;
      obj = null;
      useFirstParamAsContext = true;
    }
    var addThisToParameters = thisParamPos != null;

    return function () {
      var args = arguments;
      if (addThisToParameters) {
        Array.prototype.splice.call(args, thisParamPos, 0, this);
      }
      if (useFirstParamAsContext) {
        obj = Array.prototype.shift.call(args);
      }

      if (typeof method === "string") {
        return obj[method].apply(obj, args);

      } else {
        return method.apply(obj, args);
      }
    };
  };


  /** *********** global ************** */
  var exception = function (err) {
    return err;
  };

  var isEnum = function (obj) {
    return obj != null && obj.constructor == _enumEntry;
  };

  /******* parsing *************/

  /**
   * parse a json string using the type definition to build a typed object hierarchy
   */
  var parseJSON = (function () {
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

    //noinspection JSUnusedLocalSymbols
    function unescapeOne(_, ch, hex) {
      return ch ? escapes[ch] : String.fromCharCode(parseInt(hex, 16));
    }

    var constructors = {};

    function constr(name, param) {
      var c = constructors[name];
      if (!c)
        constructors[name] = c = eval(name);
      return new c(param);
    }

    function convert(type, json) {
      if (!type)
        return json;
      var cv = converters[type.name || type];
      if (cv)
        return cv(json, type);
      //hopefully the type has a string constructor
      return constr(type, json);
    }

    function builder(type) {
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

    // A non-falsy value that coerces to the empty string when used as a key.
    var EMPTY_STRING = "";
    var SLASH = '\\';

    function nextMatch(str) {
      var m = jsonToken.exec(str);
      return m != null ? m[0] : null;
    }

    return function (json, type) {
      // Split into tokens
      // Construct the object to return
      var result;
      var tok = nextMatch(json);
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
      for (tok = nextMatch(json); tok != null; tok = nextMatch(json)) {

        var cont;
        switch (tok.charCodeAt(0)) {
          default:  // sign or digit
            cont = stack[0];
            //noinspection JSUnusedAssignment
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
                stack2[0] = cont.constructor[_TYPEDESCRIPTION] ? cont.constructor[_TYPEDESCRIPTION][key] : stack2[1].arguments[1];
                break;
              }
            }
            cont[key] = convert(stack2[0], tok);
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
        if (stack.length !== 1) {
          throw new Error();
        }
        result = result[0];
      } else {
        if (stack.length) {
          throw new Error();
        }
      }

      return result;
    };
  })();


  /** Array.isArray is not well supported in IE < 9, this is our own naive implementation */
  var isArray = function (obj) {
    return Object.prototype.toString.call(obj) === "[object Array]";
  };

  /**
   * cls can by the type of the return.
   * If it's an array it can be either the type of an element or the type definition of the field.
   * TODO - for other collections and classes is not done yet
   */
  var typefy = function (obj, cls) {
    if (isArray(obj)) {
      var result = [];
      for (var idx = 0; idx < obj.length; idx++) {
        result.push(typefy(obj[idx], elementType(cls)));
      }
      return result;
    }
    var constructors = {};

    function constr(name, param) {
      var c = constructors[name];
      if (!c)
        constructors[name] = c = eval(name);
      return new c(param);
    }

    function elementType(type) {
      if (typeof type == "function")
        return type;
      if (type.arguments) {
        return eval(type.arguments[0]);
      }
      if (typeof type == "string")
        return eval(type);
      return Object;
    }

    function convert(type, json) {
      if (!type)
        return json;
      var cv = converters[type.name || type];
      if (cv)
        return cv(json, type);
      //hopefully the type has a string constructor
      return constr(type, json);
    }

    if (obj == null)
      return null;

    var ret = new cls();
    for (var key in obj) {
      //noinspection JSUnfilteredForInLoop
      var prop = obj[key];
      if (prop == null)
        continue;
      //noinspection JSUnfilteredForInLoop
      var td = cls[_TYPEDESCRIPTION][key];
      if (!td) {
        //noinspection JSUnfilteredForInLoop
        ret[key] = prop;
        continue;
      }
      if (typeof prop == "string") //noinspection JSUnfilteredForInLoop
        ret[key] = convert(td, prop);
      else if (typeof prop == "object") //noinspection JSUnfilteredForInLoop
        ret[key] = typefy(prop, td);
    }
    return ret;
  };

  var stringify = function (obj, cls) {
    if (obj == null)
      return null;

    var ret = {};
    for (var key in obj) {
      //noinspection JSUnfilteredForInLoop
      var td = cls[_TYPEDESCRIPTION][key];
      //noinspection JSUnfilteredForInLoop
      var prop = obj[key];
      var ser = td != null ? serializers[td.name || td] : null;

      if (typeof prop == "function")
        continue;

      if (!td || !ser) {
        //noinspection JSUnfilteredForInLoop
        ret[key] = prop;
        continue;
      }
      if (typeof prop != "string")
        if (ser) //noinspection JSUnfilteredForInLoop
          ret[key] = ser(prop, td);
        else //noinspection JSUnfilteredForInLoop
          ret[key] = typefy(prop, td);
    }
    return ret;
  };

  /************* STJS asserts ***************/
  var assertHandler = function (position, code, msg) {
    throw msg + " at " + position;
  };

  var STJSAssert = {
    "setAssertHandler": function (a) {
      assertHandler = a;
    },

    "assertArgEquals": function (position, code, expectedValue, testValue) {
      if (expectedValue != testValue && assertHandler)
        assertHandler(position, code, "Wrong argument. Expected: " + expectedValue + ", got:" + testValue);
    },

    "assertArgNotNull": function (position, code, testValue) {
      if (testValue == null && assertHandler)
        assertHandler(position, code, "Wrong argument. Null value");
    },

    "assertArgTrue": function (position, code, condition) {
      if (!condition && assertHandler)
        assertHandler(position, code, "Wrong argument. Condition is false");
    },

    "assertStateEquals": function (position, code, expectedValue, testValue) {
      if (expectedValue != testValue && assertHandler)
        assertHandler(position, code, "Wrong state. Expected: " + expectedValue + ", got:" + testValue);
    },

    "assertStateNotNull": function (position, code, testValue) {
      if (testValue == null && assertHandler)
        assertHandler(position, code, "Wrong state. Null value");
    },

    "assertStateTrue": function (position, code, condition) {
      if (!condition && assertHandler)
        assertHandler(position, code, "Wrong state. Condition is false");
    }
  };

  /** exception **/
  var Throwable = function (message, cause) {
    Error.call(this);
    //noinspection JSUnresolvedVariable
    if (typeof Error.captureStackTrace === 'function') {
      // nice way to capture the stack trace for chrome
      //noinspection JSUnresolvedFunction
      Error.captureStackTrace(this, arguments.callee);
    } else {
      // alternate way to capture the stack trace for other browsers
      try {
        //noinspection ExceptionCaughtLocallyJS
        throw new Error();
      } catch (e) {
        this.stack = e.stack;
      }
    }
    if (typeof message === "string") {
      this.detailMessage = message;
      this.message = message;
      this.cause = cause;
    } else {
      this.cause = message;
    }
  };

  extend(Throwable, Error, [], function (constructor, prototype) {
    prototype["detailMessage"] = null;
    prototype["cause"] = null;
    prototype["getMessage"] = function () {
      return this["detailMessage"];
    };

    prototype["getLocalizedMessage"] = function () {
      return this["getMessage"]();
    };

    prototype["getCause"] = function () {
      return (this["cause"] == this ? null : this["cause"]);
    };

    prototype["toString"] = function () {
      var s = "Exception";//TODO should get the exception's type name here
      var message = this["getLocalizedMessage"]();
      return (message != null) ? (s + ": " + message) : s;
    };

    prototype["getStackTrace"] = function () {
      return this["stack"];
    };

    prototype["printStackTrace"] = function () {
      console.error(this["getStackTrace"]());
    };
  }, {});

  var Exception = function (message, cause) {
    Throwable.call(this, message, cause);
  };
  extend(Exception, Throwable, [], function (constructor, prototype) {
  }, {});

  var RuntimeException = function (message, cause) {
    Exception.call(this, message, cause);
  };
  extend(RuntimeException, Exception, [], function (constructor, prototype) {
  }, {});

  var Iterator = function () {
  };
  extend(Iterator, null, [], function (constructor, prototype) {
    prototype["hasNext"] = function () {
    };
    prototype["next"] = function () {
    };
    prototype["remove"] = function () {
    };
  }, {}, {});

  var Iterable = function () {
  };
  extend(Iterable, null, [], function (constructor, prototype) {
    prototype["iterator"] = function () {
    };
  }, {}, {});

  /** stjs field manipulation */
  var setField = function (obj, field, value, returnOldValue) {
    if (global["stjs"]["setFieldHandler"])
      return global["stjs"]["setFieldHandler"](obj, field, value, returnOldValue);
    var toReturn = returnOldValue ? obj[field] : value;
    obj[field] = value;
    return toReturn;
  };

  var getField = function (obj, field) {
    if (global["stjs"]["getFieldHandler"])
      return global["stjs"]["getFieldHandler"](obj, field);
    return obj[field];
  };

  // Export all global objects
  global["Byte"] = Number;
  global["Double"] = Number;
  global["Float"] = Number;
  global["Integer"] = Number;
  global["Long"] = Number;
  global["Short"] = Number;
  global["Throwable"] = Throwable;
  global["Exception"] = Exception;
  global["RuntimeException"] = RuntimeException;
  global["Iterator"] = Iterator;
  global["Iterable"] = Iterable;

  // Export all the stjs namespace
  return {
    // Functions and methods
    "ns": ns,
    "extend": extend,
    "getAnnotations": getAnnotations,
    "getTypeAnnotation": getTypeAnnotation,
    "getMemberAnnotation": getMemberAnnotation,
    "getParameterAnnotation": getParameterAnnotation,
    "isInstanceOf": isInstanceOf,
    "enumeration": enumeration,
    "exception": exception,
    "isEnum": isEnum,
    "trunc": trunc,
    "bind": bind,
    "typefy": typefy,
    "hydrate": typefy,
    "stringify": stringify,
    "setField": setField,
    "getField": getField,
    "parseJSON": parseJSON,

    // Static classes and variables
    "STJSAssert": STJSAssert,
    "mainCallDisabled": mainCallDisabled
  };
})(window);

