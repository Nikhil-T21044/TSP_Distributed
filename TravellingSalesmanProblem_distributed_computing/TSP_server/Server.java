// Java implementation of Server side
// It contains two classes : Server and ClientHandler
// Save file as Server.java

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

//import javax.json.*;
import com.google.gson.*;

// Server class
public class Server {
	
	static LinkedList<GraphNode> nodeList = new LinkedList<GraphNode>();
	static int[][] graph;
	static int minSoFar = Integer.MAX_VALUE;
	static int i = 0;
	
	static void DivideNodes(int[][] graph) {
		boolean[] v = new boolean[graph.length];
		int currPos = 0;
		int count = 0;
		int cost = 0;
		int ans = Integer.MAX_VALUE;
		
		v[0] = true;
		count++;
		
		for (int i = 1; i < graph.length; i++)
		{
			if (v[i] == false && graph[currPos][i] > 0)
			{
				v[i] = true; // Mark as visited
				
				nodeList.add( new GraphNode (v, i, count + 1, cost + graph[currPos][i], ans, graph) );

				v[i] = false; // Mark ith node as unvisited
			}
		}
	}
	
	public static void main(String[] args) throws IOException
	{
		// divide problem
		
		graph =  generateMatrix(25);
		
		DivideNodes(graph);
		
		// server is listening on port 5056
		ServerSocket ss = new ServerSocket(5057);
		
		Thread[] machines = new Thread[nodeList.size()];
		// running infinite loop for getting
		// client request
//		int i = 0;
		while (true)
		{
			Socket s = null;
			
			try
			{
				if(i >= nodeList.size()) { // all sub problem distributed
					break;
				}
				
				// socket object to receive incoming client requests
				s = ss.accept();
				
				System.out.println("A new client is connected : " + s);
				
				// obtaining input and out streams
				DataInputStream dis = new DataInputStream(s.getInputStream());
				DataOutputStream dos = new DataOutputStream(s.getOutputStream());
				
				if(i >= nodeList.size()) { // all sub problem distributed
					dos.writeUTF("Exit");
					s.close();
					dis.close();
					dos.close();
					break;
				}
				
				System.out.println("Assigning new thread for this client");

				// create a new thread object
				GraphNode subProblem = nodeList.get(i++);
				subProblem.ans = minSoFar; // updating with processed min
				Thread t = new ClientHandler(s, dis, dos, subProblem );

				machines[i] = t;
				// Invoking the start() method
				t.start();
				
			}
			catch (Exception e){
				s.close();
				e.printStackTrace();
			}
		}
		
		try {
			int threads = machines.length;
			while(threads-- > 0) { // waiting for threads
				if(machines[threads] != null) { 
					machines[threads].join();
				}
			}
			ss.close();
	 	} catch (InterruptedException e) {
	 			 System.out.println("Exception occured at runnning thread");
	 			e.printStackTrace();
	 	}
		
		System.out.println(minSoFar);
	}


	static int[][] generateMatrix(int n){
    	int[][] matrix = new int [n] [n];
    	for (int i=0; i<n; i++) {
    	    for (int j = i+1; j<n; j++) {	
    	    		matrix[i][j] = matrix[j][i] = (int) (Math.random()*10 + 1); //minimum edge weight to 1
    	    }           
    	}
    return matrix;	
    }
	
	static int[][] generateMatrix(){
    	try {
			return ( new Input().readFromFile(new File("./TSP/Input39.tsp")) ); // 39 -> 8179406
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return new int [1][1];	 // if some exception occured
    }
}

// ClientHandler class
class ClientHandler extends Thread
{
	DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd");
	DateFormat fortime = new SimpleDateFormat("hh:mm:ss");
	final DataInputStream dis;
	final DataOutputStream dos;
	final Socket s;
	GraphNode node;
	

	// Constructor
	public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos)
	{
		this.s = s;
		this.dis = dis;
		this.dos = dos;
	}
	
	public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos, GraphNode node) {
		this(s, dis, dos);
		this.node = node;
	}

	@Override
	public void run()
	{
		String received;
		String toreturn;
	try {	
		Gson gson = new Gson();
		String data = gson.toJson(node);
		dos.writeUTF("Problem-Data");
		dos.writeUTF(data);
	} catch (IOException e) {
		e.printStackTrace();
	}
		
		while (true)
		{
			try {
				// Ask user what he wants
//				dos.writeUTF("What do you want?[Date | Time]..\n"+
//							"Type Exit to terminate connection.");
				
				
				
				// receive the answer from client
				boolean close_socket = false;
				received = dis.readUTF();
				
				if(received.equals("Exit"))
				{
					System.out.println("Client " + this.s + " sends exit...");
					System.out.println("Closing this connection.");
					this.s.close();
					System.out.println("Connection closed");
					break;
				}
				
				// creating Date object
				Date date = new Date();
				
				// write on output stream based on the
				// answer from the client
				System.out.println(received);
				switch (received) {
					case "Date" :
						toreturn = fordate.format(date);
						dos.writeUTF(toreturn);
						break;
						
					case "Time" :
						toreturn = fortime.format(date);
						dos.writeUTF(toreturn);
						break;
						
					case "Problem-Result" :
						received = dis.readUTF();
						int ans = Integer.parseInt(received);
						System.out.println(ans);
						if(ans < Server.minSoFar)
							Server.minSoFar = ans;
						if(Server.i < Server.nodeList.size()) {
							//execute next problem
							Gson gson = new Gson();
							node = Server.nodeList.get(Server.i++);
							String data = gson.toJson(node);
							dos.writeUTF("Problem-Data");
							dos.writeUTF(data);
						}else {
							dos.writeUTF("Exit");
							System.out.println("Closing this connection : " +  this.s);
							this.s.close();
							System.out.println("Connection closed");
							close_socket = true;
						}
						break;
					default:
						dos.writeUTF("Invalid input");
						break;
				}
				
				if(close_socket)
					break;
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try
		{
			// closing resources
			this.dis.close();
			this.dos.close();
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
