package com.example.pakistanlawyerdiary.Lawyer;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pakistanlawyerdiary.Adapter.TypeCourtAdapter;
import com.example.pakistanlawyerdiary.Database.DatabaseContract;
import com.example.pakistanlawyerdiary.Database.DatabaseHelper;
import com.example.pakistanlawyerdiary.Lawyer.manager.NetworkManager;
import com.example.pakistanlawyerdiary.Model.TypeCourtData;
import com.example.pakistanlawyerdiary.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Case_Types extends AppCompatActivity {


    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    String email;
    ListView lv;
    ImageView img1;
    TextView nodata;
    TypeCourtAdapter tca;
    Activity activity;
    FirebaseDatabase firebasedatabase;
    DatabaseReference reference;
    FirebaseAuth fauth;
    String uid;
    NetworkManager networkManager = new NetworkManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case__types);
        activity=this;
        getEmail();
        lv = (ListView) findViewById(R.id.list_view);
        img1=findViewById(R.id.img1);
        nodata=findViewById(R.id.nodata);
        final ArrayList<TypeCourtData> tcd=new ArrayList<TypeCourtData>();
        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getReadableDatabase();
        String[] columns = {DatabaseContract.CASETYPES._ID,  DatabaseContract.CASETYPES.Col_Email,DatabaseContract.CASETYPES.Col_CaseType};
        Cursor c = db.query(DatabaseContract.CASETYPES.TABLE_NAME, columns, DatabaseContract.COURTNAMES.Col_Email + "=?", new String[]{email} , null, null, null, null);


        if(c.getCount()>0)

        {

            while (c.moveToNext()) {

                String id = String.valueOf(c.getInt(0));
                String t = c.getString(2);
                TypeCourtData Obj = new TypeCourtData(id, t,"type");
                tcd.add(Obj);


            }
            c.close();
            db.close();
            tca = new TypeCourtAdapter(activity, tcd);
            lv.setAdapter(tca);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    //Toast.makeText(getApplicationContext(), types.get(position), Toast.LENGTH_SHORT).show();

                }
            });


        }
        else
        {
            // Toast.makeText(getApplicationContext(), "No Data To Display",Toast.LENGTH_SHORT).show();
            img1.setVisibility(View.VISIBLE);
            nodata.setVisibility(View.VISIBLE);
        }

    }







    public void addType(View v)
    {

try {
    showAddDialog();
}
catch(Exception e)
{

}

    }

    public void showAddDialog()
    {

        final Dialog dialog = new Dialog(Case_Types.this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.case_type_input);
        final EditText type=dialog.findViewById(R.id.ed1);
        Button ok=dialog.findViewById(R.id.ok);
        Button cancel=dialog.findViewById(R.id.cancel);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = FirebaseDatabase.getInstance().getReference("CaseType");
                uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String c = type.getText().toString();
                if (!c.isEmpty()) {

                    if (networkManager.checkInternetConnection(Case_Types.this))
                    {
                        db = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put(DatabaseContract.CASETYPES.Col_Email, email);
                        values.put(DatabaseContract.CASETYPES.Col_CaseType, c);
                        values.put(DatabaseContract.CASETYPES.Col_isLive, "1");

                        long newRowId = db.insert(DatabaseContract.CASETYPES.TABLE_NAME, null, values);


                        if (newRowId > 0) {

                            TypeCourtData ct=new TypeCourtData();
                            ct.saveData(c,email,uid);
                            reference.push().setValue(ct);
                            // Toast.makeText(getApplicationContext(), "New Case Type Inserted with internet " + newRowId, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            final Dialog d = new Dialog(Case_Types.this);
                            d.setContentView(R.layout.custom_alert);
                            d.setCancelable(false);
                            TextView alertt=d.findViewById(R.id.alerttext);
                            alertt.setText("Case Type Saved");
                            Button ok=d.findViewById(R.id.ok);
                            ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    db.close();
                                    d.dismiss();
                                    recreate();
                                    // Toast.makeText(getApplicationContext(),"Dismissed..!!",Toast.LENGTH_SHORT).show();
                                }
                            });
                            d.show();

                            db.close();
                            values.clear();






                        }
                        else {

                            Toast.makeText(getApplicationContext(), "New Case Type Not Inserted:", Toast.LENGTH_SHORT).show();
                        }
                    }

                    else
                    {
                        db = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put(DatabaseContract.CASETYPES.Col_Email, email);
                        values.put(DatabaseContract.CASETYPES.Col_CaseType, c);
                        values.put(DatabaseContract.CASETYPES.Col_isLive, "0");


                        long newRowId = db.insert(DatabaseContract.CASETYPES.TABLE_NAME, null, values);


                        if (newRowId > 0)
                        {

                            // Toast.makeText(getApplicationContext(), "New Case Type Inserted without internet " + newRowId, Toast.LENGTH_SHORT).show();

                           dialog.dismiss();
                            final Dialog d = new Dialog(Case_Types.this);
                            d.setContentView(R.layout.custom_alert);
                            d.setCancelable(false);
                            TextView alertt=d.findViewById(R.id.alerttext);
                            alertt.setText("Case Type Saved");
                            Button ok=d.findViewById(R.id.ok);
                            ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    d.dismiss();
                                    db.close();
                                    recreate();
                                    // Toast.makeText(getApplicationContext(),"Dismissed..!!",Toast.LENGTH_SHORT).show();
                                }
                            });
                            d.show();
                            values.clear();
                            db.close();

                        } else
                            {

                            Toast.makeText(getApplicationContext(), "New Case Type Not Inserted ", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                else
                {
                    final Dialog dialog = new Dialog(Case_Types.this);
                    dialog.setContentView(R.layout.custom_alert);
                    dialog.setCancelable(false);
                    TextView alertt=dialog.findViewById(R.id.alerttext);
                    alertt.setText("Please enter case type");
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






    private  void getEmail()

    {

        SharedPreferences getShared=getSharedPreferences("userInfo",MODE_PRIVATE);
        email=getShared.getString("email","Email");
    }

    public void help(View v)
    {

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Case_Types.this);
        View itemView = getLayoutInflater().inflate(R.layout.help_dialog,null);

        builder.setView(itemView);
        TextView text= itemView.findViewById(R.id.text);
        SpannableStringBuilder ssb=new SpannableStringBuilder
                ("** You can keep only required case types." +
                        "\n\n" +
                        "**  Scroll vertically to view all case types.\n\n"
                        +"** Press  "
                );
        ssb.setSpan(new ImageSpan(this,R.drawable.black_add_icon),ssb.length()-1,ssb.length(), 0);
        ssb.append(" button to enter new case type.\n\n** Press  ");
        ssb.setSpan(new ImageSpan(this,R.drawable.black_delete_icon),ssb.length()-1,ssb.length(), 0);
        ssb.append(" icon to delete case type permanently.");
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


    public void back(View v)
    {
        Intent i=  new Intent(getApplicationContext(),Settings.class);
        startActivity(i);
        finish();

    }


    public void onBackPressed()
    {

        Intent i=  new Intent(getApplicationContext(),Settings.class);
        startActivity(i);
        finish();

    }

}