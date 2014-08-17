package org.stjs.javascript.stjs;

import org.stjs.javascript.Array;

/**
 * 
 * this is a wrapper around some of the stjs functions provided in the stjs.js javascript.
 * 
 * @author acraciun
 */
public final class STJS {
	/**
	 * 
	 * @param obj
	 * @return true if the given object is an STJS enum entry
	 */
	public native boolean isEnum(Object obj);

	/**
	 * throw an exception of any type in Javascript. Java allowes and Throwable derived classes to be thrown, but
	 * Javascript allows any other type.
	 * 
	 * @param ex
	 */
	public native Exception exception(Object ex);

	/**
	 * parse the given json String and build the object hierarchy in a typed-manner: i.e it builds the objects using
	 * their corresponding constructors. If the intermediary classes don't have an empty constructor, it will be called
	 * with null values for all the parameters.
	 * 
	 * @param json
	 * @param clazz
	 * @return
	 */
	public native <T> T parseJSON(String json, Class<T> clazz);

	/**
	 * using the type description, it converts a POJO (i.e arrived as JSON in your client) to an object of the given
	 * type, transforming recursively the fields. You need to include stjs-ext.js
	 * 
	 * @param obj
	 * @param cls
	 * @return
	 */
	public native <T> T typefy(T obj, Class<T> cls);

	/**
	 * using the type description, it converts an array of POJOs (i.e arrived as JSON in your client) to an object of
	 * the given type, transforming recursively the fields.You need to include stjs-ext.js
	 * 
	 * @param obj
	 * @param cls
	 * @return
	 */
	public native <T> Array<T> typefy(Array<T> obj, Class<T> cls);

	/**
	 * using the type description it converts the fields of the object in strings or other primitve types to be able to
	 * send the object via AJAX for example.You need to include stjs-ext.js
	 * 
	 * @param obj
	 * @param cls
	 * @return
	 */
	public native <T> T stringify(T obj, Class<T> cls);
}
