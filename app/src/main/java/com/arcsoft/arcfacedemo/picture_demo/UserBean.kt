package com.arcsoft.arcfacedemo.picture_demo

import cn.bmob.v3.BmobObject

class UserBean : BmobObject() {
    private var fileName : String? = null
    private var file : String? = null
    var isVerifyByCarOwner = false

    fun setFileName(fileName:String){
        this.fileName = fileName
    }
    fun setFile(file: String){
        this.file = file
    }
    fun getFileName():String?{
        return fileName
    }
    fun getFile():String?{
        return file
    }
}