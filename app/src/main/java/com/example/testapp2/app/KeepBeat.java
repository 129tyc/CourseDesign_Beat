package com.example.testapp2.app;

import android.app.*;
import android.content.Intent;
import android.graphics.Color;
import android.media.SoundPool;
import android.os.*;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Code on 2016/8/8 0008.
 * 锻炼页面
 */
public class KeepBeat extends Activity implements Beat_Display.BeatDisplay {
    private KeepData keepData;
    public static BeatData dispalyBeatData;
    public static String displayPartName;
    private TextView keepBeatPartName;
    private TextView keepBeatItemName;
    private TextView keepBeatDisplay;
    private TextView keepBeatDisplay2;
    private Button Cancel;
    private Button Pause;
    private int delayTime;
    private int tmp;
    private int sendTimes;
    private int tBeatData;
    private int tKeepData;
    private Audio_Vibration_Service audio_vibration_service;
    private SoundPool soundPool;
    private int soundBeatNormal;
    private int soundBeatFinal;
    private int soundBackgroundFinal;
    private int soundBeatFinish;
    private int singleBackSound;
    private BeatData tmpBeatData;
    private Fragment fragment;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    //handler发送的常量
    private final static int TOPAUSE = 1;
    private final static int UPDATEDATA = 0;
    private final static int BREAKPAUSE = 2;
    private final static int KEEPFINISH = 3;
    private final static int NEXTBEAT = 4;
    private final static int NEXTKEEP = 5;
    private final static int STARTKEEP = 6;
    private final static int STARTBEAT = 7;
    private final static int STARTDISPLAY = 8;
    public final static int FINISHDISPLAY = 9;
    //    计时切换handler
    private Handler handler;
    private List<List<BeatData>> beatDataLists;
    private List<BeatData> beatDatas;
    private final static String[] partArray = {"准备动作", "正式锻炼", "拉伸动作"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keepbeat);
//        获得即将锻炼的KeepData
        keepData = DataResourses.getTempKeepData();
//        实例化对象
        keepBeatPartName = (TextView) findViewById(R.id.keepbeatpartname);
        keepBeatItemName = (TextView) findViewById(R.id.keepbeatkeepitemname);
        keepBeatDisplay = (TextView) findViewById(R.id.keepbeatdisplay);
        keepBeatDisplay2 = (TextView) findViewById(R.id.keepbeatdisplay2);
        Cancel = (Button) findViewById(R.id.keepbeatcancel);
        Pause = (Button) findViewById(R.id.keepbeatpause);
//        配置Fragment
        fragmentManager = getFragmentManager();
        fragment = new Beat_Display();
        dispalyBeatData = null;
        displayPartName = null;
//        启动音频服务
        audio_vibration_service = new Audio_Vibration_Service(KeepBeat.this, 10);
//        实例化音频池
        soundPool = audio_vibration_service.getSoundPool();
//        获得播放用音频实例
        soundBeatNormal = soundPool.load(this, R.raw.beat_normal, 1);
        soundBeatFinal = soundPool.load(this, R.raw.beat_final, 1);
        soundBackgroundFinal = soundPool.load(this, R.raw.background_final, 1);
        soundBeatFinish = soundPool.load(this, R.raw.beat_finish, 1);
//        audio_vibration_service.addAudio("beatNormal", R.raw.beat_normal, 1);
//        audio_vibration_service.addAudio("beatFinal", R.raw.beat_final, 1);
//        audio_vibration_service.addAudio("displayDiDi", R.raw.display_didi, 1);
        Pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogConfirm.KeepBeatPauseConfirm(soundPool, handler, UPDATEDATA, TOPAUSE, BREAKPAUSE, KeepBeat.this);
            }
        });
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogConfirm.KeepBeatCancelConfirm(soundPool, KeepBeat.this, KeepBeat.this, handler, UPDATEDATA, TOPAUSE, BREAKPAUSE);
            }
        });
//        启动handler
        MyHandler myHandler = new MyHandler();
        handler = new Handler(myHandler);
//        处理将使用的BeatData
        beatDataLists = new ArrayList<List<BeatData>>();
        beatDataLists.add(keepData.getPrepareDataList());
        beatDataLists.add(keepData.getKeepDataList());
        beatDataLists.add(keepData.getStretchDataList());
