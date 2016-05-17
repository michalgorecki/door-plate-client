package com.thesis.guras.doorplate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button sendMessageButton;
    Button saveLocationButton;
    Button chooseLocationButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendMessageButton = (Button) findViewById(R.id.sendMessageMainButton);
        saveLocationButton = (Button) findViewById(R.id.saveMainButton);
        chooseLocationButton = (Button) findViewById(R.id.chooseMainButton);

    }

    public void onClickSendMessage(View v) {
        Intent intent = new Intent(this, SendMessageActivity.class);
        MainActivity.this.startActivity(intent);
    }

    public void onClickSaveLocation(View v) {
        Intent intent = new Intent(this, SaveLocationActivity.class);
        MainActivity.this.startActivity(intent);
    }

    public void onClickChooseLocation(View v) {
        Intent intent = new Intent(this, ChooseLocationActivity.class);
        MainActivity.this.startActivity(intent);
    }
}
