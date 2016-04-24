package com.wangdao.mutilword.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by MonkeyzZi on 2016/4/22.
 */
public class PrefUtils {
    public static final String PREF_NAME= "config";

    public static boolean getBoolean(Context context,String key, boolean defaultValue){
        //判断之前有没有显示过新手引导
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, context.MODE_PRIVATE);

        return sp.getBoolean(key,defaultValue);
    }
    public static void setBoolean(Context context,String key, boolean value){
        //判断之前有没有显示过新手引导
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, context.MODE_PRIVATE);

        sp.edit().putBoolean(key,value).commit();
    }
}
