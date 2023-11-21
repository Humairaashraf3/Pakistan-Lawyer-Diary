package com.example.pakistanlawyerdiary.Lawyer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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

import com.example.pakistanlawyerdiary.Adapter.PartyAdapter;
import com.example.pakistanlawyerdiary.Database.DatabaseContract;
import com.example.pakistanlawyerdiary.Database.DatabaseHelper;
import com.example.pakistanlawyerdiary.Model.PartyData;
import com.example.pakistanlawyerdiary.R;

import java.util.ArrayList;

public class All_Clients extends AppCompatActivity
{
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    Cursor cursor;
    ListView lv;
    SearchView sv;
    ImageView imageView;
    TextView nodata;
    String email;
    PartyAdapter adapter;
    Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all__clients);
        activity = this;
        getEmail();
        imageView = findViewById(R.id.img1);
        nodata = findViewById(R.id.nodata);
        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getReadableDatabase();
        lv = (ListView) findViewById(R.id.list_view);
        sv = (SearchView) findViewById(R.id.search_view);
        final ArrayList<PartyData> pd = new ArrayList<PartyData>();

        String[] columns = {DatabaseContract.CLIENT._ID, DatabaseContract.CLIENT.Col_Name, DatabaseContract.CLIENT.Col_Phone,
                DatabaseContract.CLIENT.Col_LawyerEmail};
        cursor = db.query(DatabaseContract.CLIENT.TABLE_NAME, columns,
                DatabaseContract.CLIENT.Col_LawyerEmail + "=?", new String[]{email}, null, null, null, null);

        if (cursor.getCount() == 0) {
            db.close();
            //Toast.makeText(this, "No Data To Display ", Toast.LENGTH_SHORT).show();
            imageView.setVisibility(View.VISIBLE);
            nodata.setVisibility(View.VISIBLE);


        }

        else {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String phn = cursor.getString(2);
                PartyData Obj = new PartyData(id, name, phn);
                pd.add(Obj);
            }

            adapter = new PartyAdapter(activity, pd);
            lv.setAdapter(adapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                    Intent intent = new Intent(getApplicationContext(), Update_Client.class);
                    intent.putExtra("id", String.valueOf(pd.get(position).getId()));
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
                    final ArrayList<PartyData> results = new ArrayList<PartyData>();
                    for (PartyData x : pd) {
                        if (x.getClientName().contains(newText)) {
                            results.add(x);
                        }
                    }
                    if (results.size() > 0) {
                        ((PartyAdapter) lv.getAdapter()).update(results);   // refresh list view
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                                String cid = String.valueOf(results.get(position).getId());
                                //Toast.makeText(getApplicationContext(), cid, Toast.LENGTH_SHORT).show();
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

    public void help(View v)


    {


        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(All_Clients.this);
        View itemView = getLayoutInflater().inflate(R.layout.help_dialog,null);

        builder.setView(itemView);
        TextView text= itemView.findViewById(R.id.text);
        SpannableStringBuilder ssb=new SpannableStringBuilder
                ("** Scroll vertically to view all clients." +
                        "\n\n" +
                        "** Tap on any client to view and edit details of that client.\n\n"
                        +
                        "** You can search client with name.\n\n"
                        +"** Press "
                );
        ssb.setSpan(new ImageSpan(this,R.drawable.black_add_icon),ssb.length()-1,ssb.length(), 0);
        ssb.append(" button to enter new client details.");
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

    public void addClient(View v)


    {
        Intent intent = new Intent(getApplicationContext(), Add_Client.class);
        startActivity(intent);
        finish();
    }


    private  void getEmail()

    {

        SharedPreferences getShared=getSharedPreferences("userInfo",MODE_PRIVATE);
        email=getShared.getString("email","Email");
    }

    public void main(View v)

    {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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