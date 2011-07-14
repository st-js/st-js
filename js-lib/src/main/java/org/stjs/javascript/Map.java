package org.stjs.javascript;

/**
 * This interface represents a normal object in javascript (that acts as a map). The key is always a String. The value may be typed. The
 * iteration is done on the keys to have the javascript equivalent of <br>
 * <b>for(var key in map)</b> <br>
 * The methods are prefixed with $ to let the generator know that is should generate braket access instead, i.e <br>
 * map.$get(key) => map[key] <br>
 * map.$put(key, value) => map[key]=value
 * @author acraciun
 */
public interface Map<V> extends Iterable<String> {
	public V $get(String key);

	public void $put(String key, V value);
}
