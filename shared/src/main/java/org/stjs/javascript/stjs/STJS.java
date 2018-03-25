package org.stjs.javascript.stjs;

import org.stjs.javascript.Array;
import org.stjs.javascript.Map;
import org.stjs.javascript.functions.CallbackOrFunction;
import org.stjs.javascript.functions.Function2;
import org.stjs.javascript.functions.Function3;

/**
 * this is a wrapper around some of the stjs functions provided in the stjs.js javascript.
 *
 * @author acraciun
 * @version $Id: $Id
 */
public final class STJS {
	/**
	 * <p>isEnum.</p>
	 *
	 * @param obj a {@link java.lang.Object} object.
	 * @return true if the given object is an STJS enum entry
	 */
	public native boolean isEnum(Object obj);

	/**
	 * throw an exception of any type in Javascript. Java allowes and Throwable derived classes to be thrown, but Javascript allows any other
	 * type.
	 *
	 * @param ex a {@link java.lang.Object} object.
	 * @return a {@link java.lang.Exception} object.
	 */
	public native Exception exception(Object ex);

	/**
	 * parse the given json String and build the object hierarchy in a typed-manner: i.e it builds the objects using their corresponding
	 * constructors. If the intermediary classes don't have an empty constructor, it will be called with null values for all the parameters.
	 *
	 * @param json a {@link java.lang.String} object.
	 * @param clazz a {@link java.lang.Class} object.
	 * @param <T> a T object.
	 * @return a T object.
	 */
	public native <T> T parseJSON(String json, Class<T> clazz);

	/**
	 * using the type description, it converts a POJO (i.e arrived as JSON in your client) to an object of the given type, transforming
	 * recursively the fields.
	 *
	 * @deprecated use STJS.hydrate instead
	 * @param obj a T object.
	 * @param cls a {@link java.lang.Class} object.
	 * @param <T> a T object.
	 * @return a T object.
	 */
	@Deprecated
	public native <T> T typefy(T obj, Class<T> cls);

	/**
	 * using the type description, it converts a POJO (i.e arrived as JSON in your client) to an object of the given type, transforming
	 * recursively the fields.
	 *
	 * @param obj a T object.
	 * @param cls a {@link java.lang.Class} object.
	 * @param <T> a T object.
	 * @return a T object.
	 */
	public native <T> T hydrate(T obj, Class<T> cls);

	/**
	 * using the type description, it converts a POJO (i.e arrived as JSON in your client) to an object of the given type, transforming
	 * recursively the fields.
	 *
	 * @deprecated use STJS.hydrate instead
	 * @param obj a {@link org.stjs.javascript.Map} object.
	 * @param cls a {@link java.lang.Class} object.
	 * @param <T> a T object.
	 * @return a T object.
	 */
	@Deprecated
	public native <T> T typefy(Map<String, ? extends Object> obj, Class<T> cls);

	/**
	 * using the type description, it converts a POJO (i.e arrived as JSON in your client) to an object of the given type, transforming
	 * recursively the fields.
	 *
	 * @param obj a {@link org.stjs.javascript.Map} object.
	 * @param cls a {@link java.lang.Class} object.
	 * @param <T> a T object.
	 * @return a T object.
	 */
	public native <T> T hydrate(Map<String, ? extends Object> obj, Class<T> cls);

	/**
	 * using the type description, it converts an array of POJOs (i.e arrived as JSON in your client) to an object of the given type,
	 * transforming recursively the fields.
	 *
	 * @deprecated use STJS.hydrate instead
	 * @param obj a {@link org.stjs.javascript.Array} object.
	 * @param cls a {@link java.lang.Class} object.
	 * @param <T> a T object.
	 * @return a {@link org.stjs.javascript.Array} object.
	 */
	@Deprecated
	public native <T> Array<T> typefy(Array<Object> obj, Class<T> cls);

	/**
	 * using the type description, it converts an array of POJOs (i.e arrived as JSON in your client) to an object of the given type,
	 * transforming recursively the fields.
	 *
	 * @param obj a {@link org.stjs.javascript.Array} object.
	 * @param cls a {@link java.lang.Class} object.
	 * @param <T> a T object.
	 * @return a {@link org.stjs.javascript.Array} object.
	 */
	public native <T> Array<T> hydrate(Array<Object> obj, Class<T> cls);

