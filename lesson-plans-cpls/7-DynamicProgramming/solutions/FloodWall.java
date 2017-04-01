import java.util.*;
import java.io.*;

public class FloodWall {
    // Instance Variables
    static class Painting implements Comparable<Painting> {
        long left, right, range;

        public Painting(long l, long r) {
            left = l; right = r;
            range = right - left;
        }

        public int compareTo(Painting other) {
            if (right == other.right) {
                // cmp left
                return (left > other.left) ? 1 : -1; // long -> int is not allowed...
            }
            return (right > other.right) ? 1 : -1;
        }
    }

    // Functions
    public static void main(String[] args) {
        Scanner sin = new Scanner(System.in);

        while (sin.hasNext()) {
            int N = sin.nextInt();
            Painting[] submissions = new Painting[N];
            long[] rightEnds = new long[N];
            Map<Long, Long> optimalUpTo = new HashMap<Long, Long>();

            for (int i = 0 ; i < N ; i++) {
                long l = sin.nextLong();
                long r = sin.nextLong();
                submissions[i] = new Painting(l, r);
                optimalUpTo.put(r, 0l);
                rightEnds[i] = r;
            }

            Arrays.sort(submissions);
            Arrays.sort(rightEnds);

            for (int i = 0 ; i < N ; i++) {
                Painting current = submissions[i];

                long currentBest = (i > 0) ? optimalUpTo.get(rightEnds[i - 1]) : 0;
                long containingThis = current.range;
                if (current.left >= submissions[0].right) { // so there is a search...
                    int index = Arrays.binarySearch(rightEnds, current.left);
                    if (index == rightEnds.length) {
                        index--;
                    }
                    if (index < 0) {
                        index = -1 * index - 2;
                    }

                    containingThis += optimalUpTo.get(rightEnds[index]);
                }
                if (containingThis > currentBest) {
                    optimalUpTo.put(rightEnds[i], containingThis);
                }
                else {
                    optimalUpTo.put(rightEnds[i], currentBest);
                }
            }

            System.out.println(optimalUpTo.get(rightEnds[N - 1]));

            break;
        }
    }
}
