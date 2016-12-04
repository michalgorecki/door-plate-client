package pl.mgorecki.student.turingmachine;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.Spannable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class MainActivity extends Activity {
    long SLEEP_TIME = 1000;
    long ARROW_OFFSET = 90;
    long TOTAL_WIDTH = 1810;
    Button playButton;
    Button nextButton;
    Button insertButton;
    TextView tapeTextView;
    TextView stateTextView;
    ImageView imageView;
    View.OnClickListener onClickListener;
    View.OnClickListener nextOnClickListener;
    View.OnClickListener playOnClickListener;
    AlertDialog.Builder alert;
    String numberRegex = "[1-7][0-7]*";

    Handler handler = new Handler();
    Runnable runnable;

    ArrayList<Integer> tape = new ArrayList<>();
    TuringMachine turingMachine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        playButton = (Button) findViewById(R.id.playButton);
        nextButton = (Button) findViewById(R.id.nextButton);
        insertButton = (Button) findViewById(R.id.insertButton);
        tapeTextView = (TextView) findViewById(R.id.tapeTextView);
        stateTextView = (TextView) findViewById(R.id.stateTextView);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setRight(0);
        imageView.setVisibility(View.INVISIBLE);
        alert = new AlertDialog.Builder(this);


        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        };
        insertButton.setOnClickListener(onClickListener);

        runnable = new Runnable() {
            @Override
            public void run() {
                turingMachine.changeState();

                imageView.setX(TOTAL_WIDTH - turingMachine.getIndex() * ARROW_OFFSET);

                tape = turingMachine.getDigitsList();
                tapeTextView.setText(arrayListToString(tape));
                stateTextView.setText("State: " + turingMachine.getState() + " | Index: " + turingMachine.getIndex());
                if (turingMachine.getState() == 3){
                    handler.removeCallbacks(this);
                    Log.d("Runnable","reached final state");
                    stateTextView.setText("Reached final state : 3");
                    return;
                }
                handler.postDelayed(runnable,SLEEP_TIME);

            }
        };

        nextOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (turingMachine == null) {
                    Log.d("nextOnClick", "turingMachine was null.");
                } else {

                    turingMachine.changeState();
                    imageView.setX(1800 - turingMachine.getIndex() * ARROW_OFFSET);
                    tape = turingMachine.getDigitsList();
                    tapeTextView.setText(arrayListToString(tape));
                    stateTextView.setText("State: " + turingMachine.getState() + " | Index: " + turingMachine.getIndex());
                    if (turingMachine.getState() == 3) {
                        nextButton.setVisibility(View.INVISIBLE);
                        stateTextView.setText("Reached final state : 3");
                    }

                    Log.d("nextOnClickListener", "X coordinate: " + imageView.getX());

                }
            }
        };
        nextButton.setOnClickListener(nextOnClickListener);






        playOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (turingMachine == null) {
                    Log.d("PlayOnClick", "turingMachine was null");
                } else {
                    Log.d("PlayOnClick", "turingMachine was ok");
                    imageView.setVisibility(View.VISIBLE);
                    handler.post(runnable);

                }
            }
        };
        playButton.setOnClickListener(playOnClickListener);


    }

    private void showDialog() {
        final EditText edittext = new EditText(this);
        edittext.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setMessage("Insert your number");

        alert.setView(edittext);

        alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String inputNumber = edittext.getText().toString();
                if (Pattern.matches(numberRegex, inputNumber)) {
                    tape = stringToArrayList(inputNumber);
                    tapeTextView.setText(inputNumber);
                    turingMachine = new TuringMachine(tape);
                    imageView.setX(1790);
                    nextButton.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid number", Toast.LENGTH_SHORT).show();
                }

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.show();
    }

    public ArrayList<Integer> stringToArrayList(String inputString) {
        ArrayList<Integer> integerArrayList = new ArrayList<>();
        Character currentCharacter;
        if (!inputString.isEmpty()) {
            for (int i = inputString.length() - 1; i > -1; i--) {
                currentCharacter = inputString.charAt(i);
                if (Character.isDigit(currentCharacter)) {
                    integerArrayList.add(Character.getNumericValue(currentCharacter));
                }
            }
        }

        return integerArrayList;
    }

    public String arrayListToString(ArrayList<Integer> list) {
        String tapeString = "";
        for (int i = list.size() - 1; i > -1; i--) {
            tapeString += list.get(i).toString();
        }
        return tapeString;
    }
}
