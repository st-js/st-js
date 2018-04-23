Strongly-Typed Javascript (STJS)
================================
[![Build Status](https://travis-ci.org/st-js/st-js.svg?branch=master)](https://travis-ci.org/st-js/st-js)

STJS is an open source (Apache 2.0 licensed) Javascript code generator from a Java source. It is built as a Maven plugin that can be executed after the compilation of your Java code.

Our full website can be found at http://st-js.org

This branch
-----------

This branch is special as it outputs valid TypeScript.
It's work in progress but it supports.

### Syntax changes

- Interfaces are converted to TypeScript interfaces
- Classes are converted to TypeScript classes
- `let`/`const` instead of `var`
- Use arrow functions where it can, keeping `stjs.bind()` where it can't do otherwise
- field getters/setters instead of `stjs.setField`/`stjs.getField`
- TypeScript enum instead of `stjs.enumeration`
- varargs instead of `stjs.varargs`
- `instanceof` instead of `stjs.isInstanceOf`
- Java `for (String element : array)` loops are converted to `for ... of ` loops
- Ignore `@Namespace` annotations (except for bridges)
- Write types
  - Field Types
  - Method Return types
  - Method arguments types
  - Parametrized types
  - Wildcard types ((Replaced by `any` as this doesn't exist in TypeScript))
  - Generic Types
  
## Auto fixes
- `String.prototype.equals()` is replaced by `==` and `!String.prototype.equals()` is replaced by `!=`
- `Integer.parseInt()`, `Short.parseShort()`, `Float.parseFloat()`, ... replaced by `parseInt()` and `parseFloat()`
- `Integer.prototype.shortValue()`, `Short.prototype.intValue()`, ... replaced by `parseInt()` and `parseFloat()`
- `Boolean.valueOf(foo)` replaced by `!!foo`
- `String.valueOf(foo)` replaced by `"" + foo`
- `Integer.valueOf(foo)` replaced by `new Number(foo).valueOf()`
- `foo.replaceFirst("bar", "baz")` replaced by `foo.replace(new RegExp("bar"), "baz")`
- `foo.replaceAll("bar", "baz")` replaced by `foo.replace(new RegExp("bar", "g"), "baz")`
- `foo.contains(bar)` replaced by `foo.indexOf(bar) >= 0`
- `foo.matches(bar)` replaced by `foo.match("^" + regexp + "$") != null`

### New Checks

- You cannot call Java specific methods on JavaScript (methods added by st-js, or base methods from java.lang.Object)
- You cannot call methods on Enums (like `.ordinal()` / `valueOf()` / `.name()` ...)
- You cannot do `instanceof` on interfaces nor primitive types
- You cannot add a body to an interface method
- You cannot define inner types (Class / Enum / Interface) inside interfaces
- You cannot define inner types (Class / Enum / Interface) inside anonymous classes
- You cannot call `stjs.getTypeAnnotation` / `stjs.getAnnotations` / `stjs.getMemberAnnotation` / `stjs.getParameterAnnotation` as annotations aren't generated in TypeScript

## Roadmap

- Don't rely on stjs.bind for method references
- Write types for variables
- Correctly write complex generic types (M extends Serie & FinancialSerie) (Situation improved, still not great)
- Correctly write types for functional interfaces (lambdas) 
  - instead of : `static method(f: Function1<number, number>): void {}`
  - write : `static method(f: (param1: number) => number): void {}`

## In idea

- Add imports automatically
- Add `export` and `export default` to classes / interfaces
- Convert tests to a Jest test suite

Compiling the Project
---------------------

ST-JS compiles with the traditional mvn install command, but it currently needs both Java 6 - that is the default JDK on the command line when you call the Maven command,
but also Java 8 to compile the generator-plugin-java8 artifact. To achieve this, you need to to configure the environment variable JAVA8_HOME that points to the home of your JDK 8 home folder.

## Notes on Primitives+Arrays Support

### Arrays

`AnyNonPrimitiveType[]` is `Array`

```java
String[] a = {"a", "b"};
```

```javascript
var a = ["a", "b"];
```

### Multidimensional arrays

```java
float[][][] arr = new float[1][2][3];
Object[][][] objarr = new Object[][][]{ new String[][]{ { "hello" }, { "world" }, new String[]{} }, new Integer[][]{ { 1, 2 }, { 3, 4 } }, new Double[4][5] };
```

```javascript
var arr = Array.apply(null, Array(1)).map(function(){return Array.apply(null, Array(2)).map(function(){return new Float32Array(3);});});"
var objarr = [[["hello"], ["world"], []], [[1, 2], [3, 4]], Array.apply(null, Array(4)).map(function() { return Array(5);})];
```

### Primitive Arrays

| Java        | JavaScript     | 
| ----------- | -------------- | 
| `boolean[]` | `Int8Array`    | 
| `byte[]`    | `Int8Array`    | 
| `short[]`   | `Int16Array`   |
| `char[]`    | `Uint16Array`  |
| `int[]`     | `Int32Array`   |
| `float[]`   | `Float32Array` |
| `double[]`  | `Float64Array` |
| `long[]`    | `Array`        |

### Foreach 

Enhanced for loop over Java arrays will iterate over values instead of keys
```java
String[] msg = {"hello", "world"};
for(String s : msg) {
	console.log(s);
}
```

```javascript
var msg = ["hello", "world"];
for(var index$s = 0, arr$s = msg; index$s < arr$s.length; index$s++) {
	var s = arr$s[index$s];
	console.log(s);
}
```

### `long`

There is no support for 64bit integer type in JavaScript. Max value for an integer is 2^53-1
```
> Number.MAX_SAFE_INTEGER
9007199254740991
```

### `char`

`char` is stored in Java as `unsigned short` so it is in st-js primitive support.

`char[]` in st-js is `Uint16Array`.

The following side effects appear:

```java
String a = "hello worl";
char c = 'd';
String result = a + c;
```

```javascript
var a = "hello worl";
var c = 'd'.charCodeAt(0); //looks ugly but compatible
var result = a + String.fromCharCode(c);
```

```java
char c = 'a';
byte b = (byte) c;
```

```javascript
var c = 'a'.charCodeAt(0);
var b = c << 24 >> 24;
```

### `float` and `double`

Are considered equivalent no attempt for Java compatibility is made.

### primitive type casting

| Java            | JavaScript      | 
| --------------- | --------------- | 
| int to short    | `((i)<<16>>16)` | 
| int to byte     | `((i)<<24>>24)` | 
| int to char     | `((i)&0xfff)`   |
| int to long     | `var l = i`     |
| int to float    | `var f = i`     |
| int to double   | `var d = i`     |
| short to byte   | `((s)<<24>>24)` | 
| short to char   | `((s)&0xfff)`   |
| short to int    | `var i = s`     |
| short to long   | `var l = s`     |
| short to float  | `var f = s`     |
| short to double | `var d = s`     |
| byte to short   | `var s = b`     | 
| byte to char    | `var c = b`     |
| byte to int     | `var i = b`     |
| byte to long    | `var l = b`     |
| byte to float   | `var f = b`     |
| byte to double  | `var d = b`     |
| char to byte    | `((c)<<24>>24)` | 
| char to short   | `var s = c`     | 
| char to int     | `var i = c`     |
| char to long    | `var l = c`     |
| char to float   | `var f = c`     |
| char to double  | `var d = c`     |
| long to byte    | `((l)<<24>>24)` | 
| long to short   | `((l)<<16>>16)` | 
| long to char    | `((l)&0xfff)`   |
| long to int     | `var i = l\|0`   |
| long to float   | `var f = l`     |
| long to double  | `var d = l`     |
| float to byte   | `((f)<<24>>24)` | 
| float to short  | `((f)<<16>>16)` | 
| float to char   | `((f)&0xfff)`   |
| float to int    | `var i = f\|0`   |
| float to long   | `var l = stjs.trunc(f)`     |
| float to double | `var d = f`     |
| double to byte  | `((d)<<24>>24)` | 
| double to short | `((d)<<16>>16)` | 
| double to char  | `((d)&0xfff)`   |
| double to int   | `var i = d\|0`   |
| double to long  | `var l = stjs.trunc(d)`     |
| double to float | `var f = d`     |
