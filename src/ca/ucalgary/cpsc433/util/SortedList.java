package ca.ucalgary.cpsc433.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.ListIterator;
import java.util.PriorityQueue;

/**
 * The <code>BinaryList</code> is a <code>List</code> implementation used
 * to quickly sort elements as you add them. Ideally, this will cut down
 * the complexity cost of sorting elements, as it it provides a more
 * linear-based complexity. As the number of elements increase, the time it
 * takes for this system to sort elements is increasing at a rate lower
 * that most sorting algorithms.
 * <p>
 * It is assumed that the list, before and after, adding an element is in a
 * state where all the elements are in sequential order. A limitation of
 * this, is the removal of compatibility with many of the basic util. Some
 * of these include {@link SortedList#add(int, T)} and {@link
 * SortedList#set(int, T)}.
 * <p>
 * These methods were removed due to the potentially hazardous effects that
 * they may have on the list. The index provided may not match the correct
 * index that the element belongs at. This will mean the list may no longer
 * be sorted.
 * <p>
 * Whenever you add possibly random-order elements into a list, then sort
 * the list: this implementation will tend to perform better.
 * <p>
 * This algorithm is not stable - meaning that elements may not remain in
 * the order they were added in. In the case that elements that
 * comparatively are equal, the order in which they are added will dictate
 * their index.
 *
 * @author Obicere
 * @version 1.0
 */
public class SortedList<T extends Comparable<? super T>> extends ArrayList<T> {

    /**
     * The <code>comparator</code> instance is used to provide a separate
     * way to sort the elements. This is great if you wish to override the
     * implemented <code>compareTo(T, T)</code> method in the class
     * <code>T</code>.
     */
    private final Comparator<T> comparator;

    /**
     * Creates an empty <code>BinaryList</code> with a <code>null
     * comparator</code>.
     * <p>
     * This list has a <code>null</code> comparator and an initial capacity
     * of <code>10</code>.
     */
    public SortedList() {
        comparator = null;
    }

    /**
     * Creates an empty <code>BinaryList</code> with a <code>null
     * comparator</code>.
     * <p>
     * This list will have the set <code>initialCapacity</code>, which can
     * lead to potentially faster insertion speeds.
     *
     * @param initialCapacity The initial capacity to buffer this list
     *                        for.
     */
    public SortedList(final int initialCapacity) {
        super(initialCapacity);
        comparator = null;
    }

    /**
     * Creates a new binary list that functions off of the given
     * <code>comparator</code>. This <code>comparator</code> will be given
     * precedence over the standard comparator, if <code>T</code>
     * implements {@link java.lang.Comparable}.
     *
     * @param comparator The comparator you wish to use in place of {@link
     *                   Comparable#compareTo(Object)}
     */
    public SortedList(final Comparator<T> comparator) {
        this.comparator = comparator;
    }

    /**
     * Creates a new binary list that functions off of the given
     * <code>comparator</code>. This <code>comparator</code> will be given
     * precedence over the standard comparator, if <code>T</code>
     * implements {@link java.lang.Comparable}.
     *
     * @param initialCapacity The initial capacity to buffer this list
     *                        for.
     * @param comparator      The comparator you wish to use in place of
     *                        {@link Comparable#compareTo(Object)}
     */
    public SortedList(final int initialCapacity, final Comparator<T> comparator) {
        super(initialCapacity);
        this.comparator = comparator;
    }

    /**
     * Creates a <code>BinaryList</code> with the elements from the
     * specified collection. These elements need not be in order, as they
     * get sorted upon indexing.
     *
     * @param data The default elements for the instance
     */

    public SortedList(final Collection<T> data) {
        super(data.size());
        addAll(new ArrayList<>(data));
        comparator = null;
    }

    /**
     * Creates a filled <code>BinaryList</code> with the elements from the
     * specified collection. These elements do get sorted using the
     * specified <code>comparator</code> instance, similar to how they will
     * be added later.
     *
     * @param data       The default elements for the instance
     * @param comparator The comparator you wish to use in place of {@link
     *                   Comparable#compareTo(Object)}
     */

