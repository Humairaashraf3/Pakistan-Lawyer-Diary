package com.example.pakistanlawyerdiary.Lawyer.Reports;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pakistanlawyerdiary.Adapter.ReportAdapter;
import com.example.pakistanlawyerdiary.Database.DatabaseContract;
import com.example.pakistanlawyerdiary.Database.DatabaseHelper;
import com.example.pakistanlawyerdiary.R;

import java.util.ArrayList;

public class Specific_Client_Cases extends AppCompatActivity {

    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    SQLiteDatabase db1;
    String email;
    Cursor cursor;
    Activity activity;
    ListView lv;
    ReportAdapter ccl;
    String clientname;
    TextView t;
    ImageView imageView;
    TextView nodata;
    HorizontalScrollView sv;
    LinearLayout l1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific__client__cases);
        Intent i = getIntent();
        clientname = i.getStringExtra("client");
        activity = this;
        t = findViewById(R.id.textview);
        t.setText("Cases Of Client " + clientname);
        imageView=findViewById(R.id.img1);
        nodata=findViewById(R.id.nodata);
        sv=(HorizontalScrollView)findViewById(R.id.sv);
        l1=(LinearLayout)findViewById(R.id.l1);
        getEmail();
        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getReadableDatabase();
        db1 = dbHelper.getReadableDatabase();
        lv = (ListView) findViewById(R.id.list_view);
        final ArrayList<ReportCaseData> cd = new ArrayList<ReportCaseData>();


        String[] columns = {DatabaseContract.CASE._ID, DatabaseContract.CASE.Col_CaseTitle,
                DatabaseContract.CASE.Col_CourtName,
                DatabaseContract.CASE.Col_CaseType,
                DatabaseContract.CASE.Col_CaseNumber,
                DatabaseContract.CASE.Col_CaseYear,
                DatabaseContract.CASE.Col_OnBehalfOf,
                DatabaseContract.CASE.Col_PartyName,
                DatabaseContract.CASE.Col_ContactNumber,DatabaseContract.CASE.Col_PartyAdvocateName,
                DatabaseContract.CASE.Col_AdvocateContactNumber,DatabaseContract.CASE.Col_RespondantName,
                DatabaseContract.CASE.Col_FiledUnderSection,
        };


        cursor = db.query(DatabaseContract.CASE.TABLE_NAME,
                columns,
                DatabaseContract.CASE.Col_Email + "=?" + " AND " +
                        DatabaseContract.CASE.Col_PartyName + "=?"
                , new String[]{email, clientname}, null, null, null, null);

        if (cursor.getCount() == 0)
        {

            //Toast.makeText(this, "No Data To Display ", Toast.LENGTH_SHORT).show();
            sv.setVisibility(View.GONE);
            l1.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            nodata.setVisibility(View.VISIBLE);


        } else {
            int n=1;
            while (cursor.moveToNext()) {

                String t = cursor.getString(1);
                String cn = cursor.getString(2);
                String ct = cursor.getString(3);
                String caseno = String.valueOf(cursor.getString(4));
                String caseyear = String.valueOf(cursor.getString(5));
                String obo = cursor.getString(6);
                String pn = cursor.getString(7);
                String partyno = cursor.getString(8);
                String adv = cursor.getString(9);
                String advno = cursor.getString(10);
                String respo = cursor.getString(11);
                String usec = cursor.getString(12)
                        ;

                ReportCaseData Obj = new ReportCaseData(n, t, cn, ct, caseno,caseyear, obo, pn,
                        partyno, adv, advno,respo,usec);
                cd.add(Obj);
                n++;
            }
            cursor.close();
            db.close();


            ccl = new ReportAdapter(activity, cd);
            lv.setAdapter(ccl);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id)

                {

                }

            });


        }


    }
    private  void getEmail()

    {

        SharedPreferences getShared=getSharedPreferences("userInfo",MODE_PRIVATE);
        email=getShared.getString("email","Email");
    }


    public void back(View v) {


        finish();


    }

    @Override
    public void onBackPressed() {
        finish();
    }



}






