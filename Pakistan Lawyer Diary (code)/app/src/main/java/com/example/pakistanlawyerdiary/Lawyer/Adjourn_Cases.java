package com.example.pakistanlawyerdiary.Lawyer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pakistanlawyerdiary.Adapter.CustomCaseList;
import com.example.pakistanlawyerdiary.Database.DatabaseContract;
import com.example.pakistanlawyerdiary.Database.DatabaseHelper;
import com.example.pakistanlawyerdiary.Model.CaseData;
import com.example.pakistanlawyerdiary.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Adjourn_Cases extends AppCompatActivity {

    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    SQLiteDatabase db1;
    String email;
    Cursor cursor;
    Activity activity;
    ListView lv;
    CustomCaseList ccl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adjourn__cases);
        getEmail();
        activity = this;
        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getReadableDatabase();
        db1 = dbHelper.getReadableDatabase();
        lv = (ListView) findViewById(R.id.list_view);


        final ArrayList<CaseData> cd = new ArrayList<CaseData>();


        String[] columns = {DatabaseContract.CASE._ID, DatabaseContract.CASE.Col_CaseTitle,
                DatabaseContract.CASE.Col_CourtName, DatabaseContract.CASE.Col_CaseType, DatabaseContract.CASE.Col_CaseNumber,
                DatabaseContract.CASE.Col_CaseYear, DatabaseContract.CASE.Col_OnBehalfOf, DatabaseContract.CASE.Col_PartyName,
        };
        String[] columns1 = {DatabaseContract.CASEHISTORY._ID, DatabaseContract.CASEHISTORY.Col_CaseId,
                DatabaseContract.CASEHISTORY.Col_Previousdate, DatabaseContract.CASEHISTORY.Col_Adjourndate, DatabaseContract.CASEHISTORY.Col_Step
        };

        // String sortOrder = DatabaseContract.ADDCASE._ID + " ASC";
        cursor = db.query(DatabaseContract.CASE.TABLE_NAME, columns, DatabaseContract.CASE.Col_Email + "=?" + " AND " +
                        DatabaseContract.CASE.Col_Status + "=?"
                , new String[]{email, "open"} , null, null, null, null);

        if (cursor.getCount() == 0) {
            db.close();
            //Toast.makeText(this, "No Adjourn Cases ", Toast.LENGTH_SHORT).show();


        } else {
            while (cursor.moveToNext()) {
                String pd = "";
                String ad = "";
                String step = "";

                int id = cursor.getInt(0);
                String t = cursor.getString(1);
                String cn = cursor.getString(2);
                String ct = cursor.getString(3);
                String caseno = String.valueOf(cursor.getString(4));
                String obo = cursor.getString(6);
                String pn = cursor.getString(7);

                Cursor c1 = db1.query(DatabaseContract.CASEHISTORY.TABLE_NAME, columns1, DatabaseContract.CASEHISTORY.Col_CaseId + "=? AND "+DatabaseContract.CASEHISTORY.Col_LawyerEmail+ " =?", new String[]{caseno,email}, null, null, null);
                if (c1.getCount() > 0) {
                    c1.moveToLast();
                    pd = c1.getString(2);
                    ad = c1.getString(3);
                    step = c1.getString(4);

                }
                c1.close();
                if (gettomorrowDate().equals(ad))
                {
                    CaseData Obj = new CaseData(id, t, cn, ct, caseno, obo, pn, pd, ad, step);
                    cd.add(Obj);
                }
            }
            cursor.close();
            db.close();

            if (cd.size() > 0) {
                ccl = new CustomCaseList(activity, cd);
                lv.setAdapter(ccl);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                        String cid = String.valueOf(cd.get(position).getId());
                        //Toast.makeText(getApplicationContext(), cid, Toast.LENGTH_SHORT).show();
                        finishAffinity();
                        Intent intent = new Intent(getApplicationContext(), Case_Details.class);
                        intent.putExtra("id", cid);
                        intent.putExtra("t", "tommorrow");
                        startActivity(intent);
                        finish();

                    }

                });
            }
        }


    }

    private  void getEmail()

    {

        SharedPreferences getShared=getSharedPreferences("userInfo",MODE_PRIVATE);
        email=getShared.getString("email","Email");
    }



    public String gettomorrowDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("d-M-yyyy");
        String tomorrowAsString = dateFormat.format(tomorrow);
        return tomorrowAsString;
    }


    public void main(View v)
    {
        finishAffinity();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onBackPressed() {
        finishAffinity();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();

    }
}