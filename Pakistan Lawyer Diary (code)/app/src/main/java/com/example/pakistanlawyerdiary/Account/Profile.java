package com.example.pakistanlawyerdiary.Account;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.pakistanlawyerdiary.Client.ClientMainActivity;
import com.example.pakistanlawyerdiary.Lawyer.MainActivity;
import com.example.pakistanlawyerdiary.Lawyer.manager.NetworkManager;
import com.example.pakistanlawyerdiary.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


public class Profile extends AppCompatActivity {

    TextView email,username,address,mobileNumber,about;
    String image;
    ImageView img;
    RatingBar rb;
    String type;
    ProgressBar pb;
    LinearLayout linearLayout;
    ImageView edit;
    // permissions
    private static final  int CAMERA_REQUEST_CODE=100;
    private static final  int STORAGE_REQUEST_CODE=200;
    private static final  int IMAGE_PICK_GALLARY_CODE=300;
    private static final  int IMAGE_PICK_CAMERA_CODE=400;
    String cameraPermissions[];
    String storagePemissions[];
    Uri imageuri;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    StorageReference storageReference;
    Bitmap bitmap;
    String userid="";
    LinearLayout info;
    //String storagepath="Users_Profile_Imgs/*";
    NetworkManager networkManager = new NetworkManager();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getType();
        // init permissions
        cameraPermissions=new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePemissions=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        img=findViewById(R.id.profile_img);
        email = findViewById(R.id.email);
        username = findViewById(R.id.username);
        address = findViewById(R.id.address);
        about=findViewById(R.id.about);
        mobileNumber = findViewById(R.id.mobile_number);
        rb = findViewById(R.id.rating);
        linearLayout=findViewById(R.id.linearlayout);
        info=findViewById(R.id.information);
        edit=findViewById(R.id.edit);
        pb=findViewById(R.id.progressbar);
        pb.setVisibility(View.VISIBLE);
        //storageReference = FirebaseStorage.getInstance().getReference();

        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        userid=firebaseUser.getUid();
        storageReference= FirebaseStorage.getInstance().getReference();
        if (!networkManager.checkInternetConnection(Profile.this))
        {
            Toast.makeText(getApplicationContext(), "No Internet Connecion", Toast.LENGTH_SHORT).show();
        }

        if(type.equals("1")) {
            databaseReference=FirebaseDatabase.getInstance().getReference().child("Lawyers");
        }
        else
        {
            databaseReference=FirebaseDatabase.getInstance().getReference().child("Clients");
        }


            Query applesQuery =databaseReference.orderByChild("email").equalTo(firebaseUser.getEmail());
            applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                  pb.setVisibility(View.GONE);

                    linearLayout.setVisibility(View.VISIBLE);
                    edit.setVisibility(View.VISIBLE);
                    if(type.equals("1"))
                    {
                        rb.setVisibility(View.VISIBLE);
                        info.setVisibility(View.VISIBLE);
                    }

