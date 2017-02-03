package com.example.testapp2.app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.List;

/**
 * Created by Code on 2016/8/29 0029.
 * 锻炼编辑页ListView适配器
 */
public class NewItemAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private List<BeatData> beatDatas;
    private boolean DeleteMode = false;
    private boolean SortMode = false;
    public boolean isHidden;

    /**
     * 设置删除模式
     *
     * @param deleteMode 是否开启删除模式
     */
    void setDeleteMode(boolean deleteMode) {
        DeleteMode = deleteMode;
        notifyDataSetChanged();
    }

    /**
     * 设置排序模式
     *
     * @param sortMode 是否开始排序模式
     */
    void setSortMode(boolean sortMode) {
        SortMode = sortMode;
        notifyDataSetChanged();
    }

    /**
     * 确认是否在排序模式
     *
     * @return 排序模式返回真，否则返回假
     */
    public boolean getSortMode() {
        return SortMode;
    }

    /**
     * 确认是否在删除模式
     *
     * @return 删除模式返回真，否则返回假
     */
    boolean getDeleteMode() {
        return DeleteMode;
    }

    /**
     * View缓存类
     */
    static class ViewHolder {
        TextView BeatName;
        TextView BeatNumbers;
        TextView BeatTime;
        TextView BeatSeconds;
        LinearLayout BeatNumbersLine;
        LinearLayout BeatSecondsLine;
        Button DeleteButton;
        ImageButton SortButton;
    }

    /**
     * 构造函数
     *
     * @param context   上下文参数
     * @param beatDatas 加载的BeatData表
     */
    NewItemAdapter(Context context, List<BeatData> beatDatas) {
        this.layoutInflater = LayoutInflater.from(context);
        this.beatDatas = beatDatas;
    }

    @Override
    public int getCount() {
        return beatDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return beatDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
//            若没有view，实例化缓存并存入view
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.newitemlistview_item, null);
            viewHolder.BeatName = (TextView) convertView.findViewById(R.id.newitem_item_BeatName);
            viewHolder.BeatNumbers = (TextView) convertView.findViewById(R.id.newitem_item_BeatNumbers);
            viewHolder.BeatTime = (TextView) convertView.findViewById(R.id.newitem_item_BeatTime);
            viewHolder.BeatSeconds = (TextView) convertView.findViewById(R.id.newitem_item_BeatSeconds);
            viewHolder.BeatNumbersLine = (LinearLayout) convertView.findViewById(R.id.newitem_item_BeatNumbersLine);
            viewHolder.BeatSecondsLine = (LinearLayout) convertView.findViewById(R.id.newitem_item_BeatSecondsLine);
            viewHolder.DeleteButton = (Button) convertView.findViewById(R.id.deleteButton);
            viewHolder.SortButton = (ImageButton) convertView.findViewById(R.id.sortButton);
            convertView.setTag(viewHolder);
        } else {
//            加载缓存
            viewHolder = (ViewHolder) convertView.getTag();
        }
//            根据数据展示不同页面
        if (beatDatas.get(position).getBeatNumbers() != -1) {
            viewHolder.BeatTime.setText(String.valueOf(beatDatas.get(position).getBeatTime()));
            viewHolder.BeatNumbers.setText(String.valueOf(beatDatas.get(position).getBeatNumbers()));
            viewHolder.BeatNumbersLine.setVisibility(View.VISIBLE);
            viewHolder.BeatSecondsLine.setVisibility(View.GONE);
        } else {
            viewHolder.BeatSeconds.setText(String.valueOf(beatDatas.get(position).getBeatSeconds()));
            viewHolder.BeatNumbersLine.setVisibility(View.GONE);
            viewHolder.BeatSecondsLine.setVisibility(View.VISIBLE);
        }
        viewHolder.BeatName.setText(beatDatas.get(position).getBeatName());
        if (DeleteMode) {
//            若启动删除模式，显示删除按钮并增加监听器
            viewHolder.DeleteButton.setVisibility(View.VISIBLE);
            viewHolder.DeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    beatDatas.remove(position);
                    notifyDataSetChanged();
                }
            });
        } else {
            viewHolder.DeleteButton.setVisibility(View.GONE);
        }
        if (SortMode) {
//            启动排序模式，显示排序按钮
            viewHolder.SortButton.setVisibility(View.VISIBLE);
        } else {
            viewHolder.SortButton.setVisibility(View.GONE);
        }
        return convertView;
    }

    /**
     * 数据位置交换
     *
     * @param start 开始位置
     * @param end   结束位置
     */
    void change(int start, int end) {
        BeatData beatData = beatDatas.get(start);
        beatDatas.remove(beatData);
        beatDatas.add(end, beatData);
        notifyDataSetChanged();
    }

}
