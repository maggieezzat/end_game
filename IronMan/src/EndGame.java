import java.util.Hashtable;

public class EndGame extends SearchProblem {
	
	@Override
	public boolean goalTest(State state) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public State transitionFun(State state, Operator op) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int pathCost(State currentState, Operator op) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	public static void main(String[] args) {
		
	}
	
	
	

}

class EG_State extends State{
	Coordinates iPos;
	//warriors
	Hashtable <Coordinates, Boolean> stones = new Hashtable <Coordinates, Boolean>(6);
	int Damage = 0;
	
	
}

class Coordinates{
	int x;
	int y;
}


enum Operator{
	Up, Down, Left, Right, Collect, Kill, Snap
}



