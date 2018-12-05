package com.lee.map01;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.busline.BusLineSearch;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.lee.map01.adapter.SreachAdapter;
import com.lee.map01.overlay.MyOverLay;
import com.lee.map01.overlayutil.PoiOverlay;
import com.lee.map01.utils.CommonUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    @BindView(R.id.bmapView)
    MapView mMapView;
    @BindView(R.id.map_normal)
    Button mapNormal;
    @BindView(R.id.map_satellite)
    Button mapSatellite;
    @BindView(R.id.map_blank)
    Button mapBlank;
    @BindView(R.id.map_traffic)
    Button mapTraffic;
    @BindView(R.id.map_heat)
    Button mapHeat;
    @BindView(R.id.activity_main)
    RelativeLayout activityMain;
    @BindView(R.id.location)
    FloatingActionButton location;
    @BindView(R.id.top_btn)
    LinearLayout topBtn;
    @BindView(R.id.main_list)
    ListView mainList;
    private BaiduMap mBaiduMap = null;
    private boolean ISOPEN_TRAFFIC, ISOPEN_HEAT;
    private Marker marker;
    // 定位相关声明  
    public LocationClient locationClient = null;
    private PoiSearch mPoiSearch;
    private String cityName;
    private BusLineSearch mBusLineSearch;
    private BitmapDescriptor mDescriptor;
    private MyLocationConfiguration.LocationMode mLocationMode;
    private String busLineId;
    private PoiNearbySearchOption nearbySearchOption;
    private SreachAdapter mSreachAdapter;
    private InfoWindow mInfoWindow;
    private BitmapDescriptor bitmap;
    private GeoCoder mSreach = null;
    private String addressStr;
    private Toolbar toolbar;
    private ImageView mImageView;//头像点击
