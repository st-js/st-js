package org.stjs.javascript.stjs;

/**
 * 
 * this is a wrapper around some of the stjs functions provided in the stjs.js javascript.
 * 
 * @author acraciun
 */
public interface STJS {
	/**
	 * 
	 * @param obj
	 * @return true if the given object is an STJS enum entry
	 */
	public boolean isEnum(Object obj);

	/**
	 * throw an exception of any type in Javascript. Java allowes and Throwable derived classes to be thrown, but
	 * Javascript allows any other type.
	 * 
	 * @param ex
	 */
	public Exception exception(Object ex);
}
