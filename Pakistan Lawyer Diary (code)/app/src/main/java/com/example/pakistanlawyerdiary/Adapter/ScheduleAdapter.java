package com.example.pakistanlawyerdiary.Adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.pakistanlawyerdiary.Model.ScheduleData;
import com.example.pakistanlawyerdiary.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ScheduleAdapter extends BaseAdapter {
    private Activity context;
    List<ScheduleData> sl;
    Date time1, time2;
    String[] s_hour;

    String[] c_hour;

    int sHOUR, sMinute;
    String sAMPM;

    int cHOUR, cMinute;
    String cAMPM;
    String istoday;

    public ScheduleAdapter(Activity context, List<ScheduleData> sl) {
        // super(context, R.layout.row_item, countries);
        this.context = context;
        this.sl = sl;

    }


    public static class ViewHolder {
        TextView textViewtitle;
        TextView textViewtype;
        TextView textViewNo;
        TextView textViewparty;
        TextView textViewdate;
        TextView textViewtime;
        TextView schedule_title;

    }


    public View getView(int position, View convertView, ViewGroup parent) {


        View row = convertView;

        LayoutInflater inflater = context.getLayoutInflater();
        ScheduleAdapter.ViewHolder vh;
        if (row == null) {
            vh = new ViewHolder();
            row = inflater.inflate(R.layout.schedule_row_item, parent, false);
            vh.schedule_title = row.findViewById(R.id.schedule_title);
            vh.textViewtitle = row.findViewById(R.id.ct);
            vh.textViewtype = row.findViewById(R.id.ctype);

            vh.textViewparty = (TextView) row.findViewById(R.id.pn);
            vh.textViewdate = (TextView) row.findViewById(R.id.date);
            vh.textViewtime = (TextView) row.findViewById(R.id.time);

            // store the holder with the view.
            row.setTag(vh);
        } else {
            vh = (ScheduleAdapter.ViewHolder) convertView.getTag();
        }

        if (sl.size() != 0) {

            vh.textViewtitle.setText("  " + sl.get(position).getTitle());
            vh.textViewtype.setText("  " + sl.get(position).getType());
            vh.textViewparty.setText("  " + sl.get(position).getPartyName());
            vh.textViewdate.setText("  " + sl.get(position).getDate());
            vh.schedule_title.setText("  " + sl.get(position).getStitle());
            istoday=sl.get(position).getIstoday().toString();
            String ttime = sl.get(position).getTime().trim();
            String currentTime = new SimpleDateFormat("h:mm:a", Locale.getDefault()).format(new Date());
            // Date date = new Date();
            if(!ttime.isEmpty()) {
                s_hour = ttime.split(":");
                sHOUR = Integer.parseInt(s_hour[0]);
                String m=s_hour[1];
                sMinute = Integer.parseInt(s_hour[1].trim());
                sAMPM = s_hour[2];
                vh.textViewtime.setText("  " + sHOUR + ":" + m + " " + sAMPM);
            }

            c_hour = currentTime.split(":");
            cHOUR = Integer.parseInt(c_hour[0]);
            cMinute = Integer.parseInt(c_hour[1].trim());
            cAMPM = c_hour[2].toLowerCase();
if(istoday.equals("1")) {
    if(!ttime.isEmpty()) {

        if (cHOUR == sHOUR) {
            if (cMinute >= sMinute) {
                if (sAMPM.equals(cAMPM)) {
                    row.setBackgroundColor(Color.GRAY);
                }
            }
        } else if (cHOUR > sHOUR) {
            if (sAMPM.equals(cAMPM)) {
                row.setBackgroundColor(Color.GRAY);
            }
        }
    }

}

        }


        return row;
    }

    public long getItemId(int position) {
        return position;
    }

    public Object getItem(int position) {
        return position;
    }

    public int getCount() {

        if (sl.size() <= 0)
            return 1;
        return sl.size();
    }


    private long getMillis(String givenTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
        try {
            Date mDate = sdf.parse(givenTime);
            long timeInMilliseconds = mDate.getTime();
            System.out.println("Date in milli :: " + timeInMilliseconds);
            return timeInMilliseconds;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
