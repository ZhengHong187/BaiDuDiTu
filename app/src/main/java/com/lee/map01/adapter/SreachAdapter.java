package com.lee.map01.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.lee.map01.Common;
import com.lee.map01.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SreachAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private List<PoiInfo> infos;

    public SreachAdapter(Context context, List<PoiInfo> infos) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.infos = infos;
    }

    @Override
    public int getCount() {
        return infos.size();
    }

    @Override
    public Object getItem(int position) {
        return infos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.sreach_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        holder.addressName.setText(infos.get(position).name);
        holder.addressDetails.setText(infos.get(position).city + infos.get(position).address);
        if (null != infos.get(position).phoneNum){
            holder.addressPhone.setText(infos.get(position).phoneNum);
        }
        Common.Location_End = infos.get(position).name;
        Common.Sreach_City = infos.get(position).city;
        Common.Sreach_Latitude = infos.get(position).location.latitude;
        Common.Sreach_Longitude = infos.get(position).location.longitude;
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.address_name)
        TextView addressName;
        @BindView(R.id.address_details)
        TextView addressDetails;
        @BindView(R.id.address_phone)
        TextView addressPhone;
        

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}