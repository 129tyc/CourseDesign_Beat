package com.example.testapp2.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import java.util.List;

/**
 * Created by Code on 2016/8/14 0014.
 * 新建鼓点对话框
 */
public class NewDialog extends Activity {
    private RadioGroup radioGroup;
    private Button positive;
    //    private TableLayout beatSecondsTable;
//    private TableLayout beatNumbersTable;
    private KeepData keepData;
    private TextInputLayout beatName_TextInput;
    private EditText beatName;
    private TextInputLayout beatNumbers_TextInput;
    private EditText beatNumbers;
    private TextInputLayout beatTime_TextInput;
    private EditText beatTime;
    private TextInputLayout beatSeconds_TextInput;
    private EditText beatSeconds;
    //    private static final int INTEGER = 1;
//    private static final int FLOAT = 2;
//    public static final String newDialogIntentIndicateCode = "TypeCode";
    private int tabPosition;
    private final int DEFULAT_POSITION = 0;
    private final int PREPARE = 0;
    private final int KEEP = 1;
    private final int STRETCH = 2;
    private BeatData editBeatData = null;
    private List<BeatData> currentBeatDatas;
    private int dataPosition;
    private int keepDataType;
    private TextView.OnEditorActionListener onEditorActionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newitemdialog);
        //获得共享编辑对象
        keepData = DataResourses.getEditableKeepData();
        //实例化UI
        positive = (Button) findViewById(R.id.dialogFinish);
//        beatNumbersTable = (TableLayout) findViewById(R.id.newitemDialogBeatNumbersTable);
//        beatSecondsTable = (TableLayout) findViewById(R.id.newitemDialogBeatSecondsTable);
//        beatSecondsTable.setVisibility(View.GONE);

        radioGroup = (RadioGroup) findViewById(R.id.newitemDialogRadioGroup);
        beatName_TextInput = (TextInputLayout) findViewById(R.id.newitemDialogBeatName);
        beatNumbers_TextInput = (TextInputLayout) findViewById(R.id.newitemDialogBeatNumbers);
        beatSeconds_TextInput = (TextInputLayout) findViewById(R.id.newitemDialogBeatSeconds);
        beatTime_TextInput = (TextInputLayout) findViewById(R.id.newitemDialogBeatTime);
        beatName = beatName_TextInput.getEditText();
        beatNumbers = beatNumbers_TextInput.getEditText();
        beatSeconds = beatSeconds_TextInput.getEditText();
        beatTime = beatTime_TextInput.getEditText();
        beatSeconds_TextInput.setVisibility(View.GONE);
//        设置监听器，按回车保存
        onEditorActionListener = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
//                    隐藏输入法
                    View view = getWindow().peekDecorView();
                    if (view != null) {
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
//                    点击保存按钮
                    positive.performClick();
                    return true;
                }
                return false;
            }
        };
//        配置监听器
        beatTime.setOnEditorActionListener(onEditorActionListener);
        beatSeconds.setOnEditorActionListener(onEditorActionListener);
        //设定单选组单选监听器切换输入类型 鼓点时间 或 鼓点次数
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.newitemDialogRadioButtonBeatNumbers) {
                    beatNumbers_TextInput.setVisibility(View.VISIBLE);
                    beatTime_TextInput.setVisibility(View.VISIBLE);
                    beatSeconds_TextInput.setVisibility(View.GONE);
                } else {
                    beatNumbers_TextInput.setVisibility(View.GONE);
                    beatTime_TextInput.setVisibility(View.GONE);
                    beatSeconds_TextInput.setVisibility(View.VISIBLE);
                }
            }
        });
        tabPosition = getIntent().getIntExtra(ItemEdit.CURRENTTAB, DEFULAT_POSITION);
        Log.i("Adapter", String.valueOf(tabPosition));
        //根据识别码检测是否展示编辑对象
        keepDataType = getIntent().getIntExtra("EditPage", 3);
        dataPosition = getIntent().getIntExtra("Position", 0);
        if (keepDataType != 3) {
            switch (keepDataType) {
                case 0:
                    currentBeatDatas = DataResourses.getEditableKeepData().getPrepareDataList();
                    break;
                case 1:
                    currentBeatDatas = DataResourses.getEditableKeepData().getKeepDataList();
                    break;
                case 2:
                    currentBeatDatas = DataResourses.getEditableKeepData().getKeepDataList();
                    break;
            }
            editBeatData = currentBeatDatas.get(dataPosition);
            if (editBeatData.getBeatName() != null) {
                beatName.setText(editBeatData.getBeatName());
                //根据传入数据改变单选框选项
                if (editBeatData.getBeatNumbers() == BeatData.INVALIDCODE) {
                    radioGroup.check(R.id.newitemDialogRadioButtonBeatSeconds);
                    beatSeconds.setText(String.valueOf(editBeatData.getBeatSeconds()));
                } else {
                    radioGroup.check(R.id.newitemDialogRadioButtonBeatNumbers);
                    beatTime.setText(String.valueOf(editBeatData.getBeatTime()));
                    beatNumbers.setText(String.valueOf(editBeatData.getBeatNumbers()));
                }
            }
        }
