package org.stjs.generator.handlers.utils;

import static org.stjs.generator.handlers.utils.PreConditions.checkState;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Lists {


	public static <T> T getOnlyElement(Iterable<T> elems) {
		if (elems instanceof List<?>) {
			List<T> list = (List<T>) elems;
			checkState(list.size()==1,  "elems must contain exactly one element");
			return list.get(0);
		}
		Iterator<T> iterator = elems.iterator();
		checkState(iterator.hasNext(), "elems must not be empty");
		T obj = iterator.next();
		checkState(!iterator.hasNext(), "elems must contain exactly one element");
		return obj;
	}
	
	public static <T,U> List<U> transform(final List<T> list,
			final Function<? super T, ? extends U> function) {
		return new AbstractList<U>() {

			@Override
			public U get(int i) {
				return function.apply(list.get(i));
			}

			@Override
			public int size() {
				return list.size();
			}
		};
	}

	public static <T> List<T> union(final List<T> list1,
			final List<T> list2) {
		return new AbstractList<T>() {

			@Override
			public T get(int i) {
				if (i < list1.size()) {
					return list1.get(i);
				}
				return list2.get(i-list1.size());
			}

			@Override
			public int size() {
				return list1.size() + list2.size();
			}
		};
	}
	
	public static <T> List<T> newArrayList() {
		return new ArrayList<T>();
	}
	
	public static <T> List<T> newArrayList(T... elems) {
		return new ArrayList<T>(Arrays.asList(elems));
	}
	
	public static <T> List<T> newArrayList(Collection<T> collection) {
		return new ArrayList<T>(collection);
	}
}
