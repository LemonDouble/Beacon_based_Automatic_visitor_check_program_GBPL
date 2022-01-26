package com.example.covid_check_program;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class activeBluetooth extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_bluetooth);

        SharedPreferences userDataGetter = getSharedPreferences("userdata", MODE_PRIVATE);

        String userName = userDataGetter.getString("userName","");
        TextView test1 = findViewById(R.id.test1);
        test1.setText(userName);

        String userPhone = userDataGetter.getString("userPhone","");
        TextView test2 = findViewById(R.id.test2);
        test2.setText(userPhone);

        String userEmail = userDataGetter.getString("userEmail","");
        TextView test3 = findViewById(R.id.test3);
        test3.setText(userEmail);

        Intent serviceIntent = new Intent(this, BeaconBackgroundService.class);
        ContextCompat.startForegroundService(this, serviceIntent);
    }
}