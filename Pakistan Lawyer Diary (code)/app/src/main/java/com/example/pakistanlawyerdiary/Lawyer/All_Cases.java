package com.example.pakistanlawyerdiary.Lawyer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pakistanlawyerdiary.Adapter.CustomCaseList;
import com.example.pakistanlawyerdiary.Database.DatabaseContract;
import com.example.pakistanlawyerdiary.Database.DatabaseHelper;
import com.example.pakistanlawyerdiary.Model.CaseData;
import com.example.pakistanlawyerdiary.R;

import java.util.ArrayList;

public class All_Cases extends AppCompatActivity {

    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    SQLiteDatabase db1;
    String email;
    Cursor cursor;
    Activity activity;
    ListView lv;
    SearchView sv;
    CustomCaseList ccl;
    ImageView imageView;
    TextView nodata;
  String casenumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all__cases);
        getEmail();
        activity = this;
        imageView = findViewById(R.id.img1);
        nodata = findViewById(R.id.nodata);
        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getReadableDatabase();
        db1 = dbHelper.getReadableDatabase();
        lv = (ListView) findViewById(R.id.list_view);
        sv = (SearchView) findViewById(R.id.search_view);
        final ArrayList<CaseData> cd = new ArrayList<CaseData>();


        String[] columns = {DatabaseContract.CASE._ID, DatabaseContract.CASE.Col_CaseTitle,
                DatabaseContract.CASE.Col_CourtName, DatabaseContract.CASE.Col_CaseType, DatabaseContract.CASE.Col_CaseNumber,
                DatabaseContract.CASE.Col_CaseYear, DatabaseContract.CASE.Col_OnBehalfOf, DatabaseContract.CASE.Col_PartyName,
                DatabaseContract.CASE.Col_Status
        };
        String[] columns1 = {DatabaseContract.CASEHISTORY._ID, DatabaseContract.CASEHISTORY.Col_CaseId,
                DatabaseContract.CASEHISTORY.Col_Previousdate, DatabaseContract.CASEHISTORY.Col_Adjourndate,
                DatabaseContract.CASEHISTORY.Col_Step
        };

        // String sortOrder = DatabaseContract.ADDCASE._ID + " ASC";
        cursor = db.query(DatabaseContract.CASE.TABLE_NAME, columns, DatabaseContract.CASE.Col_Email + "=?" + " AND " +
                        DatabaseContract.CASE.Col_Status + "=?"
                , new String[]{email, "open"}, null, null, null, null);

        if (cursor.getCount() == 0) {
            db.close();
            //Toast.makeText(this, "No Data To Display ", Toast.LENGTH_SHORT).show();
            //lv.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            nodata.setVisibility(View.VISIBLE);


        }

        else {
            while (cursor.moveToNext()) {

                    String pd = "";
                    String ad = "";
                    String step = "";

                    int id = cursor.getInt(0);
                    String t = cursor.getString(1);
                    String cn = cursor.getString(2);
                    String ct = cursor.getString(3);
                    String caseno = String.valueOf(cursor.getString(4));
                    casenumber = caseno;
                    String obo = cursor.getString(6);
                    String pn = cursor.getString(7);
                    String query = "SELECT * FROM casehistory WHERE casehistory.CaseId = ? AND casehistory.LawyerEmail =?" ;
                    db1 = dbHelper.getReadableDatabase();
                    Cursor historycursor = db1.rawQuery(query, new String[]{casenumber,email});
                    try {
                        if (historycursor.getCount()>0)
                        {
                            historycursor.moveToLast();
                                    pd = historycursor.getString(2);
                                    ad = historycursor.getString(3);
                                    step = historycursor.getString(4);

                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }


                    CaseData Obj = new CaseData(id, t, cn, ct, caseno, obo, pn, pd, ad, step);
                    cd.add(Obj);

                }



        cursor.close();
        db.close();


        ccl = new CustomCaseList(activity, cd);
        lv.setAdapter(ccl);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                String cid = String.valueOf(cd.get(position).getId());
                // Toast.makeText(getApplicationContext(), cid, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), Case_Details.class);
                intent.putExtra("id", cid);
                intent.putExtra("t", "all");
                startActivity(intent);
                finish();

            }

        });
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final ArrayList<CaseData> results = new ArrayList<CaseData>();
                for (CaseData x : cd) {
                    if (x.getCasetitle().contains(newText) || x.getCasetype().contains(newText) || x.getParty().contains(newText)) {
                        results.add(x);
                    }
                }
                if (results.size() > 0) {
                    ((CustomCaseList) lv.getAdapter()).update(results);// refresh list view
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                            String cid = String.valueOf(results.get(position).getId());
                           // Toast.makeText(getApplicationContext(), cid, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), Case_Details.class);
                            intent.putExtra("id", cid);
                            startActivity(intent);
                            finish();

                        }

                    });
                }
                return false;
            }
        });
    }

        }







    private  void getEmail()

    {

        SharedPreferences getShared=getSharedPreferences("userInfo",MODE_PRIVATE);
        email=getShared.getString("email","Email");
    }


    public void help(View v)


    {
       /* final Dialog dialog = new Dialog(All_Cases.this);
        dialog.setContentView(R.layout.help_dialog);
        TextView text=dialog.findViewById(R.id.text);
        text.setText("1. Scroll vertically to see all the cases." +
                "\n\n" +
                "2. Tap on any case to see details of that case.\n\n"
                +
                "2. Tap on add sign button to add new case."
        );

        ImageView cancel=dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             dialog.dismiss();
            }
        });
        dialog.show();*/



        /*SpannableString ss = new SpannableString("abc");
        Drawable d = ContextCompat.getDrawable(this, R.drawable.delete);
        d.setBounds(0, 0, 40, 40);
        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
        ss.setSpan(span, ss.length()-1, ss.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);*/


        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(All_Cases.this);
        View itemView = getLayoutInflater().inflate(R.layout.help_dialog,null);

        builder.setView(itemView);
        TextView text= itemView.findViewById(R.id.text);
        SpannableStringBuilder ssb=new SpannableStringBuilder
        ("** Scroll vertically to view all cases." +
                "\n\n" +
                "** Case number is generated by app which is unique for each case.\n\n"
                +
                "** Tap on any case to view details of that case.\n\n"
                +
                "** You can search case with any of these case data(Title,Type,Party).\n\n"
                +"** Press  "
        );
        ssb.setSpan(new ImageSpan(this,R.drawable.black_add_icon),ssb.length()-1,ssb.length(), 0);
        ssb.append(" button to enter new case details.");
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

    public void main(View v )
    {

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();


    }


    public void addCase(View v )
    {

        Intent intent = new Intent(getApplicationContext(), Add_Case.class);
        startActivity(intent);
        finish();

    }

    public void onBackPressed()
    {

        Intent i=  new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
        finish();

    }
}