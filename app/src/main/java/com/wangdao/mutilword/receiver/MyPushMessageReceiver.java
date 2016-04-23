package com.wangdao.mutilword.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.wangdao.mutilword.R;
import com.wangdao.mutilword.activity.ShowArticleDetailActivity;
import com.wangdao.mutilword.bean.PushInfo;

import cn.bmob.push.PushConstants;
import cn.bmob.v3.helper.NotificationCompat;

/**
 * Created by haijun on 2016/4/23.
 */
public class MyPushMessageReceiver extends BroadcastReceiver {
    private static final String TAG = "MyPushMessageReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        String message = intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING);


        /*推送的数据格式，json格式，alert为标题，articleurl为传递为要显示的网页内容
        {
            "alert": "xxxx",
            "articleurl": "http://xxx"
        }*/


        Gson gson = new Gson();
        PushInfo pushInfo = gson.fromJson(message, PushInfo.class);

        String alert = pushInfo.getAlert();
        String articleurl = pushInfo.getArticleurl();

        // 发送通知
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = new Notification();
        notification.icon = R.drawable.logo;
        notification.tickerText = "美文:"+alert;
        notification.defaults = Notification.DEFAULT_SOUND;

        notification.flags |= Notification.FLAG_AUTO_CANCEL;       //设置notification点击取消

        Intent intent1 = new Intent(context, ShowArticleDetailActivity.class);

        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent1.putExtra("articleurl",articleurl);

        //FLAG_UPDATE_CURRENT  更新当前的通知信息
        PendingIntent activity = PendingIntent.getActivity(context, 130, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

        notification.setLatestEventInfo(context,"美文",alert,activity);
        nm.notify(0, notification);
    }
}
