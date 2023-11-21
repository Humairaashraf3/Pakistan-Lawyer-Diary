package com.example.pakistanlawyerdiary.Client;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pakistanlawyerdiary.Adapter.CustomLawyerList;
import com.example.pakistanlawyerdiary.Lawyer.manager.NetworkManager;
import com.example.pakistanlawyerdiary.Model.User;
import com.example.pakistanlawyerdiary.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Search_Lawyer extends AppCompatActivity {
    SearchView sv;
    RecyclerView recview;
    CustomLawyerList adapter;
    List<User> userlist;
    ProgressBar pb;
    NetworkManager networkManager = new NetworkManager();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search__lawyer);

        if (!networkManager.checkInternetConnection(Search_Lawyer.this))
        {
            Toast.makeText(getApplicationContext(), "check your internet connection", Toast.LENGTH_SHORT).show();
        }
        sv = (SearchView) findViewById(R.id.search_view);
        pb=(ProgressBar) findViewById(R.id.progressbar);
        pb.setVisibility(View.VISIBLE);
        recview=(RecyclerView)findViewById(R.id.rec_view);
        recview.setLayoutManager(new LinearLayoutManager(this));
        recview.setHasFixedSize(true);
        userlist=new ArrayList<User>();
        getAllUsers();
       /* FirebaseRecyclerOptions<User> options=new  FirebaseRecyclerOptions.Builder<User>().setQuery(FirebaseDatabase.getInstance()
                .getReference().child("Lawyers"),  User.class).build();
        adapter=new CustomLawyerList(options);
        recview.setAdapter(adapter);*/

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                if (!TextUtils.isEmpty(query.trim()))
                {
                    processSearch(query);
                }
                else {

              getAllUsers();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (!TextUtils.isEmpty(newText.trim())) {
                    processSearch(newText);
                }
                else {

                    getAllUsers();
                }
                return false;
            }

        });
    }

    /*
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        adapter.stopListening();
        super.onStop();
    }*/


    private void getAllUsers()
    {

        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Lawyers");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                pb.setVisibility(View.GONE);
                userlist.clear();
                for(DataSnapshot ds:snapshot.getChildren())
                {
                    User user=ds.getValue(User.class);
                    if(!user.getUid().equals(firebaseUser.getUid()))
                    {
                            userlist.add(user);
                    }
                }
                adapter=new CustomLawyerList(Search_Lawyer.this,userlist);
                recview.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void processSearch(final String s)
    {

       /* pd.show();
        String sl=s.toLowerCase();
        FirebaseRecyclerOptions<  User > options=new  FirebaseRecyclerOptions.Builder<  User >().setQuery(FirebaseDatabase.getInstance()
                .getReference().child("Lawyers").orderByChild("fullname.toLowerCase")startAt(s).endAt(s+"\uf8ff"),  User .class).build();
        pd.dismiss();
        adapter=new CustomLawyerList(options);
        adapter.startListening();
        recview.setAdapter(adapter);*/
       final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Lawyers");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                userlist.clear();
                for(DataSnapshot ds:snapshot.getChildren())
                {
                    User user=ds.getValue(User.class);
                    if(!user.getUid().equals(firebaseUser.getUid()))
                    {
                        if(user.getFullname().toLowerCase().contains(s.toLowerCase()))
                        userlist.add(user);
                    }
                }
                adapter=new CustomLawyerList(Search_Lawyer.this,userlist);
                adapter.notifyDataSetChanged();
                recview.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    public void help(View v)

    {

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Search_Lawyer.this);
        View itemView = getLayoutInflater().inflate(R.layout.help_dialog,null);

        builder.setView(itemView);
        TextView text= itemView.findViewById(R.id.text);
        SpannableStringBuilder ssb=new SpannableStringBuilder
                ("** Scroll vertically to view all lawyers." +
                        "\n\n" +
                        "** Tap on any lawyer to view profile of that lawyer.\n\n"
                        +
                        "** You can search any lawyer with its name.");

        text.setText(ssb);
        //text.setText(ss);
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




    public void main(View v)
    {

        finish();
    }
    public void onBackPressed(View v)
    {

        finish();
    }
    }
