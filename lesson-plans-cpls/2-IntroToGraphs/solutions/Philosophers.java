import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Scanner;

public class Philosophers {

	static int solve(int N, boolean[][] E) {
	    boolean[] visited = new boolean[N];
	    int[] degrees = new int[N];
	    boolean branches = false;
	    boolean pushed = false;
	    Queue <Integer> q = new ArrayDeque<Integer>();
	    
	    for (int i = 0 ; i < N ; i++) {
	        for (int j = 0 ; j < N ; j++) {
	            if (E[j][i]) {
	                degrees[i]++;
	            }
	        }

	        if (degrees[i] == 0) {
	            if (!pushed) {
	                pushed = true;
	            }
	            else {
	                branches = true;
	            }
	            q.add(i);
	        }
	    }

	    if (!pushed) { // one big cycle
	        return 0;
	    }

	    while (!q.isEmpty()) {
	        pushed = false;
	        int a = q.poll();
	        visited[a] = true;

	        for (int i = 0 ; i < N ; i++) {
	            if (E[a][i]) {
	                if (--degrees[i] == 0) {
	                    if (!pushed) {
	                        pushed = true;
	                    }
	                    else {
	                        branches = true;
	                    }
	                    q.add(i);
	                }
	            }
	        }
	    }

	    for (int i = 0 ; i < N ; i++) {
	        if (!visited[i]) {
	            return 0; // if there sort terminated prematurely, there is a cycle
	        }
	    }

	    if (branches) {
	        return 2;
	    }

	    else {
	        return 1;
	    }
	}
	
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		int N = s.nextInt();
		int M = s.nextInt();



	    while ((N != 0) && (M != 0)) 
	    {
	        boolean[][] E = new boolean[N][N];

	        for (int i = 0 ; i < M ; i++) {
	            int from = s.nextInt();
	            int to = s.nextInt();
	            E[from - 1][to - 1] = true;
	        }

	        System.out.println(solve(N, E));

	        N = s.nextInt();
	        M = s.nextInt();
	    }
	  
	}


}