                    if(dataSnapshot.exists()) {

                        for(DataSnapshot ds: dataSnapshot.getChildren())
                        {



                            rb.setRating(floatValue((long)ds.child("ratingValue").getValue()));
                            email.setText(ds.child("email").getValue().toString());
                            username.setText(ds.child("fullname").getValue().toString());
                            mobileNumber.setText(ds.child("phone").getValue().toString());
                            address.setText(ds.child("address").getValue().toString());
                            if(ds.child("about").getValue().toString().equals(""))
                            {
                                about.setText("No info yet");
                            }
                            else
                            {
                                about.setText(ds.child("about").getValue().toString());
                            }
                            image=""+ds.child("image").getValue().toString();

                            try {

                                if(!image.equals(""))
                                {

                                    Glide.with(getApplicationContext()).load(ds.child("image").getValue().toString()).into(img);
                                }
                            }
                            catch (Exception e)
                            {

                                Glide.with(getApplicationContext()).load(R.drawable.account_icon).into(img);
                               // Picasso.get().load(R.drawable.account_icon).into(img);
                            }
                            // Toast.makeText(Profile.this,userprofile.getEmail(), Toast.LENGTH_SHORT).show();
                            //Toast.makeText(Profile.this,userprofile.getAddress(), Toast.LENGTH_SHORT).show();

                        }

                    }
                    else
                    {
                        //Toast.makeText(Profile.this,"snap shot does not exist", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                   // pd.dismiss();
                    Log.e("TAG", "onCancelled", databaseError.toException());
                }
            });


        }





    public void editProfile(View v)
    {
        String options[]={"Edit Profile","Edit info"};
        AlertDialog.Builder builder=new AlertDialog.Builder(Profile.this);
        builder.setTitle("Choose Action");
        // set items to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle dialog items click
                if (which == 0) {
                    // edit name
                   // pd.setMessage("Updating Name");
                    showProfileDialog("fullname");

                } else if (which == 1) {

                    // edit phone
                    Intent intent = new Intent(getApplicationContext(), Edit_Profile.class);
                    intent.putExtra("name", username.getText().toString());
                    intent.putExtra("phone", mobileNumber.getText().toString());
                    intent.putExtra("address", address.getText().toString());
                    intent.putExtra("about", about.getText().toString());

                    startActivity(intent);
                    finish();
                }

            }
        });
        builder.create().show();

        //showeditProfileDialog();




    }

    /// storage permission

    private boolean checkStoragePermission()
    {
        boolean result= ContextCompat.checkSelfPermission(Profile.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }
    private void resquestStoragePermission()
    {
        ActivityCompat.requestPermissions(Profile.this,storagePemissions,STORAGE_REQUEST_CODE);
    }


    /// Camera permission

    private boolean checkCameraPermission()
    {
        boolean result= ContextCompat.checkSelfPermission(Profile.this,Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);
        boolean result1= ContextCompat.checkSelfPermission(Profile.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }


    private void resquestCameraPermission()
    {
        ActivityCompat.requestPermissions(Profile.this,cameraPermissions,CAMERA_REQUEST_CODE);
    }





    private void showProfileDialog(String fullname) {


        String options[]={"Camera","Gallery"};
        AlertDialog.Builder builder=new AlertDialog.Builder(Profile.this);
        builder.setTitle("Choose Action");
        // set items to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle dialog items click
                if (which == 0)
                {

                    if(!checkCameraPermission())
                    {
                        resquestCameraPermission();
                    }
                    else
                    {
                        pickFromCamera();
                    }

                } else if (which == 1)
                {

                    if(!checkStoragePermission())
                    {
                        resquestStoragePermission();
                    }
                    else
                    {
                        pickFromGallery();
                    }

                }

            }
        });
        builder.create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode)
        {
            case CAMERA_REQUEST_CODE : {
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && writeStorageAccepted) {
                        pickFromCamera();
                    } else
                        {
                        Toast.makeText(Profile.this, "Please enable camera and storage permission", Toast.LENGTH_SHORT).show();
                    }

                }
            }
            break;
                case STORAGE_REQUEST_CODE:
            {


                if (grantResults.length > 0)
                {
                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageAccepted)
                    {
                        pickFromGallery();
                    } else
                    {
                        Toast.makeText(Profile.this, "Please enable storage permission", Toast.LENGTH_SHORT).show();
                    }

                }

            }
            break;
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK)
        {
            if(requestCode==IMAGE_PICK_GALLARY_CODE) {
                imageuri = data.getData();
                try {
                    {

                        InputStream inputStream = getContentResolver().openInputStream(imageuri);
                        bitmap= BitmapFactory.decodeStream(inputStream);
                        img.setImageBitmap(bitmap);
                        uploadProfilePicture(imageuri);
                    }
                }
                    catch(Exception e)
                    {
                        Toast.makeText(Profile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
            }

            if(requestCode==IMAGE_PICK_CAMERA_CODE)
            {
                try {
                    {

                        InputStream inputStream = getContentResolver().openInputStream(imageuri);
                        bitmap= BitmapFactory.decodeStream(inputStream);
                        img.setImageBitmap(bitmap);
                        uploadProfilePicture(imageuri);
                    }
                }
                catch(Exception e)
                {
                    Toast.makeText(Profile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

        }
    }



    private void pickFromCamera()
    {
        ContentValues contentValues= new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE,"Temp Pic");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION,"Temp Description");
imageuri=Profile.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
Intent cameraintent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
cameraintent.putExtra(MediaStore.EXTRA_OUTPUT,imageuri);
startActivityForResult(cameraintent,IMAGE_PICK_CAMERA_CODE);
    }

    private void pickFromGallery()
    {


        Intent galleryintent=new Intent(Intent.ACTION_PICK);
        galleryintent.setType("image/*");
        startActivityForResult(galleryintent,IMAGE_PICK_GALLARY_CODE);
    }








    private void uploadProfilePicture(Uri uri)
    {

        if(type.equals("1"))
        {
            databaseReference=FirebaseDatabase.getInstance().getReference().child("Lawyers");

        }
        else
        {
            databaseReference=FirebaseDatabase.getInstance().getReference().child("Clients");

        }

        final ProgressDialog pd=new ProgressDialog(this);
        pd.setTitle("Uploading");
        pd.show();
        final StorageReference uploader=storageReference.child("profileimages/"+"img"+System.currentTimeMillis());
        uploader.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri)
                    {
                        final Map<String,Object> map=new HashMap<>();
                        map.put("image",uri.toString());
                        databaseReference.child(userid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot)
                            {
                                if(snapshot.exists())
                                {
                                    databaseReference.child(userid).updateChildren(map);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        pd.dismiss();
                        Toast.makeText(Profile.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot)
            {
                float percent=(100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                pd.setMessage("Uploaded :"+(int)percent+"%");
            }
        });

    }


    private void getType()
    {

        SharedPreferences getShared=getSharedPreferences("userInfo",MODE_PRIVATE);
        type=getShared.getString("type","Type");

    }


    private float floatValue(long d)
    {
        float f = (float)d;
        return f;
    }

    public void help(View v)
    {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Profile.this);
        View itemView = getLayoutInflater().inflate(R.layout.help_dialog,null);

        builder.setView(itemView);
        TextView text= itemView.findViewById(R.id.text);
        SpannableStringBuilder ssb=new SpannableStringBuilder
                (
                        "** You can only edit profile picture,username,mobile number and address data.\n\n** Press  "
                );
        ssb.setSpan(new ImageSpan(this,R.drawable.black_edit_icon),ssb.length()-1,ssb.length(), 0);
        ssb.append(" icon to edit profile picture or information.");
        text.setText(ssb);
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
        if(type.equals("1")) {

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
        else
        {
            Intent intent = new Intent(getApplicationContext(), ClientMainActivity.class);
            startActivity(intent);
            finish();
        }


    }
    public void onBackPressed()
    {
        if(type.equals("1")) {

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
        else
        {
            Intent intent = new Intent(getApplicationContext(), ClientMainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}