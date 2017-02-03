package com.example.testapp2.app;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Code on 2016/10/3 0003.
 * Fragment页类
 */
public class PageFragment extends Fragment {

    public static String ARGS_PAGE = "args_page";
    private int mPage;
    private View view;
    private DragListView listView;
    //    private static final int PREPARE = 1;
//    private static final int KEEP = 2;
//    private static final int STRETCH = 3;
    private NewItemAdapter newItemAdapter;
    private List<BeatData> beatDatas;

    /**
     * 设置适配器删除模式
     *
     * @param deleteMode 是否开启删除模式
     */
    void setAdapterDeleteMode(boolean deleteMode) {
        newItemAdapter.setDeleteMode(deleteMode);
    }

    /**
     * 设置适配器排序模式
     *
     * @param sortMode 是否开启排序模式
     */
    void setAdapterSortMode(boolean sortMode) {
        newItemAdapter.setSortMode(sortMode);
    }

    /**
     * 获得删除模式状态
     *
     * @return 启动返回真，否则为假
     */
    public boolean getAdapterDeleteMode() {
        return newItemAdapter.getDeleteMode();
    }

    /**
     * 返回PageFragment实例
     *
     * @param page 页数
     * @return 对应的PageFragment
     */
    static PageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARGS_PAGE, page);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARGS_PAGE);
        switch (mPage) {
//            根据不同页面设置不同的BeatData表
            case 0:
                beatDatas = DataResourses.getEditableKeepData().getPrepareDataList();
                break;
            case 1:
                beatDatas = DataResourses.getEditableKeepData().getKeepDataList();
                break;
            case 2:
                beatDatas = DataResourses.getEditableKeepData().getStretchDataList();
                break;
        }
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_item, container, false);
        listView = (DragListView) view.findViewById(R.id.item_listView);
        newItemAdapter = new NewItemAdapter(getContext(), beatDatas);
//        设置排序按钮到可拖拽ListView
        listView.setDragViewId(R.id.sortButton);
        listView.setAdapter(newItemAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                点击打开编辑页
                Intent intent = new Intent(getContext(), NewDialog.class);
                intent.putExtra("EditPage", mPage);
                intent.putExtra("Position", position);
//                NewDialog.editBeatData = beatDatas.get(position);
//                Log.i("Adapter", NewDialog.editBeatData.toString());
                startActivity(intent);
            }
        });
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (newItemAdapter != null) {
            newItemAdapter.notifyDataSetChanged();
            Log.i("Adapter", "--->" + beatDatas.toString());
            Log.i("Adapter", DataResourses.getEditableKeepData().toString());
        }
    }

}
