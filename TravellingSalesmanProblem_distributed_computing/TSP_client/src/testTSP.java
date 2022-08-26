public class testTSP {
	// Function to find the minimum weight
	// Hamiltonian Cycle
	static int tsp(int[][] graph, boolean[] v,
				int currPos, int n,
				int count, int cost, int ans)
	{

		// If last node is reached and it has a link
		// to the starting node i.e the source then
		// keep the minimum value out of the total cost
		// of traversal and "ans"
		// Finally return to check for more possible values
		if (count == n && graph[currPos][0] > 0)
		{
			ans = Math.min(ans, cost + graph[currPos][0]);
			return ans;
		}

		// BACKTRACKING STEP
		// Loop to traverse the adjacency list
		// of currPos node and increasing the count
		// by 1 and cost by graph[currPos,i] value
		for (int i = 0; i < n; i++)
		{
			if (v[i] == false && graph[currPos][i] > 0)
			{

				// Mark as visited
				v[i] = true;
				ans = tsp(graph, v, i, n, count + 1,
						cost + graph[currPos][i], ans);

				// Mark ith node as unvisited
				v[i] = false;
			}
		}
		return ans;
	}

	// Driver code
	public static void main(String[] args)
	{

		// n is the number of nodes i.e. V
		int n = 11;

		int[][] graph = {{0, 10, 15, 20},
						{10, 0, 35, 25},
						{15, 35, 0, 30},
						{20, 25, 30, 0}};
		graph = new int[][] {{0,  7,   9,   6,   10,  4,   5,   7,   3,   3 ,  1  } ,
			{7 ,  0 ,  7 ,  5,   1 ,  5 ,  6 ,  9,   2,   8,   10  },
			{9 ,  7 ,  0 ,  3,   1 ,  2 ,  2  , 10,  1,   6 ,  4   },
			{6 ,  5 ,  3 ,  0,   8 ,  2 ,  5 ,  1,   4,   10 , 6   },
			{10,  1 ,  1 ,  8,   0 ,  9 ,  2 ,  4,   2,   3   ,8   },
			{4 ,  5 ,  2 ,  2,   9 ,  0 ,  6,   6,   4,   2,   2   },
			{5 ,  6 ,  2 ,  5,   2 ,  6 ,  0,   6,   3,   9 ,  2   },
			{7 ,  9 ,  10,  1,   4 ,  6 ,  6,   0,   3,   9 ,  10  },
			{3 ,  2 ,  1 ,  4 ,  2 ,  4 ,  3,   3,   0,   3 ,  4   },
			{3 ,  8 ,  6 ,  10,  3 ,  2 ,  9,   9,   3,   0 ,  5   },
			{1 ,  10,  4 ,  6 ,  8 ,  2 ,  2,   10,  4,   5 ,  0   }};

		// Boolean array to check if a node
		// has been visited or not
		boolean[] v = new boolean[n];

		// Mark 0th node as visited
		v[0] = true;
		int ans = Integer.MAX_VALUE;

		// Find the minimum weight Hamiltonian Cycle
		ans = tsp(graph, v, 0, n, 1, 0, ans);

		// ans is the minimum weight Hamiltonian Cycle
		System.out.println(ans);
	}
}

//This code is contributed by Rajput-Ji
