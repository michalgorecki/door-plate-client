package com.thesis.guras.doorplate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendMessageButton(View v){
        Intent intent = new Intent(this, SendMessageActivity.class);
        MainActivity.this.startActivity(intent);
    }
    public void onClickSaveLocation(View v){
        Intent intent = new Intent(this, SaveLocationActivity.class);
        MainActivity.this.startActivity(intent);
    }
}
