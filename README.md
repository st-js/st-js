Strongly-Typed Javascript (STJS)
================================

STJS is an open source (Apache 2.0 licensed) Javascript code generator from a Java source. It is built as a Maven plugin that can be executed after the compilation of your Java code.

Why we built it
---------------


Dynamically typed languages are good for small to medium projects. But when the size of the project grows and handled data becomes complex a strongly typed language gives you an extra guarantee that your changes won’t break your code.

The current trend is to build very rich web interfaces. The problem is that the only option all the browsers offer is Javascript, that is dynamically typed language. Even if the support from IDEs and browsers keeps growing, you'll find out pretty soon that a Javascript line of code gets quite expensive in a big (2000+ Javascript LOC) project.

If you landed to our project’s page, it’s almost sure you already have experience with GWT or at least you heard about it. Even though as a Java developer we are naturally attracted by their philosophy, it bothered us that their solution is too intrusive: it imposes you a new framework, new tools, when we were only looking for something that can add a strongly typed dimension to our Javascript code.

An example
==========
Here is an example of a code written in Java and the corresponding generated Javascript. The Java code may seem a bit longer and possibly more complicated at a first glance, but it provides you with all the advantages of a typed language, while preserving as much as possible the resemblance to the javascript style of programming. The visual resemblance is crucial here since it allows for faster learning and easier transition. This visual resemblance maintained throughout the library design

```java
$("#form").submit(new EventHandler() {
  public void onEvent(Event ev, final HTMLElement THIS) {
    StockData stockData = that.updateStock($("#newStock").val());
    $(that.row(stockData)).appendTo("table tbody");
    that.stocks.push(stockData.stock);
    }
  });
```
```javascript
$("#form").submit(function(ev) {
  var stockData = that.updateStock($("#newStock").val());
  $(that.row(stockData)).appendTo("table tbody");
  that.stocks.push(stockData.stock);
});
```
   
How does it work
================
STJS does not try to hide from you how Javascript works. STJS addresses Java developers that are comfortable with a regular Javascript way of building application usually using a component library (jQuery or others). We don’t believe in generated Javascript that is unreadable and impossible to debug.

So we limited STJS to only generate Javascript from a Java code that is as close as possible to the original code. We’d like to believe that the generated Javascript code is as we’d of write it ourselves.

The only dependency STJS requires at runtime is a small Javascript library (that basically provides the function to simulate inheritance in Javascript). That’s it! STJS imposes no java dependency, no visual component, no AJAX framework. You can feel free to use the Javascript libraries and components you like. We’d like to believe that if one day you’ll stop using STJS (!?), you just take the generated Javascript code and continue building your project.

The Java code you write (with the purpose to generate Javascript) will not be executed as such. You can only use in this code the Java classes that have their Javascript counterpart when you launch it in your browser. So you CANNOT use typical Java classes like Java util or other JDK packages.

If we managed to convince you, please continue further for a few minutes tutorial.

Other Javascript constructions
==============================
The problem with having Javascript code generated from Java is that Java is less rich than Javascript. There are Javascript constructions that do not have their corresponding in Java (or they are different) like object (map) constructors, array constructors, inline functions. We tried to provide constructions that may look unusual for a Java developer but somehow familiar to a Javascript developer.

Map / Object
------------

In Javascript as an object may have dynamic properties it can be used also as a map.

In Java we’d prefer the typed version.
```java
StockData stock = new StockData() {{
   last = 10.0;
   close = 2.0;
   stock = "ABC";
}};
```
```javascript
var stock = {
  "last":10,
  "close":2,
  "stock":"ABC"
};
```
But if you prefer the dynamic (map-like version) you can do, we created a class Map that provides the same methods and access mode as the Javascript counterpart.
```java
Map<String, Object> stock = $map(
  "last", 2,
  "close", 3,
  "stock", "ABC"
);
```
```javascript
var stock = {
  "last":10,
  "close" : 2,
  "stock":"ABC"
};
```
Both codes will generate the same Javascript! 
To access a map element:
```java
Double value = map.$get("key");
map.$set("key", 4);
map.$delete("key");
```
```javascript
var value = map["key"];
map["key"] = 4;
delete map["key"];
```
Array
-----

