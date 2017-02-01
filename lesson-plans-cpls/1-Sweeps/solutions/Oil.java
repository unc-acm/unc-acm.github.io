import java.util.*;
import java.io.*;

class Main {

    static class Segment {
        // each reserve of oil
        public long l, r, y;
        public long reserveSize;

        public Segment(long _l, long _r, long _y) {
            l = _l; r = _r; y = _y;
            reserveSize = r - l;
        }

        public String toString() {
            return l + " - " + r + " at " + y;
        }
    }

    static class Event implements Comparable<Event> {
        // Event used for scanning radially
        public double angle;
        public int segmentID;

        public Event(long dx, long dy, int _sID) {
            angle = Math.atan2(dy, dx); // we use atan2 b/c it's always +
            segmentID = _sID;
        }

        public int compareTo(Event e) {
            //if (this.angle - e.angle > 0) {
            //    return 1;
            //} else {
            //    return -1;
            //}
            if (this.angle - e.angle > 0.0005) {
                return 1;
            }
            else if ((this.angle - e.angle <= 0.0005) &&
                     (this.angle - e.angle >= -0.0005)) {
                return 0;
            }
            else {
                return -1;
            }
        }

        public String toString() {
            return "Segment " + segmentID + " (" + angle + ")";
        }
    }

    public static void main(String[] args) throws IOException {
        // Scanner sc = new Scanner(System.in);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        while (reader.ready()) { // while there is input
        //while (sc.hasNext()) {
            String line = reader.readLine();
            int n = Integer.parseInt(line);
            //int n = sc.nextInt();

            List<Segment> deposits = new ArrayList<Segment>();
            int empty = 0;

            for (int i = 0 ; i < n ; i++) {
                String inputs[] = reader.readLine().split(" ");

                long l = Long.parseLong(inputs[0]);
                long r = Long.parseLong(inputs[1]);
                long y = Long.parseLong(inputs[2]);
                
                //int l = sc.nextInt();
                //int r = sc.nextInt();
                //int y = sc.nextInt();

                if (r != l) {
                    if (r < l) {
                        // make sure r > l
                        long tmp = r;
                        r = l;
                        l = tmp;
                    }
                    // empty reserves should be ignored
                    deposits.add(new Segment(l, r, y));
                }
                else {
                    empty++;
                }
            }

            n = n - empty;

            //for (int i = 0 ; i < n ; i++) {
            //    System.out.println(i + ": " + deposits.get(i));
            //}

            System.out.println(getMaxExtraction(deposits));
        }
    }

    static long getMaxExtraction(List<Segment> deposits) {
        long maxExtraction = 0;

        for (int i = 0 ; i < deposits.size() ; i++) {
            long currentMax = Math.max(radialSweep(deposits, i, true),
                                      radialSweep(deposits, i, false));

            currentMax += deposits.get(i).reserveSize; // also count self :)

            if (currentMax > maxExtraction) {
                //System.out.println(deposits.get(i));
                //System.out.println(currentMax);
                maxExtraction = currentMax;
            }
        }
        return maxExtraction;
    }

    static long radialSweep(List<Segment> deposits, int origin, boolean left) {
        long y = deposits.get(origin).y;
        long x;
        if (left) {
            x = deposits.get(origin).l;
        }
        else {
            x = deposits.get(origin).r;
        }

        List<Event> endpoints = new ArrayList<Event>();

        for (int i = 0 ; i < deposits.size() ; i++) {
            if (i != origin) {
                long x1 = deposits.get(i).l;
                long x2 = deposits.get(i).r;
                long y1 = deposits.get(i).y;

                if (y1 - y > 0) { // only consider the top 180 degrees
                    endpoints.add(new Event(x1 - x, y1 - y, i));
                    endpoints.add(new Event(x2 - x, y1 - y, i));
                }
            }
        }

        Collections.sort(endpoints);
        
        //System.out.println("Origin: " + x + ", " + y);
        //for (Event e : endpoints) {
        //    System.out.println(e);
        //}
        //System.out.println("==========================");

        Set<Integer> active = new TreeSet<Integer>();
        long maxForOrigin = 0;
        long currentDeposit = 0;

        for (int i = 0 ; i < endpoints.size() ; i++) {
            Event current = endpoints.get(i);
            //System.out.println(current.angle);
            
            if (Math.abs(current.angle - 0) > 0.001 &&
                Math.abs(current.angle - Math.PI) > 0.001) {
                // aligned wells cannot be drilled as they must reach the
                // surface
                if (active.add(current.segmentID)) {
                    // if this succeeds, this is a new segmentID
                    currentDeposit += deposits.get(current.segmentID).reserveSize;
                    //System.out.println("Added" + current + " (" + Math.toDegrees(current.angle) + ")");
                    if (currentDeposit > maxForOrigin) {
                        maxForOrigin = currentDeposit;
                    }
                }
                else {
                    long reserveToRemove = 0;

                    while (i + 1 < endpoints.size() && doubleEqual(endpoints.get(i + 1).angle, current.angle)) { 
                        Event overlapped = endpoints.get(i + 1);
                        if (active.contains(overlapped.segmentID)) {
                            reserveToRemove += deposits.get(overlapped.segmentID).reserveSize;
                            active.remove(overlapped.segmentID);
                        }
                        else {
                            active.add(overlapped.segmentID);
                            currentDeposit += deposits.get(overlapped.segmentID).reserveSize;
                            //System.out.println("Added" + current + " (" + Math.toDegrees(current.angle) + ")");
                            if (currentDeposit > maxForOrigin) {
                                maxForOrigin = currentDeposit;
                            }
                        }
                        i++;
                    }
                    active.remove(current.segmentID);
                    currentDeposit -= deposits.get(current.segmentID).reserveSize - reserveToRemove;
                    //System.out.println("Removed " + current + " (" + Math.toDegrees(current.angle) + ")");
                }
            }
        }

        //System.out.println("DONE " + maxForOrigin);
        
        return maxForOrigin;
    }

    static boolean doubleEqual(double a, double b) {
        return Math.abs(a - b) < 0.00001;
    }
}
