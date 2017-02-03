package com.example.testapp2.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Code on 2016/12/7 0007.
 * 锻炼历史页面ListView的Adapter
 */
public class HistoryAdapter extends BaseAdapter {
    private List<KeepHistory> keepHistories;
    //    private Context context;
    private LayoutInflater layoutInflater;

    /**
     * ViewHolder类，加快Adapter加载速度
     */
    static class ViewHolder {
        public TextView KeepName;
        public TextView KeepTime;
        public TextView KeepDate;
        public TextView KeepType;
    }

    /**
     * 构造函数
     *
     * @param context       上下文参数
     * @param keepHistories KeepHistory表
     */
    public HistoryAdapter(Context context, List<KeepHistory> keepHistories) {
//        this.context = context;
        this.keepHistories = keepHistories;
        this.layoutInflater = LayoutInflater.from(context);
    }

    /**
     * 设置KeepHistory表为数据源
     *
     * @param keepHistories 待导入的KeepHistory表
     */
    public void setKeepHistories(List<KeepHistory> keepHistories) {
        this.keepHistories = keepHistories;
    }

    @Override
    public int getCount() {
        return keepHistories.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
//            若convertView不存在，实例化并将其赋予viewHolder
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.historylistview_item, null);
            viewHolder.KeepName = (TextView) convertView.findViewById(R.id.historyItem_KeepName);
            viewHolder.KeepTime = (TextView) convertView.findViewById(R.id.historyItem_KeepTime);
            viewHolder.KeepDate = (TextView) convertView.findViewById(R.id.historyItem_KeepDate);
            viewHolder.KeepType = (TextView) convertView.findViewById(R.id.historyItem_KeepType);
            convertView.setTag(viewHolder);
        } else {
//            直接获取viewHolder
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        加载数据
        viewHolder.KeepName.setText(keepHistories.get(position).getKeepName());
        viewHolder.KeepType.setText(keepHistories.get(position).getKeepType());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(keepHistories.get(position).getKeepDate());
        String date = calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) - 1) + "月" + calendar.get(Calendar.DATE) + "日";
        String time = calendar.get(Calendar.HOUR) + "点" + calendar.get(Calendar.MINUTE) + "分";
        viewHolder.KeepDate.setText(date);
        viewHolder.KeepTime.setText(time);
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return keepHistories.get(position);
    }
}
