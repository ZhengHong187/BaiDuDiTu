package com.lee.map01.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.MassTransitRouteLine;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.lee.map01.R;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.ViewHolder> {
    private MyItemClickListener mItemClickListener;
    private LayoutInflater mInflater;
    private List<? extends RouteLine> routeLines;
    private Type mtype;
    private Context mContext;

    public RouteAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from( context);
    }

    public void setDatas(List<? extends RouteLine> routeLines, Type type) {
        this.routeLines = routeLines;
        this.mtype = type;
        notifyDataSetChanged();
    }

    @Override
    public RouteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.route_item, parent, false);
        ViewHolder holder = new ViewHolder(view, mItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        
        int ti = routeLines.get(position).getDuration();
        if ( ti / 3600 == 0 ) {
            holder.routeContent1.setText( "大约需要：" + ti / 60 + "分钟" );
        } else {
            holder.routeContent1.setText( "大约需要：" + ti / 3600 + "小时" + (ti % 3600) / 60 + "分钟" );
        }
        int dis = routeLines.get(position).getDistance();
        if (dis < 1000) {
            holder.routeContent2.setText("距离大约是：" + dis + "米");
        } else {
            DecimalFormat df = new DecimalFormat("0.0");
            holder.routeContent2.setText("距离大约是：" + df.format(dis / 1000) + "公里");
        }
        switch (mtype) {
            case  TRANSIT_ROUTE:
                TransitRouteLine transitRouteLine = (TransitRouteLine) routeLines.get(position);
                StringBuilder name = new StringBuilder();
                int count = 0;
                for (int i = 0; i < transitRouteLine.getAllStep().size(); i++) {
                    if (null != transitRouteLine.getAllStep().get(i).getVehicleInfo()) {
                        if (null != transitRouteLine.getAllStep().get(i).getVehicleInfo().getTitle() && !"".equals
                                (transitRouteLine.getAllStep().get(i).getVehicleInfo().getTitle())) {
                            name.append("\n" +transitRouteLine.getAllStep().get(i).getVehicleInfo().getTitle());
                            count += transitRouteLine.getAllStep().get(i).getVehicleInfo().getPassStationNum();
                        }
                    }
                }
                holder.routeName.setText(name.toString() + "\n共" + count + "站");
                break;
            case WALKING_ROUTE:
            case BIKING_ROUTE:
                holder.routeName.setText("路线" + (position + 1));
                break;
            case DRIVING_ROUTE:
                list =  routeLines.get(position).getAllStep();
                DrivingRouteLine drivingRouteLine = (DrivingRouteLine) routeLines.get(position);
                holder.routeName.setText( "线路" + (position + 1));
                holder.routeContent1.setText( "红绿灯数：" + drivingRouteLine.getLightNum());
                holder.routeContent2.setText("拥堵距离为：" + drivingRouteLine.getCongestionDistance() + "米");
                break;
            case MASS_TRANSIT_ROUTE:
                MassTransitRouteLine massTransitRouteLine = (MassTransitRouteLine) routeLines.get(position);
                holder.routeName.setText("线路" + (position + 1));
                holder.routeContent1.setText( "预计达到时间：" + massTransitRouteLine.getArriveTime() );
                holder.routeContent2.setText("总票价：" + massTransitRouteLine.getPrice() + "元");
                break;

            default:
                break;
        }
        
    }
    List<DrivingRouteLine.DrivingStep> list;
    @Override
    public int getItemCount() {
        return routeLines.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.route_name)
        TextView routeName;
        @BindView(R.id.route_content1)
        TextView routeContent1;
        @BindView(R.id.route_content2)
        TextView routeContent2;

        private MyItemClickListener mListener;
        ViewHolder(View view, MyItemClickListener myItemClickListener) {
            super(view);
            ButterKnife.bind(this, view);
            this.mListener = myItemClickListener;
            itemView.setOnClickListener(this);
        }

        /**
         * 实现OnClickListener接口重写的方法
         * @param v
         */
        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }

        }
    }
    
    /**
     * 创建一个回调接口
     */
    public interface MyItemClickListener {
        void onItemClick(View view, int position);
    }

    /**
     * 在activity里面adapter就是调用的这个方法,将点击事件监听传递过来,并赋值给全局的监听
     *
     * @param myItemClickListener
     */
    public void setItemClickListener(MyItemClickListener myItemClickListener) {
        this.mItemClickListener = myItemClickListener;
    }
    
    
    public enum Type {
        MASS_TRANSIT_ROUTE, // 综合交通
        TRANSIT_ROUTE, // 公交
        DRIVING_ROUTE, // 驾车
        WALKING_ROUTE, // 步行
        BIKING_ROUTE // 骑行

    }
}
//jhfghfh