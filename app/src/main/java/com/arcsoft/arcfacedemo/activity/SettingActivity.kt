package com.arcsoft.arcfacedemo.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View

import com.arcsoft.arcfacedemo.R

import android.view.Window.FEATURE_NO_TITLE
import com.arcsoft.arcfacedemo.Tools.SPTools
import com.arcsoft.arcfacedemo.activity.MainActivity.REQUEST_CODE
import com.arcsoft.arcfacedemo.activity.MainActivity.isFirstRunCarKey
import com.arcsoft.arcfacedemo.faceserver.FaceServer
import com.arcsoft.arcfacedemo.widget.IsFirstVarityDialog
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.include_title_bar.*

class SettingActivity : AppCompatActivity(),IsFirstVarityDialog.OnDialogClickListener {
    override fun onConfirm() {
        val faceNum = FaceServer.getInstance().getFaceNumber(this)
        if (faceNum != 0){
            val deleteCount = FaceServer.getInstance().clearAllFaces(this)
            SPTools.saveToDataBase(isFirstRunCarKey,true,this)
            Log.e("SettingActivity","删除数据：${deleteCount}")
        }
        startActivityForResult(Intent(this,RegisterActivity::class.java),REQUEST_CODE)
    }

    override fun onCancel() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(FEATURE_NO_TITLE)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        initViews()
    }

    private fun initViews() {
        tv_title.text = getString(R.string.setting_title)
        iv_change_camera.visibility = View.GONE
        iv_back.setOnClickListener{
            finish()
        }
        rl_reset_face.setOnClickListener{
            val dialog = IsFirstVarityDialog(this)
            dialog.showDialog(this,"温馨提示","是否重置人脸数据，并重新添加人脸信息？",
                    getString(R.string.dialog_confirm_text),getString(R.string.dialog_cancel_text))
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            Log.e("SettingActivity", "onActivityResult")
            if (resultCode == REQUEST_CODE) {
                Log.e("MainActivity", "注册成功")
                SPTools.saveToDataBase(isFirstRunCarKey, false, this)
            }
        }
    }
}
