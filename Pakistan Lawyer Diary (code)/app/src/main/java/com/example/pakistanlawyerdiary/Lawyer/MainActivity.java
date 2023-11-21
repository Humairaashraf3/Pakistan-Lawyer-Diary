package com.example.pakistanlawyerdiary.Lawyer;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.pakistanlawyerdiary.Account.Profile;
import com.example.pakistanlawyerdiary.Account.SignIn;
import com.example.pakistanlawyerdiary.Database.DatabaseContract;
import com.example.pakistanlawyerdiary.Database.DatabaseHelper;
import com.example.pakistanlawyerdiary.Lawyer.Reminder.Reminder_For_Adjourn_Day_Cases;
import com.example.pakistanlawyerdiary.Lawyer.Reminder.Reminder_For_Particular_Case;
import com.example.pakistanlawyerdiary.Lawyer.manager.NetworkManager;
import com.example.pakistanlawyerdiary.Model.CaseData;
import com.example.pakistanlawyerdiary.Model.HistoryData;
import com.example.pakistanlawyerdiary.Model.PartyData;
import com.example.pakistanlawyerdiary.Model.TypeCourtData;
import com.example.pakistanlawyerdiary.Model.User;
import com.example.pakistanlawyerdiary.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import sun.bob.mcalendarview.MarkStyle;
import sun.bob.mcalendarview.listeners.OnDateClickListener;
import sun.bob.mcalendarview.listeners.OnExpDateClickListener;
import sun.bob.mcalendarview.listeners.OnMonthScrollListener;
import sun.bob.mcalendarview.views.ExpCalendarView;
import sun.bob.mcalendarview.vo.DateData;




public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    TextView name ,emailtxtview;
    TextView total, tomorrow, today, disposed;
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    SQLiteDatabase db1;
    Cursor cursor;
   public static String email;
    static  String fname;
    View nview;
FirebaseUser firebaseUser;
    ProgressDialog pd;
    String casenum;
    String ad;
    FirebaseDatabase firebasedatabase;
    DatabaseReference reference;
    FirebaseAuth fauth;
    NetworkManager networkManager = new NetworkManager();

    TextView current_month;
    private TextView YearMonthdayTv;
    private ExpCalendarView expCalendarView;
    private DateData selectedDate;
    ImageView forward,backword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setInfo();
        total = (TextView) findViewById(R.id.total);
        tomorrow = (TextView) findViewById(R.id.tommorrow);
        today = (TextView) findViewById(R.id.today);
        disposed = (TextView) findViewById(R.id.disposed);
        dbHelper = new DatabaseHelper(this);
        pd=new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setTitle("Logging Out");
        // setTitle("Pakistan Lawyer Diary");
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);// toggle will listen drawer
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);


        // get reference of navigation header in order to set header info
        nview = navigationView.getHeaderView(0);
        name = (TextView) nview.findViewById(R.id.full_name);
        emailtxtview = (TextView) nview.findViewById(R.id.email_address);


        // add listener on navigation view item
        navigationView.setNavigationItemSelectedListener(this);
        setUserInfo();
        today.setText("Today : " + findtoday());
        total.setText("Total : " + findtotal());
        tomorrow.setText("Tomorrow : " + findtomorrow());
        disposed.setText("Disposed: " + finddisposed());

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());



 /********************************* Calnder View********************************************************/
//      Get instance.
        current_month= (TextView) findViewById(R.id.current_month);
        forward=(ImageView) findViewById(R.id.forward);
        backword=(ImageView) findViewById(R.id.backword);
        expCalendarView = ((ExpCalendarView) findViewById(R.id.calendar_exp));
        YearMonthdayTv = (TextView) findViewById(R.id.main_YYMM_Tv);
        YearMonthdayTv.setText(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+"-"+ (Calendar.getInstance().get(Calendar.MONTH) + 1)+"-"+Calendar.getInstance().get(Calendar.YEAR));
        current_month.setText(convertDate(String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1))+" "+Calendar.getInstance().get(Calendar.YEAR));

        selectedDate = new DateData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        selectedDate.setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND, Color.BLUE));
        expCalendarView.markDate(selectedDate);
//      Set up listeners.
        expCalendarView.setOnDateClickListener(new OnExpDateClickListener()).setOnMonthScrollListener(new OnMonthScrollListener() {
            @Override
            public void onMonthChange(int year, int month)
            {
               current_month.setText( convertDate(String.valueOf(month))+" "+year);
            }

            @Override
            public void onMonthScroll(float positionOffset) {
//                Log.i("listener", "onMonthScroll:" + positionOffset);
            }
        });

        expCalendarView.setOnDateClickListener(new OnDateClickListener()
        {
            @Override
            public void onDateClick(View view, DateData date) {
               // expCalendarView.getMarkedDates().removeAdd();
               // expCalendarView.markDate(date);
               // selectedDate = date;
               // selectedDate.setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND, Color.BLUE));
                markDates();
               // Toast.makeText(MainActivity.this, String.format("%d-%d", date.getMonth(), date.getDay()), Toast.LENGTH_SHORT).show();
                String slctdate=date.getDay()+"-"+date.getMonth()+"-"+date.getYear();
                Intent i=new Intent(getApplicationContext(),Schedule.class);
                i.putExtra("date",slctdate);
                startActivity(i);
                finish();
            }
        });




