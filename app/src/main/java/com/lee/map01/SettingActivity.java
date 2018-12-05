package com.lee.map01;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.lee.map01.utils.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SettingActivity extends AppCompatActivity {
   @BindView(R.id.switch_show_zoom)
    Switch switch_show_zoom;
   @BindView(R.id.tv_theme)
    TextView tv_theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("设置中心");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        if (CommonUtils.getSpBool(getApplicationContext(), "showZoom", true)) {
            switch_show_zoom.setChecked(true);
        } else {
            switch_show_zoom.setChecked(false);
        }
        switch_show_zoom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    CommonUtils.setSpBool(getApplicationContext(), "showZoom", true);
                } else {
                    CommonUtils.setSpBool(getApplicationContext(), "showZoom", false);
                }
            }
        });
        tv_theme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setTitle("请选择主题");
                String[] data = {"普通模式", "黑夜模式", "清新蓝色", "午夜蓝色"};
                builder.setItems(data, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                CommonUtils.setSpInt(getApplicationContext(), "currentTheme", 0);
                                break;
                            case 1:
                                CommonUtils.setSpInt(getApplicationContext(), "currentTheme", 1);
                                break;
                            case 2:
                                CommonUtils.setSpInt(getApplicationContext(), "currentTheme", 2);
                                break;
                            case 3:
                                CommonUtils.setSpInt(getApplicationContext(), "currentTheme", 3);
                                break;
                        }
                    }
                });
                builder.show();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
