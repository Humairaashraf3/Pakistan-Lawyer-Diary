package com.example.pakistanlawyerdiary.Lawyer;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pakistanlawyerdiary.Database.DatabaseContract;
import com.example.pakistanlawyerdiary.Database.DatabaseHelper;
import com.example.pakistanlawyerdiary.Lawyer.manager.NetworkManager;
import com.example.pakistanlawyerdiary.Model.HistoryData;
import com.example.pakistanlawyerdiary.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Add_Step extends AppCompatActivity {
    DatabaseReference reference;
    String uid,adTime;
    EditText time;
    String tt;
    private String mTime;
    EditText date;
    String Id ,a ,s;
    private int mYear, mMonth, mHour, mMinute, mDay;
    private String mDate;
    NetworkManager networkManager = new NetworkManager();
    private Calendar calendar;
    private int year,month ,day;
    private TextView dateView;
    String previousdate;
    EditText step;
    EditText adj;
    String casenum;
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    private static final int PICK_IMAGE_REQUEST=100;
    String casen;
    String dt,t;
    String email;
    TextView abc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__step);
        Intent i=getIntent();
        previousdate=i.getStringExtra("previous");
        Id=i.getStringExtra("id");
        t = i.getStringExtra("t");
        casen=i.getStringExtra("CASE_NUMBER");
        casenum=casen.trim();
        if (t.equals("daycases"))

        {
            dt = i.getStringExtra("date");
        }
        getEmail();
        dbHelper = new DatabaseHelper(this);
        step=findViewById(R.id.ed1);
        adj=findViewById(R.id.ed2);
        date=findViewById(R.id.ed2);
        time=findViewById(R.id.edtime);

        calendar= Calendar.getInstance();
        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH);
        day=calendar.get(Calendar.DAY_OF_MONTH);

    }


    public void save(View v) {

try {


    uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    reference = FirebaseDatabase.getInstance().getReference("CaseHistory");
    s = step.getText().toString();
    a = adj.getText().toString();
    tt = time.getText().toString();
    if (s.isEmpty() || a.isEmpty() || tt.isEmpty())
    {

        final Dialog dialog = new Dialog(Add_Step.this);
        dialog.setContentView(R.layout.custom_alert);
        dialog.setCancelable(false);
        TextView alertt = dialog.findViewById(R.id.alerttext);
        alertt.setText("Please enter all details");
        Button ok = dialog.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        dialog.show();


    }
    else if(istimePassed())
    {

        final Dialog dialog = new Dialog(Add_Step.this);
        dialog.setContentView(R.layout.custom_alert);
        dialog.setCancelable(false);
        TextView alertt = dialog.findViewById(R.id.alerttext);
        alertt.setText("This time has been passed plaese select valid time ");
        Button ok = dialog.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        dialog.show();
    }

    else if(isReser().equals("1"))
    {

        final Dialog dialog = new Dialog(Add_Step.this);
        dialog.setContentView(R.layout.custom_alert);
        dialog.setCancelable(false);
        TextView alertt = dialog.findViewById(R.id.alerttext);
        alertt.setText("This Date and Time is already reserved ");
        Button ok = dialog.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        dialog.show();
        // Toast.makeText(getApplicationContext(), String.valueOf(isReser()), Toast.LENGTH_SHORT).show();


    }

    else {

        db=dbHelper.getWritableDatabase();
        if (networkManager.checkInternetConnection(Add_Step.this)) {
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DatabaseContract.CASEHISTORY.Col_CaseId, casenum);
            values.put(DatabaseContract.CASEHISTORY.Col_Previousdate, previousdate);
            values.put(DatabaseContract.CASEHISTORY.Col_Adjourndate, a);
            values.put(DatabaseContract.CASEHISTORY.Col_Step, s);
            values.put(DatabaseContract.CASEHISTORY.Col_HearingTime, adTime);
            values.put(DatabaseContract.CASEHISTORY.Col_isLive, "1");
            values.put(DatabaseContract.CASEHISTORY.Col_LawyerEmail, email);
            long newRowId = db.insert(DatabaseContract.CASEHISTORY.TABLE_NAME, null, values);


            if (newRowId > 0) {

                //Toast.makeText(getApplicationContext(), "New Adjourn Date Inserted With Internet: " + newRowId, Toast.LENGTH_SHORT).show();
                db.close();

                HistoryData ch = new HistoryData(casenum, previousdate,
                        a, s, adTime, uid, email);
                reference.push().setValue(ch);
                final Dialog dialog = new Dialog(Add_Step.this);
                dialog.setContentView(R.layout.custom_alert);
                TextView alertt = dialog.findViewById(R.id.alerttext);
                alertt.setText("Step data has been saved");
                Button ok = dialog.findViewById(R.id.ok);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i;
                        if (t.equals("all")) {
                            i = new Intent(Add_Step.this, Case_Details.class);
                            i.putExtra("id", Id);
                            i.putExtra("t", t);
                            startActivity(i);
                            finish();
                        } else if (t.equals("daycases")) {
                            i = new Intent(Add_Step.this, Case_Details.class);
                            i.putExtra("id", Id);
                            i.putExtra("t", t);
                            i.putExtra("date", dt);
                            startActivity(i);
                            finish();

                        } else {
                            i = new Intent(Add_Step.this, Case_Details.class);
                            i.putExtra("id", Id);
                            i.putExtra("t", t);
                            startActivity(i);
                            finish();
                        }
                        dialog.dismiss();
                    }
                });
                dialog.show();

            } else {
                db.close();
                Toast.makeText(getApplicationContext(), "Adjourn Date Not Inserted : " + newRowId, Toast.LENGTH_SHORT).show();
            }
        } else {
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DatabaseContract.CASEHISTORY.Col_CaseId, casenum);
            values.put(DatabaseContract.CASEHISTORY.Col_Previousdate, previousdate);
            values.put(DatabaseContract.CASEHISTORY.Col_Adjourndate, a);
            values.put(DatabaseContract.CASEHISTORY.Col_Step, s);
            values.put(DatabaseContract.CASEHISTORY.Col_HearingTime, adTime);
            values.put(DatabaseContract.CASEHISTORY.Col_LawyerEmail, email);
            values.put(DatabaseContract.CASEHISTORY.Col_isLive, "0");

            long newRowId = db.insert(DatabaseContract.CASEHISTORY.TABLE_NAME, null, values);


            if (newRowId > 0) {


                // Toast.makeText(getApplicationContext(), "New Adjourn Date Inserted without internet: " + newRowId, Toast.LENGTH_SHORT).show();
                final Dialog dialog = new Dialog(Add_Step.this);
                dialog.setContentView(R.layout.custom_alert);
                TextView alertt = dialog.findViewById(R.id.alerttext);
                alertt.setText("Step data has been saved");
                Button ok = dialog.findViewById(R.id.ok);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i;
                        if (t.equals("all")) {
                            i = new Intent(Add_Step.this, Case_Details.class);
                            i.putExtra("id", Id);
                            i.putExtra("t", t);
                            startActivity(i);
                            finish();
                        } else if (t.equals("daycases")) {
                            i = new Intent(Add_Step.this, Case_Details.class);
                            i.putExtra("id", Id);
                            i.putExtra("t", t);
                            i.putExtra("date", dt);
                            startActivity(i);
                            finish();

                        } else {
                            i = new Intent(Add_Step.this, Case_Details.class);
                            i.putExtra("id", Id);
                            i.putExtra("t", t);
                            startActivity(i);
                            finish();
                        }
                        dialog.dismiss();
                    }
                });
                dialog.show();


                db.close();

            } else {
                db.close();
                Toast.makeText(getApplicationContext(), "Adjourn Date Not Inserted: " + newRowId, Toast.LENGTH_SHORT).show();
            }
        }


    }
}
catch(Exception e)
{

}
    }



    public void setTime(View v) {
        Calendar now = Calendar.getInstance();
        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        mHour = hourOfDay;
                        mMinute = minute;
                        String getAMPMValue = "am";
                        if (hourOfDay > 11) {
                            getAMPMValue = "pm";
                            if (hourOfDay != 12) {
                                hourOfDay = hourOfDay - 12;
                            }

                        }
                        if (hourOfDay == 0) {
                            hourOfDay = 12;
                        }
                        if (minute < 10) {
                            mTime = hourOfDay + ":" + "0" + minute;
                        } else {
                            mTime = hourOfDay + ":" + minute;
                        }
                        time.setText(mTime + " " + getAMPMValue);
                        adTime = mTime +":"+getAMPMValue;
                    }
                }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), false);
        timePickerDialog.show();
    }
    public void setDate(View v) {
        Calendar now = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        mMonth = monthOfYear;
                        monthOfYear++;
                        mDay = dayOfMonth;
                        mYear = year;
                        mDate = dayOfMonth + "-" + monthOfYear + "-" + year;
                        adj.setText(mDate);
                        a = mDate;

                    }
                }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }




    @SuppressWarnings("deprecation")
    public void chooseDate(View view) {
        dateView = (TextView) view;
        showDialog(999);
        // Toast.makeText(getApplicationContext(), "calander", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    dateView.setText(new StringBuilder().append(arg3).append("/").append(arg2+1).append("/").append(arg1));
                    // showDate(arg1, arg2+1, arg3);
                }
            };





    private String isReser()
    {

        String id,st,mt,md,cn;
        String ad = "";
        String ti="";
        db = dbHelper.getReadableDatabase();
        String[] columns = {DatabaseContract.CASE._ID,DatabaseContract.CASE.Col_CaseNumber, DatabaseContract.CASE.Col_Status, DatabaseContract.CASE.Col_Email,DatabaseContract.CASE.Col_MeetingDate,DatabaseContract.CASE.Col_MeetingTime};
        Cursor c = db.query(DatabaseContract.CASE.TABLE_NAME, columns, DatabaseContract.CASE.Col_Email + "=?", new String[]{email}, null, null, null);


        while (c.moveToNext())
        {
            cn = c.getString(1);
            st = c.getString(2);
            md = c.getString(4);
            mt = c.getString(5);

            if (st.equals("open") && md.equals(String.valueOf(a)) && mt.equals(String.valueOf(adTime))) {

                //Toast.makeText(getApplicationContext(), "inside isteserved", Toast.LENGTH_SHORT).show();

                    return "1";

            }
            else {


                String query = "SELECT * FROM casehistory WHERE casehistory.CaseId = ? AND casehistory.LawyerEmail =?";
                Cursor historycursor = db.rawQuery(query, new String[]{cn, email});
                if (historycursor.getCount() > 0) {
                    historycursor.moveToLast();
                    id = historycursor.getString(1);
                    if (!id.equals(casenum))
                    {
                        ad = historycursor.getString(3);
                        ti = historycursor.getString(5);
                        if (ad.equals(a) && ti.equals(adTime)) {
                            return "1";
                        }

                    }
                }

            }
        }



        c.close();
        db.close();

        return "0";

    }


    private boolean istimePassed()

    {

        String[] s_hour;

        String[] c_hour;

        int sHOUR, sMinute;
        String sAMPM;

        int cHOUR, cMinute;
        String cAMPM;
        String istoday;

        SimpleDateFormat df = new SimpleDateFormat("d-M-yyyy");
        String cdate = df.format(Calendar.getInstance().getTime());
        //Toast.makeText(getApplicationContext(), cdate, Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(), meetingDate, Toast.LENGTH_SHORT).show();
        if(a.equals(cdate)) {
            String currentTime = new SimpleDateFormat("h:mm:a", Locale.getDefault()).format(new Date());
            s_hour = adTime.split(":");
           // Toast.makeText(getApplicationContext(), currentTime, Toast.LENGTH_SHORT).show();
           // Toast.makeText(getApplicationContext(), meetingTime, Toast.LENGTH_SHORT).show();
            sHOUR = Integer.parseInt(s_hour[0]);
            sMinute = Integer.parseInt(s_hour[1].trim());
            sAMPM = s_hour[2];
            c_hour = currentTime.split(":");
            cHOUR = Integer.parseInt(c_hour[0]);
            cMinute = Integer.parseInt(c_hour[1].trim());
            cAMPM = c_hour[2].toLowerCase();
            if (cHOUR == sHOUR) {
                if (cMinute >= sMinute) {
                    if (sAMPM.equals(cAMPM)) {
                        return true;
                    }
                }
            } else if (cHOUR > sHOUR) {
                if (sAMPM.equals(cAMPM)) {
                    return true;
                }
            }
        }
        return false;

    }



    private  void getEmail()

    {

        SharedPreferences getShared=getSharedPreferences("userInfo",MODE_PRIVATE);
        email=getShared.getString("email","Email");
    }


    public void cancel(View v)
    {
        Intent i;
        if(t.equals("all")) {
            i = new Intent(this, Case_Details.class);
            i.putExtra("id", Id);
            i.putExtra("t", t);
            startActivity(i);
            finish();
        }

        else if(t.equals("daycases"))
        {
            i = new Intent(this, Case_Details.class);
            i.putExtra("id", Id);
            i.putExtra("t", t);
            i.putExtra("date",dt);
            startActivity(i);
            finish();

        }
        else
        {
            i = new Intent(this, Case_Details.class);
            i.putExtra("id", Id);
            i.putExtra("t", t);
            startActivity(i);
            finish();
        }
    }

    public void back(View v)
    {
        Intent i;
        if(t.equals("all")) {
            i = new Intent(this, Case_Details.class);
            i.putExtra("id", Id);
            i.putExtra("t", t);
            startActivity(i);
            finish();
        }

        else if(t.equals("daycases"))
        {
            i = new Intent(this, Case_Details.class);
            i.putExtra("id", Id);
            i.putExtra("t", t);
            i.putExtra("date",dt);
            startActivity(i);
            finish();

        }
        else
        {
            i = new Intent(this, Case_Details.class);
            i.putExtra("id", Id);
            i.putExtra("t", t);
            startActivity(i);
            finish();
        }
    }
    public void onBackPressed()
    {

        Intent i;
        if(t.equals("all")) {
            i = new Intent(this, Case_Details.class);
            i.putExtra("id", Id);
            i.putExtra("t", t);
            startActivity(i);
            finish();
        }

        else if(t.equals("daycases"))
        {
            i = new Intent(this, Case_Details.class);
            i.putExtra("id", Id);
            i.putExtra("t", t);
            i.putExtra("date",dt);
            startActivity(i);
            finish();

        }
        else
        {
            i = new Intent(this, Case_Details.class);
            i.putExtra("id", Id);
            i.putExtra("t", t);
            startActivity(i);
            finish();
        }

    }


}