package com.example.pakistanlawyerdiary.Client;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pakistanlawyerdiary.Adapter.MessageAdapter;
import com.example.pakistanlawyerdiary.Fragments.APIService;
import com.example.pakistanlawyerdiary.Lawyer.manager.NetworkManager;
import com.example.pakistanlawyerdiary.Model.Chat;
import com.example.pakistanlawyerdiary.Model.User;
import com.example.pakistanlawyerdiary.Notifications.Client;
import com.example.pakistanlawyerdiary.Notifications.Data;
import com.example.pakistanlawyerdiary.Notifications.MyResponse;
import com.example.pakistanlawyerdiary.Notifications.Sender;
import com.example.pakistanlawyerdiary.Notifications.Token;
import com.example.pakistanlawyerdiary.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Messages extends AppCompatActivity {
    TextView lawyername;
    CircleImageView profile_image;
    FirebaseUser fuser;
    DatabaseReference reference;
    Intent intent;
    ImageButton btn_send;
    EditText text_send;
    MessageAdapter messageAdapter;
    List<Chat> mChat;
    RecyclerView recyclerView;
    ValueEventListener seenListener;
    String userid;
    boolean notify = false;
    int ans1[] = {0};
    int r;
    int msgCount = 0;
    APIService apiService;
    NetworkManager networkManager = new NetworkManager();
   // Handler handler=new Handler();
   // Runnable myRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        profile_image= (CircleImageView)findViewById(R.id.profile_img);
        lawyername = (TextView) findViewById(R.id.lname);
        btn_send = (ImageButton) findViewById(R.id.btn_send);
        text_send = (EditText) findViewById(R.id.text_send);
        intent = getIntent();
        userid = intent.getStringExtra("userid");
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;
                String msg = text_send.getText().toString();
                if (!msg.equals("")) {
                    if (!networkManager.checkInternetConnection(Messages.this))
                    {
                        Toast.makeText(Messages.this, "Check your internet connection and try again!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        sendMessage(fuser.getUid(), userid, msg);
                        text_send.setText("");
                    }
                } else {
                    Toast.makeText(Messages.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                    text_send.setText("");
                }

            }
        });
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Lawyers").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(   User .class);
                lawyername.setText(user.getFullname());
                try {

                    if (!user.getImage().isEmpty()) {
                        Glide.with(Messages.this).load(user.getImage()).into(profile_image);
                    } else {
                        profile_image.setImageResource(R.drawable.account_icon);
                    }
                }
                catch(Exception e)
                {
                    profile_image.setImageResource(R.drawable.account_icon);
                }
                readMessages(fuser.getUid(), userid);
                //Toast.makeText(Messages.this, fuser.getUid() + userid, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        seenMessage(userid);
        //isChatExist();
        //grantPermission();



    }


    private void seenMessage(final String userid) {
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Chat chat = ds.getValue(Chat.class);
                    if (chat.getReciever().equals(fuser.getUid()) && chat.getSender().equals(userid)) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isseen", true);
                        ds.getRef().updateChildren(hashMap);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void sendMessage(String sender, final String reciever, String message) {


        // Toast.makeText(Messages.this, String.valueOf(ans), Toast.LENGTH_SHORT).show();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("reciever", reciever);
        hashMap.put("message", message);
        hashMap.put("isseen", false);
        reference.child("Chats").push().setValue(hashMap);

    // add client in lawyer chats
        final DatabaseReference chatRef1=FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(userid).child(fuser.getUid());
        chatRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists())
                {

                    DatabaseReference chatRef2=FirebaseDatabase.getInstance().getReference("Chatlist").child(userid).child(fuser.getUid());
                    HashMap<String ,Object> hashMap=new HashMap<>();
                    hashMap.put("id",fuser.getUid());
                    chatRef2.updateChildren(hashMap);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        // add user to chat fragment
        final DatabaseReference chatRef=FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(fuser.getUid()).child(userid);
        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists())
                {
                    chatRef.child("id").setValue(userid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






        final String msg = message;
        reference = FirebaseDatabase.getInstance().getReference("Clients").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User  user = snapshot.getValue(  User .class);
                if (notify) {
                    sendNotification(reciever, user.getFullname(), msg);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

/*
        if (ans1[0] == 0) {

            //sendSMS("03460655946","ABC");
            myRunnable = new Runnable() {
                @Override
                public void run() {

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Chats");
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                Chat chat = ds.getValue(Chat.class);
                                if (chat.getSender().equals(fuser.getUid()) && chat.getReciever().equals(userid)) {
                                    if (chat.isIsseen() == false) {

                                        //r1[0]=1;
                                        //r=r1[0];
                                        // Toast.makeText(Messages.this, String.valueOf(r), Toast.LENGTH_SHORT).show();

                                        sendTextMessage();
                                        finishActivity(1);

                                        break;

                                    }

                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                    Toast.makeText(Messages.this, String.valueOf(r), Toast.LENGTH_SHORT).show();

                }
            };

            start();






          /* new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(Messages.this, "inside thread", Toast.LENGTH_SHORT).show();


                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Chats");
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                Chat chat = ds.getValue(Chat.class);
                                if (chat.getSender().equals(fuser.getUid()) && chat.getReciever().equals(userid)) {
                                    if (chat.isIsseen() == false) {

                                        //r1[0]=1;
                                        //r=r1[0];
                                        // Toast.makeText(Messages.this, String.valueOf(r), Toast.LENGTH_SHORT).show();

                                        sendTextMessage();
                                      finishActivity(1);

                                        break;

                                    }

                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                    Toast.makeText(Messages.this, String.valueOf(r), Toast.LENGTH_SHORT).show();

                }


            }, 60000);

        }
        }*/

}


private void currentUser(String userid)
{
    SharedPreferences.Editor editor=getSharedPreferences("PREFS",MODE_PRIVATE).edit();
    editor.putString("currentuser",userid);
    editor.apply();
}

private void sendNotification(String reciever, final String username, final String message)
{
    DatabaseReference tokens=FirebaseDatabase.getInstance().getReference("Tokens");
    Query query=tokens.orderByKey().equalTo(reciever);
    query.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot)
        {

            for(DataSnapshot ds:snapshot.getChildren())
            {
                Token token=ds.getValue(Token.class);
                Data data=new Data(fuser.getUid(),R.mipmap.ic_launcher,username+": "+message,"New Message",
                        userid,"lawyer");
                Sender sender=new Sender(data,token.getToken());
                apiService.sendNotification(sender).
                        enqueue(new Callback<MyResponse>() {
                            @Override
                            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                if(response.code()==200)
                                {
                                    if(response.body().success!=1)
                                    {
                                        //Toast.makeText(Messages.this,"Failed",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<MyResponse> call, Throwable t) {

                            }
                        });
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });

}



    private  void readMessages(final String myid, final String userid)
    {

       // Toast.makeText(Messages.this,"inside read",Toast.LENGTH_SHORT).show();
        mChat=new ArrayList<>();
     reference=FirebaseDatabase.getInstance().getReference("Chats");
     reference.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        mChat.clear();
        if(snapshot.exists()) {
            for (DataSnapshot ds : snapshot.getChildren()) {
                //Toast.makeText(Messages.this, "Inside snapshot", Toast.LENGTH_SHORT).show();
                Chat chat = ds.getValue(Chat.class);
               
                if (chat.getReciever().equals(myid) && chat.getSender().equals(userid) ||
                        chat.getReciever().equals(userid) && chat.getSender().equals(myid))
                {

                    //Toast.makeText(Messages.this, "inside if", Toast.LENGTH_SHORT).show();
                   // Toast.makeText(Messages.this, chat.getMessage(), Toast.LENGTH_SHORT).show();
                    mChat.add(chat);
                }
                else
                {
                    //Toast.makeText(Messages.this, "no chats", Toast.LENGTH_SHORT).show();
                }
                messageAdapter = new MessageAdapter(Messages.this, mChat);
                recyclerView.setAdapter(messageAdapter);

            }
        }
        else
        {
           // Toast.makeText(Messages.this, "snapshot does not exist", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
});


    }
    private  void status(String status)
    {
        reference= FirebaseDatabase.getInstance().getReference("Clients").child(fuser.getUid());
        HashMap<String ,Object> hashMap=new HashMap<>();
        hashMap.put("status",status);
        reference.updateChildren(hashMap);
    }
    protected void onResume()
    {
        super.onResume();
        status("online");
        currentUser(userid);
    }

    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(seenListener);
        status("offline");
        currentUser("none");
    }




 /*   private void isChatExist()
    {


        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Chats");


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {

                    Chat chat = ds.getValue(Chat.class);
                    if (chat.getSender().equals(fuser.getUid()) && chat.getReciever().equals(userid) ||
                            chat.getReciever().equals(fuser.getUid()) && chat.getSender().equals(userid)) {
                        Toast.makeText(Messages.this, "exist", Toast.LENGTH_SHORT).show();
                        ans1[0] = 1;
                        Toast.makeText(Messages.this, String.valueOf(ans1[0]), Toast.LENGTH_SHORT).show();
                        break;

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Toast.makeText(Messages.this, String.valueOf(ans1[0]), Toast.LENGTH_SHORT).show();
    }





    private void grantPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {

            } else {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1);
            }
        }


    }













    private void sendTextMessage()
    {

       // Toast.makeText(Messages.this,"inside send text message", Toast.LENGTH_SHORT).show();

        String phone ;
            String name;

            DatabaseReference ref1= FirebaseDatabase.getInstance().getReference("Lawyers").child(userid);
            ref1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {

                    final User user1=snapshot.getValue(User.class);
                    DatabaseReference ref2= FirebaseDatabase.getInstance().getReference("Clients").child(fuser.getUid());
                    ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot)
                        {

                            User user=snapshot.getValue(User.class);
                            sendSMS(user1.getPhone(),user.getFullname());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });




    }
    public void sendSMS(String phoneNo, String msg) {


        //Toast.makeText(Messages.this,"inside send message", Toast.LENGTH_SHORT).show();
        msg+=" wants to contact you on Pakistan Lawyer Diary APP \n please respond him ";

        if(Build.VERSION.SDK_INT> Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, msg, null, null);
                    //Toast.makeText(getApplicationContext(), "Message Sent ",
                            //Toast.LENGTH_LONG).show();
                    stop();
                }
                catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), ex.getMessage().toString(),
                            Toast.LENGTH_LONG).show();
                    ex.printStackTrace();
                }

            }
            else
                {
                    requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1);


                        try {
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
                            Toast.makeText(getApplicationContext(), "Message Sent ",
                                    Toast.LENGTH_LONG).show();
                            stop();

                        } catch (Exception ex) {
                            Toast.makeText(getApplicationContext(), ex.getMessage().toString(),
                                    Toast.LENGTH_LONG).show();
                            ex.printStackTrace();

                        }


                }
        }
    }

    public void start()
    {
        handler.postDelayed(myRunnable,60000);
    }
    public void stop()
    {
        handler.removeCallbacks(myRunnable);
       // handler.getLooper().quit();
    }
    public void back(View v)
    {
        //Looper.myLooper().quit();
        stop();
        startActivity(new Intent(Messages.this,Client_Chat.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }*/

    public void back(View v)
    {
        finishAffinity();
        Intent i=new Intent(this, Client_Chat.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // return super.onKeyDown(keyCode, event);
        finishAffinity();
        Intent i=new Intent(this,Client_Chat.class);
        startActivity(i);
        finish();
        return true;
    }

    public void onBackPressed(View v)
    {
        //super.onBackPressed();
        finishAffinity();
        Intent i=new Intent(this,Client_Chat.class);
        startActivity(i);
        finish();
    }
}