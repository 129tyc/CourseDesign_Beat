package com.example.testapp2.app;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

import static android.os.Process.killProcess;
import static android.os.Process.myPid;

/**
 * @author Code
 */
public class MainActivity extends AppCompatActivity {
    private MainAdapter mainAdapter;
    private ListView listView;
    private List<KeepData> keepDatas;
    private DataResourses dataResourses;
    private FloatingActionButton floatingActionButton;
    private DrawerLayout drawerLayout;
    private TextView userName;
    private CircleImageView avatar;
    private Button exit;
    private ListView drawerListView;
    private List<String> captionArray = new ArrayList<String>();
    private List<Drawable> drawableArray = new ArrayList<Drawable>();
    private LinearLayout drawerView;
    private MainDrawerListViewAdapter mainDrawerListViewAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
//        Intent intent = new Intent(this, ItemEdit.class);
//        startActivity(intent);
        dataResourses = new DataResourses(this, this);
        dataResourses.initUser();
//        dataResourses.readData();
//        dataResourses.initUser();
//        if (DataResourses.getCurrentUser().isEmpty()){
//            Intent intent = new Intent(MainActivity.this, LoginPage.class);
//            startActivity(intent);
//        }
//        加载列表标题和图标资源
        if (captionArray.isEmpty()) {
            captionArray.add("立即同步");
            captionArray.add("历史数据");
            captionArray.add("个人中心");
        }
        if (drawableArray.isEmpty()) {
            drawableArray.add(getResources().getDrawable(R.mipmap.exercise, getTheme()));
            drawableArray.add(getResources().getDrawable(R.mipmap.history, getTheme()));
            drawableArray.add(getResources().getDrawable(R.mipmap.user, getTheme()));
        }
//        获得需要展示的列表数据
        keepDatas = DataResourses.getKeepDataList();
//        实例化组件
        final Toolbar toolbar = (Toolbar) findViewById(R.id.mainToolBar);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.mainCreateButton);
        drawerView = (LinearLayout) findViewById(R.id.drawerView);
        listView = (ListView) findViewById(R.id.mainListView);
        mainAdapter = new MainAdapter(this, keepDatas);
        mainDrawerListViewAdapter = new MainDrawerListViewAdapter(this, captionArray, drawableArray);
        drawerLayout = (DrawerLayout) findViewById(R.id.mainDrawer);
        drawerListView = (ListView) findViewById(R.id.main_DrawerListView);
        drawerListView.setAdapter(mainDrawerListViewAdapter);
        avatar = (CircleImageView) findViewById(R.id.mainAvatar);
        userName = (TextView) findViewById(R.id.userName);
        exit = (Button) findViewById(R.id.mainExit);
        toolbar.setTitle("鼓点");
//        toolbar.setSubtitle("鼓·点");
//        toolbar.setNavigationIcon(R.drawable.create);
        toolbar.setNavigationIcon(R.mipmap.menu);
        setSupportActionBar(toolbar);
//        设置导航图标监听器，控制Drawer
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(drawerView)) {
                    drawerLayout.closeDrawers();
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                killProcess(myPid());
            }
        });
//        listView.addHeaderView(View.inflate(this,R.drawable.main_drawerlistview_item,drawerListView));
//        drawerListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
//        drawerListView.setSelection(0);
//        设置DrawerListViw点击监听器，设置跳转
        drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                switch (position) {
                    case 0:
                        dataResourses.saveData(listView, new HttpUtil.DataProcess() {
                            @Override
                            public void processComplete(Object object) {
                                Snackbar.make(listView, "同步成功！", Snackbar.LENGTH_LONG).show();
                            }
                        });
                        break;
                    case 1:
                        intent = new Intent(MainActivity.this, HistoryPage.class);
                        break;
                    case 2:
                        intent = new Intent(MainActivity.this, PersonalCentre.class);
                        break;
                }
                if (intent != null) {
                    drawerLayout.closeDrawers();
                    startActivity(intent);
                }
            }
        });