//        switch (getIntent().getStringExtra(NewDialog.newDialogIntentIndicateCode)) {
//            case NewItemPrepare.TYPECODE:
//                if (NewItemPrepare.beatData != null) {
//                    beatName.setText(NewItemPrepare.beatData.getBeatName());
//                    //根据传入数据改变单选框选项
//                    if (NewItemPrepare.beatData.getBeatNumbers() == BeatData.INVALIDCODE) {
//                        radioGroup.check(R.id.newitemDialogRadioButtonBeatSeconds);
//                        beatSeconds.setText(String.valueOf(NewItemPrepare.beatData.getBeatSeconds()));
//                    } else {
//                        radioGroup.check(R.id.newitemDialogRadioButtonBeatNumbers);
//                        beatTime.setText(String.valueOf(NewItemPrepare.beatData.getBeatTime()));
//                        beatNumbers.setText(String.valueOf(NewItemPrepare.beatData.getBeatNumbers()));
//                    }
//                }
//                break;
//            case NewItemKeep.TYPECODE:
//                if (NewItemKeep.beatData != null) {
//                    beatName.setText(NewItemKeep.beatData.getBeatName());
//                    //根据传入数据改变单选框选项
//                    if (NewItemKeep.beatData.getBeatNumbers() == BeatData.INVALIDCODE) {
//                        radioGroup.check(R.id.newitemDialogRadioButtonBeatSeconds);
//                        beatSeconds.setText(String.valueOf(NewItemKeep.beatData.getBeatSeconds()));
//                    } else {
//                        radioGroup.check(R.id.newitemDialogRadioButtonBeatNumbers);
//                        beatTime.setText(String.valueOf(NewItemKeep.beatData.getBeatTime()));
//                        beatNumbers.setText(String.valueOf(NewItemKeep.beatData.getBeatNumbers()));
//                    }
//                }
//                break;
//            case NewItemStretch.TYPECODE:
//                if (NewItemStretch.beatData != null) {
//                    beatName.setText(NewItemStretch.beatData.getBeatName());
//                    //根据传入数据改变单选框选项
//                    if (NewItemStretch.beatData.getBeatNumbers() == BeatData.INVALIDCODE) {
//                        radioGroup.check(R.id.newitemDialogRadioButtonBeatSeconds);
//                        beatSeconds.setText(String.valueOf(NewItemStretch.beatData.getBeatSeconds()));
//                    } else {
//                        radioGroup.check(R.id.newitemDialogRadioButtonBeatNumbers);
//                        beatTime.setText(String.valueOf(NewItemStretch.beatData.getBeatTime()));
//                        beatNumbers.setText(String.valueOf(NewItemStretch.beatData.getBeatNumbers()));
//                    }
//                }
//                break;
//        }
        //设定单击监听器检测完成按钮点击
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (beatName.getText().toString().equals("")) {
                    //若鼓点名称为空，则弹出消息
                    Toast toast = Toast.makeText(NewDialog.this, "鼓点名称不能为空", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    BeatData beatData;
                    //判定被确认的单选框，实例化不同类型的鼓点数据
                    try {
                        if (radioGroup.getCheckedRadioButtonId() == R.id.newitemDialogRadioButtonBeatNumbers) {
                            beatData = new BeatData(beatName.getText().toString(), Integer.parseInt(beatNumbers.getText().toString()), Double.parseDouble(beatTime.getText().toString()));
                        } else {
                            beatData = new BeatData(beatName.getText().toString(), Integer.parseInt(beatSeconds.getText().toString()));
                        }

                        //编辑情况下修改表中数据
                        if (editBeatData != null) {
                            currentBeatDatas.set(dataPosition, beatData);
                        } else {
//                        int position = getIntent().getIntExtra("position", -1);
//                        if (position != -1) {
//                            switch (getIntent().getStringExtra(NewDialog.newDialogIntentIndicateCode)) {
//                                case NewItemPrepare.TYPECODE:
//                                    keepData.getPrepareDataList().set(position, beatData);
//                                    break;
//                                case NewItemKeep.TYPECODE:
//                                    keepData.getKeepDataList().set(position, beatData);
//                                    break;
//                                case NewItemStretch.TYPECODE:
//                                    keepData.getStretchDataList().set(position, beatData);
//                                    break;
//                            }
//
//                        } else {
//                            //从意图获得activity识别码，根据识别码添加数据到不同数据表
//                            switch (getIntent().getStringExtra(NewDialog.newDialogIntentIndicateCode)) {
//                                case NewItemPrepare.TYPECODE:
//                                    keepData.getPrepareDataList().add(beatData);
//                                    break;
//                                case NewItemKeep.TYPECODE:
//                                    keepData.getKeepDataList().add(beatData);
//                                    break;
//                                case NewItemStretch.TYPECODE:
//                                    keepData.getStretchDataList().add(beatData);
//                                    break;
//                            }
//                        }
                            Log.i("Adapter", beatData.toString());
                            switch (tabPosition) {
//                                根据tab位置选择BeatData表加入数据
                                case PREPARE:
                                    keepData.getPrepareDataList().add(beatData);
                                    break;
                                case KEEP:
                                    keepData.getKeepDataList().add(beatData);
                                    break;
                                case STRETCH:
                                    keepData.getStretchDataList().add(beatData);
                                    break;
                            }
                        }
                        editBeatData = null;
                        finish();
                    } catch (NumberFormatException e) {
                        Toast toast = Toast.makeText(NewDialog.this, "输入错误！", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }

            }
        });
    }
}
