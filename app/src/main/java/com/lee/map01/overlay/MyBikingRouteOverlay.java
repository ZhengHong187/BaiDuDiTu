package com.lee.map01.overlay;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.lee.map01.R;
import com.lee.map01.overlayutil.BikingRouteOverlay;

public class MyBikingRouteOverlay extends BikingRouteOverlay {
    public MyBikingRouteOverlay(BaiduMap baiduMap) {
        super(baiduMap);
    }

    @Override
    public BitmapDescriptor getStartMarker() {
            return BitmapDescriptorFactory.fromResource(R.mipmap.icon_st);
    }

    @Override
    public BitmapDescriptor getTerminalMarker() {
            return BitmapDescriptorFactory.fromResource(R.mipmap.icon_en);
    }

}