The array in Javascript has looks a lot like a List in Java, but the access to its elements is done more like a Java array. So we provided a class Array that offers the same methods as the Javascript counterpart. as the bracket ([]) construction can only be used in Java with a regular array, we added $get/$set methods that will generate the expected code in Javascript

Here is an example
```java
Array<Integer> array = $array(1, 5, 10);
Integer value = array.$get(2);
array.$set(2, 4);
```
```javascript
var array = [1, 5, 10];
var value = array[2];
array[2] = 4;
```
Inheritance
-----------

The class inheritance is a common construction in Java, but less common in Javascript. We simulate the inheritance in Javascript by using the prototypes. With this type of inheritance, we simulate instance and static fields and methods. We currently don't support private, protected and final modifiers.

Here is an example
```java
public class Child extends Parent {
public Child(String test) {
	super(test);
}


public static int staticField = 1;
public int instanceField = 2;

@Override
public void instanceMethod(String n) {
	super.instanceMethod(n + "-");
}

public static void staticMethod() {
	staticField += 1;
}
}
 ```
```javascript   
Child = function(test) {
	this._super(null, test);
}
stjs.extend(Child, Parent);

Child.staticField = 1;
Child.prototype.instanceField = 2;


Child.prototype.instanceMethod = function(n) {
	this._super("instanceMethod", n + "-");
}

Child.staticMethod = function() {
	Child.staticField += 1;
}
```
AJAX/JSON
---------

When you transfer objects between the server and the client, your Data Transfer Objects are usually serialized on the server side (using Gson, Jackson or other library) and deserialized on the browser usualy using Javascript eval function (or safer equivalents). The problem with this approach is that all the type information is lost, because what you'll get will be regular objects (built with {}) without any type conversion for dates or enums or your own types. So you'll be unable to use correctly your data model inside your Javascript.

STJS comes with a "type-safe" JSON parser, that will use the type definition stored with the class during the Javascript generation to re-create back the serialized object hierarchy. Here is how to use it:
```java
import static org.stjs.javascript.Global.stjs;

public class Bean{
	public enum MyEnum {a,b,c};

	public Date date;
	public MyEnum enum;

	public void method(){
	}
}

...
Bean b = stjs.parseJSON("{\"date\":\"2011-11-10 10:20:22\", \"enum\":\"a\"}", Bean.class);
```

jQuery
------

As the first projects where we wanted to use STJS uses jQuery, we had to include right in our first support for this Javascript library

jQuery has an interesting plugin design: each plugin adds some methods to the jQuery element wrapper. In order to have this behavior in Java we propose one interface for the jQuery core (org.stjs.javascript.jquery.JQueryCore) and one per plugin (ex: org.stjs.javascript.jquery.plugins.Accordion)

```java
public interface Accordion<FullJQuery extends JQueryCore<?>> {
public FullJQuery accordion();
public FullJQuery accordion(AccordionOptions<FullJQuery> options);
...
```
To be able to use jQuery's methods chain (ex. $(".css").accordion().show()), all the plugin methods return a FullJQuery type the is received as generic parameter by the class. This type will give access to all the methods jQuery and the desired plugins offer. We propose already an interface that combine jQuery core and all the UI plugins: JQueryAndPlugins. This interface is used when you want to extend it with other plugins (use org.stjs.javascript.jquery.JQuery if you don't plan to other other jQuery plugins). But if you want to add your own plugin, create the plugin's interface (like Accordion) and create your own interface that will combine your plugin's interface and the JQueryAndPlugins interface.
```java
public interface MyJQueryLib<FullJQuery extends MyJQueryLib<?>> extends MyPlugin<FullJQuery>,JQueryAndPlugins<FullJQuery>{}
```
Global Objects
--------------

