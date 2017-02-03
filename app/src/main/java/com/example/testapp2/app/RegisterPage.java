package com.example.testapp2.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Code on 2016/11/3 0003.
 * 用户注册页
 */
public class RegisterPage extends AppCompatActivity {
    private Toolbar toolbar;
    private TextInputLayout registerNameTextInput;
    private TextInputLayout registerPassTextInput;
    private TextInputLayout registerPassTwiceTextInput;
    private EditText registerName;
    private EditText registerPass;
    private EditText registerPassTwice;
    private Button register;
    private final static int ISREPEAT = 1;
    private final static int NOTREPEAT = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registerpage);
        toolbar = (Toolbar) findViewById(R.id.registerpage_toolbar);
        toolbar.setTitle("请注册");
        setSupportActionBar(toolbar);
//        实例化组件
        registerNameTextInput = (TextInputLayout) findViewById(R.id.registerpage_RegisterName_TextInput);
        registerPassTextInput = (TextInputLayout) findViewById(R.id.registerpage_RegisterPass_TextInput);
        registerPassTwiceTextInput = (TextInputLayout) findViewById(R.id.registerpage_RegisterPassTwice_TextInput);
        registerName = registerNameTextInput.getEditText();
        registerPass = registerPassTextInput.getEditText();
        registerPassTwice = registerPassTwiceTextInput.getEditText();
        registerNameTextInput.setHint("请设置想要的用户名,仅允许大小写字母和数字");
        registerPassTextInput.setHint("请设置一个不安全的密码");
        registerPassTwiceTextInput.setHint("请再输一遍密码");
//        设置handler处理用户名查询返回消息
        final Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == ISREPEAT) {
//                    用户名重复
                    registerNameTextInput.setErrorEnabled(true);
                    registerNameTextInput.setError("用户名与已有的重复！");
                } else if (msg.what == NOTREPEAT) {
                    registerNameTextInput.setErrorEnabled(false);
                }
                return true;
            }
        });
//        设置文字监听器判断用户名是否合规
        registerName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
//                    用户名长度不等于0
                    registerNameTextInput.setErrorEnabled(true);
                    registerNameTextInput.setError("请创建用户名！");
                } else if (s.length() > 64) {
//                    用户名长度小于64
                    registerNameTextInput.setErrorEnabled(true);
                    registerNameTextInput.setError("用户名过长！");
                } else {
//                    符合规范，调用方法查询是否重复
                    new HttpUtil().actionQueryIsRepeat(s.toString(), new HttpUtil.DataProcess() {
                        @Override
                        public void processComplete(Object object) {
                            if (String.valueOf(object).equals("0")) {
                                handler.sendEmptyMessage(ISREPEAT);
                            } else {
                                handler.sendEmptyMessage(NOTREPEAT);
                            }
                        }
                    });
                }
            }
        });
//        设置密码监听器判断密码是否合规
        registerPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                registerPassTextInput.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    registerPassTextInput.setErrorEnabled(true);
                    registerPassTextInput.setError("请创建密码！");
                } else {
                    registerPassTextInput.setErrorEnabled(false);
                }
                if (s.length() > 64) {
                    registerPassTextInput.setErrorEnabled(true);
                    registerPassTextInput.setError("密码过长！");
                } else {
                    registerPassTextInput.setErrorEnabled(false);
                }
            }
        });
//        判断两次输入密码是否相等
        registerPassTwice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                registerPassTwiceTextInput.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!registerPassTwice.getText().toString().equals(registerPass.getText().toString())) {
                    registerPassTwiceTextInput.setErrorEnabled(true);
                    registerPassTwiceTextInput.setError("两次输入不一致！");
                } else {
                    registerPassTwiceTextInput.setErrorEnabled(false);
                }
            }
        });
        register = (Button) findViewById(R.id.registerpage_RegisterButton);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                若用户不输入直接点击注册，判断
                if (registerName.getText().toString().equals("")) {
                    registerNameTextInput.setErrorEnabled(true);
                    registerNameTextInput.setError("用户名不能为空！");
                } else if (registerPass.getText().toString().equals("")) {
                    registerPassTextInput.setErrorEnabled(true);
                    registerPassTextInput.setError("密码不能为空！");
                } else if (!registerPassTwiceTextInput.isErrorEnabled()) {
//                    调用方法进行注册
                    new HttpUtil().actionRegister(v, RegisterPage.this, registerName.getText().toString(), registerPass.getText().toString(), new HttpUtil.DataProcess() {
                        @Override
                        public void processComplete(Object object) {
                            Intent intent = new Intent(RegisterPage.this, LoginPage.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }
        });
    }
}
