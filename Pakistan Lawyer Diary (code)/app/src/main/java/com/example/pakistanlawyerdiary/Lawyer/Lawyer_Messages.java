package com.example.pakistanlawyerdiary.Lawyer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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

public class Lawyer_Messages extends AppCompatActivity {
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
    NetworkManager networkManager = new NetworkManager();
    String userid;
    boolean notify=false;
    APIService apiService;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyer__messages);

        apiService= Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        profile_image= (CircleImageView)findViewById(R.id.profile_img);
        lawyername=(TextView)findViewById(R.id.lname);
        btn_send=(ImageButton)findViewById(R.id.btn_send);
        text_send=(EditText) findViewById(R.id.text_send);
        intent=getIntent();
        userid=intent.getStringExtra("userid");
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify=true;
                String msg=text_send.getText().toString();
                if(!msg.equals(""))
                {

                    if (!networkManager.checkInternetConnection(Lawyer_Messages.this))
                    {
                        Toast.makeText(Lawyer_Messages.this, "Check your internet connection and try again!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        sendMessage(fuser.getUid(), userid, msg);
                        text_send.setText("");
                    }
                }
                else
                {
                    Toast.makeText(Lawyer_Messages.this,"You can't send empty message",Toast.LENGTH_SHORT).show();
                }

            }
        });
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Clients").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {

                User user=snapshot.getValue (  User .class);
                lawyername.setText(user.getFullname());

                try {

                    if (!user.getImage().isEmpty()) {
                        Glide.with(Lawyer_Messages.this).load(user.getImage()).into(profile_image);
                    } else {
                        profile_image.setImageResource(R.drawable.account_icon);
                    }
                }
                catch(Exception e)
                {
                    profile_image.setImageResource(R.drawable.account_icon);
                }
                readMessages(fuser.getUid(),userid);
               // Toast.makeText(Lawyer_Messages.this,fuser.getUid()+userid,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        seenMessage(userid);
    }



    private void seenMessage(final String userid)
    {
        reference=FirebaseDatabase.getInstance().getReference("Chats");
        seenListener=reference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren())


                {

                    Chat chat=ds.getValue(Chat.class);
                    if(chat.getReciever().equals(fuser.getUid()) && chat.getSender().equals(userid))
                    {
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("isseen",true);
                        ds.getRef().updateChildren(hashMap);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }









    private void sendMessage(String sender, final String reciever, String message)
    {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("reciever",reciever);
        hashMap.put("message",message);
        hashMap.put("isseen",false);
        reference.child("Chats").push().setValue(hashMap);

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


        final String msg=message;
        reference=FirebaseDatabase.getInstance().getReference("Lawyers").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {

                User  user=snapshot.getValue(   User .class);
                if(notify)
                {
                    sendNotification(reciever, user.getFullname(), msg);
                }
                notify=false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
                            userid,"client");
                    Sender sender=new Sender(data,token.getToken());
                    apiService.sendNotification(sender).
                            enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if(response.code()==200)
                                    {
                                        if(response.body().success!=1)
                                        {
                                            //Toast.makeText(Lawyer_Messages.this,"Failed",Toast.LENGTH_SHORT).show();
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

        //Toast.makeText(Lawyer_Messages.this,"inside read",Toast.LENGTH_SHORT).show();
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
                           // Toast.makeText(Lawyer_Messages.this, chat.getMessage(), Toast.LENGTH_SHORT).show();
                            mChat.add(chat);
                        }
                        else
                        {
                            //Toast.makeText(Lawyer_Messages.this, "no chats", Toast.LENGTH_SHORT).show();
                        }
                        messageAdapter = new MessageAdapter(Lawyer_Messages.this, mChat);
                        recyclerView.setAdapter(messageAdapter);

                    }
                }
                else
                {
                    //Toast.makeText(Lawyer_Messages.this, "snapshot does not exist", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }




    private void currentUser(String userid)
    {
        SharedPreferences.Editor editor=getSharedPreferences("PREFS",MODE_PRIVATE).edit();
        editor.putString("currentuser",userid);
        editor.apply();
    }







    private  void status(String status)
    {
        reference= FirebaseDatabase.getInstance().getReference("Lawyers").child(fuser.getUid());
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



    public void back(View v)
    {
        finishAffinity();
        Intent i=new Intent(this,Lawyer_Chat.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
       // return super.onKeyDown(keyCode, event);
        finishAffinity();
        Intent i=new Intent(this,Lawyer_Chat.class);
        startActivity(i);
        finish();
        return true;
    }

    public void onBackPressed(View v)
    {
        //super.onBackPressed();
        finishAffinity();
       Intent i=new Intent(this,Lawyer_Chat.class);
         startActivity(i);
        finish();
    }
}