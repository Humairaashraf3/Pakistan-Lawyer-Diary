package com.example.pakistanlawyerdiary.Lawyer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pakistanlawyerdiary.Adapter.CustomHistoryList;
import com.example.pakistanlawyerdiary.Database.DatabaseContract;
import com.example.pakistanlawyerdiary.Database.DatabaseHelper;
import com.example.pakistanlawyerdiary.Model.HistoryData;
import com.example.pakistanlawyerdiary.R;

import java.util.ArrayList;

public class Case_History extends AppCompatActivity {
    ArrayList<HistoryData> casehistory=new ArrayList<HistoryData>();
    SQLiteDatabase db;
    DatabaseHelper dbHelper;
    Cursor cursor;
    ListView lv;
    String casenumber;
    String Id;
    Activity activity;
    TextView tv1;
    LinearLayout linearLayout;
    TextView nodata;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case__history);


        Intent i1 = getIntent();
        Id= i1.getStringExtra("id");
        casenumber= i1.getStringExtra("casenumber");
        getEmail();
        dbHelper = new DatabaseHelper(this);

        activity = this;
        nodata=findViewById(R.id.nodata);
        linearLayout=findViewById(R.id.linearlayout);
        tv1=(TextView)findViewById(R.id.ch1);
        initArray();
        lv = (ListView) findViewById(R.id.chlist);

        if(casehistory.size()<=0)
        {
            nodata.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);

        }
        else
        {

            tv1.setText("Case Number : " + casenumber);
            CustomHistoryList chl = new CustomHistoryList(activity, casehistory);
            lv.setAdapter(chl);
        }


    }
    public void initArray() {
        db = dbHelper.getReadableDatabase();

        String[] columns={DatabaseContract.CASEHISTORY._ID,DatabaseContract.CASEHISTORY.Col_CaseId,
                DatabaseContract.CASEHISTORY.Col_Previousdate,
                DatabaseContract.CASEHISTORY.Col_Adjourndate , DatabaseContract.CASEHISTORY.Col_Step,
                DatabaseContract.CASEHISTORY.Col_LawyerEmail,
        };

        cursor = db.query(DatabaseContract.CASEHISTORY.TABLE_NAME, columns,
                DatabaseContract.CASEHISTORY.Col_CaseId + "=?" +" AND "+DatabaseContract.CASEHISTORY.Col_LawyerEmail+ " =?"
                , new String[]{casenumber,email},
                null, null, null, null);

        if(cursor.getCount()==0) {
            db.close();
            //Toast.makeText(this, "Empty Case History ", Toast.LENGTH_SHORT).show();


        }
        else
        {
            while (cursor.moveToNext())
            {



                    casehistory.add(new HistoryData(cursor.getString(1), cursor.getString(2),
                            cursor.getString(3), cursor.getString(4)) );


            }
            db.close();

        }


    }



    private  void getEmail()

    {

        SharedPreferences getShared=getSharedPreferences("userInfo",MODE_PRIVATE);
        email=getShared.getString("email","Email");
    }

    public void back(View v)
    {
        finish();
    }
}
