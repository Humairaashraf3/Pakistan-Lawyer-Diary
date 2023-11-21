package com.example.pakistanlawyerdiary.Lawyer;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
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
import com.example.pakistanlawyerdiary.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Update_Client extends AppCompatActivity

{
    String Id;
    SQLiteDatabase db;
    DatabaseHelper dbHelper;
    Cursor cursor;
    EditText client_name;
    EditText client_email;
    EditText client_number;
    EditText client_address;
    String cname,email,phone,address,lawyeremail;
    String isLive="";
    String EditKey="";
    NetworkManager networkManager = new NetworkManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update__client);
        Intent i1 = getIntent();
        Id= i1.getStringExtra("id");
        client_name=(EditText)findViewById(R.id.client_name);
        client_email=(EditText)findViewById(R.id.client_email);
        client_number=(EditText)findViewById(R.id.phone_number);
        client_address=(EditText)findViewById(R.id.client_address);
        dbHelper = new DatabaseHelper(this);
        db=dbHelper.getReadableDatabase();

        String[] columns = {DatabaseContract.CLIENT._ID, DatabaseContract.CLIENT.Col_Name, DatabaseContract.CLIENT.Col_Email,
                DatabaseContract.CLIENT.Col_Phone, DatabaseContract.CLIENT.Col_Address,DatabaseContract.CLIENT.Col_LawyerEmail,
                DatabaseContract.CLIENT.Col_isLive,DatabaseContract.CLIENT.Col_EditKey};
        cursor = db.query(DatabaseContract.CLIENT.TABLE_NAME, columns, DatabaseContract.CLIENT._ID + "=?", new String[]{Id}, null, null, null, null);

        if(cursor.getCount()>0)
        {
            cursor.moveToNext();
            String id=cursor.getString(0);
            cname = cursor.getString(1);
            email = cursor.getString(2);
            phone = cursor.getString(3);
            address = cursor.getString(4);
            lawyeremail = cursor.getString(5);
            isLive=cursor.getString(6);
            if(isLive.equals("1"))
            {
                EditKey = cursor.getString(7);
            }
            //Toast.makeText(getApplicationContext(),EditKey,Toast.LENGTH_SHORT).show();
            client_name.setText(cname);
            client_email.setText(email);
            client_number.setText(phone);
            client_address.setText(address);
            cursor.close();
        }
        else
        {
            //Toast.makeText(getApplicationContext(), "No Data to display", Toast.LENGTH_SHORT).show();

        }
        db.close();
    }




    public void resetValues()
    {
        client_name.setText("");
        client_email.setText("");
        client_number.setText("");
        client_address.setText("");

    }





    public void updateClient(View v)
    {
        try {


            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            cname = client_name.getText().toString().trim();
            email = client_email.getText().toString().trim();
            phone = client_number.getText().toString().trim();
            address = client_address.getText().toString().trim();
            if (cname.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                showDialog();
                return;

            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                client_email.setError("Please provide a valid email");
                client_email.requestFocus();
                return;
            }

            if (networkManager.checkInternetConnection(Update_Client.this)) {

                values.put(DatabaseContract.CLIENT.Col_Name, cname);
                values.put(DatabaseContract.CLIENT.Col_Email, email);
                values.put(DatabaseContract.CLIENT.Col_Phone, phone);
                values.put(DatabaseContract.CLIENT.Col_Address, address);
                values.put(DatabaseContract.CLIENT.Col_LawyerEmail, lawyeremail);

                long newRowId = db.update(DatabaseContract.CLIENT.TABLE_NAME, values, DatabaseContract.CLIENT._ID + "=?", new String[]{String.valueOf(Id)});


                if (newRowId > 0) {
                    if (isLive.equals("1")) {
                        values.clear();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ManualClient").child(EditKey);
                        HashMap<String, Object> hashMap = new HashMap<>();

                        hashMap.put("clientName", cname);
                        hashMap.put("phone", phone);
                        hashMap.put("clientemail", email);
                        hashMap.put("address", address);

                        reference.updateChildren(hashMap);
                        // adding editlive value to sqlite database
                        values.put(DatabaseContract.CLIENT.Col_isEditLive, "1");
                        db.update(DatabaseContract.CLIENT.TABLE_NAME, values, DatabaseContract.CLIENT._ID + "=?", new String[]{Id});

                    }
                    db.close();
                    values.clear();
                    final Dialog dialog = new Dialog(Update_Client.this);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.custom_alert);
                    TextView alertt = dialog.findViewById(R.id.alerttext);
                    alertt.setText("Details has been updated successfully");
                    Button ok = dialog.findViewById(R.id.ok);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            //Toast.makeText(getApplicationContext(),"Dismissed..!!",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), All_Clients.class);
                            startActivity(intent);
                            finish();

                        }
                    });
                    dialog.show();


                } else {
                    // Toast.makeText(this, "Client Not Updated: " + newRowId, Toast.LENGTH_SHORT).show();
                }
                db.close();

                values.clear();

            } else {
                values.put(DatabaseContract.CLIENT.Col_Name, cname);
                values.put(DatabaseContract.CLIENT.Col_Email, email);
                values.put(DatabaseContract.CLIENT.Col_Phone, phone);
                values.put(DatabaseContract.CLIENT.Col_Address, address);
                values.put(DatabaseContract.CLIENT.Col_LawyerEmail, lawyeremail);
                if (isLive.equals("1")) {
                    values.put(DatabaseContract.CLIENT.Col_isEditLive, "0");
                }

                long newRowId = db.update(DatabaseContract.CLIENT.TABLE_NAME, values, DatabaseContract.CLIENT._ID + "=?", new String[]{String.valueOf(Id)});


                if (newRowId > 0) {
                    db.close();
                    values.clear();

                    final Dialog dialog = new Dialog(Update_Client.this);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.custom_alert);
                    TextView alertt = dialog.findViewById(R.id.alerttext);
                    alertt.setText("Details has been updated successfully");
                    Button ok = dialog.findViewById(R.id.ok);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            //Toast.makeText(getApplicationContext(),"Dismissed..!!",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), All_Clients.class);
                            startActivity(intent);
                            finish();

                        }
                    });
                    dialog.show();


                } else {
                    // Toast.makeText(this, "Client Not Updated: " + newRowId, Toast.LENGTH_SHORT).show();
                }
                db.close();

                values.clear();

            }
        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }






    public void deleteClient(View v)
    {

        db = dbHelper.getWritableDatabase();
        final Dialog dialog = new Dialog(Update_Client.this);
        dialog.setContentView(R.layout.custom_main_alert);
        TextView alertt=dialog.findViewById(R.id.alerttext);
        alertt.setText("Do you really want to delete this client ?");
        Button ok=dialog.findViewById(R.id.yes);
        Button no=dialog.findViewById(R.id.no);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Integer i1= db.delete(DatabaseContract.CLIENT.TABLE_NAME, DatabaseContract.CLIENT._ID +"=?",new String[] {String.valueOf(Id)});
                if (i1 > 0)
                {
                    db.close();
                    dialog.dismiss();
                    //Toast.makeText(getApplicationContext(), " client deleted: " , Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),All_Clients.class);
                    startActivity(intent);
                    finish();

                }
                else {
                    Toast.makeText(getApplicationContext(), " client Not deleted: ", Toast.LENGTH_SHORT).show();
                    db.close();
                    dialog.dismiss();
                }
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



    public void showDialog()
    {
        final Dialog dialog = new Dialog(Update_Client.this);
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


    public void back(View v)
    {
        Intent ed = new Intent(getApplicationContext(), All_Clients.class);
        startActivity(ed);
        finish();
    }


    public void onBackPressed()
    {
        Intent ed = new Intent(getApplicationContext(),All_Clients.class);
        startActivity(ed);
        finish();

    }
}