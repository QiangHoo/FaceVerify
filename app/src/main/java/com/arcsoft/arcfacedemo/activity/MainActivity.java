package com.arcsoft.arcfacedemo.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arcsoft.arcfacedemo.R;
import com.arcsoft.arcfacedemo.Tools.SPTools;
import com.arcsoft.arcfacedemo.common.Constants;
import com.arcsoft.arcfacedemo.widget.IsFirstVarityDialog;

import com.arcsoft.arcfacedemo.widget.ToastUtil;
import com.arcsoft.face.ActiveFileInfo;
import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.FaceEngine;


import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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
    private Toast toast;
    private static final int ACTION_REQUEST_PERMISSIONS = 0x001;
    private FaceEngine faceEngine = new FaceEngine();
    private static final String[] NEEDED_PERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_STATE
    };


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
        activeEngine();
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
        //finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            Log.e("MainActivity", "onActivityResult");
            if (resultCode == REQUEST_CODE) {
                Log.e("MainActivity", "注册成功");
                Objects.requireNonNull(ToastUtil.Companion.getToastInstance(this, getString(R.string.register_success))).show();
                SPTools.saveToDataBase(isFirstRunCarKey, false, this);
                mIvLockedOrUnLocked.setImageResource(R.mipmap.icon_face_success);
                isActivityResult = true;
                isVerifySuccess = true;
                Intent intent = new Intent(this,VechileActivity.class);
                startActivity(intent);
            } else if (resultCode == RESULT_OK_CODE) {
                Objects.requireNonNull(ToastUtil.Companion.getToastInstance(this, getString(R.string.verify_success))).show();
                Log.e("MainActivity", "识别成功");
                isActivityResult = true;
                mIvLockedOrUnLocked.setImageResource(R.mipmap.icon_face_success);
                isVerifySuccess = true;
                Intent intent = new Intent(this,VechileActivity.class);
                startActivity(intent);
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

    /**
     * 激活引擎
     *
     * @param
     */
    public void activeEngine() {
        if (!checkPermissions(NEEDED_PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, NEEDED_PERMISSIONS, ACTION_REQUEST_PERMISSIONS);
            return;
        }
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                int activeCode = faceEngine.activeOnline(MainActivity.this, Constants.APP_ID,
                        Constants.SDK_KEY);
                emitter.onNext(activeCode);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer activeCode) {
                        if (activeCode == ErrorInfo.MOK) {
                            showToast(getString(R.string.active_success));
                        } else if (activeCode == ErrorInfo.MERR_ASF_ALREADY_ACTIVATED) {
                            showToast(getString(R.string.already_activated));
                        } else {
                            showToast(getString(R.string.active_failed, activeCode));
                        }

                        ActiveFileInfo activeFileInfo = new ActiveFileInfo();
                        int res = faceEngine.getActiveFileInfo(MainActivity.this,activeFileInfo);
                        if (res == ErrorInfo.MOK) {
                            Log.i(TAG, activeFileInfo.toString());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
    private boolean checkPermissions(String[] neededPermissions) {
        if (neededPermissions == null || neededPermissions.length == 0) {
            return true;
        }
        boolean allGranted = true;
        for (String neededPermission : neededPermissions) {
            allGranted &= ContextCompat.checkSelfPermission(this, neededPermission) == PackageManager.PERMISSION_GRANTED;
        }
        return allGranted;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ACTION_REQUEST_PERMISSIONS) {
            boolean isAllGranted = true;
            for (int grantResult : grantResults) {
                isAllGranted &= (grantResult == PackageManager.PERMISSION_GRANTED);
            }
            if (isAllGranted) {
                activeEngine();
            } else {
                showToast(getString(R.string.permission_denied));
            }
        }
    }

    private void showToast(String s) {
        Objects.requireNonNull(ToastUtil.Companion.getToastInstance(this, s)).show();
    }
}
