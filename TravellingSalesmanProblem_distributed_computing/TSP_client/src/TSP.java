import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntBinaryOperator;

public class TSP {
	
	static long nodeTraversed = 0;
	
	 //TSP without Branch&Bound
	 int tsp(int[][] graph, boolean[] v, int currPos, int n, int count, int cost, int ans)
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
	//NOTE : function code ref taken from Internet resources 
}
	
	 static AtomicInteger minSoFar = new AtomicInteger(Integer.MAX_VALUE);
	 IntBinaryOperator checkSetOp = (x, y) -> (x < y ? x : y );

	 //TSP with Branch&Bound
	 int tsp(int[][] graph, boolean[] v, int currPos, int n, int count, int cost, int ans, boolean mstInclude)
	{
		 // Stop processing the path if already passing threshold of minSoFar
		 if(PruningPath(cost, minSoFar.get(), v, graph, mstInclude, count)) {
			 return minSoFar.get();
		 }

	// If last node is reached and it has a link
	// to the starting node i.e the source then
	// keep the minimum value out of the total cost
	// of traversal and "ans"
	// Finally return to check for more possible values
	if (count == n && graph[currPos][0] > 0)
	{
		ans = minSoFar.accumulateAndGet(cost + graph[currPos][0], checkSetOp);
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
					cost + graph[currPos][i], ans, mstInclude);

			// Mark ith node as unvisited
			v[i] = false;
		}
	}
//	 if(count == n - (n - 6)) {
//		 nodeTraversed++;
//		 System.out.println(" Ans So Far " + ans + " | " + currPos + " | " + nodeTraversed);
//	 }
	return ans;
	}


	 boolean PruningPath(int cost, int minSoFar, boolean[] visited, int[][] tspGraph, boolean mstInclude, int noOfVisited) {
		 
		if(cost > minSoFar) { 
		// if already traversed weight is exceeding the best so far no need to go further
			return true;
		}
		
		if (!mstInclude) return false; // MST config not enabled
		
		if(minSoFar == Integer.MAX_VALUE) { 
			// no exhaustive path is processed yet
			// no need of MST calculation
			return false;
		}
		
		if(noOfVisited > visited.length - 2) return false; // no vetices for min span tree calculation
		
//		long start, end;
//		start = System.currentTimeMillis();
		
		int vertices = 0; // no of vertices for mst calculation
		int[] map = new int[visited.length];
		
		for (int index = 0 ; index < visited.length ; index++ ) {
			if(visited[index] == false) {
				map[vertices] =  index;
				vertices++;
			}
		}
		
		int[][] graph = new int[vertices][vertices];
		
		for(int i = 0 ; i < vertices ; i++) {
			int rowNo = map[i];
			for(int j = 0; j < vertices ; j++) {
				graph[i][j] = tspGraph[rowNo][map[j]];
			}
		}
		
		int mstWeight = MinSpanningTree.primMST(graph);
		
//		end = System.currentTimeMillis();   //checker for time cosumption of mst
//		if (noOfVisited < 3 && mstInclude) System.out.format("\nexeccution time :\t"+ (end - start) + " ms \t | " );
		
		if( mstWeight + cost > minSoFar) {
			return true;
		}
		
		 return false; // no case satisfied
	 }
}
