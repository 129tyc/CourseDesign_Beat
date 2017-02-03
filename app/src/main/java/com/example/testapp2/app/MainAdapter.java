package com.example.testapp2.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Code on 2016/8/23 0023.
 */
public class MainAdapter extends BaseAdapter {
    /**
     * 数据源KeepData表
     */
    private List<KeepData> keepDatas;
    /**
     * 布局填充器
     */
    //    private Context context;
    private LayoutInflater layoutInflater;

    /**
     * view缓存
     */
    static class ViewHolder {
        public TextView KeepName;
        public TextView KeepTime;
        public TextView KeepDuration;
        public TextView KeepType;
    }

    /**
     * 构造方法
     *
     * @param context   上下文参数
     * @param keepDatas 数据源
     */
    public MainAdapter(Context context, List<KeepData> keepDatas) {
//        this.context = context;
        this.keepDatas = keepDatas;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setKeepDatas(List<KeepData> keepDatas) {
        this.keepDatas = keepDatas;
    }

    @Override
    public int getCount() {
        return keepDatas.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
//            无view，实例化缓存并存入view
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.mainlistview_item, null);
            viewHolder.KeepName = (TextView) convertView.findViewById(R.id.mainItem_KeepName);
            viewHolder.KeepTime = (TextView) convertView.findViewById(R.id.mainItem_KeepTime);
            viewHolder.KeepDuration = (TextView) convertView.findViewById(R.id.mainItem_KeepDuration);
            viewHolder.KeepType = (TextView) convertView.findViewById(R.id.mainItem_KeepType);
            convertView.setTag(viewHolder);
        } else {
//            加载缓存
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        加载数据
        viewHolder.KeepName.setText(keepDatas.get(position).getKeepName());
        viewHolder.KeepType.setText(keepDatas.get(position).getKeepType());
        viewHolder.KeepTime.setText(String.valueOf(keepDatas.get(position).getKeepTime()));
        viewHolder.KeepDuration.setText(String.valueOf((int) keepDatas.get(position).getKeepDuration()));
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return keepDatas.get(position);
    }
}
