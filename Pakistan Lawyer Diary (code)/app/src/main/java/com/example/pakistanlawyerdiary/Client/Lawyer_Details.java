package com.example.pakistanlawyerdiary.Client;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.pakistanlawyerdiary.Lawyer.manager.NetworkManager;
import com.example.pakistanlawyerdiary.Model.User;
import com.example.pakistanlawyerdiary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Lawyer_Details extends AppCompatActivity {
    TextView name,number,address,about;
    CircleImageView profile_image;
    RatingBar lawyer_rating;
    String id,n,num,ad,ab;
    double r_value;
ImageView btn_rating;
    double rv;
    long c;
DatabaseReference reference;
    ProgressDialog pd;
    NetworkManager networkManager = new NetworkManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyer__details);
        Intent i=getIntent();
        id=i.getStringExtra("id");
        //Toast.makeText(Lawyer_Details.this,id,Toast.LENGTH_SHORT).show();

        name=(TextView)findViewById(R.id.name);
        number=(TextView)findViewById(R.id.number);
        address=(TextView)findViewById(R.id.address);
        about=(TextView)findViewById(R.id.about);
        lawyer_rating=(RatingBar)findViewById(R.id.lawyer_rating);
        profile_image=(CircleImageView) findViewById(R.id.profile_img);
        pd=new ProgressDialog(this);
        pd.setTitle("Loading");
        pd.show();
        if (!networkManager.checkInternetConnection(Lawyer_Details.this))
        {
            Toast.makeText(getApplicationContext(), "check your internet connection", Toast.LENGTH_SHORT).show();
            pd.dismiss();
        }
        reference= FirebaseDatabase.getInstance().getReference("Lawyers").child(id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                pd.dismiss();

                User l=snapshot.getValue(  User .class);

                try {

                    if (!l.getImage().isEmpty()) {
                        Glide.with(Lawyer_Details.this).load(l.getImage()).into(profile_image);
                    } else {
                        profile_image.setImageResource(R.drawable.account_icon);
                    }
                }
                catch(Exception e)
                {
                    profile_image.setImageResource(R.drawable.account_icon);
                }

                    n=l.getFullname();
                name.setText(n);
                    num=l.getPhone();
                number.setText(num);
                    ad=l.getAddress();
                address.setText(ad);
                ab=l.getAbout();
                about.setText(ab);
                    r_value=l.getRatingValue();
                lawyer_rating.setRating(floatValue(r_value));
               // Toast.makeText(Lawyer_Details.this,n+e+num+ad+r_value,Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
pd.dismiss();
            }
        });






        btn_rating=(ImageView)findViewById(R.id.btn_rating);
        btn_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
       showRatingDialog();

            }
        });

    }
    private float floatValue(double d)
    {
        float f = (float)d;
        return f;
    }

    private void showRatingDialog() {

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Lawyer_Details.this);
        builder.setTitle("Rating");
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
                if (!networkManager.checkInternetConnection(Lawyer_Details.this)) {
                    Toast.makeText(getApplicationContext(), "check your internet connection and try again", Toast.LENGTH_SHORT).show();
                } else {
                    float ratingValue = (rBar.getRating());
                    reference = FirebaseDatabase.getInstance().getReference("Lawyers").child(id);
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            User l = snapshot.getValue(User.class);
                            rv = l.getRatingValue();
                            c = l.getRatingCount();

                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    double sumRating = rv + ratingValue;
                    long count = c + 1;
                    double result = sumRating / count;
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("ratingValue", result);
                    hashMap.put("ratingCount", count);
                    reference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Lawyer_Details.this, "Thank You", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Lawyer_Details.this, "Failed try again !", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });


        AlertDialog dialog=builder.create();
        dialog.show();
    }




    public void callLawyer(View v)
    {

        Intent intent=new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+number.getText().toString()));
        startActivity(intent);

    }

    public void back(View v)
    {

        finish();
    }

    public void onBackPressed(View v)
    {
        finish();
    }

}