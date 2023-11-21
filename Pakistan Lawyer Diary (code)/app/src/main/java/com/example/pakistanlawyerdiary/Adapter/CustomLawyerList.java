package com.example.pakistanlawyerdiary.Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pakistanlawyerdiary.Client.Lawyer_Details;
import com.example.pakistanlawyerdiary.Model.User;
import com.example.pakistanlawyerdiary.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomLawyerList extends RecyclerView.Adapter<CustomLawyerList.MyHolder> {

Context context;
List<User> users;

   public CustomLawyerList(Context context, List<User> users) {
      this.context = context;
      this.users = users;
   }

   @NonNull
   @Override
   public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
   {
      View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.lawyer_row_item,parent,false);
      return new MyHolder(view);
   }

   @Override
   public void onBindViewHolder(@NonNull final MyHolder holder, int position) {


      final String lid= users.get(position).getUid();
     /* final String ad=model.getAddress();
      final String n=model.getFullname();
      final String e=model.getEmail();
      final String p=model.getPhone();*/

      try {

         if (!users.get(position).getImage().isEmpty()) {
            Glide.with(context).load(users.get(position).getImage()).into(holder.profile_image);
         } else {
            holder.profile_image.setImageResource(R.drawable.account_icon);
         }
      }
      catch(Exception e)
      {
         holder.profile_image.setImageResource(R.drawable.account_icon);
      }
     float r=(float)users.get(position).getRatingValue();
     holder.name.setText(" "+users.get(position).getFullname());
    holder.number.setText(""+users.get(position).getPhone());
    holder.lawyer_rating.setRating(r);
      holder.l.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v)
         {

            Intent i=new Intent(v.getContext(), Lawyer_Details.class);
            i.putExtra("id",lid);
            v.getContext().startActivity(i);
           // ((Search_Lawyer)context).finish();

         }
      });

     /* holder.number.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Intent intent=new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:"+holder.number.getText().toString()));
            v.getContext().startActivity(intent);
         }
      });*/

   }

   @Override
   public int getItemCount() {
      return users.size();
   }

   class MyHolder extends RecyclerView.ViewHolder {
      TextView name;
      TextView number;
      RatingBar lawyer_rating;
      LinearLayout l;
      CircleImageView profile_image;

      public MyHolder(@NonNull View itemView) {
         super(itemView);
         name=(TextView)itemView.findViewById(R.id.name);
         number=(TextView)itemView.findViewById(R.id.number);
         l=(LinearLayout)itemView.findViewById(R.id.layout);
         lawyer_rating=(RatingBar)itemView.findViewById(R.id.lawyer_rating);
         profile_image=(CircleImageView)itemView.findViewById(R.id.profile_img);
      }
   }

}

/*public class CustomLawyerList extends FirebaseRecyclerAdapter<  User ,CustomLawyerList.myviewHolder>


{


   /**
    * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
    * {@link FirebaseRecyclerOptions} for configuration options.
    *
    * @param options

   public CustomLawyerList(@NonNull FirebaseRecyclerOptions<User> options) {
      super(options);
   }

   @Override
   protected void onBindViewHolder(@NonNull final myviewHolder holder, int position, @NonNull   User model)

   {
      final String lid=model.getUid();
     /* final String ad=model.getAddress();
      final String n=model.getFullname();
      final String e=model.getEmail();
      final String p=model.getPhone();*/
     /* float r=(float)model.getRatingValue();
     holder.name.setText(" "+model.getFullname());
    holder.number.setText(""+model.getPhone());
      holder.lawyer_rating.setRating(r);
      holder.l.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v)
         {

            Intent i=new Intent(v.getContext(), Lawyer_Details.class);
            i.putExtra("id",lid);
            v.getContext().startActivity(i);


         }
      });

      holder.number.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Intent intent=new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:"+holder.number.getText().toString()));
            v.getContext().startActivity(intent);
         }
      });
   }

   @NonNull
   @Override
   public myviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.lawyer_row_item,parent,false);
      return new myviewHolder(view);
   }

   class myviewHolder extends RecyclerView.ViewHolder
   {
      TextView name;
      TextView number;
      RatingBar lawyer_rating;
      LinearLayout l;

      public myviewHolder(@NonNull View itemView) {
         super(itemView);
         name=(TextView)itemView.findViewById(R.id.name);
         number=(TextView)itemView.findViewById(R.id.number);
         l=(LinearLayout)itemView.findViewById(R.id.layout);
         lawyer_rating=(RatingBar)itemView.findViewById(R.id.lawyer_rating);
      }
   }
}
/*
public class CustomLawyerList extends RecyclerView.Adapter<CustomLawyerList.MyviewHolder>
{
ArrayList<User> list;


   public CustomLawyerList( ArrayList<User> list) {
      this.list=list;
   }

   @NonNull
   @Override
   public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.lawyer_row_item,parent,false);
      return new MyviewHolder(view);
   }

   @Override
   public void onBindViewHolder(@NonNull MyviewHolder holder, int position)
   {
      holder.name.setText(" "+list.get(position).getFullname());
      holder.email.setText(" "+list.get(position).getEmail());
      holder.number.setText(" "+list.get(position).getPhone());

   }

   @Override
   public int getItemCount() {
      return list.size();
   }

   class MyviewHolder extends RecyclerView.ViewHolder
   {
      TextView name;
      TextView email;
      TextView number;
      LinearLayout l;
      public MyviewHolder(@NonNull View itemView) {
         super(itemView);

         name=(TextView)itemView.findViewById(R.id.name);
         email=(TextView)itemView.findViewById(R.id.email);
         number=(TextView)itemView.findViewById(R.id.number);
         l=(LinearLayout)itemView.findViewById(R.id.layout);
      }
   }*/
  /* List<User> user;

   public CustomLawyerList(List<User> user) {
      this.user = user;
   }

}*/
