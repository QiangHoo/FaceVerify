package com.arcsoft.arcfacedemo.picture_demo


import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import cn.bmob.v3.Bmob
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.QueryListener
import cn.bmob.v3.listener.SaveListener
import cn.bmob.v3.listener.UpdateListener
import com.arcsoft.arcfacedemo.Tools.SPTools
import kotlinx.android.synthetic.main.activity_demo.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class DemoActivity : AppCompatActivity() {
    private var excutor = Executors.newScheduledThreadPool(1)

    private var objectId: String? = null
    private val OBJECTID = "object_id"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.arcsoft.arcfacedemo.R.layout.activity_demo)
        initial()
    }
    fun initial(){
        Bmob.initialize(this, "336e34ccf51ce4b0ae083eb2f6edd0f3")
        objectId = SPTools.getObjcetId(OBJECTID, this)
    }

    fun onClick(view: View) {
        val intent = Intent(Intent.ACTION_PICK, null)
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        startActivityForResult(intent, 2)
    }

    fun saveInfoToServer(base64: String) {
        val userBean = UserBean()
        userBean.setFileName("FacePic")
        userBean.setFile(base64)
        if (objectId != null) {
            userBean.update(objectId,object : UpdateListener() {
                override fun done(p0: BmobException?) {
                    if (p0 == null) {
                        Log.e("DemoActivity", "更新成功")
                        iv_content.setImageBitmap(FileTruePath.base64ToBitmap(base64))
                    } else {
                        Log.e("DemoActivity", "更新失败")
                    }
                }

            })
        } else {
            userBean.save(object : SaveListener<String>() {
                override fun done(objectId: String, e: BmobException?) {
                    if (e == null) {
                        this@DemoActivity.objectId = objectId
                        Log.e("DemoActivity", "上传成功${objectId}")
                        SPTools.saveToDataBase(OBJECTID, objectId, this@DemoActivity)
                    } else {
                        Log.e("DemoActivity", "上传失败")
                    }
                }
            })
        }
    }

    fun waitClientSession() {
        excutor.scheduleAtFixedRate({
            val bmobQuery = BmobQuery<UserBean>()
            bmobQuery.getObject(objectId, object : QueryListener<UserBean>() {

                override fun done(obj: UserBean, e: BmobException?) {
                    if (e == null) {
                        if (obj.isVerifyByCarOwner){
                            //解锁car
                        }
                    } else {

                    }
                }
            })


        }, 0, 1000, TimeUnit.MILLISECONDS)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                try {

                    val uri: Uri? = data.data
                    val filePath = FileTruePath.getRealPathFromUri(this, uri)
                    val bmp = BitmapFactory.decodeFile(filePath)
                    val base64Str = FileTruePath.bitmapToString(bmp)
                    saveInfoToServer(base64Str)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    }
}
