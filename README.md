Strongly-Typed Javascript (STJS)
================================
[![Build Status](https://travis-ci.org/st-js/st-js.svg?branch=master)](https://travis-ci.org/st-js/st-js)

STJS is an open source (Apache 2.0 licensed) Javascript code generator from a Java source. It is built as a Maven plugin that can be executed after the compilation of your Java code.

Our full website can be found at http://st-js.org

Compiling the Project
---------------------

ST-JS compiles with the traditional mvn install command, but it currently needs both Java 6 - that is the default JDK on the command line when you call the Maven command,
but also Java 8 to compile the generator-plugin-java8 artifact. To achieve this, you need to to configure the environment variable JAVA8_HOME that points to the home of your JDK 8 home folder.

