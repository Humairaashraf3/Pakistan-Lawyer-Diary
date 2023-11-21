package com.example.pakistanlawyerdiary.Lawyer.Reminder;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.pakistanlawyerdiary.Lawyer.Adjourn_Cases;
import com.example.pakistanlawyerdiary.Database.DatabaseContract;
import com.example.pakistanlawyerdiary.Database.DatabaseHelper;
import com.example.pakistanlawyerdiary.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class Adjourn_Notification_Reciever extends BroadcastReceiver {
    private static final String CHANNEL_ID="SAMPLE_CHANNEL";
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    SQLiteDatabase db1;
    Cursor cursor;
    String email;
    @Override
    public void onReceive(Context context, Intent intent)
    {
        email=intent.getStringExtra("email");
        dbHelper = new DatabaseHelper(context);



                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                {
                    // for API 26 or above
                    CharSequence channel_name = "My Notification";
                    int importance = NotificationManager.IMPORTANCE_DEFAULT;
                    NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channel_name, importance);
                    notificationManager.createNotificationChannel(channel);
                }
                Intent repeating_inent = new Intent(context, Adjourn_Cases.class); // activity that will open when user click on notificcation
                repeating_inent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 180, repeating_inent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                long vibrate[] = {100, 500, 100, 500};
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID).setContentIntent(pendingIntent)
                        .setSmallIcon(R.mipmap.ic_launcher).
                                setContentTitle("Reminder For Tommorrow Adjourn Cases")
                                .setStyle(new NotificationCompat.BigTextStyle()
            .bigText("You have Adjourn Cases Tommorrow. You can call or \n sms to client from Pakistan Lawyer Diary App")
                                  )

                        .setSound(defaultSound)
                        .setVibrate(vibrate)
                        .setLights(Color.YELLOW, 200, 200)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true);
        boolean r=isAdjournCases();
        if(r==true)
        {
            SharedPreferences getShared=context.getApplicationContext().getSharedPreferences("userInfo",MODE_PRIVATE);
            String currentuser=getShared.getString("email","Email");
            if(email.equals(currentuser))
            {

                notificationManager.notify(600, builder.build()); }
            }

        else
        {

        }
    }


    private boolean isAdjournCases()
    {



        String casenumber;
        db = dbHelper.getReadableDatabase();
        db1 = dbHelper.getReadableDatabase();

        String[] columns = {DatabaseContract.CASE._ID, DatabaseContract.CASE.Col_CaseTitle,
                DatabaseContract.CASE.Col_CourtName, DatabaseContract.CASE.Col_CaseType, DatabaseContract.CASE.Col_CaseNumber,
                DatabaseContract.CASE.Col_CaseYear, DatabaseContract.CASE.Col_OnBehalfOf, DatabaseContract.CASE.Col_PartyName,
                DatabaseContract.CASE.Col_ContactNumber, DatabaseContract.CASE.Col_PartyAdvocateName, DatabaseContract.CASE.Col_AdvocateContactNumber,
                DatabaseContract.CASE.Col_RespondantName, DatabaseContract.CASE.Col_FiledUnderSection,
                DatabaseContract.CASE.Col_Email,
        };

        cursor = db.query(DatabaseContract.CASE.TABLE_NAME, columns, DatabaseContract.CASE.Col_Email + "=?" + " AND " +
                        DatabaseContract.CASE.Col_Status + "=?"
                , new String[]{email, "open"} , null, null, null, null);


        if (cursor.getCount() == 0)
        {
            db.close();
            return  false;
        }
        else
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

                if (c1.getCount() > 0)
                {
                    c1.moveToLast();
                    ad = c1.getString(3);



                    if (gettomorrowDate().equals(ad)) {
                        // Toast.makeText(this, " "+gettomorrowDate(), Toast.LENGTH_SHORT).show();
                        return  true;
                    }
                }
                c1.close();

            }
            cursor.close();
            db1.close();
            db.close();

        }

return false;
    }



    public String gettomorrowDate()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("d-M-yyyy");
        String tomorrowAsString = dateFormat.format(tomorrow);
        return  tomorrowAsString;
    }


}