In Javascript several objects are accessible as global objects, like window, $ (for jQuery). To simulate this in Java we added all these objects as static fields in the Global object or GlobalJQuery object. We preferred not to add all the fields and methods of the window object - that in Javascript are directly accessible. Please use the window object to access them.
```java
import static org.stjs.javascript.Global.window;
import static org.stjs.javascript.jquery.GlobalJQuery.$;
```
A web page
==========
```html
<head>
  <script src="${pageContext.request.contextPath}/generated-js/stjs.js"
    type="text/javascript"></script>
  <script src="${pageContext.request.contextPath}/generated-js/StockApplication.js"
    type="text/javascript"></script>
</head>
```
Modules
=======
STJS has mainly three modules each of which is used in different phases of your development. As you'll remark we used the property stjs.version to specify the version of STJS to use (the modules are released together).
```xml
<properties>
	<stjs.version>1.1.4</stjs.version>
</properties>
```
The current STJS version is 1.2.0
---------------------------------
The generator
-------------

This is a maven plugin that takes the sources of your project that you indicate and generate the corresponding javascript files in the target folder

```xml
<plugin>
  <groupId>org.st-js</groupId>
  <artifactId>maven-plugin</artifactId>
  <version>${stjs.version}</version>
  <executions>
    <execution>
      <id>main</id>
      <goals>
	<goal>generate</goal>
      </goals>
    </execution>
    <!-- if you use the test helper -->
    <execution>
      <id>test</id>
      <goals>
	<goal>generate-test</goal>
      </goals>
    </execution>
  </executions>
  <configuration>
    <rootPackage>auto</rootPackage>
  </configuration>
</plugin>
```
The Javascript library
----------------------

The Javascript library (js-lib) is a library that you need as dependency for the Java code (that will be transformed in Javascript) you write. The purpose of this library is to offer bridges to all the Javascript objects defined by the browsers.
```xml
<dependency>
  <groupId>org.st-js</groupId>
  <artifactId>js-lib</artifactId>
  <version>${stjs.version}</version>
</dependency>
```
As a convenience we added as well a bridge for the jQuery library and jQuery UI plugins.
```xml
<dependency>
  <groupId>org.st-js</groupId>
  <artifactId>jquery</artifactId>
  <version>${stjs.version}</version>
</dependency>
```
If in your code you’d like to use other Javascript libraries not already provided by js-lib, you should build bridges for them following our model.

If you'd like to share objects between the client and the server, on the server side you should add the following dependency:
```xml
<dependency>
  <groupId>org.st-js</groupId>
  <artifactId>server</artifactId>
  <version>${stjs.version}</version>
</dependency>
```
This dependency allows you to create maps and arrays and to serialize them (as JSON for example).

The test helper
---------------

This modules can be used for your unit tests. You’ll be able to write unit tests also in Java. When executed, the unit tests Java code will be converted to Javascript and together with the Javascript code generated from the tested classes will be executed in a browser via the our internal JUnit test driver.
```xml
<dependency>
  <groupId>org.st-js</groupId>
  <artifactId>test-helper</artifactId>
  <version>${stjs.version}</version>
  <scope>test</scope>
</dependency>
```
Maven
=====
Repostory
---------
The artifacts are deployed to the central Maven repository.

Maven configuration for auto-generation in Eclipse

