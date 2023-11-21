package com.example.pakistanlawyerdiary.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.pakistanlawyerdiary.Model.PartyData;
import com.example.pakistanlawyerdiary.R;

import java.util.ArrayList;

public class PartyAdapter extends BaseAdapter {
    private Activity context;
    ArrayList<PartyData> party;


    public  PartyAdapter(Activity context, ArrayList party) {
        // super(context, R.layout.row_item, countries);
        this.context = context;
        this.party=party;
    }

    public void update(ArrayList<PartyData> results)
    {
        party=new ArrayList<PartyData>();
        party.addAll(results);
        notifyDataSetChanged(); // this method indicate android that our list view  needs to get refreshed
    }
    public static class ViewHolder
    {
        TextView t1;
        TextView t2;
        TextView t3;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row=convertView;

        LayoutInflater inflater = context.getLayoutInflater();

      final PartyAdapter.ViewHolder vh;
        if(convertView==null) {
            vh=new PartyAdapter.ViewHolder();
            row = inflater.inflate(R.layout.client_row_item, null, true);

            vh.t1 = (TextView) row.findViewById(R.id.id);
            vh.t2 = (TextView) row.findViewById(R.id.name);
            vh.t3 = (TextView) row.findViewById(R.id.num);


            // store the holder with the view.
            row.setTag(vh);
        }
        else {
            vh = (PartyAdapter.ViewHolder) convertView.getTag();
        }



        vh.t1.setText(String.valueOf(party.get(position).getId()));
        vh.t2.setText(party.get(position).getClientName());
        vh.t3.setText(party.get(position).getPhone());
        vh.t3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+vh.t3.getText().toString()));
                context.startActivity(intent);

            }
        });

        return  row;
    }


    public long getItemId(int position) {
        return position;
    }

    public Object getItem(int position) {
        return position;
    }

    public int getCount() {

        if(party.size()<=0)
            return 1;
        return party.size();
    }
}

