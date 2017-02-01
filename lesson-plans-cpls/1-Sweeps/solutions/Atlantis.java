import java.util.*;
import java.io.*;

class Main {

    static class Rectangle {
        // this is an inner class used to hold rectangles
        // in 401 you are told that things should not be public
        // in competitive programming you and the grader are the only ones using
        // the code, therefore security doesn't matter :)
        // make everything public so it makes life easier
        public double l, r, t, b; // left right top bottom
        
        public Rectangle(double x0, double y0, double x1, double y1) {
            l = x0; r = x1;
            t = y0; b = y1;
        }
    }

    static class Event implements Comparable {
        public double value;
        public boolean begin;
        public int rectIndex;

        public Event (double _v, boolean _b, int _index) {
            value = _v;
            begin = _b;
            rectIndex = _index;
        }

        public int compareTo(Object o) {
            return (int)(this.value - ((Event) o).value);
        }
    }

    // Functions
    public static void main(String[] args) throws IOException {
        // the main method usually does the I/O operations for the problem
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int testCaseCounter = 1;

        int n = Integer.parseInt(reader.readLine());

        while (n != 0) {
            // the code for handling each test case is here
            // exits when n == 0

            List<Rectangle> rects = new ArrayList<Rectangle>();

            for (int i = 0 ; i < n ; i++) {
                String inputs[] = reader.readLine().split(" ");

                double x0 = Double.parseDouble(inputs[0]);
                double y0 = Double.parseDouble(inputs[1]);
                double x1 = Double.parseDouble(inputs[2]);
                double y1 = Double.parseDouble(inputs[3]);

                rects.add(new Rectangle(x0, y0, x1, y1));
            }

            double area = findTotalArea(rects);

            System.out.println("Test case #" + testCaseCounter);
            System.out.printf("Total explored area: %.2f\n\n", area);

            n = Integer.parseInt(reader.readLine());
            testCaseCounter++;
        }

        return;
    }

    static double findTotalArea(List<Rectangle> rects) {
        // where the solution to the problem actually is

        List<Event> xEvents = new ArrayList<Event>();
        Set<Integer> activeRectangles = new TreeSet<Integer>();

        for (int i = 0 ; i < rects.size() ; i++) {
            xEvents.add(new Event(rects.get(i).l, true, i));
            xEvents.add(new Event(rects.get(i).r, false, i));
        }

        Collections.sort(xEvents);

        double lastYRange = 0; // record the y range 
        double lastXCoor = 0;
        double totalArea = 0;

        // sweep in the x direction
        for (int i = 0 ; i < xEvents.size() ; i++) {
            Event current = xEvents.get(i);

            totalArea += lastYRange * (current.value - lastXCoor);
    
            if (current.begin) {
                activeRectangles.add(current.rectIndex);
            }
            else {
                activeRectangles.remove(current.rectIndex);
            }

            lastXCoor = current.value;
            lastYRange = totalYRange(rects, activeRectangles);
        }

        return totalArea;
    }

    static double totalYRange(List<Rectangle> rects, Set<Integer> actives) {
        List<Event> yEvents = new ArrayList<Event>();

        for (Integer i : actives) {
            yEvents.add(new Event(rects.get(i).t, true, i));
            yEvents.add(new Event(rects.get(i).b, false, i));
        }

        Collections.sort(yEvents);

        int activeCounter = 0;
        double lastY = 0;
        double totalLength = 0;

        for (int i = 0 ; i < yEvents.size() ; i++) {
            Event current = yEvents.get(i);

            if (activeCounter > 0) {
                totalLength += current.value - lastY;
            }

            if (current.begin) {
                activeCounter++;
            }
            else {
                activeCounter--;
            }

            lastY = current.value;
        }

        return totalLength;
    }
}
