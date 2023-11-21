package com.example.pakistanlawyerdiary.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pakistanlawyerdiary.Client.Client_Chat;
import com.example.pakistanlawyerdiary.Client.Messages;
import com.example.pakistanlawyerdiary.Model.Chat;
import com.example.pakistanlawyerdiary.Model.User;
import com.example.pakistanlawyerdiary.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class LawyerAdapter extends RecyclerView.Adapter <LawyerAdapter.ViewHolder>{
    private Context mcontext;
    private List<User> musers;
    private boolean isChat;
    String theLastMessage;

    public LawyerAdapter(Context mcontext, List<  User > musers, boolean isChat)
    {

        this.mcontext=mcontext;
        this.musers=musers;
        this.isChat=isChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(isChat)
        {
             view = LayoutInflater.from(mcontext).inflate(R.layout.lawyerfragment_row_item, parent, false);
        }
        else
        {
            view = LayoutInflater.from(mcontext).inflate(R.layout.lf_row_item, parent, false);
        }
        return new LawyerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        final   User  user=musers.get(position);
        holder.lawyername.setText(user.getFullname());
        try {

            if (!user.getImage().isEmpty()) {
                Glide.with(mcontext).load(user.getImage()).into(holder.profile_image);
            } else {
                holder.profile_image.setImageResource(R.drawable.account_icon);
            }
        }
        catch(Exception e)
        {
            holder.profile_image.setImageResource(R.drawable.account_icon);
        }

        if(isChat)
        {
            lastMeassage(user.getUid(),holder.last_msg);
        }
        else
        {
            holder.last_msg.setText(user.getAddress());
        }
        if(isChat) {
            if (user.getStatus().equals("online"))
            {

                holder.img_off.setVisibility(View.INVISIBLE);
                holder.img_on.setVisibility(View.VISIBLE);
            }
            else
                {


                    holder.img_on.setVisibility(View.INVISIBLE);
                    holder.img_off.setVisibility(View.VISIBLE);
            }
        }
             else
            {
                holder.img_on.setVisibility(View.GONE);
                holder.img_off.setVisibility(View.GONE);
            }

       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(mcontext, Messages.class);
               intent.putExtra("userid",user.getUid());
               mcontext.startActivity(intent);
               ((Client_Chat)mcontext).finish();
           }
       });

    }

    @Override
    public int getItemCount()
    {
        return musers.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
public TextView lawyername;
private ImageView img_on;
        private ImageView img_off;
        private  TextView last_msg;
        private ImageView profile_image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lawyername=itemView.findViewById(R.id.name);
            img_on=itemView.findViewById(R.id.img_on);
            img_off=itemView.findViewById(R.id.img_off);
           last_msg=itemView.findViewById(R.id.last_msg);
            profile_image=itemView.findViewById(R.id.profile_img);
        }
    }


    // check for last message
    private void lastMeassage(final String userid, final TextView last_msg)
    {
        theLastMessage="default";
        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren())
                {
                    Chat chat=ds.getValue(Chat.class);
                    if(chat.getReciever().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) ||
                            chat.getReciever().equals(userid) && chat.getSender().equals(firebaseUser.getUid())  )
                    {
theLastMessage=chat.getMessage();
                    }
                }
                switch (theLastMessage)
                {
                    case"default":
                        last_msg.setText("No message");
                        break;

                    default: last_msg.setText(theLastMessage);
                    break;
                }
                theLastMessage="default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
