package com.example.pakistanlawyerdiary.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.pakistanlawyerdiary.Model.HistoryData;
import com.example.pakistanlawyerdiary.R;

import java.util.ArrayList;

public class CustomHistoryList extends BaseAdapter {
    private Activity context;
    ArrayList<HistoryData> casehistory;


    public  CustomHistoryList(Activity context, ArrayList casehistory) {
        // super(context, R.layout.row_item, countries);
        this.context = context;
        this.casehistory=casehistory;
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

        ViewHolder vh;
        if(convertView==null) {
            vh=new ViewHolder();
            row = inflater.inflate(R.layout.history_row_item, null, true);

            vh.t1 = (TextView) row.findViewById(R.id.textView1);
            vh.t2 = (TextView) row.findViewById(R.id.textView2);
            vh.t3 = (TextView) row.findViewById(R.id.textView3);


            // store the holder with the view.
            row.setTag(vh);
        }
        else {
            vh = (ViewHolder) convertView.getTag();
        }



        vh.t1.setText(casehistory.get(position).getPreviousdate());
        vh.t2.setText(""+casehistory.get(position).getAdjourndate());
        vh.t3.setText("step: "+casehistory.get(position).getStep());



        return  row;
    }


    public long getItemId(int position) {
        return position;
    }

    public Object getItem(int position) {
        return position;
    }

    public int getCount() {

        if(casehistory.size()<=0)
            return 1;
        return casehistory.size();
    }
}