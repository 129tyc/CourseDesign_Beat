package com.example.testapp2.app;

import android.graphics.Bitmap;
import android.net.Uri;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Code on 2016/11/1 0001.
 * 用户类
 */
public class User {
    /**
     * 用户ID，服务器赋予
     */
    private int UserId;
    /**
     * 用户昵称
     */
    private String UserNickName;
    /**
     * 用户头像
     */
    private Bitmap UserAvatar;
    /**
     * 用户头像服务器地址，为文件名
     */
    private String UserAvatarUrl;
    /**
     * 用户头像Uri
     */
    private Uri UserAvatarUri;
    /**
     * 已从服务器同步的用户数据
     */
    private List<KeepData> SyncedKeepDatas;
    /**
     * 本地的用户数据
     */
    private List<KeepData> LocalKeepDatas;
    /**
     * 用户名
     */
    private String UserName;

    /**
     * 性别
     */
    enum Sex {
        /**
         * 男
         */
        Man,
        /**
         * 女
         */
        Woman,
        /**
         * 未选择
         */
        None
    }

    /**
     * 用户锻炼历史表
     */
    private List<KeepHistory> keepHistoryList;
    /**
     * 性别选择
     */
    private Sex SexEnum;
    /**
     * 用户生日
     */
    private Calendar BirthDay;
    /**
     * 最爱运动
     */
    private List<String> FavorSports;
    /**
     * 个人签名
     */
    private String PersonalSignature;
    /**
     * 登录时间
     */
    private Date LoginTime;
    /**
     * 是否总是在在线
     */
    private boolean isAlwaysOnline;
    /**
     * 服务器发送的JSON字符串的Key数组
     */
    private String[] jsonString = {"ID", "UserNickName", "Avatar", "LastLoginDate", "Sex", "BirthDay"};

    List<KeepHistory> getKeepHistoryList() {
        return keepHistoryList;
    }

    @Override
    public String toString() {
        return "User{" +
                "UserId=" + UserId +
                ", UserNickName='" + UserNickName + '\'' +
                ", UserAvatar=" + UserAvatar +
                ", UserAvatarUrl='" + UserAvatarUrl + '\'' +
                ", UserAvatarUri=" + UserAvatarUri +
                ", SyncedKeepDatas=" + SyncedKeepDatas +
                ", LocalKeepDatas=" + LocalKeepDatas +
                ", UserName='" + UserName + '\'' +
                ", keepHistoryList=" + keepHistoryList +
                ", SexEnum=" + SexEnum +
                ", BirthDay=" + BirthDay +
                ", FavorSports=" + FavorSports +
                ", PersonalSignature='" + PersonalSignature + '\'' +
                ", LoginTime=" + LoginTime +
                ", isAlwaysOnline=" + isAlwaysOnline +
                ", jsonString=" + Arrays.toString(jsonString) +
                '}';
    }

    Uri getUserAvatarUri() {
        return UserAvatarUri;
    }

    void setUserAvatarUri(Uri userAvatarUri) {
        UserAvatarUri = userAvatarUri;
    }

    void setKeepHistoryList(List<KeepHistory> keepHistoryList) {
        this.keepHistoryList = keepHistoryList;
    }

    public void setSexEnum(Sex sexEnum) {
        SexEnum = sexEnum;
    }

    String getUserAvatarUrl() {
        return UserAvatarUrl;
    }

    /**
     * 判断用户是否为空
     *
     * @return 空返回真，非空返回假
     */
    boolean isEmpty() {
        return UserId == 0;

    }

    /**
     * 获得生日的常规化字符串
     *
     * @return 字符串
     */
    String getBirthdayStr() {
        if (this.BirthDay == null) {
            return "";
        }
        return getStr(this.BirthDay.getTime());
    }

    /**
     * 获得Date类的常规化字符串
     *
     * @param date 待转换的Date
     * @return 转换后的字符串
     */
    private String getStr(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }

    /**
     * 获得登录时间的常规化字符串
     *
     * @return 转换后的字符串
     */
    String getLoginTimeStr() {
        if (this.LoginTime == null) {
            return "";
        }
        return getStr(this.LoginTime);
    }

