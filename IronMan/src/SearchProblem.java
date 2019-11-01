import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Set;
import java.util.PriorityQueue;

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
		
		PriorityQueue<Node> pq = new PriorityQueue<Node>();
		LinkedList<Node> q = new LinkedList<Node>(); 
		//initial node (root)
		Node node = new Node(problem.initialState, null, 0, 0, null);
		
		switch (strategy){
			case 0: q.add(node); break; //DFS
			case 1: q.add(node); break; //IDS
			case 2: q.add(node); break; //BFS 
			case 3: pq = new PriorityQueue<Node>(new ucComparator()); pq.add(node); break; //UCS
			case 4: pq = new PriorityQueue<Node>(new gr1Comparator(problem)); pq.add(node); break; //GR1
			case 5: pq = new PriorityQueue<Node>(new gr2Comparator(problem)); pq.add(node); break; //GR2
			case 6: pq = new PriorityQueue<Node>(new as1Comparator(problem)); pq.add(node); break; //AS1
			case 7: pq = new PriorityQueue<Node>(new as2Comparator(problem)); pq.add(node); break; //AS2 
		}
		

		while(true) {
			
			switch(strategy) {
				case 0: //DFS
					if(q.isEmpty()) return null;
					node = q.removeFirst(); break;
				case 2: //BFS
					if(q.isEmpty()) return null;
					node = q.removeFirst(); break;
				case 1:	//IDS
					if(q.isEmpty()) {
						depthLimit++; //increase the depth limit
						q = new LinkedList<Node>(); //restart
						q.add(new Node(problem.initialState, null, 0, 0, null));
						problem.resetExploredStates();
					}
					node = q.removeFirst(); break;
				case 3: //UCS
				case 4: //GR1
				case 5: //GR2
				case 6: //AS1
				case 7: if(pq.isEmpty()) return null;
					node = pq.poll(); break;
			}
				
			visited++;
			exploredNodes++;
			
			//if this node passes the goal test, it is the solution node, return it
			if(problem.goalTest(node)) 
				return node;
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
		        switch(strategy) {	
					case 0: for(Node child : children)  q.addFirst(child); break; //DFS
					case 1: for(Node child : children)  q.addFirst(child); break; //IDS
					case 2: for(Node child : children)  q.addLast(child);  break; //BFS
					case 3: //UCS is based only on the path cost g(n)
					case 4: //Greedy is based only on the heuristic function h(n)
					case 5:
					case 6: //A* is based on the evaluation function h(n) + g(n)
					case 7: for(Node child : children)  pq.add(child); break;	
				}
			
			
			} //end of else if
			
		} //end of while True
		
	} //end of genericSearch
	
	
	
	public abstract int heuristicValue1(Node node);
	public abstract int heuristicValue2(Node node);
	public abstract void resetExploredStates();
	
	
}



abstract class State{
}

//a node is a 5-tuple:
class Node {
	
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
	
}




class ucComparator implements Comparator<Node>{
	public int compare(Node n1, Node n2){
        if(n1.cost == n2.cost)
            return 0;
        return n1.cost < n2.cost ? -1 : 1;
    }
}

class gr1Comparator implements Comparator<Node>{
	
	SearchProblem problem;
	
	public gr1Comparator(SearchProblem problem) {
		this.problem = problem;
	}
	
	public int compare(Node n1, Node n2){
        if(problem.heuristicValue1(n1) == problem.heuristicValue1(n2))
            return 0;
        return problem.heuristicValue1(n1) < problem.heuristicValue1(n2) ? -1 : 1;
    }
}

class gr2Comparator implements Comparator<Node>{
	
	SearchProblem problem;
	
	public gr2Comparator(SearchProblem problem) {
		this.problem = problem;
	}
	
	public int compare(Node n1, Node n2){
        if(problem.heuristicValue2(n1) == problem.heuristicValue2(n2))
            return 0;
        return problem.heuristicValue2(n1) < problem.heuristicValue2(n2) ? -1 : 1;
    }
}


class as1Comparator implements Comparator<Node>{
	
	SearchProblem problem;
	
	public as1Comparator(SearchProblem problem) {
		this.problem = problem;
	}
	public int compare(Node n1, Node n2){
		if(problem.heuristicValue1(n1) + n1.cost == problem.heuristicValue1(n2) + n2.cost)
            return 0;
        return problem.heuristicValue1(n1) + n1.cost < problem.heuristicValue1(n2) + n2.cost ? -1 : 1;    
	
	}
}


class as2Comparator implements Comparator<Node>{
	
	SearchProblem problem;
	
	public as2Comparator(SearchProblem problem) {
		this.problem = problem;
	}
	public int compare(Node n1, Node n2){
		if(problem.heuristicValue2(n1) + n1.cost == problem.heuristicValue2(n2) + n2.cost)
            return 0;
        return problem.heuristicValue2(n1) + n1.cost < problem.heuristicValue2(n2) + n2.cost ? -1 : 1;    
	
	}
}
