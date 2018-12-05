package com.lee.map01;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.lee.map01.utils.Md5Util;

public class RegisterActivity extends AppCompatActivity {
    private EditText mRegTextName,mRegTextPassword,mRegTextRePassword;
    private Button mRegister;
    private TextInputLayout mTextInputLayoutRegName,mTextInputLayoutRegPassword,mTextInputLayoutRegRePassword;
    private String userName,psw,pswAgain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("注册");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        init();
    }

    private void init() {
        mRegister = findViewById(R.id.Register);
        mRegTextName = findViewById(R.id.RegTextName);
        mRegTextPassword = findViewById(R.id.RegTextPassword);
        mRegTextRePassword = findViewById(R.id.RegTextRePassword);
        mTextInputLayoutRegName = findViewById(R.id.textInputLayoutRegName);
        mTextInputLayoutRegPassword = findViewById(R.id.textInputLayoutRegPassword);
        mTextInputLayoutRegRePassword = findViewById(R.id.textInputLayoutRegRePassword);

        //用户名
        mRegTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkRegName(s.toString(),false);
            }
        });
        mRegTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkRegpassword(s,false);
            }
        });
        mRegTextRePassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkRegRepassword(s,false);
            }
        });

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkRegName(mRegTextName.getText(),true)){
                    return;
                }
                if (!checkRegpassword(mRegTextPassword.getText(),true)){
                    return;
                }
                if (!checkRegRepassword(mRegTextRePassword.getText(),true)){
                    return;
                }
                getEditString();

                if (!psw.equals(pswAgain)){
                    Toast.makeText(RegisterActivity.this,"密码不一致",Toast.LENGTH_SHORT).show();
                }else if (isExistUserName(userName)) {
                    Toast.makeText(RegisterActivity.this,"用户名已存在",Toast.LENGTH_SHORT).show();
                }else {
                    /**
                     * 保存账号和密码到SharedPreferences中
                     */
                    saveRegisterInfo(userName, psw);
                    Toast.makeText(RegisterActivity.this,"恭喜注册成功",Toast.LENGTH_SHORT).show();
                    Intent data = new Intent();
                    data.putExtra("userName", userName);
                    setResult(RESULT_OK, data);
                    //RESULT_OK为Activity系统常量，状态码为-1，
                    // 表示此页面下的内容操作成功将data返回到上一页面，如果是用back返回过去的则不存在用setResult传递data值
                    RegisterActivity.this.finish();
                }
            }
        });

    }
    /**
     * 保存账号和密码到SharedPreferences中
     */
    private void saveRegisterInfo(String userName, String psw) {
        String pswMd5 = Md5Util.encoder(psw);
        SharedPreferences sp = getSharedPreferences("logininfo",MODE_PRIVATE);
        //获取编辑器， SharedPreferences.Editor  editor -> sp.edit();
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(userName,pswMd5).apply();
    }


    private boolean isExistUserName(String mRegTextName) {
        boolean hasUserName = false;
        SharedPreferences sp = getSharedPreferences("logininfo",MODE_PRIVATE);
        String spPsw = sp.getString(userName, "");//获取密码
        if (!TextUtils.isEmpty(spPsw)){
            return hasUserName = true;
        }
        return hasUserName = false;

    }

    private void getEditString() {
        userName = mRegTextName.getText().toString().trim();
        psw = mRegTextPassword.getText().toString().trim();
        pswAgain = mRegTextRePassword.getText().toString().trim();

    }

    private boolean checkRegRepassword(Editable RegRepassword, boolean isRegister) {
        if (TextUtils.isEmpty(RegRepassword)){
            if (isRegister){
                mTextInputLayoutRegPassword.setError("请确认密码");
                return false;
            }
        }else {
            mTextInputLayoutRegPassword.setError(null);
        }
        return true;
    }

    private boolean checkRegpassword(CharSequence Regpassword, boolean isRegister) {
        if (TextUtils.isEmpty(Regpassword)){
            if (isRegister){
                mTextInputLayoutRegRePassword.setError("请输入密码");
                return false;
            }
        }else {
            mTextInputLayoutRegRePassword.setError(null);
        }
        return true;
    }

    private boolean checkRegName(CharSequence Regname, boolean isRegister) {
        if (TextUtils.isEmpty(Regname)){
            if (isRegister){
                mTextInputLayoutRegName.setError("用户名/Email/手机号不能为空");
                return false;
            }
        }else {
            mTextInputLayoutRegName.setError(null);
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
