import java.util.Hashtable;
import java.util.LinkedList;

public class EndGame extends SearchProblem {
	
	Point tPos;
	Point gridSize; // x = 0 --> (gridSize.x - 1), y = 0 --> (gridSize.y - 1)
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
	//change to take node, to calculate cost
	public boolean goalTest(Node node) {
		//System.out.println("goal function");
		EG_State currentState = (EG_State)node.state;
		if(currentState.iPos.equals(tPos) && currentState.stones.isEmpty() 
				&& currentState.snapped == true && node.cost < 100)
			return true;
		else
			return false;
	}
	

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
		case "Left": break;
		case "Collect": cost += 3; break;
		case "Kill": cost += (prevS.warriors.size() - newS.warriors.size()) * 2; break;
		case "Snap": return 0;
		}
		int i = newS.iPos.x;
		int j = newS.iPos.y;
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
	
	
	public EG_State handleRepeatedStates(EG_State newState)
	{

		LinkedList<EG_State> states;
		
		if (! this.explored_states.containsKey(newState.iPos)) {	
			states = new LinkedList<EG_State>();
			states.add(newState);
			this.explored_states.put(newState.iPos, states);
			return newState;
		}
		else {
			states = this.explored_states.get(newState.iPos);
			
			for(EG_State state : states) {
				if (newState.equals(state)) 
					return null;
				
			}
			states.add(newState);
			this.explored_states.put(newState.iPos, states);
			return newState;
		}
	}
}

class EG_State extends State{
	
	Point iPos;
	LinkedList<Point> stones;
	LinkedList<Point> warriors;
	boolean snapped;
	
	public EG_State(Point iPos, LinkedList<Point> stones, LinkedList<Point> warriors) { //initial state
		
		this.iPos = iPos;
		this.stones = stones;
		this.warriors = warriors;
		snapped = false;
	}
	public EG_State(Point iPos, LinkedList<Point> stones, LinkedList<Point> warriors, boolean snapped) {
		this.iPos = iPos;
		this.stones = stones;
		this.warriors = warriors;
		this.snapped = snapped;
	}
	
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
	
	public int hashCode() {
		String x = this.x + "";
		String y = this.y + "";
		String hash = x + "0" + y;
		return Integer.parseInt(hash);
	}
}





