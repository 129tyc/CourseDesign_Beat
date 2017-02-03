package com.example.testapp2.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;
import java.util.zip.DataFormatException;

/**
 * Created by Code on 2016/8/20 0020.
 * 锻炼结束展示页
 */
public class KeepFinish extends Activity {
    private Button again;
    private Button done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keepfinish);
//        实例化组件
        again = (Button) findViewById(R.id.keepfinishagain);
        done = (Button) findViewById(R.id.keepfinishdone);
//        DataResourses dataResourses = new DataResourses(this, this);
//        获得当前锻炼的KeepData并将锻炼次数＋1
        KeepData keepData = DataResourses.getKeepDataList().get(getIntent().getIntExtra("position", 0));
        keepData.setKeepTime(keepData.getKeepTime() + 1);
        if (DataResourses.isNoUser()) {
//            无用户，不保存历史
            Snackbar.make(done, "未登陆，无法保存锻炼历史！", Snackbar.LENGTH_SHORT).show();
        } else {
//            实例化新锻炼历史
            KeepHistory keepHistory = new KeepHistory();
            keepHistory.setID(0);
            keepHistory.setKeepDataId(Integer.parseInt(keepData.getKeepId()));
            keepHistory.setKeepDate(Calendar.getInstance().getTime());
            keepHistory.setKeepName(keepData.getKeepName());
            keepHistory.setKeepType(keepData.getKeepType());
            keepHistory.setUserId(DataResourses.getCurrentUser().getUserId());
            DataResourses.getCurrentUser().getKeepHistoryList().add(0, keepHistory);
        }
//        dataResourses.saveData();
        again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                重新开始
                Intent intent = new Intent(KeepFinish.this, KeepBeat.class);
                intent.putExtra("position", getIntent().getIntExtra("position", 0));
                startActivity(intent);
                finish();
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                清除临时变量，结束页面
                DataResourses.clearTempKeepData();
                Intent intent = new Intent(KeepFinish.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
