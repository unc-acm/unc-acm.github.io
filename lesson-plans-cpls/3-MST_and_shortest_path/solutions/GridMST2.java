import java.util.*;
import java.io.*;

public class GridMST2 {

    static class QueueMember {
        short x, y, d;
        int id;

        public QueueMember(short _x, short _y, int _id, short _d) {
            x = _x;
            y = _y;
            id = _id;
            d = _d;
        }
    }

    static int parent[];
    static int rank[];

    static int find(int x) {
        if (parent[x] == x) {
            return x;
        }
        else {
            return find(parent[x]);
        }
    }

    static boolean union(int x, int y) {
        int xroot = find(x);
        int yroot = find(y);

        if (xroot == yroot) {
            return false;
        }

        if (rank[xroot] < rank[yroot]) {
            parent[xroot] = yroot;
        }
        else if (rank[xroot] > rank[yroot]) {
            parent[yroot] = xroot;
        }
        else {
            parent[yroot] = xroot;
            rank[xroot]++;
        }

        return true;
    }

    public static int generateMST(int[][] board,
            Queue<QueueMember> bfsQueue,
            short[][] points,
            int N, short max_x, short max_y) {

        int totalWeight = 0;
        short currentStepSize = 0;

        Queue<QueueMember> toFill = new LinkedList<QueueMember>();

        while (!bfsQueue.isEmpty()) {
            QueueMember current = bfsQueue.poll();

            short x = current.x; short y = current.y;
            int id = current.id;
            short d = current.d;

            if (d > currentStepSize) {
                while (!toFill.isEmpty()) {
                    QueueMember tf = toFill.poll();
                    short xx = tf.x; short yy = tf.y;
                    int idd = tf.id;

                    if (board[xx][yy] == -1) {
                        board[xx][yy] = idd;
                    }

                    else if (find(idd) != find(board[xx][yy])) {
                        int otherID = board[xx][yy];

                        totalWeight += Math.abs(points[idd][0] - points[otherID][0]);
                        totalWeight += Math.abs(points[idd][1] - points[otherID][1]);

                        union(idd, otherID);
                    }
                }

                currentStepSize++;
            }

            if (board[x][y] == -1) {
                toFill.offer(current);

                if (x > 0) {
                    bfsQueue.offer(new QueueMember((short)(x - 1), y, id, (short)(d + 1)));
                }

                if (x < max_x - 1) {
                    bfsQueue.offer(new QueueMember((short)(x + 1), y, id, (short)(d + 1)));
                }

                if (y > 0) {
                    bfsQueue.offer(new QueueMember(x, (short)(y - 1), id, (short)(d + 1)));
                }

                if (y < max_y - 1) {
                    bfsQueue.offer(new QueueMember(x, (short)(y + 1), id, (short)(d + 1)));
                }
            }

            else {
                int otherID = board[x][y];

                if (find(id) != find(otherID)) {

                    //System.out.println("Add edge " + id + " - " + otherID + ": " + (Math.abs(points[id][0] - points[otherID][0]) + Math.abs(points[id][1] - points[otherID][1])));

                    totalWeight += Math.abs(points[id][0] - points[otherID][0]);
                    totalWeight += Math.abs(points[id][1] - points[otherID][1]);

                    union(id, otherID);
                }
            }
        }

        return totalWeight;
    }

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int N = s.nextInt();
        short max_x = 0, max_y = 0;
        short[][] points = new short[N][2];
        rank = new int[N];
        parent = new int[N];

        //ArrayList<HashSet<Integer>> forestLookup = new ArrayList<HashSet<Integer>>();
        //HashMap<Integer, HashSet<Integer>> forestLookup = new HashMap<Integer, HashSet<Integer>>();
        Queue<QueueMember> bfsQueue = new LinkedList<QueueMember>();

        for (int i = 0 ; i < N ; i++) {
            short x = s.nextShort();
            short y = s.nextShort();

            points[i][0] = x; points[i][1] = y;

            max_x = (x > max_x) ? x : max_x;
            max_y = (y > max_y) ? y : max_y;

            parent[i] = i;
            rank[i] = 0;

            bfsQueue.offer(new QueueMember(x, y, i, (short)0));
        }

        max_x++;
        max_y++;

        int[][] board = new int[max_x][max_y];

        for (int i = 0 ; i < max_x ; i++) {
            for (int j = 0 ; j < max_y ; j++) {
                board[i][j] = -1;
            }
        }

        System.out.println(generateMST(board, bfsQueue, points, N, max_x, max_y));   
    }
}
