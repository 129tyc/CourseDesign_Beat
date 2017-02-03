package com.example.testapp2.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Code on 2016/8/12 0012.
 * 数据资源
 * 核心层
 * 负责沟通上下部分
 * FIXME 2016年12月12日15:17:54
 */
public class DataResourses {
    /**
     * 主KeepData表，负责大多数时候的数据展示
     */
    private static List<KeepData> keepDataList;
    /**
     * 文件服务
     */
    private FileServices fileServices;
    /**
     * 保存文件名
     */
    private final String FILENAME = "data.txt";
    /**
     * 可编辑KeepData对象
     */
    private static KeepData keepData;
    /**
     * 上下文
     */
    private Context context;
    /**
     * 调用的activity
     */
    private Activity activity;
    /**
     * 临时KeepData对象
     */
    private static KeepData tempKeepData;
    /**
     * 当前用户
     */
    private static User currentUser;
    /**
     * 是否有用户
     */
    private static boolean isNoUser;

    public static boolean isNoUser() {
        return isNoUser;
    }

    public static void setIsNoUser(boolean isNoUser) {
        DataResourses.isNoUser = isNoUser;
    }

    /**
     * 获得临时KeepData
     *
     * @return 临时KeepData
     */
    public static KeepData getTempKeepData() {
        return tempKeepData;
    }

    /**
     * 清空临时KeepData
     *
     * @return 清空返回真，未清空返回假
     */
    public static boolean clearTempKeepData() {
        if (tempKeepData != null) {
            tempKeepData = null;
            return true;
        }
        return false;
    }

    /**
     * 清空可编辑KeepData
     *
     * @return 清空返回真，未清空返回假
     */
    public static boolean clearEditableKeepData() {
        if (keepData != null) {
            keepData = null;
            return true;
        }
        return false;
    }

    /**
     * 设置KeepData
     *
     * @param keepData 待设置的KeepData
     * @deprecated 不建议使用
     */
    public static void setKeepData(KeepData keepData) {
        if (DataResourses.keepData == null) {
            DataResourses.keepData = new KeepData();
        }
        DataResourses.keepData.setKeepName(keepData.getKeepName());
        DataResourses.keepData.setKeepType(keepData.getKeepType());
        DataResourses.keepData.setKeepTime(keepData.getKeepTime());
        DataResourses.keepData.setKeepDuration(keepData.getKeepDuration());
        DataResourses.keepData.setPrepareDataList(keepData.getPrepareDataList());
        DataResourses.keepData.setKeepDataList(keepData.getKeepDataList());
        DataResourses.keepData.setStretchDataList(keepData.getStretchDataList());
    }

    /**
     * 设置临时KeepData
     *
     * @param keepData 待存入的KeepData
     */
    public static void setTempKeepData(KeepData keepData) {
        DataResourses.tempKeepData = new KeepData();
        DataResourses.tempKeepData.setKeepName(keepData.getKeepName());
        DataResourses.tempKeepData.setKeepType(keepData.getKeepType());
        DataResourses.tempKeepData.setKeepTime(keepData.getKeepTime());
        DataResourses.tempKeepData.setKeepDuration(keepData.getKeepDuration());
        DataResourses.tempKeepData.setPrepareDataList(keepData.getPrepareDataList());
        DataResourses.tempKeepData.setKeepDataList(keepData.getKeepDataList());
        DataResourses.tempKeepData.setStretchDataList(keepData.getStretchDataList());
    }

    /**
     * 获得可编辑的KeepData
     *
     * @return 可编辑的KeepData
     */
    public static KeepData getEditableKeepData() {
        if (keepData == null) {
            keepData = new KeepData();
        }
        return keepData;
    }

    /**
     * 清空当前用户，并置isNoUser为真
     */
    public static void clearCurrentUser() {
        if (currentUser != null) {
            currentUser = null;
        }
        currentUser = new User();
        isNoUser = true;
    }

    /**
     * 判断可编辑对象是否为空
     *
     * @return 空返回真，不空返回假
     */
    public static boolean isDestroyEditableKeepData() {
        if (keepData != null) {
            keepData = null;
            return true;
        }
        return false;
    }

    /**
     * 获得当前用户，当前用户为空时实例化新用户并赋予当前用户
     *
     * @return 当前用户
     */
    public static User getCurrentUser() {
        if (currentUser == null) {
            currentUser = new User();
        }
        return currentUser;
    }

    /**
     * 设置当前用户
     *
     * @param currentUser 待设置的用户
     */
    public static void setCurrentUser(User currentUser) {
        DataResourses.currentUser = currentUser;
    }

