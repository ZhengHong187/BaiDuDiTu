package com.lee.map01;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.lee.map01.adapter.RouteDetailsAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;



public class RouteDetailsActivity extends AppCompatActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.listview)
    ListView listview;
    
    ArrayList<String> mStrings;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route_details);
        ButterKnife.bind(this);
        
        title.setText("起点：" + Common.Location_Address + "\n终点：" + Common.Location_End);
        
        mStrings = new ArrayList<>();
        mStrings = getIntent().getStringArrayListExtra("STRING");
        RouteDetailsAdapter adapter = new RouteDetailsAdapter(this);
        listview.setAdapter(adapter);
        adapter.setDatas(mStrings);
        
        
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
//jhfghfh