package com.example.pakistanlawyerdiary.Lawyer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.pakistanlawyerdiary.R;

public class Client_Reminder extends AppCompatActivity {
    String casenumber,adjourndate,contact,partyname;
    String contactnum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client__reminder);

        Intent i1 = getIntent();
        casenumber= i1.getStringExtra("casenumber");
        partyname=i1.getStringExtra("partyname");
        contact=i1.getStringExtra("contact");
        adjourndate = i1.getStringExtra("adjourndate");

        contactnum="tel:"+contact;
        final Dialog dialog = new Dialog(Client_Reminder.this);
        dialog.setContentView(R.layout.client_reminder_alert);

        final Button call=dialog.findViewById(R.id.call);
        Button sms=dialog.findViewById(R.id.sms);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                call();
                dialog.dismiss();


            }
        });
        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sms();
                dialog.dismiss();

            }
        });
        dialog.show();
    }


    public void call()
    {

        String url= contactnum;
        Intent i=new Intent(Intent.ACTION_DIAL, Uri.parse(url));
        startActivity(i);
        finish();

    }
    public void sms()
    {
        String url= "smsto:"+contact;
        Uri sms_uri = Uri.parse(url);
        Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri);
        String sms_body="Reminder:\nMr/Mrs "+partyname+" YOUR CASE ADJOURN DATE IS SCHEDULED ON "+adjourndate+" PLEASE MAKE SURE TO ATTEND AT COURT. INFORM TO ME YOUR AVAILABILITY STATUS SOON.\nKindly Regards,";
        sms_intent.putExtra("sms_body", sms_body);
        startActivity(sms_intent);
        finish();
    }
}