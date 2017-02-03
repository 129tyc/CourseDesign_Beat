package com.example.testapp2.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Code on 2016/12/5 0005.
 * 锻炼历史页面
 */
public class HistoryPage extends AppCompatActivity {
    private ListView listView;
    private Toolbar toolbar;
    private HistoryAdapter historyAdapter;
    private List<KeepHistory> keepHistories;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.histroy_page);
//        加载锻炼历史数据
        if (DataResourses.isNoUser()) {
            keepHistories = new ArrayList<KeepHistory>();
        } else {
            keepHistories = DataResourses.getCurrentUser().getKeepHistoryList();
        }
//        实例化组件
        listView = (ListView) findViewById(R.id.history_page_ListView);
        toolbar = (Toolbar) findViewById(R.id.history_page_Toolbar);
        toolbar.setTitle("锻炼历史");
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (keepHistories.isEmpty()) {
//            若无锻炼数据，弹出SnackBar
            Snackbar.make(toolbar, "无历史锻炼数据！", Snackbar.LENGTH_SHORT).show();
        } else {
//            将数据加载至Adapter并显示
            historyAdapter = new HistoryAdapter(this, keepHistories);
            listView.setAdapter(historyAdapter);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
