
public abstract class SearchProblem {
	
	
	public Operator operators[];
	public Node initialState;
	public Node stateSpace[];
	
	public abstract Node[] qingFunction();
	
	public void generalSearch() {
		
	}
	
	public boolean goalTestFunction() {
		
		return false;
	}
	
	public int pathCostFunction() {
		
		return 0;
	}
	
	//public Node transitionFunction(Node currentState) {
		
		//Node newState = new Node();
		//return newState;
	//}
	
	public String search() {
		
	}
	
	

}
