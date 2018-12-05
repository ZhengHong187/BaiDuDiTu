package com.lee.map01;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.lee.map01.adapter.SreachAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SreachActivity extends AppCompatActivity {

    @BindView(R.id.edit_sreach)
    EditText editSreach;
    @BindView(R.id.btn_sreach)
    Button btnSreach;
    @BindView(R.id.sreach_result)
    ListView sreachResult;
   
    private PoiSearch mPoiSearch;
    private SreachAdapter mSreachAdapter;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sreach_activity);
        ButterKnife.bind(this);

        mPoiSearch = PoiSearch.newInstance();   //初始化检索功能
        btnSreach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = editSreach.getText().toString().trim();
                mPoiSearch.setOnGetPoiSearchResultListener(poiLisener);
                if (!"".equals(keyword)) {
                    mPoiSearch.searchInCity((new PoiCitySearchOption())
                            .city(Common.Location_City)
                            .keyword(keyword)
                            .pageNum(10));
                }
            }
        });
    }

    //搜索监听
    public OnGetPoiSearchResultListener poiLisener = new OnGetPoiSearchResultListener() {
        @Override
        public void onGetPoiResult(PoiResult poiResult) {       //检索结果
            if (poiResult == null || poiResult.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(SreachActivity.this, "很抱歉，暂无该位置信息", Toast.LENGTH_SHORT).show();
                return;
            }
            if (poiResult.error == SearchResult.ERRORNO.NO_ERROR){
                mSreachAdapter = new SreachAdapter(SreachActivity.this, poiResult.getAllPoi());
                sreachResult.setAdapter(mSreachAdapter);
                sreachResult.setOnItemClickListener(mOnItemClickListener);
            }
        }
        @Override
        public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {     // 获取Place详情页检索结果

        }
        @Override
        public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

        }
    };
    
    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(SreachActivity.this, RoutePlanActivity.class);
            intent.putExtra("flag", "SreachActivity");
            startActivity(intent);
        }
    };
}
//jhfghfh