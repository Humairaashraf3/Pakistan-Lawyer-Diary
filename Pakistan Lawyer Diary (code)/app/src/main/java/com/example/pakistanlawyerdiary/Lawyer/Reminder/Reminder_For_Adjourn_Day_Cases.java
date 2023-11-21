package com.example.pakistanlawyerdiary.Lawyer.Reminder;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pakistanlawyerdiary.Database.DatabaseContract;
import com.example.pakistanlawyerdiary.Database.DatabaseHelper;
import com.example.pakistanlawyerdiary.R;

import java.util.Calendar;

public class Reminder_For_Adjourn_Day_Cases extends AppCompatActivity {

    String email;
    String status;
    Switch sw;
    TimePicker timePicker;
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    Cursor cursor;
    TextView remind_at;
    String t;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder__for__adjourn__day__cases);
        getEmail();
        dbHelper = new DatabaseHelper(this);
        sw = (Switch) findViewById(R.id.switch1);
        timePicker=(TimePicker) findViewById(R.id.time_picker);
        remind_at=(TextView) findViewById(R.id.tv3);
        setSwitch();

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                createRecord();


                if (isChecked)
                {
                    // The toggle is enabled
                    remind_at.setVisibility(View.VISIBLE);
                    timePicker.setVisibility(View.VISIBLE);

                }

                else
                    {
                    // The toggle is disabled
                        db = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        timePicker.setVisibility(View.GONE);
                        remind_at.setVisibility(View.GONE);
                        values.put(DatabaseContract.ADJOURNCASEREMINDER.Col_Status, "off");
                        long newRowId = db.update(DatabaseContract.ADJOURNCASEREMINDER.TABLE_NAME, values, DatabaseContract.ADJOURNCASEREMINDER.Col_Email +"=?",new String[] {email});

                        db.close();
                        values.clear();
                        cancelReminder();
                }
            }
        });


        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            /*
                onTimeChanged

                    void onTimeChanged (
                                    TimePicker view,
                                    int hourOfDay,
                                    int minute
                                    )
                    Parameters
                        view TimePicker: The view associated with this listener.
                        hourOfDay int: The current hour.
                        minute int: The current minute.
            */
            @Override
            public void onTimeChanged(
                    TimePicker timePicker, // TimePicker view
                    int i, // hourOfDay
                    int i1 // Minute
            ) {
                String getAMPMValue = "am";
                if(i>11){
                    getAMPMValue="pm";
                    if(i!=12){
                        i=i-12;
                    }

                }
                if(i==0)
                {
                    i=12;
                }
                if(i1<10)
                {

                    t=""+i+":"+"0"+ i1+ " "+ getAMPMValue;
                }
                // Display the 12 hour format time in TextView
                else {
                    t = "" + i + ":" + i1 + " " + getAMPMValue;
                }
            }
        });
        setS();

    }

    private void cancelReminder()
    {

AlarmManager alarmManager= (AlarmManager) getSystemService(Context.ALARM_SERVICE);
Intent intent=new Intent(this,Adjourn_Notification_Reciever.class);
PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);
alarmManager.cancel(pendingIntent);
//Toast.makeText(this,"Reminder Canceled",Toast.LENGTH_SHORT).show();

    }


    public void save(View v)
{
    if(sw.isChecked())
    {

        showConfirmationDialog();


    }
    else
    {
        showDialog();
    }
}

    private void showConfirmationDialog()

    {
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
        c.set(Calendar.MINUTE, timePicker.getCurrentMinute());
        c.set(Calendar.SECOND, 0);
        //String s= DateFormat.getTimeInstance(DateFormat.SHORT).format(c);
        final Dialog dialog = new Dialog(Reminder_For_Adjourn_Day_Cases.this);
        dialog.setContentView(R.layout.custom_adjourn_reminder_alert);
        dialog.setCancelable(false);
        TextView alertt=dialog.findViewById(R.id.alerttext);
        alertt.setText("You will get the reminder daily at "+t+" time ,if you have adjourn cases on next(tommorrow) date .Do you want to continue ?");
        Button ok = dialog.findViewById(R.id.yes);
        Button no = dialog.findViewById(R.id.no);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(DatabaseContract.ADJOURNCASEREMINDER.Col_Status, "on");
                values.put(DatabaseContract.ADJOURNCASEREMINDER.Col_Hour, timePicker.getCurrentHour());
                values.put(DatabaseContract.ADJOURNCASEREMINDER.Col_Mints, timePicker.getCurrentMinute());
                long newRowId = db.update(DatabaseContract.ADJOURNCASEREMINDER.TABLE_NAME, values, DatabaseContract.ADJOURNCASEREMINDER.Col_Email +"=?",new String[] {email});
                db.close();
                values.clear();
                Intent intent = new Intent(Reminder_For_Adjourn_Day_Cases.this, Adjourn_Notification_Reciever.class);
                intent.putExtra("email",email);
                PendingIntent pendingIntent =PendingIntent.getBroadcast(getApplicationContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT); //time can be updated

                AlarmManager alarmManager= (AlarmManager)getSystemService(ALARM_SERVICE);
                // RTC_wakeup -> alarm will be triggered even device is in sleeep mode
                // interval day-> because we want notification on daily basis
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
                finish();

                dialog.dismiss();
                finish();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.show();



    }


    public void setSwitch()
{
    db = dbHelper.getReadableDatabase();
    String[] columns = {DatabaseContract.ADJOURNCASEREMINDER._ID, DatabaseContract.ADJOURNCASEREMINDER.Col_Email,
            DatabaseContract.ADJOURNCASEREMINDER.Col_Status,
            DatabaseContract.ADJOURNCASEREMINDER.Col_Hour,DatabaseContract.ADJOURNCASEREMINDER.Col_Mints};
    cursor = db.query(DatabaseContract.ADJOURNCASEREMINDER.TABLE_NAME, columns, DatabaseContract.ADJOURNCASEREMINDER.Col_Email + "=?", new String[]{email}, null, null, null, null);

    if (cursor.getCount() == 0)
    {
        db.close();
        cursor.close();
    }
    else
    {
        cursor.moveToNext();
        String status = cursor.getString(2);
        if(status.equals("on"))
        {
            timePicker.setVisibility(View.VISIBLE);
            remind_at.setVisibility(View.VISIBLE);
            timePicker.setCurrentHour(cursor.getInt(3));
            timePicker.setCurrentMinute(cursor.getInt(4));
            sw.setChecked(true);
        }
        else
        {
            sw.setChecked(false);
        }
        db.close();
        cursor.close();
    }




}

