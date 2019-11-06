package com.arcsoft.arcfacedemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arcsoft.arcfacedemo.R;
import com.arcsoft.arcfacedemo.Tools.SPTools;
import com.arcsoft.arcfacedemo.widget.IsFirstVarityDialog;

import static android.view.Window.FEATURE_NO_TITLE;

public class MainActivity extends AppCompatActivity implements IsFirstVarityDialog.OnDialogClickListener, View.OnClickListener {


    private static final String TAG = "MainActivity";
    public static final int REQUEST_CODE = 1;
    public static final int RESULT_OK_CODE = 2;
    public static final String isFirstRunCarKey = "is_first_run";
    private boolean isFirstRunCar = true;
    private TextView mTvTitle;
    private ImageView mIvReset;
    private ImageView mIvLockedOrUnLocked;
    private boolean isVerifySuccess = false;
    private boolean isActivityResult = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_main);
        isFirstRunCar = SPTools.getDataValueByKey(isFirstRunCarKey, this);
        if (isFirstRunCar) {
            showDialog();
        } else {
            Intent intent = new Intent(this, RegisterActivity.class);
            intent.putExtra("isVerify", true);
            startActivityForResult(intent, REQUEST_CODE);
        }
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isFirstRunCar = SPTools.getDataValueByKey(isFirstRunCarKey, this);
        Log.e(TAG,"是否有人脸信息:"+SPTools.getDataValueByKey(isFirstRunCarKey, this));
        if (!isActivityResult){
            if (isFirstRunCar) {
                mIvLockedOrUnLocked.setImageResource(R.mipmap.icon_face_none);
            } else {
                mIvLockedOrUnLocked.setImageResource(R.mipmap.icon_face_failed);
            }
        }
    }

    private void initViews() {
        mIvLockedOrUnLocked = findViewById(R.id.iv_locked_or_unlocked);
        mTvTitle = findViewById(R.id.tv_title);
        mTvTitle.setText(getString(R.string.main_title_text));
        findViewById(R.id.iv_back).setVisibility(View.GONE);
        mIvReset = findViewById(R.id.iv_change_camera);
        mIvReset.setImageResource(R.mipmap.reset_settings);
        mIvReset.setClickable(true);
        mIvReset.setOnClickListener(this);
        mIvLockedOrUnLocked.setOnClickListener(this);
    }

    /**
     * 如果是第一次启动车机，需要录入车主人脸信息
     */
    private void showDialog() {
        Log.e("MainActivity", "showDialog");
        IsFirstVarityDialog dialog = new IsFirstVarityDialog(this);
        dialog.showDialog(this, "温馨提示", "请录入人脸信息解锁您的爱驾,否则您将无法驾驶该设备。",
                getString(R.string.dialog_confirm_text), getString(R.string.dialog_cancel_text));
    }

    @Override
    public void onConfirm() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onCancel() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            Log.e("MainActivity", "onActivityResult");
            if (resultCode == REQUEST_CODE) {
                Log.e("MainActivity", "注册成功");
                SPTools.saveToDataBase(isFirstRunCarKey, false, this);
                mIvLockedOrUnLocked.setImageResource(R.mipmap.icon_face_success);
                isActivityResult = true;
                isVerifySuccess = true;
            } else if (resultCode == RESULT_OK_CODE) {
                Log.e("MainActivity", "识别成功");
                isActivityResult = true;
                mIvLockedOrUnLocked.setImageResource(R.mipmap.icon_face_success);
                isVerifySuccess = true;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_change_camera:
                isActivityResult = false;
                startActivity(new Intent(this, SettingActivity.class));
                break;
            case R.id.iv_locked_or_unlocked:
                if (!isVerifySuccess){
                    if (isFirstRunCar){
                        Intent intent = new Intent(this, RegisterActivity.class);
                        startActivityForResult(intent, REQUEST_CODE);
                    }else {
                        isActivityResult = false;
                        Intent intent = new Intent(this, RegisterActivity.class);
                        intent.putExtra("isVerify", true);
                        startActivityForResult(intent, REQUEST_CODE);
                    }
                }
                break;
            default:
                break;
        }
    }
}
