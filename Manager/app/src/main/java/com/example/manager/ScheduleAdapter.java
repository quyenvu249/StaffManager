package com.example.manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.manager.model.Schedule;

import java.util.ArrayList;

public class ScheduleAdapter extends BaseAdapter {
    Context context;
    ArrayList<Schedule> arrayList;
    int layout;


    public ScheduleAdapter(Context context, ArrayList<Schedule> arrayList, int layout) {
        this.context = context;
        this.arrayList = arrayList;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView tvDate, tvShift1, tvShift2;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = inflater.inflate(layout, null);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
            viewHolder.tvShift1 = (TextView) convertView.findViewById(R.id.tvShift1);
            viewHolder.tvShift2 = (TextView) convertView.findViewById(R.id.tvShift2);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvDate.setText(arrayList.get(position).date);
        viewHolder.tvShift1.setText(arrayList.get(position).dayShift + "");
        viewHolder.tvShift2.setText(arrayList.get(position).nightShift + "");
        return convertView;
    }

}
