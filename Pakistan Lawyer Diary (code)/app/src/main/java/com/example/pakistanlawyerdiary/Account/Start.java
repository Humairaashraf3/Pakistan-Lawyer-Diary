package com.example.pakistanlawyerdiary.Account;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pakistanlawyerdiary.Client.ClientMainActivity;
import com.example.pakistanlawyerdiary.Lawyer.MainActivity;
import com.example.pakistanlawyerdiary.R;
import com.google.firebase.auth.FirebaseAuth;

public class Start extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                checkLogedIn();


            }
        }, SPLASH_TIME_OUT);
    }

    // if user already logedin he/she will be directed towards main activity
    private void checkLogedIn() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            SharedPreferences getShared = getSharedPreferences("userInfo", MODE_PRIVATE);
            String ut = getShared.getString("type", "type");
            //Toast.makeText(this, ut, Toast.LENGTH_SHORT).show();
            if (ut.equals("1")) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            } else {
                Intent i = new Intent(getApplicationContext(), ClientMainActivity.class);
                startActivity(i);
                finish();
            }
        }
        else
        {
            Intent i = new Intent(getApplicationContext(), SignIn.class);
            startActivity(i);
            finish();
        }

    }
}