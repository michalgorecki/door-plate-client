package pl.mgorecki.student.nondeterministicautomaton;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class NFAutomaton {

	public static final int[][][] states = {	
			{ {0, 1 }, { 0 , 2}, { 0, 3 }, { 0, 4 }, { 0, 5 }, { 0, 6 }, { 0, 7 }, { 0, 8 }, { 0, 9 }, { 0, 10 } },	
			{ { 1, 11 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 } },
			{ { 2 }, { 2, 11 }, { 2 }, { 2 }, { 2 }, { 2 }, { 2 }, { 2 }, { 2 }, { 2 } },
			{ { 3 }, { 3 }, { 3, 11 }, { 3 }, { 3 }, { 3 }, { 3 }, { 3 }, { 3 },  { 3 } },
			{ { 4 }, { 4 }, { 4 }, { 4, 11 }, { 4 }, { 4 }, { 4 }, { 4 }, { 4 },  { 4 }},
			{ { 5 }, { 5 }, { 5 }, { 5 }, { 5, 11 }, { 5 }, { 5 }, { 5 }, { 5 }, { 5 } },
			{ { 6 }, { 6 }, { 6 }, { 6 }, { 6 }, { 6, 11 }, { 6 }, { 6 }, { 6 }, { 6 } },
			{ { 7 }, { 7 }, { 7 }, { 7 }, { 7 }, { 7 }, { 7, 11 }, { 7 }, { 7 }, { 7 } },
			{ { 8 }, { 8 }, { 8 }, { 8 }, { 8 }, { 8 }, { 8 }, { 8, 11 }, { 8 }, { 8 } },
			{ { 9 }, { 9 }, { 9 }, { 9 }, { 9 }, { 9 }, { 9 }, { 9 }, { 9, 11 }, { 9 }},
			{ { 10 }, { 10 }, { 10 }, { 10 }, { 10 }, { 10 }, { 10 }, { 10 }, { 10 }, { 10, 11 } },
			{ { 11 }, { 11 }, { 11 }, { 11 }, { 11 }, { 11 }, { 11 }, { 11 }, { 11 }, { 11 } } };

	/**
	 * history of states - single element of the list contains all states to
	 *which the automaton has jumped after the input number
	 **/
	
	private ArrayList<String> stateHistory = new ArrayList<String>();
    ArrayList<ArrayList<String>> nStatesHistory = new ArrayList<>();


    /**
	 *  stores states only from the previous run
	 */
	private ArrayList<String> previousStates = new ArrayList<String>();

	private boolean reachedFinalState; 
	
	public NFAutomaton() {
		stateHistory.add("0");
		reachedFinalState = false;
	}
	
	/**
	 * this method changes state based on previous states and the current input.
	 * It performs changes for the characters separately
	 * @param input - a single character from file
	 * @return new set of states the automaton has jumped to
	 */
	public String changeState(int input) {
		
		String lastStates = stateHistory.get(stateHistory.size()-1);		
		System.out.println("--States from the previous run: "+lastStates);
		String newStates = "";
        ArrayList<String> nStates = new ArrayList<String>();

		//go into states from the previous run one by one
		for(int i=0;i < lastStates.length();i++){
			int currentStateOfAnalysis = Character.getNumericValue(lastStates.charAt(i));
			System.out.println("Current state that is analyzed = "+currentStateOfAnalysis);
			//go into every possibility to jump from every state
			for(int s : states[currentStateOfAnalysis][input]){
				System.out.println("state["+currentStateOfAnalysis+"]"+"["+input+"]"+"["+s+"]");
				System.out.println("Jump from "+lastStates.charAt(i)+" to "+s);
				
				if(s == 11){
					reachedFinalState = true;
				}
				//3rd dimension of states array
				newStates += s;
                nStates.add(String.valueOf(s));

			}
		}
		stateHistory.add(newStates);
        nStatesHistory.add(nStates);

		return newStates;
	}
	public boolean getReachedFinalState(){
		return reachedFinalState;
	}
	public ArrayList<String> getStateHistory(){ return stateHistory; }
    public ArrayList<ArrayList<String>> getNStatesHistory() { return nStatesHistory;}
}