public void createRecord()

{


    db = dbHelper.getReadableDatabase();
    String[] columns = {DatabaseContract.ADJOURNCASEREMINDER._ID, DatabaseContract.ADJOURNCASEREMINDER.Col_Email,
            DatabaseContract.ADJOURNCASEREMINDER.Col_Status
    };
    cursor = db.query(DatabaseContract.ADJOURNCASEREMINDER.TABLE_NAME, columns, DatabaseContract.ADJOURNCASEREMINDER.Col_Email + "=?", new String[]{email}, null, null, null, null);
    if (cursor.getCount() != 0)
    {
        db.close();
        cursor.close();

    }
    else
    {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.ADJOURNCASEREMINDER.Col_Email, email);
        values.put(DatabaseContract.ADJOURNCASEREMINDER.Col_Status, "off");
        values.put(DatabaseContract.ADJOURNCASEREMINDER.Col_Hour, 0);
        values.put(DatabaseContract.ADJOURNCASEREMINDER.Col_Mints, 0);
        long newRowId = db.insert(DatabaseContract.ADJOURNCASEREMINDER.TABLE_NAME, null, values);


        if (newRowId > 0) {
            //Toast.makeText(this, "New Record Inserted: " + newRowId, Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(this, "Record Not Inserted: " + newRowId, Toast.LENGTH_SHORT).show();
        }
        db.close();
        cursor.close();
        values.clear();
    }

}

private  void getEmail()

    {

        SharedPreferences getShared=getSharedPreferences("userInfo",MODE_PRIVATE);
        email=getShared.getString("email","Email");
    }

    public void showDialog()
    {

        final Dialog dialog = new Dialog(Reminder_For_Adjourn_Day_Cases.this);
        dialog.setContentView(R.layout.custom_main_alert);
        dialog.setCancelable(false);
        TextView alertt=dialog.findViewById(R.id.alerttext);
        alertt.setText("Reminder for adjourn cases turned off.Do you want to continue ?");
        Button ok = dialog.findViewById(R.id.yes);
        Button no = dialog.findViewById(R.id.no);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.show();
    }

    private void setS()
    {
        String getAMPMValue = "AM";
        int i=timePicker.getCurrentHour();
        int i1=timePicker.getCurrentMinute();
        if(i>11){
            getAMPMValue="PM";
            if(i!=12){
                i=i-12;
            }
        }
        // Display the 12 hour format time in TextView
        t=""+i+":"+ i1+ " "+ getAMPMValue;
    }


    public void main(View v)

    {
        finish();
    }

}