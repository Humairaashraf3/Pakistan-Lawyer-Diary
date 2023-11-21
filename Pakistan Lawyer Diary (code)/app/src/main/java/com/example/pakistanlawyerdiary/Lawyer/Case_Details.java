package com.example.pakistanlawyerdiary.Lawyer;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pakistanlawyerdiary.Database.DatabaseContract;
import com.example.pakistanlawyerdiary.Database.DatabaseHelper;
import com.example.pakistanlawyerdiary.Lawyer.manager.NetworkManager;
import com.example.pakistanlawyerdiary.Model.HistoryData;
import com.example.pakistanlawyerdiary.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

public class Case_Details extends AppCompatActivity
{

    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    SQLiteDatabase db1;
    Cursor cursor;
    String  mDate, mTime;
    FirebaseDatabase firebasedatabase;
    DatabaseReference reference;
    NetworkManager networkManager = new NetworkManager();
    String Id;
    private Calendar calendar;
    private int year,month ,day;
    private TextView dateView;
    FirebaseAuth fauth;
    String uid;
    String casenum;
    TextView   casetId, casetitle,courtname,casetype,casenumber, caseyear,onbehalfof,partyname,contactnum,
            partyadvocate,advocatecontactnum, respondantname,undersection,adjorrndate,previousdate,step,
     meetingDate, meetingTime;
    String email;
BottomNavigationView bottomNavigationView;
String t;
String dt;
String newPreviousDate="";
    TextView HT;
    String isLive="";
    String EditKey="";
    String []splitedtime;
    String hours,mins,SAMPM;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case__details);
        getEmail();
        HT = findViewById(R.id.htime);
        Intent i = getIntent();
        Id= i.getStringExtra("id");
            t = i.getStringExtra("t");
            if (t.equals("daycases"))

            {
                dt = i.getStringExtra("date");
        }
        //Toast.makeText(this, t+d, Toast.LENGTH_SHORT).show();

        bottomNavigationView=findViewById(R.id.bottom_navigation);
        casetId =(TextView)findViewById(R.id.id) ;
                casetitle =(TextView)findViewById(R.id.ct) ;
                courtname=(TextView)findViewById(R.id.cn) ;
                casetype=(TextView)findViewById(R.id.ctype) ;
                casenumber=(TextView)findViewById(R.id.cnum) ;
                caseyear=(TextView)findViewById(R.id.cyear) ;
                onbehalfof=(TextView)findViewById(R.id.obo) ;
                partyname=(TextView)findViewById(R.id.pn) ;
                contactnum=(TextView)findViewById(R.id.pnum) ;
                partyadvocate=(TextView)findViewById(R.id.adverse) ;
                advocatecontactnum=(TextView)findViewById(R.id.adverse_adv_no) ;
                respondantname=(TextView)findViewById(R.id.respo) ;
                undersection=(TextView)findViewById(R.id.usec) ;
                previousdate =(TextView)findViewById(R.id.p_date) ;
                adjorrndate=(TextView)findViewById(R.id.adj_date) ;
                step=(TextView)findViewById(R.id.steps);
                meetingDate = findViewById(R.id.meetingDate);
                meetingTime = findViewById(R.id.meetingTime);
                dbHelper = new DatabaseHelper(this);
                db=dbHelper.getReadableDatabase();
                db1=dbHelper.getReadableDatabase();



        String[] columns = {DatabaseContract.CASE._ID, DatabaseContract.CASE.Col_CaseTitle,
                DatabaseContract.CASE.Col_CourtName, DatabaseContract.CASE.Col_CaseType, DatabaseContract.CASE.Col_CaseNumber,
                DatabaseContract.CASE.Col_CaseYear, DatabaseContract.CASE.Col_OnBehalfOf, DatabaseContract.CASE.Col_PartyName,
                DatabaseContract.CASE.Col_ContactNumber, DatabaseContract.CASE.Col_PartyAdvocateName,
                DatabaseContract.CASE.Col_AdvocateContactNumber,
                DatabaseContract.CASE.Col_RespondantName, DatabaseContract.CASE.Col_FiledUnderSection,
                DatabaseContract.CASE.Col_Email,
                DatabaseContract.CASE.Col_MeetingDate,
                DatabaseContract.CASE.Col_MeetingTime,DatabaseContract.CASE.Col_isLive,DatabaseContract.CASE.Col_EditKey
        };

        cursor = db.query(DatabaseContract.CASE.TABLE_NAME, columns,  DatabaseContract.CASE._ID + "=?" ,
                new String[]{String.valueOf(Id)}, null, null, null, null);

            cursor.moveToNext();
                String pd="";
                String ad="";
                String s="";
               String ti = "";// hearing time

                int id=cursor.getInt(0);
                String t=cursor.getString(1);
                String cn= cursor.getString(2);
                String ct = cursor.getString(3);
                String caseno = cursor.getString(4);
                casenum=caseno;
                String cyear = cursor.getString(5);
                String  obo= cursor.getString(6);
                String  pn= cursor.getString(7);
                String  pnum= cursor.getString(8);
                String  adverse= cursor.getString(9);
                String  adverseno= cursor.getString(10);
                String  respo= cursor.getString(11);
                String  usec= cursor.getString(12);
                email = cursor.getString(13);
                mDate = cursor.getString(14);
                mTime = cursor.getString(15);
        isLive=cursor.getString(16);
        if(isLive.equals("1"))
        {
            EditKey = cursor.getString(17);
        }
        String query="SELECT * FROM casehistory WHERE casehistory.CaseId = ? AND casehistory.LawyerEmail =?";
        db1 = dbHelper.getReadableDatabase();
        Cursor historycursor=db1.rawQuery(query,new String[]{casenum,email});

            if(historycursor.getCount()>0)
            {
            historycursor.moveToLast();
                        pd = historycursor.getString(2);
                        ad = historycursor.getString(3);
                        newPreviousDate=ad;
                        s = historycursor.getString(4);
                        ti=historycursor.getString(5);

            }

        casetId.setText("  " + String.valueOf(id));
        casetitle.setText("  " + t);
        courtname.setText("  " + cn);
        casetype.setText("  " + ct);
        casenumber.setText("  " + caseno);
        caseyear.setText("  " + cyear);
        onbehalfof.setText("  " + obo);
        partyname.setText("  " + pn);
        contactnum.setText(pnum);
        partyadvocate.setText(adverse);
        if(!adverseno.equals(""))
        {
            advocatecontactnum.setVisibility(View.VISIBLE);
            advocatecontactnum.setText(adverseno);

        }
        respondantname.setText("  " + respo);
        undersection.setText("  " + usec);
        previousdate.setText(" " + pd);
        adjorrndate.setText("  " + ad);
        step.setText("  " + s);
        if(!ti.isEmpty())
        {
            splitedtime = ti.split(":");
            hours = splitedtime[0];
            mins = splitedtime[1].trim();
            SAMPM = splitedtime[2];
            HT.setText(" " + hours + ":" + mins + " " + SAMPM);
        }
        else
        {
            HT.setText(ti);
        }

        meetingDate.setText(" " + mDate);
        if(!mTime.equals("")) {
            splitedtime = mTime.split(":");
            hours = splitedtime[0];
            mins = splitedtime[1].trim();
            SAMPM = splitedtime[2];
            meetingTime.setText("  " + hours + ":" + mins + " " + SAMPM);
        }
        else
        {
            meetingTime.setText("");

        }


     //   c1.close();


        db1.close();
        db.close();
        historycursor.close();
        cursor.close();



            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Intent intent;
                    switch (item.getItemId())
                    {
                        case R.id.client_reminder:
                            sendReminder();
                            break;
                        case R.id.add_step:
                            addStep();
                            break;
                        case R.id.history:
                            intent=new Intent(Case_Details.this, Case_History.class);
                            intent.putExtra("id", Id);
                            intent.putExtra("casenumber", casenum);
                            startActivity(intent);
                            break;
                        case R.id.dispose:
                            disposeCase();
                            break;
                    }
                    return false;
                }
            });

    }



    public void callParty(View v)
    {
        Intent intent=new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+contactnum.getText().toString()));
        startActivity(intent);

    }
    public void callAdvocate(View v)
    {
        Intent intent=new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+advocatecontactnum.getText().toString()));
        startActivity(intent);


    }
    public void addStep()
    {
        Intent i;
        if(t.equals("all"))
        {
            i = new Intent(this, Add_Step.class);
            i.putExtra("previous", newPreviousDate);
            i.putExtra("id", Id);
            i.putExtra("t", t);
            i.putExtra("CASE_NUMBER", casenumber.getText().toString());
            startActivity(i);
            finish();
        }

        else if(t.equals("daycases"))
        {
            i = new Intent(this, Add_Step.class);
            i.putExtra("previous", adjorrndate.getText().toString());
            i.putExtra("id", Id);
            i.putExtra("t", t);
            i.putExtra("date", dt);
            i.putExtra("CASE_NUMBER", casenumber.getText().toString());
            startActivity(i);
            finish();

        }
        else
        {
            i = new Intent(this, Add_Step.class);
            i.putExtra("previous", adjorrndate.getText().toString());
            i.putExtra("id", Id);
            i.putExtra("t", t);
            i.putExtra("CASE_NUMBER", casenumber.getText().toString());
            startActivity(i);
            finish();
        }

    }



    public void editCase(View v)
    {
        Intent i;
        if(t.equals("all"))
        {
            i = new Intent(this, Edit_Case.class);
            i.putExtra("id", Id);
            i.putExtra("t", t);
            startActivity(i);
            finish();
        }

        else if(t.equals("daycases"))
        {
            i = new Intent(this, Edit_Case.class);
            i.putExtra("id", Id);
            i.putExtra("t", t);
            i.putExtra("date", dt);
            startActivity(i);
            finish();

        }
        else
        {
            i = new Intent(this, Edit_Case.class);
            i.putExtra("id", Id);
            i.putExtra("t", t);
            startActivity(i);
            finish();
        }



    }




    public void sendReminder()

    {
       String cn=casenumber.getText().toString();
       final String contact=contactnum.getText().toString();
        final String pname=partyname.getText().toString();
        final String adjdate=adjorrndate.getText().toString();
        final String num="tel:"+contact;

        final Dialog dialog = new Dialog(Case_Details.this);
        dialog.setContentView(R.layout.client_reminder_alert);
        final Button call=dialog.findViewById(R.id.call);
        Button sms=dialog.findViewById(R.id.sms);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                call(num);
                dialog.dismiss();


            }
        });
        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sms(contact,pname,adjdate);
                dialog.dismiss();

            }
        });
        dialog.show();


    }


    public void call(String cn)
    {

        String url= cn;
        Intent i=new Intent(Intent.ACTION_DIAL, Uri.parse(url));
        startActivity(i);

    }
    public void sms(String contact ,String pname,String adjdate)
    {
        String url= "smsto:"+contact;
        Uri sms_uri = Uri.parse(url);
        Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri);
        String sms_body="Reminder:\nMr/Mrs "+pname+" YOUR CASE ADJOURN DATE IS SCHEDULED ON "+adjdate+" PLEASE MAKE SURE TO ATTEND AT COURT. INFORM TO ME YOUR AVAILABILITY STATUS SOON.\nKindly Regards,";
        sms_intent.putExtra("sms_body", sms_body);
        startActivity(sms_intent);
    }



    public void share(View v)
    {
        Intent intent = new Intent(getApplicationContext(), Share_Case.class);
        String sms_body="Case Details:\nCase No: "+casenumber.getText().toString()+
                "\nCase Type: "+casetype.getText().toString()+
                "\nParty Name:"+partyname.getText().toString()+
                "\nOn Behalf Of: "+onbehalfof.getText().toString()+
                "\nParty Contact Number: "+contactnum.getText().toString()+
                "\nAdjourn Date: "+adjorrndate.getText().toString()+
                "\nCourt Name: "+courtname.getText().toString()+
                "\nRespondent Name: "+respondantname.getText().toString()+
                "\nAdverse Party Advocate Name: "+partyadvocate.getText().toString()+
                "\nAdverse Party Advocate Number: "+advocatecontactnum.getText().toString()+
                "\nField Under Section : "+undersection.getText().toString()+
                "\nMeeting Date With Client Is: " + meetingDate.getText().toString() +
                "\nMeeting Time Is : " + meetingTime.getText().toString();
        intent.putExtra("casedetails", sms_body);
        startActivity(intent);
    }


    public void disposeCase()

    {

        calendar= Calendar.getInstance();
        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH);
        day=calendar.get(Calendar.DAY_OF_MONTH);
        final Dialog dialog = new Dialog(Case_Details.this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dispose_case_input);
        final EditText step=dialog.findViewById(R.id.ed1);
        final EditText adj=dialog.findViewById(R.id.ed2);
        Button ok=dialog.findViewById(R.id.ok);
        Button cancel=dialog.findViewById(R.id.cancel);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = step.getText().toString();
                String a = adj.getText().toString();
                if (s.isEmpty()|| a.isEmpty())
                {
                    final Dialog dialog = new Dialog(Case_Details.this);
                    dialog.setContentView(R.layout.custom_alert);
                    dialog.setCancelable(false);
                    TextView alertt=dialog.findViewById(R.id.alerttext);
                    alertt.setText("please enter all details");
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

                    if (networkManager.checkInternetConnection(Case_Details.this))
                    {

                        db = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put(DatabaseContract.CASEHISTORY.Col_CaseId, casenum);
                        values.put(DatabaseContract.CASEHISTORY.Col_Previousdate, newPreviousDate);
                        values.put(DatabaseContract.CASEHISTORY.Col_Adjourndate, a);
                        values.put(DatabaseContract.CASEHISTORY.Col_Step, s);
                        values.put(DatabaseContract.CASEHISTORY.Col_HearingTime, "");
                        values.put(DatabaseContract.CASEHISTORY.Col_LawyerEmail, email);
                        values.put(DatabaseContract.CASEHISTORY.Col_isLive, "1");

                        long newRowId = db.insert(DatabaseContract.CASEHISTORY.TABLE_NAME, null, values);


                        if (newRowId > 0)
                        {
                            values.clear();
                            values.put(DatabaseContract.CASE.Col_Status, "dispose");
                            Integer i = db.update(DatabaseContract.CASE.TABLE_NAME, values,
                                    DatabaseContract.CASE._ID + "=?", new String[]{Id});
                            if (i > 0) {

                                uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                reference = FirebaseDatabase.getInstance().getReference("CaseHistory");
                                HistoryData ch = new HistoryData(casenum, newPreviousDate, a, s, "", uid, email);
                                reference.push().setValue(ch);

                                // update case status on server
                                if (isLive.equals("1"))
                                {
                                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Case").child(EditKey);
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("status", "dispose");
                                    reference1.updateChildren(hashMap);

                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put(DatabaseContract.CASE.Col_isEditLive, "1");
                                    db.update(DatabaseContract.CASE.TABLE_NAME, contentValues,
                                            DatabaseContract.CASE._ID + "=?", new String[]{Id});

                                }
                                db.close();
                                dialog.dismiss();
                                final Dialog dialog = new Dialog(Case_Details.this);
                                dialog.setContentView(R.layout.custom_alert);
                                TextView alertt = dialog.findViewById(R.id.alerttext);
                                alertt.setText("Disposed data has been saved");
                                Button ok = dialog.findViewById(R.id.ok);
                                ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intnt = new Intent(getApplicationContext(), Disposed_Case_Details.class);
                                        intnt.putExtra("id", Id);
                                        startActivity(intnt);
                                        finish();
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();


                            }

                        }

                        else {
                            db.close();
                            //Toast.makeText(getApplicationContext(), "Dispose Data not saved: " + newRowId, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }


                    }


                    else
                    {
                       // String d = adjorrndate.getText().toString();

                        db = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put(DatabaseContract.CASEHISTORY.Col_CaseId, casenum);
                        values.put(DatabaseContract.CASEHISTORY.Col_Previousdate, newPreviousDate);
                        values.put(DatabaseContract.CASEHISTORY.Col_Adjourndate, a);
                        values.put(DatabaseContract.CASEHISTORY.Col_Step, s);
                        values.put(DatabaseContract.CASEHISTORY.Col_HearingTime, "");
                        values.put(DatabaseContract.CASEHISTORY.Col_LawyerEmail, email);
                        values.put(DatabaseContract.CASEHISTORY.Col_isLive, "0");


                        long newRowId = db.insert(DatabaseContract.CASEHISTORY.TABLE_NAME, null, values);


                        if (newRowId > 0)
                        {

                            values.clear();
                            values.put(DatabaseContract.CASE.Col_Status, "dispose");
                            Integer i = db.update(DatabaseContract.CASE.TABLE_NAME, values,
                                    DatabaseContract.CASE._ID + "=?", new String[]{Id});
                            if (i > 0)
                            {

                                if(isLive.equals("1"))
                                {
                                    values.clear();
                                    values.put(DatabaseContract.CASE.Col_isEditLive, "0");
                                     db.update(DatabaseContract.CASE.TABLE_NAME, values,
                                            DatabaseContract.CASE._ID + "=?", new String[]{Id});
                                }
                                db.close();
                                dialog.dismiss();
                                final Dialog dialog = new Dialog(Case_Details.this);
                                dialog.setContentView(R.layout.custom_alert);
                                TextView alertt = dialog.findViewById(R.id.alerttext);
                                alertt.setText("Disposed data has been saved");
                                Button ok = dialog.findViewById(R.id.ok);
                                ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intnt = new Intent(getApplicationContext(), Disposed_Case_Details.class);
                                        intnt.putExtra("id", Id);
                                        startActivity(intnt);
                                        finish();
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();

                            }


                        }
                        else {
                            db.close();
                            //Toast.makeText(getApplicationContext(), "Dispose Data not saved: " + newRowId, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                }
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();



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
                    dateView.setText(new StringBuilder().append(arg3).append("-").append(arg2+1).append("-").append(arg1));
                    // showDate(arg1, arg2+1, arg3);
                }
            };






    private  void getEmail()

    {

        SharedPreferences getShared=getSharedPreferences("userInfo",MODE_PRIVATE);
        email=getShared.getString("email","Email");
    }

    public void help(View v)


    {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Case_Details.this);
        View itemView = getLayoutInflater().inflate(R.layout.help_dialog,null);

        builder.setView(itemView);
        TextView text= itemView.findViewById(R.id.text);
        SpannableStringBuilder ssb=new SpannableStringBuilder
                ("** Press  ");
        ssb.setSpan(new ImageSpan(this,R.drawable.black_edit_icon),ssb.length()-1,ssb.length(), 0);
        ssb.append(" icon to edit case details.\n\n** Press  ");
        ssb.setSpan(new ImageSpan(this,R.drawable.black_share_icon),ssb.length()-1,ssb.length(), 0);
        ssb.append(" icon to share case details.\n\n** Press  ");
        ssb.setSpan(new ImageSpan(this,R.drawable.black_add_icon),ssb.length()-1,ssb.length(), 0);
        ssb.append(" icon to enter new step details.\n\n** Press  ");
        ssb.setSpan(new ImageSpan(this,R.drawable.black_history_icon),ssb.length()-1,ssb.length(), 0);
        ssb.append(" icon to view case history.\n\n** Press  ");
        ssb.setSpan(new ImageSpan(this,R.drawable.black_bell),ssb.length()-1,ssb.length(), 0);
        ssb.append(" icon to remind client about hearing through call or sms.\n\n** Press  ");
        ssb.setSpan(new ImageSpan(this,R.drawable.black_disposal),ssb.length()-1,ssb.length(), 0);
        ssb.append(" icon to dispose case.");
        //ssb.setSpan(new ImageSpan(this,R.drawable.black_reopen),ssb.length()-1,ssb.length(), 0);

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

    public void onBackPressed()
    {
        Intent i;
        if(t.equals("all")) {
            i = new Intent(this, All_Cases.class);
            startActivity(i);
            finish();
        }

        else if(t.equals("daycases"))
        {
            i = new Intent(this, Schedule.class);
            i.putExtra("date",dt);
            startActivity(i);
            finish();

        }
        else
        {
            i = new Intent(this, Adjourn_Cases.class);
            startActivity(i);
            finish();
        }
    }

    public void back(View v )
    {

        Intent i;
        if(t.equals("all")) {
            i = new Intent(this, All_Cases.class);
            startActivity(i);
            finish();
        }
        else if(t.equals("daycases"))
        {
            i = new Intent(this, Schedule.class);
            i.putExtra("date",dt);
            startActivity(i);
            finish();

        }
        else
        {
            i = new Intent(this, Adjourn_Cases.class);
            startActivity(i);
            finish();
        }


    }

}