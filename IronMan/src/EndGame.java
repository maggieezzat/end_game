
public class EndGame extends SearchProblem {
	
	@Override
	public Boolean goalTest(State state) {
		// TODO Auto-generated method stub
		return null;
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
	
	

}

class EG_State extends State{
	Coordinates iPos;
	//warriors
	Hashtable <Coordinates, Boolean> stones = new Hashtable <Coordinates, Boolean>(6);
	int Damage = 0;
}
