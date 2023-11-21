package com.example.pakistanlawyerdiary.Lawyer.Reminder;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pakistanlawyerdiary.Database.DatabaseContract;
import com.example.pakistanlawyerdiary.Database.DatabaseHelper;
import com.example.pakistanlawyerdiary.R;

import java.util.ArrayList;
import java.util.Calendar;

public class Edit_Reminder extends AppCompatActivity {
    String id;
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    Cursor cursor;
    String email;
    EditText client_name,time,date;
    AutoCompleteTextView title;
    ImageView dd1;
    private String mDate;
    private String mTime;
    private Calendar calendar;
    private int mYear,mMonth,mHour,mMinute,mDay;
    ArrayList<String> title_array=new ArrayList<String>();
    ArrayList<String>party_array=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__reminder);
        id=getIntent().getStringExtra("id");

        //Toast.makeText(this,id,Toast.LENGTH_SHORT).show();
        title=findViewById(R.id.rtitle);
        client_name=findViewById(R.id.cname);
        time=findViewById(R.id.time);
        date=findViewById(R.id.date);
        dd1=(ImageView)findViewById(R.id.dd1);
        calendar=Calendar.getInstance();
        dbHelper=new DatabaseHelper(this);
        db=dbHelper.getReadableDatabase();
        String[] columns={DatabaseContract.CASEREMINDER._ID,DatabaseContract.CASEREMINDER.Col_Title,
                DatabaseContract.CASEREMINDER.Col_Client,DatabaseContract.CASEREMINDER.Col_Date,DatabaseContract.CASEREMINDER.Col_Time,
                DatabaseContract.CASE.Col_Email
        };

        cursor = db.query(DatabaseContract.CASEREMINDER.TABLE_NAME, columns,  DatabaseContract.CASEREMINDER._ID + "=?" , new String[]{id}, null, null, null, null);
        cursor.moveToNext();
        String id = String.valueOf(cursor.getInt(0));
        String t = cursor.getString(1);
        String client = cursor.getString(2);
        String d = cursor.getString(3);
        String tim = cursor.getString(4);
        email= cursor.getString(5);

        title.setText(t);
        client_name.setText(client);
        date.setText(d);
        mDate=d;
        String arr1[]=d.split("/");
        mDay=Integer.parseInt(arr1[0]);
        mMonth=Integer.parseInt(arr1[1])-1;
        mYear=Integer.parseInt(arr1[2]);

        String arr[]=tim.split(":");
        mHour=Integer.parseInt(arr[0]);
        mMinute=Integer.parseInt(arr[1]);

        String getAMPMValue = "AM";
        int hourOfDay=mHour;
        int minute=mMinute;
        if(hourOfDay>11){
            getAMPMValue="PM";
            if(hourOfDay!=12)
            {
                hourOfDay=hourOfDay-12;
            }

        }
        if(hourOfDay==0)
        {
            hourOfDay=12;
        }
        if(minute<10)
        {
            mTime=hourOfDay+":"+"0"+minute;
        }
        else
        {
            mTime=hourOfDay+":"+minute;
        }
        time.setText(mTime+" "+ getAMPMValue);

        setAutocomplete();
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(Edit_Reminder.this, android.R.layout.simple_list_item_1, title_array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        title.setAdapter(adapter);

        dd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (title_array.size() < 1) {
                    final Dialog dialog = new Dialog(Edit_Reminder.this);
                    dialog.setContentView(R.layout.custom_alert);
                    TextView alertt=dialog.findViewById(R.id.alerttext);
                    alertt.setText("Case data is not available please enter new case");
                    Button ok=dialog.findViewById(R.id.ok);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();


                }
                else
                {
                    title.showDropDown();

                }
            }
        });
        title.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                client_name.setText(String.valueOf(party_array.get(position)));
            }
        });



        //title.setSelection(title.getText().length()); by doing this we wont be able to edit text in center because cursor wont move in center
        //client_name.setSelection( client_name.getText().length());
    }




    private void setAutocomplete()
    {

        db = dbHelper.getReadableDatabase();
        String[] columns = {DatabaseContract.CASE._ID, DatabaseContract.CASE.Col_CaseTitle,DatabaseContract.CASE.Col_PartyName, DatabaseContract.CASE.Col_Email};
        Cursor c = db.query(DatabaseContract.CASE.TABLE_NAME, columns, DatabaseContract.CASE.Col_Email +"=?",new String[] {email}, null, null, null);


        while (c.moveToNext()) {

            String t = c.getString(1);
            String client = c.getString(2);
            title_array.add(t);
            party_array.add(client);



        }

        c.close();
        db.close();

    }

    public void setDate(View v)
    {
        Calendar now=Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        mMonth=monthOfYear;
                        monthOfYear++;
                        mDay=dayOfMonth;
                        mYear=year;
                        mDate=dayOfMonth+"/"+monthOfYear+"/"+year;
                        date.setText(mDate);

                    }
                },now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }


    public void setTime(View v)
    {
        Calendar now=Calendar.getInstance();
        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        mHour=hourOfDay;
                        mMinute=minute;
                        String getAMPMValue = "AM";
                        if(hourOfDay>11){
                            getAMPMValue="PM";
                            if(hourOfDay!=12)
                            {
                                hourOfDay=hourOfDay-12;
                            }

                        }
                        if(hourOfDay==0)
                        {
                            hourOfDay=12;
                        }
                        if(minute<10)
                        {
                            mTime=hourOfDay+":"+"0"+minute+" "+ getAMPMValue;
                        }
                        else
                        {
                            mTime=hourOfDay+":"+minute+" "+ getAMPMValue;
                        }
                        time.setText(mTime);

                    }
                }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), false);
        timePickerDialog.show();

    }

    public void save(View v)
    {
    String t=title.getText().toString();
    String c=client_name.getText().toString();
    String d=date.getText().toString();
    String tm=time.getText().toString();
        if(t.isEmpty()||c.isEmpty()||d.isEmpty()||t.isEmpty()||tm.isEmpty())
    {
        showDialog("Please enter all details");

        return;
    }

        Calendar c1 = Calendar.getInstance();
        Calendar datetime = Calendar.getInstance();
        datetime.set(Calendar.HOUR_OF_DAY, mHour);
        datetime.set(Calendar.MINUTE, mMinute);

// set up calender for notification
        calendar.set(Calendar.MONTH,mMonth);
        calendar.set(Calendar.YEAR,mYear);
        calendar.set(Calendar.DAY_OF_MONTH,mDay);
        calendar.set(Calendar.HOUR_OF_DAY,mHour);
        calendar.set(Calendar.MINUTE,mMinute);
        calendar.set(Calendar.SECOND,0);
        c1.set(Calendar.SECOND,0);
       // Date date1 = c1.getTime();
        //Date date2 = calendar.getTime();

       // Toast.makeText(getApplicationContext(),"1"+ date1.toString(), Toast.LENGTH_LONG).show();
        //Toast.makeText(getApplicationContext(), "2"+date2.toString(), Toast.LENGTH_LONG).show();
            if (calendar.getTimeInMillis() < c1.getTimeInMillis())  {

                //Entered date is backdated from current date
                {
                    showDialog("please select valid date and time");
                    return;
                }

            }



                long SelectedTimeStamp = calendar.getTimeInMillis();
                db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(DatabaseContract.CASEREMINDER.Col_Email, email);
                values.put(DatabaseContract.CASEREMINDER.Col_Title, title.getText().toString().trim());
                values.put(DatabaseContract.CASEREMINDER.Col_Client, client_name.getText().toString().trim());
                values.put(DatabaseContract.CASEREMINDER.Col_Date, mDate);
                values.put(DatabaseContract.CASEREMINDER.Col_Time, mHour + ":" + mMinute);
                values.put(DatabaseContract.CASEREMINDER.Col_Active, "true");
                long newRowId = db.update(DatabaseContract.CASEREMINDER.TABLE_NAME, values, DatabaseContract.CASEREMINDER._ID + "=?", new String[]{String.valueOf(id)});
                int r = Integer.parseInt(id);
                if (newRowId > 0)
                {

                    db.close();
                    values.clear();
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    Intent intent = new Intent(this, Particular_Notification_Reciever.class);
                    intent.putExtra("id", String.valueOf(r));
                    intent.putExtra("title", title.getText().toString().trim());
                    intent.putExtra("email",email);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(
                            this.getApplicationContext(), r, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, SelectedTimeStamp, pendingIntent);
                    //Toast.makeText(this, "Reminder updated: " + newRowId, Toast.LENGTH_SHORT).show();
                    final Dialog dialog = new Dialog(Edit_Reminder.this);
                    dialog.setContentView(R.layout.custom_alert);
                    dialog.setCancelable(false);
                    TextView alertt=dialog.findViewById(R.id.alerttext);
                    alertt.setText("Reminder has been updated successfully");
                    Button ok=dialog.findViewById(R.id.ok);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            //Toast.makeText(getApplicationContext(),"Dismissed..!!",Toast.LENGTH_SHORT).show();
                            finishAffinity();
                            Intent ed = new Intent(getApplicationContext(), Reminder_For_Particular_Case.class);
                            startActivity(ed);
                            finish();
                        }
                    });
                    dialog.show();
                }

                else {
                    db.close();
                    values.clear();
                    //Toast.makeText(this, "Reminder Not updated: " + newRowId, Toast.LENGTH_SHORT).show();
                }
                db.close();
                values.clear();

}



    public void showDialog(String s)
    {

        final Dialog dialog = new Dialog(Edit_Reminder.this);
        dialog.setContentView(R.layout.custom_alert);
        dialog.setCancelable(false);
        TextView alertt=dialog.findViewById(R.id.alerttext);
        alertt.setText(s);
        Button ok=dialog.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //Toast.makeText(getApplicationContext(),"Dismissed..!!",Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }


    public void delete(View v)
    {
        db=dbHelper.getWritableDatabase();
        final Dialog dialog = new Dialog(Edit_Reminder.this);
        dialog.setContentView(R.layout.custom_main_alert);
        TextView alertt=dialog.findViewById(R.id.alerttext);
        alertt.setText("Do you really want to delete this Reminder ?");
        Button ok=dialog.findViewById(R.id.yes);
        Button no=dialog.findViewById(R.id.no);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(Edit_Reminder.this, Particular_Notification_Reciever.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), Integer.parseInt(id), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);
                //Toast.makeText(Edit_Reminder.this, "Reminder Canceled", Toast.LENGTH_SHORT).show();

                Integer i1 = db.delete(DatabaseContract.CASEREMINDER.TABLE_NAME, DatabaseContract.CASEREMINDER._ID + "=?", new String[]{String.valueOf(id)});
                if (i1 > 0) {
                    db.close();
                    dialog.dismiss();
                    //Toast.makeText(getApplicationContext(), " reminder deleted: ", Toast.LENGTH_SHORT).show();
                    finishAffinity();
                    Intent ed = new Intent(getApplicationContext(), Reminder_For_Particular_Case.class);
                    startActivity(ed);
                    finish();

                }
                else {
                    dialog.dismiss();
                    db.close();
                   // Toast.makeText(getApplicationContext(), " reminder Not deleted: ", Toast.LENGTH_SHORT).show();

                }

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


public void back(View v)
{

    finishAffinity(); // close all open activities
    Intent ed = new Intent(getApplicationContext(), Reminder_For_Particular_Case.class);
    startActivity(ed);
    finish();
}


    public void onBackPressed()
    {
        finishAffinity();
        Intent i=  new Intent(getApplicationContext(), Reminder_For_Particular_Case.class);
        startActivity(i);
        finish();

    }
    public void help(View v)
    {

    }
}