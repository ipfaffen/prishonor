package com.ipfaffen.prishonor.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;

/**
 * @author John Hendrikx
 * @author Isaias Pfaffenseller
 */
public class WeakBinder {
	private static WeakBinder weakBinder;

	private final List<Object> hardRefs = new ArrayList<Object>();
	private final Map<ObservableValue<?>, WeakInvalidationListener> invalidationListeners = new HashMap<ObservableValue<?>, WeakInvalidationListener>();

	public void unbindAll() {
		for(ObservableValue<?> observableValue: invalidationListeners.keySet()) {
			observableValue.removeListener(invalidationListeners.get(observableValue));
		}

		hardRefs.clear();
		invalidationListeners.clear();
	}

	/**
	 * @param property
	 * @param dest
	 */
	public <T> void bind(final Property<T> property, final ObservableValue<? extends T> dest) {
		InvalidationListener invalidationListener = new InvalidationListener() {
			@Override
			public void invalidated(Observable observable) {
				property.setValue(dest.getValue());
			}
		};

		WeakInvalidationListener weakInvalidationListener = new WeakInvalidationListener(invalidationListener);

		invalidationListeners.put(dest, weakInvalidationListener);

		dest.addListener(weakInvalidationListener);
		property.setValue(dest.getValue());

		hardRefs.add(dest);
		hardRefs.add(invalidationListener);
	}

	/**
	 * @return
	 */
	public static WeakBinder get() {
		if(weakBinder == null) {
			weakBinder = new WeakBinder();
		}
		return weakBinder;
	}
}