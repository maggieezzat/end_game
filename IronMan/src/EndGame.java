import java.util.Hashtable;
import java.util.LinkedList;

public class EndGame extends SearchProblem {
	
	//thanos position
	Point tPos;
	Point gridSize; // x : 0 --> (gridSize.x - 1), y : 0 --> (gridSize.y - 1)
	
	//hashtable of explored states
	//the key is iron man position
	//the value is a linkedlist of states with iron man's position the same as the key
	Hashtable<Point,LinkedList<EG_State>> explored_states;
	
	public EndGame(Point gridSize, Point iPos, Point tPos, LinkedList <Point> stones, LinkedList<Point> warriors) {
		super(new EG_State(iPos, stones, warriors), new LinkedList<String>()); //initial state, operators
		operators.add("Up");
		operators.add("Down");
		operators.add("Left");
		operators.add("Right");
		operators.add("Collect");
		operators.add("Kill");
		operators.add("Snap");
		this.gridSize = gridSize;
		this.tPos = tPos;
		explored_states = new Hashtable<Point,LinkedList<EG_State>>();
	}
	
	@Override
	//overriding the goal test function 
	public boolean goalTest(Node node) {
		//type cast the generic state of the node to an end game state 
		EG_State currentState = (EG_State)node.state;
		
		//a node is the goal if there are no remaining stones, and iron man in the same cell as thanos
		//he also snapped his fingers and the cost from the root is less than 100
		if(currentState.iPos.equals(tPos) && currentState.stones.isEmpty() 
				&& currentState.snapped == true && node.cost < 100)
			return true;
		else
			return false;
	}
	
	//overriding the transition function 
	@Override
	public State transitionFun(Node node, String op) {
		EG_State nextState = ((EG_State)node.state).clone();

		if(node.cost > 100)
			return null; //no need to generate child states as they will never reach the goal
		int i = nextState.iPos.x;
		int j = nextState.iPos.y;
		switch(op) {
			case "Up":
				if(i-1 < 0 || nextState.warriors.contains(new Point(i-1,j)))
					return null; //invalid operator
				else if(tPos.equals(new Point(i-1,j)) && !nextState.stones.isEmpty())
					return null;
				else i -= 1; break;
			case "Down":
				if(i+1 >= gridSize.x || nextState.warriors.contains(new Point(i+1,j)))
					return null; //invalid operator
				else if(tPos.equals(new Point(i+1,j)) && !nextState.stones.isEmpty())
					return null;
				else i += 1; break;
			case "Left": 
				if(j-1 < 0 || nextState.warriors.contains(new Point(i,j-1)))
					return null; //invalid operator
				else if(tPos.equals(new Point(i,j-1)) && !nextState.stones.isEmpty() )
					return null;
				else j -= 1; break;
			case "Right": 
				if(j+1 >= gridSize.y || nextState.warriors.contains(new Point(i,j+1)))
					return null; //invalid operator
				else if(tPos.equals(new Point(i,j+1)) && !nextState.stones.isEmpty())
					return null;
				else j += 1; break;
			case "Collect":
				if(!nextState.stones.contains(nextState.iPos))
					return null; //invalid operator
				else {
					nextState.stones.remove(nextState.iPos);
					//System.out.println("collecting");
					//d += 3;
				} break;
			case "Kill": 
				boolean killed = false;
				if(i-1 >= 0 && nextState.warriors.contains(new Point(i-1,j)) ) {
					killed = true;
					//d += 2;
					nextState.warriors.remove(new Point(i-1,j));
					}
				if(i+1 < gridSize.x && nextState.warriors.contains(new Point(i+1,j)) ) {
					killed = true;
					//d += 2;
					nextState.warriors.remove(new Point(i+1,j));
					}
				if(j+1 < gridSize.y && nextState.warriors.contains(new Point(i,j+1)) ) {
					killed = true;
					//d += 2;
					nextState.warriors.remove(new Point(i,j+1));
				}
				if(j-1 >= 0 && nextState.warriors.contains(new Point(i,j-1)) ) {
					killed = true;
					//d += 2;
					nextState.warriors.remove(new Point(i,j-1));
				}
				if(!killed) return null; //noone to kill ==> invalid operator
				break;
			case "Snap": 
				if(tPos.equals(nextState.iPos) && nextState.stones.isEmpty()) {
					nextState.snapped = true;
					return nextState;
				}
				break;
		}
		
		
		
		nextState.iPos = new Point(i,j);
		return handleRepeatedStates(nextState);
		
	}

