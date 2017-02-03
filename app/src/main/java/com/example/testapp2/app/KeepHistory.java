package com.example.testapp2.app;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Code on 2016/11/17 0017.
 * 锻炼历史基础类
 */
public class KeepHistory {
    /**
     * 锻炼历史ID，服务器赋予
     */
    private int ID;
    /**
     * 锻炼用户ID
     */
    private int UserId;
    /**
     * 锻炼数据ID
     */
    private int KeepDataId;
    /**
     * 锻炼运动名
     */
    private String KeepName;
    /**
     * 锻炼运动类型
     */
    private String KeepType;
    /**
     * 锻炼日期
     */
    private Date KeepDate;

    public KeepHistory() {
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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

    @Override
    public String toString() {
        return "KeepHistory{" +
                "ID=" + ID +
                ", UserId=" + UserId +
                ", KeepDataId=" + KeepDataId +
                ", KeepName='" + KeepName + '\'' +
                ", KeepType='" + KeepType + '\'' +
                ", KeepDate=" + KeepDate +
                '}';
    }

    public Date getKeepDate() {
        return KeepDate;
    }

    public void setKeepDate(Date keepDate) {
        KeepDate = keepDate;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public int getKeepDataId() {
        return KeepDataId;
    }

    public void setKeepDataId(int keepDataId) {
        KeepDataId = keepDataId;
    }
}
