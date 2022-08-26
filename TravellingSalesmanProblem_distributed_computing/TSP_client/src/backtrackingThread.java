/*
 * @author Nikhil
 * */
import java.util.LinkedList;

public class backtrackingThread extends Thread {
	LinkedList<GraphNode> nodeList = new LinkedList<GraphNode>();
	int ans = Integer.MAX_VALUE;
	boolean branchNBound = false;
	boolean mstInclude = false;
	
	public void run() {		
		int size = nodeList.size();
		while(size-- > 0) {
			GraphNode node = nodeList.get(size);
			
			// calculate distance
			if(branchNBound) 
				node.processPath(mstInclude);
			else
				node.processPath();
			
			ans = Math.min(node.ans, ans);
		}
    }
	
	public backtrackingThread(boolean branchNBound , boolean mstInclude,  GraphNode node){
		this.branchNBound = branchNBound;
		this.mstInclude = mstInclude;
		nodeList.add(node);
	}


	void addNode(GraphNode node){
		nodeList.add(node);
	}
}