	//overriding the path cost function 
	@Override
	public int pathCost(State prevState, State newState, String op) { //operator applicability already checked in transitionFun
		//return ((EG_State)currentState).damage - ((EG_State)newState).damage;
		EG_State prevS = (EG_State) prevState;
		EG_State newS = (EG_State) newState;
		int cost = 0;
		switch (op){
		case "Up":
		case "Down":
		case "Right":
		case "Left": break; //no cost for these operators
		
		case "Collect": cost += 3; break;
		case "Kill": cost += (prevS.warriors.size() - newS.warriors.size()) * 2; break; //2*num of warriors in adjacent cells
		case "Snap": return 0; //no cost
		}
		int i = newS.iPos.x;
		int j = newS.iPos.y;
		
		//checking the cost of adjacency:
		//adjacent to thanos or adjacent to warriors
		if( tPos.equals(new Point(i,j)) ) cost += 5;
		if(i-1 >= 0) { //check up for adjacency-damage
			if( newS.warriors.contains(new Point(i-1,j)) ) cost += 1;
			if( tPos.equals(new Point(i-1,j)) ) cost += 5;
		}
		if(i+1 < gridSize.x) { //check down for adjacency-damage
			if( newS.warriors.contains(new Point(i+1,j)) ) cost += 1;
			if( tPos.equals(new Point(i+1,j)) ) cost += 5;
		}
		if(j+1 < gridSize.y) { //check right for adjacency-damage
			if( newS.warriors.contains(new Point(i,j+1)) ) cost += 1;
			if( tPos.equals(new Point(i,j+1)) ) cost += 5;
		}
		if(j-1 >= 0) { //check left for adjacency-damage
			if( newS.warriors.contains(new Point(i,j-1)) ) cost += 1;
			if( tPos.equals(new Point(i,j-1)) ) cost += 5;
		}
		
		return cost;
	}
	
	//checking if a state is a repeated state
	public EG_State handleRepeatedStates(EG_State newState)
	{

		LinkedList<EG_State> states;
		//explored states is a hashtable, with keys as the points (locations) of iron man
		//and values as linked list of states where iron man is in position key 
		
		//if the key is not there, then for sure this is a new state
		if (! this.explored_states.containsKey(newState.iPos)) {	
			states = new LinkedList<EG_State>();
			states.add(newState);
			this.explored_states.put(newState.iPos, states);
			return newState;
		}
		else {
			states = this.explored_states.get(newState.iPos);
			
			//if it's a repeated state, returns null
			for(EG_State state : states) {
				if (newState.equals(state)) 
					return null;
				
			}
			//if it's a new state, add it and return it
			states.add(newState);
			this.explored_states.put(newState.iPos, states);
			return newState;
		}
	}
}

//a state for the end game problem:
class EG_State extends State{
	
	//iron man position
	Point iPos;
	//remaining stones positions
	LinkedList<Point> stones;
	//remaining warriors positions
	LinkedList<Point> warriors;
	
	//whether he snapped or not yet
	boolean snapped;
	
	//initial state
	public EG_State(Point iPos, LinkedList<Point> stones, LinkedList<Point> warriors) { 
		
		this.iPos = iPos;
		this.stones = stones;
		this.warriors = warriors;
		snapped = false;
	}
	
	//any other state
	public EG_State(Point iPos, LinkedList<Point> stones, LinkedList<Point> warriors, boolean snapped) {
		this.iPos = iPos;
		this.stones = stones;
		this.warriors = warriors;
		this.snapped = snapped;
	}
	
	//used to update the state, a state is clones, then applying the operators modified accordingly to give the new state
	public EG_State clone() {
		LinkedList<Point> stonesCopy = new LinkedList<Point>();
		LinkedList<Point> warriorsCopy = new LinkedList<Point>();
		for(Point p : this.stones) {
			stonesCopy.add(p.clone());
		}
		for(Point p : this.warriors) {
			warriorsCopy.add(p.clone());
		}
		return new EG_State(this.iPos.clone(), stonesCopy, warriorsCopy, this.snapped);
	}

	//just for testing purposes
	public String toString()
	{
		String s = "iman:" + this.iPos.toString() + "; stones: ";
		
		for(Point stone: this.stones)
		{
			s += stone.toString() + "; ";
		}
		s += "; warriors";
		
		for(Point warrior: this.warriors)
		{
			s += warrior.toString() + "; ";
		}
		return  s;
		
	}
	
	//overriding the equals method in order to compare states
	//this is used to make sure we never enqueue redundant states
	public boolean equals(Object o)
	{
		if (o == this) //reference
            return true; 
  
        if (!(o instanceof EG_State)) 
            return false; 
           
        EG_State compared_state = (EG_State) o;
        if(this.iPos.equals(compared_state.iPos)) {
        	if(this.warriors.size() != compared_state.warriors.size() 
        			|| this.stones.size() != compared_state.stones.size())
        		return false;
        	
        	for(Point warr : this.warriors)
        		if(!compared_state.warriors.contains(warr))
        			return false;
        	
        	for(Point stone : this.stones)
        		if(!compared_state.stones.contains(stone))
        			return false;
        
        	
        	return true;
        }
        return false;
      
	}
}

//a point is a (x,y) tuple in space
class Point{
	int x;
	int y;
	public Point(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public Point clone() {
		return new Point(this.x,this.y);
	}
	
	public boolean equals(Object o) { 
		   
        if (o == this) { 
            return true; 
        } 
  
        if (!(o instanceof Point)) { 
            return false; 
        } 
           
        Point p = (Point) o; 
           
        return (this.x == p.x && this.y == p.y) ? true : false;
    } 
	
	public String toString()
	{
		return this.x + "," + this.y;
	}
	
	//define the hash code for the hash function to be a string with x and y separated with 0, then typecasting this string to an int
	//this is needed in order to make the points as keys to the hashtable
	public int hashCode() {
		String x = this.x + "";
		String y = this.y + "";
		String hash = x + "0" + y;
		return Integer.parseInt(hash);
	}
}





