import java.util.ArrayList;
import java.util.LinkedList;

public class Main {
	
	public static String backtracking(Node solNode) {
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
		return plan + ";" + cost + ";";
	}
	
	
	public static String solve(String grid, Strategy strategy, boolean visualize) {
		String[] gridArray = grid.split(";");
		//m,n
		//ix,iy
		//tx,ty
		//six,siy
		//wix,wiy
		int m = Integer.parseInt(gridArray[0].split(",")[0]);
		int n = Integer.parseInt(gridArray[0].split(",")[1]);
		
		int ix = Integer.parseInt(gridArray[1].split(",")[0]);
		int iy = Integer.parseInt(gridArray[1].split(",")[1]);
		Point iPos = new Point(ix, iy);
		
		int tx = Integer.parseInt(gridArray[2].split(",")[0]);
		int ty = Integer.parseInt(gridArray[2].split(",")[1]);
		Point tPos = new Point(tx, ty);
		
		LinkedList<Point> stones = new LinkedList<Point>();
		int sx, sy;
		String[]stonesArray = gridArray[3].split(",");
		for(int i=0; i < stonesArray.length-1; i+=2) {
			sx = Integer.parseInt(stonesArray[i]);
			sy = Integer.parseInt(stonesArray[i+1]);
			stones.add(new Point(sx, sy));
		}
		
		LinkedList<Point> warriors = new LinkedList<Point>();
		int wx, wy;
		String[]warriorsArray = gridArray[4].split(",");
		for(int i=0; i < warriorsArray.length-1; i+=2) {
			wx = Integer.parseInt(warriorsArray[i]);
			wy = Integer.parseInt(warriorsArray[i+1]);
			warriors.add(new Point(wx, wy));
		}
				
		
		EndGame problem = new EndGame(new Point(m,n), iPos, tPos, stones, warriors);
		Node solNode = SearchProblem.genericSearch(problem, strategy);
		if(solNode == null)
			return "There is no solution";
		else {
			return backtracking(solNode);
		}
		
	}
	
	
	public static void main(String []args) {
		
		long startTime;
		long endTime;
		long totalTime;
		
		startTime = System.nanoTime();
		String sol_bf = solve("5,5;1,2;3,1;0,2,1,1,2,1,2,2,4,0,4,1;0,3,3,0,3,2,3,4,4,3",Strategy.BF, false);
		System.out.println(sol_bf);
		endTime   = System.nanoTime();
		totalTime = endTime - startTime;
		System.out.println("BFS DURATION TIME");
		System.out.println(totalTime/1000000000.0 + " seconds.");
		System.out.println("*********************************************");
		
		startTime = System.nanoTime();
		String sol_df = solve("5,5;1,2;3,1;0,2,1,1,2,1,2,2,4,0,4,1;0,3,3,0,3,2,3,4,4,3",Strategy.DF, false);
		System.out.println(sol_df);
		endTime   = System.nanoTime();
		totalTime = endTime - startTime;
		System.out.println("DFS DURATION TIME");
		System.out.println(totalTime/1000000000.0 + " seconds.");
		System.out.println("*********************************************");
		
		startTime = System.nanoTime();
		String sol_uc = solve("5,5;1,2;3,1;0,2,1,1,2,1,2,2,4,0,4,1;0,3,3,0,3,2,3,4,4,3",Strategy.UC, false);
		System.out.println(sol_uc);
		endTime   = System.nanoTime();
		totalTime = endTime - startTime;
		System.out.println("UCS DURATION TIME");
		System.out.println(totalTime/1000000000.0 + " seconds.");
		System.out.println("*********************************************");
		
		/*Point stone1 = new Point(4,3);
		Point stone2 = new Point(4,3);
		
		Point stone3 = new Point(1,6);
		Point stone4 = new Point(1,6);
		
		Point war1 = new Point(5,3);
		Point war2 = new Point(5,3);
		
		Point war3 = new Point(8,9);
		Point war4 = new Point(8,9);
		

		
		LinkedList<Point> stones1 = new LinkedList<Point>();
		LinkedList<Point> stones2 = new LinkedList<Point>();
		LinkedList<Point> wars1 = new LinkedList<Point>();
		LinkedList<Point> wars2 = new LinkedList<Point>();
		
		stones1.add(stone1);
		stones1.add(stone3);
		
		stones2.add(stone4);
		stones2.add(stone2);
		
		wars1.add(war1);
		wars1.add(war3);
		
		wars2.add(war4);
		wars2.add(war2);
		
		Point im1 = new Point(1,2);
		Point im2 = new Point(1,2);
		
		
		EG_State st1 = new EG_State(im1, stones1, wars1);
		EG_State st2 = new EG_State(im2, stones2, wars2);
		
		//System.out.println(st1.equals(st2));
		
		
		
		
		
		//l.add(p);
		//System.out.println(l.contains(c));*/
		
//		Node n1 = new Node(null, null, 1, 1, "Up");
//		Node n2 = new Node(null, null, 2, 1, "Up");
//		Node n3 = new Node(null, null, 3, 1, "Up");
//		Node n4 = new Node(null, null, 4, 1, "Up");
//		Node n5 = new Node(null, null, 4, 1, "Up");
//		
//		
//		
//		LinkedList<Node> nodes_list = new LinkedList<Node>();
//		nodes_list.add(n4);
//		nodes_list.add(n2);
//		nodes_list.add(n5);
//		nodes_list.add(n3);
//		nodes_list.add(n1);
		//System.out.println(nodes_list);
		//Collections.sort(nodes_list, Comparator.comparingInt(node -> node.cost));
		
		//System.out.println(nodes_list);
	
	
	}
}
