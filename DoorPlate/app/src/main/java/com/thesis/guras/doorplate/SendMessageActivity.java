package com.thesis.guras.doorplate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

public class SendMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        EditText editText = (EditText) findViewById(R.id.sendToLocationEditText);
        Bundle intentExtras = getIntent().getExtras();
        if (intentExtras != null) {
            Log.d("SendMessageActivity","Intent extras was not null");
            String selectedLocationExtra = intentExtras.getString("LocationName");
            editText.setText(selectedLocationExtra);
        }
    }
}
