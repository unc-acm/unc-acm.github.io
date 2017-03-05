import java.util.*;
import java.io.*;

public class GoodSubstrings {
    // Instance Variables
    public static final long P = 67280421310721L;
    public static final int B = 128;

    // Functions
    static int solve(String input, boolean[] bad, int limit) {
        Set<Long> hashes = new TreeSet<Long>();
        int N = input.length();

        for (int i = 0 ; i < N ; i++) {
            int badCount = 0;
            long hash = 0;

            for (int j = i ; j < N ; j++) {
                // we are not even rolling, we can just expand
                hash = ((hash * B) % P) + ((int)input.charAt(j)) % P;

                if (bad[input.charAt(j) - 'a']) {
                    badCount++;
                }

                if (badCount <= limit) {
                    hashes.add(hash);
                }
            }
        }

        return hashes.size();
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String input = br.readLine();

        String badChars = br.readLine();

        boolean[] bad = new boolean[26];
        for (int i = 0 ; i < 26 ; i++) {
            bad[i] = (badChars.charAt(i) == '0');
        }

        int limit = Integer.parseInt(br.readLine());

        System.out.println(solve(input, bad, limit));
    }
}