    /**
     * 构造函数
     *
     * @deprecated 不建议使用，可能导致GC
     */
    public DataResourses() {
        if (keepDataList == null) {
            keepDataList = new ArrayList<KeepData>();
            Log.i("Test", "InitSuccess");
        }
        if (currentUser == null) {
            currentUser = new User();
        }
    }

    /**
     * 将KeepDataList中指定位置的数据导入可编辑KeepData中
     *
     * @param position 导出的位置
     */
    public static void setEditableKeepData(int position) {
        if (keepDataList == null) {
            keepDataList = new ArrayList<KeepData>();
        }
        if (keepData == null) {
            getEditableKeepData();
        }
        KeepData tmpKeepData = keepDataList.get(position);
        keepData.setKeepName(tmpKeepData.getKeepName());
        keepData.setKeepTime(tmpKeepData.getKeepTime());
        keepData.setKeepType(tmpKeepData.getKeepType());
        keepData.setKeepDuration(tmpKeepData.getKeepDuration());
        keepData.setPrepareDataList(tmpKeepData.getPrepareDataList());
        keepData.setKeepDataList(tmpKeepData.getKeepDataList());
        keepData.setStretchDataList(tmpKeepData.getStretchDataList());
    }

    /**
     * 构造函数
     *
     * @param context  上下文参数
     * @param activity 调用活动参数
     */
    public DataResourses(Context context, Activity activity) {
        if (keepDataList == null) {
            keepDataList = new ArrayList<KeepData>();
            Log.i("Test", "InitSuccess");
        }
        this.context = context;
        this.activity = activity;
//        实例化FileServices
        fileServices = new FileServices(context, activity);
        if (currentUser == null) {
            currentUser = new User();
        }
    }

    /**
     * 从网络获得头像
     *
     * @param handler 回调handler
     */
    public static void getAvatar(final Handler handler) {
        new HttpUtil().actionGetAvatar(currentUser.getUserAvatarUrl(), new HttpUtil.DataProcess() {
            @Override
            public void processComplete(Object object) {
                if (object != null) {
                    currentUser.setUserAvatar((Bitmap) object);
//                try {
//                    FileServices.saveImageFile( currentUser.getUserAvatar(),  currentUser.getUserAvatarUrl());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                    handler.sendEmptyMessage(10);
                } else {
                    handler.sendEmptyMessage(0);
                }
            }
        });
    }

