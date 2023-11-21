package com.example.pakistanlawyerdiary.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.pakistanlawyerdiary.Model.CaseData;
import com.example.pakistanlawyerdiary.R;

import java.util.ArrayList;

public class CustomCaseList  extends BaseAdapter {
    private Activity context;
    ArrayList<CaseData> cd;


    public CustomCaseList(Activity context, ArrayList cd) {
        // super(context, R.layout.row_item, countries);
        this.context = context;
        this.cd = cd;

    }
    public void update(ArrayList<CaseData> results)
    {
        cd=new ArrayList<CaseData>();
        cd.addAll(results);
        notifyDataSetChanged(); // this method indicate android that our list view  needs to get refreshed
    }

    public static class ViewHolder {
        TextView textViewId;
        TextView textViewtitle;
        TextView textViewcourt;
        TextView textViewtype;
        TextView textViewNo;
        TextView textViewonbehalf;
        TextView textViewparty;
        TextView textViewprevious;
        TextView textViewadjourn;
        TextView textViewsteps;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        LayoutInflater inflater = context.getLayoutInflater();
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            row = inflater.inflate(R.layout.allcase_row_item, null, true);
            vh.textViewId = (TextView) row.findViewById(R.id.id);
            vh.textViewtitle = (TextView) row.findViewById(R.id.ct);
            vh.textViewcourt = (TextView) row.findViewById(R.id.cn);
            vh.textViewtype = (TextView) row.findViewById(R.id.ctype);
            vh.textViewNo = (TextView) row.findViewById(R.id.cnum);
            vh.textViewonbehalf = (TextView) row.findViewById(R.id.obo);
            vh.textViewparty = (TextView) row.findViewById(R.id.pn);
            vh.textViewprevious = (TextView) row.findViewById(R.id.p_date);
            vh.textViewadjourn = (TextView) row.findViewById(R.id.adj_date);
            vh.textViewsteps = (TextView) row.findViewById(R.id.steps);
            // store the holder with the view.
            row.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        String nid = String.valueOf(cd.get(position).getId());
        vh.textViewId.setText(nid);
        vh.textViewtitle.setText("  "+cd.get(position).getCasetitle());
        vh.textViewcourt.setText("  "+cd.get(position).getCourtname());
        vh.textViewtype.setText("  "+cd.get(position).getCasetype());
        vh.textViewNo.setText("  "+cd.get(position).getNo());
        vh.textViewonbehalf.setText("  "+cd.get(position).getOnbehalf());
        vh.textViewparty.setText("  "+cd.get(position).getParty());
        vh.textViewprevious.setText("  "+cd.get(position).getPreviousdate());
        vh.textViewadjourn.setText("  "+cd.get(position).getAdjourndate());
        vh.textViewsteps.setText("  "+cd.get(position).getSteps());

        return row;
    }

    public long getItemId(int position) {
        return position;
    }

    public Object getItem(int position) {
        return position;
    }

    public int getCount() {

        if (cd.size() <= 0)
            return 1;
        return cd.size();
    }
}