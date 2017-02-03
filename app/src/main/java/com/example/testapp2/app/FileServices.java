package com.example.testapp2.app;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import org.json.JSONArray;
import android.Manifest;
import org.json.JSONException;
import android.app.Activity;
import org.json.JSONObject;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Code on 2016/8/12 0012.
 * 文件服务，负责处理文件数据，与核心层沟通
 */
public class FileServices {
    /**
     * 上下文参数
     */
    private Context context;
    /**
     * @deprecated 保存文件地址
     */
    private String path = "data.txt";
    /**
     * 相册地址
     */
    private final static String ALBUM_PATH = Environment.getExternalStorageDirectory() + "/download_test/";

    /**
     * 构造函数
     *
     * @param context  上下文参数
     * @param activity 调用的activity
     */
    public FileServices(Context context, Activity activity) {
        this.context = context;
//        请求存储权限
        verifyStoragePermissions(activity);
    }

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     * <p>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    /**
     * @deprecated 不使用，大几率崩溃
     */
    public FileServices() {

    }

    /**
     * 保存图片文件
     *
     * @param bm       需要保存的Bitmap
     * @param fileName 保存文件名
     * @throws IOException 输入输出意外
     */
    public static void saveImageFile(Bitmap bm, String fileName) throws IOException {
        File dirFile = new File(ALBUM_PATH);
        if (!dirFile.exists()) {
//            创建存储目录
            dirFile.mkdir();
        }
//        实例化文件
        File myCaptureFile = new File(ALBUM_PATH + fileName);
//        打开缓存输出流
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
    }

