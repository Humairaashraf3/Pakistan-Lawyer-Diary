package com.example.pakistanlawyerdiary.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pakistanlawyerdiary.Adapter.LawyerAdapter;
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
import java.util.List;


public class ChatsFragment extends Fragment {

    private RecyclerView recyclerView;
    private LawyerAdapter lawyerAdapter;
    private List<User> mUsers;
    FirebaseUser fuser;
    DatabaseReference reference;
    private List<ChatList> usersList;
    TextView tv;
    ProgressBar pb;
    NetworkManager networkManager = new NetworkManager();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chats, container, false);
        pb=view.findViewById(R.id.progressbar);
        pb.setVisibility(View.VISIBLE);
        if(!networkManager.checkInternetConnection(getContext()))
        {
            Toast.makeText(getContext(), "check your internet connection", Toast.LENGTH_SHORT).show();
        }
        tv=view.findViewById(R.id.nochats);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        usersList = new ArrayList<>();
reference=FirebaseDatabase.getInstance().getReference("Chatlist").child(fuser.getUid());
reference.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {

        usersList.clear();
        if (snapshot.exists()) {
            pb.setVisibility(View.INVISIBLE);
            for (DataSnapshot ds : snapshot.getChildren()) {
                ChatList chatList = ds.getValue(ChatList.class);
                usersList.add(chatList);
            }
            chatList();
        }

    else
    {
        pb.setVisibility(View.INVISIBLE);
        tv.setVisibility(View.VISIBLE);
    }

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
});

        updateToken(FirebaseInstanceId.getInstance().getToken());
        return view;
    }

    private void chatList()
    {

        mUsers=new ArrayList<>();
        reference=FirebaseDatabase.getInstance().getReference("Lawyers");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
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
                lawyerAdapter=new LawyerAdapter(getContext(),mUsers,true);
                recyclerView.setAdapter(lawyerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void updateToken(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(fuser.getUid()).setValue(token1);
    }



}









   /* private void chatList()
    {
        mUsers=new ArrayList<>();
        reference=FirebaseDatabase.getInstance().getReference("Lawyers");
reference.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
mUsers.clear();
for(DataSnapshot ds:snapshot.getChildren())
{
    User user=ds.getValue(User.class);
    for(Chatlist chatlist:usersList)
    {
        if(user.getUid().equals(chatlist.getId()))
        {
            mUsers.add(user);
        }
    }
}
lawyerAdapter=new LawyerAdapter(getContext(),mUsers,true);
recyclerView.setAdapter(lawyerAdapter);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
});
    }
*/