	/**
	 * using the type description it converts the fields of the object in strings or other primitve types to be able to send the object via AJAX
	 * for example.
	 *
	 * @param obj a T object.
	 * @param cls a {@link java.lang.Class} object.
	 * @param <T> a T object.
	 * @return a {@link org.stjs.javascript.Map} object.
	 */
	public native <T> Map<String, ? extends Object> stringify(T obj, Class<T> cls);

	/**
	 * this handler will be called any time a field annotated with the template "gsetter" is set. For a statement like this:<br>
	 * object.field = value;<br>
	 * The last parameter is true if the function should return the previous value, false/null to return the new value.<br>
	 * the call will be setFieldHandler-$invoke(object, field, value, value) For example for object.field++ <br>
	 * the call will be setFieldHandler-$invoke(object, field, object.field+1, false)
	 */
	public Function3<Object, String, Object, Boolean> setFieldHandler;

	/**
	 * this handler will be called any time a field annotated with the template "gsetter" is get. For a statement like this:<br>
	 * object.field<br>
	 * the call will be getFieldHandler-$invoke(object, field)
	 */
	public Function2<Object, String, Object> getFieldHandler;

	/**
	 * return the annotations of the given type in the format:
	 *
	 * <pre>
	 *  $annotations : {
	 * _: {....}
	 * field1: {...}
	 * method1: {...}
	 * method1$0:  {...}
	 * method1$1:  {...}...
	 * }
	 * </pre>
	 *
	 * for each annotation list you have:
	 *
	 * <pre>
	 * {
	 * "annotationType1": {value:"v1", value2: "v2"},
	 * "annotationType2": {}
	 * }
	 * </pre>
	 *
	 * @param clazz a {@link java.lang.Class} object.
	 * @return a {@link org.stjs.javascript.Map} object.
	 */
	public native Map<String, Map<String, Map<String, Object>>> getAnnotations(Class<?> clazz);

	/**
	 * <p>getTypeAnnotation.</p>
	 *
	 * @param clazz a {@link java.lang.Class} object.
	 * @param annTypeName a {@link java.lang.String} object.
	 * @return a {@link org.stjs.javascript.Map} object.
	 */
	public native Map<String, Object> getTypeAnnotation(Class<?> clazz, String annTypeName);

	/**
	 * <p>getMemberAnnotation.</p>
	 *
	 * @param clazz a {@link java.lang.Class} object.
	 * @param memberName a {@link java.lang.String} object.
	 * @param annTypeName a {@link java.lang.String} object.
	 * @return a {@link org.stjs.javascript.Map} object.
	 */
	public native Map<String, Object> getMemberAnnotation(Class<?> clazz, String memberName, String annTypeName);

	/**
	 * <p>getParameterAnnotation.</p>
	 *
	 * @param clazz a {@link java.lang.Class} object.
	 * @param methodName a {@link java.lang.String} object.
	 * @param idx a int.
	 * @param annTypeName a {@link java.lang.String} object.
	 * @return a {@link org.stjs.javascript.Map} object.
	 */
	public native Map<String, Object> getParameterAnnotation(Class<?> clazz, String methodName, int idx, String annTypeName);

	/**
	 * this functions is used to be able to send method references as callbacks
	 *
	 * @param object a {@link java.lang.Object} object.
	 * @param methodName a {@link java.lang.String} object.
	 * @return a T object.
	 */
	public native <T extends CallbackOrFunction> T bind(Object object, String methodName);

	/**
	 * this functions is used to be able to atach lambdas to an object
	 *
	 * @param object a {@link java.lang.Object} object.
	 * @param func a {@link org.stjs.javascript.functions.CallbackOrFunction} object.
	 * @return a T object.
	 */
	public native <T extends CallbackOrFunction> T bind(Object object, CallbackOrFunction func);

	/**
	 * this functions is used to be able to atach lambdas to an object
	 *
	 * @param object a {@link java.lang.Object} object.
	 * @param func a {@link org.stjs.javascript.functions.CallbackOrFunction} object.
	 * @param THISParamPosition a int.
	 * @return a T object.
	 */
	public native <T extends CallbackOrFunction> T bind(Object object, CallbackOrFunction func, int THISParamPosition);

}
