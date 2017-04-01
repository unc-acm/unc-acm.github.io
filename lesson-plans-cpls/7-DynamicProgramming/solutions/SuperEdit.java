import java.util.*;
import java.io.*;

public class SuperEdit {
    public static int LARGE_NUMBER = Integer.MAX_VALUE / 2;
    public static void main(String[] args) {
        Scanner sin = new Scanner(System.in);
        String a = sin.next();
        String b = sin.next();
        System.out.println(superEditDistance(a.toCharArray(), b.toCharArray()));
    }

    public static int superEditDistance(char[] a, char[] b) {
        if (a.length < b.length) {
            return superEditDistance(b, a);
            // because of kill, we want to know which one is longer
            // spcifically we want a to be longer
        }
        // solution matrix
        // prefixDistance[i][j] is the number of edits needed to get from a[:i]
        // to b[:j]
        int[][] prefixDistance = new int [a.length + 1][b.length + 1];
        char[][] action = new char[a.length + 1][b.length + 1];

        for (int i = 0 ; i <= a.length ; i++) {
            prefixDistance[i][0] = i;
            action[i][0] = 'i';
        }

        for (int j = 0 ; j <= b.length ; j++) {
            prefixDistance[0][j] = j;
            action[0][j] = 'd';
        }

        // we consider kill in the end, first let's just do insert, delete,
        // substitute and twiddle
        for (int i = 1 ; i <= a.length ; i++) {
            for (int j = 1 ; j <= b.length ; j++) {
                int substitutionCost = (a[i - 1] == b[j - 1]) ? 0 : 1;
                int twiddleCost = (i > 1 && j > 1 && a[i - 1] == b[j - 2] && a[i - 2] == b[j - 1]) ? 1 : LARGE_NUMBER;
                prefixDistance[i][j] = prefixDistance[i - 1][j] + 1;
                action[i][j] = 'i';
                if (prefixDistance[i][j-1] + 1 < prefixDistance[i][j]) {
                    prefixDistance[i][j] = prefixDistance[i][j-1] + 1;
                    action[i][j] = 'd';
                }
                if (prefixDistance[i-1][j-1] + substitutionCost < prefixDistance[i][j]) {
                    prefixDistance[i][j] = prefixDistance[i-1][j-1] + substitutionCost;
                    action[i][j] = 's';
                }
                if (i > 1 && j > 1 && prefixDistance[i-2][j-2] + twiddleCost < prefixDistance[i][j]) {
                    prefixDistance[i][j] = prefixDistance[i-2][j-2] + twiddleCost;
                    action[i][j] = 't';
                }
            }
        }

        //for (int[] row : prefixDistance) {
        //    System.out.println(Arrays.toString(row));
        //}

        // now we kill
        // since a is longer, we should kill a
        // so we consider all i in [0, a.length) and compare
        // prefixDistance[i][b.length] + cost(kill) against
        // prefixDistance[a.length][b.length]
        
        int minCost = prefixDistance[a.length][b.length];


        System.out.print("       |       |");
        for (int j = 0 ; j < b.length ; j++) {
            System.out.printf("   %c   |", b[j]);
        }
        System.out.println();
        for (int i = 0 ; i <= a.length ; i++) {
            if (i > 0) {
                System.out.printf("   %c   |", a[i - 1]);
            }
            else {
                System.out.printf("       |");
            }
            for (int j = 0 ; j <= b.length ; j++) {
                System.out.printf("%2d (%c) |", prefixDistance[i][j], action[i][j]);
            }
            System.out.println();
        }

        for (int i = 0 ; i < a.length ; i++) {
            if (minCost > prefixDistance[i][b.length] + 1) {
                minCost = prefixDistance[i][b.length] + 1;
            }
        }

        return minCost;
    }
}
