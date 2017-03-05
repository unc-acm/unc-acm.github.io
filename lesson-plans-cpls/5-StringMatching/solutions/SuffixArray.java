import java.util.*;
import java.io.*;

public class SuffixArray {
    // Instance Variables
    public static final int ALPHABET_SIZE = 128;

    String[] suffixes;
    int[] sortedSuffix;
    int[] inverseSortedSuffix;

    // Constructors
    public SuffixArray(String text) {
        int N = text.length();
        suffixes = new String[N];
        sortedSuffix = new int[N];
        inverseSortedSuffix = new int[N];

        for (int i = N - 1 ; i >= 0 ; i--) {
            suffixes[N - i - 1] = text.substring(i);
        }

        //System.out.println(Arrays.toString(suffixes));

        // first round radix sort on the first letter
        List<List<Integer>> radix = new ArrayList<List<Integer>>();
        for (int i = 0 ; i < ALPHABET_SIZE ; i++) {
            radix.add(new ArrayList<Integer>());
        }

        for (int i = 0 ; i < N ; i++) {
            radix.get((int)suffixes[i].charAt(0)).add(i);
        }

        int[] uncertainRange = new int[N];

        int x = 0;
        int o;

        for (int i = 0 ; i < ALPHABET_SIZE ; i++) {
            o = x;
            if (radix.get(i).size() > 1) {
                Collections.sort(radix.get(i));
            }
            for (int j = 0 ; j < radix.get(i).size() ; j++) {
                sortedSuffix[x] = radix.get(i).get(j);
                inverseSortedSuffix[radix.get(i).get(j)] = x;
                x++;
            }
            for (; o < x ; o++) {
                uncertainRange[o] = x; // this variable is used to keep track of where an yet-to-be-sorted range lasts until
            }
        }

        boolean continuing = true;
        int i, endpoint;
        int[] buckets = new int[N];

        //for (int j = 0 ; j < N ; j++) {
        //    System.out.println("" + sortedSuffix[j] + " " + inverseSortedSuffix[j] + " " + uncertainRange[j]);
        //}

        // prefix doubling time!
        for (int step = 1 ; continuing && step < N ; step = step * 2) {
            //System.out.println("++++" + step + "++++");
            continuing = false; // we assume we don't need to continue until we have to change stuff
            i = 0;
            while (i < N) {
                if (uncertainRange[i] != i + 1) { // this range is uncertain
                    //System.out.println("--" + i + "-" + uncertainRange[i] + "--");
                    continuing = true;
                    endpoint = uncertainRange[i]; // the end of the uncertain range

                    for (int j = 0 ; j < N ; j++) {
                        buckets[j] = -1; // reset the buckets
                    }

                    x = 0;
                    int[] correctOrder = new int[endpoint - i];
                    int[] uncertainSuffixRange = new int[endpoint - i];

                    for (int j = i ; j < endpoint ; j++) {
                        // this step is important, it uses the fact that
                        // suffixes of suffixes are earlier in the original list
                        // except step indices
                        //if (sortedSuffix[j] - step < 0) {
                        //    correctOrder[x] = sortedSuffix[j];
                        //    uncertainSuffixRange[x] = -1;
                        //    x++;
                        //}
                        //else {
                        buckets[inverseSortedSuffix[sortedSuffix[j] - step]] = sortedSuffix[j];
                        //}
                    }

                    for (int j = 0 ; j < N ; j++) {
                        if (buckets[j] != -1) {
                            correctOrder[x] = buckets[j];
                            uncertainSuffixRange[x] = uncertainRange[j]; // check if the ordering of the next elements re uncertain
                            x++;
                        }
                    }

                    int lastUncertainRange = uncertainSuffixRange[0];
                    int lastUncertainIndex = i;

                    for (int j = i ; j < endpoint ; j++) {
                        // actually updating everything
                        sortedSuffix[j] = correctOrder[j - i];
                        inverseSortedSuffix[correctOrder[j - i]] = j;

                        if (uncertainSuffixRange[j - i] != lastUncertainRange) {
                            for (int k = j - 1 ; k >= lastUncertainIndex ; k--) {
                                uncertainRange[k] = j;
                            }
                            lastUncertainRange = uncertainSuffixRange[j - i];
                            lastUncertainIndex = j;
                        }
                    }
                    i = endpoint - 1;
                }
                i++;
            }

            //for (int j = 0 ; j < N ; j++) {
            //    System.out.println("" + j + ": " + sortedSuffix[j] + " " + inverseSortedSuffix[j] + " " + uncertainRange[j]);
            //}
        }
    }

    // Functions
    private int prefixComparison(String a, String b) {
        // returns the comparison of b to the prefix of a
        if (b.length() > a.length()) {
            return a.compareTo(b);
        }

        for (int i = 0 ; i < b.length() ; i++) {
            if (a.charAt(i) > b.charAt(i)) {
                return 1;
            }
            else if (a.charAt(i) < b.charAt(i)) {
                return -1;
            }
        }
        return 0;
    }

    public int countOccurences(String pattern) {
        // count the number of occurnce of pattern in the string
        int upper, lower, l, r;
        l = 0; r = sortedSuffix.length - 1;

        // binary search for lower
        while (l <= r) {
            int m = (l + r) / 2;
            int flag = prefixComparison(suffixes[sortedSuffix[m]], pattern);

            if (flag >= 0) {
                r = m - 1;
            }
            else {
                l = m + 1;
            }
        }

        lower = l;

        l = 0; r = sortedSuffix.length - 1;
        // binary search for upper
        while (l <= r) {
            int m = (l + r) / 2;
            int flag = prefixComparison(suffixes[sortedSuffix[m]], pattern);

            if (flag > 0) {
                r = m - 1;
            }
            else {
                l = m + 1;
            }
        }
        upper = r + 1;

        return (upper - lower > 0) ? (upper - lower) : 0;
    }

    // main
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String inputs[] = br.readLine().split(" ");
        int N = Integer.parseInt(inputs[0]);
        int M = Integer.parseInt(inputs[1]);
        int K = Integer.parseInt(inputs[2]);

        SuffixArray sa = new SuffixArray(br.readLine());

        int occurences = 0;

        for (int i = 0 ; i < M ; i++) {
            occurences += sa.countOccurences(br.readLine());
        }

        System.out.println(occurences);
    }
}
