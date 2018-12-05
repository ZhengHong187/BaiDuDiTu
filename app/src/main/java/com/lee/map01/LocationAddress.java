package com.lee.map01;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import java.util.Comparator;
import java.util.List;


public class LocationAddress {

    private String[] location;//{时间， 纬度， 经度， 详细地址， 其他}
    public String[] getAddress() {
        return location;
    }
    private static LocationAddress mLocationAddress;
    private LocationClient mLocationClient = null;
    private Context mContext;
    private BDLocationListener myListener = new MyLocationListener();


    public static LocationAddress getInstence(){
        if (mLocationAddress == null){
            mLocationAddress = new LocationAddress();
        }
        return mLocationAddress;
    }

    public void initAddress(Context context) {
        this.mContext = context;
        mLocationClient = new LocationClient(context.getApplicationContext());

        mLocationClient.registerLocationListener(myListener);
        initLocation();
    }
    
    /**
     * 获取实时位置信息
     * LocationClientOption类，该类用来设置定位SDK的定位方式
     */
    public void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 5000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation
        // .getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(false);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);

        mLocationClient.start();
        mLocationClient.requestLocation();
    }


    public   class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            //Receive Location
            //获取定位结果
            StringBuffer sb = new StringBuffer(256);

            //            sb.append("time : ");
            sb.append(bdLocation.getTime());    //获取定位时间

            //            sb.append("\nerror code : ");
            //            sb.append(bdLocation.getLocType());    //获取类型类型

            //            sb.append("\nlatitude : ");
            sb.append("+");
            sb.append(bdLocation.getLatitude());    //获取纬度信息

            //            sb.append("\nlontitude : ");
            sb.append("+");
            sb.append(bdLocation.getLongitude());    //获取经度信息

            //            sb.append("\nradius : ");
            //            sb.append(bdLocation.getRadius());    //获取定位精准度

            //            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation){
            //
            //                // GPS定位结果
            //                sb.append("\nspeed : ");
            //                sb.append(bdLocation.getSpeed());    // 单位：公里每小时
            //
            //                sb.append("\nsatellite : ");
            //                sb.append(bdLocation.getSatelliteNumber());    //获取卫星数
            //
            //                sb.append("\nheight : ");
            //                sb.append(bdLocation.getAltitude());    //获取海拔高度信息，单位米
            //
            //                sb.append("\ndirection : ");
            //                sb.append(bdLocation.getDirection());    //获取方向信息，单位度
            //
            //                sb.append("\naddr : ");
            //                sb.append(bdLocation.getAddrStr());    //获取地址信息
            //
            //                sb.append("\ndescribe : ");
            //                sb.append("gps定位成功");
            //
            //            } else 
            if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation){

                // 网络定位结果
                //                sb.append("\naddr : ");
                sb.append("+");
                String s = bdLocation.getAddrStr() + bdLocation.getLocationDescribe();  //获取地址信息
                sb.append(bdLocation.getAddrStr());  //获取地址信息
                //                sb.append("\noperationers : ");
                //                sb.append(bdLocation.getOperators());    //获取运营商信息

                //                sb.append("\ndescribe : ");
                sb.append("+");
                sb.append("网络定位成功");

            } else if (bdLocation.getLocType() == BDLocation.TypeOffLineLocation) {

                // 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");

            } else if (bdLocation.getLocType() == BDLocation.TypeServerError) {

                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");

            } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkException) {

                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");

            } else if (bdLocation.getLocType() == BDLocation.TypeCriteriaException) {

                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");

            }

            List<Poi> list = bdLocation.getPoiList();    // POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }

            String string = sb.toString();
            location = string.split("\\+");
                
            Common.getInstense().setLATITUDE(location[1]);
            Common.getInstense().setLONGITUDE(location[2]);
            Common.getInstense().setADDRESS(location[3]);
            Common.getInstense().setRADIUS(bdLocation.getRadius());
            Toast.makeText(mContext, location[1] + "  " + location[2] + "  " + location[3], Toast.LENGTH_SHORT).show();

            

                Message message = new Message();
            message.what = 1000;
            mHandler.sendMessage(message);


            
        }


        private Handler mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1000) {
                    
                    if (mLocationClient.isStarted()) {
                        mLocationClient.stop();
                    }
                }
            }
        };



        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }
    

}
//jhfghfh