package com.ipfaffen.prishonor.util;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * @author Isaias Pfaffenseller
 */
public class ReversedIterator<T> implements Iterable<T> {
	
	private final List<T> original;

	/**
	 * @param original
	 */
	public ReversedIterator(List<T> original) {
		this.original = original;
	}

	@Override
	public Iterator<T> iterator() {
		final ListIterator<T> i = original.listIterator(original.size());

		return new Iterator<T>() {
			public boolean hasNext() {
				return i.hasPrevious();
			}

			public T next() {
				return i.previous();
			}

			public void remove() {
				i.remove();
			}
		};
	}

	/**
	 * @param original
	 * @return
	 */
	public static <T> ReversedIterator<T> get(List<T> original) {
		return new ReversedIterator<T>(original);
	}
}