package com.lee.map01;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lee.map01.R;
import com.lee.map01.utils.Md5Util;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEditTextName;
    private EditText mEditTextPassword;
    private Button mLoginButton,mRegisterButton;
    private TextInputLayout mTextInputLayoutName;
    private TextInputLayout mTextInputLayoutPswd;
    private String userName,psw ,sppsw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("用户登陆");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        mTextInputLayoutName= (TextInputLayout) findViewById(R.id.textInputLayoutName);
        mTextInputLayoutPswd = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);

        mEditTextName = (EditText) findViewById(R.id.editTextName);
        mTextInputLayoutName.setErrorEnabled(true);
        mEditTextPassword = (EditText) findViewById(R.id.editTextPassword);
        mTextInputLayoutPswd.setErrorEnabled(true);
        mRegisterButton = findViewById(R.id.buttonRegister);

        mLoginButton = (Button) findViewById(R.id.buttonLogin);
        mLoginButton.setOnClickListener(this);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        mEditTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkName(s.toString(),false);
            }
        });

        mEditTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkPswd(s.toString(),false);
            }
        });
    }

    //有菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
    //返回按钮的实现
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.buttonLogin){
            hideKeyBoard();
            getEditString();
            String pswMd5 = Md5Util.encoder(psw);
            sppsw = readpsw(userName);
            if(!checkName(mEditTextName.getText(),true)) {
                return;
            }
            if(!checkPswd(mEditTextPassword.getText(),true)) {
                return;
            }
            if (pswMd5.equals(sppsw)){
                Toast.makeText(this,"登录成功",Toast.LENGTH_SHORT).show();
                saveLoginStatus(true, userName);
                Intent data = new Intent();
                //datad.putExtra( ); name , value ;
                data.putExtra("isLogin", true);
                //RESULT_OK为Activity系统常量，状态码为-1
                // 表示此页面下的内容操作成功将data返回到上一页面，如果是用back返回过去的则不存在用setResult传递data值
                setResult(RESULT_OK, data);
                finish();
            }else if (sppsw !=null&&!TextUtils.isEmpty(sppsw)&&!pswMd5.equals(sppsw)){
                Toast.makeText(LoginActivity.this,"用户名和密码不一致",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(LoginActivity.this,"用户名不存在",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveLoginStatus(boolean status, String userName) {
        SharedPreferences sp = getSharedPreferences("logininfo",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isLogin",status);
        editor.putString("loginUserName",userName);
        editor.apply();
    }

    private String readpsw(String userName) {
        SharedPreferences sp = getSharedPreferences("logininfo",MODE_PRIVATE);
        return sp.getString(userName,"");
    }

    private void getEditString() {
        userName = mEditTextName.getText().toString().trim();
        psw = mEditTextPassword.getText().toString().trim();
    }

    private boolean checkPswd(CharSequence pswd, boolean isLogin) {
        if(TextUtils.isEmpty(pswd)) {
            if(isLogin) {
                mTextInputLayoutPswd.setError("密码不能为空");
                return false;
            }
        }else{
            mTextInputLayoutPswd.setError(null);
        }
        return true;
    }

    private boolean checkName(CharSequence name, boolean isLogin) {
        if(TextUtils.isEmpty(name)) {
            if(isLogin) {
                mTextInputLayoutName.setError("用户名/Email/手机号不能为空");
                return false;
            }
        }else{
            mTextInputLayoutName.setError(null);
        }
        return true;
    }

    private void hideKeyBoard() {
        View view = getCurrentFocus();
        if(view!=null){
            ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
