package com.example.pakistanlawyerdiary.Lawyer.Reports;

import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pakistanlawyerdiary.Database.DatabaseHelper;
import com.example.pakistanlawyerdiary.Lawyer.Settings;
import com.example.pakistanlawyerdiary.R;

public class Report extends AppCompatActivity {
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
    }


    public void adverseadvocate(View v )
    {

       showAdverseAdvocateDialog("Adverse Advocate");

    }

    public void particular_type(View v )
    {

        showTypeDialog(" Case Type");


    }



    public void particular_client(View v )
    {

        showClientDialog("Client Name");


    }

    public void particular_year(View v )
    {

        final Dialog dialog = new Dialog(Report.this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.case_type_input);
        final EditText year=dialog.findViewById(R.id.ed1);
        year.setHint("Enter Case Year");
        year.setInputType(InputType.TYPE_CLASS_NUMBER);
        TextView alerttext=dialog.findViewById(R.id.alerttext);
        alerttext.setText("Year");
        Button ok=dialog.findViewById(R.id.ok);
        Button cancel=dialog.findViewById(R.id.cancel);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String c = year.getText().toString();
                if (!c.isEmpty()) {
                    Intent i=new Intent(getApplicationContext(),Specific_Year_Cases.class);
                    i.putExtra("year",c);
                    startActivity(i);
                    dialog.dismiss();

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






    public void showTypeDialog( String s)
    {

        final Dialog dialog = new Dialog(Report.this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.case_type_input);
        final EditText type=dialog.findViewById(R.id.ed1);
        type.setHint("Enter Type");
        TextView alerttext=dialog.findViewById(R.id.alerttext);
        alerttext.setText(s);
        Button ok=dialog.findViewById(R.id.ok);
        Button cancel=dialog.findViewById(R.id.cancel);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String c = type.getText().toString();
                if (!c.isEmpty()) {
                    Intent i=new Intent(getApplicationContext(), Specific_Type_Cases.class);
                    i.putExtra("type",c);
                    startActivity(i);
                    dialog.dismiss();
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


    public void showClientDialog( String s)
    {

        final Dialog dialog = new Dialog(Report.this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.case_type_input);
        final EditText type=dialog.findViewById(R.id.ed1);
        type.setHint("Enter Client Name");
        TextView alerttext=dialog.findViewById(R.id.alerttext);
        alerttext.setText(s);
        Button ok=dialog.findViewById(R.id.ok);
        Button cancel=dialog.findViewById(R.id.cancel);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String c = type.getText().toString();
                if (!c.isEmpty()) {

                    Intent i=new Intent(getApplicationContext(), Specific_Client_Cases.class);
                    i.putExtra("client",c);
                    startActivity(i);
                    dialog.dismiss();
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





    public void showAdverseAdvocateDialog( String s)
    {
        final Dialog dialog = new Dialog(Report.this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.case_type_input);
        final EditText type=dialog.findViewById(R.id.ed1);
        type.setHint("Enter Adverse Advocate");
        TextView alerttext=dialog.findViewById(R.id.alerttext);
        alerttext.setText(s);
        Button ok=dialog.findViewById(R.id.ok);
        Button cancel=dialog.findViewById(R.id.cancel);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String c = type.getText().toString();
                if (!c.isEmpty()) {
                    Intent i=new Intent(getApplicationContext(),Specific_Advocate_Cases.class);
                    i.putExtra("advocate",c);
                    startActivity(i);
                    dialog.dismiss();
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







    public void help(View v)


    {
        final Dialog dialog = new Dialog(Report.this);
        dialog.setContentView(R.layout.help_dialog);
        TextView alertt=dialog.findViewById(R.id.text);
        alertt.setText("1. Generate report of cases of a particular client by tapping at cases of a particular client. " +
                "\n\n" +
                "2. Generate report of cases of a particular year by tapping at cases of a particular year." +
                "\n\n" +
                "3. Generate report of cases of a particular type by tapping at cases of a particular type." +
                "\n\n" +
                "4. Generate report of cases of a adverse advocate by tapping at adverse advocate Cases.");
        ImageView ok=dialog.findViewById(R.id.cancel);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
    public void back(View v )
    {

        Intent intent = new Intent(getApplicationContext(), Settings.class);
        startActivity(intent);
        finish();


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Settings.class);
        startActivity(intent);
        finish();

    }

}