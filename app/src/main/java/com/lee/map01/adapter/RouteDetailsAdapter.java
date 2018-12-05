package com.lee.map01.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lee.map01.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.id.list;


public class RouteDetailsAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<String> lists;

    public RouteDetailsAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        lists = new ArrayList<>();
    }

    public void setDatas(ArrayList<String> lists) {
        this.lists.clear();
        this.lists = lists;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.route_item_child, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        holder.details.setText(lists.get(position).toString());
        if (position == 0){
            holder.firstView.setVisibility(View.GONE);
        } 
        if (position == lists.size() - 1){
            holder.lastView.setVisibility(View.GONE);
        }

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.first_view)
        View firstView;
        @BindView(R.id.last_view)
        View lastView;
        @BindView(R.id.details)
        TextView details;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}