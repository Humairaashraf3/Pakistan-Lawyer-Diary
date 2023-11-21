package com.example.pakistanlawyerdiary.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.pakistanlawyerdiary.Lawyer.Reports.ReportCaseData;
import com.example.pakistanlawyerdiary.R;

import java.util.ArrayList;

public class ReportAdapter extends BaseAdapter {
    private Activity context;
    ArrayList<ReportCaseData> cd;
    public ReportAdapter(Activity context, ArrayList cd) {
        this.context = context;
        this.cd = cd;

    }

    public static class ViewHolder {
        TextView textViewNumber;
        TextView textViewtitle;
        TextView textViewcourt;
        TextView textViewtype;
        TextView textViewCNo;
        TextView textViewCYear;
        TextView textViewonbehalf;
        TextView textViewparty;
        TextView textViewPartyNo;
        TextView textViewAdverse;
        TextView textViewAdverseNo;
        TextView textViewRespo;
        TextView textViewUsec;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        LayoutInflater inflater = context.getLayoutInflater();
        ReportAdapter.ViewHolder vh;
        if (convertView == null) {
            vh = new ReportAdapter.ViewHolder();
            row = inflater.inflate(R.layout.report_row_item, null, true);
            vh.textViewNumber = (TextView) row.findViewById(R.id.count);
            vh.textViewtitle = (TextView) row.findViewById(R.id.title);
            vh.textViewcourt = (TextView) row.findViewById(R.id.court_name);
            vh.textViewtype = (TextView) row.findViewById(R.id.type);
            vh.textViewCNo = (TextView) row.findViewById(R.id.case_no);
            vh.textViewCYear = (TextView) row.findViewById(R.id.case_year);
            vh.textViewonbehalf = (TextView) row.findViewById(R.id.obo);
            vh.textViewparty = (TextView) row.findViewById(R.id.party_name);
            vh.textViewPartyNo = (TextView) row.findViewById(R.id.no);
            vh.textViewAdverse = (TextView) row.findViewById(R.id.adverse_adv);
            vh.textViewAdverseNo = (TextView) row.findViewById(R.id.adverse_adv_no);
            vh.textViewRespo = (TextView) row.findViewById(R.id.respodent);
            vh.textViewUsec= (TextView) row.findViewById(R.id.usec);
            // store the holder with the view.
            row.setTag(vh);
        } else

            {

            vh = (ReportAdapter.ViewHolder) convertView.getTag();
        }

        String no = String.valueOf(cd.get(position).getCno());
        vh.textViewNumber.setText(no);
        vh.textViewtitle.setText("  "+cd.get(position).getCasetitle());
        vh.textViewcourt.setText("  "+cd.get(position).getCourtname());
        vh.textViewtype.setText("  "+cd.get(position).getCasetype());
        vh.textViewCNo.setText("  "+cd.get(position).getCasenumber());
        vh.textViewCYear.setText("  "+cd.get(position).getCaseyear());
        vh.textViewonbehalf.setText("  "+cd.get(position).getOnbehalf());
        vh.textViewparty.setText("  "+cd.get(position).getParty());
        vh.textViewPartyNo.setText("  "+cd.get(position).getPartyNo());
        vh.textViewAdverse.setText("  "+cd.get(position).getAdverse());
        vh.textViewAdverseNo.setText("  "+cd.get(position).getAdverseNo());
        vh.textViewRespo.setText("  "+cd.get(position).getRespo());
        vh.textViewUsec.setText("  "+cd.get(position).getAusec());

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
