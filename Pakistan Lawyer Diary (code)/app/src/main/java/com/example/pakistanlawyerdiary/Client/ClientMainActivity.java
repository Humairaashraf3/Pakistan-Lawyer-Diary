package com.example.pakistanlawyerdiary.Client;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pakistanlawyerdiary.Account.Profile;
import com.example.pakistanlawyerdiary.Account.SignIn;
import com.example.pakistanlawyerdiary.Adapter.CustomLawyerList;
import com.example.pakistanlawyerdiary.Lawyer.manager.NetworkManager;
import com.example.pakistanlawyerdiary.Model.User;
import com.example.pakistanlawyerdiary.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ClientMainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener

{
    private DrawerLayout drawer;
    TextView name ,emailtxtview;
    static String email;
    static  String fname;
    View nview;
    FirebaseDatabase firebasedatabase;
    DatabaseReference reference;
    RecyclerView recview;
    CustomLawyerList adapter;

    ProgressDialog pd;
    NetworkManager networkManager = new NetworkManager();
FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_main);
        drawer = findViewById(R.id.drawer_layout);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);// toggle will listen drawer
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        nview=navigationView.getHeaderView(0);
        name = (TextView)nview. findViewById(R.id.full_name);
        emailtxtview=(TextView)nview. findViewById(R.id.email_address);


        navigationView.setNavigationItemSelectedListener(this);
        firebasedatabase=FirebaseDatabase.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        setInfo();
        setUserInfo();
        pd=new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setTitle("Logging Out");
      /* recview=(RecyclerView)findViewById(R.id.rec_view);
        recview.setLayoutManager(new LinearLayoutManager(this));
        reference=FirebaseDatabase.getInstance().getReference().child("Lawyers");
        FirebaseRecyclerOptions<User> options=new FirebaseRecyclerOptions.Builder<  User >().
                setQuery(FirebaseDatabase.getInstance().getReference().child("Lawyers"),  User .class).build();
        adapter=new CustomLawyerList(options);
        recview.setAdapter(adapter);*/

    }
/*
   @Override
    protected void onStart()
    {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        adapter.stopListening();
    }
*/


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        Intent i;
        switch (item.getItemId()) {

            case R.id.search:
                drawer.closeDrawer(GravityCompat.START);// menu will close after clicking an item of menu
                i = new Intent(this, Search_Lawyer.class);
                startActivity(i);
                break;
            case R.id.chat:
                drawer.closeDrawer(GravityCompat.START);// menu will close after clicking an item of menu
                i = new Intent(this, Client_Chat.class);
                startActivity(i);
                finish();
                break;
            case R.id.profile:
                    drawer.closeDrawer(GravityCompat.START);
                    i = new Intent(this, Profile.class);
                    startActivity(i);
                    finish();

                break;
            case R.id.rate_app:
                drawer.closeDrawer(GravityCompat.START);
                showRatingDialog();
                break;

            case R.id.logout:
                drawer.closeDrawer(GravityCompat.START);
                pd.show();
                FirebaseAuth.getInstance().signOut();
                SharedPreferences shrd=getSharedPreferences("userInfo",MODE_PRIVATE);// private mode means this file will only be used by this app
                SharedPreferences.Editor editor=shrd.edit();
                editor.putString("name","Full Name");
                editor.putString("email","Email");
                editor.putString("type","");
                editor.apply();
                // chnage this because your app will crash
                pd.dismiss();
                i = new Intent(this, SignIn.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
                break;

        }
        return true;
    }
    public void chat(View v)
    {

       Intent i = new Intent(this, Client_Chat.class);
        startActivity(i);
        finish();
    }

    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            showDialog();

        }

    }







    public void showDialog() {
        // custom dialog
        final Dialog dialog = new Dialog(ClientMainActivity.this);
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_main_alert);
        TextView alertt = dialog.findViewById(R.id.alerttext);
        alertt.setText("Do you really want to exit Pakistan Lawyer Diary ?");
        Button ok = dialog.findViewById(R.id.yes);
        Button no = dialog.findViewById(R.id.no);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
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

    private void setUserInfo()
    {

        SharedPreferences getShared=getSharedPreferences("userInfo",MODE_PRIVATE);
        email=getShared.getString("email","Email");
        fname=getShared.getString("name","Full Name");
        name.setText(fname);
        emailtxtview.setText(email);
    }








    private void setInfo()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Clients");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        reference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User  userprofile = snapshot.getValue(   User .class);
                if (userprofile != null) {
                    String fullname = userprofile.getFullname();
                    String email = userprofile.getEmail();
                    name.setText(fullname);
                    emailtxtview.setText(email);

                } else {
                    //Toast.makeText(getApplicationContext(), "usr profile is null", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getApplicationContext(), " Something wrong", Toast.LENGTH_SHORT).show();

            }
        });

    }
    private void showRatingDialog()
    {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ClientMainActivity.this);
        builder.setTitle(" App Rating");
        builder.setCancelable(false);
        View itemView = getLayoutInflater().inflate(R.layout.rating_layout,null);
        builder.setView(itemView);
        final RatingBar rBar =itemView.findViewById(R.id.rating_bar);
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (networkManager.checkInternetConnection(ClientMainActivity.this)) {

                    final float ratingValue = (rBar.getRating());


                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("AppRating").child(firebaseUser.getUid());
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("ratingValue", ratingValue);
                    ref.updateChildren(hashMap);
                    Toast.makeText(getApplicationContext(), "Thank You For Rating", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "check your internet connection and try again", Toast.LENGTH_SHORT).show();
                }
            }



        });
        AlertDialog dialog=builder.create();
        dialog.show();
    }




}