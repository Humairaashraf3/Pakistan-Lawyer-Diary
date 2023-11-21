package com.example.pakistanlawyerdiary.Lawyer.Reminder;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.pakistanlawyerdiary.R;

import static android.content.Context.MODE_PRIVATE;

public class Particular_Notification_Reciever extends BroadcastReceiver {
    private static final String CHANNEL_ID="SAMPLE_CHANNEL1";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        String id=intent.getStringExtra("id");
        String title=intent.getStringExtra("title");
        String email=intent.getStringExtra("email");


            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // for API 26 or above
                CharSequence channel_name = "My Notification";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channel_name, importance);
                notificationManager.createNotificationChannel(channel);
            }
            Intent repeating_inent = new Intent(context, Edit_Reminder.class);
            repeating_inent.putExtra("id", id);// activity that will open when user click on notificcation
            repeating_inent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, Integer.parseInt(id), repeating_inent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            long vibrate[] = {100, 500, 100, 500};
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID).setContentIntent(pendingIntent)
                    .setSmallIcon(R.mipmap.ic_launcher).
                            setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(title).
                            setContentText("")
                    .setLights(Color.YELLOW, 200, 200)
                    .setVibrate(vibrate)
                    .setSound(defaultSound)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);
       boolean r=isCorrectReciever(context,email);
       if(r==true)
        {

            notificationManager.notify(Integer.parseInt(id), builder.build());

// notification id must be unique
        }



    }

    private boolean isCorrectReciever(Context context,String email)
    {
        SharedPreferences getShared = context.getSharedPreferences("userInfo", MODE_PRIVATE);
        String currentemail=getShared.getString("email","Email");
        if(email.equals( currentemail))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