    public SortedList(final Collection<T> data, final Comparator<T> comparator) {
        super(data.size());
        addAll(new ArrayList<>(data));
        this.comparator = comparator;
    }

    public SortedList(final PriorityQueue<T> data) {
        super(data.size());
        while (!data.isEmpty()) {
            super.add(data.poll());
        }
        comparator = null;
    }

    /**
     * Not supported. This will throw an error in place of compromising the
     * integrity of the contents of the list.
     *
     * @param index   Ignored
     * @param content Ignored
     * @return Ignored.
     * @throws java.lang.UnsupportedOperationException always.
     */

    @Override
    public T set(final int index, final T content) {
        throw new UnsupportedOperationException("Modification of elements could break integrity.");
    }

    /**
     * Not supported. This will throw an error in place of compromising the
     * integrity of the contents of the list.
     *
     * @param index   Ignored
     * @param content Ignored
     * @throws java.lang.UnsupportedOperationException always.
     */

    @Override
    public void add(final int index, final T content) {
        throw new UnsupportedOperationException("Index must be dynamic.");
    }

    /**
     * This overrides the {@link java.util.ArrayList#add(Object)} method.
     *
     * @param content The data you wish to add the list
     * @return <code>true</code> for all cases it was added
     */

    @Override
    @SuppressWarnings("unchecked")
    public boolean add(final T content) {
        int index = findIndexFor(content);
        if (index < 0) {
            index = ~index;
        }
        super.add(index, content);
        return true;
    }

    /**
     * Delegates to {@link SortedList#add(T)}. This will add all of the
     * elements sequentially, then return <code>true</code> if all of the
     * indexing returned <code>true</code>.
     *
     * @param content The elements you wish to add to the list.
     * @return <code>true</code> for all cases the <code>content</code> was
     * added
     */

    @Override
    public boolean addAll(final Collection<? extends T> content) {
        boolean clear = true;
        for (final T t : content) {
            clear = clear && add(t);
        }
        return clear;
    }

    /**
     * Not supported. This will throw an error in place of compromising the
     * integrity of the contents of the list.
     *
     * @param index   Ignored
     * @param content Ignored
     * @return Ignored
     * @throws java.lang.UnsupportedOperationException always.
     */

    @Override
    public boolean addAll(final int index, final Collection<? extends T> content) {
        throw new UnsupportedOperationException("Index must be dynamic.");
    }

    /**
     * Uses a binary search algorithm to find the index of a given object
     * <code>t</code>.
     *
     * @param t The object to look for
     * @return The index if found, otherwise <code>-1</code>
     */

    @SuppressWarnings("unchecked")
    @Override
    public int indexOf(final Object t) {
        return findIndexFor((T) t);
    }

    /**
     * Not supported. This will throw an error in place of compromising the
     * integrity of the contents of the list.
     *
     * @return Ignored
     * @throws java.lang.UnsupportedOperationException always.
     */

    @Override
    public ListIterator<T> listIterator() {
        throw new UnsupportedOperationException("Cannot iterate over list with mutable iterator.");
    }

    /**
     * This method will find the index of the element you wish to add. To
     * therefore add the <code>content</code>, you must split the
     * <code>data</code> from [0, <code>index</code>) and
     * [<code>index</code>, <code>size</code>). From there, you must grow
     * the <code>data</code> by one, then add this element in at the
     * respective <code>index</code> returned from this method. This will
     * maintain the order.
     *
     * @param content The content to find the index of.
     * @return An index restrained by [0, <code>size</code>).
     */

    @SuppressWarnings("unchecked")
    private int findIndexFor(final T content) {
        return Collections.binarySearch(this, content);
        /*if (comparator == null) {
            if (content instanceof Comparable) {
                return Collections.binarySearch((List<Comparable<T>>) this, content);
            } else {
                throw new IllegalStateException("No way to compare data types for: " + content);
            }
        } else {
            return Collections.binarySearch(this, content, comparator);
        }*/
    }
}