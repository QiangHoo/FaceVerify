package com.arcsoft.arcfacedemo.Tools;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences 工具类
 */
public class SPTools {
    private static final String spName = "car_port_face_verity";
    public static final String defaultValue = "";

    private SPTools() {

    }

    /**
     * 保存数据到SharedPreferences
     * @param key 存取数据的->键
     * @param value 存取数据的->值
     * @param context 应用上下文对象
     */
    public static void saveToDataBase(String key, boolean value, Context context) {
        if (!(key != null && context != null)) {
            return;
        }
        SharedPreferences sp = context.getApplicationContext()
                .getSharedPreferences(spName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * 通过『键』从SharedPreferences获取『值』
     * @param key 存取的->键
     * @param context 应用上下文对象
     * @return 获取到的值
     */
    public static boolean getDataValueByKey(String key, Context context) {
        SharedPreferences sp = context.getApplicationContext()
                .getSharedPreferences(spName, Context.MODE_PRIVATE);
        return sp.getBoolean(key, true);
    }
}
