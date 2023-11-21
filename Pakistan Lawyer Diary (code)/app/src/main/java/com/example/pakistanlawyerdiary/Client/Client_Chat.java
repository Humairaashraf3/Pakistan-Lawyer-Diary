package com.example.pakistanlawyerdiary.Client;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.pakistanlawyerdiary.Fragments.ChatsFragment;
import com.example.pakistanlawyerdiary.Fragments.LawyersFragment;
import com.example.pakistanlawyerdiary.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class Client_Chat extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client__chat);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);







        final ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());


        viewPagerAdapter.addFragment(new ChatsFragment(),"Chats");
        viewPagerAdapter.addFragment(new LawyersFragment(),"Lawyers");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        reference=FirebaseDatabase.getInstance().getReference("Chats");
       /*

         display number of unread messages
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                final ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
                int unread=0;
                for(DataSnapshot ds:snapshot.getChildren()) {
                    Chat chat = ds.getValue(Chat.class);
                    if(chat.getReciever().equals(firebaseUser.getUid())&& !chat.isIsseen())
                    {
                        unread++;
                    }
                }
                if(unread==0)
                {
                    viewPagerAdapter.addFragment(new ChatsFragment(),"Chats");
                }
                else
                {
                    viewPagerAdapter.addFragment(new ChatsFragment(),"("+unread+") Chats");
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
*/

    }


        class ViewPagerAdapter extends FragmentPagerAdapter
        {
private ArrayList<Fragment> fragments;
private ArrayList<String> titles;

            public ViewPagerAdapter(@NonNull FragmentManager fm) {
                super(fm);
                this.fragments = new ArrayList<>();
                this.titles = new ArrayList<>();
            }

            @NonNull
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {

                return fragments.size();
            }
            public void addFragment(Fragment fragment, String title)
            {
                fragments.add(fragment);
                titles.add(title);
            }
            // ctrl+o

            @Nullable
            @Override
            public CharSequence getPageTitle(int position)
            {
                return titles.get(position);

            }
        }


////         Online Status
private  void status(String status)
{
    reference= FirebaseDatabase.getInstance().getReference("Clients").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
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
        Intent i=new Intent(this, ClientMainActivity.class);
        startActivity(i);
        finish();
    }
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        // return super.onKeyDown(keyCode, event);
        Intent i=new Intent(this,ClientMainActivity.class);
        startActivity(i);
        finish();
        return true;
    }

    public void onBackPressed(View v)
    {

        Intent i=new Intent(this,ClientMainActivity.class);
        startActivity(i);
        finish();
    }
}