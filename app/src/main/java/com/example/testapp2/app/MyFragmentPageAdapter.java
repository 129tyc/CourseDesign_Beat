package com.example.testapp2.app;


import android.content.Context;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Code on 2016/10/3 0003.
 * Fragment适配器
 */
public class MyFragmentPageAdapter extends FragmentPagerAdapter {
    /**
     * Fragment数量
     */
    private final int COUNT = 3;
    /**
     * Fragment标题
     */
    private String[] titles = {"热身", "主要", "拉伸"};
    /**
     * pageFragment表
     */
    private List<PageFragment> pageFragmentList;

    /**
     * 构造函数
     *
     * @param fragmentManager Fragment管理器
     * @param context         上下文参数
     * @param pageFragments   加载的Fragment表
     */
    public MyFragmentPageAdapter(FragmentManager fragmentManager, Context context, List<PageFragment> pageFragments) {
        super(fragmentManager);
        this.pageFragmentList = pageFragments;
    }

    @Override
    public Fragment getItem(int position) {
        return pageFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
