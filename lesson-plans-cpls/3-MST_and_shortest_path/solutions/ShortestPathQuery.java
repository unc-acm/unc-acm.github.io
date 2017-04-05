import java.util.*;
import java.io.*;

public class ShortestPathQuery {

    public static long INF = 10000000000000000L;

    static class Edge {
        int to, color;
        long weight;

        public Edge(int _t, long _w, int _c) {
            to = _t;
            weight = _w;
            color = _c;
        }
    }

    // records effectively the path to v from s
    // dist == recorded distance from s
    // lastColor == edge leading into v
    static class VertexComp implements Comparable<VertexComp> {
        int v, lastColor;
        long dist;

        public VertexComp(int _v, long _d, int _c) {
            v = _v;
            dist = _d;
            lastColor = _c;
        }

        @Override
        public int compareTo(VertexComp o) {
            return Long.compare(dist, o.dist);
        }
    }

    // Functions
    public static void main(String[] args) throws IOException {
        // Scanner sc = new Scanner(System.in);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String[] inputs = br.readLine().split(" ");

        int n = Integer.parseInt(inputs[0]); //sc.nextInt();
        int m = Integer.parseInt(inputs[1]); //sc.nextInt();
        //int C = Integer.parseInt(inputs[2]) //sc.nextInt();

        List<List<Edge>> graph = new ArrayList<List<Edge>>();

        for (int i = 0 ; i < n ; i++) {
            graph.add(new ArrayList<Edge>());
        }

        for (int i = 0 ; i < m ; i++) {
            inputs = br.readLine().split(" ");
            int u = Integer.parseInt(inputs[0]) - 1; //sc.nextInt() - 1;
            int v = Integer.parseInt(inputs[1]) - 1; //sc.nextInt() - 1;
            long w = Integer.parseInt(inputs[2]); //sc.nextInt();
            int c = Integer.parseInt(inputs[3]); //sc.nextInt();

            graph.get(u).add(new Edge(v, w, c));
        }

        inputs = br.readLine().split(" ");
        int s = Integer.parseInt(inputs[0]) - 1; //sc.nextInt() - 1;

        boolean[] visited = new boolean[n];

        // key realization: we need to keep two distances, distance of shortest
        // path and another distance at EVERY NODE with a color different from
        // that used in the shortest path. Both of which will be in the priority
        // queue
        //
        // Explanation: this way, if the shortest path hits a dead end, nothing
        // further will be pushed onto the priority queue, but the other color
        // will continue the search. And since we do this for every vertex, if
        // by the end it is unreachable, then no troyic path exists. Otherwise
        // we are guaranteed the shortest troyic path.
        long[][] distTo = new long[n][2];
        int[][] incomingColor = new int[n][2];

        for (int i = 0 ; i < n ; i++) {
            if (i == s) {
                distTo[i][0] = distTo[i][1] = 0;
            }
            else {
                distTo[i][0] = distTo[i][1] = INF;
            }

            incomingColor[i][0] = incomingColor[i][1] = -1;
        }

        PriorityQueue<VertexComp> frontier = new PriorityQueue<VertexComp>();
        
        frontier.offer(new VertexComp(s, 0, -1));
        
        while (!frontier.isEmpty()) {

            VertexComp current = frontier.poll();
            int v = current.v;
            long d = current.dist;
            int c = current.lastColor;

            for (int i = 0 ; i < graph.get(v).size() ; i++) {
                Edge e = graph.get(v).get(i);

                if (e.color == c) {
                    continue;
                }

                if (d + e.weight < distTo[e.to][0]) {
                    distTo[e.to][0] = d + e.weight;
                    incomingColor[e.to][0] = e.color;
                    frontier.offer(new VertexComp(e.to, distTo[e.to][0], incomingColor[e.to][0]));
                }

                // recall that the second path has to have a different incoming
                // color than the first path
                else if (incomingColor[e.to][0] != e.color && d + e.weight < distTo[e.to][1]) {
                    distTo[e.to][1] = d + e.weight;
                    incomingColor[e.to][1] = e.color;
                    frontier.offer(new VertexComp(e.to, distTo[e.to][1], incomingColor[e.to][1]));
                }
            }

        }

        int q = Integer.parseInt(inputs[1]); //sc.nextInt();

        for (int i = 0 ; i < q ; i++) {
            int t = Integer.parseInt(br.readLine()) - 1;
            if (distTo[t][0] == INF) {
                System.out.println(-1);
            }
            else {
                System.out.println(distTo[t][0]);
            }
        }
    }
}
