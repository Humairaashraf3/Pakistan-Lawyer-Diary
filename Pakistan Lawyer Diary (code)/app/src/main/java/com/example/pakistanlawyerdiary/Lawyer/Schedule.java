package com.example.pakistanlawyerdiary.Lawyer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pakistanlawyerdiary.Adapter.ScheduleAdapter;
import com.example.pakistanlawyerdiary.Database.DatabaseContract;
import com.example.pakistanlawyerdiary.Database.DatabaseHelper;
import com.example.pakistanlawyerdiary.Model.ScheduleData;
import com.example.pakistanlawyerdiary.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Schedule extends AppCompatActivity {
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    SQLiteDatabase db1;
    String email;
    Cursor cursor;
    String  ad, htime;
    String date;
    TextView current_date;
    ImageView imageView;
    TextView nodata;
    ScheduleAdapter sa;
    List<ScheduleData> schedulelist = new ArrayList<ScheduleData>();
    ListView schedule_list_view;
    Activity activity;
    String cdate;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        Intent i=getIntent();
        date=i.getStringExtra("date");
        getEmail();
        imageView = findViewById(R.id.img1);
        nodata = findViewById(R.id.nodata);
        activity = this;
        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getReadableDatabase();
        db1 = dbHelper.getReadableDatabase();
        current_date = findViewById(R.id.current_date);
        schedule_list_view = findViewById(R.id.schedule_list_view);
       SimpleDateFormat df = new SimpleDateFormat("d-M-yyyy");
       cdate = df.format(Calendar.getInstance().getTime());
        current_date.setText("Date: " + date);
        GetScheduleData(date);

        if (schedulelist.isEmpty())
        {
            imageView.setVisibility(View.VISIBLE);
            nodata.setVisibility(View.VISIBLE);
        }
        else
            {

            sa = new ScheduleAdapter(activity, schedulelist);
            schedule_list_view.setAdapter(sa);
            schedule_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    Intent intent = new Intent(getApplicationContext(), Case_Details.class);
                    intent.putExtra("id",schedulelist.get(position).getId());
                    intent.putExtra("t","daycases");
                    intent.putExtra("date",date);
                    startActivity(intent);
                    finish();

                }

            });
        }
    }

    private void GetScheduleData(String todayDate)
    {
        String[] columns = {DatabaseContract.CASE._ID,DatabaseContract.CASE.Col_CaseTitle,
                DatabaseContract.CASE.Col_CaseType, DatabaseContract.CASE.Col_CaseNumber,
                DatabaseContract.CASE.Col_PartyName,
                DatabaseContract.CASE.Col_MeetingDate,
                DatabaseContract.CASE.Col_MeetingTime,
                DatabaseContract.CASE._ID
        };
        cursor = db.query(DatabaseContract.CASE.TABLE_NAME, columns,
                DatabaseContract.CASE.Col_MeetingDate + "=? AND "+DatabaseContract.CASE.Col_Email+ " =? AND "+DatabaseContract.CASE.Col_Status+ " =?" ,
                new String[]{todayDate,email,"open"},
                null, null, null, null);
        if (cursor.getCount() == 0) {
//            db.close();
        }
        else
            {
            while (cursor.moveToNext()) {
             String id=String.valueOf(cursor.getInt(0));
                String t = cursor.getString(1);
                String ct = cursor.getString(2);
                String caseno = cursor.getString(3);
                String pn = cursor.getString(4);
                String mdate = cursor.getString(5);
                String mtime = cursor.getString(6);
                if(todayDate.equals(cdate)) {
                    schedulelist.add(new ScheduleData(id, t, ct, caseno, pn, mdate, mtime, "CASE MEETING","1"));
                }
                else
                {
                   schedulelist.add(new ScheduleData(id, t, ct, caseno, pn, mdate, mtime, "CASE MEETING","0"));
                }
            }
        }
        cursor.close();


       /* Cursor c1=db1.rawQuery("SELECT Addcase.CaseTitle, Addcase.CaseType,Addcase.PartyName, casehistory.CaseId,casehistory.AdjournDate," +
                        "casehistory.HiringTime FROM Addcase JOIN casehistory ON Addcase.CaseNumber=casehistory.CaseId WHERE casehistory.AdjournDate=? ",
                new String[]{date});
*/


        Cursor c = db.query(DatabaseContract.CASE.TABLE_NAME, columns,
                DatabaseContract.CASE.Col_Status + "=? AND "+DatabaseContract.CASE.Col_Email+ " =?" ,
                new String[]{"open",email},
                null, null, null, null);
        if (c.getCount() > 0)
        {
            while (c.moveToNext()) {
                String id=String.valueOf(c.getInt(0));
                String t = c.getString(1);
                String ct = c.getString(2);
                String casenumber=c.getString(3);
                String pn = c.getString(4);


                String query = "SELECT * FROM casehistory WHERE casehistory.CaseId = ? AND casehistory.LawyerEmail =?";
                Cursor historycursor = db1.rawQuery(query, new String[]{casenumber,email});
                if(historycursor.getCount()>0)
                {

                    historycursor.moveToLast();
                    ad = historycursor.getString(3);
                    if(ad.equals(todayDate))
                    {

                        htime = historycursor.getString(5);
                        if(todayDate.equals(cdate)) {
                            schedulelist.add(new ScheduleData(id, t, ct, casenumber, pn, ad, htime, "CASE HEARING","1"));
                        }
                        else
                        {
                            schedulelist.add(new ScheduleData(id, t, ct, casenumber, pn, ad, htime, "CASE HEARING","0"));
                        }
                    }
                }
            }
        }


    }


    private void getEmail() {
        SharedPreferences getShared = getSharedPreferences("userInfo", MODE_PRIVATE);
        email = getShared.getString("email", "Email");
    }

    public void help(View v)


    {
        // AllCaseHelp arh= new AllCaseHelp();
        // arh.show(getSupportFragmentManager(),"TIPS");
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