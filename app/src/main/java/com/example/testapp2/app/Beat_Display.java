package com.example.testapp2.app;

import android.app.Fragment;
import android.content.Context;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Code on 2016/8/19 0019.
 * 锻炼页面展示鼓点信息
 */
public class Beat_Display extends Fragment {
    private TextView partName;
    private TextView itemName;
    private TextView itemSecItemNum;
    private TextView itemTime;
    private TextView displayTime;
    private Handler handler;
    private DisplayHandler displayHandler;
    private SoundPool soundPool;
    private int soundDisplayDiDi;
    private static final int UPDATE = 0;
    private static final int START = 1;
    private static final int FINISH = 2;
    private int displayNumbers;
    private BeatData beatData;
    private BeatDisplay beatDisplay;

    /**
     * 显示接口
     * 结束锻炼回调
     */
    public interface BeatDisplay {
        void FinishDisplay();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        填充布局
        View view = inflater.inflate(R.layout.beat_display, container, false);
//       实例化部件
        partName = (TextView) view.findViewById(R.id.Beat_Display_PartName);
        itemName = (TextView) view.findViewById(R.id.Beat_Display_ItemName);
        itemSecItemNum = (TextView) view.findViewById(R.id.Beat_Display_ItemSec_ItemNum);
        itemTime = (TextView) view.findViewById(R.id.Beat_Display_ItemTime);
        displayTime = (TextView) view.findViewById(R.id.Beat_Display_Time);
//        实例化倒计时handler
        displayHandler = new DisplayHandler();
        handler = new Handler(displayHandler);
//        实例化音频池
        SoundPool.Builder builder = new SoundPool.Builder();
//        设置最大音频流数量
        builder.setMaxStreams(10);
        soundPool = builder.build();
//        加载倒计时音频
        soundDisplayDiDi = soundPool.load(getActivity(), R.raw.display_didi, 1);
//        获得需要展示的Beat并加载至组件
        beatData = KeepBeat.dispalyBeatData;
        String displayString = KeepBeat.displayPartName;
        partName.setText(displayString);
        itemName.setText(beatData.getBeatName());
//        处理显示的频次信息
        StringBuffer stringBuffer;
        if (beatData.getBeatNumbers() != -1) {
//            当鼓点为持续时间时
            stringBuffer = new StringBuffer(String.valueOf(beatData.getBeatNumbers()));
            stringBuffer.append("次");
            itemSecItemNum.setText(stringBuffer);
            stringBuffer = new StringBuffer(String.valueOf(beatData.getBeatTime()));
            stringBuffer.append("秒/次");
            itemTime.setText(stringBuffer);
        } else {
//            当鼓点为次数时
            stringBuffer = new StringBuffer(String.valueOf(beatData.getBeatSeconds()));
            stringBuffer.append("秒");
            itemSecItemNum.setText(stringBuffer);
            itemTime.setText("");
        }
//        handler发送消息开始倒计时
        handler.sendEmptyMessage(START);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        获取接口对象
        try {
            beatDisplay = (BeatDisplay) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    /**
     * 鼓点展示时间倒计时handler
     */
    public class DisplayHandler implements Handler.Callback {
        @Override
        /**
         * 处理倒计时消息
         * @param msg
         */
        public boolean handleMessage(Message msg) {
            if (handler.hasMessages(UPDATE)) {
//                去除handler队列中多余的更新消息
                handler.removeMessages(UPDATE);
            }
            switch (msg.what) {
                case START:
//                    收到开始消息，设置倒计时次数，发送更新页面消息
                    displayNumbers = 5;
                    handler.sendEmptyMessage(UPDATE);
                    break;
                case UPDATE:
//                    收到更新消息，播放倒计时提示音一次，设置倒计时数字
                    soundPool.play(soundDisplayDiDi, 1, 1, 1, 0, 1);
                    if (displayNumbers > 0) {
//                        未结束，延迟1秒发送更新消息
                        displayTime.setText(String.valueOf(displayNumbers));
                        displayNumbers--;
                        handler.sendEmptyMessageDelayed(UPDATE, 1000);
                    } else {
//                        发送结束消息
                        handler.sendEmptyMessage(FINISH);
                    }
                    break;
                case FINISH:
//                    清理数据，调用接口函数
                    KeepBeat.dispalyBeatData = null;
                    KeepBeat.displayPartName = null;
                    beatDisplay.FinishDisplay();
                    break;
            }
            return true;
        }
    }

}
