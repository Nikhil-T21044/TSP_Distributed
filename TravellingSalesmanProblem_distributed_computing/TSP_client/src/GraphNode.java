/*
 * @author Nikhil
 * */
public class GraphNode {
	boolean[] v;
	int[][] graph;
	int currPos; 
	int count;
	int cost;
	int ans;
	
	GraphNode(boolean[] v, int currPos, int count, int cost, int ans){
		this.v = v.clone();
		this.currPos = currPos;
		this.count = count;
		this.cost = cost;
		this.ans = ans;
	}
	
	GraphNode(boolean[] v, int[][] graph, int currPos, int count, int cost, int ans){
		this(v, currPos, count, cost, ans);
		this.graph = graph;
	}
	
	void processPath(boolean mstInclude){
		ans = new TSP().tsp(graph, v, currPos, graph.length, count, cost, ans, mstInclude);
	}
	
	void processPath(){
		ans = new TSP().tsp(graph, v, currPos, graph.length, count, cost, ans);
	}
	
	public String toString() {
		return v + "" + graph ;
	}
}
