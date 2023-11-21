package com.example.pakistanlawyerdiary.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.pakistanlawyerdiary.Lawyer.Reminder.ReminderData;
import com.example.pakistanlawyerdiary.R;

import java.util.ArrayList;

public class ReminderAdapter extends BaseAdapter {
    private Activity context;
    ArrayList<ReminderData> rd;

    public ReminderAdapter (Activity context, ArrayList rd) {

        this.context = context;
        this.rd = rd;

    }


    public static class ViewHolder {
        TextView textViewtitle;
        TextView textViewDay;
        TextView textViewMonth;
        TextView textViewTime;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        LayoutInflater inflater = context.getLayoutInflater();
        ReminderAdapter.ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            row = inflater.inflate(R.layout.reminder_row_item, null, true);
            vh.textViewtitle = (TextView) row.findViewById(R.id.title);
            vh.textViewDay = (TextView) row.findViewById(R.id.day);
            vh.textViewMonth = (TextView) row.findViewById(R.id.month);
            vh.textViewTime= (TextView) row.findViewById(R.id.time);
            // store the holder with the view.
            row.setTag(vh);
        } else {
            vh = (ReminderAdapter.ViewHolder) convertView.getTag();
        }

        vh.textViewtitle.setText(rd.get(position).getReminder_title());
        vh.textViewDay.setText(rd.get(position).getDate());
        vh.textViewMonth.setText(rd.get(position).getMonth());
        vh.textViewTime.setText(rd.get(position).getTime());


        return row;
    }


















    @Override
    public int getCount() {
        if (rd.size() <= 0)
            return 1;
        return rd.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
