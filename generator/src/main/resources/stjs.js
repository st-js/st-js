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
/**** Functionality in JS, but not in Java ********
 * functions (static methods of Global) to be called in Java
 */


/* String */
/*
anchor()
big()
blink()
bold()
fixed()
fontcolor()
fontsize()
italics()
link()
small()
strike()
sub()
sup()
*/

/* Number */
function toFixed(n, pos){
	return n.toFixed(pos);
}
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

stjs.extend=function( _constructor,  _super){
	var key;
	var I = function(){};
	I.prototype	= _super.prototype;
	_constructor.prototype	= new I();

	// this prototype accepts zero or more arguments
	// if the first argument is a nullable value (undefined, false, 0, "", null)
	// this function will call directly the constructor
	// while if name is a string, this function will call
	// super method with that name (if any, otherwise it will rightly raise an error)
	_constructor.prototype._super = function(name){
		var	_rem	= this._super;

		// to continue with inherited chain
		// the _super property has to be
		// setted as the one stored in parent constructor prototype
		this._super	= _super.prototype._super;
		// this is because inside super method we would like to be able
		// to use again the magic _super with parent constructor or method as well
		var result		= (name ? _super.prototype[name] : _super).apply(this, Array.prototype.slice.call(arguments, 1));

		// after constructor or parent method execution
		// we have to set the original super to be able
		// to call this method another time
		this._super	= _rem;

		// operation result
		return	result;
	};

	//copy static properties
	// assign every method from proto instance
	for(key in _super){
		_constructor[key]	= _super[key];
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

stjs.exception=function(err){
	return err;
}

function isEnum(obj){
	return obj != null && obj.constructor == stjs.enumEntry;
}