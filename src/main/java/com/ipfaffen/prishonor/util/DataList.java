package com.ipfaffen.prishonor.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Isaias Pfaffenseller
 */
public class DataList<T> extends ArrayList<T> {

	public DataList() {
		super();
	}

	/**
	 * @param c
	 */
	public DataList(Collection<T> c) {
		super(c);
	}

	/**
	 * @param initialCapacity
	 */
	public DataList(int initialCapacity) {
		super(initialCapacity);
	}

	/**
	 * @return
	 */
	public T first() {
		if(!isEmpty()) {
			return (T) get(0);
		}
		return null;
	}

	/**
	 * @return
	 */
	public T last() {
		if(!isEmpty()) {
			return (T) get(size() - 1);
		}
		return null;
	}

	/**
	 * @param fromIndex
	 * @return
	 */
	public List<T> from(int fromIndex) {
		return subList(fromIndex, size());
	}

	/**
	 * @param toIndex
	 * @return
	 */
	public List<T> to(int toIndex) {
		toIndex++;
		return subList(0, (size() > toIndex ? toIndex : size()));
	}
}