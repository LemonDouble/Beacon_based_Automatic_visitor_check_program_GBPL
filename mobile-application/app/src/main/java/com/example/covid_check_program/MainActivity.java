package com.example.covid_check_program;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void goToPrivacyPolicy(View view){
        SharedPreferences userDataGetter = getSharedPreferences("userdata", MODE_PRIVATE);
        String userName = userDataGetter.getString("userName","");

        if(!userName.equals("")){
            Intent intent = new Intent(this, activeBluetooth.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(this, privacy_policy.class);
            startActivity(intent);
        }
    }
}