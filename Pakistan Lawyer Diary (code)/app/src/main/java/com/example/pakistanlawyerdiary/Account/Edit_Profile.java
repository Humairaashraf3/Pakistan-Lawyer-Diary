package com.example.pakistanlawyerdiary.Account;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pakistanlawyerdiary.Lawyer.manager.NetworkManager;
import com.example.pakistanlawyerdiary.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Edit_Profile extends AppCompatActivity {

    TextView username,address,mobileNumber,about;
    String type;
    String u,n,a,ab;
    NetworkManager networkManager = new NetworkManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__profile);
        getType();
        Intent i=getIntent();
        u=i.getStringExtra("name");
        n=i.getStringExtra("phone");
        a=i.getStringExtra("address");
        ab=i.getStringExtra("about");
        username = findViewById(R.id.username);
        address = findViewById(R.id.address);
        mobileNumber = findViewById(R.id.mobile_number);
        about = findViewById(R.id.about);
        username.setText(u);
        mobileNumber.setText(n);
        address.setText(a);
        if(type.equals("1"))
        {
            about.setVisibility(View.VISIBLE);
            about.setText(ab);
        }
    }



    public void editProfile(View v )

    {

        String un=username.getText().toString().trim();
        String phn=mobileNumber.getText().toString().trim();
        String ad=address.getText().toString().trim();

        if(un.isEmpty() || phn.isEmpty() ||ad.isEmpty())
        {
            showDialog();
            return;

        }

        if (networkManager.checkInternetConnection(Edit_Profile.this))
        {


            final Dialog dialog = new Dialog(Edit_Profile.this);
            //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.custom_main_alert);
            TextView alertt = dialog.findViewById(R.id.alerttext);
            alertt.setText("Do you really want to update information?");
            Button ok = dialog.findViewById(R.id.yes);
            Button no = dialog.findViewById(R.id.no);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                    saveDetails();

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
        else
        {
            Toast.makeText(Edit_Profile.this,"Check your internet connection and try again",Toast.LENGTH_SHORT).show();
        }

    }


    private void saveDetails()
    {

        if (type.equals("1"))
        {

            DatabaseReference reference = FirebaseDatabase.getInstance().
                    getReference("Lawyers").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("fullname", username.getText().toString());
            hashMap.put("phone", mobileNumber.getText().toString());
            hashMap.put("address", address.getText().toString());
            hashMap.put("about", about.getText().toString());
            reference.updateChildren(hashMap);
            successDialog();

        }

        else
        {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Clients").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("fullname", username.getText().toString());
            hashMap.put("phone", mobileNumber.getText().toString());
            hashMap.put("address", address.getText().toString());
            reference.updateChildren(hashMap);
            successDialog();
        }


    }

private void successDialog()
{
    final Dialog dialog = new Dialog(Edit_Profile.this);
    dialog.setCancelable(false);
    dialog.setContentView(R.layout.custom_alert);
    TextView alertt=dialog.findViewById(R.id.alerttext);
    alertt.setText("Inoformation has been updated successfully");
    Button ok=dialog.findViewById(R.id.ok);
    ok.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialog.dismiss();
            //Toast.makeText(getApplicationContext(),"Dismissed..!!",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), Profile.class);
            startActivity(intent);
            finish();

        }
    });
    dialog.show();

}


    public void showDialog()
    {
        final Dialog dialog = new Dialog(Edit_Profile.this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_alert);
        TextView alertt=dialog.findViewById(R.id.alerttext);
        alertt.setText("Please enter all details");
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
    private void getType()
    {

        SharedPreferences getShared=getSharedPreferences("userInfo",MODE_PRIVATE);
        type=getShared.getString("type","Type");

    }


    public void back(View v )
    {

        Intent intent = new Intent(getApplicationContext(),Profile.class);
        startActivity(intent);
        finish();


    }
    public void onBackPressed()
    {

        Intent i=  new Intent(getApplicationContext(),Profile.class);
        startActivity(i);
        finish();

    }
}