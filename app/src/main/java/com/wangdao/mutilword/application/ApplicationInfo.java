package com.wangdao.mutilword.application;

import android.app.Application;

import com.wangdao.mutilword.bean.UserInfo;

import cn.bmob.v3.Bmob;

/**
 * Created by haijun on 2016/4/19.
 */
public class ApplicationInfo extends Application{
    private static UserInfo userInfo;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    //初始化用户数据
    public static void initUserInfo(String userid, String username, String password, String usericon, String phone, int userrank, int userpoints){
        userInfo = new UserInfo(userid,username, password, usericon, phone, userrank, userpoints);
    }
}
