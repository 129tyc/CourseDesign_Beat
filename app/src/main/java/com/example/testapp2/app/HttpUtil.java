package com.example.testapp2.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Code on 2016/11/5 0005.
 * 网络工具类，负责与服务器沟通数据
 */
public class HttpUtil {
    /**
     * 根地址
     */
//    private String rootUrl = "http://192.168.191.4:8001/";
//    private String rootUrl = "http://zzusimulationlabs.in.3322.org:25378/beat/";
    private String rootUrl = "http://zzuexpfz.f3322.net:8080/beat/";
    /**
     * servlet地址
     */
    private String url = rootUrl + "servlet/";
    /**
     * 头像存储地址
     */
    private String avatarUrl = rootUrl + "avatar/";


    /**
     * 构造函数
     */
    public HttpUtil() {
//        try {
//            this.url = new URL("");
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 接口，负责网络连接结果回调
     *
     * @param <T>
     */
    interface PostCallBack<T> {
        void postFailed(T object);

        void postSuccess(T object);
    }

    /**
     * 接口，负责数据处理结果回调
     *
     * @param <T>
     */
    interface DataProcess<T> {
        void processComplete(T object);
    }

    /**
     * 登陆操作
     *
     * @param view         调用的view
     * @param context      上下文参数
     * @param userName     登陆用户名
     * @param userPassword 登陆用户密码
     * @param dataProcess  回调接口
     */
    public void actionLogin(final View view, final Context context, final String userName, final String userPassword, final DataProcess dataProcess) {
//        实例化进度条
        final ProgressDialog progressDialog = new ProgressDialog(context);
//        启动异步任务
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
//                构造Url
                StringBuffer stringBuffer = new StringBuffer(url + "actionLogin?");
                stringBuffer.append("UserName=" + paramsLegalize(userName) + "&" + "UserPass=" + paramsLegalize(userPassword));
                String str = stringBuffer.toString();
                ;
                try {
                    URL mUrl = new URL(str);
//                    通过get发送数据
                    postDataByGet(mUrl, new PostCallBack() {
                        @Override
                        public void postFailed(Object object) {
//                            发送数据失败，提示连接错误
                            progressDialog.dismiss();
                            String err;
                            if (Boolean.class.isInstance(object)) {
                                if ((Boolean) object) {
                                    err = "认证服务器离线，请稍后再试！";
                                } else {
                                    err = "无法连接到服务器，请检查网络连接是否正确";
                                }
                            } else {
                                err = "登陆失败，错误码" + object.toString();
                            }
                            Snackbar.make(view, err, Snackbar.LENGTH_LONG).show();
                        }

                        @Override
                        public void postSuccess(Object object) {
                            try {
//                                发送成功，获得返回数据
                                progressDialog.dismiss();
                                JSONObject jsonObject = new JSONObject((String) object);
                                String msg = jsonObject.getString("msg");
                                String event = jsonObject.getString("event");
                                String obj = jsonObject.getString("info");
                                if (event.equals("1")) {
//                                    返回码为1，提示登陆成功，处理用户信息
                                    Snackbar snackbar = Snackbar.make(view, "登陆成功！", Snackbar.LENGTH_SHORT);
                                    snackbar.show();
                                    while (snackbar.isShown()) {

                                    }
                                    JSONObject temp = new JSONObject(obj);
                                    new DataResourses().setUser(dataProcess, temp);
                                    Log.i("Test", obj);
                                } else {
//                                    返回码不为1，提示登陆失败
                                    Snackbar.make(view, "认证失败，请检查用户名或密码是否正确", Snackbar.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                设置进度条基本属性
                progressDialog.setTitle("注意！");
                progressDialog.setMessage("登陆中……");
                progressDialog.show();
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);

            }
        };
        asyncTask.execute();
    }

    /**
     * 查询注册用户名是否重复
     *
     * @param userName    待查询用户名
     * @param dataProcess 回调接口
     */
    public void actionQueryIsRepeat(final String userName, final DataProcess dataProcess) {
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
//                构造Url
                StringBuffer stringBuffer = new StringBuffer(url + "actionQuery?");
                stringBuffer.append("UserName=" + paramsLegalize(userName));
                String str = stringBuffer.toString();
                ;
                try {
                    URL mUrl = new URL(str);
//                    通过GET发送
                    postDataByGet(mUrl, new PostCallBack() {
                        @Override
                        public void postFailed(Object object) {

                        }

                        @Override
                        public void postSuccess(Object object) {
                            try {
//                                数据获取成功，调用回调函数并将结果码返回
                                JSONObject jsonObject = new JSONObject((String) object);
                                String msg = jsonObject.getString("msg");
                                String event = jsonObject.getString("event");
                                String obj = jsonObject.getString("info");
                                dataProcess.processComplete(event);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                return null;
            }

        };
        asyncTask.execute();
    }

    /**
     * 注册操作
     *
     * @param view        调用的view
     * @param context     上下文参数
     * @param userName    注册的用户名
     * @param userPass    注册的用户密码
     * @param dataProcess 回调接口
     */
    public void actionRegister(final View view, final Context context, final String userName, final String userPass, final DataProcess dataProcess) {
//        实例化进度条
        final ProgressDialog progressDialog = new ProgressDialog(context);
//        启动异步任务
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
//                构造Url
                StringBuffer stringBuffer = new StringBuffer(url + "actionRegister?");
                stringBuffer.append("UserName=" + paramsLegalize(userName) + "&" + "UserPass=" + paramsLegalize(userPass));
                String str = stringBuffer.toString();
                ;
                try {
                    URL mUrl = new URL(str);
//                    通过GET发送数据
                    postDataByGet(mUrl, new PostCallBack() {
                        @Override
                        public void postFailed(Object object) {
//                            数据发送失败，根据错误码提示消息
                            progressDialog.dismiss();
                            String err;
                            if ((Boolean) object) {
                                err = "服务器离线，请稍后重试！";
                            } else if (!(Boolean) object) {
                                err = "连接服务器失败，请检查网络！";
                            } else {
                                err = "登陆失败，错误码" + object.toString();
                            }
                            Snackbar.make(view, err, Snackbar.LENGTH_LONG).show();
                        }

                        @Override
                        public void postSuccess(Object object) {
                            try {
//                                数据发送成功，得到服务器返回数据
                                progressDialog.dismiss();
                                JSONObject jsonObject = new JSONObject((String) object);
                                String msg = jsonObject.getString("msg");
                                String event = jsonObject.getString("event");
                                String obj = jsonObject.getString("info");
                                if (event.equals("1")) {
//                                    结果码为1，注册成功，调用回调函数
                                    Snackbar snackbar = Snackbar.make(view, "注册新用户成功！", Snackbar.LENGTH_SHORT);
                                    snackbar.show();
                                    while (snackbar.isShown()) {

                                    }
                                    dataProcess.processComplete(true);
                                } else {
                                    Snackbar.make(view, "注册被拒绝！", Snackbar.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                配置进度条基本参数
                progressDialog.setTitle("注意！");
                progressDialog.setMessage("正在联系服务器……");
                progressDialog.show();
            }
        };
        asyncTask.execute();
    }

    /**
     * 查询用户数据操作
     *
     * @param userId      用户ID
     * @param userName    用户名
     * @param context     上下文参数
     * @param view        调用的view
     * @param dataProcess 回调接口
     */
    public void actionQueryData(final String userId, final String userName, Context context, final View view, final DataProcess dataProcess) {
//        实例化进度条
        final ProgressDialog progressDialog = new ProgressDialog(context);
//        启动异步任务
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(final Object[] params) {
//                构造Url
                StringBuffer stringBuffer = new StringBuffer(url + "actionSync?");
                stringBuffer.append("UserName=" + paramsLegalize(userName) + "&ID=" + paramsLegalize(userId) + "&Type=QueryKeepData");
                String str = stringBuffer.toString();
                try {
                    URL mUrl = new URL(str);
//                    通过GET发送数据
                    postDataByGet(mUrl, new PostCallBack() {
                        @Override
                        public void postFailed(Object object) {
//                            发送失败，提示错误消息
                            progressDialog.dismiss();
                            Snackbar.make(view, "获取用户数据失败，请检查网络！", Snackbar.LENGTH_LONG).show();
                        }

                        @Override
                        public void postSuccess(Object object) {
//                            发送成功，处理服务器回传数据
                            progressDialog.dismiss();
                            try {
                                JSONObject jsonObject = new JSONObject((String) object);
                                String msg = jsonObject.getString("msg");
                                String event = jsonObject.getString("event");
                                String obj = jsonObject.getString("info");
                                if (event.equals("1")) {
//                                    结果码为1，调用回调函数并将数据传出
                                    dataProcess.processComplete(obj);
                                } else if (event.equals("2")) {
                                    Snackbar.make(view, "未查询到锻炼数据！", Snackbar.LENGTH_LONG).show();
                                } else {
                                    Snackbar.make(view, "错误码" + event + "," + msg, Snackbar.LENGTH_LONG).show();

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                配置进度条基本属性
                progressDialog.setMessage("正在获取锻炼数据……");
                progressDialog.setTitle("注意！");
                progressDialog.show();
            }
        };
        asyncTask.execute();
    }

    /**
     * 上传用户头像
     *
     * @param context     上下文参数
     * @param view        调用的viwe
     * @param syncUser    待上传的用户
     * @param avatar      待上传的头像
     * @param dataProcess 回调接口
     */
    public void actionUploadAvatar(final Context context, final View view, final User syncUser, Bitmap avatar, final DataProcess dataProcess) {
//        启动异步任务
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(final Object[] params) {
//                构造Url
                StringBuffer stringBuffer = new StringBuffer(url + "FileService");
                Map<String, String> userParams = new HashMap<String, String>();
                userParams.put("UserName", syncUser.getUserName());
                userParams.put("UserId", String.valueOf(syncUser.getUserId()));
                Map<String, File> fileMap = new HashMap<String, File>();
//                将头像Uri实例化为File
                File file = new File(FileServices.getRealFilePath(context, syncUser.getUserAvatarUri()));
                fileMap.put(file.getName(), file);
                try {
//                    通过POST发送数据
                    postDataByPost(stringBuffer.toString(), userParams, fileMap, new PostCallBack() {
                        @Override
                        public void postFailed(Object object) {
//                            发送失败，提示错误
                            Snackbar.make(view, "头像上传失败！", Snackbar.LENGTH_SHORT).show();
                            Log.i("Test", object.toString());
                        }

                        @Override
                        public void postSuccess(Object object) {
//                            发送成功，提示消息，调用回调函数
                            Snackbar snackbar = Snackbar.make(view, "上传成功！", Snackbar.LENGTH_SHORT);
                            snackbar.show();
                            while (snackbar.isShown()) {
                            }
                            dataProcess.processComplete("");
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
        };
        asyncTask.execute();

    }

    /**
     * 上传用户信息
     *
     * @param context     上下文参数
     * @param view        调用的view
     * @param dataProcess 回调接口
     * @param syncUser    待同步的用户
     * @param infoJsonStr 用户信息JSON字符串
     * @param avatar      待上传头像
     */
    public void actionUploadUser(final Context context, final View view, final DataProcess dataProcess, final User syncUser, final String infoJsonStr, final Bitmap avatar) {
//        实例化进度条
        final ProgressDialog progressDialog = new ProgressDialog(context);
//        启动异步任务
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(final Object[] params) {
//                构造Url
                StringBuffer stringBuffer = new StringBuffer(url + "actionSync?");
                stringBuffer.append("UserName=" + paramsLegalize(syncUser.getUserName()));
                stringBuffer.append("&ID=" + paramsLegalize(String.valueOf(syncUser.getUserId())));
                stringBuffer.append("&Type=SyncUserInfo");
                stringBuffer.append("&UserInfo=" + paramsLegalize(infoJsonStr));
                String str = stringBuffer.toString();
                ;
                try {
                    URL mUrl = new URL(str);
//                    通过GET发送数据
                    postDataByGet(mUrl, new PostCallBack() {
                        @Override
                        public void postFailed(Object object) {
//                            发送失败，提示错误
                            Log.i("Test", "Failed-->" + object);
                            progressDialog.dismiss();
                            Snackbar.make(view, "上传失败，请检查网络！", Snackbar.LENGTH_LONG).show();
                        }

                        @Override
                        public void postSuccess(Object object) {
                            try {
//                                发送成功，处理服务器回传数据
                                progressDialog.dismiss();
                                JSONObject jsonObject = new JSONObject((String) object);
                                String msg = jsonObject.getString("msg");
                                String event = jsonObject.getString("event");
                                String obj = jsonObject.getString("info");
                                if (event.equals("1")) {
//                                    结果码为1，上传成功，判断是否需要上传头像
                                    if (syncUser.getUserAvatarUri() != null) {
                                        actionUploadAvatar(context, view, syncUser, avatar, dataProcess);
                                    } else {
//                                        无需上传头像，提示成功，调用回调函数
                                        Snackbar snackbar = Snackbar.make(view, "上传成功！", Snackbar.LENGTH_SHORT);
                                        snackbar.show();
                                        while (snackbar.isShown()) {
                                        }
                                        dataProcess.processComplete("");
                                    }
                                } else if (event.equals("0")) {
                                    Snackbar.make(view, "上传失败，无此用户！", Snackbar.LENGTH_LONG).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                配置进度条基本属性
                progressDialog.setMessage("正在上传……");
                progressDialog.setTitle("注意！");
                progressDialog.show();
            }
        };
        asyncTask.execute();
    }

    /**
     * 下载用户头像
     *
     * @param mAvatarUrl  用户头像Url
     * @param dataProcess 回调接口
     */
    public void actionGetAvatar(final String mAvatarUrl, final DataProcess dataProcess) {
//        启动异步任务
        final AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(final Object[] params) {
//                构造Url
                String mUrl = avatarUrl + mAvatarUrl;
                try {
//                    获取图片输入流并将其转换为Bitmap
                    Bitmap mBitmap = BitmapFactory.decodeStream(getImageStream(mUrl));
//                    调用回调函数将头像传出
                    dataProcess.processComplete(mBitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
        };
        asyncTask.execute();
    }

    /**
     * Get image from network
     *
     * @param path The path of image
     * @return InputStream
     * @throws Exception
     */
    public InputStream getImageStream(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5 * 1000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return conn.getInputStream();
        }
        return null;
    }

    /**
     * 同步锻炼数据和锻炼历史操作
     *
     * @param view        调用的view
     * @param context     上下文参数
     * @param dataProcess 回调接口
     * @param syncUser    待同步用户
     * @param syncData    待同步数据
     */
    public void actionSync(final View view, Context context, final DataProcess dataProcess, final User syncUser, final String syncData) {
//        实例化进度条
        final ProgressDialog progressDialog = new ProgressDialog(context);
//        启动异步任务
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(final Object[] params) {
//                构造Url
                StringBuffer stringBuffer = new StringBuffer(url + "actionSync?");
                stringBuffer.append("UserName=" + paramsLegalize(syncUser.getUserName()));
                stringBuffer.append("&ID=" + paramsLegalize(String.valueOf(syncUser.getUserId())));
                stringBuffer.append("&Type=SyncData");
                stringBuffer.append("&Data=" + paramsLegalize(syncData));
                String str = stringBuffer.toString();
                ;
                try {
                    URL mUrl = new URL(str);
//                    通过GET发送数据
                    postDataByGet(mUrl, new PostCallBack() {
                        @Override
                        public void postFailed(Object object) {
//                            发送失败，提示错误
                            progressDialog.dismiss();
                            Snackbar.make(view, "同步失败，请检查网络！", Snackbar.LENGTH_LONG).show();
                        }

                        @Override
                        public void postSuccess(Object object) {
                            try {
//                                发送成功，处理服务器回传数据
                                progressDialog.dismiss();
                                JSONObject jsonObject = new JSONObject((String) object);
                                String msg = jsonObject.getString("msg");
                                String event = jsonObject.getString("event");
                                String obj = jsonObject.getString("info");
                                if (event.equals("1")) {
//                                    结果码为1，调用回调函数
                                    dataProcess.processComplete(obj);
                                } else if (event.equals("0")) {
                                    Snackbar.make(view, "同步失败，无此用户！", Snackbar.LENGTH_LONG).show();
                                } else if (event.equals("2")) {
                                    Snackbar.make(view, "同步失败，未找到同步数据！", Snackbar.LENGTH_LONG).show();

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                配置进度条基本属性
                progressDialog.setMessage("正在同步……");
                progressDialog.setTitle("注意！");
                progressDialog.show();
            }
        };
        asyncTask.execute();
    }

    /**
     * POST方式发送数据
     *
     * @param actionUrl    发送地址
     * @param params       发送参数Map
     * @param files        发送文件Map
     * @param postCallBack 回调接口
     * @throws IOException 输入输出意外
     */
    private /*String*/void postDataByPost(String actionUrl, Map<String, String> params, Map<String, File> files, PostCallBack postCallBack) throws IOException {
        String BOUNDARY = java.util.UUID.randomUUID().toString();
        String PREFIX = "--", LINEND = "\r\n";
        String MULTIPART_FROM_DATA = "multipart/form-data";
        String CHARSET = "UTF-8";
        URL uri = new URL(actionUrl);
        HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
        conn.setReadTimeout(5 * 1000); // 缓存的最长时间
        conn.setDoInput(true);// 允许输入
        conn.setDoOutput(true);// 允许输出
        conn.setUseCaches(false); // 不允许使用缓存
        conn.setRequestMethod("POST");
        conn.setRequestProperty("connection", "keep-alive");
        conn.setRequestProperty("Charset", "UTF-8");
        conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);
// 首先组拼文本类型的参数
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINEND);
            sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINEND);
            sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
            sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
            sb.append(LINEND);
            sb.append(entry.getValue());
            sb.append(LINEND);
        }
        DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
        outStream.write(sb.toString().getBytes());
// 发送文件数据
        if (files != null)
            for (Map.Entry<String, File> file : files.entrySet()) {
                StringBuilder sb1 = new StringBuilder();
                sb1.append(PREFIX);
                sb1.append(BOUNDARY);
                sb1.append(LINEND);
                sb1.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getKey() + "\"" + LINEND);
                sb1.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINEND);
                sb1.append(LINEND);
                outStream.write(sb1.toString().getBytes());
                InputStream is = new FileInputStream(file.getValue());
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                }
                is.close();
                outStream.write(LINEND.getBytes());
            }
//请求结束标志
        byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
        outStream.write(end_data);
        outStream.flush();
// 得到响应码
        int res = conn.getResponseCode();
        InputStream in = null;
        if (res == 200) {
            in = conn.getInputStream();
            int ch;
            StringBuilder sb2 = new StringBuilder();
            while ((ch = in.read()) != -1) {
                sb2.append((char) ch);
            }
            postCallBack.postSuccess(sb2);
        } else {
            postCallBack.postFailed(res);
        }
        outStream.close();
        conn.disconnect();
//        return in.toString();
    }

    /**
     * GET方式发送数据
     *
     * @param mUrl         构造完成的Url
     * @param postCallBack 回调接口
     */
    private void postDataByGet(URL mUrl, PostCallBack postCallBack) {
        try {
            Log.i("Test", mUrl.toString());
//            实例化HTTP连接
            HttpURLConnection httpURLConnection = (HttpURLConnection) mUrl.openConnection();
            httpURLConnection.setConnectTimeout(3000);
            httpURLConnection.setReadTimeout(3000);
//            从服务器获得结果码
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                结果码为200，连接正常
                InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String resp = bufferedReader.readLine();
                if (resp == null || resp.equals("")) {
//                    若返回结果为空，调用发送失败回调函数
                    postCallBack.postFailed(true);
                } else {
                    Log.i("Test", resp);
//                    返回结果不为空，调用发送成功回调函数
                    postCallBack.postSuccess(resp);
                }
//                关闭流
                inputStreamReader.close();
                httpURLConnection.disconnect();
            } else {
                postCallBack.postFailed(httpURLConnection.getResponseCode());
            }
//        } catch (JSONException e) {
//            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            postCallBack.postFailed(false);
        }
    }


    /**
     * 编码参数保证URL合法化
     *
     * @param params 参数字符串
     * @return 合法化参数
     */
    private String paramsLegalize(String params) {
        try {
            return URLEncoder.encode(params, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return params;
    }
}
