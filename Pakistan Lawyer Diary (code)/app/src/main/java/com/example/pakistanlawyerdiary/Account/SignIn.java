package com.example.pakistanlawyerdiary.Account;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.pakistanlawyerdiary.Client.ClientMainActivity;
import com.example.pakistanlawyerdiary.Lawyer.MainActivity;
import com.example.pakistanlawyerdiary.Model.User;
import com.example.pakistanlawyerdiary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {
    EditText email;
    EditText password;
    String email1, password1;
    FirebaseAuth fauth;
    ProgressBar pb;
    String type="lawyer";
    FirebaseDatabase firebasedatabase;
    DatabaseReference reference;
    FirebaseUser user;
    ConstraintLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        fauth = FirebaseAuth.getInstance();
        pb = (ProgressBar) findViewById(R.id.progressbar);
        relativeLayout=findViewById(R.id.cl);
        firebasedatabase = FirebaseDatabase.getInstance();

    }

    public void signin(View v)


    {
        email1 = String.valueOf(email.getText()).trim();
        password1 = String.valueOf(password.getText()).trim();
        if (email1.isEmpty()) {
            email.setError("Email is required");
            email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email1).matches()) {
            email.setError("Please provide a valid email");
            email.requestFocus();
            return;
        }
        if (password1.isEmpty()) {
            password.setError("Password is required");
            password.requestFocus();
            return;

        }

        pb.setVisibility(View.VISIBLE);
        relativeLayout.setVisibility(View.INVISIBLE);

                    fauth.signInWithEmailAndPassword(email1, password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {


                                //if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified())

                                //{
                                    Query r = FirebaseDatabase.getInstance().getReference().child("Clients").orderByChild("email").equalTo(email1);
                                    r.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                            if (snapshot.exists()) {
                                                type = "Client";
                                                loadUserInfo();
                                                //pb.setVisibility(View.GONE);
                                                //relativeLayout.setVisibility(View.VISIBLE);
                                                Intent i = new Intent(getApplicationContext(), ClientMainActivity.class);
                                                startActivity(i);
                                                finish();
                                            } else {
                                                loadUserInfo();
                                                // pb.setVisibility(View.GONE);
                                                // relativeLayout.setVisibility(View.VISIBLE);
                                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                                startActivity(i);
                                                finish();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });







                               /* else {
                                    FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                                    FirebaseAuth.getInstance().signOut();
                                    pb.setVisibility(View.GONE);
                                    relativeLayout.setVisibility(View.VISIBLE);
                                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SignIn.this);
                                    builder.setMessage("Verifcation email has been sent Please verify your account and try again !");
                                    builder.setTitle("Account Verifcation!");
                                    builder.setCancelable(false);
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
                                            "Cancel", new DialogInterface
                                                    .OnClickListener() {

                                                @Override
                                                public void onClick(DialogInterface dialog,
                                                                    int which) {
                                                    dialog.cancel();
                                                }
                                            });
                                    android.app.AlertDialog alertDialog = builder.create();

                                    alertDialog.show();

                                }*/
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {



                            pb.setVisibility(View.GONE);
                            relativeLayout.setVisibility(View.VISIBLE);
                           //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            showErrorDialog(e.getMessage());
                        }
                    });




    }


    public void goToSignup(View v)
    {
        //Toast.makeText(getApplicationContext(), type, Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getApplicationContext(), SignUp.class);
        startActivity(i);
        finish();
    }


    public void resetPassword(View v)

    {

        Intent i = new Intent(getApplicationContext(), Reset_Password.class);
        startActivity(i);
        finish();
    }


    // load user info in shared preferences
    private void loadUserInfo() {
        if(type.equals("lawyer"))
        {
            reference = FirebaseDatabase.getInstance().getReference("Lawyers");
        }
        else
        {
            reference = FirebaseDatabase.getInstance().getReference("Clients");
        }

        user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
       // Toast.makeText(getApplicationContext(), uid, Toast.LENGTH_SHORT).show();
        reference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userprofile = snapshot.getValue(   User .class);
                if (userprofile != null) {
                    String fullname = userprofile.getFullname();
                    String email = userprofile.getEmail();
                    int t = userprofile.getUserType();
                    //Toast.makeText(getApplicationContext(), fullname, Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getApplicationContext(), email, Toast.LENGTH_SHORT).show();


                    /*String filePath = getApplicationContext().getFilesDir().getParent()+"/shared_prefs/PREFS.xml";
                    File deletePrefFile = new File(filePath );
                    deletePrefFile.delete();*/
                    SharedPreferences shrd = getSharedPreferences("userInfo", MODE_PRIVATE);// private mode means this file will only be used by this app
                    SharedPreferences.Editor editor = shrd.edit();
                    editor.putString("name", fullname);
                    editor.putString("email", email);
                    editor.putString("type", String.valueOf(t));
                    editor.apply();
                }
                else
                    {
                    //Toast.makeText(getApplicationContext(), "usr profile is null", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                //Toast.makeText(getApplicationContext(), " Something wrong", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void showErrorDialog(String s)
    {




        AlertDialog.Builder builder = new AlertDialog.Builder(SignIn.this);
        builder.setMessage(s);
        // builder.setTitle("Account Verifcation!");
        //builder.setCancelable(false);
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

    public void onBackPressed()

    {


        finish();
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        //FirebaseAuth.getInstance().signOut();
    }

}

