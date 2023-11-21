package com.example.pakistanlawyerdiary.Lawyer;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pakistanlawyerdiary.Database.DatabaseContract;
import com.example.pakistanlawyerdiary.Database.DatabaseHelper;
import com.example.pakistanlawyerdiary.Lawyer.manager.NetworkManager;
import com.example.pakistanlawyerdiary.Model.PartyData;
import com.example.pakistanlawyerdiary.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Add_Client extends AppCompatActivity {
    EditText client_name;
    EditText client_email;
    EditText client_number;
    EditText client_address;
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    String email=MainActivity.email;
    String c_name,c_email,c_phone,c_address;
    DatabaseReference reference;
    String uid;
    String EditKey="";
    NetworkManager networkManager
            =
            new NetworkManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__client);
        getEmail();
        client_name = (EditText) findViewById(R.id.client_name);
        client_email = (EditText) findViewById(R.id.client_email);
        client_number = (EditText) findViewById(R.id.phone_number);
        client_address = (EditText) findViewById(R.id.client_address);
        dbHelper = new DatabaseHelper(this);


    }

    public void saveClient(View v) {

        try {

            reference = FirebaseDatabase.getInstance().getReference("ManualClient");
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            c_name = client_name.getText().toString().trim();
            c_email = client_email.getText().toString().trim();
            c_phone = client_number.getText().toString().trim();
            c_address = client_address.getText().toString().trim();
            if (c_name.isEmpty() || c_phone.isEmpty() || c_address.isEmpty()) {
                showDialog();
                return;

            }
            if (!c_email.isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(c_email).matches()) {
                client_email.setError("Please provide a valid email");
                client_email.requestFocus();
                return;
            }
            if(c_email.isEmpty())
            {
                c_email="";
            }
            if (networkManager.checkInternetConnection(Add_Client.this)) {

                values.put(DatabaseContract.CLIENT.Col_Name, c_name);
                values.put(DatabaseContract.CLIENT.Col_Email, c_email);
                values.put(DatabaseContract.CLIENT.Col_Phone, c_phone);
                values.put(DatabaseContract.CLIENT.Col_Address, c_address);
                values.put(DatabaseContract.CLIENT.Col_LawyerEmail, email);
                values.put(DatabaseContract.CLIENT.Col_isLive, "1");
                long newRowId = db.insert(DatabaseContract.CLIENT.TABLE_NAME, null, values);
                if (newRowId > 0) {
                    values.clear();

                    EditKey = reference.push().getKey();
                    PartyData c = new PartyData(c_name, c_phone, c_email, c_address, email, uid, EditKey);
                    reference.child(EditKey).setValue(c);

                    /// Adding editkey to firebase
                    values.put(DatabaseContract.CLIENT.Col_EditKey, EditKey);
                    db.update(DatabaseContract.CLIENT.TABLE_NAME, values, DatabaseContract.CLIENT._ID + "=?", new String[]{String.valueOf(newRowId)});

                    db.close();
                    values.clear();
                    final Dialog dialog = new Dialog(Add_Client.this);
                    dialog.setContentView(R.layout.custom_alert);
                    dialog.setCancelable(false);
                    TextView alertt = dialog.findViewById(R.id.alerttext);
                    alertt.setText("Client details has been saved successfully");
                    Button ok = dialog.findViewById(R.id.ok);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Intent intent = new Intent(getApplicationContext(), All_Clients.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    dialog.show();
                } else {
                    Toast.makeText(this, "Client Not Inserted: " + newRowId, Toast.LENGTH_SHORT).show();
                }
                db.close();
                values.clear();
            } else {


                values.put(DatabaseContract.CLIENT.Col_Name, c_name);
                values.put(DatabaseContract.CLIENT.Col_Email, c_email);
                values.put(DatabaseContract.CLIENT.Col_Phone, c_phone);
                values.put(DatabaseContract.CLIENT.Col_Address, c_address);
                values.put(DatabaseContract.CLIENT.Col_LawyerEmail, email);
                values.put(DatabaseContract.CLIENT.Col_isLive, "0");

                //Toast.makeText(this, c_name + c_email + c_phone + c_address + email, Toast.LENGTH_SHORT).show();

                long newRowId = db.insert(DatabaseContract.CLIENT.TABLE_NAME, null, values);


                if (newRowId > 0) {
                    db.close();
                    values.clear();
                    final Dialog dialog = new Dialog(Add_Client.this);
                    dialog.setContentView(R.layout.custom_alert);
                    dialog.setCancelable(false);
                    TextView alertt = dialog.findViewById(R.id.alerttext);
                    alertt.setText("Client details has been saved successfully");
                    Button ok = dialog.findViewById(R.id.ok);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Intent intent = new Intent(getApplicationContext(), All_Clients.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    dialog.show();

                } else {
                    Toast.makeText(this, "Client Not Inserted: " + newRowId, Toast.LENGTH_SHORT).show();
                }


                db.close();
                values.clear();


            }
        }
        catch(Exception e)
            {
                Toast.makeText(this, e.getMessage(),Toast.LENGTH_SHORT).show();
            }

    }





    public void showDialog()
    {

        final Dialog dialog = new Dialog(Add_Client.this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_alert);
        TextView alertt=dialog.findViewById(R.id.alerttext);
        alertt.setText("Please enter mendatory details");
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



    public void help(View v) {

    }
    public void resetValues()
    {
        client_name.setText("");
        client_email.setText("");
        client_number.setText("");
        client_address.setText("");

    }



    public void back(View v )
    {

        Intent intent = new Intent(getApplicationContext(),All_Clients.class);
        startActivity(intent);
        finish();


    }

    public void onBackPressed()
    {

        Intent i=  new Intent(getApplicationContext(),All_Clients.class);
        startActivity(i);
        finish();

    }

}