    /**
     * 获得生日的常规化字符串
     *
     * @param calendar Calendar类的生日
     * @return 常规字符串
     */
    String getBirthdayStr(Calendar calendar) {
        if (calendar == null) {
            return "";
        }
        return getStr(calendar.getTime());
    }

    void setUserAvatarUrl(String userAvatarUrl) {
        UserAvatarUrl = userAvatarUrl;
    }

    Sex getSexEnum() {
        return SexEnum;
    }

    void setSexEnum(String sexEnum) {
        SexEnum = Sex.valueOf(sexEnum);
    }

    /**
     * @return
     * @deprecated
     */
    public String[] getJsonString() {
        return jsonString;
    }

    /**
     * @param jsonString
     * @deprecated
     */
    public void setJsonString(String[] jsonString) {
        this.jsonString = jsonString;
    }

    /**
     * 构造函数
     *
     * @param userId    用户ID
     * @param userName  用户名
     * @param sex       性别
     * @param birthDay  生日
     * @param loginTime 登陆时间
     */
    public User(int userId, String userName, String sex, Calendar birthDay, Date loginTime) {
        UserId = userId;
        UserName = userName;
        SexEnum = Sex.valueOf(sex);
        BirthDay = birthDay;
        LoginTime = loginTime;
        initList();
    }

    /**
     * 初始化表
     */
    private void initList() {
        this.SyncedKeepDatas = new ArrayList<KeepData>();
        this.LocalKeepDatas = new ArrayList<KeepData>();
        this.keepHistoryList = new ArrayList<KeepHistory>();
        this.FavorSports = new ArrayList<String>();
    }

    /**
     * 构造函数
     *
     * @param userId 用户ID
     */
    public User(int userId) {
        UserId = userId;
        initList();
    }

    /**
     * 构造函数
     */
    User() {
        UserName = "";
        UserId = 0;
        initList();
    }

    int getUserId() {
        return UserId;
    }

    void setUserId(int userId) {
        UserId = userId;
    }

    String getUserNickName() {
        return UserNickName;
    }

    void setUserNickName(String userNickName) {
        UserNickName = userNickName;
    }

    Bitmap getUserAvatar() {
        return UserAvatar;
    }

    void setUserAvatar(Bitmap userAvatar) {
        UserAvatar = userAvatar;
    }


    List<KeepData> getSyncedKeepDatas() {
        return SyncedKeepDatas;
    }

    void setSyncedKeepDatas(List<KeepData> syncedKeepDatas) {
        SyncedKeepDatas = syncedKeepDatas;
    }

    List<KeepData> getLocalKeepDatas() {
        return LocalKeepDatas;
    }

    void setLocalKeepDatas(List<KeepData> localKeepDatas) {
        LocalKeepDatas = localKeepDatas;
    }

    String getUserName() {
        return UserName;
    }

    void setUserName(String userName) {
        UserName = userName;
    }

    Calendar getBirthDay() {
        return BirthDay;
    }

    void setBirthDay(Calendar birthDay) {
        BirthDay = birthDay;
    }

    /**
     * @return
     * @deprecated
     */
    public List<String> getFavorSports() {
        return FavorSports;
    }

    /**
     * @param favorSports
     * @deprecated
     */
    public void setFavorSports(List<String> favorSports) {
        FavorSports = favorSports;
    }

    /**
     * @return
     * @deprecated
     */
    public String getPersonalSignature() {
        return PersonalSignature;
    }

    /**
     * @param personalSignature
     * @deprecated
     */
    public void setPersonalSignature(String personalSignature) {
        PersonalSignature = personalSignature;
    }

    public Date getLoginTime() {
        return LoginTime;
    }

    void setLoginTime(Date loginTime) {
        LoginTime = loginTime;
    }

    boolean isAlwaysOnline() {
        return isAlwaysOnline;
    }

    void setAlwaysOnline(boolean alwaysOnline) {
        isAlwaysOnline = alwaysOnline;
    }
}
