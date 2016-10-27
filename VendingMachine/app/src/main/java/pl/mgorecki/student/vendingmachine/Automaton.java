package pl.mgorecki.student.vendingmachine;

import android.widget.Toast;

import java.io.Console;
import java.util.Scanner;
import java.util.logging.Logger;

public class Automaton {
	public static int MAX_AMOUNT_OF_MONEY = 7;



    private String stateHistory = "->0";

	public final int[][] states = { { 1, 2, 5 }, { 2, 3, 6 }, { 3, 4, 7 }, { 4, 5, 0 }, { 5, 6, 0 }, { 6, 7, 0 }, { 7, 0, 0 },
			{ 0, 0, 0 } };
	private int currentStateNumber = 0;

	public String changeState(String coin) {

			switch (coin) {
				case "1":
					currentStateNumber = states[currentStateNumber][0];
					stateHistory += "-"+currentStateNumber;
					break;

				case "2":
					currentStateNumber = states[currentStateNumber][1];
					stateHistory += "-"+currentStateNumber;
					break;

				case "5":
					currentStateNumber = states[currentStateNumber][2];
					stateHistory += "-"+currentStateNumber;
					break;
				default:
					break;
			}

			return String.valueOf(currentStateNumber);
	}
    public String getStateHistory() {
        return stateHistory;
    }


}
