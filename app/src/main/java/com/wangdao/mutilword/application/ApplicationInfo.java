package com.wangdao.mutilword.application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.wangdao.mutilword.bean.UserInfo;
import com.wangdao.mutilword.service.NotificationService;

/**
 * Created by haijun on 2016/4/19.
 */
public class ApplicationInfo extends Application{
    public static SharedPreferences sp;
    public static SharedPreferences.Editor editor;
    public static Context mContext;
    public static UserInfo userInfo;
    public static String nickname;



    @Override
    public void onCreate() {
        super.onCreate();
        sp = getSharedPreferences("userinfo", MODE_PRIVATE);
        editor = sp.edit();
        mContext = this;
        startService(new Intent(this,NotificationService.class));
    }

    //初始化用户数据
    public static void initUserInfo(String objectId,String userid, String username, String password, String usericon, String phone,String autograph,
                                    int collectedArticle, int collectedWord, int exchangeAwarded, int exchangeAward, int articleCount,
                                    int wordCount, int userrank, int userpoints){
        userInfo = new UserInfo(objectId,userid,username, password, usericon, phone, autograph,
                collectedArticle,collectedWord,exchangeAwarded,exchangeAward,articleCount,wordCount,
                userrank, userpoints);

    }

    @Override
    public void onTerminate() {
        super.onTerminate();

    }

    public static void initNickname(String name){
        nickname = name;
    }

}