//    private int currentTheme;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        int type = CommonUtils.getSpInt(getApplicationContext(), "currentTheme", 0);
//        currentTheme = type;
        ButterKnife.bind(this);

        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mLocationMode = MyLocationConfiguration.LocationMode.FOLLOWING;     //跟随
        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(mLocationMode, true, mDescriptor));
        locationClient = new LocationClient(getApplicationContext()); // 实例化LocationClient类
        locationClient.registerLocationListener(myListener); // 注册监听函数
        this.setLocationOption();    //设置定位参数
        locationClient.start(); // 开始定位

        mPoiSearch = PoiSearch.newInstance();   //初始化检索功能
        mSreach = GeoCoder.newInstance();
        mSreach.setOnGetGeoCodeResultListener(listener);

        setDrawer();
        setLinster();
    }

    private void setDrawer() {
        /**
         使用DrawerLayout实现侧滑功能
         */
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mImageView = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.ivAvatar);
        mImageView.setOnClickListener(this);
    }


    /*
     * 侧滑菜单开关*/
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*
     * 右菜单点击事件*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //右菜单点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this,SettingActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //子菜单点击事件
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        String string = null;
        switch (id){
            case R.id.jiayou:
                string = "加油";
                sreachNearBy(string);
                break;
            case R.id.yiyuan:
                string = "医院";
                sreachNearBy(string);
                break;
            case R.id.meishi:
                string = "美食";
                sreachNearBy(string);
                break;
            case R.id.jiudian:
                string = "酒店";
                sreachNearBy(string);
                break;
            case R.id.sousuo:
                startActivity(new Intent(this, SreachActivity.class));
                break;
            case R.id.jingdian:
                string = "景点";
                sreachNearBy(string);
                break;
            case R.id.nav_setting: //设置
                Intent intent = new Intent(MainActivity.this,SettingActivity.class);
                startActivity(intent);
                default:
                    break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivAvatar:
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
        }

    }

     /**
     *>>>>>>>>>>>>>>>>>>>>>>>>以下是地图相关的代码<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<*/

    OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
        public void onGetGeoCodeResult(GeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                //没有检索到结果  
                return;
            }
            //获取地理编码结果  
        }
        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                //没有找到检索结果  
                return;
            }
            //获取反向地理编码结果  
            addressStr = result.getAddressDetail().street + result.getAddressDetail().streetNumber
                    + "\n" + result.getSematicDescription();
        }
    };
    
    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(MainActivity.this, RoutePlanActivity.class);
            intent.putExtra("flag", "MainActivity");
            startActivity(intent);
        }
    };

    //定位监听
    public BDLocationListener myListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdlocation) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null)
                return;
            MyLocationData locData = new MyLocationData.Builder().accuracy(bdlocation.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(bdlocation.getLatitude()).longitude(bdlocation.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);    //设置定位数据
            cityName = bdlocation.getCity();
            cityName = cityName.substring(0, cityName.length() - 1);
            Common.Location_City = cityName;
            Common.Location_Address = bdlocation.getAddress().district + bdlocation.getAddress().street;
            Common.Location_Latitude = bdlocation.getLatitude();
            Common.Location_Longitude = bdlocation.getLongitude();
            LatLng ll = new LatLng(bdlocation.getLatitude(), bdlocation.getLongitude());
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll).zoom(19.0f);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {
        }
    };

    //搜索监听
    public OnGetPoiSearchResultListener poiLisener = new OnGetPoiSearchResultListener() {
        @Override
        public void onGetPoiResult(PoiResult poiResult) {       //检索结果
            if (poiResult == null || poiResult.error != SearchResult.ERRORNO.NO_ERROR) {
                return;
            }
            if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {
                mBaiduMap.clear();
                //创建PoiOverlay  
                PoiOverlay overlay = new MyOverLay(mBaiduMap);
                //设置overlay可以处理标注点击事件  
                mBaiduMap.setOnMarkerClickListener(overlay);
                //设置PoiOverlay数据  
                overlay.setData(poiResult);
                //添加PoiOverlay到地图中  
                overlay.addToMap();
                overlay.zoomToSpan();
                mainList.setVisibility(View.VISIBLE);
                mSreachAdapter = new SreachAdapter(MainActivity.this, poiResult.getAllPoi());              
                mainList.setAdapter(mSreachAdapter);
                mainList.setOnItemClickListener(mOnItemClickListener);
                return;
            }
        }

        @Override
        public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {     // 获取Place详情页检索结果

        }

        @Override
        public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

        }
    };



    public void setLinster() {
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //点击地图任意位置获取该位置的经纬度坐标
//                PoiSearch             
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() { //点击覆盖物事件
            @Override
            public boolean onMarkerClick(Marker arg0) {
                LatLng la = new LatLng(arg0.getPosition().latitude, arg0.getPosition().longitude);
                showPopu(la);
                return true;
            }
        });
    }
    
    public void showPopu(final LatLng latlng){
        mSreach.reverseGeoCode(new ReverseGeoCodeOption().location(latlng));
        View view = getLayoutInflater().inflate(R.layout.map_popu, null, false);
        TextView text = (TextView) view.findViewById(R.id.my_postion);
        text.setText(addressStr);
        InfoWindow info = new InfoWindow(view, latlng, -47);
        mBaiduMap.showInfoWindow(info);
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(latlng);
        mBaiduMap.setMapStatus(update);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.Location_End = addressStr;
                Common.Sreach_Latitude = latlng.latitude;
                Common.Sreach_Longitude = latlng.longitude;
                Intent intent = new Intent(MainActivity.this, RoutePlanActivity.class);
                intent.putExtra("flag", "MainActivity");
                startActivity(intent);
            }
        });
    }
    
    /**
     * 设置定位参数
     */
    private void setLocationOption() {
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开GPS
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
        option.setCoorType("bd09ll"); // 返回的定位结果是百度经纬度,默认值gcj02
        //        option.setScanSpan(5000); // 设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true); // 返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true); // 返回的定位结果包含手机机头的方向

        locationClient.setLocOption(option);
    }

    //设置Markerr
    public void setMark() {
        LatLng point = new LatLng(Double.parseDouble(Common.getInstense().getLATITUDE()), Double.parseDouble(Common
                .getInstense().getLONGITUDE()));
        //构建Marker图标  
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
        //构建MarkerOption，用于在地图上添加Marker  
        OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
        //在地图上添加Marker，并显示  
        mBaiduMap.addOverlay(option);
        //设置可拖拽maker
        OverlayOptions options = new MarkerOptions().position(point)  //设置marker的位置
                .icon(bitmap)  //设置marker图标
                .zIndex(9)  //设置marker所在层级
                .draggable(true);  //设置手势拖拽
        //将marker添加到地图上
        marker = (Marker) (mBaiduMap.addOverlay(options));

        //调用BaiduMap对象的setOnMarkerDragListener方法设置marker拖拽的监听
        mBaiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
            public void onMarkerDrag(Marker marker) {
                //拖拽中
            }

            public void onMarkerDragEnd(Marker marker) {
                //拖拽结束
            }

            public void onMarkerDragStart(Marker marker) {
                //开始拖拽
            }
        });

        //marker设置透明度
        MarkerOptions ooA = new MarkerOptions().position(point).icon(bitmap).zIndex(0).period(10).alpha(0.5f);
        mBaiduMap.addOverlay(ooA);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出时销毁定位  
        locationClient.stop();
        // 关闭定位图层  
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        int type = CommonUtils.getSpInt(getApplicationContext(), "currentTheme", 0);
//        if (type != currentTheme) {
//            startActivity(new Intent(this, MainActivity.class));
//            overridePendingTransition(0, 0);
//            finish();
//            System.exit(0);
//        }
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
        if (CommonUtils.getSpBool(getApplicationContext(), "showZoom", true)) {
            mMapView.showZoomControls(true);
        } else {
            mMapView.showZoomControls(false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理  
        mMapView.onPause();
    }

    @OnClick({R.id.map_normal, R.id.map_satellite, R.id.map_blank, R.id.map_traffic, R.id.map_heat, R.id.location})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.map_normal:       //普通
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                break;
            case R.id.map_satellite:    //卫星
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.map_blank:        //空白
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NONE);
                break;
            case R.id.map_traffic:      //交通
                if (ISOPEN_TRAFFIC) {
                    mBaiduMap.setTrafficEnabled(false);
                    ISOPEN_TRAFFIC = false;
                } else {
                    mBaiduMap.setTrafficEnabled(true);
                    ISOPEN_TRAFFIC = true;
                }
                break;
            case R.id.map_heat:         //热力
                if (ISOPEN_HEAT) {
                    mBaiduMap.setBaiduHeatMapEnabled(false);
                    ISOPEN_HEAT = false;
                } else {
                    mBaiduMap.setBaiduHeatMapEnabled(true);
                    ISOPEN_HEAT = true;
                }
                break;
            case R.id.location:       //定位
                locationClient = new LocationClient(getApplicationContext()); // 实例化LocationClient类
                locationClient.registerLocationListener(myListener); // 注册监听函数
                this.setLocationOption();    //设置定位参数
                locationClient.start(); // 开始定位
                break;
            default:
                break;

        }
    }

    //查找搜索
    public void sreachNearBy(String keyWord) {
        mPoiSearch.setOnGetPoiSearchResultListener(poiLisener);
        nearbySearchOption = new PoiNearbySearchOption();
        nearbySearchOption.keyword(keyWord).location(new LatLng(Common.Location_Latitude, Common.Location_Longitude))
                .radius(3000)       //半径 米
                .pageCapacity(50);      //默认条数
        mPoiSearch.searchNearby(nearbySearchOption);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        View view = getCurrentFocus();
        mainList.setVisibility(View.GONE);
        return true;
    }

}
