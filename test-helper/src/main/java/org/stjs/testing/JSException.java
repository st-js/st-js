package org.stjs.testing;

/**
 * A placeholder class for javascript exceptions. Since STJS doesn't handle typed exceptions yet, the only Type of
 * Throwable that is accepted by the STJSTestDriverRunner in the @Test.expected field is this class. This makes
 * sure that users of the @Test annotation clearly understand that it is currently not possible to distinguish between
 * different types of exceptions in STJS, and by extension in the STJS JUnit runner.
 * 
 * @author lordofthepigs
 */
public class JSException extends RuntimeException {

}
