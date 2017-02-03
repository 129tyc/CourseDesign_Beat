package com.example.testapp2.app;

import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.drm.DrmStore;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Code on 2016/12/5 0005.
 * 个人中心页面
 */
public class PersonalCentre extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView avatar;
    private Button changeAvatarButton;
    private TextView userID;
    private TextView userName;
    private EditText userNickName;
    private RadioGroup sexGroup;
    private RadioButton sexMan;
    private RadioButton sexWoMan;
    private Button birthday;
    private User user;
    private Button save;
    private Button logOut;
    //    sdcard临时头像文件
    private File sdcardTempFile = new File("/mnt/sdcard/", "tmp_pic_" + SystemClock.currentThreadTimeMillis() + ".jpg");


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_centre);
//        获得数据资源
        final DataResourses dataResourses = new DataResourses(this, this);
//        实例化组件
        toolbar = (Toolbar) findViewById(R.id.personal_centre_toolbar);
        changeAvatarButton = (Button) findViewById(R.id.personal_centre_setImageButton);
        avatar = (CircleImageView) findViewById(R.id.personal_centre_avatar);
        userID = (TextView) findViewById(R.id.personal_centre_userId);
        userName = (TextView) findViewById(R.id.personal_centre_userName);
        userNickName = (EditText) findViewById(R.id.personal_centre_userNickName);
        sexGroup = (RadioGroup) findViewById(R.id.personal_centre_sexGroup);
        sexMan = (RadioButton) findViewById(R.id.personal_centre_sexMan);
        sexWoMan = (RadioButton) findViewById(R.id.personal_centre_sexWoman);
        birthday = (Button) findViewById(R.id.personal_centre_birthdayButton);
        save = (Button) findViewById(R.id.personal_centre_save);
        logOut = (Button) findViewById(R.id.personal_centre_logOut);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setTitle("个人中心");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (DataResourses.isNoUser() || DataResourses.getCurrentUser().isEmpty()) {
//            无用户，提示消息
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("注意");
            builder.setMessage("无用户，请先登陆！");
            builder.setPositiveButton("去登陆", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(PersonalCentre.this, LoginPage.class);
                    startActivity(intent);
                    finish();
                }
            });
            builder.setCancelable(false);
            builder.setNegativeButton("离开", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.show();
        } else {
//            读取并加载用户数据
            user = DataResourses.getCurrentUser();
            if (user.getUserAvatar() != null) {
                avatar.setImageBitmap(user.getUserAvatar());
            }
            userID.setText(String.valueOf(user.getUserId()));
            userName.setText(user.getUserName());
            userNickName.setText(user.getUserNickName());
            changeAvatarButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    更改头像，进入系统图片选择界面
                    Intent intent = new Intent();
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    intent.setAction(Intent.ACTION_PICK);
                    startActivityForResult(intent, 1);
//                    startPhotoZoom();
                }
            });
            Log.i("Test", user.getSexEnum().toString());
            switch (user.getSexEnum()) {
                case Man:
                    sexGroup.check(R.id.personal_centre_sexMan);
                    break;
                case Woman:
                    sexGroup.check(R.id.personal_centre_sexWoman);
                    break;
                case None:
                    sexGroup.clearCheck();
            }
            Calendar tmpCalendar;
//            配置显示的生日
            if (user.getBirthdayStr().equals("")) {
                birthday.setText("请设置");
                tmpCalendar = Calendar.getInstance();
            } else {
                birthday.setText(user.getBirthdayStr());
                tmpCalendar = user.getBirthDay();
            }
            final Calendar finalTmpCalendar = tmpCalendar;
            birthday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    设置监听器点击打开日期选择器并加载日期
                    final DatePickerDialog datePickerDialog = new DatePickerDialog(PersonalCentre.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                            当选择日期后，改变TextView显示
                            finalTmpCalendar.set(year, monthOfYear, dayOfMonth);
                            birthday.setText(user.getBirthdayStr(finalTmpCalendar));
                        }
                    }, finalTmpCalendar.get(Calendar.YEAR), finalTmpCalendar.get(Calendar.MONTH), finalTmpCalendar.get(Calendar.DATE));
                    datePickerDialog.show();
                }
            });
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    保存用户信息并上传
                    if (avatar.getDrawable() != null) {
                        Bitmap bitmap = BitmapFactory.decodeFile(FileServices.getRealFilePath(PersonalCentre.this, user.getUserAvatarUri()));
                        user.setUserAvatar(bitmap);
                    }
                    if (userNickName.getText().length() != 0) {
                        user.setUserNickName(userNickName.getText().toString());
                    } else {
                        user.setUserNickName("");
                    }
                    user.setBirthDay(finalTmpCalendar);
                    switch (sexGroup.getCheckedRadioButtonId()) {
                        case R.id.personal_centre_sexMan:
                            user.setSexEnum("Man");
                            break;
                        case R.id.personal_centre_sexWoman:
                            user.setSexEnum("Woman");
                            break;
                        default:
                            user.setSexEnum("None");
                            break;
                    }
//                    调用核心层方法上传用户
                    DataResourses.uploadUserInfo(PersonalCentre.this, save, new HttpUtil.DataProcess() {
                        @Override
                        public void processComplete(Object object) {
                            finish();
                        }
                    }, user.getUserAvatar());
                }
            });
            logOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    注销用户
                    AlertDialog.Builder builder = new AlertDialog.Builder(PersonalCentre.this);
                    builder.setTitle("注意！");
                    builder.setMessage("确定注销么？");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dataResourses.clearUserInfo();
                            DataResourses.clearCurrentUser();
//                            DataResourses.setIsNoUser(true);
//                            Log.i("Test", "setNOOOOOOO" + String.valueOf(DataResourses.isNoUser()));
                            Snackbar snackbar = Snackbar.make(logOut, "注销成功！", Snackbar.LENGTH_SHORT);
                            snackbar.show();
                            while (!snackbar.isShown()) {
                            }
                            finish();
                        }
                    });
                    builder.show();

                }
            });
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri 图片Uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
//裁剪框比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
//图片输出大小
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(sdcardTempFile));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//不启用人脸识别
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
//                选择图片返回
                if (data != null) {
//                    打开系统图片剪裁
                    startPhotoZoom(data.getData());
                }
                break;
            case 2:
//                图片剪裁返回
                user.setUserAvatarUri(Uri.fromFile(sdcardTempFile));
                Log.i("Test", user.getUserAvatarUri().toString());
//                if (data != null) {
//                    Log.i("Test", "ccccc");
//                    Bundle extras = data.getExtras();
//                    if (extras != null) {
//                        Log.i("Test", "ddddd");
//                        photo = extras.getParcelable("data");
//                        user.setUserAvatar(photo);
//                        avatar.setImageBitmap(photo);
//                    }
//                } else {
                Log.i("Test", "setAvatar");
                user.setUserAvatar(getBitmapFromUri(user.getUserAvatarUri()));
                avatar.setImageBitmap(user.getUserAvatar());
//                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 从Uri读取Bitmap头像
     *
     * @param uri 图片Uri
     * @return 对应的头像
     */
    private Bitmap getBitmapFromUri(Uri uri) {
        try {
            // 读取uri所在的图片
            return MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        } catch (Exception e) {
            Log.e("[Android]", e.getMessage());
            Log.e("[Android]", "目录为：" + uri);
            e.printStackTrace();
            return null;
        }
    }
}
