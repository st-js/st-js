package org.stjs.javascript;

/**
 * This interface represents an array from Javascript.The value may be typed. The iteration is done on the indexes to have the javascript
 * equivalent of <br>
 * <b>for(var key in array)</b> <br>
 * The methods are prefixed with $ to let the generator know that is should generate braket access instead, i.e <br>
 * array.$get(key) => array[key] <br>
 * array.$set(key, value) => array[key]=value
 * @author acraciun
 */
public interface Array<V> extends Iterable<Integer> {
	public V $get(int index);

	public void $set(int index, V value);

	public int $length();

	public void $length(int newLength);

	public void concat(V... values);

	public int indexOf(V element);

	public int indexOf(V element, int start);

	public String join();

	public String join(String separator);

	public V pop();

	public int push(V... values);

	public void reverse();

	public V shift();

	public Array<V> slice(int start);

	public Array<V> slice(int start, int end);

	public Array<V> splice(int start);

	public Array<V> splice(int start, int end);

	public Array<V> splice(int start, int end, V... values);

	public void sort(SortFunction<V> function);

	public int unshift(V... values);
}
