package pl.mgorecki.student.turingmachine;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by emigore on 2016-11-23.
 */

public class TuringMachine {

	private final ArrayList<Integer> states = new ArrayList<Integer>();
	Integer newValue = null;
	private Integer currentState;
	private Integer currentIndex;
	private ArrayList<Integer> tape = new ArrayList<>();
	private static final String DEBUG_TAG = "TuringMachine";

	public TuringMachine(ArrayList<Integer> inputDigits) {

		for (int i = 0; i < 5; i++) {
			states.add(i);
		}
		tape = inputDigits;
		System.out.println(tape);
		currentState = 0;
		currentIndex = 0;
	}

	public boolean changeState() {
		System.out.println("Current index: " + currentIndex
				+ ", current state: " + currentState);
		/*
		 * Rule #1
		 */
		if (currentState == 0 && tape.get(currentIndex) > 4) {
			System.out.println("Rule 1");

			switch (tape.get(currentIndex)) {
			case 5:
				newValue = 0;
				tape.set(currentIndex, newValue);
				break;
			case 6:
				newValue = 1;
				tape.set(currentIndex, newValue);
				break;
			case 7:
				newValue = 2;
				tape.set(currentIndex, newValue);
				break;
			default:
				break;
			}
			currentIndex++;
			currentState = 1;
			/*
			 * System.out.println("Current index: " + currentIndex +
			 * ", current state: " + currentState);
			 */
			return false;

		}/*
		 * Rule#2
		 */else if (currentState == 0 && tape.get(currentIndex) <= 4) {
			System.out.println("Rule 2");
			switch (tape.get(currentIndex)) {
			case 0:
				newValue = 3;
				tape.set(currentIndex, newValue);
				break;
			case 1:
				newValue = 4;
				tape.set(currentIndex, newValue);
				break;
			case 2:
				newValue = 5;
				tape.set(currentIndex, newValue);
				break;
			case 3:
				newValue = 6;
				tape.set(currentIndex, newValue);
				break;
			case 4:
				newValue = 7;
				tape.set(currentIndex, newValue);
				break;
			default:
				break;
			}
			currentState = 2;
			currentIndex++;
			/*
			 * System.out.println("Current index: " + currentIndex +
			 * ", current state: " + currentState);
			 */
			return false;
		}

		/*
		 * Rule #3
		 */
		if (currentState == 1 && currentIndex < tape.size() && tape.get(currentIndex) == 7) {
			System.out.println("Rule 3");
			newValue = 0;
			tape.set(currentIndex, newValue);
			currentIndex++;
			/*
			 * System.out.println("Current index: " + currentIndex +
			 * ", current state: " + currentState);
			 */
			return false;
		}/*
		 * Rule #4
		 */else if (currentState == 1 && currentIndex == tape.size()) {
			System.out.println("Rule 4");

			newValue = 1;
			tape.add(newValue);
			currentIndex++;
			currentState = 2;

			return true;
		} else if (currentState == 1 && tape.get(currentIndex) != 7) {

			switch (tape.get(currentIndex)) {
			case 0:
				newValue = 1;
				tape.set(currentIndex, newValue);
				break;
			case 1:
				newValue = 2;
				tape.set(currentIndex, newValue);
				break;
			case 2:
				newValue = 3;
				tape.set(currentIndex, newValue);
				break;
			case 3:
				newValue = 4;
				tape.set(currentIndex, newValue);
				break;
			case 4:
				newValue = 5;
				tape.set(currentIndex, newValue);
				break;
			case 5:
				newValue = 6;
				tape.set(currentIndex, newValue);
				break;
			case 6:
				newValue = 7;
				tape.set(currentIndex, newValue);
				break;
			default:
				break;
			}
			currentIndex++;
			currentState = 2;
			/*
			 * System.out.println("Current index: " + currentIndex +
			 * ", current state: " + currentState);
			 */
			return false;
		}

		/*
		 * Rule #5
		 */
		if (currentState == 2 && currentIndex < tape.size()) {
			System.out.println("Rule 5");
			currentIndex++;
			return false;
		}
		/*
		 * Rule #6
		 */else if (currentState == 2 && currentIndex == tape.size()) {
			System.out.println("Rule 6");
			currentState = 3;
			/*
			 * System.out.println("Current index: " + currentIndex +
			 * ", current state: " + currentState);
			 */
			return true;
		}

		return false;
	}

	public ArrayList<Integer> getDigitsList() {
		return tape;
	}
	public int getState(){
		return currentState;
	}
	public int getIndex(){
		return currentIndex;
	}
}
