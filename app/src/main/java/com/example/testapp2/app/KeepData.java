package com.example.testapp2.app;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Code on 2016/8/10 0010.
 * 锻炼动作数据
 */
public class KeepData {
    /**
     * 锻炼数据ID，服务器赋予
     */
    private String KeepId;
    /**
     * 锻炼练习名称
     */
    private String KeepName;
    /**
     * 运动锻炼类型
     */
    private String KeepType;
    /**
     * 锻炼次数
     */
    private int KeepTime;
    /**
     * 锻炼动作时长
     */
    private double KeepDuration;
    /**
     * 准备动作鼓点表
     */
    private List<BeatData> PrepareDataList;
    /**
     * 锻炼动作鼓点表
     */
    private List<BeatData> KeepDataList;
    /**
     * 拉伸动作锻炼表
     */
    private List<BeatData> StretchDataList;
//    private OnBeatDataChangedListenter onBeatDataChangedListenter;

//    public interface OnBeatDataChangedListenter {
//        void OnBeatDataChanged(List<BeatData> beatDatas);
//    }
//
//    public void setOnBeatDataChangedListenter(OnBeatDataChangedListenter listenter) {
////        if (isEmpty()){
////            onBeatDataChangedListenter.OnBeatDataChanged();
////        }
//        onBeatDataChangedListenter = listenter;
//    }

    public String getKeepId() {
        return KeepId;
    }

    public void setKeepId(String keepId) {
        KeepId = keepId;
    }

    @Override
    public String toString() {
        return "KeepData{" +
                "KeepId='" + KeepId + '\'' +
                ", KeepName='" + KeepName + '\'' +
                ", KeepType='" + KeepType + '\'' +
                ", KeepTime=" + KeepTime +
                ", KeepDuration=" + KeepDuration +
                ", PrepareDataList=" + PrepareDataList +
                ", KeepDataList=" + KeepDataList +
                ", StretchDataList=" + StretchDataList +
                '}';
    }

    public boolean isEmpty() {
        if (PrepareDataList.isEmpty() && KeepDataList.isEmpty() && StretchDataList.isEmpty()) {
            return true;
        }
        return false;
    }

    public int getKeepTime() {
        return KeepTime;
    }

    public void setKeepTime(int keepTime) {
        KeepTime = keepTime;
    }

    public double getKeepDuration() {
        return KeepDuration;
    }

    public void setKeepDuration(double keepDuration) {
        KeepDuration = keepDuration;
    }

    public KeepData() {
        setKeepId("0");
        PrepareDataList = new ArrayList<BeatData>();
        KeepDataList = new ArrayList<BeatData>();
        StretchDataList = new ArrayList<BeatData>();
    }

    public String getKeepName() {
        return KeepName;
    }

    public void setKeepName(String keepName) {
        KeepName = keepName;
    }

    public String getKeepType() {
        return KeepType;
    }

    public void setKeepType(String keepType) {
        KeepType = keepType;
    }

    public List<BeatData> getPrepareDataList() {
//        onBeatDataChangedListenter.OnBeatDataChanged(PrepareDataList);
        return PrepareDataList;
    }

    public void setPrepareDataList(List<BeatData> prepareDataList) {
        PrepareDataList.clear();
        PrepareDataList.addAll(prepareDataList);
//        onBeatDataChangedListenter.OnBeatDataChanged(PrepareDataList);
    }

    public List<BeatData> getKeepDataList() {
//        onBeatDataChangedListenter.OnBeatDataChanged(KeepDataList);
        return KeepDataList;
    }

    public void setKeepDataList(List<BeatData> keepDataList) {
        KeepDataList.clear();
        KeepDataList.addAll(keepDataList);
//        onBeatDataChangedListenter.OnBeatDataChanged(KeepDataList);
    }

    public List<BeatData> getStretchDataList() {
//        onBeatDataChangedListenter.OnBeatDataChanged(StretchDataList);
        return StretchDataList;
    }

    public void setStretchDataList(List<BeatData> stretchDataList) {
        StretchDataList.clear();
        StretchDataList.addAll(stretchDataList);
//        onBeatDataChangedListenter.OnBeatDataChanged(StretchDataList);
    }

}
