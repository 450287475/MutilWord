package com.wangdao.mutilword.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.wangdao.mutilword.R;
import com.wangdao.mutilword.activity.MainActivity;
import com.wangdao.mutilword.application.ApplicationInfo;
import com.wangdao.mutilword.dao.RepeatWordDao;

import java.util.Date;

public class NotificationService extends Service {
    private static final String TAG = "NotificationService";

    public NotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Log.i(TAG,"onCreate");
        super.onCreate();
        final RepeatWordDao dao = new RepeatWordDao(this, "oldWord.db", 1);
        new Thread(){
            @Override
            public void run() {
                int i=0;
                while (true){
                    try {
                        sleep(6000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    long currentTimeMillis = System.currentTimeMillis();
                    Date date = new Date(currentTimeMillis);
                    int hours = date.getHours();
                    int day = date.getDay();
                    int notificationDay = ApplicationInfo.sp.getInt("notificationDay", -1);
                    if((hours>21)&&(day-notificationDay>1)&&dao.getOldWord().size()!=0){
                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        Notification notification = new Notification(R.drawable.icon, "测试title",currentTimeMillis );
                        notification.defaults=Notification.DEFAULT_ALL;
                        Intent intent = new Intent(NotificationService.this, MainActivity.class);
                        PendingIntent pendingIntent = PendingIntent.getActivity(NotificationService.this, 0, intent, 0);
                        notification.setLatestEventInfo(NotificationService.this,"测试展开title","测试展开内容",pendingIntent);
                        if(notification!=null){
                            Log.i(TAG,"notifacation is ok");
                            notificationManager.notify(1000+i,notification);
                            ApplicationInfo.editor.putInt("notificationDay",day).commit();
                            i++;
                        }
                    }

                }

            }
        }.start();

    }
}
