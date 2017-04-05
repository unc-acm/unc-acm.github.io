import java.util.*;
import java.io.*;

public class GridMST {
    /* SOLUTION!!!
     * 
     * This solution is a different one from the one that we went over in class.
     * Partially because the solution went over in class ran into some
     * unexpected complications. So instead we decided to send out a much
     * simpler solution with Prims.
     *
     * this is basically the same idea except it uses Prim's instead of
     * Kruskals, this solution uses a 2D array of booleans to mark which points
     * have not yet been added to the MST. We start from an arbitrary point then
     * use dijkstras to find the closest point that we are still interest in.
     * Then we set dist at that new point to 0 and continue the search.
     *
     * this way dijkstras basically finds the global minimum edge between V and
     * U and we add that to the MST. We return when the tree is spanning.
     */
    
    // Instance Variables
    static final int INF = 10000000;
    static int N;
    static int[][] board;
    static int[][] points;
    static int[][] dist;
    static boolean[][] interested;

    static class VertexComp implements Comparable<VertexComp> {
        int x, y, d;

        public VertexComp(int _x, int _y, int _d) {
            x = _x;
            y = _y;
            d = _d;
        }

        @Override
        public int compareTo(VertexComp o) {
            return d - o.d;
        }
    }

    // Functions
    public static void main(String[] args) {
       Scanner sc = new Scanner(System.in);

       N = sc.nextInt();

       points = new int[N][2];

       int max_x = 0, max_y = 0;

       for (int i = 0 ; i < N ; i++) {
           int x = sc.nextInt();
           int y = sc.nextInt();

           points[i][0] = x;
           points[i][1] = y;

           max_x = (x > max_x) ? x : max_x;
           max_y = (y > max_y) ? y : max_y;
       }
       
       max_x++; max_y++;

       dist = new int[max_x][max_y];
       interested = new boolean[max_x][max_y];

       for (int x = 0 ; x < max_x ; x++) {
           for (int y = 0 ; y < max_y ; y++) {
               dist[x][y] = INF;
               interested[x][y] = false;
           }
       }
       
       for (int i = 0 ; i < N ; i++) {
           interested[points[i][0]][points[i][1]] = true;
       }

       PriorityQueue<VertexComp> pq = new PriorityQueue<VertexComp>();

       int reached = 0;
       int MSTWeight = 0;

       interested[points[0][0]][points[0][1]] = false;
       dist[points[0][0]][points[0][1]] = 0;
       reached++;

       pq.offer(new VertexComp(points[0][0], points[0][1], 0));

       while (!pq.isEmpty() && reached < N) {
           // while tree is not spanning
           VertexComp v = pq.poll();

           //System.out.println("" + v.x + " " + v.y);

           if (v.d > dist[v.x][v.y]) {
               continue;
           }

           if (interested[v.x][v.y]) {
               interested[v.x][v.y] = false;
               MSTWeight += dist[v.x][v.y];
               v.d = dist[v.x][v.y] = 0;
               reached++;
           }

           if (v.x > 0 && v.d + 1 < dist[v.x - 1][v.y]) {
               dist[v.x - 1][v.y] = v.d + 1;
               pq.offer(new VertexComp(v.x - 1, v.y, v.d + 1));
           }

           if (v.x < max_x - 1 && v.d + 1 < dist[v.x + 1][v.y]) {
               dist[v.x + 1][v.y] = v.d + 1;
               pq.offer(new VertexComp(v.x + 1, v.y, v.d + 1));
           }

           if (v.y > 0 && v.d + 1 < dist[v.x][v.y - 1]) {
               dist[v.x][v.y - 1] = v.d + 1;
               pq.offer(new VertexComp(v.x, v.y - 1, v.d + 1));
           }

           if (v.y < max_y - 1 && v.d + 1 < dist[v.x][v.y + 1]) {
               dist[v.x][v.y + 1] = v.d + 1;
               pq.offer(new VertexComp(v.x, v.y + 1, v.d + 1));
           }
       }

       System.out.println(MSTWeight);
    }
}
