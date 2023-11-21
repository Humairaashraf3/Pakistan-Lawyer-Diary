package com.example.pakistanlawyerdiary.Lawyer;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pakistanlawyerdiary.Adapter.ClientAdapter;
import com.example.pakistanlawyerdiary.Lawyer.manager.NetworkManager;
import com.example.pakistanlawyerdiary.Model.ChatList;
import com.example.pakistanlawyerdiary.Model.User;
import com.example.pakistanlawyerdiary.Notifications.Token;
import com.example.pakistanlawyerdiary.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Lawyer_Chat extends AppCompatActivity {

    DatabaseReference reference;
    private RecyclerView recyclerView;
    private ClientAdapter lawyerAdapter;
    private List<User> mUsers;
    FirebaseUser fuser;
    private List<ChatList> usersList;
    ProgressBar pb;
    TextView tv;
    NetworkManager networkManager = new NetworkManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyer__chat);
        pb=findViewById(R.id.progressbar);
        pb.setVisibility(View.VISIBLE);
        if (!networkManager.checkInternetConnection(Lawyer_Chat.this))
        {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
        tv=findViewById(R.id.nochats);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Lawyer_Chat.this));
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        usersList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Chatlist").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                usersList.clear();
                if (!snapshot.exists()) {
                    pb.setVisibility(View.INVISIBLE);

                    tv.setVisibility(View.VISIBLE);
                } else
                    {
                        pb.setVisibility(View.INVISIBLE);
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        ChatList chatList = ds.getValue(ChatList.class);
                        usersList.add(chatList);
                    }
                    chatList();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


        updateToken(FirebaseInstanceId.getInstance().getToken());

    }





    private void chatList()
    {

        mUsers=new ArrayList<>();
        reference=FirebaseDatabase.getInstance().getReference("Clients");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists())
                {
                    //Toast.makeText(Lawyer_Chat.this,"no data",Toast.LENGTH_SHORT).show();
                }
                mUsers.clear();
                for(DataSnapshot ds:snapshot.getChildren())
                {
                    User  lawyer = ds.getValue(  User .class);
                    for(ChatList chatList:usersList)
                    {
                        if(lawyer.getUid().equals(chatList.getId()))
                        {
                            mUsers.add(lawyer);
                        }
                    }
                }
                lawyerAdapter=new ClientAdapter(Lawyer_Chat.this,mUsers,true);
                recyclerView.setAdapter(lawyerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateToken(String token)
    {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1=new Token(token);
        reference.child(fuser.getUid()).setValue(token1);
    }






    ///         Online Status
    private  void status(String status)
    {
        reference= FirebaseDatabase.getInstance().getReference("Lawyers").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        HashMap<String ,Object> hashMap=new HashMap<>();
        hashMap.put("status",status);
        reference.updateChildren(hashMap);
    }

    protected void onResume()
    {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }



    public void main(View v)
    {
        Intent i=new Intent(this,MainActivity.class);
        startActivity(i);
        finish();
    }
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        // return super.onKeyDown(keyCode, event);
        Intent i=new Intent(this,MainActivity.class);
        startActivity(i);
        finish();
        return true;
    }

    public void onBackPressed(View v)
    {

       Intent i=new Intent(this,MainActivity.class);
       startActivity(i);
       finish();
    }

   /* // check for network connection
    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }*/

}