//        MainDrawerListViewAdapter mainDrawerListViewAdapter = new MainDrawerListViewAdapter(this, );
//        Drawer开合调整ToolBar标题
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                toolbar.setTitle("选项菜单");
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                toolbar.setTitle("鼓点");
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        Log.i("Adapter", "--Main-->" + keepDatas.toString());
//        listView.setAdapter(mainAdapter);
//        新建锻炼
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ItemEdit.class);
                startActivity(intent);
            }
        });
//        点击启动锻炼
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DialogConfirm.MainConfirm(view.getContext(), keepDatas, MainActivity.this, position);
            }
        });
//        长按弹出菜单
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
                popupMenu.inflate(R.menu.main_listview);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.editItem:
//                                编辑锻炼
                                Intent intent = new Intent(MainActivity.this, ItemEdit.class);
                                intent.putExtra("editPosition", position);
                                startActivity(intent);
                                break;
                            case R.id.deleteItem_main:
//                                删除锻炼
                                final Snackbar snackbar = Snackbar.make(view, "确实要删除么？", Snackbar.LENGTH_INDEFINITE);
                                snackbar.setAction("我确定", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        DataResourses.getKeepDataList().remove(position);
                                        mainAdapter.notifyDataSetChanged();
//                                        dataResourses.saveData();
//                                        snackbar.dismiss();
                                    }
                                });
                                snackbar.show();
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();
                return true;
            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.create:
//                Intent intent = new Intent(MainActivity.this, ItemEdit.class);
//                startActivity(intent);
////                finish();
//                break;
//        }
//        return true;
//    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Test", String.valueOf(DataResourses.isNoUser()));
        Log.i("Test", DataResourses.getCurrentUser().getSyncedKeepDatas().toString());
        if (!DataResourses.isNoUser() && !DataResourses.getCurrentUser().isEmpty()) {
            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, PersonalCentre.class);
                    startActivity(intent);
                    drawerLayout.closeDrawers();
                }
            };
            userName.setOnClickListener(onClickListener);
            avatar.setOnClickListener(onClickListener);
//            存在用户,查询数据
            if (DataResourses.needQueryData()) {
//                判断需要查询锻炼数据
                dataResourses.queryData(floatingActionButton, new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        if (msg.what == 1) {
                            DataResourses.setKeepDataList(DataResourses.getCurrentUser().getSyncedKeepDatas());
                            Log.i("test", "Success!--->" + keepDatas);
                            mainAdapter.notifyDataSetChanged();
                            listView.setAdapter(mainAdapter);
                            return true;
                        }
                        return false;
                    }
                }));
            }
            avatar.setImageDrawable(getDrawable(R.drawable.ic_menu_blocked_user));
//            Log.i("Test", DataResourses.getCurrentUser().toString());
            if (DataResourses.getCurrentUser().getUserNickName().equals("")) {
//                无昵称,显示用户名
                userName.setText(DataResourses.getCurrentUser().getUserName());
            } else {
                userName.setText(DataResourses.getCurrentUser().getUserNickName());
            }
            if (DataResourses.getCurrentUser().getUserAvatar() == null) {
//                无头像,尝试获取头像
                DataResourses.getAvatar(new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        if (msg.what == 10) {
                            avatar.setImageBitmap(DataResourses.getCurrentUser().getUserAvatar());
                        }
                        return false;
                    }
                }));
            } else {
                avatar.setImageBitmap(DataResourses.getCurrentUser().getUserAvatar());
            }
        } else {
//            无用户,提示登陆,设置监听器点击跳转到登陆页
            userName.setText("请先登陆");
            avatar.setImageDrawable(null);
            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, LoginPage.class);
                    startActivity(intent);
                    drawerLayout.closeDrawers();
                }
            };
            userName.setOnClickListener(onClickListener);
            avatar.setOnClickListener(onClickListener);
        }
        mainAdapter.notifyDataSetChanged();
        listView.setAdapter(mainAdapter);
    }
}
