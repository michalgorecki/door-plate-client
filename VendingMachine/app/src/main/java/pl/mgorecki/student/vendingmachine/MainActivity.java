package pl.mgorecki.student.vendingmachine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    String currentState = "";
    Automaton automaton = new Automaton();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button1 = (Button) findViewById(R.id.button1);
        final Button button2 = (Button) findViewById(R.id.button2);
        final Button button5 = (Button) findViewById(R.id.button5);
        final Button exitButton = (Button) findViewById(R.id.exitbutton);
        final Button resetButton = (Button) findViewById(R.id.resetbutton);
        final Button purchaseButton = (Button) findViewById(R.id.purchasebutton);
        final TextView textView = (TextView) findViewById(R.id.textView);
        purchaseButton.setVisibility(View.INVISIBLE);

        textView.setText("Insert coin...");

        button1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                currentState = automaton.changeState("1");
                if(currentState == "7"){
                    Toast.makeText(MainActivity.this,"Reached final price. Inserting more coins will return all you've inserted!",Toast.LENGTH_LONG).show();
                    purchaseButton.setVisibility(View.VISIBLE);
                }else{
                    purchaseButton.setVisibility(View.INVISIBLE);
                }
                textView.setText(currentState);

            }
        });
        button2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                currentState = automaton.changeState("2");
                if(currentState == "7"){
                    Toast.makeText(MainActivity.this,"Reached final price. Inserting more coins will return all you've inserted!",Toast.LENGTH_LONG).show();
                    purchaseButton.setVisibility(View.VISIBLE);
                }else{
                    purchaseButton.setVisibility(View.INVISIBLE);
                }
                textView.setText( currentState);
            }
        });
        button5.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                currentState = automaton.changeState("5");
                if(currentState == "7"){
                    Toast.makeText(MainActivity.this,"Reached final price. Inserting more coins will return all you've inserted!",Toast.LENGTH_LONG).show();
                    purchaseButton.setVisibility(View.VISIBLE);
                }else{
                    purchaseButton.setVisibility(View.INVISIBLE);
                }
                textView.setText( currentState);
            }
        });
        exitButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                textView.setText(automaton.getStateHistory());
                automaton = new Automaton();
            }
        });
        resetButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                textView.setText("Insert coin...");
                automaton = new Automaton();
            }
        });

        purchaseButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                textView.setText("Please collect your ticket. Insert coin to buy another ticket...");
                purchaseButton.setVisibility(View.GONE);

            }
        });

    }


}
