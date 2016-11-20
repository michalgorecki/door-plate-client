package pl.mgorecki.student.nondeterministicautomaton;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ShowHistoryActivity extends AppCompatActivity {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_history);
        final Bundle extraContent = getIntent().getExtras();
        final Button showButton = (Button) findViewById(R.id.sButton);
        final TextView textView2 = (TextView) findViewById(R.id.textView2);



        showButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                    if(extraContent != null){
                        int i = 1;
                        ArrayList<ArrayList<ArrayList<String>>> history = (ArrayList) extraContent.get("history");

                        for(ArrayList<ArrayList<String>> list : history){
                            textView2.append("Sequence "+ i+"\n");
                            i++;
                            for(ArrayList<String> singleList : list){
                                for(String state: singleList){
                                    textView2.append(state+",");
                                }
                                textView2.append("\n");
                            }

                        }
                    }
                else{
                        textView2.append("No history found");
                    }
                }
        });
    }
}
