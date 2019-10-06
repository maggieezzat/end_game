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
			plan.concat(operators.get(i) + ",");
		}
		String cost = solNode.cost + "";
		return plan + ";" + cost + ";";
	}
	public static String solve(String grid, Strategy strategy, boolean visualize) {
		String[] gridArray = grid.split(";");
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
		for(int i=0; i < stonesArray.length-1; i+=2) {
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
		System.out.println(solve("5,5;1,2;3,1;0,2,1,1,2,1,2,2,4,0,4,1;0,3,3,0,3,2,3,4,4,3",Strategy.BF, false));
	}
}