    /**
     * 从JSON中设置用户
     *
     * @param dataProcess 回调接口
     * @param jsonObject  JSON对象
     */
    public void setUser(HttpUtil.DataProcess dataProcess, JSONObject jsonObject) {
        User user = new User();
        try {
            user.setUserId(Integer.parseInt(jsonObject.getString("ID")));
            user.setSexEnum(jsonObject.getString("Sex"));
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            calendar.setTime(simpleDateFormat.parse(jsonObject.getString("BirthDay")));
            user.setBirthDay(calendar);
            user.setUserAvatarUrl(jsonObject.getString("Avatar"));
            user.setUserName(jsonObject.getString("UserName"));
            user.setUserNickName(jsonObject.getString("UserNickName"));
            user.setLoginTime(simpleDateFormat.parse(jsonObject.getString("LastLoginDate")));
            user.setLocalKeepDatas(new ArrayList<KeepData>());
            user.setSyncedKeepDatas(new ArrayList<KeepData>());
//            设置用户，调用回调函数
            dataProcess.processComplete(user);
        } catch (JSONException e1) {
            e1.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    /**
     * 保存用户信息到SharedPreference中
     */
    public void saveUserInfo() {
        if (!isNoUser || !currentUser.isEmpty()) {
            fileServices.saveUser(currentUser);
        }
    }

    /**
     * 清除SharedPreference中的用户信息
     */
    public void clearUserInfo() {
        if (!isNoUser() || !currentUser.isEmpty()) {
            fileServices.clearUser();
        }
        isNoUser = true;
    }

    /**
     * 上传用户信息
     *
     * @param context     上下文参数
     * @param view        SnackBar调用view参数
     * @param dataProcess 回调接口
     * @param avatar      用户头像
     */
    public static void uploadUserInfo(Context context, View view, HttpUtil.DataProcess dataProcess, Bitmap avatar) {
        JSONObject json = new JSONObject();
        try {
            json.put("UserNickName", currentUser.getUserNickName());
            json.put("Sex", currentUser.getSexEnum().toString());
            json.put("BirthDay", currentUser.getBirthdayStr());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new HttpUtil().actionUploadUser(context, view, dataProcess, currentUser, json.toString(), avatar);
    }

    /**
     * 判断是否需要查询锻炼数据
     *
     * @return SyncedKeepDatas为空，返回真，否则返回假
     */
    public static boolean needQueryData() {
        if (currentUser.getSyncedKeepDatas() == null || currentUser.getSyncedKeepDatas().isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * app启动时初始化用户及锻炼数据
     */
    public void initUser() {
//        调用FileServices初始化数据
        fileServices.initData();
        if (isNoUser) {
//            若未找到保存用户，进入登陆页面
            Intent intent = new Intent(activity, LoginPage.class);
            context.startActivity(intent);
        } else {
//            设置keepDataList为当前用户的本地锻炼数据
            if (currentUser.getLocalKeepDatas() != null) {
                keepDataList = currentUser.getLocalKeepDatas();
            } else {
                keepDataList = new ArrayList<KeepData>();
            }
            Log.i("Test", "--InitUser-->" + currentUser.toString());
        }
    }

//    public void queryUserInfo() {
//
//    }

    /**
     * 解析BeatData的JSON数据
     *
     * @param jsonArray BeatDataJSON数组
     * @return BeatData表
     * @throws JSONException JSON解析意外
     */
    private List<BeatData> parseBeatDataList(JSONArray jsonArray) throws JSONException {
        List<BeatData> tmpBeatDatas = new ArrayList<BeatData>();
        for (int i = 0; i < jsonArray.length(); i++) {
//            根据JSON数组元素数循环添加BeatData
            JSONObject tmpBeatData = jsonArray.getJSONObject(i);
            BeatData beatData;
            if (tmpBeatData.getString("BeatNumbers").equals("-1")) {
//                当BeatNumbers为-1，即鼓点数据记录持续时间
                beatData = new BeatData(tmpBeatData.getString("BeatName"),
                        Integer.parseInt(tmpBeatData.getString("BeatSeconds")));
            } else {
//                BeatNumbers不为-1，记录次数以及间隔
                beatData = new BeatData(tmpBeatData.getString("BeatName"),
                        Integer.parseInt(tmpBeatData.getString("BeatNumbers")),
                        Double.parseDouble(tmpBeatData.getString("BeatTime")));
            }
            tmpBeatDatas.add(beatData);
        }
        return tmpBeatDatas;
    }

    /**
     * 解析KeepDataJSON数据
     *
     * @param keepDatas KeepDataJSON数组
     * @return KeepData表
     * @throws JSONException JSON解析意外
     */
    private List<KeepData> parseKeepData(JSONArray keepDatas) throws JSONException {
        List<KeepData> tmpKeepDatas = new ArrayList<KeepData>();
        for (int i = 0; i < keepDatas.length(); i++) {
//            根据JSON数组元素数循环添加KeepData
            KeepData keepData = new KeepData();
            JSONObject keepDataJson = keepDatas.getJSONObject(i);
            Log.i("Test", keepDataJson.toString());
            keepData.setKeepId(keepDataJson.getString("ID"));
            keepData.setKeepName(keepDataJson.getString("KeepName"));
            keepData.setKeepType(keepDataJson.getString("KeepType"));
            keepData.setKeepTime(Integer.parseInt(keepDataJson.getString("KeepTime")));
            keepData.setKeepDuration(Double.parseDouble(keepDataJson.getString("KeepDuration")));
//            调用parseBeatDataList解析所属的BeatData表
            keepData.setPrepareDataList(parseBeatDataList(keepDataJson.getJSONArray("PrepareBeatDataList")));
            keepData.setKeepDataList(parseBeatDataList(keepDataJson.getJSONArray("KeepBeatDataList")));
            keepData.setStretchDataList(parseBeatDataList(keepDataJson.getJSONArray("StretchBeatDataList")));
            tmpKeepDatas.add(keepData);
        }
        return tmpKeepDatas;
    }

    /**
     * 解析锻炼历史
     *
     * @param keepHistories KeepHistoryJSON数组
     * @return KeepHistory表
     * @throws JSONException  JSON解析意外
     * @throws ParseException Date解析意外
     */
    private List<KeepHistory> parseKeepHistory(JSONArray keepHistories) throws JSONException, ParseException {
        List<KeepHistory> tmpKeepHistories = new ArrayList<KeepHistory>();
        for (int i = 0; i < keepHistories.length(); i++) {
            JSONObject tmpKeepHistory = keepHistories.getJSONObject(i);
            KeepHistory keepHistory = new KeepHistory();
            keepHistory.setID(Integer.parseInt(tmpKeepHistory.getString("KeepHistoryID")));
            keepHistory.setKeepName(tmpKeepHistory.getString("KeepName"));
            keepHistory.setKeepType(tmpKeepHistory.getString("KeepType"));
            keepHistory.setKeepDataId(Integer.parseInt(tmpKeepHistory.getString("KeepDataId")));
            keepHistory.setKeepDate(sqlDateStr2Date(tmpKeepHistory.getString("KeepDate"), tmpKeepHistory.getString("KeepDate_Time")));
            tmpKeepHistories.add(keepHistory);
        }
        return tmpKeepHistories;
    }

    /**
     * sql类型日期转换为Date类型
     *
     * @param date 日期字符串
     * @param time 时间字符串
     * @return 解析的日期
     * @throws ParseException Date解析意外
     */
    private Date sqlDateStr2Date(String date, String time) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.parse(date + " " + time);
    }

    /**
     * Date类转换为常规字符串
     *
     * @param date 待转换的Date日期
     * @return 转换后的字符串
     */
    private String date2Str(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = simpleDateFormat.format(date.getTime());
        return str;
    }

    /**
     * sql类型日期转换为Calendar日期
     *
     * @param date 待转换日期字符串
     * @param time 待转换时间字符串
     * @return 转换后的Calendar类
     * @throws ParseException Date解析意外
     */
    private Calendar date2Calendar(String date, String time) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sqlDateStr2Date(date, time));
        return calendar;
    }

    /**
     * BeatData转为JSON数组
     *
     * @param beatDatas BeatData表
     * @return 转换后的JSON数组
     */
    private JSONArray beatData2Json(List<BeatData> beatDatas) {
        JSONArray beatDataJsonArray = new JSONArray();
        try {
            for (int i = 0; i < beatDatas.size(); i++) {
//                循环将各鼓点元素转换为JSONObject
                JSONObject beatData = new JSONObject();
                beatData.put("BeatName", beatDatas.get(i).getBeatName());
                beatData.put("BeatNumbers", beatDatas.get(i).getBeatNumbers());
                beatData.put("BeatTime", beatDatas.get(i).getBeatTime());
                beatData.put("BeatSeconds", beatDatas.get(i).getBeatSeconds());
                beatDataJsonArray.put(i, beatData);
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return beatDataJsonArray;
    }

    /**
     * 锻炼历史转为JSON数组
     *
     * @param keepHistoryList 待转换的KeepHistory表
     * @return 转换后的JSON数组
     */
    public JSONArray keepHistories2Json(List<KeepHistory> keepHistoryList) {
        JSONArray keepHistories = new JSONArray();
        for (int i = 0; i < keepHistoryList.size(); i++) {
//            循环将各个元素转为JSONObject
            JSONObject keepHistory = new JSONObject();
            try {
                keepHistory.put("ID", keepHistoryList.get(i).getID());
                keepHistory.put("KeepDate", date2Str(keepHistoryList.get(i).getKeepDate()));
                keepHistory.put("KeepDataId", keepHistoryList.get(i).getKeepDataId());
                keepHistory.put("KeepUserId", keepHistoryList.get(i).getUserId());
                keepHistories.put(keepHistory);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return keepHistories;
    }

    /**
     * 转换KeepHistory为JSONArray并返回String形式
     *
     * @param keepHistoryList 待转换的KeepHistory表
     * @return String类型的JSONArray
     */
    public String keepHistories2JsonStr(List<KeepHistory> keepHistoryList) {
        return keepHistories2Json(keepHistoryList).toString();
    }

    /**
     * KeepData表转为JSON数组
     *
     * @param list 待转换的KeepData表
     * @return 转换后的JSONArray
     */
    public JSONArray keepDatas2Json(List<KeepData> list) {
        JSONArray keepDatas = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
//            循环将各个KeepData转为JSONObject
            try {
                JSONObject keepData = new JSONObject();
                keepData.put("ID", list.get(i).getKeepId());
                keepData.put("KeepName", list.get(i).getKeepName());
                keepData.put("KeepType", list.get(i).getKeepType());
                keepData.put("KeepTime", list.get(i).getKeepTime());
                keepData.put("KeepDuration", list.get(i).getKeepDuration());
                keepData.put("PrepareBeatDataList", beatData2Json(list.get(i).getPrepareDataList()));
                keepData.put("KeepBeatDataList", beatData2Json(list.get(i).getKeepDataList()));
                keepData.put("StretchBeatDataList", beatData2Json(list.get(i).getStretchDataList()));
                keepDatas.put(i, keepData);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        return keepDatas;
    }

    /**
     * 转换KeepData为JSONArray并返回String形式
     *
     * @param list 待转换的KeepData表
     * @return String类型的JSONArray
     */
    public String keepDatas2JsonStr(List<KeepData> list) {
        return keepDatas2Json(list).toString();
    }

//    public void syncData(View view) {
//    }

    /**
     * 查询用户的锻炼数据
     *
     * @param view    调用的View
     * @param handler 回调的Handler
     */
    public void queryData(View view, final Handler handler) {
        if (!isNoUser && !currentUser.isEmpty()) {
//            确定存在登陆用户，开始调用HttpUtil查询数据
            new HttpUtil().actionQueryData(String.valueOf(currentUser.getUserId()), currentUser.getUserName(), context, view, new HttpUtil.DataProcess() {
                @Override
                public void processComplete(Object object) {
                    try {
//                        查询成功，将获得的JSON数据转换为KeepData和KeepHistory表
                        JSONObject info = new JSONObject(object.toString());
                        JSONArray keepDatas = info.getJSONArray("KeepDatas");
                        JSONArray keepHistories = info.getJSONArray("KeepHistory");
                        currentUser.setSyncedKeepDatas(parseKeepData(keepDatas));
                        currentUser.setKeepHistoryList(parseKeepHistory(keepHistories));
                        if (currentUser.getLocalKeepDatas() == null || currentUser.getLocalKeepDatas().isEmpty()) {
//                            若本地无数据，则将该数据赋予当前用户的LocalKeepDatas
                            currentUser.setLocalKeepDatas(currentUser.getSyncedKeepDatas());
                        }
//                        处理结束，通知activity更新数据
                        handler.sendEmptyMessage(1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
//    public void readData() {
//        fileServices = new FileServices(context, activity);
//        keepDataList = fileServices.readKeepData(FILENAME);
//        if (keepDataList == null) {
//            keepDataList = new ArrayList<KeepData>();
//        }
//    }


    public static List<KeepData> getKeepDataList() {
        return keepDataList;
    }

    public static void setKeepDataList(List<KeepData> keepDataList) {
        if (keepDataList != null && !keepDataList.isEmpty()) {
            DataResourses.keepDataList.clear();
            DataResourses.keepDataList.addAll(keepDataList);
        }
    }

    /**
     * 上传用户数据并保存到本地
     * FIXME: 2016/12/12 0012
     *
     * @param view        调用的view
     * @param dataProcess 回调接口
     * @return 保存到本地成功返回真，否则返回假
     */
    public boolean saveData(View view, HttpUtil.DataProcess dataProcess) {
//        将KeepDataList设置到当前用户LocalKeepDatas
        currentUser.setLocalKeepDatas(keepDataList);
        JSONObject data = new JSONObject();
        try {
//            将KeepData和KeepHistory表转换为JSONArray并打包到data
            data.put("KeepDatas", keepDatas2Json(currentUser.getLocalKeepDatas()));
            data.put("KeepHistory", keepHistories2Json(currentUser.getKeepHistoryList()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("Test", data.toString());
        Log.i("Test", keepDataList.toString());
//        向服务器发送转换后的数据
        new HttpUtil().actionSync(view, context, dataProcess, currentUser, data.toString());
        fileServices = new FileServices(context, activity);
//        保存KeepDataList到本地
        return fileServices.saveKeepData(FILENAME, keepDatas2JsonStr(keepDataList));

    }

    //    public static List<Map<String, String>> getDisplayListMaps() {
//        List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
//        if (keepDataList != null) {
//            for (KeepData e :
//                    keepDataList) {
//                Map<String, String> map = new HashMap<String, String>();
//                map.put("KeepName", e.getKeepName());
//                map.put("KeepType", e.getKeepType());
//                mapList.add(map);
//            }
//        }
//        return mapList;
//    }

    /**
     * @deprecated 没有维护过
     */
    public static List<Map<String, String>> getBeatDataListMaps(List<BeatData> beatDataList) {
        List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
        if (beatDataList != null) {
            for (BeatData e :
                    beatDataList) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("BeatName", e.getBeatName());
                if (e.getBeatNumbers() == BeatData.INVALIDCODE) {
                    map.put("BeatSeconds", String.valueOf(e.getBeatSeconds()));
                    map.put("BeatTime", "");
                    map.put("BeatNumbers", "");
                } else {
                    map.put("BeatTime", String.valueOf(e.getBeatTime()));
                    map.put("BeatNumbers", String.valueOf(e.getBeatNumbers()));
                    map.put("BeatSeconds", "");
                }
                mapList.add(map);
            }
        }
        return mapList;
    }
}
