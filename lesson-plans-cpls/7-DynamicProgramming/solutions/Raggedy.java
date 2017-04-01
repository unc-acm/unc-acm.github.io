import java.util.*;
import java.io.*;

public class Raggedy {
    // Functions
    public static void main(String[] args) {
        Scanner sin = new Scanner(System.in);

        int L = sin.nextInt();
        sin.nextLine();

        while (L != 0) {
            List<String> paragraph = new ArrayList<String>();
            while (true) {
                String[] words = sin.nextLine().split(" ");
                if (words.length == 1 && words[0].equals("")) {
                    break;
                }
                for (String word : words) {
                    paragraph.add(word);
                }
            }

            int N = paragraph.size();
            int[] lengths = new int[N];
            for (int i = 0 ; i < N ; i++) {
                lengths[i] = paragraph.get(i).length();
            }

            //System.out.println(L);
            //for (int i : lengths) {
            //    System.out.print(i + " ");
            //}
            //System.out.println();

            // solution matrix :D
            // dp[n][l] == raggedness using the first n words, with l remaining
            // on the line, this makes combining things easy
            int[][] dp = new int[N][L + 1];
            // points to previous words, used for path reconstruction
            int[][] prev = new int[N][L + 1];

            for (int i = 0 ; i < N ; i++) {
                for (int j = 0 ; j <= L ; j++) {
                    dp[i][j] = Integer.MAX_VALUE / 3;
                }
            }

            // base case: first word on first line
            dp[0][L - lengths[0]] = (L - lengths[0]) * (L - lengths[0]);

            for (int i = 1 ; i < N ; i++) {
                for (int j = 0 ; j <= L ; j++) {
                    int spaceNeeded = lengths[i] + 1;

                    if (spaceNeeded <= j) { // enough space, put next word on same line
                        if (dp[i][j - spaceNeeded] > dp[i - 1][j] - j * j + (j - spaceNeeded) * (j - spaceNeeded)) {
                            dp[i][j - spaceNeeded] = dp[i - 1][j] - j * j + (j - spaceNeeded) * (j - spaceNeeded);
                            prev[i][j - spaceNeeded] = j;
                        }
                    }

                    // put next word on the next line
                    spaceNeeded--; // first word on next line doesn't need ' '
                    if (dp[i][L - spaceNeeded] > dp[i - 1][j] + (L - spaceNeeded) * (L - spaceNeeded)) {
                        dp[i][L - spaceNeeded] = dp[i - 1][j] + (L - spaceNeeded) * (L - spaceNeeded);
                        prev[i][L - spaceNeeded] = -1 * j; // negative for newline
                    }
                }
            }

            //for (int[] line : dp) {
            //    System.out.println(Arrays.toString(line));
            //}

            // finally we find configuration with least total raggedness
            // here is also where we correct for last line
            int min_r = Integer.MAX_VALUE;
            int j_opt = 0;

            for (int j = 0 ; j <= L ; j++) {
                if (min_r > dp[N - 1][j] - j * j) {
                    min_r = dp[N - 1][j] - j * j;
                    j_opt = j;
                }
            }

            recursively_print(prev, paragraph, N - 1, j_opt);
            System.out.println("\n===");

            L = sin.nextInt();
            sin.nextLine();
        }
    }

    public static void recursively_print(int[][] prev, List<String> words, int i, int j) {
        if (i == 0) {
            System.out.print(words.get(0));
        }
        else {
            int prev_word = prev[i][j];
            recursively_print(prev, words, i-1, Math.abs(prev_word));
            if (prev_word < 0) {
                System.out.println();
            }
            else {
                System.out.print(" ");
            }

            System.out.print(words.get(i));
        }
    }

}
