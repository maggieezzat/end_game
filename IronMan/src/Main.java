import java.util.ArrayList;
import java.util.LinkedList;

public class Main {
	
	public static String backtracking(Node solNode, int exploredNodes) {
		ArrayList<String> operators = new ArrayList<String>(solNode.depth);
		Node node = solNode;

		while(node.operator != null) {
			operators.add(node.operator);
			node = node.parent;
		}
		String plan = "";
		for(int i=operators.size()-1; i>=0; i--) {
			plan += operators.get(i) + ",";
		}
		String cost = solNode.cost + "";
		return plan.substring(0, plan.length() - 1) + "; " + cost + "; " + exploredNodes;
	}
	
	public static void visualizePath(Node solNode)
	{
		ArrayList<Node> nodes = new ArrayList<Node>();
		
		Node node = solNode;
		while(node.operator != null) {
			nodes.add(node);
			node = node.parent;
		}
		
		for(int i=nodes.size()-1; i>=0; i--) {
			EG_State state = (EG_State)nodes.get(i).state;
			Point iman = state.iPos;
			LinkedList<Point> stones = state.stones;
			LinkedList<Point> warriors = state.warriors;
			System.out.println(iman.toString()+"; "+stones.toString()+"; "+warriors.toString());
		}
	}
	
	
	public static String solve(String grid, String strategy, boolean visualize) {
		
		String[] gridArray = grid.split(";");
		
		//grid size
		int m = Integer.parseInt(gridArray[0].split(",")[0]);
		int n = Integer.parseInt(gridArray[0].split(",")[1]);
		
		//iron man position
		int ix = Integer.parseInt(gridArray[1].split(",")[0]);
		int iy = Integer.parseInt(gridArray[1].split(",")[1]);
		Point iPos = new Point(ix, iy);
		
		//thanos position
		int tx = Integer.parseInt(gridArray[2].split(",")[0]);
		int ty = Integer.parseInt(gridArray[2].split(",")[1]);
		Point tPos = new Point(tx, ty);
		
		//linkedlist of stones
		LinkedList<Point> stones = new LinkedList<Point>();
		int sx, sy;
		String[]stonesArray = gridArray[3].split(",");
		for(int i=0; i < stonesArray.length-1; i+=2) {
			sx = Integer.parseInt(stonesArray[i]);
			sy = Integer.parseInt(stonesArray[i+1]);
			stones.add(new Point(sx, sy));
		}
		
		//linkedlist of warriors
		LinkedList<Point> warriors = new LinkedList<Point>();
		int wx, wy;
		String[]warriorsArray = gridArray[4].split(",");
		for(int i=0; i < warriorsArray.length-1; i+=2) {
			wx = Integer.parseInt(warriorsArray[i]);
			wy = Integer.parseInt(warriorsArray[i+1]);
			warriors.add(new Point(wx, wy));
		}
				
		//make a problem
		EndGame problem = new EndGame(new Point(m,n), iPos, tPos, stones, warriors);
		
		//get the solution using the generic search procedure
		Node solNode = SearchProblem.genericSearch(problem, strategy);
		
		if(visualize) {
			visualizePath(solNode);
		}
		if(solNode == null)
			return "There is no solution";
		else {
			//from the solution node, get the sequence of all nodes from root to solution
			return backtracking(solNode, SearchProblem.exploredNodes);
		}
		
	}
	
	
	public static void main(String []args) {
		
		long startTime;
		long endTime;
		long totalTime;
		
		startTime = System.nanoTime();
		System.out.println("******************* BFS ********************");
		String sol_bf = solve("5,5;1,2;3,1;0,2,1,1,2,1,2,2,4,0,4,1;0,3,3,0,3,2,3,4,4,3", "BF", false);
		System.out.println(sol_bf);
		endTime   = System.nanoTime();
		totalTime = endTime - startTime;
		System.out.printf("BFS Duration: %.2f seconds", totalTime/1000000000.0);
		System.out.println();
		System.out.println("*********************************************");
		System.out.println();
		
		
		startTime = System.nanoTime();
		System.out.println("******************* DFS ********************");
		String sol_df = solve("5,5;1,2;3,1;0,2,1,1,2,1,2,2,4,0,4,1;0,3,3,0,3,2,3,4,4,3", "DF", false);
		System.out.println(sol_df);
		endTime   = System.nanoTime();
		totalTime = endTime - startTime;
		System.out.printf("DFS Duration: %.4f seconds", totalTime/1000000000.0);
		System.out.println();
		System.out.println("*********************************************");
		System.out.println();
		
		
		startTime = System.nanoTime();
		System.out.println("******************* UCS ********************");
		String sol_uc = solve("5,5;1,2;3,1;0,2,1,1,2,1,2,2,4,0,4,1;0,3,3,0,3,2,3,4,4,3", "UC", false);
		System.out.println(sol_uc);
		endTime   = System.nanoTime();
		totalTime = endTime - startTime;
		System.out.printf("UCS Duration: %.4f seconds", totalTime/1000000000.0);
		System.out.println();
		System.out.println("*********************************************");
		System.out.println();
		
		
//		startTime = System.nanoTime();
//		System.out.println("******************* IDS ********************");
//		String sol_id = solve("5,5;1,2;3,1;0,2,1,1,2,1,2,2,4,0,4,1;0,3,3,0,3,2,3,4,4,3", "ID", false);
//		System.out.println(sol_id);
//		endTime   = System.nanoTime();
//		totalTime = endTime - startTime;
//		System.out.printf("IDS Duration: %.4f seconds", totalTime/1000000000.0);
//		System.out.println();
//		System.out.println("*********************************************");
//		System.out.println();
		
		
//		startTime = System.nanoTime();
//		System.out.println("******************* AS1 ********************");
//		String sol_as1 = solve("5,5;1,2;3,1;0,2,1,1,2,1,2,2,4,0,4,1;0,3,3,0,3,2,3,4,4,3", "AS1", false);
//		System.out.println(sol_as1);
//		endTime   = System.nanoTime();
//		totalTime = endTime - startTime;
//		System.out.printf("AS1 Duration: %.4f seconds", totalTime/1000000000.0);
//		System.out.println();
//		System.out.println("*********************************************");
//		System.out.println();
		
		
//		startTime = System.nanoTime();
//		System.out.println("******************* AS1 ********************");
//		String sol_as2 = solve("5,5;1,2;3,1;0,2,1,1,2,1,2,2,4,0,4,1;0,3,3,0,3,2,3,4,4,3", "AS2", false);
//		System.out.println(sol_as2);
//		endTime   = System.nanoTime();
//		totalTime = endTime - startTime;
//		System.out.printf("AS2 Duration: %.4f seconds", totalTime/1000000000.0);
//		System.out.println();
//		System.out.println("*********************************************");
//		System.out.println();
		
		
//		startTime = System.nanoTime();
//		System.out.println("******************* GR1 ********************");
//		String sol_gr1 = solve("5,5;1,2;3,1;0,2,1,1,2,1,2,2,4,0,4,1;0,3,3,0,3,2,3,4,4,3", "GR1", false);
//		System.out.println(sol_gr1);
//		endTime   = System.nanoTime();
//		totalTime = endTime - startTime;
//		System.out.printf("GR1 Duration: %.4f seconds", totalTime/1000000000.0);
//		System.out.println();
//		System.out.println("*********************************************");
//		System.out.println();
		
		
//		startTime = System.nanoTime();
//		System.out.println("******************* GR1 ********************");
//		String sol_gr2 = solve("5,5;1,2;3,1;0,2,1,1,2,1,2,2,4,0,4,1;0,3,3,0,3,2,3,4,4,3", "GR2", false);
//		System.out.println(sol_gr2);
//		endTime   = System.nanoTime();
//		totalTime = endTime - startTime;
//		System.out.printf("GR2 Duration: %.4f seconds", totalTime/1000000000.0);
//		System.out.println();
//		System.out.println("*********************************************");
//		System.out.println();
		
	
	
	}
}
