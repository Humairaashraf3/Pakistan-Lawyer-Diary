package com.example.pakistanlawyerdiary.Lawyer.Reminder;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pakistanlawyerdiary.Database.DatabaseContract;
import com.example.pakistanlawyerdiary.Database.DatabaseHelper;
import com.example.pakistanlawyerdiary.R;

import java.util.ArrayList;
import java.util.Calendar;

public class Add_Reminder extends AppCompatActivity {
    EditText  client_name,time,date;
    AutoCompleteTextView title;
    ImageView dd1;
    private Calendar calendar;
    private int mYear,mMonth,mHour,mMinute,mDay;
    private String mDate;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    String email;
    private String mTime;
    private String mActive;
    ArrayList<String> title_array=new ArrayList<String>();
    ArrayList<String>party_array=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__reminder);
      getEmail();
        databaseHelper=new DatabaseHelper(this);
        title=findViewById(R.id.rtitle);
        client_name=findViewById(R.id.cname);
        time=findViewById(R.id.time);
        date=findViewById(R.id.date);
        dd1=(ImageView)findViewById(R.id.dd1);
        mActive="true";
        calendar=Calendar.getInstance();
        setAutocomplete();

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(Add_Reminder.this, android.R.layout.simple_list_item_1, title_array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      title.setAdapter(adapter);

        dd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (title_array.size() < 1) {
                    final Dialog dialog = new Dialog(Add_Reminder.this);
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

        title.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                client_name.setText(String.valueOf(party_array.get(position)));
            }
        });


    }

    private void setAutocomplete()
    {

        db = databaseHelper.getReadableDatabase();
        String[] columns = {DatabaseContract.CASE._ID, DatabaseContract.CASE.Col_CaseTitle,DatabaseContract.CASE.Col_PartyName, DatabaseContract.CASE.Col_Email};
        Cursor c = db.query(DatabaseContract.CASE.TABLE_NAME, columns, DatabaseContract.CASE.Col_Email +"=?",new String[] {email}, null, null, null);


        while (c.moveToNext())
        {

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
                            mTime=hourOfDay+":"+"0"+minute;
                        }
                        else
                        {
                            mTime=hourOfDay+":"+minute;
                        }
                        time.setText(mTime+" "+ getAMPMValue);

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




        // set up calender for notification
        calendar.set(Calendar.MONTH,mMonth);
        calendar.set(Calendar.YEAR,mYear);
        calendar.set(Calendar.DAY_OF_MONTH,mDay);
        calendar.set(Calendar.HOUR_OF_DAY,mHour);
        calendar.set(Calendar.MINUTE,mMinute);
        calendar.set(Calendar.SECOND,0);
        Calendar c1 = Calendar.getInstance();
        c1.set(Calendar.SECOND,0);
       // Date date1 = c1.getTime();
       // Date date2 = calendar.getTime();

        //Toast.makeText(getApplicationContext(),"1"+ date1.toString(), Toast.LENGTH_LONG).show();
        //Toast.makeText(getApplicationContext(), "2"+date2.toString(), Toast.LENGTH_LONG).show();
        if (calendar.getTimeInMillis() < c1.getTimeInMillis())  {

            //Entered date is backdated from current date
            {
                //Toast.makeText(getApplicationContext(), "please select valid date and time", Toast.LENGTH_LONG).show();
                showDialog("please select valid date and time");
                return;
            }

        }

        long SelectedTimeStamp=calendar.getTimeInMillis();
        db=databaseHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(DatabaseContract.CASEREMINDER.Col_Email,email);
        values.put(DatabaseContract.CASEREMINDER.Col_Title,title.getText().toString().trim());
        values.put(DatabaseContract.CASEREMINDER.Col_Client,client_name.getText().toString().trim());
        values.put(DatabaseContract.CASEREMINDER.Col_Date,mDate);
        values.put(DatabaseContract.CASEREMINDER.Col_Time,mHour+":"+mMinute);
        values.put(DatabaseContract.CASEREMINDER.Col_Active,mActive);
        long newRowId = db.insert(DatabaseContract.CASEREMINDER.TABLE_NAME, null, values);
        int r=(int)newRowId;
        if (newRowId > 0)
        {
            db.close();
            values.clear();
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent intent = new Intent(this, Particular_Notification_Reciever.class);
            intent.putExtra("id",String.valueOf(r));
            intent.putExtra("title",title.getText().toString().trim());
            intent.putExtra("email",email);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    this.getApplicationContext(), r, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.set(AlarmManager.RTC_WAKEUP, SelectedTimeStamp, pendingIntent);
           // Toast.makeText(this, "Reminder set: " + newRowId, Toast.LENGTH_SHORT).show();
            final Dialog dialog = new Dialog(Add_Reminder.this);
            dialog.setContentView(R.layout.custom_alert);
            dialog.setCancelable(false);
            TextView alertt=dialog.findViewById(R.id.alerttext);
            alertt.setText("Reminder has been set successfully");
            Button ok=dialog.findViewById(R.id.ok);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    //Toast.makeText(getApplicationContext(),"Dismissed..!!",Toast.LENGTH_SHORT).show();
                    Intent ed = new Intent(getApplicationContext(), Reminder_For_Particular_Case.class);
                    startActivity(ed);
                    finish();
                }
            });
            dialog.show();

        }

        else
            {
                db.close();
                values.clear();
            //Toast.makeText(this, "Reminder Not Set: " + newRowId, Toast.LENGTH_SHORT).show();
        }
        db.close();
        values.clear();
    }






    public void showDialog(String s)
    {

        final Dialog dialog = new Dialog(Add_Reminder.this);
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
    private  void getEmail()

    {

        SharedPreferences getShared=getSharedPreferences("userInfo",MODE_PRIVATE);
        email=getShared.getString("email","Email");
    }



    public void help(View v)
    {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Add_Reminder.this);
        View itemView = getLayoutInflater().inflate(R.layout.help_dialog,null);

        builder.setView(itemView);
        TextView text= itemView.findViewById(R.id.text);
        SpannableStringBuilder ssb=new SpannableStringBuilder
                ("** Press  ");
        ssb.setSpan(new ImageSpan(this,R.drawable.black_dropdown),ssb.length()-1,ssb.length(), 0);
        ssb.append(" icon to choose case title from list of saved cases,by selecting case title client name will be automatically filled.\n \n** Press  ");
        ssb.setSpan(new ImageSpan(this,R.drawable.black_done_icon),ssb.length()-1,ssb.length(), 0);
        ssb.append(" icon to set reminder.");

        text.setText(ssb);
        ImageView cancel= itemView.findViewById(R.id.cancel);

        final AlertDialog dialog=builder.create();
        dialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }


    public void onBackPressed()
    {

        Intent i=  new Intent(getApplicationContext(), Reminder_For_Particular_Case.class);
        startActivity(i);
        finish();

    }
    public void main(View v)
    {
        Intent i=  new Intent(getApplicationContext(), Reminder_For_Particular_Case.class);
        startActivity(i);
        finish();
    }
}