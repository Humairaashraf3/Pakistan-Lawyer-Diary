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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pakistanlawyerdiary.Database.DatabaseContract;
import com.example.pakistanlawyerdiary.Database.DatabaseHelper;
import com.example.pakistanlawyerdiary.Lawyer.manager.NetworkManager;
import com.example.pakistanlawyerdiary.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Edit_Case extends AppCompatActivity {


    String mDate;
    String mTime;
    String uid;
    DatabaseReference reference;
    private int mYear, mMonth, mHour, mMinute, mDay;
    EditText case_title;
    AutoCompleteTextView court_name;
    AutoCompleteTextView case_type;
    NetworkManager networkManager = new NetworkManager();
    EditText case_year;
    AutoCompleteTextView onbehalf;
    AutoCompleteTextView party_name;
    EditText contact_no;
    EditText adverse_advocate;
    EditText adverse_advocateno;
    EditText respodent;
    EditText filed_usec;
    EditText time, date;
    ImageButton remove_date;
    ImageButton remove_time;
    String title,court,type,year,behalfof,p_name,party_no,adverse,adverse_no,respo,usec;
    String meetingDate;
    String meetingTime;
    ArrayList<String> types_array=new ArrayList<String>();
    ArrayList<String> court_array=new ArrayList<String>();
    ArrayList<String>party_array=new ArrayList<String>();
    ArrayList<String>party_contact_array=new ArrayList<String>();
    String[] onbehalf_array;
    ImageView dd1;
    ImageView dd2;
    ImageView dd3;
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    String Id;
    String email;
    Cursor cursor;
    String d,t;
    String isLive="";
    String EditKey="";
    String []splitedtime;
    String hours,mins,SAMPM;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__case);
        getEmail();
        date=findViewById(R.id.date);
        time=findViewById(R.id.time);
        Intent i = getIntent();
        Id= i.getStringExtra("id");
        t = i.getStringExtra("t");
        if (t.equals("daycases"))

        {
            d = i.getStringExtra("date");
        }
        getEmail();
        case_title=(EditText)findViewById(R.id.case_title);
        court_name=(AutoCompleteTextView)findViewById(R.id.court_name);
        case_type=(AutoCompleteTextView)findViewById(R.id.case_type);
        case_year=(EditText) findViewById(R.id.case_year);
        onbehalf=(AutoCompleteTextView)findViewById(R.id.on_behalf);
        party_name=(AutoCompleteTextView)findViewById(R.id.party_name);
        contact_no=(EditText)findViewById(R.id.contact_no);
        adverse_advocate=(EditText)findViewById(R.id.adverse_adv);
        adverse_advocateno=(EditText)findViewById(R.id.adverse_adv_no);
        respodent=(EditText)findViewById(R.id.respodent);
        filed_usec=(EditText)findViewById(R.id.filed_u);
        remove_date=findViewById(R.id.remove_date);
        remove_time=findViewById(R.id.remove_time);
        onbehalf_array=getResources().getStringArray(R.array.onbehalfof);
        dbHelper=new DatabaseHelper(this);
        dd1=(ImageView)findViewById(R.id.dd1);
        dd2=(ImageView)findViewById(R.id.dd2);
        dd3=(ImageView)findViewById(R.id.dd3);
        initializeTypes();
        initializeCourts();
        initializeParty();




        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, types_array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        case_type.setAdapter(adapter);

        dd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (types_array.size() < 1)
                {

                    final Dialog dialog = new Dialog(Edit_Case.this);
                    dialog.setContentView(R.layout.custom_alert);
                    TextView alertt=dialog.findViewById(R.id.alerttext);
                    alertt.setText("Case type data is not available please enter new Case type in Settings");
                    Button ok=dialog.findViewById(R.id.ok);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();

                } else {

                    case_type.showDropDown();

                }
            }
        });


        // Adapter on CourtNames

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, court_array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        court_name.setAdapter(adapter1);
        dd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (court_array.size() < 1) {
                    final Dialog dialog = new Dialog(Edit_Case.this);
                    dialog.setContentView(R.layout.custom_alert);
                    TextView alertt=dialog.findViewById(R.id.alerttext);
                    alertt.setText("Courts data is not available please enter new court in Settings");
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
                    court_name.showDropDown();

                }
            }
        });






        // Adapter on On Behalf of

        ArrayAdapter<String> adapter2=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,onbehalf_array);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        onbehalf.setAdapter(adapter2);

        onbehalf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onbehalf.showDropDown();

            }
        });



        // Adapter on party Names

        ArrayAdapter<String> adapter4 =new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,party_array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        party_name.setAdapter(adapter4);
        dd3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (party_array.size() < 1)
                {

                    final Dialog dialog = new Dialog(Edit_Case.this);
                    dialog.setContentView(R.layout.custom_alert);
                    dialog.setCancelable(false);
                    TextView alertt=dialog.findViewById(R.id.alerttext);
                    alertt.setText("Parties data is not available please enter new party in Settings");
                    Button ok=dialog.findViewById(R.id.ok);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();

                } else {
                    party_name.showDropDown();

                }
            }
        });
        party_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                contact_no.setText(String.valueOf(party_contact_array.get(position)));
            }
        });


        // Read Data From Database
        db=dbHelper.getReadableDatabase();

        String[] columns={DatabaseContract.CASE._ID,DatabaseContract.CASE.Col_CaseTitle,
                DatabaseContract.CASE.Col_CourtName,DatabaseContract.CASE.Col_CaseType,
                DatabaseContract.CASE.Col_CaseYear, DatabaseContract.CASE.Col_OnBehalfOf, DatabaseContract.CASE.Col_PartyName,
                DatabaseContract.CASE.Col_ContactNumber, DatabaseContract.CASE.Col_PartyAdvocateName, DatabaseContract.CASE.Col_AdvocateContactNumber,
                DatabaseContract.CASE.Col_RespondantName, DatabaseContract.CASE.Col_FiledUnderSection,
                DatabaseContract.CASE.Col_MeetingDate,
                DatabaseContract.CASE.Col_MeetingTime,DatabaseContract.CASE.Col_isLive,DatabaseContract.CASE.Col_EditKey
        };
        cursor = db.query(DatabaseContract.CASE.TABLE_NAME, columns,  DatabaseContract.CASE._ID + "=?" , new String[]{String.valueOf(Id)}, null, null, null, null);

        cursor.moveToNext();
        String t=cursor.getString(1);
        String cn= cursor.getString(2);
        String ct = cursor.getString(3);
        String cyear = cursor.getString(4);
        String  obo= cursor.getString(5);
        String  pn= cursor.getString(6);
        String  pnum= cursor.getString(7);
        String  adverse= cursor.getString(8);
        String  adverseno= cursor.getString(9);
        String  respo= cursor.getString(10);
        String  usec= cursor.getString(11);
        String da= cursor.getString(12);
        meetingDate=da;
        String ti= cursor.getString(13);
        meetingTime=ti;
        isLive=cursor.getString(14);
        if(isLive.equals("1"))
        {
            EditKey = cursor.getString(15);
        }
      //  Toast.makeText(getApplicationContext(),EditKey,Toast.LENGTH_SHORT).show();
        case_title.setText(t) ;
        court_name.setText(cn) ;
        case_type.setText(ct) ;
        case_year.setText(cyear) ;
        onbehalf.setText(obo) ;
        party_name.setText(pn) ;
        contact_no.setText(pnum) ;
        adverse_advocate.setText(adverse) ;
        adverse_advocateno.setText(adverseno) ;
        respodent.setText(respo) ;
        filed_usec.setText(usec) ;
        date.setText(da);
