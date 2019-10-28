import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;


public abstract class SearchProblem {
	
	//A search problem is a 5-tuple:
	
	//1. initial state
	State initialState;
	//2. a set of operators
	LinkedList <String> operators;
	
	public static int exploredNodes;
	
	public SearchProblem(State initialState, LinkedList<String>operators) {
		this.initialState = initialState;
		this.operators = operators;
	}
	
	//3. a goal-test function
	public abstract boolean goalTest(Node node);
	
	//4. a state space / transition function
	public abstract State transitionFun(Node currentNode, String op);
	
	//5. a goal-test function
	public abstract int pathCost(State prevState, State newState, String op);
	
	
	//The generic search procedure
	public static Node genericSearch(SearchProblem problem, String strategy) {
		
		int visited=0;
		int depthLimit = 0;
		
		//a queue of nodes
		LinkedList<Node> q = new LinkedList <Node>();
		
		//add the initial state's node to the queue
		Node node = new Node(problem.initialState, null, 0, 0, null);
		q.add(node);
		
		
		while(true) {
			if(strategy == "ID" && q.isEmpty()) { //IDS: no solution found for this depth
				depthLimit++; //increase the depth limit
				//restart
				q = new LinkedList <Node>();
				q.add(new Node(problem.initialState, null, 0, 0, null));
			}
			else if(q.isEmpty()) //other strategies: There is no solution
				return null;
			
			//expand the first node in the queue
			node = q.removeFirst();
			visited++;
			exploredNodes++;
			
			//if this node passes the goal test, it is the solution node, return it
			if(problem.goalTest(node)) {
				System.out.println("# Visited Nodes: " + visited);
				return node;
			}
			
			//if not, enqueue its children according to the specified search strategy
			//however, in case of IDS we only generate its children if the node hasn't yet reached the depth limit
			else if(strategy != "ID" || node.depth != depthLimit) {
				//the children of this node are the result of all the possible operators
				LinkedList <Node> children = new LinkedList<Node>();
				
				for(String op : problem.operators) {
					State newState = problem.transitionFun(node, op);
					if(newState != null) { //operator applicable to current state
						//the transition function returns null when the operator is not applicable
						int newCost = node.cost + problem.pathCost(node.state, newState, op);
						Node newNode = new Node(newState,node,newCost,node.depth + 1,op);
						children.add(newNode);

					}
				}
				//enqueue the children in the queue according to the strategy
				for(Node child : children) {
					switch(strategy) {
						case "DF":
						case "ID":
							q.addFirst(child); break;
						case "BF":
							q.addLast(child); break;
						case "UC":  //Uniform cost is an insertion sort based only on the path cost g(n)
							q.addFirst(child);
							Collections.sort(q, Comparator.comparingInt(obj -> obj.cost));
							break;
						case "GR1": //Greedy is an insertion sort based only on the heuristic function h(n)
							q.addFirst(child);
							Collections.sort(q, Comparator.comparingInt(obj -> problem.heuristicValue1(obj)));
							break;
						case "GR2":
							q.addFirst(child);
							Collections.sort(q, Comparator.comparingInt(obj -> problem.heuristicValue2(obj)));
							break;
						case "AS1": //A* is an insertion sort based on the evaluation function h(n) + g(n)
							q.addFirst(child);
							Collections.sort(q, Comparator.comparingInt(obj ->
								(problem.heuristicValue1(obj) + obj.cost)));
							break;
						case "AS2":
							q.addFirst(child);
							Collections.sort(q, Comparator.comparingInt(obj ->
								(problem.heuristicValue2(obj) + obj.cost)));
							break;
					}
				}
			}
		}
		
	}
	
	public abstract int heuristicValue1(Node node);
	public abstract int heuristicValue2(Node node);
	
}

abstract class State{
}

//a node is a 5-tuple:
class Node{
	
	//1.parent node
	Node parent;
	
	//2. state
	State state;
	
	//3. cost from root
	int cost;
	
	//4. depth of the node
	int depth;
	
	//5. operator resulting in this node
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