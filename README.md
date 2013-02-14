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

Please note that you need at least *Java 6* to have ST-JS compiling your projects.

You can find the documentation, a tutorial, the change log and how to get in contact with us on the site's website [http://www.st-js.org](http://www.st-js.org).

