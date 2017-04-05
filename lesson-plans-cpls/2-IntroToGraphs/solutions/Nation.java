import java.util.ArrayList;
import java.util.Scanner;

public class Nation {
	
	static int clique_size(ArrayList<Integer>[] graph, ArrayList<Integer> govs, ArrayList <Boolean> visited, int starting) {
	    visited.set(starting, true);
	    int size = 1;
	    
	    for (int i = 0 ; i < graph[starting].size() ; i++) {
	        if (!visited.get(graph[starting].get(i))) {
	            size += clique_size(graph, govs, visited, graph[starting].get(i));
	        }
	    }

	    return size;
	}

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
	    int n = s.nextInt();
	    int m = s.nextInt();
	    int k = s.nextInt();

	    int[][] graph = new int[n][n];
	    int[] govs = new int[k];
	    boolean[] visited = new boolean[n];
	    
	    for (int i = 0 ; i < k ; i++) {
	        govs[i] = s.nextInt() - 1;
	    }
	    
	    

	    for (int i = 0 ; i < m ; i++) {
	        int u = s.nextInt();
	        int v = s.nextInt();
	        graph[u - 1].add(v - 1);
	        graph[v - 1].add(u - 1);
	    }

	    int connected_points = 0;
	    int max_state_size = 0;
	    int answer = 0;

	    for (int i = 0 ; i < k ; i++) {
	        int count = clique_size(graph, govs, visited, govs.get(i));
	        connected_points += count;
	        max_state_size = (count > max_state_size) ? count : max_state_size;
	        answer += count * (count - 1) / 2;
	    }

	    // the remaining points should go to the largest clique
	    int remaining = n - connected_points;
	    answer -= max_state_size * (max_state_size - 1) / 2;
	    max_state_size += remaining;
	    answer += max_state_size * (max_state_size - 1) / 2;

	    System.out.println(answer - m);
	}

}


