import java.util.*;
import java.io.*;

public class Freckles {

    static class EdgeComparison implements Comparable<EdgeComparison>{
        public int from, to;
        public double weight;

        public EdgeComparison(int _f, int _t, double _w) {
            from = _f;
            to = _t;
            weight = _w;
        }

        @Override
        public int compareTo(EdgeComparison other) {
            return Double.compare(weight, other.weight);
        }

        public String toString() {
            return ("" + from + " - " + to + ": " + weight);
        }
    }

    public static double MST(int V, double[][] edges) {
        double MSTWeight = 0;
        PriorityQueue<EdgeComparison> costs = new PriorityQueue<EdgeComparison>();
        boolean[] visited = new boolean[V];
        int visited_count = 0;

        costs.offer(new EdgeComparison(0, 0, 0));

        while (!costs.isEmpty()) {
            // System.out.println("==============");
            EdgeComparison current = costs.poll();

            // System.out.println(current);

            if (visited[current.to]) {
                continue;
            }

            visited[current.to] = true;
            MSTWeight += current.weight;

            for (int i = 0 ; i < V ; i++) {
                if (i != current.to && !visited[i]) {
                    costs.offer(new EdgeComparison(current.to, i, edges[current.to][i]));
                }
            }
        }

        return MSTWeight;
    }

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        int N = s.nextInt();

        for (int a = 0 ; a < N ; a++) {
            int V = s.nextInt();
            double[][] points = new double[V][2];
            double[][] edges = new double[V][V];

            for (int i = 0 ; i < V ; i++) {
                points[i][0] = s.nextDouble();
                points[i][1] = s.nextDouble();
            }

            for (int i = 0 ; i < V ; i++) {
                edges[i][i] = -1;
                for (int j = i + 1 ; j < V ; j++) {
                    double dist = Math.sqrt(
                            (points[i][0] - points[j][0]) * (points[i][0] - points[j][0]) +
                            (points[i][1] - points[j][1]) * (points[i][1] - points[j][1])
                            );
                    //System.out.printf("%d - %d: %f\n", i, j, dist);
                    edges[i][j] = dist;
                    edges[j][i] = dist;
                }
            }

            System.out.printf("%.2f\n\n", MST(V, edges));
        }
    }
}
