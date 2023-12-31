package com.example.pakistanlawyerdiary.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pakistanlawyerdiary.Model.Chat;
import com.example.pakistanlawyerdiary.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter <MessageAdapter .ViewHolder> {
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private Context mcontext;
    private List<Chat> mChat;
    FirebaseUser fuser;

    public MessageAdapter(Context mcontext, List<Chat> mChat) {

        this.mcontext = mcontext;
        this.mChat = mChat;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        if(viewType==MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mcontext).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
        else
        {
            View view = LayoutInflater.from(mcontext).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position)
    {
        Chat chat=mChat.get(position);
   holder.show_message.setText(chat.getMessage());
   if(position==mChat.size()-1)
   {
       if(chat.isIsseen())
       {
           holder.seen.setVisibility(View.VISIBLE);
       }
       else
       {
           holder.sent.setVisibility(View.VISIBLE);
       }
   }
   else
   {
       holder.seen.setVisibility(View.GONE);
       holder.sent.setVisibility(View.GONE);
   }
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView show_message;
        public ImageView sent;
        public ImageView seen;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.show_message);
            sent = itemView.findViewById(R.id.sent);
            seen = itemView.findViewById(R.id.seen);
        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        if (mChat.get(position).getSender().equals(fuser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else
            {
                return MSG_TYPE_LEFT;
        }
    }
}