package com.example.testapp2.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.SoundPool;
import android.os.Handler;
import android.util.Log;
import android.widget.SimpleAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by Code on 2016/8/17 0017.
 * 确认对话框集合类
 * 需要重做
 */
public class DialogConfirm {

//    public static boolean DeleteConfirm(Context context, final List<Map<String, String>> mapList, final List<BeatData> beatDatas, final SimpleAdapter simpleAdapter, final int position) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle("注意！");
//        builder.setMessage("确认删除么？");
//        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        mapList.remove(position);
//                        beatDatas.remove(position);
//                        Log.i("Test", beatDatas.toString());
//                        simpleAdapter.notifyDataSetChanged();
//                    }
//                }
//
//        );
//        builder.show();
//        return true;
//    }

    /**
     * 开始锻炼确认对话框
     *
     * @param context      上下文参数
     * @param keepDataList KeepData表
     * @param activity     调用的activity
     * @param position     选择的锻炼在表中的位置
     * @return 返回真
     */
    public static boolean MainConfirm(Context context, final List<KeepData> keepDataList, final Activity activity, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("注意！");
        builder.setMessage("开始锻炼？");
        builder.setPositiveButton("好的", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DataResourses.setTempKeepData(keepDataList.get(position));
                Intent intent = new Intent(activity, KeepBeat.class);
                intent.putExtra("position", position);
                activity.startActivity(intent);
            }
        });
        builder.show();
        return true;
    }

    /**
     * 锻炼时暂停确认对话框
     *
     * @param soundPool  需要暂停的音频池
     * @param handler    需要暂停的handler
     * @param UPDATEDATA 常量参数——更新
     * @param TOPAUSE    常量参数——暂停
     * @param BREAKPAUSE 常量参数——打破暂停
     * @param context    上下文参数
     * @return 返回真
     */
    public static boolean KeepBeatPauseConfirm(final SoundPool soundPool, final Handler handler, Integer UPDATEDATA, final Integer TOPAUSE, final Integer BREAKPAUSE, Context context) {
//        暂停全部播放声音
        soundPool.autoPause();
        if (handler.hasMessages(UPDATEDATA)) {
//            移除多余的更新消息
            handler.removeMessages(UPDATEDATA);
        }
//        发送暂停消息
        handler.sendEmptyMessage(TOPAUSE);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("休息中……");
        builder.setTitle("注意！");
        builder.setPositiveButton("恢复锻炼", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (handler.hasMessages(TOPAUSE)) {
//                    移除多余的暂停消息
                    handler.removeMessages(TOPAUSE);
                }
                Log.i("Test", "Success");
//                发送结束暂停消息
                handler.sendEmptyMessage(BREAKPAUSE);
//                恢复声音播放
                soundPool.autoResume();
            }
        });
        builder.setCancelable(false);
        builder.show();
        return true;
    }

    /**
     * 取消锻炼确认对话框
     *
     * @param soundPool  需要暂停的音频池
     * @param activity   调用的activity
     * @param context    上下文参数
     * @param handler    需要暂停的handler
     * @param UPDATEDATA 常量参数——更新
     * @param TOPAUSE    常量参数——暂停
     * @param BREAKPAUSE 常量参数——打破暂停
     * @return 返回真
     */
    public static boolean KeepBeatCancelConfirm(final SoundPool soundPool, final Activity activity, Context context, final Handler handler, Integer UPDATEDATA, final Integer TOPAUSE, final Integer BREAKPAUSE) {
//        暂停所有播放声音
        soundPool.autoPause();
        if (handler.hasMessages(UPDATEDATA)) {
//            移除多余的更新消息
            handler.removeMessages(UPDATEDATA);
        }
//        发送暂停消息
        handler.sendEmptyMessage(TOPAUSE);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("注意！");
        builder.setMessage("确实要停止锻炼么？");
        builder.setPositiveButton("我确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                释放音频池数据
                soundPool.release();
//                结束活动
                activity.finish();
            }
        });
        builder.setNegativeButton("继续锻炼", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (handler.hasMessages(TOPAUSE)) {
//                    清除多余的暂停消息
                    handler.removeMessages(TOPAUSE);
                }
                Log.i("Test", "Success");
//                发送打破暂停消息
                handler.sendEmptyMessage(BREAKPAUSE);
//                恢复音乐播放
                soundPool.autoResume();
            }
        });
        builder.setCancelable(false);
        builder.show();
        return true;
    }

//    public static boolean CancelConfirm(Context context, final Activity activity) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle(R.string.newitemprepare_AlertDialog_Title);
//        if (MyActivity.editPosition == MyActivity.INVALIDCODE) {
//            builder.setMessage(R.string.newitem_cancelalertdialog_message);
//        } else {
//            builder.setMessage(R.string.newitem_cancelalertdialog_editmessage);
//        }
//        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                DataResourses.isDestroyEditableKeepData();
//                MyActivity.editPosition = MyActivity.INVALIDCODE;
//                Intent intent = new Intent(activity, MyActivity.class);
//                activity.startActivity(intent);
//                activity.finish();
//            }
//        });
//        builder.show();
//        return true;
//    }

//    public static boolean MainDeleteConfirm(Context context, final int position, final List<Map<String, String>> mapList, final MainAdapter mainAdapter, final DataResourses dataResourses) {
//        final List<KeepData> keepDataList = DataResourses.getKeepDataList();
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle(R.string.newitemprepare_AlertDialog_Title);
//        builder.setMessage(R.string.newitemprepare_AlertDialog_Message);
//        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        keepDataList.remove(position);
//                        mapList.remove(position);
//                        mainAdapter.notifyDataSetChanged();
//                        dataResourses.saveData();
//                    }
//                }
//
//        );
//        builder.show();
//        return true;
//    }
}
