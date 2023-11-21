package com.example.pakistanlawyerdiary.Account;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pakistanlawyerdiary.Model.User;
import com.example.pakistanlawyerdiary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {
    EditText fullname;
    EditText email;
    EditText password;
   EditText repassword;
    EditText phone;
    EditText address;
    FirebaseAuth fauth;
    String fname, email1, password1, repassword1, phone1, address1;
    ProgressBar pb;
    FirebaseUser user;
    String type="lawyer";
    DatabaseReference reference;
    User l;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        fullname = (EditText) findViewById(R.id.full_name);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
      repassword = (EditText) findViewById(R.id.repassword);
        phone = (EditText) findViewById(R.id.phone_number);
        address = (EditText) findViewById(R.id.address);
        fauth = FirebaseAuth.getInstance();
        pb=(ProgressBar)findViewById(R.id.progressbar);
       scrollView=findViewById(R.id.sv);
    }
    public void signup(View v) {

        //Toast.makeText(getApplicationContext(), "Data Validated", Toast.LENGTH_SHORT).show();
        String regex="^[+]?[0-9]{10,13}$";
        fname = String.valueOf(fullname.getText()).trim();
        email1 = String.valueOf(email.getText()).trim();
        password1 = String.valueOf(password.getText()).trim();
        repassword1 = String.valueOf(repassword.getText()).trim();
        phone1 = String.valueOf(phone.getText()).trim();
        address1 = String.valueOf(address.getText()).trim();
        if (fname.isEmpty()) {
            fullname.setError("Full name is required");
            fullname.requestFocus();
            return;
        }
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
        if (password1.length() < 6) {
            password.setError("Provide a Password of minimum length 6");
            password.requestFocus();
            return;

        }
       if (repassword1.isEmpty()) {
            repassword.setError("Please confirm your password");
            repassword.requestFocus();
            return;
        }
        if (!password1.equals(repassword1)) {
            repassword.setError("Password Do Not Match");
            repassword.requestFocus();
            return;
        }

        if (phone1.isEmpty()) {
            phone.setError("Phone number is required");
            phone.requestFocus();
            return;
        }
        if (!phone1.matches(regex)) {
            phone.setError("Please enter a valid phone number");
            phone.requestFocus();
            return;
        }

        if (address1.isEmpty()) {
            address.setError("Office address is required");
            address.requestFocus();
            return;

        }


        //Toast.makeText(getApplicationContext(), "Data Validated", Toast.LENGTH_SHORT).show();
        if(type.equals("lawyer")) {
            pb.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.INVISIBLE);
            fauth.createUserWithEmailAndPassword(email1, password1)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {


                                String uid = task.getResult().getUser().getUid();


                                User  l = new   User (uid, fname, email1, password1, phone1, address1, 1, "", 0, 0,"","");

                                FirebaseDatabase.getInstance().getReference("Lawyers").child(FirebaseAuth.getInstance().getCurrentUser().getUid())// return cuurently registerd user id
                                        .setValue(l)
                                        .addOnCompleteListener(new OnCompleteListener<Void>()// check wether data has been inserted in database
                                        {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    user = fauth.getCurrentUser();


                                                    //send verifcation link

                                                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            pb.setVisibility(View.GONE);
                                                            scrollView.setVisibility(View.VISIBLE);
                                                            //Toast.makeText(getApplicationContext(), " User has been registered successfully ,Verifcation email has been sent please verify your account", Toast.LENGTH_SHORT).show();
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                                                            builder.setMessage("User has been registered successfully");
                                                            builder.setTitle("Successfull Registeration!");
                                                            builder.setCancelable(false);
                                                            builder.setPositiveButton(
                                                                    "Ok",
                                                                    new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog,
                                                                                            int which) {

                                                                            dialog.cancel();

                                                                            empty();
                                                                            FirebaseAuth.getInstance().signOut();
                                                                            Intent i = new Intent(getApplicationContext(), SignIn.class);
                                                                            startActivity(i);
                                                                            finish();// its going to be remove from backstack
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

                                                                            empty();
                                                                            FirebaseAuth.getInstance().signOut();
                                                                            Intent i = new Intent(getApplicationContext(), SignIn.class);
                                                                            startActivity(i);
                                                                            finish();// its going to be remove from backstack
                                                                        }
                                                                    });
                                                            AlertDialog alertDialog = builder.create();
                                                            alertDialog.show();


                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            pb.setVisibility(View.GONE);
                                                            scrollView.setVisibility(View.VISIBLE);


                                                            // Toast.makeText(getApplicationContext(), " verifcation email not sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                            showErrorDialog(e.getMessage());


                                                        }
                                                    });


                                                }
                                                else
                                                    {

                                                    pb.setVisibility(View.GONE);
                                                        scrollView.setVisibility(View.VISIBLE);
                                                    //Toast.makeText(getApplicationContext(), "failed to register! Try again Later", Toast.LENGTH_SHORT).show();
                                                    //Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                                                       showErrorDialog( task.getException().toString());


                                                }
                                            }
                                        });
                            }

                            else {

                                pb.setVisibility(View.GONE);
                                scrollView.setVisibility(View.VISIBLE);


                               // Toast.makeText(getApplicationContext(), task.getException().getMessage() + "failed to register! Try again Later", Toast.LENGTH_SHORT).show();
                                showErrorDialog(task.getException().getMessage());



                            }

                        }
                    });
        }


        else
        {

            pb.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.INVISIBLE);
            fauth.createUserWithEmailAndPassword(email1, password1)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {


                                String uid = task.getResult().getUser().getUid();


                                User l = new    User (uid,fname, email1, password1, phone1,address1, 2,"",0,0,"","");

                                FirebaseDatabase.getInstance().getReference("Clients").child(FirebaseAuth.getInstance().getCurrentUser().getUid())// return cuurently registerd user id
                                        .setValue(l)
                                        .addOnCompleteListener(new OnCompleteListener<Void>()// check wether data has been inserted in database
                                        {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    user = fauth.getCurrentUser();


                                                    //send verifcation link

                                                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            pb.setVisibility(View.GONE);
                                                            scrollView.setVisibility(View.VISIBLE);
                                                            //Toast.makeText(getApplicationContext(), " User has been registered successfully ,Verifcation email has been sent please verify your account", Toast.LENGTH_SHORT).show();
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                                                            builder.setMessage("User has been registered successfully ,Verifcation email has been sent please verify your account");
                                                            builder.setTitle("Account Verifcation!");
                                                            builder.setCancelable(false);
                                                            builder.setPositiveButton(
                                                                    "Ok",
                                                                    new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog,
                                                                                            int which) {

                                                                            dialog.cancel();

                                                                            empty();
                                                                            FirebaseAuth.getInstance().signOut();
                                                                            Intent i = new Intent(getApplicationContext(), SignIn.class);
                                                                            startActivity(i);
                                                                            finish();// its going to be remove from backstack
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

                                                                            empty();
                                                                            FirebaseAuth.getInstance().signOut();
                                                                            Intent i = new Intent(getApplicationContext(), SignIn.class);
                                                                            startActivity(i);
                                                                            finish();// its going to be remove from backstack
                                                                        }
                                                                    });
                                                            AlertDialog alertDialog = builder.create();
                                                            alertDialog.show();


                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            pb.setVisibility(View.GONE);
                                                            scrollView.setVisibility(View.VISIBLE);
                                                            //Toast.makeText(getApplicationContext(), " verifcation email not sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                            showErrorDialog(e.getMessage());
                                                        }
                                                    });


                                                } else {
                                                    pb.setVisibility(View.GONE);
                                                    scrollView.setVisibility(View.VISIBLE);
                                                   // Toast.makeText(getApplicationContext(), "failed to register! Try again Later", Toast.LENGTH_SHORT).show();
                                                    //Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                                                    showErrorDialog( task.getException().toString());


                                                }
                                            }
                                        });
                            }




                            else
                                {

                                pb.setVisibility(View.GONE);
                                    scrollView.setVisibility(View.VISIBLE);
                               // Toast.makeText(getApplicationContext(), task.getException().getMessage() + "failed to register! Try again Later", Toast.LENGTH_SHORT).show();
                                    showErrorDialog(task.getException().getMessage());

                                }

                        }
                    });

        }
    }


    // method to check type of Account
    public void setType(View v)
    {
        boolean checked = ((RadioButton) v).isChecked();
        switch(v.getId()) {
            case R.id.lawyer:
                if (checked)
                    type="lawyer";
                break;
            case R.id.client:
                if (checked)
                    type="client";
                break;
        }

    }

    public void goToSignin(View v)
    {
        Intent i=new Intent(getApplicationContext(), SignIn.class);
        startActivity(i);
        finish();

    }

    private void showErrorDialog(String s)
    {




        AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
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

        Intent i=new Intent(getApplicationContext(), SignIn.class);
        startActivity(i);
        finish();

    }

    public void empty()
    {
        fullname.setText("");
        email.setText("");
        password.setText("");
        phone.setText("");
        address.setText("");
    }


    protected void onDestroy() {
        super.onDestroy();
        FirebaseAuth.getInstance().signOut();
    }
}