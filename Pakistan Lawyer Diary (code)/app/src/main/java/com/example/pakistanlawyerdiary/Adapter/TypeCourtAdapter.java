package com.example.pakistanlawyerdiary.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pakistanlawyerdiary.Database.DatabaseContract;
import com.example.pakistanlawyerdiary.Database.DatabaseHelper;
import com.example.pakistanlawyerdiary.Model.TypeCourtData;
import com.example.pakistanlawyerdiary.R;

import java.util.ArrayList;

public class TypeCourtAdapter extends BaseAdapter {
    private Activity context;
    ArrayList<TypeCourtData> tcd;

    DatabaseHelper dbHelper;
    SQLiteDatabase db;

    public TypeCourtAdapter(Activity context, ArrayList tcd) {
        // super(context, R.layout.row_item, countries);
        this.context = context;
        this.tcd = tcd;
    }

    public static class ViewHolder {
        TextView t2;
        ImageView del;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;

        LayoutInflater inflater = context.getLayoutInflater();

        final TypeCourtAdapter.ViewHolder vh;
        if (convertView == null) {
            vh = new TypeCourtAdapter.ViewHolder();
            row = inflater.inflate(R.layout.type_court_list_item, null, true);

            vh.t2 = (TextView) row.findViewById(R.id.court);
            vh.del = (ImageView) row.findViewById(R.id.delete);


            // store the holder with the view.
            row.setTag(vh);
        } else {
            vh = (TypeCourtAdapter.ViewHolder) convertView.getTag();
        }


        vh.t2.setText(tcd.get(position).getName());
        vh.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(tcd.get(position).getDataype(),  tcd.get(position).getId());


            }
        });

        return row;
    }


    public long getItemId(int position) {
        return position;
    }

    public Object getItem(int position) {
        return position;
    }

    public int getCount() {

        if (tcd.size() <= 0)
            return 1;
        return tcd.size();
    }


    public void delete(String type, final String id)
    {
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        if (type.equals("court"))
        {

            db = dbHelper.getWritableDatabase();
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.custom_main_alert);
            TextView alertt=dialog.findViewById(R.id.alerttext);
            alertt.setText("Do you really want to delete this court ?");
            Button ok=dialog.findViewById(R.id.yes);
            Button no=dialog.findViewById(R.id.no);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    Integer i = db.delete(DatabaseContract.COURTNAMES.TABLE_NAME, DatabaseContract.COURTNAMES._ID + "=?", new String[]{id});
                    if (i > 0) {
                        db.close();
                        context.recreate();

                    }


                    else {

                        db.close();
                        dialog.dismiss();
                    }
                }
            });
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();

                }
            });
            dialog.show();








        }

        else {


            db = dbHelper.getWritableDatabase();
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.custom_main_alert);
            TextView alertt=dialog.findViewById(R.id.alerttext);
            alertt.setText("Do you really want to delete this type ?");
            Button ok=dialog.findViewById(R.id.yes);
            Button no=dialog.findViewById(R.id.no);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    Integer i= db.delete(DatabaseContract.CASETYPES.TABLE_NAME, DatabaseContract.CASETYPES._ID +"=?",new String[] {id});
                    if (i > 0)
                    {
                        db.close();
                        context.recreate();
                    }



                    else {

                        db.close();
                        dialog.dismiss();
                    }
                }
            });
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();

                }
            });
            dialog.show();



        }
    }
}
