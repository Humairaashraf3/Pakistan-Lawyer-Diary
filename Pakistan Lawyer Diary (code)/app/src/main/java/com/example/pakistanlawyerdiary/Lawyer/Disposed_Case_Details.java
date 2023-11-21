package com.example.pakistanlawyerdiary.Lawyer;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pakistanlawyerdiary.Database.DatabaseContract;
import com.example.pakistanlawyerdiary.Database.DatabaseHelper;
import com.example.pakistanlawyerdiary.Lawyer.manager.NetworkManager;
import com.example.pakistanlawyerdiary.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Disposed_Case_Details extends AppCompatActivity {

    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    SQLiteDatabase db1;
    Cursor cursor;
    String Id;
    TextView casetId, casetitle,courtname,casetype,casenumber, caseyear,onbehalfof,partyname,contactnum,
            partyadvocate,advocatecontactnum, respondantname,undersection,adjorrndate,step;
    String email;
    String casenum;
    String isLive="";
    String EditKey="";
    NetworkManager networkManager = new NetworkManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disposed__case__details);
        Intent i = getIntent();
        Id= i.getStringExtra("id");
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
        adjorrndate=(TextView)findViewById(R.id.adj_date) ;
        step=(TextView)findViewById(R.id.steps);
        dbHelper = new DatabaseHelper(this);
        db=dbHelper.getReadableDatabase();
        db1=dbHelper.getReadableDatabase();
        String[] columns={DatabaseContract.CASE._ID,DatabaseContract.CASE.Col_CaseTitle,
                DatabaseContract.CASE.Col_CourtName,DatabaseContract.CASE.Col_CaseType,DatabaseContract.CASE.Col_CaseNumber,
                DatabaseContract.CASE.Col_CaseYear, DatabaseContract.CASE.Col_OnBehalfOf, DatabaseContract.CASE.Col_PartyName,
                DatabaseContract.CASE.Col_ContactNumber, DatabaseContract.CASE.Col_PartyAdvocateName, DatabaseContract.CASE.Col_AdvocateContactNumber,
                DatabaseContract.CASE.Col_RespondantName, DatabaseContract.CASE.Col_FiledUnderSection,
                DatabaseContract.CASE.Col_Email,DatabaseContract.CASE.Col_isLive,DatabaseContract.CASE.Col_EditKey
        };

        cursor = db.query(DatabaseContract.CASE.TABLE_NAME, columns,
                DatabaseContract.CASE._ID + "=?" , new String[]{String.valueOf(Id)},
                null, null, null, null);

        cursor.moveToNext();
        String pd="";
        String ad="";
        String s="";
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
        email= cursor.getString(13);
        isLive=cursor.getString(14);
        if(isLive.equals("1"))
        {
            EditKey = cursor.getString(15);
        }
        String query="SELECT * FROM casehistory WHERE casehistory.CaseId = ? AND casehistory.LawyerEmail =?";
        db1 = dbHelper.getReadableDatabase();
        Cursor historycursor=db1.rawQuery(query,new String[]{caseno,email});
        try{
            if(historycursor.getCount()>0)
            {
                historycursor.moveToLast();
                ad = historycursor.getString(3);
                s = historycursor.getString(4);
            }
            }
        catch (SQLException ex){
            ex.printStackTrace();
        }


        casetId.setText("  "+String.valueOf(id));
        casetitle.setText("  "+t) ;
        courtname.setText("  "+cn) ;
        casetype.setText("  "+ct) ;
        casenumber.setText("  "+caseno) ;
        caseyear.setText("  "+cyear) ;
        onbehalfof.setText("  "+obo) ;
        partyname.setText("  "+pn) ;
        contactnum.setText(pnum) ;
        partyadvocate.setText(adverse) ;
        advocatecontactnum.setText(adverseno) ;
        respondantname.setText("  "+respo) ;
        undersection.setText("  "+usec) ;
        adjorrndate.setText("  "+ad) ;
        step.setText("  "+s) ;


       // c1.close();


        db1.close();

        db.close();
        cursor.close();

    }










    public void reopenCase(View v)
    {



        db = dbHelper.getWritableDatabase();
        final Dialog dialog = new Dialog(Disposed_Case_Details.this);
        dialog.setContentView(R.layout.custom_main_alert);
        TextView alertt=dialog.findViewById(R.id.alerttext);
        alertt.setText("Do you really want to reopen this case ?");
        Button ok=dialog.findViewById(R.id.yes);
        Button no=dialog.findViewById(R.id.no);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                if (networkManager.checkInternetConnection(Disposed_Case_Details.this)) {
                    ContentValues values = new ContentValues();
                    values.put(DatabaseContract.CASE.Col_Status, "open");
                    Integer i = db.update(DatabaseContract.CASE.TABLE_NAME, values, DatabaseContract.CASE._ID + "=?", new String[]{Id});
                    if (i > 0)
                    {
   if(isLive.equals("1"))
{
    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Case").child(EditKey);
    HashMap<String, Object> hashMap = new HashMap<>();
    hashMap.put("status", "open");
    reference1.updateChildren(hashMap);

    ContentValues contentValues = new ContentValues();
    contentValues.put(DatabaseContract.CASE.Col_isEditLive, "1");
    db.update(DatabaseContract.CASE.TABLE_NAME, contentValues,
            DatabaseContract.CASE._ID + "=?", new String[]{Id});
}

                        final Dialog dialog = new Dialog(Disposed_Case_Details.this);
                        dialog.setContentView(R.layout.custom_alert);
                        dialog.setCancelable(false);
                        TextView alertt = dialog.findViewById(R.id.alerttext);
                        alertt.setText("Case has been reopened please see in Cases");
                        Button ok = dialog.findViewById(R.id.ok);
                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(), All_Cases.class);
                                startActivity(intent);
                                finish();
                                dialog.dismiss();
                            }
                        });
                        dialog.show();

                    }
                    db.close();
                    dialog.dismiss();

                }





                else
                {

                    ContentValues values = new ContentValues();
                    values.put(DatabaseContract.CASE.Col_Status, "open");
                    if(isLive.equals("1"))
                    {
                        values.put(DatabaseContract.CASE.Col_isEditLive, "0");
                    }
                    Integer i = db.update(DatabaseContract.CASE.TABLE_NAME, values, DatabaseContract.CASE._ID + "=?", new String[]{Id});
                    if (i > 0)
                    {


                        final Dialog dialog = new Dialog(Disposed_Case_Details.this);
                        dialog.setContentView(R.layout.custom_alert);
                        dialog.setCancelable(false);
                        TextView alertt = dialog.findViewById(R.id.alerttext);
                        alertt.setText("Case has been reopened please see in Cases");
                        Button ok = dialog.findViewById(R.id.ok);
                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(), All_Cases.class);
                                startActivity(intent);
                                finish();
                                dialog.dismiss();
                            }
                        });
                        dialog.show();

                    }
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


    public void deleteCase(View v)
    {

        db = dbHelper.getWritableDatabase();
        final Dialog dialog = new Dialog(Disposed_Case_Details.this);
        dialog.setContentView(R.layout.custom_main_alert);
        TextView alertt=dialog.findViewById(R.id.alerttext);
        alertt.setText("Do you really want to delete this case ?");
        Button ok=dialog.findViewById(R.id.yes);
        Button no=dialog.findViewById(R.id.no);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Integer i1= db.delete(DatabaseContract.CASE.TABLE_NAME, DatabaseContract.CASE._ID +"=?",new String[] {String.valueOf(Id)});
                if (i1 > 0)
                {
                    Integer i= db.delete(DatabaseContract.CASEHISTORY.TABLE_NAME, DatabaseContract.CASEHISTORY.Col_CaseId +"=?  AND "+DatabaseContract.CASEHISTORY.Col_LawyerEmail+ " =?",new String[] {casenum,email});
                    if(i>0)
                    {
                        //Toast.makeText(getApplicationContext(), " hostory also deleted: " , Toast.LENGTH_SHORT).show();
                    }
                    db.close();
                    dialog.dismiss();
                   // Toast.makeText(getApplicationContext(), " case deleted: " , Toast.LENGTH_SHORT).show();
                    Intent ed = new Intent(getApplicationContext(), Disposed_Cases.class);
                    startActivity(ed);
                    finish();

                }
                //Toast.makeText(getApplicationContext(), " case Not deleted: " , Toast.LENGTH_SHORT).show();
                db.close();
                dialog.dismiss();
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




    public void help(View v)
    {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Disposed_Case_Details.this);
        View itemView = getLayoutInflater().inflate(R.layout.help_dialog,null);

        builder.setView(itemView);
        TextView text= itemView.findViewById(R.id.text);
        SpannableStringBuilder ssb=new SpannableStringBuilder
                ("** Press  "
                );
        ssb.setSpan(new ImageSpan(this,R.drawable.black_reopen),ssb.length()-1,ssb.length(), 0);
        ssb.append(" icon to reopen case.\n\n** Press  ");
        ssb.setSpan(new ImageSpan(this,R.drawable.black_delete_icon),ssb.length()-1,ssb.length(), 0);
        ssb.append(" icon to delete case permanently.");
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



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent i=new Intent(this,Disposed_Cases.class);
        if(keyCode==event.KEYCODE_BACK)
            startActivity(i);
        finish();
        return false;
    }
    public void onBackPressed()
    {

        Intent i=new Intent(this,Disposed_Cases.class);
        startActivity(i);
        finish();
    }

    public void back(View v)
{
    Intent i=new Intent(this,Disposed_Cases.class);
    startActivity(i);
    finish();
}
}