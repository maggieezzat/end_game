import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;


public abstract class SearchProblem {
	
	State initialState;
	LinkedList <String> operators;
	
	public static int exploredNodes;
	
	public SearchProblem(State initialState, LinkedList<String>operators) {
		this.initialState = initialState;
		this.operators = operators;
	}
	
	public abstract boolean goalTest(Node node);
	public abstract State transitionFun(Node currentNode, String op);
	public abstract int pathCost(State prevState, State newState, String op);
	
	public static Node genericSearch(SearchProblem problem, Strategy strategy) {
		
		int visited=0;
		LinkedList<Node> q = new LinkedList <Node>();
		Node node = new Node(problem.initialState, null, 0, 0, null);
		q.add(node);
		
		
		while(true) {
			if(q.isEmpty()) 
				return null;
			node = q.removeFirst();
			visited++;
			exploredNodes++;
			if(problem.goalTest(node)) {
				System.out.println("# Visited Nodes: " + visited);
				return node;
			}
				
			else {
				LinkedList <Node> children = new LinkedList<Node>();
				for(String op : problem.operators) {
					State newState = problem.transitionFun(node, op);
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
						case UC: 
							q.addFirst(child);
							Collections.sort(q, Comparator.comparingInt(obj -> obj.cost));
							break;
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
	
	public String toString() {
		return "Cost: " + cost;
	}
}