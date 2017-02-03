package com.example.testapp2.app;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Code on 2016/11/24 0024.
 * 主页面DrawerListView数据适配器
 */
public class MainDrawerListViewAdapter extends BaseAdapter {
    private List<String> captionArray;
    private List<Drawable> captionImageResArray;
    private Context context;
    private LayoutInflater layoutInflater;

    /**
     * view缓存类
     */
    static class ViewHolder {
        public ImageView captionImage;
        public TextView caption;
    }

    /**
     * 构造函数
     *
     * @param context              上下文参数
     * @param captionArray         标题资源
     * @param captionImageResArray 图标资源
     */
    public MainDrawerListViewAdapter(Context context, List<String> captionArray, List<Drawable> captionImageResArray) {
        this.captionArray = captionArray;
        this.captionImageResArray = captionImageResArray;
        this.context = context;
        initLayoutInflater();
    }

    /**
     * 构造函数
     *
     * @param context      上下文参数
     * @param captionArray 标题资源
     */
    public MainDrawerListViewAdapter(Context context, List<String> captionArray) {
        this.captionArray = captionArray;
        this.captionImageResArray = null;
        this.context = context;
        initLayoutInflater();
    }

    /**
     * 初始化Layout填充
     */
    private void initLayoutInflater() {
        if (layoutInflater == null) {
            this.layoutInflater = LayoutInflater.from(context);
        }
    }

    @Override
    public int getCount() {
        return captionArray.size();
    }

    @Override
    public Object getItem(int position) {
        if (position < captionImageResArray.size()) {
            return null;
        } else {
            return new HashMap<String, Drawable>().put(captionArray.get(position), captionImageResArray.get(position));
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
//            无缓存，实例view并缓存
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.drawerlistview_item, null);
            viewHolder.caption = (TextView) convertView.findViewById(R.id.main_DrawerListView_item_TextView);
            viewHolder.captionImage = (ImageView) convertView.findViewById(R.id.main_DrawerListView_item_ImageView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        Log.i("Test" , captionArray.get(position));
        viewHolder.caption.setText(captionArray.get(position));
        if (!(captionImageResArray == null ||
                captionImageResArray.size() == 0) &&
                position < captionImageResArray.size()) {
//            当标题资源未完，但图标资源也未完，
            viewHolder.captionImage.setImageDrawable(captionImageResArray.get(position));
        } else {
//            图标数组已到结尾
            viewHolder.captionImage.setVisibility(View.GONE);
        }

        return convertView;
    }
}
