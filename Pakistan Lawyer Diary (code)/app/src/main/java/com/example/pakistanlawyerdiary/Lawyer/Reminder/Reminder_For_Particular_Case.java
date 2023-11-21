package com.example.pakistanlawyerdiary.Lawyer.Reminder;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pakistanlawyerdiary.Adapter.ReminderAdapter;
import com.example.pakistanlawyerdiary.Database.DatabaseContract;
import com.example.pakistanlawyerdiary.Database.DatabaseHelper;
import com.example.pakistanlawyerdiary.Lawyer.MainActivity;
import com.example.pakistanlawyerdiary.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Reminder_For_Particular_Case extends AppCompatActivity {
    ImageView imageView;
    TextView nodata;
    private FloatingActionButton add_reminder;
    ReminderAdapter reminderAdapter;
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    ListView lv;
    Cursor cursor;
    String email;
    ReminderAdapter ra;
    Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder__for__particular__case);
        activity=this;
        getEmail();
        imageView=findViewById(R.id.img1);
        nodata=findViewById(R.id.nodata);
        lv=findViewById(R.id.reminder_listview);
        dbHelper=new DatabaseHelper(this);
        db=dbHelper.getReadableDatabase();
        final ArrayList<ReminderData> rd=new ArrayList<ReminderData>();




        String[] columns={DatabaseContract.CASEREMINDER._ID,DatabaseContract.CASEREMINDER.Col_Title,
                DatabaseContract.CASEREMINDER.Col_Client,DatabaseContract.CASEREMINDER.Col_Date,DatabaseContract.CASEREMINDER.Col_Time,
                DatabaseContract.CASE.Col_Email
        };


        cursor = db.query(DatabaseContract.CASEREMINDER.TABLE_NAME, columns,  DatabaseContract.CASEREMINDER.Col_Email + "=?" , new String[]{email}, null, null, null, null);

        if(cursor.getCount()==0)
        {
            db.close();
            imageView.setVisibility(View.VISIBLE);
            nodata.setVisibility(View.VISIBLE);

        }
        else {

            while (cursor.moveToNext()) {

                String id = String.valueOf(cursor.getInt(0));
                String t = cursor.getString(1);
                String client = cursor.getString(2);
                String d = cursor.getString(3);
                String time = cursor.getString(4);

             ReminderData Obj = new ReminderData(id, t, client, getDay(d),getMonth(d),getTime(time));
                rd.add(Obj);
            }
            cursor.close();
            db.close();


            ra = new ReminderAdapter(activity, rd);
            lv.setAdapter(ra);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent=new Intent(Reminder_For_Particular_Case.this,Edit_Reminder.class);
                    intent.putExtra("id",rd.get(position).getId());
                    startActivity(intent);
                    finish();
                }
            });
        }


    }




private String getDay(String s)

{
    String day;
    String arr[]=s.split("/");
    day=arr[0].toString();
   int d=Integer.parseInt(arr[0].toString());

if(d<10)
{
    day="0"+day;
}

    return(day);
}

    private String  getMonth(String s)
    {

        String arr[]=s.split("/");
        String m=arr[1];
        //Toast.makeText(this,  m, Toast.LENGTH_SHORT).show();

        if(m.equals("1")) {
            return "Jan";
        }
        else if(m.equals("2")) {

                return "Feb";

        }
        else if(m.equals("3")) {

            return "Mar";

        }
        else if(m.equals("4")) {

            return "Apr";

        }
        else if(m.equals("5")) {

            return "May";

        }
        else if(m.equals("6")) {

            return "Jun";

        }
        else if(m.equals("7")) {

            return "Jul";

        }
        else if(m.equals("8")) {

            return "Aug";

        }
        else if(m.equals("9")) {

            return "Sep";

        }
        else if(m.equals("10")) {

            return "Oct";

        }
        else if(m.equals("11")) {

            return "Nov";

        }
        else {

            return "Dec";

        }
    }

    public String getTime(String s)
    {
 String mTime;
        String arr[]=s.split(":");
        int hourOfDay=Integer.parseInt(arr[0]);
        int minute=Integer.parseInt(arr[1]);

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
        return mTime;

    }

    public void addReminder(View v)
    {
        Intent intent = new Intent(getApplicationContext(), Add_Reminder.class);
        startActivity(intent);
        finish();
    }


            private  void getEmail()

            {

                SharedPreferences getShared=getSharedPreferences("userInfo",MODE_PRIVATE);
                email=getShared.getString("email","Email");
            }


    public void help(View v)
    {

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Reminder_For_Particular_Case.this);
        View itemView = getLayoutInflater().inflate(R.layout.help_dialog,null);

        builder.setView(itemView);
        TextView text= itemView.findViewById(R.id.text);
        SpannableStringBuilder ssb=new SpannableStringBuilder
                ("** Scroll vertically to view all reminders." +
                        "\n\n" +
                        "** Tap on any reminder to view and edit details of that reminder.\n\n** Press    "
                );
        ssb.setSpan(new ImageSpan(this,R.drawable.black_add_reminder),ssb.length()-1,ssb.length(), 0);
        ssb.append("  button to set new reminder.");
        text.setText(ssb);
        //text.setText(ss);
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


    public void main(View v)
    {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
    public void onBackPressed()
    {

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();

    }

}