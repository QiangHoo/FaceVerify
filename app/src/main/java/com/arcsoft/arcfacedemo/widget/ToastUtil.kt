package com.arcsoft.arcfacedemo.widget

import android.content.Context
import android.widget.Toast

class ToastUtil {
    companion object{
        private  var mInstance : Toast? = null
        fun getToastInstance(context:Context,content:String):Toast?{
            if (mInstance == null) {
                mInstance = Toast.makeText(context,content,Toast.LENGTH_SHORT)
            }else{
                mInstance?.setText(content)
            }
            return mInstance
        }
    }

}