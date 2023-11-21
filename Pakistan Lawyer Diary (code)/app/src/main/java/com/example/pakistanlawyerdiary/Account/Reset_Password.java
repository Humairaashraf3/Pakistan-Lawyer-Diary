package com.example.pakistanlawyerdiary.Account;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pakistanlawyerdiary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Reset_Password extends AppCompatActivity {
    EditText email;
    ProgressBar pb;
    FirebaseAuth fauth;
    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset__password); email=(EditText)findViewById(R.id.email);
        pb=(ProgressBar)findViewById(R.id.progressbar);
        fauth=FirebaseAuth.getInstance();
    }

    public void resetPassword(View v)
    {

        String email1=email.getText().toString().trim();
        if(email1.isEmpty())
        {
            email.setError("Please enter email");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email1).matches()) {
            email.setError("Please provide a valid email");
            email.requestFocus();
            return;
        }
        pb.setVisibility(View.VISIBLE);
        fauth.sendPasswordResetEmail(email1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    pb.setVisibility(View.GONE);
                   // Toast.makeText(getApplicationContext(), "Reset Password Link has been sent to your email", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(Reset_Password.this);
                    builder.setMessage("Reset Password Link has been sent to your email.");
                   builder.setTitle("Reset Password!");
                    //builder.setCancelable(false);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {

                                    dialog.cancel();
                                    Intent i = new Intent(getApplicationContext(), SignIn.class);
                                    startActivity(i);
                                    finish();
                                }
                            });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    Intent i = new Intent(getApplicationContext(), SignIn.class);
                                    startActivity(i);
                                    finish();
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();


                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                pb.setVisibility(View.GONE);
                //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                showErrorDialog(e.getMessage());

            }
        });
    }




    private void showErrorDialog(String s)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(Reset_Password.this);
        builder.setMessage(s);
        builder.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.cancel();
                    }
                });
        builder.setNegativeButton(
                "Cancel",
                new DialogInterface
                        .OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public void goToSignin(View v)
    {

      Intent i=new Intent(getApplicationContext(), SignIn.class);
      startActivity(i);
        finish();

    }

    public void onBackPressed()
    {

        Intent i=new Intent(getApplicationContext(), SignIn.class);
        startActivity(i);
        finish();

    }
    protected void onDestroy()
    {
        super.onDestroy();
        FirebaseAuth.getInstance().signOut();
    }
}