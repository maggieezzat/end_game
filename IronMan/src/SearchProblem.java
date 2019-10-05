import java.util.Hashtable;

public abstract class SearchProblem {
	State initialState;
	enum operators;
	public abstract boolean goalTest(State state);
	public abstract State transitionFun(State state, Operator op);
	public abstract int pathCost(State currentState, Operator op);
}

abstract class State{
//	Coordinates iPos;
//	//warriors
//	Hashtable <Coordinates, Boolean> stones = new Hashtable <Coordinates, Boolean>(6);
//	int Damage = 0;
}

class Coordinates{
	int x;
	int y;
}
enum Operator{
	Up, Down, Left, Right, Collect, Kill, Snap
}