if(!ti.equals("")) {
    splitedtime = ti.split(":");
    hours = splitedtime[0];
    mins = splitedtime[1].trim();
    SAMPM = splitedtime[2];
    time.setText(hours + ":" + mins + " " + SAMPM);
}
else
{
    time.setText("");
}

if(!meetingDate.isEmpty())
{
    remove_date.setVisibility(View.VISIBLE);
}
if(!meetingTime.isEmpty())
{
    remove_time.setVisibility(View.VISIBLE);
}

        // listener on remove date button
        remove_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                date.setText(null);
                meetingDate="";
                remove_date.setVisibility(View.GONE);
            }
        });

        // listener on remove time button
        remove_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                time.setText(null);
                meetingTime="";
                remove_time.setVisibility(View.GONE);
            }
        });


    }





    public void initializeTypes()
    {


        db = dbHelper.getReadableDatabase();
        String[] columns = {DatabaseContract.CASETYPES._ID,  DatabaseContract.CASETYPES.Col_Email,DatabaseContract.CASETYPES.Col_CaseType};
        Cursor c = db.query(DatabaseContract.CASETYPES.TABLE_NAME, columns, DatabaseContract.CASETYPES.Col_Email +"=?",new String[] {email}, null, null, null);



        while (c.moveToNext()) {

            String t = c.getString(2);
            types_array.add(t);


        }
        c.close();
        db.close();

    }



    public void initializeCourts()

    {

        db = dbHelper.getReadableDatabase();
        String[] columns = {DatabaseContract.COURTNAMES._ID, DatabaseContract.COURTNAMES.Col_Email, DatabaseContract.COURTNAMES.Col_CourtName};
        Cursor c = db.query(DatabaseContract.COURTNAMES.TABLE_NAME, columns, DatabaseContract.COURTNAMES.Col_Email +"=?",new String[] {email}, null, null, null);


        while (c.moveToNext()) {

            String cort = c.getString(2);
            court_array.add(cort);




        }
        c.close();
        db.close();
    }

    public void initializeParty()
    {

        db = dbHelper.getReadableDatabase();
        String[] columns = {DatabaseContract.CLIENT._ID, DatabaseContract.CLIENT.Col_Name,DatabaseContract.CLIENT.Col_Phone, DatabaseContract.CLIENT.Col_LawyerEmail};
        Cursor c = db.query(DatabaseContract.CLIENT.TABLE_NAME, columns, DatabaseContract.CLIENT.Col_LawyerEmail +"=?",new String[] {email}, null, null, null);


        while (c.moveToNext()) {

            String client = c.getString(1);
            String phn = c.getString(2);
            party_array.add(client);
            party_contact_array.add(phn);



        }
        c.close();
        db.close();



    }


    public void resetValues()
    {
        case_title.setText("");
        court_name.setText("");
        case_type.setText("");
        case_year.setText("");
        onbehalf.setText("");
        party_name.setText("");
        contact_no.setText("");
        adverse_advocate.setText("");
        adverse_advocateno.setText("");
        respodent.setText("");
        filed_usec.setText("");
//di.isChecked()=false;
        date.setText("");
        time.setText("");


    }


    public void updateCase(View v)
    {

        try {


            db = dbHelper.getWritableDatabase();

            // Toast.makeText(getApplicationContext(), "inside update", Toast.LENGTH_SHORT).show();
            ContentValues values = new ContentValues();
            title = case_title.getText().toString();
            court = court_name.getText().toString();
            type = case_type.getText().toString();
            year = case_year.getText().toString();
            behalfof = onbehalf.getText().toString();
            p_name = party_name.getText().toString();
            party_no = contact_no.getText().toString();
            if(adverse_advocate.getText().toString().isEmpty())
            {
                adverse="";
            }

            else {
                adverse = adverse_advocate.getText().toString();
            }

            if(adverse_advocateno.getText().toString().isEmpty())
            {
                adverse_no="";
            }
            else
            {
                adverse_no = adverse_advocateno.getText().toString();
            }
            if(respodent.getText().toString().isEmpty())
            {
                respo="";
            }
            else {
                respo = respodent.getText().toString();
            }
            if(filed_usec.getText().toString().isEmpty())
            {
                usec="";
            }
            else {
                usec = filed_usec.getText().toString();
            }
            //meetingDate = date.getText().toString();
            //meetingTime = time.getText().toString();
            if (title.isEmpty() || court.isEmpty() || type.isEmpty() || year.isEmpty() || behalfof.isEmpty() || p_name.isEmpty() || party_no.isEmpty()
                    ) {

                showDialog("Please enter mendatory details");

                return;
            }
            if(!meetingTime.equals(""))
            {
            if (istimePassed()) {

                // Toast.makeText(getApplicationContext(), String.valueOf(isReser()), Toast.LENGTH_SHORT).show();
                showDialog("This time has been passed plaese select valid time ");
                return;
            }
            }

            String isreser=isReser();
            if(!meetingDate.equals("")&&!meetingTime.equals("")) {
                if (isreser.equals("1")) {

                    // Toast.makeText(getApplicationContext(), String.valueOf(isReser()), Toast.LENGTH_SHORT).show();
                    showDialog("This Date and Time is already reserved ");
                    return;

                }
            }
            db=dbHelper.getWritableDatabase();

            if (networkManager.checkInternetConnection(Edit_Case.this)) {

                values.put(DatabaseContract.CASE.Col_CaseTitle, title);
                values.put(DatabaseContract.CASE.Col_CourtName, court);
                values.put(DatabaseContract.CASE.Col_CaseType, type);
                values.put(DatabaseContract.CASE.Col_CaseYear, year);
                values.put(DatabaseContract.CASE.Col_OnBehalfOf, behalfof);
                values.put(DatabaseContract.CASE.Col_PartyName, p_name);
                values.put(DatabaseContract.CASE.Col_ContactNumber, party_no);
                values.put(DatabaseContract.CASE.Col_PartyAdvocateName, adverse);
                values.put(DatabaseContract.CASE.Col_AdvocateContactNumber, adverse_no);
                values.put(DatabaseContract.CASE.Col_RespondantName, respo);
                values.put(DatabaseContract.CASE.Col_FiledUnderSection, usec);
                values.put(DatabaseContract.CASE.Col_Status, "open");
                values.put(DatabaseContract.CASE.Col_Email, email);
                values.put(DatabaseContract.CASE.Col_MeetingDate, meetingDate);
                values.put(DatabaseContract.CASE.Col_MeetingTime, meetingTime);
                long newRowId = db.update(DatabaseContract.CASE.TABLE_NAME, values,
                        DatabaseContract.CASE._ID + "=?", new String[]{String.valueOf(Id)});
                if (newRowId > 0) {

                    if (isLive.equals("1")) {
                        values.clear();
                        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        reference = FirebaseDatabase.getInstance().getReference("Case").child(EditKey);
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("casetitle", title);
                        hashMap.put("courtname", court);
                        hashMap.put("casetype", type);
                        hashMap.put("caseYear", year);
                        hashMap.put("onbehalf", behalfof);
                        hashMap.put("party", p_name);
                        hashMap.put("partyContact", party_no);
                        hashMap.put("advocateName", adverse);
                        hashMap.put("advocateNum", adverse_no);
                        hashMap.put("respondentName", respo);
                        hashMap.put("fieldUnSection", usec);
                        hashMap.put("meetingDate", meetingDate);
                        hashMap.put("meetingTime", meetingTime);
                        reference.updateChildren(hashMap);

                        // adding editlive value to sqlite database
                        values.put(DatabaseContract.CASE.Col_isEditLive, "1");
                        db.update(DatabaseContract.CASE.TABLE_NAME, values, DatabaseContract.CASE._ID + "=?", new String[]{Id});

                    }
                    db.close();
                    values.clear();
                    final Dialog dialog = new Dialog(Edit_Case.this);
                    dialog.setContentView(R.layout.custom_alert);
                    dialog.setCancelable(false);
                    TextView alertt = dialog.findViewById(R.id.alerttext);
                    alertt.setText("Case details has been updated successfully");
                    Button ok = dialog.findViewById(R.id.ok);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Intent i;
                            if (t.equals("all")) {
                                i = new Intent(Edit_Case.this, Case_Details.class);
                                i.putExtra("id", Id);
                                i.putExtra("t", t);
                                startActivity(i);
                                finish();
                            } else if (t.equals("daycases")) {
                                i = new Intent(Edit_Case.this, Case_Details.class);
                                i.putExtra("id", Id);
                                i.putExtra("t", t);
                                i.putExtra("date", d);
                                startActivity(i);
                                finish();

                            } else {
                                i = new Intent(Edit_Case.this, Case_Details.class);
                                i.putExtra("id", Id);
                                i.putExtra("t", t);
                                startActivity(i);
                                finish();
                            }
                        }

                    });
                    dialog.show();

                } else {

                    Toast.makeText(this, "Record Not updated: " + newRowId, Toast.LENGTH_SHORT).show();
                }
                db.close();
                values.clear();


            } else {

                values.put(DatabaseContract.CASE.Col_CaseTitle, title);
                values.put(DatabaseContract.CASE.Col_CourtName, court);
                values.put(DatabaseContract.CASE.Col_CaseType, type);
                values.put(DatabaseContract.CASE.Col_CaseYear, year);
                values.put(DatabaseContract.CASE.Col_OnBehalfOf, behalfof);
                values.put(DatabaseContract.CASE.Col_PartyName, p_name);
                values.put(DatabaseContract.CASE.Col_ContactNumber, party_no);
                values.put(DatabaseContract.CASE.Col_PartyAdvocateName, adverse);
                values.put(DatabaseContract.CASE.Col_AdvocateContactNumber, adverse_no);
                values.put(DatabaseContract.CASE.Col_RespondantName, respo);
                values.put(DatabaseContract.CASE.Col_FiledUnderSection, usec);
                values.put(DatabaseContract.CASE.Col_Status, "open");
                values.put(DatabaseContract.CASE.Col_Email, email);
                values.put(DatabaseContract.CASE.Col_MeetingDate, meetingDate);
                values.put(DatabaseContract.CASE.Col_MeetingTime, meetingTime);
                if (isLive.equals("1")) {
                    values.put(DatabaseContract.CASE.Col_isEditLive, "0");
                }
                long newRowId = db.update(DatabaseContract.CASE.TABLE_NAME,
                        values, DatabaseContract.CASE._ID + "=?", new String[]{String.valueOf(Id)});
                if (newRowId > 0) {

                    db.close();
                    values.clear();
                    final Dialog dialog = new Dialog(Edit_Case.this);
                    dialog.setContentView(R.layout.custom_alert);
                    dialog.setCancelable(false);
                    TextView alertt = dialog.findViewById(R.id.alerttext);
                    alertt.setText("Case details has been updated successfully");
                    Button ok = dialog.findViewById(R.id.ok);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Intent i;
                            if (t.equals("all")) {
                                i = new Intent(Edit_Case.this, Case_Details.class);
                                i.putExtra("id", Id);
                                i.putExtra("t", t);
                                startActivity(i);
                                finish();
                            } else if (t.equals("daycases")) {
                                i = new Intent(Edit_Case.this, Case_Details.class);
                                i.putExtra("id", Id);
                                i.putExtra("t", t);
                                i.putExtra("date", d);
                                startActivity(i);
                                finish();

                            } else {
                                i = new Intent(Edit_Case.this, Case_Details.class);
                                i.putExtra("id", Id);
                                i.putExtra("t", t);
                                startActivity(i);
                                finish();
                            }
                        }

                    });
                    dialog.show();
                } else {
                    Toast.makeText(this,
                            "Record not updated", Toast.LENGTH_SHORT).show();
                }


                db.close();
                values.clear();


            }


        }
        catch(Exception e)
        {
            Toast.makeText(Edit_Case.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }


    }







    public void showDialog( String s)
    {
        // custom dialog
        final Dialog dialog = new Dialog(Edit_Case.this);
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_alert);
        dialog.setCancelable(false);
        TextView alertt=dialog.findViewById(R.id.alerttext);
        alertt.setText(s);
        // if button is clicked, close the custom dialog
        Button ok=dialog.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                // Toast.makeText(getApplicationContext(),"Dismissed..!!",Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }



    private  void getEmail()

    {

        SharedPreferences getShared=getSharedPreferences("userInfo",MODE_PRIVATE);
        email=getShared.getString("email","Email");
    }


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
            id=String.valueOf(c.getInt(0));
            cn = c.getString(1);
            st = c.getString(2);
            md = c.getString(4);
            mt = c.getString(5);

                if (st.equals("open") && md.equals(String.valueOf(meetingDate)) && mt.equals(String.valueOf(meetingTime))) {

                    //Toast.makeText(getApplicationContext(), "inside isteserved", Toast.LENGTH_SHORT).show();
                    if(!id.equals(Id))
                    {
                        return "1";
                    }

                }
                else {


                    String query = "SELECT * FROM casehistory WHERE casehistory.CaseId = ? AND casehistory.LawyerEmail =?";
                    Cursor historycursor = db.rawQuery(query, new String[]{cn, email});
                    if (historycursor.getCount() > 0) {
                        historycursor.moveToLast();
                        ad = historycursor.getString(3);
                        ti = historycursor.getString(5);
                        if (ad.equals(meetingDate) && ti.equals(meetingTime))
                        {
                            return "1";
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
        if(meetingDate.equals(cdate)) {
            String currentTime = new SimpleDateFormat("h:mm:a", Locale.getDefault()).format(new Date());
            s_hour = meetingTime.split(":");
            //Toast.makeText(getApplicationContext(), currentTime, Toast.LENGTH_SHORT).show();
            //Toast.makeText(getApplicationContext(), meetingTime, Toast.LENGTH_SHORT).show();
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

    public void help(View v)


    {
    }

    public void back(View v )
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
            i.putExtra("date",d);
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
            i.putExtra("date",d);
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
                        meetingTime = mTime +":"+ getAMPMValue;
                        remove_time.setVisibility(View.VISIBLE);
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
                        date.setText(mDate);
                        meetingDate = mDate;
                        remove_date.setVisibility(View.VISIBLE);

                    }
                }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }
}