    /**
     * 获得Uri真实地址
     *
     * @param context 上下文参数
     * @param uri     需要转换的Uri
     * @return 真实地址
     */
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 存储KeepData到本地
     *
     * @param fileName  存储文件名
     * @param resultStr 存储的KeepDataJSON字符串
     * @return 保存是否成功
     */
    public boolean saveKeepData(String fileName, String resultStr) {
        boolean flag = false;
        FileOutputStream fileOutputStream = null;
        File file = new File(Environment.getExternalStorageDirectory(), fileName);
        Log.i("Adapter", "File" + file);
//        String resultStr;
//        resultStr = keepDatas.toString();
        Log.i("Adapter", "resultStr-->" + resultStr);
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            try {
                fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(resultStr.getBytes());
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                    flag = true;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.i("Adapter", "False");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    /**
     * 初始化数据，从SharedPreference读取用户信息以及锻炼数据
     */
    public void initData() {
        User user = new User();
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserInfo", Activity.MODE_PRIVATE);
        if (sharedPreferences.getInt("ID", -1) != -1) {
//            若获取到用户ID有效，继续加载数据
            Log.i("Test", String.valueOf(sharedPreferences.getInt("ID", -1)));
            user.setUserId(sharedPreferences.getInt("ID", 0));
            user.setUserName(sharedPreferences.getString("UserName", ""));
            user.setUserNickName(sharedPreferences.getString("UserNickName", ""));
            user.setAlwaysOnline(sharedPreferences.getBoolean("AlwaysOnline", false));
            user.setSexEnum(sharedPreferences.getString("Sex", "None"));
            user.setUserAvatarUrl(sharedPreferences.getString("AvatarUrl", ""));
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                calendar.setTime(simpleDateFormat.parse(sharedPreferences.getString("BirthDay", "1977-01-01")));
                user.setBirthDay(calendar);
                user.setLoginTime(simpleDateFormat.parse(sharedPreferences.getString("LoginTime", "1997-01-01")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
//            判断是否保存了LocalKeepDatas
            if (sharedPreferences.getString("LocalKeepData", "").equals("")) {
//                user.setLocalKeepDatas(readLocalKeepData(path));
            } else {
                user.setLocalKeepDatas(readShareKeepData(sharedPreferences.getString("LocalKeepDatas", "")));
            }
            DataResourses.setIsNoUser(false);
            DataResourses.setCurrentUser(user);
        } else {
//            DataResourses.setKeepDataList(readLocalKeepData(path));
            DataResourses.setIsNoUser(true);
        }
    }

    /**
     * 保存用户信息
     *
     * @param user 待保存的用户
     */
    public void saveUser(User user) {
        if (user.isAlwaysOnline()) {
//            若选择了长期在线，则保存用户
//            Log.i("Test",)
            SharedPreferences.Editor editor = context.getSharedPreferences("UserInfo", Activity.MODE_PRIVATE).edit();
            editor.putInt("ID", user.getUserId());
            editor.putString("UserName", user.getUserName());
            editor.putString("UserNickName", user.getUserNickName());
            editor.putString("Sex", user.getSexEnum().toString());
            editor.putBoolean("AlwaysOnline", user.isAlwaysOnline());
            editor.putString("BirthDay", user.getBirthdayStr());
            editor.putString("LoginTime", user.getLoginTimeStr());
            editor.putString("AvatarUrl", user.getUserAvatarUrl());
            editor.apply();
        }
    }

    /**
     * 从SharedPreference清除用户数据
     */
    public void clearUser() {
        SharedPreferences.Editor editor = context.getSharedPreferences("UserInfo", Activity.MODE_PRIVATE).edit();
        editor.clear();
        Log.i("Test", "Clear!!!");
        editor.apply();
    }

    /**
     * 读取SharedPreference中的KeepDataJSON字符串
     *
     * @param jsonString 待转换的JSON字符串
     * @return KeepData表
     */
    public List<KeepData> readShareKeepData(String jsonString) {
        return json2KeepDatas(jsonString);
    }

    /**
     * JSON字符串转换为KeepData表
     *
     * @param jsonString 待转换的JSON字符串
     * @return 转换后的KeepData表
     */
    public List<KeepData> json2KeepDatas(String jsonString) {
        List<KeepData> keepdatas = new ArrayList<KeepData>();
        try {
//            JSONArray jsonArray = new JSONArray(cache);
            JSONArray jsonArray = new JSONArray(jsonString);
            if (jsonArray.length() > 0) {
//                JSONArray长度大于0，继续解析
                for (int i = 0; i < jsonArray.length(); i++) {
                    KeepData keepData = new KeepData();
                    JSONObject jsonKeepData = jsonArray.getJSONObject(i);
                    keepData.setKeepId(jsonKeepData.getString("ID"));
                    keepData.setKeepName(jsonKeepData.getString("KeepName"));
                    keepData.setKeepType(jsonKeepData.getString("KeepType"));
                    keepData.setKeepTime(jsonKeepData.getInt("KeepTime"));
                    keepData.setKeepDuration(jsonKeepData.getDouble("KeepDuration"));
                    keepData.setPrepareDataList(jSONArrayToBeat(jsonKeepData.getJSONArray("PrepareBeatDataList")));
                    keepData.setKeepDataList(jSONArrayToBeat(jsonKeepData.getJSONArray("KeepBeatDataList")));
                    keepData.setStretchDataList(jSONArrayToBeat(jsonKeepData.getJSONArray("StretchBeatDataList")));
                    keepdatas.add(keepData);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return keepdatas;
    }

    /**
     * 读取本地KeepData
     *
     * @param filename 本地文件名
     * @return KeepData表
     */
    public List<KeepData> readLocalKeepData(String filename) {
        List<KeepData> keepdatas = new ArrayList<KeepData>();
        String cache = null;
        FileInputStream fileInputStream;
        File file = new File(Environment.getExternalStorageDirectory(), filename);
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            try {
                fileInputStream = new FileInputStream(file);
                byte[] tmp = new byte[fileInputStream.available()];
                fileInputStream.read(tmp);
                cache = new String(tmp);
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (FileNotFoundException e) {
//                keepdatas = null;
                return keepdatas;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.i("Test", cache);
        return json2KeepDatas(cache);
    }

    /**
     * JSONArray转为BeatData表
     *
     * @param jsonArray 待转换的JSONArray
     * @return 转换后的BeatData表
     */
    private List<BeatData> jSONArrayToBeat(JSONArray jsonArray) {
        List<BeatData> beatDatas = new ArrayList<BeatData>();
        if (jsonArray.length() > 0) {
//            JSONArray长度大于0，继续
            try {
                for (int i = 0; i < jsonArray.length(); i++) {
                    BeatData beatData;
                    JSONObject beatDataJson = jsonArray.getJSONObject(i);
                    String beatName = beatDataJson.getString("BeatName");
                    int beatNumbers = beatDataJson.getInt("BeatNumbers");
                    double beatTime = beatDataJson.getDouble("BeatTime");
                    int beatSeconds = beatDataJson.getInt("BeatSeconds");
                    if (beatNumbers == -1) {
                        beatData = new BeatData(beatName, beatSeconds);
                    } else {
                        beatData = new BeatData(beatName, beatNumbers, beatTime);
                    }
                    beatDatas.add(beatData);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return beatDatas;
    }

//    private JSONArray beatDataToJSONArray(List<BeatData> beatDatas) {
//        JSONArray beatDataJsonArray = new JSONArray();
//        try {
//            for (int i = 0; i < beatDatas.size(); i++) {
//                JSONObject beatData = new JSONObject();
//                beatData.put("BeatName", beatDatas.get(i).getBeatName());
//                beatData.put("BeatNumbers", beatDatas.get(i).getBeatNumbers());
//                beatData.put("BeatTime", beatDatas.get(i).getBeatTime());
//                beatData.put("BeatSeconds", beatDatas.get(i).getBeatSeconds());
//                beatDataJsonArray.put(i, beatData);
//            }
//        } catch (JSONException e1) {
//            e1.printStackTrace();
//        }
//        return beatDataJsonArray;
//    }
}
