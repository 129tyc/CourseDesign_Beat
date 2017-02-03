package com.example.testapp2.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * Created by Code on 2016/11/3 0003.
 * 登陆页面
 */
public class LoginPage extends AppCompatActivity {
    private Toolbar toolbar;
    private TextInputLayout loginNameTextInput;
    private TextInputLayout loginPassTextInput;
    private EditText loginName;
    private EditText loginPass;
    private Button login;
    private Button forgetPass;
    private Button toRegister;
    private CheckBox checkBox;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginpage);
//        实例化数据资源
        final DataResourses dataResourses = new DataResourses(this, this);
//        实例化组件
        toolbar = (Toolbar) findViewById(R.id.loginpage_toolbar);
        toolbar.setTitle("请登陆");
        setSupportActionBar(toolbar);
        loginNameTextInput = (TextInputLayout) findViewById(R.id.loginpage_LoginName_TextInput);
        loginPassTextInput = (TextInputLayout) findViewById(R.id.loginpage_LoginPass_TextInput);
        login = (Button) findViewById(R.id.loginpage_LoginButton);
        checkBox = (CheckBox) findViewById(R.id.loginpage_checkbox);
        forgetPass = (Button) findViewById(R.id.loginpage_ForgetButton);
        toRegister = (Button) findViewById(R.id.loginpage_RegisterButton);
        loginNameTextInput.setHint("在此输入用户名");
        loginPassTextInput.setHint("在此输入密码");
        loginName = loginNameTextInput.getEditText();
        loginPass = loginPassTextInput.getEditText();
//        按回车键登陆
        loginPass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
//                    隐藏输入法
                    View view = getWindow().peekDecorView();
                    if (view != null) {
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
//                    点击登陆按钮
                    login.performClick();
                    return true;
                }
                return false;

            }
        });
        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                忘记密码提示
                Snackbar snackbar = Snackbar.make(v, "密码忘了就GG了~T_T", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });
        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                进入注册页
                Intent intent = new Intent(LoginPage.this, RegisterPage.class);
                startActivity(intent);
                finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String name = loginName.getText().toString();
                String pass = loginPass.getText().toString();
//                调用登陆操作
                new HttpUtil().actionLogin(v, LoginPage.this, name, pass, new HttpUtil.DataProcess() {
                    @Override
                    public void processComplete(Object object) {
//                        登陆成功，处理用户信息
                        User user = (User) object;
                        if (checkBox.isChecked()) {
                            user.setAlwaysOnline(true);
                        } else {
                            user.setAlwaysOnline(false);
                        }
                        user.setLocalKeepDatas(DataResourses.getKeepDataList());
                        DataResourses.setCurrentUser(user);
                        DataResourses.setIsNoUser(false);
//                        保存用户信息
                        dataResourses.saveUserInfo();
                        Log.i("Test", user.toString());
                        finish();
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
