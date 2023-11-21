package com.example.pakistanlawyerdiary.Lawyer;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pakistanlawyerdiary.Database.DatabaseContract;
import com.example.pakistanlawyerdiary.Database.DatabaseHelper;
import com.example.pakistanlawyerdiary.Lawyer.manager.NetworkManager;
import com.example.pakistanlawyerdiary.Model.CaseData;
import com.example.pakistanlawyerdiary.Model.HistoryData;
import com.example.pakistanlawyerdiary.Model.PartyData;
import com.example.pakistanlawyerdiary.Model.TypeCourtData;
import com.example.pakistanlawyerdiary.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Restore_Options extends AppCompatActivity {
    String cnum;
    RelativeLayout rel2, rel3, rel4, rel1, rel5;
    FirebaseAuth fauth;
    FirebaseDatabase firebasedatabase;
    DatabaseReference reference;
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    NetworkManager networkManager = new NetworkManager();
    final ArrayList<PartyData> clientarray = new ArrayList<>();
    final ArrayList<TypeCourtData> casetypes = new ArrayList<>();
    final ArrayList<TypeCourtData> courtname = new ArrayList<>();
    final ArrayList<CaseData> cases = new ArrayList<CaseData>();
    final ArrayList<HistoryData> histories = new ArrayList<HistoryData>();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore__options);

        rel1 = findViewById(R.id.rel1);
        rel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchCase();

            }
        });

        rel2 = findViewById(R.id.relativerestore2);
        dbHelper = new DatabaseHelper(this);
        rel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchClient();
            }
        });

        rel3 = findViewById(R.id.rel3);
        rel3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchCourtName();
            }
        });

        rel4 = findViewById(R.id.rel4);
        rel4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fetchCaseType();
            }
        });
        rel5 = findViewById(R.id.rel5);
        rel5.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fetchCaseHistory();
                    }
                });
    }


    /////////////////****************** Fetching data from Server**********************************//////////////////


    public void fetchCase() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait....");
        progressDialog.show();
        String userid;
        if (networkManager.checkInternetConnection(Restore_Options.this)) {
            fauth = FirebaseAuth.getInstance();
            firebasedatabase = FirebaseDatabase.getInstance();
            reference = FirebaseDatabase.getInstance().getReference("Case");
            userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            // single value event listener for retriving data one time
            reference.orderByChild("u_id").equalTo(userid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    if (snapshot.exists()) {
                        for (DataSnapshot child :
                                snapshot.getChildren()) {
                            CaseData c = child.getValue(CaseData.class);
                            String title = c.getCasetitle();
                            String court = c.getCourtname();
                            String type = c.getCasetype();
                            String cno = c.getNo();
                            String year = c.getCaseYear();
                            String obo = c.getOnbehalf();
                            String party = c.getParty();
                            String contact = c.getPartyContact();
                            String advocate = c.getAdvocateName();
                            String advocate_no = c.getAdvocateNum();
                            String res = c.getRespondentName();
                            String fus = c.getFieldUnSection();
                            String status = c.getStatus();
                            String uID = c.getU_id();
                            String meetingDate = c.getMeetingDate();
                            String meetingTime = c.getMeetingTime();
                            String email = c.getEmail();
                            String editkey=c.getEditkey();
                            CaseData c1 = new CaseData(title, court, type, cno, year, obo, party, contact, advocate, advocate_no, res, fus,
                                    status, email, meetingDate, meetingTime, uID,editkey);
                            cases.add(c1);
                            cnum = cno;
                            // Toast.makeText(Restore_Options.this, "CASE FETCHED", Toast.LENGTH_SHORT).show();


                        }

                    }
                    progressDialog.dismiss();
                    insertCaseInSqlite();


                    // Toast.makeText(Restore_Options.this, "CAlling CAseHIstory Function", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });



        } else {
            Toast.makeText(this,
                    "No Interent Connection", Toast.LENGTH_SHORT).show();
        }


    }


    public void fetchClient() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait....");
        progressDialog.show();
        final String userid;
        if (networkManager.checkInternetConnection(Restore_Options.this)) {
            fauth = FirebaseAuth.getInstance();
            firebasedatabase = FirebaseDatabase.getInstance();
            reference = FirebaseDatabase.getInstance().getReference("ManualClient");
            userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            reference.orderByChild("uid").equalTo(userid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.exists()) {
                        for (DataSnapshot child :
                                snapshot.getChildren()) {
                            PartyData c = child.getValue( PartyData.class);
                            String c_name = c.getClientName();
                            String phone = c.getPhone();
                            String c_email = c.getClientemail();
                            String address = c.getAddress();
                            String lemail = c.getEmail();
                            String uid = c.getUid();
                            String editkey=c.getEditKey();
                            PartyData c1 = new PartyData(c_name, phone, c_email, address, lemail, uid,editkey);
                            clientarray.add(c1);
                        }
                    }
                    progressDialog.dismiss();
                    insertClientDataInSqlite();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } else {
            Toast.makeText(this, "No Interent Connection", Toast.LENGTH_SHORT).show();
        }


    }


    public void fetchCourtName() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait....");
        progressDialog.show();
        String userid;
        if (networkManager.checkInternetConnection(Restore_Options.this)) {
            fauth = FirebaseAuth.getInstance();
            firebasedatabase = FirebaseDatabase.getInstance();
            reference = FirebaseDatabase.getInstance().getReference("CourtName");
            userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
             reference.orderByChild("uid").equalTo(userid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.exists()) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            TypeCourtData c = child.getValue(TypeCourtData.class);
                            String cn = c.getName();
                            String email = c.getEmail();
                            String ID = c.getUid();
                            TypeCourtData c1 = new TypeCourtData();
                            c1.saveData(cn, email, ID);
                            courtname.add(c1);


                        }
                    }
                    progressDialog.dismiss();
                    insertCourtNameInSqlite();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });
        } else {
            Toast.makeText(this, "No Interent Connection", Toast.LENGTH_SHORT).show();
        }


    }


    public void fetchCaseType() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        String userid;
        if (networkManager.checkInternetConnection(Restore_Options.this)) {
            fauth = FirebaseAuth.getInstance();
            firebasedatabase = FirebaseDatabase.getInstance();
            reference = FirebaseDatabase.getInstance().getReference("CaseType");
            userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
             reference.orderByChild("uid").equalTo(userid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.exists()) {
                        for (DataSnapshot child :
                                snapshot.getChildren()) {
                            TypeCourtData c = child.getValue(TypeCourtData.class);
                            String email = c.getEmail();
                            String casetype = c.getName();
                            String ID = c.getUid();
                            TypeCourtData c1 = new TypeCourtData();
                            c1.saveData(casetype, email, ID);
                            casetypes.add(c1);
                        }
                    }

                    progressDialog.dismiss();
                    insertCaseTypeInSqlite();


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });
        }
        else
        {

            Toast.makeText(this, "No Interent Connection", Toast.LENGTH_SHORT).show();
        }


    }



    public void  fetchCaseHistory() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait....");
        progressDialog.show();
        String userid;

        if (networkManager.checkInternetConnection(Restore_Options.this)) {
            fauth = FirebaseAuth.getInstance();
            firebasedatabase = FirebaseDatabase.getInstance();
            reference = FirebaseDatabase.getInstance().getReference("CaseHistory");
            userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            reference.orderByChild("uid").equalTo(userid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    if (snapshot.exists()) {
                        for (DataSnapshot child :snapshot.getChildren())
                        {
                            HistoryData c = child.getValue(HistoryData.class);
                            String cid = c.getCasenumber();
                            String pd = c.getPreviousdate();
                            String ad = c.getAdjourndate();
                            String step = c.getStep();
                            String adTime = c.getHiringTime();
                            String uid = c.getUid();
                            String Lawyeremail=c.getLawyer_email();

                            HistoryData c1 = new HistoryData(cid, pd, ad, step, adTime, uid,Lawyeremail);
                            histories.add(c1);


                        }

                    }
                    progressDialog.dismiss();
                    insertCaseHistoryInSqlite();


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });
        }
        else
        {

            Toast.makeText(this, "No Interent Connection", Toast.LENGTH_SHORT).show();
        }



    }











    /////////////////****************** Inserting Data in Sqlite**********************************//////////////////


    public void insertCaseInSqlite() {
        /*progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait....");
        progressDialog.show();*/
        db = dbHelper.getWritableDatabase();
        db.close();
        db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM " + DatabaseContract.CASE.TABLE_NAME);
        for (int i = 0; i < cases.size(); i++) {


            String t = cases.get(i).getCasetitle();
            String cn = cases.get(i).getCourtname();
            String ct = cases.get(i).getCasetype();
            String no = cases.get(i).getNo();
            String y = cases.get(i).getCaseYear();
            String obo = cases.get(i).getOnbehalf();
            String p = cases.get(i).getParty();
            String con = cases.get(i).getPartyContact();
            String adname = cases.get(i).getAdvocateName();
            String adnum = cases.get(i).getAdvocateNum();
            String res = cases.get(i).getRespondentName();
            String fus = cases.get(i).getFieldUnSection();
            String status = cases.get(i).getStatus();
            String mdate = cases.get(i).getMeetingDate();
            String mtime = cases.get(i).getMeetingTime();
            String email = cases.get(i).getEmail();
            String uid = cases.get(i).getU_id();
            String editkey=cases.get(i).getEditkey();

            ContentValues values = new ContentValues();
            values.put(DatabaseContract.CASE.Col_CaseTitle, t);
            values.put(DatabaseContract.CASE.Col_CourtName, cn);
            values.put(DatabaseContract.CASE.Col_CaseType, ct);
            values.put(DatabaseContract.CASE.Col_CaseNumber, no);
            values.put(DatabaseContract.CASE.Col_CaseYear, y);
            values.put(DatabaseContract.CASE.Col_OnBehalfOf, obo);
            values.put(DatabaseContract.CASE.Col_PartyName, p);
            values.put(DatabaseContract.CASE.Col_ContactNumber, con);
            values.put(DatabaseContract.CASE.Col_PartyAdvocateName, adname);
            values.put(DatabaseContract.CASE.Col_AdvocateContactNumber, adnum);
            values.put(DatabaseContract.CASE.Col_RespondantName, res);
            values.put(DatabaseContract.CASE.Col_FiledUnderSection, fus);
            values.put(DatabaseContract.CASE.Col_Status, status);
            values.put(DatabaseContract.CASE.Col_Email, email);
            values.put(DatabaseContract.CASE.Col_MeetingDate, mdate);
            values.put(DatabaseContract.CASE.Col_MeetingTime, mtime);
            values.put(DatabaseContract.CASE.Col_isLive, "1");
            values.put(DatabaseContract.CASE.Col_EditKey,editkey);

            long newRowId = db.insert(DatabaseContract.CASE.TABLE_NAME, null, values);


            values.clear();
        }

        db.close();
        Toast.makeText(getApplicationContext(), "Cases has been restored successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), All_Cases.class);
        startActivity(intent);
        finish();
       /* final Dialog dialog = new Dialog(Restore_Options.this);
        dialog.setContentView(R.layout.custom_alert);
        dialog.setCancelable(false);
        TextView alertt = dialog.findViewById(R.id.alerttext);
        alertt.setText("Cases has been restored successfully");
        Button ok = dialog.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

                Intent intent = new Intent(getApplicationContext(), All_Cases.class);
                startActivity(intent);
                finish();
            }
        });
        dialog.show();*/
        //Toast.makeText(getApplicationContext(), "CASE DATA RESTORED " + newRowId, Toast.LENGTH_SHORT).show();
        // fetchCaseHistory();

    }


    public void insertClientDataInSqlite() {


        db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM " + DatabaseContract.CLIENT.TABLE_NAME);
        // db.delete(DatabaseContract.CLIENT.TABLE_NAME, null,null);
        db.close();
        db = dbHelper.getWritableDatabase();
        for (int i = 0; i < clientarray.size(); i++) {

            String c_name = clientarray.get(i).getClientName();
            String c_email = clientarray.get(i).getClientemail();
            String c_phone = clientarray.get(i).getPhone();
            String c_address = clientarray.get(i).getAddress();
            String email = clientarray.get(i).getEmail();
            String editkey=clientarray.get(i).getEditKey();
            ContentValues values = new ContentValues();
            values.put(DatabaseContract.CLIENT.Col_Name, c_name);
            values.put(DatabaseContract.CLIENT.Col_Email, c_email);
            values.put(DatabaseContract.CLIENT.Col_Phone, c_phone);
            values.put(DatabaseContract.CLIENT.Col_Address, c_address);
            values.put(DatabaseContract.CLIENT.Col_LawyerEmail, email);
            values.put(DatabaseContract.CLIENT.Col_isLive, "1");
            values.put(DatabaseContract.CLIENT.Col_EditKey, editkey);

            //Toast.makeText(this, c_name + c_email + c_phone + c_address + email, Toast.LENGTH_SHORT).show();

            long newRowId = db.insert(DatabaseContract.CLIENT.TABLE_NAME, null, values);
            values.clear();
        }
        db.close();
        Toast.makeText(getApplicationContext(), "Clients data has been restored successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), All_Clients.class);
        startActivity(intent);
        finish();

        //  progressDialog.dismiss();
        /*
        final Dialog dialog = new Dialog(Restore_Options.this);
        dialog.setContentView(R.layout.custom_alert);
        dialog.setCancelable(false);
        TextView alertt = dialog.findViewById(R.id.alerttext);
        alertt.setText("Clients data has been restored successfully");
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
        dialog.show();*/

    }


    public void insertCourtNameInSqlite() {


        db = dbHelper.getWritableDatabase();

        db.execSQL("DELETE FROM " + DatabaseContract.COURTNAMES.TABLE_NAME);
        db.close();
        db = dbHelper.getWritableDatabase();
        for (int i = 0; i < courtname.size(); i++) {
            String email = courtname.get(i).getEmail();
            String c = courtname.get(i).getName();
            ContentValues values = new ContentValues();
            values.put(DatabaseContract.COURTNAMES.Col_Email, email);
            values.put(DatabaseContract.COURTNAMES.Col_CourtName, c);
            values.put(DatabaseContract.COURTNAMES.Col_isLive, "1");


            long newRowId = db.insert(DatabaseContract.COURTNAMES.TABLE_NAME, null, values);


            values.clear();

        }
        db.close();
        Toast.makeText(getApplicationContext(), "Court Names has been restored successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), Court_Names.class);
        startActivity(intent);
        finish();
        /*final Dialog dialog = new Dialog(Restore_Options.this);
        dialog.setContentView(R.layout.custom_alert);
        dialog.setCancelable(false);
        TextView alertt = dialog.findViewById(R.id.alerttext);
        alertt.setText("Court Names has been restored successfully");
        Button ok = dialog.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

                Intent intent = new Intent(getApplicationContext(), Court_Names.class);
                startActivity(intent);
                finish();
            }
        });
        dialog.show();*/
    }


    public void insertCaseTypeInSqlite() {
       ;

        db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM " + DatabaseContract.CASETYPES.TABLE_NAME);
        db.close();
        db = dbHelper.getWritableDatabase();
        for (int i = 0; i < casetypes.size(); i++) {

            String email = casetypes.get(i).getEmail();
            String c = casetypes.get(i).getName();
            ContentValues values = new ContentValues();
            values.put(DatabaseContract.CASETYPES.Col_Email, email);
            values.put(DatabaseContract.CASETYPES.Col_CaseType, c);
            values.put(DatabaseContract.CASETYPES.Col_isLive, "1");
            long newRowId = db.insert(DatabaseContract.CASETYPES.TABLE_NAME, null, values);
            values.clear();
        }

        db.close();
        Toast.makeText(getApplicationContext(), "Case types has been restored successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), Case_Types.class);
        startActivity(intent);
        finish();
        /*final Dialog dialog = new Dialog(Restore_Options.this);
        dialog.setContentView(R.layout.custom_alert);
        dialog.setCancelable(false);
        TextView alertt = dialog.findViewById(R.id.alerttext);
        alertt.setText("Case types has been restored successfully");
        Button ok = dialog.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

                Intent intent = new Intent(getApplicationContext(), Case_Types.class);
                startActivity(intent);
                finish();
            }
        });
        dialog.show();*/
    }




    public void insertCaseHistoryInSqlite()
    {

        db = dbHelper.getWritableDatabase();
        db.close();
        db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM " + DatabaseContract.CASEHISTORY.TABLE_NAME);
        for (int i = 0; i < histories.size(); i++) {
            String cn=histories.get(i).getCasenumber();
            String pd=histories.get(i).getPreviousdate();
            String ad=histories.get(i).getAdjourndate();
            String s=histories.get(i).getStep();
            String ti= histories.get(i).getHiringTime();
            String lawyeremail=histories.get(i).getLawyer_email();
            ContentValues values = new ContentValues();
            values.put(DatabaseContract.CASEHISTORY.Col_CaseId, cn);
            values.put(DatabaseContract.CASEHISTORY.Col_Previousdate, pd);
            values.put(DatabaseContract.CASEHISTORY.Col_Adjourndate, ad);
            values.put(DatabaseContract.CASEHISTORY.Col_Step, s);
            values.put(DatabaseContract.CASEHISTORY.Col_HearingTime, ti);
            values.put(DatabaseContract.CASEHISTORY.Col_LawyerEmail, lawyeremail);
            values.put(DatabaseContract.CASEHISTORY.Col_isLive, "1");

            long newRowId = db.insert(DatabaseContract.CASEHISTORY.TABLE_NAME, null, values);

            values.clear();

        }
        db.close();
        Toast.makeText(getApplicationContext(), "Case's history has been restored successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), All_Cases.class);
        startActivity(intent);
        finish();
        /*final Dialog dialog = new Dialog(Restore_Options.this);
        dialog.setContentView(R.layout.custom_alert);
        dialog.setCancelable(false);
        TextView alertt = dialog.findViewById(R.id.alerttext);
        alertt.setText("Case's history has been restored successfully");
        Button ok = dialog.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                Intent intent = new Intent(getApplicationContext(), All_Cases.class);
                startActivity(intent);
                finish();
            }
        });
        dialog.show();*/


    }





    public void back(View v )
    {

        Intent intent = new Intent(getApplicationContext(), Settings.class);
        startActivity(intent);
        finish();


    }


    public void onBackPressed()
    {

        Intent i=  new Intent(getApplicationContext(),Settings.class);
        startActivity(i);
        finish();

    }

}

