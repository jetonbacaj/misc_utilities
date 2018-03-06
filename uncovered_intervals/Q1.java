import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
//import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Comparator;
import java.util.List;

public class Q1 {
    static class Interval {
        int start;
        int end;
        public Interval(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    private static final Comparator<Interval> INTERVAL_COMPARATOR = new IntervalComparator();

    /* A simple comparator, so that we can use it to sort the input - the `intervals` list */
    private static final class IntervalComparator implements Comparator<Interval> {
        @Override
        public int compare(final Interval o1, final Interval o2) {
            return Integer.compare(o1.start, o2.start);
        }
    }

    /*
     * The runtime of this method is nlogn, where n is the number of
     * interval tuples supplied.
     * The reason it is of that order of runtime is that those entries are
     * sorted by the `start` attribute of the interval.  The sort algorithm
     * runs in that order (according to JavaDocs for List.sort(...)).
     *
     * The second loop is the traversal of the `intervals` (post sorted) list.
     * This is of order n, therefore (n + nlogn) => 2nlogn => nlogn.
     *
     * The space complexity, other than the input, is constant plus the space
     * needed to hold the "uncovered" tuples, which would be, at worst, order
     * of n (if all of the input tuples had gaps in coverage, for example).
     * The only other piece of extraneous data we have is `maxEnd` attribute,
     * which is to keep the maximum "end" attribute seen, up until that point
     * in time during `intervals` list traversal.
     *
     *
     */
    private static List<Interval> uncoveredIntervals(List<Q1.Interval> intervals) {
        final List<Interval> uncovered = new LinkedList<>();

        if (intervals != null && !intervals.isEmpty()) {

            // sort by start attribute of Interval - in place, so no extra space is
            // needed... though at the expense of "dirtying" the input, thus if
            // the list passed it needed to be sent elsewhere, it would be in this
            // new order ... which may or may not matter (for this narrow solution,
            // it does not matter)
            intervals.sort(INTERVAL_COMPARATOR);

            // let's make the base case the first element of the list
			//
			// this does a shift if `intervals` is an ArrayList!!! If so, another O(n) operation ... have to avoid this!
			// But in our case, since everything is a LinkedList, we're OK.
			//
            final Interval baseInterval = intervals.remove(0); 
            int maxEnd = baseInterval.end; 

            for (final Interval interval : intervals) {
                if (interval.start > maxEnd) {
                    uncovered.add(new Interval(maxEnd, interval.start));
                }

                // ensure we're keeping the max `end` attribute of intervals
                maxEnd = Math.max(maxEnd, interval.end);
            }
        }

        return uncovered;
    }

    /*
     *  Hey! You probably don't need to edit anything below here
     */

    private static List<Q1.Interval> readIntervals(final InputStream is) throws IOException {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        final List<Q1.Interval> intervals = new LinkedList<Q1.Interval>();
        String line;
        while ((line = reader.readLine()) != null && line.length() != 0) {
            intervals.add(toInterval(line));
        }
        return intervals;
    }

    private static Q1.Interval toInterval(final String line) {
        final String[] tokenizedInterval = line.split(" ");

        return new Interval(Integer.valueOf(tokenizedInterval[0]),
                            Integer.valueOf(tokenizedInterval[1]));
    }

    public static void main(String... args) throws IOException {
        final List<Q1.Interval> intervals = Q1.readIntervals(System.in);
        final List<Q1.Interval> uncovered = Q1.uncoveredIntervals(intervals);
        for (final Interval i : uncovered) {
            System.out.println(i.start + " " + i.end);
        }
    }
}