If you use Eclipse you can take advantage of the Maven / Eclipse integration. The Javascript code is generated each time the corresponding Java class is modified and saved. This way any modification you bring to your code will be instantly observed on the browser! Note: you need the at least the 1.0 version of the Eclipse plugin for Maven.
```xml
<plugin>
  <groupId>org.eclipse.m2e</groupId>
  <artifactId>lifecycle-mapping</artifactId>
  <version>1.0.0</version>
  <configuration>
    <lifecycleMappingMetadata>
      <pluginExecutions>
	<pluginExecution>
	  <pluginExecutionFilter>
	    <groupId>org.st-js</groupId>
	    <artifactId>
	      maven-plugin
	    </artifactId>
	    <versionRange>
	      [${stjs.version},)
	    </versionRange>
	    <goals>
	      <goal>generate</goal>
	      <!-- if you use the test helper -->
	      <goal>generate-test</goal>
	    </goals>
	  </pluginExecutionFilter>
	  <action>
	     <execute />
	  </action>
	</pluginExecution>
      </pluginExecutions>
    </lifecycleMappingMetadata>
  </configuration>
</plugin>
```
What you cannot do in STJS
==========================
Even though Javascript looks to be more flexible than Java, there are things that you can do in Java but you cannot do (at least directly) in Javascript. Of course, during the Javascript generation, we could've add some Javascript constructions to go around these limitations. But again our philosophy is to alter as little as possible the Java code during the transformation. That's why we prefer to generate errors and to ask the user to find a compatible alternative. So here is the list (that keeps growing - unfortunately):

   - you cannot have two methods with the same name (overloading is not possible - overriding is)
   - you cannot have a method and a field with the same name within the class and its parents!
   - you cannot have more than one constructor
   - you cannot use as field or method name Javascript keywords (that are not Java keywords) like: function, prototype, var
   - you can only have one form of variable arguments method: one argument only that should be called "arguments" (like the variable available in each Javascript function)
   - you cannot initialize inline the instance fields - with the exception of primitive types (and their wrappers) and String
   - you cannot use Java arrays (for example String[]) - they are not compatible with the Javascript arrays as they don't have the methods like push, slice, etc. More, the iteration in Java is done on values (for(String s : array)) while the iteration in Javascript is done on indexes (for(var idx : array)). Please use the org.stjs.javascript.Array instead.
In all these cases the alternative is prety simple and it doesn't require you to weird workarounds.

STJS bridge libraries
=====================
As specified before, STJS is NOT a web framework, so no visual components or other components come with STJS. So you should rely on existing components coming with different Javascript frameworks like jQuery, DOJO, etc. There are basically two ways to combine the STJS-generated code depending on were do you want to make the integration:

in Javacript - by using both the components and the STJS generated code
in Java - by building a Java bridge to the component libraries
We believe the second option brings you the best of both worlds. In fact STJS already provides bridges to the DOM object, jQuery and jQuery UI plugins. The bridge is composed mostly of Java interfaces that have the same name as their Javascript counterpart. Sometimes you may also need to provide global objects or functions (that will be implemented as static fields or methods of the object). In the case of a static method, the method should throw UnsupportedOperationException - to indicate it's a method to be used in the Javascript code (not in some server-side Java code).

One interesting feature of the bridges is that they are not submitted to the same list of constrains as the regular STJS Java code. This means that you can overload methods in order to provide more clarity to the user. A good example is the way plugins are built in jQuery where the same function (usually the plugin's name) has many different usages: activate the plugin, change an option, get a state value, call a method, etc.

Testing (experimental)
======================
To take even more advantage of the strongly-typed code, we added a JUnit runner that allow you to execute the generated Javascript (for both the main code and the unit test) in a browser and display back the assert errors as any JUnit test.

The test driver is somehow inspired by Selenium and JSTestDriver, but it aims to be lighter and easier to use. For the moment the driver does not handle AJAX callbacks (so you have to mock your server's response).
But it comes with an integration with the Mockjax plugin that mocks jQuery AJAX calls. Have a look on the documentation page for more details.

Here is an example (taken from Mockjax site and re-wrote with STJS):
```java
@RunWith(STJSTestDriverRunner.class)
@HTMLFixture("<div id='fortune'></div>")

@Scripts({ "classpath://jquery.js",
       "classpath://jquery.mockjax.js", "classpath://json2.js" })
public class MockjaxExampleTest {
  @Test
  public void myTest() {
    $.ajaxSetup($map("async", false));
    $.mockjax(new MockjaxOptions() {
      {
	url = "/restful/fortune";
	responseText = new Fortune() {
	  {
	    status = "success";
	    fortune = "Are you a turtle?";
	  }
	};
      }
    });

    $.getJSON("/restful/fortune", null, new Callback3<Fortune, String, JQueryXHR>() {
      @Override
      public void $invoke(Fortune response, String p2, JQueryXHR p3) {
	if (response.status.equals("success")) {
	  $("#fortune").html("Your fortune is: " + response.fortune);
	} else {
	  $("#fortune").html("Things do not look good, no fortune was told");
	}

      }
    });
    assertEquals("Your fortune is: Are you a turtle?", $("#fortune").html());
  }

  private static class Fortune {
    public String status;
    public String fortune;
  }
}
```

