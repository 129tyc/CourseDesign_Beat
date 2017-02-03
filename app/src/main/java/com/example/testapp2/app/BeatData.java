package com.example.testapp2.app;

/**
 * Created by Code on 2016/8/10 0010.
 * <p>
 * 鼓点数据
 */
public class BeatData {
    /**
     * 鼓点名称
     */
    private String BeatName;

    /**
     * 鼓点数量
     */
    private int BeatNumbers;
    /**
     * 鼓点持续秒数
     */
    private int BeatSeconds;
    /**
     * 鼓点间隔时间
     */
    private double BeatTime;
    /**
     * 无效码，判断为频次还是持续时间
     */
    public static final int INVALIDCODE = -1;

    @Override
    public String toString() {
        return "BeatData{" +
                "BeatName='" + BeatName + '\'' +
                ", BeatNumbers=" + BeatNumbers +
                ", BeatSeconds=" + BeatSeconds +
                ", BeatTime=" + BeatTime +
                '}';
    }

    public double getBeatTime() {
        return BeatTime;
    }

    public String getBeatName() {
        return BeatName;
    }

    public int getBeatNumbers() {
        return BeatNumbers;
    }

    public int getBeatSeconds() {
        return BeatSeconds;
    }

    public BeatData() {

    }

    /**
     * 构造函数（时间型，鼓点之间固定1s间隔）
     *
     * @param BeatName    鼓点名称
     * @param BeatSeconds 鼓点持续时间
     */
    public BeatData(String BeatName, int BeatSeconds) {
        this.BeatName = BeatName;
        this.BeatSeconds = BeatSeconds;
        BeatTime = INVALIDCODE;
        BeatNumbers = INVALIDCODE;
    }

    /**
     * 构造函数（鼓点型，设定鼓点间隔时间）
     *
     * @param BeatName    鼓点名称
     * @param BeatNumbers 鼓点次数
     * @param BeatTime    鼓点间隔时间
     */
    public BeatData(String BeatName, int BeatNumbers, double BeatTime) {
        this.BeatName = BeatName;
        this.BeatTime = BeatTime;
        this.BeatNumbers = BeatNumbers;
    }
}