// highlighting Calender Dates
         markDates();







        // uploading data
       uploadCase();
        uploadClient();
        uploadCaseType();
        uploadCourtNames();
        uploadCaseHistory();

        // updating data
        updateCase();
        updateClient();

    }



    public void TravelToClick(View v) {
        expCalendarView.travelTo(new DateData(1980, 11, 14));
    }




    private void markDates()
    {
        db=dbHelper.getReadableDatabase();
        List<String> datelist = new ArrayList<String>();

        // fetching  meeting dates of cases

        String[] columns = {DatabaseContract.CASE.Col_MeetingDate,DatabaseContract.CASE.Col_CaseNumber};
        cursor = db.query(DatabaseContract.CASE.TABLE_NAME, columns,
                DatabaseContract.CASE.Col_Email+ " =? AND "+DatabaseContract.CASE.Col_Status+ " =?" ,
                new String[]{email,"open"},
                null, null, null, null);
        if (cursor.getCount() == 0) {
//            db.close();
        }
        else
        {
            while (cursor.moveToNext()) {

                String date = cursor.getString(0);
                datelist.add(date);

            }
        }
        cursor.close();


        // fetching  Adjourn dates of cases


        Cursor c = db.query(DatabaseContract.CASE.TABLE_NAME, columns,
                DatabaseContract.CASE.Col_Status + "=? AND "+DatabaseContract.CASE.Col_Email+ " =?" ,
                new String[]{"open",email},
                null, null, null, null);
        if (c.getCount() > 0)
        {
            while (c.moveToNext()) {

                String casenumber = c.getString(1);


                String query = "SELECT * FROM casehistory WHERE casehistory.CaseId = ? AND casehistory.LawyerEmail =?";
                Cursor historycursor = db.rawQuery(query, new String[]{casenumber, email});
                if (historycursor.getCount() > 0) {

                    historycursor.moveToLast();
                    String adjournadate = historycursor.getString(3);
                    datelist.add(adjournadate);
                }
            }

        }
        c.close();

// marking dates
        String[] splitted;
        for(int i=0;i<datelist.size();i++)
        {
            String dt=datelist.get(i);
            if(dt.isEmpty())
            {

            }
            else {
                splitted = dt.split("-");
                int year = Integer.parseInt(splitted[2]);
                int month = Integer.parseInt(splitted[1]);
                int date = Integer.parseInt(splitted[0]);
                expCalendarView.markDate(new DateData(year, month, date).setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND, Color.RED)))
                        .hasTitle(false);
            }
        }




    }
    private String convertDate(String m)
    {
        if(m.equals("1")) {
            return "Janauary";
        }
        else if(m.equals("2")) {

            return "February";

        }
        else if(m.equals("3")) {

            return "March";

        }
        else if(m.equals("4")) {

            return "April";

        }
        else if(m.equals("5")) {

            return "May";

        }
        else if(m.equals("6")) {

            return "June";

        }
        else if(m.equals("7")) {

            return "July";

        }
        else if(m.equals("8")) {

            return "August";

        }
        else if(m.equals("9")) {

            return "September";

        }
        else if(m.equals("10")) {

            return "October";

        }
        else if(m.equals("11")) {

            return "November";

        }
        else {

            return "December";

        }
    }

    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            showDialog();

        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
            case R.id.schedule:
                drawer.closeDrawer(GravityCompat.START);// menu will close after clicking an item of menu
                i = new Intent(this, Schedule.class);
                i.putExtra("date",getCurrentDate());
                startActivity(i);
                finish();
                break;
            case R.id.show_all:
                drawer.closeDrawer(GravityCompat.START);// menu will close after clicking an item of menu
                i = new Intent(this, All_Cases.class);
                startActivity(i);
                finish();
                break;

            case R.id.client:
                drawer.closeDrawer(GravityCompat.START);
                i = new Intent(this, All_Clients.class);
                startActivity(i);
                finish();
                break;

            case R.id.chats:
                drawer.closeDrawer(GravityCompat.START);
                i = new Intent(this, Lawyer_Chat.class);
                startActivity(i);
                finish();
                break;

            case R.id.for_adjourn_cases:
                drawer.closeDrawer(GravityCompat.START);
                i = new Intent(this, Reminder_For_Adjourn_Day_Cases.class);
                startActivity(i);
                break;

            case R.id.particular_case:
                drawer.closeDrawer(GravityCompat.START);
                i = new Intent(this, Reminder_For_Particular_Case.class);
                startActivity(i);
                finish();
                break;

            case R.id.settings:
                drawer.closeDrawer(GravityCompat.START);
                i = new Intent(this, Settings.class);
                startActivity(i);
                finish();
                break;
            case R.id.profile:
                drawer.closeDrawer(GravityCompat.START);

                    i = new Intent(this, Profile.class);
                    startActivity(i);
                    finish();

                break;
            case R.id.rate_app:
                drawer.closeDrawer(GravityCompat.START);
                showRatingDialog();
                break;
            case R.id.logout:
                drawer.closeDrawer(GravityCompat.START);
                pd.show();
                FirebaseAuth.getInstance().signOut();
                SharedPreferences shrd=getSharedPreferences("userInfo",MODE_PRIVATE);// private mode means this file will only be used by this app
                SharedPreferences.Editor editor=shrd.edit();
                editor.putString("name","Full Name");
                editor.putString("email","Email");
                editor.putString("type","");
                editor.apply();
                pd.dismiss();
                i = new Intent(this, SignIn.class);
                startActivity(i);
                finish();
                break;

        }
        return true;
    }


    public void showDialog() {
        // custom dialog
        final Dialog dialog = new Dialog(MainActivity.this);
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_main_alert);
        TextView alertt = dialog.findViewById(R.id.alerttext);
        alertt.setText("Do you really want to quit Pakistan Lawyer Diary ?");
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


    public int findtomorrow()
    {


        //Toast.makeText(this, " Inside tommorrow", Toast.LENGTH_SHORT).show();
        String casenumber;
        int count = 0;
        db = dbHelper.getReadableDatabase();
        db1 = dbHelper.getReadableDatabase();

        String[] columns = {DatabaseContract.CASE._ID, DatabaseContract.CASE.Col_CaseTitle,
                DatabaseContract.CASE.Col_CourtName, DatabaseContract.CASE.Col_CaseType, DatabaseContract.CASE.Col_CaseNumber,
                DatabaseContract.CASE.Col_CaseYear, DatabaseContract.CASE.Col_OnBehalfOf, DatabaseContract.CASE.Col_PartyName,
                DatabaseContract.CASE.Col_ContactNumber, DatabaseContract.CASE.Col_PartyAdvocateName, DatabaseContract.CASE.Col_AdvocateContactNumber,
                DatabaseContract.CASE.Col_RespondantName, DatabaseContract.CASE.Col_FiledUnderSection,
                DatabaseContract.CASE.Col_Email,
        };

        cursor = db.query(DatabaseContract.CASE.TABLE_NAME, columns, DatabaseContract.CASE.Col_Email + "=?", new String[]{email}, null, null, null, null);


        if (cursor.getCount() == 0)
        {
            db.close();
           // Toast.makeText(this, "No tommorrow data to display", Toast.LENGTH_SHORT).show();


        }
        else
        {
            while (cursor.moveToNext())
            {
                casenumber = cursor.getString(4);
                String[] columns1 = {DatabaseContract.CASEHISTORY._ID, DatabaseContract.CASEHISTORY.Col_CaseId,
                        DatabaseContract.CASEHISTORY.Col_Previousdate, DatabaseContract.CASEHISTORY.Col_Adjourndate, DatabaseContract.CASEHISTORY.Col_Step,DatabaseContract.CASEHISTORY.Col_LawyerEmail,
                };

                // String sortOrder = DatabaseContract.ADDCASE._ID + " ASC";
                Cursor c1 = db1.query(DatabaseContract.CASEHISTORY.TABLE_NAME, columns1, DatabaseContract.CASEHISTORY.Col_CaseId + "=? AND "+DatabaseContract.CASEHISTORY.Col_LawyerEmail+ " =?", new String[]{String.valueOf(casenumber),email}, null, null, null);

                cursor.moveToNext();
                String ad = "";

                if (c1.getCount() > 0)
                {
                    c1.moveToLast();
                    ad = c1.getString(3);



                    if (gettomorrowDate().equals(ad)) {
                        // Toast.makeText(this, " "+gettomorrowDate(), Toast.LENGTH_SHORT).show();

                        count++;
                    }
                }
                c1.close();//   Toast.makeText(this, " "+cursor.getString(13), Toast.LENGTH_SHORT).show();

            }
            cursor.close();
            db1.close();
            db.close();

        }
        return count;


    }

    public int findtoday() {

      //  Toast.makeText(this, " Inside today", Toast.LENGTH_SHORT).show();
        String casenumber;
        int count = 0;
        db = dbHelper.getReadableDatabase();
        db1 = dbHelper.getReadableDatabase();

        String[] columns = {DatabaseContract.CASE._ID, DatabaseContract.CASE.Col_CaseTitle,
                DatabaseContract.CASE.Col_CourtName, DatabaseContract.CASE.Col_CaseType, DatabaseContract.CASE.Col_CaseNumber,
                DatabaseContract.CASE.Col_CaseYear, DatabaseContract.CASE.Col_OnBehalfOf, DatabaseContract.CASE.Col_PartyName,
                DatabaseContract.CASE.Col_ContactNumber, DatabaseContract.CASE.Col_PartyAdvocateName, DatabaseContract.CASE.Col_AdvocateContactNumber,
                DatabaseContract.CASE.Col_RespondantName, DatabaseContract.CASE.Col_FiledUnderSection,
                DatabaseContract.CASE.Col_Email,
        };

        cursor = db.query(DatabaseContract.CASE.TABLE_NAME, columns, DatabaseContract.CASE.Col_Email + "=?", new String[]{email}, null, null, null, null);

        if (cursor.getCount() == 0)
        {
            db.close();
           // Toast.makeText(this, "No today data to display ", Toast.LENGTH_SHORT).show();


        } else
        {

            while (cursor.moveToNext())
            {
                casenumber = cursor.getString(4);
                String[] columns1 = {DatabaseContract.CASEHISTORY._ID, DatabaseContract.CASEHISTORY.Col_CaseId,
                        DatabaseContract.CASEHISTORY.Col_Previousdate, DatabaseContract.CASEHISTORY.Col_Adjourndate, DatabaseContract.CASEHISTORY.Col_Step,DatabaseContract.CASEHISTORY.Col_LawyerEmail,
                };

                Cursor c1 = db1.query(DatabaseContract.CASEHISTORY.TABLE_NAME, columns1, DatabaseContract.CASEHISTORY.Col_CaseId + "=? AND "+DatabaseContract.CASEHISTORY.Col_LawyerEmail+ " =?", new String[]{String.valueOf(casenumber),email}, null, null, null);

                cursor.moveToNext();
                String ad = "";
                if (c1.getCount() > 0) {
                    c1.moveToLast();
                    ad = c1.getString(3);



                    if (getCurrentDate().equals(ad)) {

                        count++;
                    }
                }
                c1.close();
            }
            db1.close();
            db.close();

        }
        return count;

    }

    public int findtotal()
    {
        int count = 0;
        db = dbHelper.getReadableDatabase();


        String[] columns = {DatabaseContract.CASE._ID, DatabaseContract.CASE.Col_CaseTitle,
                DatabaseContract.CASE.Col_CourtName, DatabaseContract.CASE.Col_CaseType, DatabaseContract.CASE.Col_CaseNumber,
                DatabaseContract.CASE.Col_CaseYear, DatabaseContract.CASE.Col_OnBehalfOf, DatabaseContract.CASE.Col_PartyName,
                DatabaseContract.CASE.Col_ContactNumber, DatabaseContract.CASE.Col_PartyAdvocateName, DatabaseContract.CASE.Col_AdvocateContactNumber,
                DatabaseContract.CASE.Col_RespondantName, DatabaseContract.CASE.Col_FiledUnderSection,
                DatabaseContract.CASE.Col_Email,
        };

        // String sortOrder = DatabaseContract.ADDCASE._ID + " ASC";
        cursor = db.query(DatabaseContract.CASE.TABLE_NAME, columns, DatabaseContract.CASE.Col_Email + "=?", new String[]{email}, null, null, null, null);

        if (cursor.getCount() == 0) {
            db.close();
            //Toast.makeText(this, "No data to display", Toast.LENGTH_SHORT).show();


        } else {
            while (cursor.moveToNext()) {
                count++;
            }
            db.close();

        }
        return count;

    }


    public int finddisposed() {
        int count = 0;
        db = dbHelper.getReadableDatabase();


        String[] columns = {DatabaseContract.CASE._ID, DatabaseContract.CASE.Col_CaseTitle,
                DatabaseContract.CASE.Col_CourtName, DatabaseContract.CASE.Col_CaseType, DatabaseContract.CASE.Col_CaseNumber,
                DatabaseContract.CASE.Col_CaseYear, DatabaseContract.CASE.Col_OnBehalfOf, DatabaseContract.CASE.Col_PartyName,
                DatabaseContract.CASE.Col_ContactNumber, DatabaseContract.CASE.Col_PartyAdvocateName, DatabaseContract.CASE.Col_AdvocateContactNumber,
                DatabaseContract.CASE.Col_RespondantName, DatabaseContract.CASE.Col_FiledUnderSection,DatabaseContract.CASE.Col_Status,
                DatabaseContract.CASE.Col_Email,
        };

        // String sortOrder = DatabaseContract.ADDCASE._ID + " ASC";
        cursor = db.query(DatabaseContract.CASE.TABLE_NAME, columns, DatabaseContract.CASE.Col_Email + "=?", new String[]{email}, null, null, null, null);

        if (cursor.getCount() == 0) {
            db.close();
           // Toast.makeText(this, "No  disposed data to display", Toast.LENGTH_SHORT).show();


        } else {
            while (cursor.moveToNext())
            {
                String status = cursor.getString(13);
                if(status.equals("dispose")) {
                    count++;
                }
            }
            db.close();

        }
        return count;

    }






    public String gettomorrowDate() {
        Calendar c=Calendar.getInstance();
        StringBuilder builder = new StringBuilder();
        builder.append(c.get(Calendar.DAY_OF_MONTH)+ 1 + "-");
        builder.append((c.get(Calendar.MONTH) + 1 )+ "-");//month is 0 based
        builder.append(c.get(Calendar.YEAR));
        //Toast.makeText(this, builder.toString(), Toast.LENGTH_SHORT).show();
        return builder.toString();
    }




    public String getCurrentDate() {
        Calendar c=Calendar.getInstance();
        StringBuilder builder = new StringBuilder();
        builder.append(c.get(Calendar.DAY_OF_MONTH) + "-");
        builder.append((c.get(Calendar.MONTH)+ 1) + "-");//month is 0 based
        builder.append(c.get(Calendar.YEAR));
        //Toast.makeText(this, builder.toString(), Toast.LENGTH_SHORT).show();
        return builder.toString();
    }


    private void setUserInfo()
    {

        SharedPreferences getShared=getSharedPreferences("userInfo",MODE_PRIVATE);
        email=getShared.getString("email","Email");
        fname=getShared.getString("name","Full Name");
        name.setText(fname);
        emailtxtview.setText(email);
    }

    private void setInfo()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Lawyers");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        reference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               User userprofile = snapshot.getValue(   User.class);
                if (userprofile != null) {
                     fname = userprofile.getFullname();
                     email = userprofile.getEmail();
                    name.setText(fname);
                    emailtxtview.setText(email);

                } else {
                    //Toast.makeText(getApplicationContext(), "usr profile is null", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getApplicationContext(), " Something wrong", Toast.LENGTH_SHORT).show();

            }
        });

    }


    private void showRatingDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this);
        builder.setTitle(" App Rating");
        builder.setCancelable(false);
        View itemView = getLayoutInflater().inflate(R.layout.rating_layout,null);

        builder.setView(itemView);
        final RatingBar rBar =itemView.findViewById(R.id.rating_bar);
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if(networkManager.checkInternetConnection(MainActivity.this)) {

                    final float ratingValue = (rBar.getRating());
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("AppRating").child(firebaseUser.getUid());
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("ratingValue", ratingValue);
                    ref.updateChildren(hashMap);
                    Toast.makeText(getApplicationContext(), "Thank You For Rating", Toast.LENGTH_SHORT).show();

                }
                else

                {
                    Toast.makeText(getApplicationContext(), "Check your internet connection and try again", Toast.LENGTH_SHORT).show();
                }

            }


        });
        AlertDialog dialog=builder.create();
        dialog.show();
    }


    /*private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

*/

    /******************************************************** Uploading Data on Server*********************
     * ***************************************************************************************************
     * ****************************************************************************************************
     */
    public void uploadCase()
    {

        String uID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final ArrayList<CaseData> cd = new ArrayList<CaseData>();
        db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM Addcase WHERE Addcase.isLive = ? AND Addcase.Email  =?" ;

        Cursor _cursor = db.rawQuery(query, new String[]{"0",email});
        try {

            if (_cursor.moveToFirst()) {
                do {
                    int id= _cursor.getInt(0);
                    String t = _cursor.getString(1);
                    String cn = _cursor.getString(2);
                    String ct = _cursor.getString(3);
                    String caseno = _cursor.getString(4);
                    String cyear = _cursor.getString(5);
                    String obo = _cursor.getString(6);
                    String pn = _cursor.getString(7);
                    String pnum = _cursor.getString(8);
                    String adverse = _cursor.getString(9);
                    String adverseno = _cursor.getString(10);
                    String usec = _cursor.getString(11);
                    String respo = _cursor.getString(12);
                    String status = _cursor.getString(13);
                    String email = _cursor.getString(14);
                    String meetingDate = _cursor.getString(15);
                    String meetingTime = _cursor.getString(16);
                    //Toast.makeText(this, meetingDate, Toast.LENGTH_SHORT).show();
                    //Toast.makeText(this, meetingTime, Toast.LENGTH_SHORT).show();
                    //Toast.makeText(this, uID, Toast.LENGTH_SHORT).show();
                    CaseData Obj = new CaseData(id,t,cn,ct,caseno, cyear, obo, pn,pnum, adverse,adverseno, respo,usec,
                              status,email,meetingDate,meetingTime,uID);
                    cd.add(Obj);

                }
                while (_cursor.moveToNext());
            }

        }
        catch (SQLException exception) {
            exception.printStackTrace();
        } finally {
            cursor.close();
            db.close();
        }


        try {


            String EditKey = "";
            if (cd.size() != 0) {

                if (networkManager.checkInternetConnection(MainActivity.this)) {
                    db = dbHelper.getWritableDatabase();
                    reference = FirebaseDatabase.getInstance().getReference("Case");
                    for (int i = 0; i < cd.size(); i++) {

                        EditKey = reference.push().getKey();
                        CaseData case1 = new CaseData(cd.get(i).getCasetitle(),
                                cd.get(i).getCourtname(),
                                cd.get(i).getCasetype(),
                                cd.get(i).getNo(),
                                cd.get(i).getCaseYear(),
                                cd.get(i).getOnbehalf(),
                                cd.get(i).getParty(),
                                cd.get(i).getPartyContact(),
                                cd.get(i).getAdvocateName(),
                                cd.get(i).getAdvocateNum(),
                                cd.get(i).getRespondentName(),
                                cd.get(i).getFieldUnSection(),
                                cd.get(i).getStatus(),
                                cd.get(i).getEmail(),
                                cd.get(i).getMeetingDate(),
                                cd.get(i).getMeetingTime(),
                                cd.get(i).getU_id(), EditKey);
                        reference.child(EditKey).setValue(case1);
                        ContentValues values1 = new ContentValues();
                        values1.put(DatabaseContract.CASE.Col_EditKey, EditKey);
                        db.update(DatabaseContract.CASE.TABLE_NAME, values1, DatabaseContract.CASE._ID + "=?", new String[]{String.valueOf(cd.get(i).getId())});
                        values1.clear();
                    }

                    db.close();
                    db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(DatabaseContract.CASE.Col_isLive, "1");

                    long newRowId = db.update(DatabaseContract.CASE.TABLE_NAME,
                            values,
                            DatabaseContract.CASE.Col_isLive + " = ? AND " + DatabaseContract.CASE.Col_Email + " = ?",
                            new String[]{"0", email});
                    if (newRowId > 0)
                    {
                        values.clear();
                        db.close();
                    }
                    //Toast.makeText(this, "Record Saved to Server", Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(this, "New Record Inserted: ", Toast.LENGTH_SHORT).show();


                else {
                    //Toast.makeText(this,
                    //"No Interent Connection", Toast.LENGTH_SHORT).show();
                }
            }
        }
        catch(Exception e)
        {

        }

       // Log.d("LIST", "uploadCase: " + cd);

    }




    public void uploadClient()
    {

        String uID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final ArrayList<PartyData> cd = new ArrayList<PartyData>();
        db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM Client WHERE Client.isLive = ? AND Client.LawyerEmail  =?";

        Cursor _cursor = db.rawQuery(query, new String[]{"0",email});
        try {

            if (_cursor.moveToFirst()) {
                do {

                    int id= _cursor.getInt(0);
                    String name = _cursor.getString(1);
                    String cemail = _cursor.getString(2);
                    String phone = _cursor.getString(3);
                    String address = _cursor.getString(4);
                    String lemail = _cursor.getString(5);
                    PartyData  Obj = new PartyData (id,name, phone,
                            cemail, address, lemail, uID);
                    cd.add(Obj);

                } while (_cursor.moveToNext());
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        } finally {
            cursor.close();
            db.close();
        }

        try {


            String EditKey = "";
            if (cd.size() != 0) {

                if (networkManager.checkInternetConnection(MainActivity.this)) {
                    db = dbHelper.getWritableDatabase();
                    reference = FirebaseDatabase.getInstance().getReference("ManualClient");
                    for (int i = 0; i < cd.size(); i++) {
                        EditKey = reference.push().getKey();
                        PartyData c = new PartyData(cd.get(i).getClientName(),
                                cd.get(i).getPhone(), cd.get(i).getClientemail(), cd.get(i).getAddress(),
                                cd.get(i).getEmail(), cd.get(i).getUid(), EditKey);

                        reference.child(EditKey).setValue(c);
                        ContentValues values1 = new ContentValues();
                        values1.put(DatabaseContract.CLIENT.Col_EditKey, EditKey);
                        db.update(DatabaseContract.CLIENT.TABLE_NAME, values1, DatabaseContract.CLIENT._ID + "=?", new String[]{String.valueOf(cd.get(i).getId())});
                        values1.clear();

                    }

                    db.close();
                    db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(DatabaseContract.CASE.Col_isLive, "1");
                    long newRowId = db.update(DatabaseContract.CLIENT.TABLE_NAME,
                            values,
                            DatabaseContract.CLIENT.Col_isLive + " = ? AND " + DatabaseContract.CLIENT.Col_LawyerEmail + " = ?",
                            new String[]{"0", email});
                    if (newRowId > 0)
                    {
                        values.clear();
                        db.close();
                        //Toast.makeText(this,
                        //"Record Saved to Server", Toast.LENGTH_SHORT).show();
                    }


                } else {
                      //Toast.makeText(this,
                          //  "No Interent Connection", Toast.LENGTH_SHORT).show();
                }

            }
        }
        catch(Exception e)
        {

        }
       // Log.d("LIST", "UploadClient: " + cd);

    }

    public void uploadCaseType() {
        String uID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final ArrayList<TypeCourtData> cd = new ArrayList<TypeCourtData>();
        db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM CaseTypes WHERE CaseTypes.isLive = ? AND CaseTypes.Email  =?";

        Cursor _cursor = db.rawQuery(query, new String[]{"0", email});
        try {

            if (_cursor.moveToFirst()) {
                do {
                    String email = _cursor.getString(1);
                    String ctype = _cursor.getString(2);
                    TypeCourtData Obj = new TypeCourtData();
                    Obj.saveData(ctype, email, uID);
                    cd.add(Obj);

                } while (_cursor.moveToNext());
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        } finally {
            cursor.close();
            db.close();
        }

        // Log.d("UPLOADDATE", "uploadCaseType: "+cd);
        try {


            if (cd.size() != 0) {
                if (networkManager.checkInternetConnection(MainActivity.this)) {

                    reference = FirebaseDatabase.getInstance().getReference("CaseType");
                    for (int i = 0; i < cd.size(); i++) {
                        TypeCourtData ct = new TypeCourtData();
                        ct.saveData(cd.get(i).getName(), cd.get(i).getEmail(), cd.get(i).getUid());
                        reference.push().setValue(ct);
                    }


                    db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(DatabaseContract.CASETYPES.Col_isLive, "1");
                    long newRowId = db.update(DatabaseContract.CASETYPES.TABLE_NAME,
                            values,
                            DatabaseContract.CASETYPES.Col_isLive + " = ? AND " + DatabaseContract.CASETYPES.Col_Email + " = ?",
                            new String[]{"0", email});
                    if (newRowId > 0) {
                        values.clear();
                        db.close();
                    }

                     //Toast.makeText(this, "Record Saved to Server", Toast.LENGTH_SHORT).show();
                }

                //Toast.makeText(this, "New Record Inserted: ", Toast.LENGTH_SHORT).show();
                else {
                    //Toast.makeText(this,
                            //"No Interent Connection", Toast.LENGTH_SHORT).show();
                }
            }

        }
        catch (Exception e)
        {

        }

       // Log.d("LIST", "uploadCaseType: " + cd);

    }







    public void uploadCourtNames() {
        String uID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final ArrayList<TypeCourtData> cd = new ArrayList<TypeCourtData>();
        db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM CourtNames WHERE CourtNames.isLive = ? AND CourtNames.Email  =?";

        Cursor _cursor = db.rawQuery(query, new String[]{"0",email});
        try {

            if (_cursor.moveToFirst()) {
                do {
                    String email = _cursor.getString(1);
                    String cn = _cursor.getString(2);
                    TypeCourtData Obj = new TypeCourtData();
                    Obj.saveData(cn,email,uID);
                    cd.add(Obj);

                } while (_cursor.moveToNext());
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        } finally {
            cursor.close();
            db.close();
        }

        Log.d("LIST", "uploadCourtName: " + cd);


try {

    if (cd.size() != 0) {
        if (networkManager.checkInternetConnection(MainActivity.this)) {

            reference = FirebaseDatabase.getInstance().getReference("CourtName");
            for (int i = 0; i < cd.size(); i++) {
                TypeCourtData ct = new TypeCourtData();
                ct.saveData(cd.get(i).getName(), cd.get(i).getEmail(), cd.get(i).getUid());
                reference.push().setValue(ct);
            }
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DatabaseContract.CASETYPES.Col_isLive, "1");
            long newRowId = db.update(DatabaseContract.COURTNAMES.TABLE_NAME,
                    values,
                    DatabaseContract.COURTNAMES.Col_isLive + " = ? AND " + DatabaseContract.COURTNAMES.Col_Email + " = ?",
                    new String[]{"0", email});
            if (newRowId > 0) {
                values.clear();
                db.close();
            }
            //Toast.makeText(this,
            // "Record Saved to Server", Toast.LENGTH_SHORT).show();
        }
        // Toast.makeText(this, "New Record Inserted: ", Toast.LENGTH_SHORT).show();

        else {
            //Toast.makeText(this,
                    //"No Interent Connection", Toast.LENGTH_SHORT).show();
        }

    }
}
catch (Exception e) {

}
        //Log.d("LIST", "uploadCourtNames: " + cd);

        }



    public void uploadCaseHistory()
    {
        String uID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final ArrayList<HistoryData> cd = new ArrayList<HistoryData>();
        db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM casehistory WHERE casehistory.isLive = ? AND casehistory.LawyerEmail  =?";
        Cursor _cursor = db.rawQuery(query, new String[]{"0",email});
        try {

            if (_cursor.moveToFirst()) {

                do {
                    String cid = _cursor.getString(1);
                    String pdate = _cursor.getString(2);
                    String adate = _cursor.getString(3);
                    String step = _cursor.getString(4);
                    String adTime=_cursor.getString(5);
                    String lawyeremail=_cursor.getString(7);
                    HistoryData Obj = new HistoryData(cid, pdate, adate, step
                            ,adTime,uID,lawyeremail);
                    cd.add(Obj);

                } while (_cursor.moveToNext());
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        } finally {
            cursor.close();
            db.close();
        }

  //      Log.d("LIST", "uploadCaseHistory: " + cd);

try {

    if (cd.size() != 0) {
        if (networkManager.checkInternetConnection(MainActivity.this)) {
            fauth = FirebaseAuth.getInstance();
            firebasedatabase = FirebaseDatabase.getInstance();
            reference = FirebaseDatabase.getInstance().getReference("CaseHistory");
            for (int i = 0; i < cd.size(); i++) {
                HistoryData ct = new HistoryData(cd.get(i).getCasenumber(), cd.get(i).getPreviousdate(), cd.get(i).getAdjourndate(),
                        cd.get(i).getStep(), cd.get(i).getHiringTime(), cd.get(i).getUid(), cd.get(i).getLawyer_email());
                reference.push().setValue(ct);
            }


            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DatabaseContract.CASEHISTORY.Col_isLive, "1");
            long newRowId = db.update(DatabaseContract.CASEHISTORY.TABLE_NAME,
                    values,
                    DatabaseContract.CASEHISTORY.Col_isLive + " = ? AND " + DatabaseContract.CASEHISTORY.Col_LawyerEmail + " = ?",
                    new String[]{"0", email});
            if (newRowId > 0) {
                values.clear();
                db.close();
            }
            //Toast.makeText(this, "Record Saved to Server", Toast.LENGTH_SHORT).show();
        }
        //Toast.makeText(this, "New Record Inserted: ", Toast.LENGTH_SHORT).show();


        else {
            //Toast.makeText(this,
            // "No Interent Connection", Toast.LENGTH_SHORT).show();
        }
    }
}
catch (Exception e)
{
}
        //Log.d("LIST", "uploadCaseHistory: " + cd);


    }




    /********************************************Updating Data on server************************************
     * *********************************************************************************************************
     * **********************************************************************************************************
     */



    public void updateCase()

    {

        final ArrayList<CaseData> cd = new ArrayList<CaseData>();
        db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM Addcase WHERE Addcase.isEditLive = ? AND Addcase.Email  =?" ;

        Cursor _cursor = db.rawQuery(query, new String[]{"0",email});
        try {

            if (_cursor.moveToFirst()) {
                do {
                    String t = _cursor.getString(1);
                    String cn = _cursor.getString(2);
                    String ct = _cursor.getString(3);
                    String cyear = _cursor.getString(5);
                    String obo = _cursor.getString(6);
                    String pn = _cursor.getString(7);
                    String pnum = _cursor.getString(8);
                    String adverse = _cursor.getString(9);
                    String adverseno = _cursor.getString(10);
                    String usec = _cursor.getString(11);
                    String respo = _cursor.getString(12);
                    String status = _cursor.getString(13);
                    String meetingDate = _cursor.getString(15);
                    String meetingTime = _cursor.getString(16);
                    String editkey = _cursor.getString(19);

                    CaseData Obj = new CaseData(t,cn,ct, cyear, obo, pn,pnum, adverse,adverseno, respo,usec,
                            status,meetingDate,meetingTime,editkey);
                    cd.add(Obj);

                }
                while (_cursor.moveToNext());
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        } finally {
            cursor.close();
            db.close();
        }

        try {


            if (cd.size() != 0)
            {
                if (networkManager.checkInternetConnection(MainActivity.this)) {
                    reference = FirebaseDatabase.getInstance().getReference("Case");
                    for (int i = 0; i < cd.size(); i++) {

                        reference = FirebaseDatabase.getInstance().getReference("Case").child(cd.get(i).getEditkey());
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("casetitle", cd.get(i).getCasetitle());
                        hashMap.put("courtname", cd.get(i).getCourtname());
                        hashMap.put("casetype", cd.get(i).getCasetype());
                        hashMap.put("caseYear", cd.get(i).getCaseYear());
                        hashMap.put("onbehalf", cd.get(i).getOnbehalf());
                        hashMap.put("party", cd.get(i).getParty());
                        hashMap.put("partyContact", cd.get(i).getPartyContact());
                        hashMap.put("advocateName", cd.get(i).getAdvocateName());
                        hashMap.put("advocateNum", cd.get(i).getAdvocateNum());
                        hashMap.put("respondentName", cd.get(i).getRespondentName());
                        hashMap.put("fieldUnSection", cd.get(i).getFieldUnSection());
                        hashMap.put("meetingDate", cd.get(i).getMeetingDate());
                        hashMap.put("meetingTime", cd.get(i).getMeetingTime());
                        hashMap.put("status", cd.get(i).getStatus());
                        reference.updateChildren(hashMap);

                    }

                    db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(DatabaseContract.CASE.Col_isEditLive, "1");

                    long newRowId = db.update(DatabaseContract.CASE.TABLE_NAME,
                            values,
                            DatabaseContract.CASE.Col_isEditLive + " = ? AND " + DatabaseContract.CASE.Col_Email + " = ?",
                            new String[]{"0", email});
                    if (newRowId > 0) {
                        values.clear();
                        db.close();
                    }
                    //Toast.makeText(this, "Record updated to Server", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(this, "Record updated: ", Toast.LENGTH_SHORT).show();
                }


                else {
                    //Toast.makeText(this,
                          //  "No Interent Connection", Toast.LENGTH_SHORT).show();
                }
            }
        }
        catch (Exception e)
        {


        }



       // Log.d("LIST", "update case: " + cd);

    }





   public void updateClient()
    {


        final ArrayList<PartyData> cd = new ArrayList<PartyData>();
        db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM Client WHERE Client.isEditLive = ? AND Client.LawyerEmail  =?";

        Cursor _cursor = db.rawQuery(query, new String[]{"0",email});
        try {

            if (_cursor.moveToFirst()) {
                do {
                    String name = _cursor.getString(1);
                    String email = _cursor.getString(2);
                    String phone = _cursor.getString(3);
                    String address = _cursor.getString(4);
                    String editkey = _cursor.getString(8);

                   PartyData Obj = new PartyData(name,phone,email,address,editkey);
                    cd.add(Obj);

                }
                while (_cursor.moveToNext());
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        } finally {
            cursor.close();
            db.close();
        }


        try {

            if (cd.size() != 0) {
                if (networkManager.checkInternetConnection(MainActivity.this)) {

                    for (int i = 0; i < cd.size(); i++) {

                        reference = FirebaseDatabase.getInstance().getReference("ManualClient").child(cd.get(i).getEditKey());
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("clientName", cd.get(i).getClientName());
                        hashMap.put("phone", cd.get(i).getPhone());
                        hashMap.put("clientemail", cd.get(i).getClientemail());
                        hashMap.put("address", cd.get(i).getAddress());
                        reference.updateChildren(hashMap);

                    }

                    db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(DatabaseContract.CLIENT.Col_isEditLive, "1");

                    long newRowId = db.update(DatabaseContract.CLIENT.TABLE_NAME,
                            values,
                            DatabaseContract.CLIENT.Col_isEditLive + " = ? AND " + DatabaseContract.CLIENT.Col_LawyerEmail + " = ?",
                            new String[]{"0", email});
                    if (newRowId > 0) {
                        db.close();
                        values.clear();
                        // Toast.makeText(this, "Record updated to Server", Toast.LENGTH_SHORT).show();
                    }
                    //Toast.makeText(this, "Record updated: ", Toast.LENGTH_SHORT).show();
                } else {
                      //Toast.makeText(this,
                     //"No Interent Connection", Toast.LENGTH_SHORT).show();
                }
            }
        }
        catch (Exception e)
        {

        }



       // Log.d("LIST", "update client: " + cd);

    }


    public void help(View v)
    {

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this);
        View itemView = getLayoutInflater().inflate(R.layout.help_dialog,null);

        builder.setView(itemView);
        TextView text= itemView.findViewById(R.id.text);
        SpannableStringBuilder ssb=new SpannableStringBuilder
                ("** Select date on calender to view schedule of that date." +
                        "\n\n" +
                        "** Open side menu to explore all features of app."

                );
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

}
