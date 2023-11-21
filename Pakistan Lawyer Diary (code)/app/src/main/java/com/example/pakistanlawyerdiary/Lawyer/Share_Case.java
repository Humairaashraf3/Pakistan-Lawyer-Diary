package com.example.pakistanlawyerdiary.Lawyer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.example.pakistanlawyerdiary.R;

public class Share_Case extends AppCompatActivity {

    String sms_body;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share__case);

        Intent i1 = getIntent();
       sms_body= i1.getStringExtra("casedetails");
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width=dm.widthPixels;
        int height=dm.heightPixels;

        getWindow().setLayout((int)(width*.6),(int)(height*.4));
        wapp();
    }


    public void wapp( )
    {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT, "CASE");
        share.putExtra(Intent.EXTRA_TEXT,sms_body);
        // share.putExtra("sms_body", sms_body);
        startActivity(Intent.createChooser(share, "Share link!"));
        finish();
    }
}