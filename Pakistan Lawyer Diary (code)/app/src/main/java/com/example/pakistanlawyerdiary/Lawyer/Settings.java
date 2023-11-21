package com.example.pakistanlawyerdiary.Lawyer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pakistanlawyerdiary.Lawyer.Reports.Report;
import com.example.pakistanlawyerdiary.R;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }



    public void courtNames(View v )
    {

        Intent i=new Intent(this, Court_Names.class);
        startActivity(i);
        finish();



    }



    public void caseTypes(View v )
    {

        Intent i=new Intent(this, Case_Types.class);
        startActivity(i);
        finish();


    }


    public void showDisposedCases(View v )
    {

        Intent i=new Intent(this, Disposed_Cases.class);
        startActivity(i);
        finish();


    }


    public void restoreData(View v )
    {

        Intent i=new Intent(this, Restore_Options.class);
        startActivity(i);
        finish();

    }


    public void report(View v )
    {

        Intent intent = new Intent(getApplicationContext(), Report.class);
        startActivity(intent);
        finish();


    }



    public void main(View v )
    {

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();


    }


    public void onBackPressed()
    {

        Intent i=  new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
        finish();

    }
}