//        发送消息，开始锻炼
        handler.sendEmptyMessage(STARTKEEP);
    }

    /**
     * beat信息展示
     *
     * @param isStart 是否开始展示
     */
    private void BeatDisplay(boolean isStart) {
        if (isStart) {
//            开始，获得将要显示的BeatData表
            beatDatas = beatDataLists.get(tKeepData);
            if (beatDatas.size() > 0) {
//                若BeatData表不为空。开始设置页面数据
                dispalyBeatData = beatDatas.get(tBeatData);
//                displayPartName = this.getResources().getString(partArray[tKeepData]);
                displayPartName = partArray[tKeepData];
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.keepbeat_BeatDispaly, fragment);
                fragmentTransaction.commit();
            } else {
//                表为空，发送消息进入下个锻炼
                dispalyBeatData = null;
                handler.sendEmptyMessage(NEXTKEEP);
            }
        } else {
//            结束，移除Fragment
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(fragment);
            if (!this.isFinishing()) {
                fragmentTransaction.commit();
            }
            UpdateKeepMain();
        }

    }

    @Override
    public void FinishDisplay() {
//        继承回调接口，发送结束展示消息
        handler.sendEmptyMessage(FINISHDISPLAY);
    }

    /**
     * 主画面更新
     */
    private void UpdateKeepMain() {
        if (beatDatas.size() > 0) {
//            获得临时BeatData用于展示
            tmpBeatData = beatDatas.get(tBeatData);
//            加载基本属性到组件
            keepBeatPartName.setText(partArray[tKeepData]);
            keepBeatItemName.setText(tmpBeatData.getBeatName());
//            声明富文本StringBuilder
            SpannableStringBuilder spannableStringBuilder;
            if (tmpBeatData.getBeatNumbers() != -1) {
//                展示的为次数和间隔
                sendTimes = tmpBeatData.getBeatNumbers();
                delayTime = (int) (tmpBeatData.getBeatTime() * 1000);
                spannableStringBuilder = new SpannableStringBuilder(String.valueOf(tmpBeatData.getBeatNumbers()));
                int start = spannableStringBuilder.length();
                spannableStringBuilder.append("次");
//                调整字体大小
                spannableStringBuilder.setSpan(new AbsoluteSizeSpan(100), start, start + 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            } else {
//                展示的为持续时间
                sendTimes = tmpBeatData.getBeatSeconds();
                delayTime = 1000;
                spannableStringBuilder = new SpannableStringBuilder(String.valueOf(tmpBeatData.getBeatSeconds()));
                int start = spannableStringBuilder.length();
                spannableStringBuilder.append("秒");
                spannableStringBuilder.setSpan(new AbsoluteSizeSpan(100), start, start + 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            }
//            展示计时数字
            keepBeatDisplay2.setText(spannableStringBuilder);
//            开始鼓点计时
            handler.sendEmptyMessage(STARTBEAT);
        } else {
//            鼓点表为空，进入下个部分
            handler.sendEmptyMessage(NEXTKEEP);
        }

    }

    @Override
    public void onBackPressed() {
    }

    /**
     * 画面更新处理handler
     */
    public class MyHandler implements Handler.Callback {
        @Override
        public boolean handleMessage(Message msg) {
            if (handler.hasMessages(UPDATEDATA)) {
//                移除多余更新消息
                handler.removeMessages(UPDATEDATA);
            }
            switch (msg.what) {
                case STARTKEEP:
//                    开始锻炼，初始化临时变量
                    tBeatData = 0;
                    tKeepData = 0;
                    singleBackSound = -1;
                    handler.sendEmptyMessage(STARTDISPLAY);
                    break;
                case STARTBEAT:
//                    开始鼓点计时，发送更新消息
                    tmp = 0;
                    handler.sendEmptyMessage(UPDATEDATA);
                    break;
                case STARTDISPLAY:
//                    展示鼓点信息,
                    BeatDisplay(true);
                    break;
                case FINISHDISPLAY:
//                    结束鼓点展示,进入后处理
                    singleBackSound = -1;
                    BeatDisplay(false);
                    break;
                case UPDATEDATA:
//                    根据数据延时更新消息
                    if (tmp <= sendTimes) {
//                        未超过发送次数，实例化富文本StringBuilder
                        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(String.valueOf(tmp));
                        int start = spannableStringBuilder.length();
                        if (tmpBeatData.getBeatNumbers() != -1) {
                            spannableStringBuilder.append("次");
                        } else {
                            spannableStringBuilder.append("秒");
                        }
                        spannableStringBuilder.setSpan(new AbsoluteSizeSpan(100), start, start + 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                        if (tmp >= sendTimes * 0.8) {
//                            当该鼓点即将结束时，播放提示音效
                            if (singleBackSound == -1) {
                                singleBackSound = soundPool.play(soundBackgroundFinal, (float) 0.3, (float) 0.3, 1, -1, 1);
                            }
//                            字体设为红色
                            spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.RED), 0, start, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                        }
                        if (tmp > 0) {
//                            播放鼓点音效
                            soundPool.play(soundBeatNormal, 1, 1, 1, 0, 1);
                        }
                        keepBeatDisplay.setText(spannableStringBuilder);
                        tmp++;
//                        延时发送更新消息
                        handler.sendEmptyMessageDelayed(UPDATEDATA, delayTime);
                    } else {
//                        发送次数达到，播放结束音效，停止播放提示音效
                        soundPool.play(soundBeatFinish, 1, 1, 1, 0, 1);
                        soundPool.stop(singleBackSound);
//                        发送进入下个鼓点消息
                        handler.sendEmptyMessage(NEXTBEAT);
                    }
                    break;
                case NEXTBEAT:
                    if (tBeatData < beatDatas.size() - 1) {
//                        未到达BeatData表结尾，继续
                        tBeatData++;
                        handler.sendEmptyMessage(STARTDISPLAY);
                    } else {
//                        到达结尾，跳转到下一部分
                        handler.sendEmptyMessage(NEXTKEEP);
                    }
                    break;
                case NEXTKEEP:
                    if (tKeepData < 2) {
//                        未到达最后部分，继续
                        tKeepData++;
                        tBeatData = 0;
                        handler.sendEmptyMessage(STARTDISPLAY);
                    } else {
//                        到达最后，发送锻炼结束消息
                        handler.sendEmptyMessage(KEEPFINISH);
                    }
                    break;
                case TOPAUSE:
                    break;
                case KEEPFINISH:
//                    锻炼结束，结束页面并跳转
                    Intent intent = new Intent(KeepBeat.this, KeepFinish.class);
                    intent.putExtra("position", getIntent().getIntExtra("position", 0));
                    startActivity(intent);
                    finish();
                    break;
                case BREAKPAUSE:
                    handler.sendEmptyMessageDelayed(UPDATEDATA, delayTime);
                    break;
            }
            return true;
        }
    }
}

