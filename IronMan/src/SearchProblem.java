import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Set;


public abstract class SearchProblem {
	
	//A search problem is a 5-tuple:
	
	//1. initial state
	State initialState;
	//2. a set of operators
	Hashtable <Integer, String> operators;
	
	
	public static int exploredNodes;
	
	public SearchProblem(State initialState, Hashtable <Integer, String> operators) {
		this.initialState = initialState;
		this.operators = operators;
	}
	
	//3. a goal-test function
	public abstract boolean goalTest(Node node);
	
	//4. a state space / transition function
	public abstract State transitionFun(Node currentNode, int op);
	
	//5. a path-cost function
	public abstract int pathCost(State prevState, State newState, int op);
	
	
	//The generic search procedure
	public static Node genericSearch(SearchProblem problem, int strategy) {
		
		int visited=0;
		int depthLimit = 0;
		
		//a queue of nodes
		ArrayList<Node> q = new ArrayList <Node>();
		
		//add the initial state's node to the queue
		Node node = new Node(problem.initialState, null, 0, 0, null);
		q.add(node);
		
		
		while(true) {
			if(strategy == 1 && q.isEmpty()) { //IDS: no solution found for this depth
				depthLimit++; //increase the depth limit
				//restart
				q = new ArrayList <Node>();
				q.add(new Node(problem.initialState, null, 0, 0, null));
				problem.resetExploredStates();
			}
			else if(q.isEmpty()) //other strategies: There is no solution
				return null;
			
			//expand the first node in the queue
			node = q.remove(0);
			visited++;
			exploredNodes++;
			
			//if this node passes the goal test, it is the solution node, return it
			if(problem.goalTest(node)) {
				System.out.println("# Visited Nodes: " + visited);
				return node;
			}
			
			//if not, enqueue its children according to the specified search strategy
			//however, in case of IDS we only generate its children if the node hasn't yet reached the depth limit
			else if(strategy !=1 || node.depth != depthLimit) {
				//the children of this node are the result of all the possible operators
				ArrayList <Node> children = new ArrayList<Node>();
				
				Set<Integer> ops = problem.operators.keySet();
		        for(Integer op: ops){  
					State newState = problem.transitionFun(node, op.intValue());
					if(newState != null) { //operator applicable to current state
						//the transition function returns null when the operator is not applicable
						int newCost = node.cost + problem.pathCost(node.state, newState, op.intValue());
						Node newNode = new Node(newState,node,newCost,node.depth + 1,op);
						children.add(newNode);

					}
				}
				//enqueue the children in the queue according to the strategy
				for(Node child : children) {
					switch(strategy) {
						case 0:
						case 1:
							q.add(0, child);
							//q.addFirst(child);
							break;
						case 2:
							q.add(child);
							//q.addLast(child); 
							break;
						case 3:  //Uniform cost is an insertion sort based only on the path cost g(n)
							q.add(child);
							//q.addFirst(child);
							//Collections.sort(q, Comparator.comparingInt(obj -> obj.cost));
							//Collections.sort(q, Comparator.comparingInt(obj -> obj.getCost()));
							Collections.sort(q, new Comparator<Node>(){
							     public int compare(Node n1, Node n2){
							         if(n1.cost == n2.cost)
							             return 0;
							         return n1.cost < n2.cost ? -1 : 1;
							     }
							});
							
							break;
						case 4: //Greedy is an insertion sort based only on the heuristic function h(n)
							q.add(child);
							//q.addFirst(child);
							//Collections.sort(q, Comparator.comparingInt(obj -> problem.heuristicValue1(obj)));
							Collections.sort(q, new Comparator<Node>(){
							     public int compare(Node n1, Node n2){
							         if(problem.heuristicValue1(n1) == problem.heuristicValue1(n2))
							             return 0;
							         return problem.heuristicValue1(n1) < problem.heuristicValue1(n2) ? -1 : 1;
							     }
							});
							break;
						case 5:
							q.add(child);
							//q.addFirst(child);
							//Collections.sort(q, Comparator.comparingInt(obj -> problem.heuristicValue2(obj)));
							Collections.sort(q, new Comparator<Node>(){
							     public int compare(Node n1, Node n2){
							         if(problem.heuristicValue2(n1) == problem.heuristicValue2(n2))
							             return 0;
							         return problem.heuristicValue2(n1) < problem.heuristicValue2(n2) ? -1 : 1;
							     }
							});
							break;
						case 6: //A* is an insertion sort based on the evaluation function h(n) + g(n)
							q.add(child);
							//q.addFirst(child);
							//Collections.sort(q, Comparator.comparingInt(obj ->
							//	(problem.heuristicValue1(obj) + obj.getCost() )));
							Collections.sort(q, new Comparator<Node>(){
							     public int compare(Node n1, Node n2){
							         if(problem.heuristicValue1(n1) + n1.cost == problem.heuristicValue1(n2) + n2.cost)
							             return 0;
							         return problem.heuristicValue1(n1) + n1.cost < problem.heuristicValue1(n2) + n2.cost ? -1 : 1;
							     }
							});
							break;
						case 7:
							q.add(child);
							//q.addFirst(child);
							//Collections.sort(q, Comparator.comparingInt(obj ->
							//	(problem.heuristicValue2(obj) + obj.getCost() )));
							Collections.sort(q, new Comparator<Node>(){
							     public int compare(Node n1, Node n2){
							         if(problem.heuristicValue2(n1) + n1.cost == problem.heuristicValue2(n2) + n2.cost)
							             return 0;
							         return problem.heuristicValue2(n1) + n1.cost < problem.heuristicValue2(n2) + n2.cost ? -1 : 1;
							     }
							});
							break;
					}
				}
			}
		}
		
	}
	
	public abstract int heuristicValue1(Node node);
	public abstract int heuristicValue2(Node node);
	public abstract void resetExploredStates();
	
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
	Integer operator;
	
	public Node(State state, Node parent, int cost, int depth, Integer operator) {
		this.cost=cost;
		this.state=state;
		this.parent=parent;
		this.depth=depth;
		this.operator = operator;
	}
	
	public String toString() {
		return "Cost: " + cost;
	}
	
	public int getCost() {
		return this.cost;
	}
}