import java.util.LinkedList;

public class EndGame extends SearchProblem {
	Point tPos;
	Point gridSize; // x = 0 --> (gridSize.x - 1), y = 0 --> (gridSize.y - 1)
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
	}
	
	@Override
	public boolean goalTest(State state) {
		EG_State currentState = (EG_State)state;
		if(currentState.iPos.equals(tPos) && currentState.stones.isEmpty() && currentState.snapped == true)
			return true;
		else
			return false;
	}

	@Override
	public State transitionFun(State state, String op) {
		EG_State nextState = ((EG_State)state).clone();
		if(nextState.damage > 100)
			return null; //no need to generate child states as they will never reach the goal
		int i = nextState.iPos.x;
		int j = nextState.iPos.y;
		int d = nextState.damage;
		switch(op) {
			case "Up":
				if(i-1 < 0 || nextState.warriors.contains(new Point(i-1,j)))
					return null; //invalid operator
				else if(tPos.equals(new Point(i-1,j)) && (!nextState.stones.isEmpty() || nextState.damage >= 100))
					return null;
				else i -= 1; break;
			case "Down":
				if(i+1 >= gridSize.x || nextState.warriors.contains(new Point(i+1,j)))
					return null; //invalid operator
				else if(tPos.equals(new Point(i+1,j)) && (!nextState.stones.isEmpty() || nextState.damage >= 100))
					return null;
				else i += 1; break;
			case "Left": 
				if(j-1 < 0 || nextState.warriors.contains(new Point(i,j-1)))
					return null; //invalid operator
				else if(tPos.equals(new Point(i,j-1)) && (!nextState.stones.isEmpty() || nextState.damage >= 100))
					return null;
				else j -= 1; break;
			case "Right": 
				if(j+1 >= gridSize.y || nextState.warriors.contains(new Point(i,j+1)))
					return null; //invalid operator
				else if(tPos.equals(new Point(i,j+1)) && (!nextState.stones.isEmpty() || nextState.damage >= 100))
					return null;
				else j += 1; break;
			case "Collect":
				if(!nextState.stones.contains(nextState.iPos))
					return null; //invalid operator
				else {
					nextState.stones.remove(nextState.iPos);
					d += 3;
				} break;
			case "Kill": 
				boolean killed = false;
				if(i-1 >= 0 && nextState.warriors.contains(new Point(i-1,j)) ) {
					killed = true;
					d += 2;
					nextState.warriors.remove(new Point(i-1,j));
					}
				if(i+1 < gridSize.x && nextState.warriors.contains(new Point(i+1,j)) ) {
					killed = true;
					d += 2;
					nextState.warriors.remove(new Point(i+1,j));
					}
				if(j+1 < gridSize.y && nextState.warriors.contains(new Point(i,j+1)) ) {
					killed = true;
					d += 2;
					nextState.warriors.remove(new Point(i,j+1));
				}
				if(j-1 >= 0 && nextState.warriors.contains(new Point(i,j-1)) ) {
					killed = true;
					d += 2;
					nextState.warriors.remove(new Point(i,j-1));
				}
				if(!killed) return null; //noone to kill ==> invalid operator
				break;
			case "Snap": 
				if(tPos.equals(nextState.iPos) && nextState.stones.isEmpty() && nextState.damage < 100) {
					nextState.snapped = true;
					return nextState;
				}
				break;
		}
		
		if(i-1 >= 0) { //check up for adjacency-damage
			if( nextState.warriors.contains(new Point(i-1,j)) ) d += 1;
			if( tPos.equals(new Point(i-1,j)) ) d += 5;
		}
		if(i+1 < gridSize.x) { //check down for adjacency-damage
			if( nextState.warriors.contains(new Point(i+1,j)) ) d += 1;
			if( tPos.equals(new Point(i+1,j)) ) d += 5;
		}
		if(j+1 < gridSize.y) { //check right for adjacency-damage
			if( nextState.warriors.contains(new Point(i,j+1)) ) d += 1;
			if( tPos.equals(new Point(i,j+1)) ) d += 5;
		}
		if(j-1 >= 0) { //check left for adjacency-damage
			if( nextState.warriors.contains(new Point(i,j-1)) ) d += 1;
			if( tPos.equals(new Point(i,j-1)) ) d += 5;
		}
		
		nextState.iPos = new Point(i,j);
		nextState.damage = d;
		return nextState;
		
	}

	@Override
	public int pathCost(State currentState, State newState, String op) { //operator applicability already checked in transitionFun
		return ((EG_State)currentState).damage - ((EG_State)newState).damage;
	}
}

class EG_State extends State{
	Point iPos;
	LinkedList<Point> stones;
	LinkedList<Point> warriors;
	int damage;
	boolean snapped;
	
	public EG_State(Point iPos, LinkedList<Point> stones, LinkedList<Point> warriors) { //initial state
		
		this.iPos = iPos;
		this.stones = stones;
		this.warriors = warriors;
		damage = 0;
		snapped = false;
	}
	public EG_State(Point iPos, LinkedList<Point> stones, LinkedList<Point> warriors, int damage, boolean snapped) {
		this.iPos = iPos;
		this.stones = stones;
		this.warriors = warriors;
		this.damage = damage;
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
		return new EG_State(this.iPos.clone(), stonesCopy, warriorsCopy, this.damage, this.snapped);
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
}





