package com.example.covid_check_program;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

public class privacy_policy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
    }

    public void goToFillForm(View view){

        final CheckBox agree_check = findViewById(R.id.agree_checkbox);

        if (agree_check.isChecked()) {
            Intent intent = new Intent(this, FillForm.class);
            startActivity(intent);
        } else {
            AlertDialog.Builder alert_confirm = new AlertDialog.Builder(this);
            alert_confirm.setMessage("Please agree to continue!");
            alert_confirm.setPositiveButton("OK",null);
            AlertDialog alert = alert_confirm.create();
            alert.show();
        }

    }


}