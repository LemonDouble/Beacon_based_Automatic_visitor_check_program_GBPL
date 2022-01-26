package com.example.covid_check_program;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class FillForm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_form);
    }

    public void goToActiveBluetooth(View view){

        EditText form_name = findViewById(R.id.form_name_editText);
        EditText form_phone = findViewById(R.id.form_phone_editText);
        EditText form_email = findViewById(R.id.form_email_editText);
        SharedPreferences stored_user_data;

        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(this);

        if(form_name.getText().toString().equals("")){
            alert_confirm.setMessage("Please enter your name!");
            alert_confirm.setPositiveButton("OK",null);
            AlertDialog alert = alert_confirm.create();
            alert.show();
        }else if(form_phone.getText().toString().equals("")){
            alert_confirm.setMessage("Please enter your phone number!");
            alert_confirm.setPositiveButton("OK",null);
            AlertDialog alert = alert_confirm.create();
            alert.show();
        }else if(form_email.getText().toString().equals("")){
            alert_confirm.setMessage("Please enter your email!");
            alert_confirm.setPositiveButton("OK",null);
            AlertDialog alert = alert_confirm.create();
            alert.show();
        }else{
            stored_user_data = getSharedPreferences("userdata",MODE_PRIVATE);
            SharedPreferences.Editor saveEditor = stored_user_data.edit();

            saveEditor.putString("userName",form_name.getText().toString());
            saveEditor.putString("userPhone",form_phone.getText().toString());
            saveEditor.putString("userEmail",form_email.getText().toString());

            saveEditor.commit();

            Intent intent = new Intent(this, activeBluetooth.class);
            startActivity(intent);
        }

    }

}