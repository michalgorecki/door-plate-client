package pl.mgorecki.student.nondeterministicautomaton;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> listOfInputStrings = new ArrayList<String>();
    NFAutomaton automaton = new NFAutomaton();
    ArrayList<ArrayList<ArrayList<String>>> totalStateHistory = new ArrayList<ArrayList<ArrayList<String>>>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView textView = (TextView)findViewById(R.id.textView);
        final Button button = (Button) findViewById(R.id.button);
        final Button showButton  = (Button) findViewById(R.id.gotoHistoryButton);
        InputStream inputStream = getApplicationContext().getResources().openRawResource(R.raw.testfile);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String singleLine;

        try {
            while ((singleLine = bufferedReader.readLine()) != null) {
                listOfInputStrings.add(singleLine);
            }
            Toast.makeText(this,"Successfully imported input strings!",Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            System.out.println("Catched exception! ");
            e.printStackTrace();
        }

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                for(String s : listOfInputStrings){
                    int length = s.length();
                    System.out.println("Length = "+length);
                    for(int i = 0 ; i < length; i++){

                        automaton.changeState(Character.getNumericValue(s.charAt(i)));

                    }

                    textView.append(s);
                    totalStateHistory.add(automaton.getNStatesHistory());
                    if(automaton.getReachedFinalState() == true){
                        textView.append(" - Accepted by the automaton\n");
                    }else{
                        textView.append(" - Not accepted by the automaton\n");
                    }

                    automaton = new NFAutomaton();

                }
            }
        });
        showButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ShowHistoryActivity.class);
                intent.putExtra("history",totalStateHistory);
                startActivity(intent);
            }
        });




    }


}
