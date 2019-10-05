import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;

public abstract class SearchProblem {
	
	State initialState;
	LinkedList operators;
	
	
	public abstract boolean goalTest(State state);
	public abstract State transitionFun(State state, Object op);
	public abstract int pathCost(State currentState, Object op);
	
	public Node genericSearch(SearchProblem problem, Strategy strategy) {
		
		LinkedList<Node> q = new LinkedList <Node>();
		Node node = new Node(problem.initialState, null, 0, 0);
		q.add(node);
		while(true) {
			if(q.isEmpty()) 
				return null;
			node = q.removeFirst();
			if(problem.goalTest(node.state))
				return node;
			else {
				for(int i=0; i<operators.size(); i++) {
					int newCost = node.cost + problem.pathCost(node.state, operators.get(i));
				}
				switch(strategy) {
					case DF:break;
					case BF:
						q.addLast(node);break;
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

abstract class State{
	
}

enum Operator{
	
	public final String [] operators;
	 
    private Operator(String [] operators) {
        this.operators = operators;
    }
}

enum Strategy{
	DF,BF,ID,UC,GR1,GR2,AS1,AS2
}

class Node{
	Node parent;
	State state;
	int cost;
	int depth;
	//enum operator;
	
	public Node(State state, Node parent, int cost, int depth) {
		this.cost=cost;
		this.state=state;
		this.parent=parent;
		this.depth=depth;
	}
}