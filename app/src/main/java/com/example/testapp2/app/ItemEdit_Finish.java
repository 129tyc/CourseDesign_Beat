package com.example.testapp2.app;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Code on 2016/10/16 0016.
 * 完成编辑页面
 */
public class ItemEdit_Finish extends AppCompatActivity {
    private TextInputLayout textInputLayout;
    private TextInputLayout textInputLayout_keepType;
    private EditText keepName;
    private EditText keepType;
    private Button editOk;
    private KeepData keepData;
//    private DataResourses dataResourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        在边界外触摸则结束
        setFinishOnTouchOutside(true);
        setContentView(R.layout.item_edit_finish_dialog);
        Toolbar toolbar = (Toolbar) findViewById(R.id.itemFinishToolBar);
        toolbar.setTitle("最后一步");
        toolbar.setSubtitle("名称与属性");
        setSupportActionBar(toolbar);
//        dataResourses = new DataResourses(this, this);
        keepData = DataResourses.getEditableKeepData();
//        实例化组件
        textInputLayout = (TextInputLayout) findViewById(R.id.textInput);
        textInputLayout_keepType = (TextInputLayout) findViewById(R.id.textInput_keepType);
        editOk = (Button) findViewById(R.id.itemEdit_OK);
        textInputLayout.setHint("请输入锻炼名称");
        textInputLayout_keepType.setHint("请输入运动类型");
        keepName = textInputLayout.getEditText();
//        当编辑框中文字改变时进行不为空判断
        keepName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count != 0) {
                    textInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        keepType = textInputLayout_keepType.getEditText();
//        判断编辑框不为空
        keepType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count != 0) {
                    textInputLayout_keepType.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
//        如果为编辑对象，展示旧数据
        if (getIntent().getIntExtra("editPosition", -1) != -1) {
            keepName.setText(DataResourses.getEditableKeepData().getKeepName());
            keepType.setText(DataResourses.getEditableKeepData().getKeepType());
        }
        editOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                编辑框空判断
                if (keepName.getText().toString().equals("")) {
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError("名称不能为空！");
                } else if (keepType.getText().toString().equals("")) {
                    textInputLayout_keepType.setErrorEnabled(true);
                    textInputLayout_keepType.setError("类型不能为空！");
                } else {
//                    配置KeepData基本属性
                    if (!keepName.getText().toString().equals(keepData.getKeepName())) {
                        keepData.setKeepTime(0);
                    }
                    keepData.setKeepName(keepName.getText().toString());
                    keepData.setKeepType(keepType.getText().toString());
//                    计算keepData持续时长并写入
                    keepData.setKeepDuration(countDuration(keepData));
//                    DataResourses.getKeepDataList().add(keepData);
//                    Log.i("Adapter", DataResourses.getKeepDataList().toString());
//                    dataResourses.saveData();
                    setResult(1);
                    finish();
                }
            }
        });
    }

    /**
     * 计算KeepData总时长
     * @param keepData 待计算的KeepData
     * @return 时间，分钟
     */
    private double countDuration(KeepData keepData) {
        double tempDuration = 0;
        List<BeatData> beatDatas;
        for (int i = 0; i < 3; i++) {
            switch (i) {
                case 0:
                    beatDatas = keepData.getPrepareDataList();
                    break;
                case 1:
                    beatDatas = keepData.getKeepDataList();
                    break;
                case 2:
                    beatDatas = keepData.getStretchDataList();
                    break;
                default:
                    beatDatas = new ArrayList<BeatData>();
            }
            for (BeatData e :
                    beatDatas) {
                if (e.getBeatNumbers() != BeatData.INVALIDCODE) {
                    tempDuration += e.getBeatNumbers() * e.getBeatTime();
                } else {
                    tempDuration += e.getBeatSeconds();
                }
            }
        }
        return tempDuration / 60;
    }
}
