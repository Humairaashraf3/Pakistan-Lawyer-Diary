package com.example.pakistanlawyerdiary.Fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pakistanlawyerdiary.Adapter.LawyerAdapter;
import com.example.pakistanlawyerdiary.Lawyer.manager.NetworkManager;
import com.example.pakistanlawyerdiary.Model.User;
import com.example.pakistanlawyerdiary.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class LawyersFragment extends Fragment {


    private RecyclerView rec_view;
    private LawyerAdapter la;
    private List<User> mUsers;
 SearchView search_user;
    TextView tv;
ProgressBar pb;
    NetworkManager networkManager = new NetworkManager();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_lawyers, container, false);
        pb=view.findViewById(R.id.progressbar);
        pb.setVisibility(View.VISIBLE);
        //pd.show();
        if(!networkManager.checkInternetConnection(getContext()))
        {
            Toast.makeText(getContext(), "check your internet connection", Toast.LENGTH_SHORT).show();
        }
        tv=view.findViewById(R.id.nochats);
        rec_view=view.findViewById(R.id.recycler_view);
        rec_view.setHasFixedSize(true);
        rec_view.setLayoutManager(new LinearLayoutManager(getContext()));

        mUsers=new ArrayList<>();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Lawyers");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUsers.clear();
                if (snapshot.exists()) {
                    search_user.setVisibility(View.VISIBLE);
                    //pd.dismiss();
                    pb.setVisibility(View.GONE);
                    for (DataSnapshot ds : snapshot.getChildren())
                    {
                        User user = ds.getValue(User.class);
                        mUsers.add(user);
                    }
                    if(mUsers.size()==0)
                    {
                        search_user.setVisibility(View.INVISIBLE);
                    }
                    la = new LawyerAdapter(getContext(), mUsers, false);
                    rec_view.setAdapter(la);
                }

                else
                {
                    pb.setVisibility(View.GONE);
                    tv.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        search_user=view.findViewById(R.id.search_user);
      search_user.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
          @Override
          public boolean onQueryTextSubmit(String query)
          {
              if (!TextUtils.isEmpty(query.trim()))
              {
                  processSearch(query);
              }
              else {

                  readUsers();
              }
              return false;
          }


          @Override
          public boolean onQueryTextChange(String newText) {
              if (!TextUtils.isEmpty(newText.trim()))
              {
                  processSearch(newText);
              }
              else {

                  readUsers();
              }
              return false;
          }
      });
        return view;


    }

    private void processSearch(final String toString)
    {
        pb.setVisibility(View.VISIBLE);
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Lawyers");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUsers.clear();

                    pb.setVisibility(View.INVISIBLE);

                        for (DataSnapshot ds : snapshot.getChildren())
                        {

                            User user = ds.getValue(User.class);
                            if(user.getFullname().toLowerCase().contains(toString.toLowerCase())) {
                                mUsers.add(user);
                            }
                        la = new LawyerAdapter(getContext(), mUsers, false);
                            la.notifyDataSetChanged();
                        rec_view.setAdapter(la);

                        pb.setVisibility(View.INVISIBLE);
                    }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                pb.setVisibility(View.INVISIBLE);

            }
        });
        pb.setVisibility(View.INVISIBLE);
    }

    private void readUsers()
    {
        //FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Lawyers");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUsers.clear();

                    //pd.dismiss();
                        for (DataSnapshot ds : snapshot.getChildren())
                        {
                            User user = ds.getValue(User.class);
                            mUsers.add(user);
                        }
                        la = new LawyerAdapter(getContext(), mUsers, false);
                        rec_view.setAdapter(la);
                    }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }





}