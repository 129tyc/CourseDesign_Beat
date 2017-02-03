package com.example.testapp2.app;

import android.app.AlertDialog;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.pm.InstrumentationInfo;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Code on 2016/10/4 0004.
 * 新建、编辑锻炼数据页面
 */
public class ItemEdit extends AppCompatActivity implements View.OnClickListener {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;
    private Button finish;
    /**
     * Fragment适配器，负责调用需要显示的Fragment
     */
    private MyFragmentPageAdapter myFragmentPageAdapter;
    /**
     * Fragment数组
     */
    private List<PageFragment> pageFragmentList;
    /**
     * Tab栏标题字符串数组
     */
    private final String[] TABSTRING = {"Prepare", "Keep", "Stretch"};
    public final static String CURRENTTAB = "currentTab";
    private boolean ISFINISH = false;
    private Menu menu;
    private boolean exitable = false;
    /**
     * 退出延时handler
     */
    private mHandler mHandler;
    private static final int REQUESTCODE = 0;
    private int editPosition;
//    public static KeepData keepData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_edit);
//        尝试获得编辑对象所在位置
        editPosition = getIntent().getIntExtra("editPosition", -1);
        if (editPosition != -1) {
//            存在编辑对象，将其设置到可编辑KeepData中
            DataResourses.setEditableKeepData(editPosition);
        }
//        实例化组件
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        toolbar = (Toolbar) findViewById(R.id.itemToolBar);
//        cancel = (Button) findViewById(R.id.cancel);
        finish = (Button) findViewById(R.id.finish);
//        实例化handler
        mHandler = new mHandler();
//        初始化需要显示的Fragment
        initFragment();
//        实例化FragmentAdapter
        myFragmentPageAdapter = new MyFragmentPageAdapter(getSupportFragmentManager(), this, pageFragmentList);
        viewPager.setAdapter(myFragmentPageAdapter);
//        tabLayout根据viewPager配置
        tabLayout.setupWithViewPager(viewPager);
        toolbar.setTitle("锻炼编辑");
//        根据Tab设置副标题
        toolbar.setSubtitle(TABSTRING[tabLayout.getSelectedTabPosition()]);
        setSupportActionBar(toolbar);
//        点选Tab跳转到响应页面
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                切换到最后一页，将“下一步”改成“完成”
                if (tab.getPosition() == 2) {
                    finish.setText("完成");
                    ISFINISH = true;
//                    切换到过最后一页，配置按钮为完成
                    finish.setOnClickListener(ItemEdit.this);
                } else {
//                    未切换到过最后一页，配置按钮向后切换页面
                    finish.setOnClickListener(ItemEdit.this);
                }
                toolbar.setSubtitle(TABSTRING[tab.getPosition()]);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
//        DataResourses.getEditableKeepData().setOnBeatDataChangedListenter(new KeepData.OnBeatDataChangedListenter() {
//            @Override
//            public void OnBeatDataChanged(List<BeatData> beatDatas) {
//                if (!beatDatas.isEmpty()){
//                    finish.setText("完成");
//                }
//            }
//        });
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
//                builder.setTitle("注意");
//                builder.setMessage("确定退出么？");
//                builder.show();
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        若可编辑KeepData不为空或已切换到最后一页，修改文字
        if (!DataResourses.getEditableKeepData().isEmpty() || tabLayout.getSelectedTabPosition() == 2) {
            finish.setText("完成");
            ISFINISH = true;
            finish.setOnClickListener(ItemEdit.this);
        } else {
            finish.setText("下一步");
            ISFINISH = false;
            finish.setOnClickListener(ItemEdit.this);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_edit, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        PageFragment pageFragment;
        switch (item.getItemId()) {
            case R.id.newitem:
//                新建鼓点
                Intent intent = new Intent(this, NewDialog.class);
                intent.putExtra(CURRENTTAB, tabLayout.getSelectedTabPosition());
                startActivity(intent);
                break;
            case R.id.deleteitem:
//                删除鼓点
                pageFragment = pageFragmentList.get(viewPager.getCurrentItem());
//                Log.i("Adapter", "Position----->" + String.valueOf(viewPager.getCurrentItem()));
                pageFragment.setAdapterDeleteMode(true);
//                Log.i("Adapter", "Fragment----->" + pageFragment.getAdapterDeleteMode());
//                切换菜单
                menu.close();
                menu.clear();
                getMenuInflater().inflate(R.menu.item_edit_finish, menu);
                break;
            case R.id.sortitem:
//                对鼓点进行排序
                pageFragment = pageFragmentList.get(viewPager.getCurrentItem());
                pageFragment.setAdapterSortMode(true);
//                切换菜单
                menu.close();
                menu.clear();
                getMenuInflater().inflate(R.menu.item_edit_finish, menu);
                break;
            case R.id.editFinish:
//                删除或排序结束
                pageFragment = pageFragmentList.get(viewPager.getCurrentItem());
                pageFragment.setAdapterDeleteMode(false);
                pageFragment.setAdapterSortMode(false);
//                切换回原菜单
                menu.close();
                menu.clear();
                getMenuInflater().inflate(R.menu.item_edit, menu);
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (ISFINISH) {
//            如果可以结束，继续
            if (!DataResourses.getEditableKeepData().isEmpty()) {
//                可编辑KeepData不为空，启动完成编辑页面
                Intent intent = new Intent(this, ItemEdit_Finish.class);
                intent.putExtra("editPosition", editPosition);
                startActivityForResult(intent, REQUESTCODE);
//                startActivity(intent);
            } else {
//                提示未添加鼓点
                final Snackbar snackbar = Snackbar.make(finish, "请至少添加一个鼓点！", Snackbar.LENGTH_SHORT);
//                snackbar.setAction("好的", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        snackbar.dismiss();
//                    }
//                });
                snackbar.show();
            }

        } else {
            viewPager.setCurrentItem(tabLayout.getSelectedTabPosition() + 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            if (requestCode == REQUESTCODE) {
//                确认返回
                if (editPosition == -1) {
//                    若不是编辑而是新建
                    DataResourses.getEditableKeepData().setKeepId("0");
                    DataResourses.getKeepDataList().add(DataResourses.getEditableKeepData());
                } else {
//                    指定位置配置KeepData
                    DataResourses.getKeepDataList().set(editPosition, DataResourses.getEditableKeepData());
                }
//                DataResourses dataResourses = new DataResourses(this, this);
                DataResourses.clearEditableKeepData();
                Snackbar snackbar = Snackbar.make(viewPager, "保存成功！", Snackbar.LENGTH_SHORT);
                snackbar.show();
//                while (snackbar.isShown()) {
//                }
                finish();
//                DataResourses.clearEditableKeepData();
////                Intent intent = new Intent(ItemEdit.this, MainActivity.class);
////                startActivity(intent);
//                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (!exitable) {
//            如果第一次按退出，提示再次点击
            Snackbar snackbar = Snackbar.make(finish, "再次点击返回键退出", Snackbar.LENGTH_SHORT);
            snackbar.show();
            exitable = true;
//            延时1.5秒将exitable恢复
            mHandler.sendEmptyMessageDelayed(0, 1500);
        } else {
//            第二次按退出，结束
            DataResourses.clearEditableKeepData();
            finish();
        }
    }

    private void initFragment() {
        pageFragmentList = new ArrayList<PageFragment>();
        for (int i = 0; i < 3; i++) {
//            初始化fragment并将其添加进数组
            PageFragment fragment = PageFragment.newInstance(i);
            pageFragmentList.add(fragment);
        }
    }

    private class mHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
//                收到消息，将exitable恢复
                exitable = false;
            }
        }
    }
}
