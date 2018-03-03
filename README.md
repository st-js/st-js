Strongly-Typed Javascript (STJS)
================================
[![Build Status](https://travis-ci.org/st-js/st-js.svg?branch=master)](https://travis-ci.org/st-js/st-js)

STJS is an open source (Apache 2.0 licensed) Javascript code generator from a Java source. It is built as a Maven plugin that can be executed after the compilation of your Java code.

Our full website can be found at http://st-js.org

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
| long to int     | `var i = l|0`   |
| long to float   | `var f = l`     |
| long to double  | `var d = l`     |
| float to byte   | `((f)<<24>>24)` | 
| float to short  | `((f)<<16>>16)` | 
| float to char   | `((f)&0xfff)`   |
| float to int    | `var i = f|0`   |
| float to long   | `var l = f`     |
| float to double | `var d = f`     |
| double to byte  | `((d)<<24>>24)` | 
| double to short | `((d)<<16>>16)` | 
| double to char  | `((d)&0xfff)`   |
| double to int   | `var i = d|0`   |
| double to long  | `var l = d`     |
| double to float | `var f = d`     |