package frc.team832.lib.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class NumberListUtils {

    /**
     * @param collection an ArrayList of Comparable objects
     * @return the median of collection
     */
    public static <T extends Number> double median(List<T> collection, Comparator<T> comp) {
        double result;
        int n = collection.size() / 2;

        if (collection.size() % 2 == 0)  // even number of items; find the middle two and average them
            result = (nthSmallest(collection, n - 1, comp).doubleValue() + nthSmallest(collection, n, comp).doubleValue()) / 2.0;
        else                      // odd number of items; return the one in the middle
            result = nthSmallest(collection, n, comp).doubleValue();

        return result;
    }

    public static <T extends Number> String toString(List<T> collection) {
        return toString(collection, "");
    }

    public static <T extends Number> String toString(List<T> collection, String suffix) {
        StringJoiner joiner = new StringJoiner(", ");
        for (T x : collection) {
            String s = String.valueOf(x.doubleValue()) + suffix;
            joiner.add(s);
        }
        return joiner.toString();
    }

    /**
     * @param collection an ArrayList of Numbers
     * @return the mean of collection
     */
    public static double mean(final List<? extends Number> collection) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final Number number: collection) {
            sum = sum.add(BigDecimal.valueOf(number.doubleValue()));
        }
        return (sum.doubleValue() / collection.size());
    }

    /**
     * @param collection a collection of Comparable objects
     * @param n  the position of the desired object, using the ordering defined on the collection elements
     * @return the nth smallest object
     */

    public static <T> T nthSmallest(List<T> collection, int n, Comparator<T> comp) {
        T result, pivot;
        ArrayList<T> underPivot = new ArrayList<>(), overPivot = new ArrayList<>(), equalPivot = new ArrayList<>();

        // choosing a pivot is a whole topic in itself.
        // this implementation uses the simple strategy of grabbing something from the middle of the ArrayList.

        pivot = collection.get(n/2);

        // split collection into 3 lists based on comparison with the pivot

        for (T obj : collection) {
            int order = comp.compare(obj, pivot);

            if (order < 0)        // obj < pivot
                underPivot.add(obj);
            else if (order > 0)   // obj > pivot
                overPivot.add(obj);
            else                  // obj = pivot
                equalPivot.add(obj);
        } // for each obj in collection

        // recurse on the appropriate collection

        if (n < underPivot.size())
            result = nthSmallest(underPivot, n, comp);
        else if (n < underPivot.size() + equalPivot.size()) // equal to pivot; just return it
            result = pivot;
        else  // everything in underPivot and equalPivot is too small.  Adjust n accordingly in the recursion.
            result = nthSmallest(overPivot, n - underPivot.size() - equalPivot.size(), comp);

        return result;
    }
}
