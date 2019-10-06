import java.util.LinkedList;

public abstract class SearchProblem {
	
	State initialState;
	LinkedList <String> operators;
	
	public SearchProblem(State initialState, LinkedList<String>operators) {
		this.initialState = initialState;
		this.operators = operators;
	}
	public abstract boolean goalTest(State state);
	public abstract State transitionFun(State currentState, String op);
	public abstract int pathCost(State currentState, State newState, String op);
	
	public static Node genericSearch(SearchProblem problem, Strategy strategy) {
		
		LinkedList<Node> q = new LinkedList <Node>();
		Node node = new Node(problem.initialState, null, 0, 0, null);
		q.add(node);
		while(true) {
			if(q.isEmpty()) 
				return null;
			node = q.removeFirst();
			if(problem.goalTest(node.state))
				return node;
			else {
				LinkedList <Node> children = new LinkedList<Node>();
				for(String op : problem.operators) {
					State newState = problem.transitionFun(node.state, op);
					if(newState != null) { //operator applicable to current state
						int newCost = node.cost + problem.pathCost(node.state, newState, op);
						Node newNode = new Node(newState,node,newCost,node.depth + 1,op);
						children.add(newNode);
					}
				}
				for(Node child : children) {
					switch(strategy) {
						case DF:
							q.addFirst(child); break;
						case BF:
							q.addLast(child); break;
						case ID:break;
						case UC:break;
						case GR1:break;
						case GR2:break;
						case AS1:break;
						case AS2:break;
					}
				}
			}
		}
		
	}
	
	
}

abstract class State{
	
}

// enum Operator{
//	
//	public final String [] operators;
//	 
//    private Operator(String [] operators) {
//        this.operators = operators;
//    }
//}

enum Strategy{
	DF,BF,ID,UC,GR1,GR2,AS1,AS2
}

class Node{
	Node parent;
	State state;
	int cost;
	int depth;
	String operator;
	
	public Node(State state, Node parent, int cost, int depth, String operator) {
		this.cost=cost;
		this.state=state;
		this.parent=parent;
		this.depth=depth;
		this.operator = operator;
	}
}