import java.util.*;
import java.io.*;

public class Fedor {

    static class Coupon {
        public int l, r;

        public Coupon(int _l, int _r) {
            l = _l; r = _r;
        }

        public boolean coversRange(int lBount, int rBound) {
            return (l <= lBount && r >= rBound);
        }
    }

    static class Event implements Comparable<Event> {
        public boolean left;
        public int loc, couponID;

        public Event(boolean _l, int _loc, int _cID) {
            left = _l; loc = _loc; couponID = _cID;
        }

        public int compareTo(Event e) {
            if (this.left && ! e.left) {
                return -1;
            }
            else if (!this.left && e.left) {
                return 1;
            }
            else if (this.loc == e.loc) {
                return this.couponID - e.couponID;
            }
            return (this.loc - e.loc);
        }

        public String toString() {
            return "" + loc;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String inputs[] = reader.readLine().split(" ");

        int n = Integer.parseInt(inputs[0]);
        int k = Integer.parseInt(inputs[1]);

        List<Coupon> coupons = new ArrayList<Coupon>();

        for (int i = 0 ; i < n ; i++) {
            String coupon[] = reader.readLine().split(" ");
            int l = Integer.parseInt(coupon[0]);
            int r = Integer.parseInt(coupon[1]);

            coupons.add(new Coupon(l, r));
        }

        // unlike previous problems, b/c this problem requires multiple print
        // statements, we'll put output in solve to simplify code
        // (alternatively you can have solve return one thing and set some
        // global variable, but this is easier to understand)
        solve(coupons, k);
    }

    public static void solve(List<Coupon> coupons, int k) {
        List<Event> endpoints = new ArrayList<Event>();
        Map<Event, Event> leftLookup = new HashMap<Event, Event>();
        NavigableSet<Event> active = new TreeSet<Event>();

        for (int i = 0 ; i < coupons.size() ; i++) {
            Coupon c = coupons.get(i);
            Event l = new Event(true, c.l, i);
            Event r = new Event(false, c.r, i);
            endpoints.add(l);
            endpoints.add(r);
            leftLookup.put(r, l);
        }

        Collections.sort(endpoints);
        
        int counter = 0;
        int maxCouponsLength = 0;
        int maxCouponsLeft = 0, maxCouponsRight = 0;


        for (int i = 0 ; i < endpoints.size() ; i++) {
            Event e = endpoints.get(i);

            //System.out.println("Counter: " + active.size());

            //for (Event ev : active) {
            //    System.out.print(ev + " ");
            //}

            //System.out.println();

            if (e.left) {
                // this is the start of a coupon
                active.add(e);
            }
            else {
                // this is the end of a coupon
                Event begin = null;
                if (active.size() >= k) {
                    //Event begin = getKthElement(active, k);
                    if (active.size() > k * 2) {
                        Iterator<Event> it = active.iterator();
                        for (int j = 0 ; j < k - 1 ; j++) {
                            it.next();
                        }
                        begin = it.next();
                    }
                    else {
                        Iterator<Event> it = active.descendingIterator();
                        for (int j = 0 ; j < active.size() - k ; j++) {
                            it.next();
                        }
                        begin = it.next();
                    }
                    //Event begin = leftEndPts.get(leftEndPts.size() - (counter - k + 1));
                    //int extraSteps = 0;

                    //while (!active.contains(begin.couponID)) {
                    //    extraSteps++;
                    //    begin = leftEndPts.get(leftEndPts.size() - (counter - k + 1) - extraSteps);
                    //}

                    int KStackLength = e.loc - begin.loc + 1;

                    //if (KStackLength > 0) {
                    //    KStackLength++; // count left and right bound are inclusive
                    //}

                    //System.out.println(KStackLength + " (" + begin.loc + ", " + e.loc + ")");

                    if (KStackLength > maxCouponsLength) {
                        maxCouponsLength = KStackLength;
                        maxCouponsLeft = begin.loc;
                        maxCouponsRight = e.loc;
                        //System.out.println(maxCouponsLength + " (" + maxCouponsLeft + ", " + maxCouponsRight + ")");
                    }
                }

                //System.out.println(leftLookup.get(e));
                //System.out.println(active.contains(leftLookup.get(e)));

                active.remove(leftLookup.get(e));
            }
        }

        System.out.println(maxCouponsLength);

        int cUsed = 0;

        for (int i = 0 ; i < coupons.size() ; i++) {
            if (cUsed == k) {
                System.out.print("\n");
                break;
            }
            else {
                if (maxCouponsLength == 0 || coupons.get(i).coversRange(maxCouponsLeft, maxCouponsRight)) {
                    System.out.print((i+1) + " ");
                    cUsed++;
                }
            }
        }
    }

    //static Event getKthElement(NavigableSet<Event> active, int k) {
    //    Event middle = new Event(true, (active.first().loc + active.last().loc) / 2, 0);

    //    NavigableSet<Event> front = active.headSet(middle, false);

    //    if (k > front.size()) {
    //        NavigableSet<Event> back = active.tailSet(middle, true);

    //        if (k - front.size() < 5) {
    //            int steps = k - front.size();
    //            Iterator<Event> it = back.iterator();

    //            for (int i = 0 ; i < steps - 1 ; i++) {
    //                it.next();
    //            }

    //            return it.next();
    //        }

    //        else {
    //            return getKthElement(back, k - front.size());
    //        }
    //    }
    //    else {
    //        if (front.size() - k < 5) { // arbitrary number...
    //            int steps = front.size() - k;
    //            Iterator<Event> it = front.descendingIterator();

    //            for (int i = 0 ; i < steps - 1 ; i++) {
    //                it.next();
    //            }

    //            return it.next();
    //        }

    //        else {
    //            return getKthElement(front, k);
    //        }
    //    }
    //}
}
