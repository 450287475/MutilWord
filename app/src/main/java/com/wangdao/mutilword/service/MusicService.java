package com.wangdao.mutilword.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.wangdao.mutilword.R;
import com.wangdao.mutilword.activity.MusicActivity;
import com.wangdao.mutilword.bean.LrcBean;
import com.wangdao.mutilword.bean.MusicJsonBean;
import com.wangdao.mutilword.constant.IConstants;
import com.wangdao.mutilword.utils.LocalCacheUtil;
import com.wangdao.mutilword.utils.LocalDirUtil;
import com.wangdao.mutilword.utils.Md5Utils;
import com.wangdao.mutilword.utils.NetWorkUtil;
import com.wangdao.mutilword.utils.ParseLyric;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;


public class MusicService extends Service implements IConstants
{
    private final static String PAUSE_BROADCAST = "com.study.gourdboy.pause.broadcast";
    private final static String PRE_BROADCAST = "com.study.gourdboy.pre.broadcast";
    private final static String NEXT_BROADCAST = "com.study.gourdboy.next.broadcast";
    private final static int PAUSE_FLAG = 0x1;
    private final static int PRE_FLAG = 0x2;
    private final static int NEXT_FLAG = 0x3;
    private final static int NOTIFICATION_ID = 0x1;
    private Handler handlerUI;
    private NotificationManager mNotificationManager;
    private MusicController mc;
    private ControlBroadcast mControlBroadcast;
    private List<MusicJsonBean.MusicBeanContent> musicContentList;
    private List<LrcBean> lrcBeanList;
    @Override
    public void onCreate()
    {
        super.onCreate();
        lrcBeanList = new ArrayList<>();
        mc = new MusicController(this);
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mControlBroadcast = new ControlBroadcast();
        IntentFilter filter = new IntentFilter();
        filter.addAction(PAUSE_BROADCAST);
        filter.addAction(NEXT_BROADCAST);
        filter.addAction(PRE_BROADCAST);
        registerReceiver(mControlBroadcast,filter);
        String url = SERVER_ADDR+JSON_ADDR;
        new HttpUtils().send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                Gson gson = new Gson();
                MusicJsonBean musicJsonBean = gson.fromJson(responseInfo.result, MusicJsonBean.class);
                musicContentList = musicJsonBean.data;
                Message msg = handlerUI.obtainMessage();
                msg.what = DOWNLOAD_MJSON_COMPLETE;
                msg.obj = musicContentList;
                handlerUI.sendMessage(msg);
            }
            @Override
            public void onFailure(HttpException e, String s)
            {
                Toast.makeText(MusicService.this, "连接服务器失败，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void downLoadLrc(String url)
    {
        File file = new File(getCacheDir(),Md5Utils.getMd5Message(url));
        if(file.exists())
        {
            try
            {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                LrcBean lrcBean = (LrcBean) ois.readObject();
//                Log.i("downLoadLrc",lrcBean.toString());
                lrcBeanList.add(lrcBean);
                Message msg = handlerUI.obtainMessage();
                msg.what = DOWNLOAD_LRC_COMPLETE;
                msg.obj = lrcBeanList;
                handlerUI.sendMessage(msg);
            }
            catch (FileNotFoundException e)
            {
                downloadLrc(url);
                e.printStackTrace();
            }
            catch (StreamCorruptedException e)
            {
                downloadLrc(url);
                e.printStackTrace();
            }
            catch (IOException e)
            {
                downloadLrc(url);
                e.printStackTrace();
            }
            catch (ClassNotFoundException e)
            {
                downloadLrc(url);
                e.printStackTrace();
            }
        }
        else
        {
            downloadLrc(url);
        }
    }
    private Bitmap getImage(String url)
    {
        if(url!=null&&!url.isEmpty())
        {
            url = SERVER_ADDR+url;
            Log.i("getImage",url);
            Bitmap bitmap = LocalCacheUtil.getImgFromCache(url);
            if (bitmap == null)
            {
                Log.i("getImageee", "CACHE IS NULL");
                bitmap = LocalDirUtil.getImgFromDir(url, getCacheDir());
            }
            if (bitmap == null)
            {
                Log.i("getImageee", "CACHE IS NULL");
                NetWorkUtil.getImageFromNet(url, handlerUI, getCacheDir());
            }
            return bitmap;
        }
        return null;
    }
    private void downloadLrc(String url)
    {
        final String url1 = url;
        new HttpUtils().send(HttpRequest.HttpMethod.GET, SERVER_ADDR+url, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String str = responseInfo.result;
                LrcBean lrcBean = ParseLyric.transformString(str,url1);
//                Log.i("downLoadLrc",lrcBean.toString());
                saveLrc(lrcBean,url1);
                lrcBeanList.add(lrcBean);
                Message msg = handlerUI.obtainMessage();
                msg.what = DOWNLOAD_LRC_COMPLETE;
                msg.obj = lrcBeanList;
                handlerUI.sendMessage(msg);
            }
            @Override
            public void onFailure(HttpException e, String s)
            {
            }
        });
    }
    private void saveLrc(LrcBean lrcBean,String url)
    {
        File file = new File(getCacheDir(), Md5Utils.getMd5Message(url));
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try
        {
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(lrcBean);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(fos!=null)
            {
                try
                {
                    fos.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if(oos!=null)
            {
                try
                {
                    oos.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    public class MusicBinder extends Binder
    {
        public void updateNotification(Bitmap bitmap, String title, String singer)
        {
            MusicService.this.updateNotification(bitmap,title,singer);
        }
        public boolean play(int index)
        {
            return mc.playById(index);
        }
        public boolean pause()
        {
            return mc.pause();
        }
        public void setHandler(Handler handler)
        {
            MusicService.this.handlerUI = handler;
            mc.setMusicHandler(handler);
        }
        public void exit()
        {
            MusicService.this.cancelNotification();
            stopSelf();
            //播放器退出
        }
        public void downLoadLRC(String url)
        {
            MusicService.this.downLoadLrc(url);
        }
        public Bitmap getImage(String url)
        {
            return MusicService.this.getImage(url);
        }
        public int getDuration()
        {
            return mc.getDuration();
        }
        public int getPosition()
        {
            return mc.getPosition();
        }
        public boolean seekToPosition(int millisecond)
        {
            return mc.seekToPosition(millisecond);
        }
        public boolean preparePlayer(int index)
        {
            return mc.preparePlayer(index);
        }
        public void setMusicURL(List<MusicJsonBean.MusicBeanContent> musicList)
        {
            mc.setMusicURL(musicList);
        }
    }
    private void updateNotification(Bitmap bitmap, String title, String singer)
    {
        Intent intent = new Intent(getApplicationContext(), MusicActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(),0,intent, PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteViews rv = new RemoteViews(getPackageName(), R.layout.music_notification);
        if(bitmap!=null)
        {
            rv.setImageViewBitmap(R.id.notification_image,bitmap);
        }
        else
        {
            rv.setImageViewResource(R.id.notification_image, R.drawable.img_album_background);
        }
        Log.i("getPlayState",mc.getPlayState()+" ");
        if(mc.getPlayState()==PLAYER_PLAYING)
        {
            rv.setImageViewResource(R.id.notification_image_player,R.drawable.nc_pause_normal);
        }
        else
        {
            rv.setImageViewResource(R.id.notification_image_player,R.drawable.nc_play_normal);
        }
        rv.setTextViewText(R.id.notification_title,title);
        rv.setTextViewText(R.id.notification_singer,singer);
        //设置按钮,action不能是一样的 如果一样的 接受的flag参数只是第一个设置的值
        Intent pauseIntent = new Intent(PAUSE_BROADCAST);
        pauseIntent.putExtra("FLAG",PAUSE_FLAG);
        PendingIntent pausePIntent = PendingIntent.getBroadcast(this,0,pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        rv.setOnClickPendingIntent(R.id.notification_image_player,pausePIntent);

        Intent nextIntent = new Intent(NEXT_BROADCAST);
        nextIntent.putExtra("FLAG",NEXT_FLAG);
        PendingIntent nextPIntent = PendingIntent.getBroadcast(this,0,nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        rv.setOnClickPendingIntent(R.id.notification_image_next,nextPIntent);

        Intent preIntent = new Intent(PRE_BROADCAST);
        preIntent.putExtra("FLAG",PRE_FLAG);
        PendingIntent prePIntent = PendingIntent.getBroadcast(this,0,preIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        rv.setOnClickPendingIntent(R.id.notification_image_previous,prePIntent);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setTicker(title);
        builder.setContentIntent(pi);
        builder.setContent(rv);
        builder.setOngoing(true);//设置正在运行
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP)
        {
            builder.setVisibility(Notification.VISIBILITY_PUBLIC);
        }
        startForeground(NOTIFICATION_ID,builder.build());
    }
    @Override
    public IBinder onBind(Intent intent)
    {
        return new MusicBinder();
    }

    private class ControlBroadcast extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            int flag = intent.getIntExtra("FLAG",-1);
            switch (flag)
            {
                case PAUSE_FLAG:
                    if(mc.getPlayState()==IConstants.PLAYER_PLAYING)
                    {
                        mc.pause();
                    }
                    else
                    {
                        mc.playById(-1);
                    }
                    break;
                case NEXT_FLAG:
                    mc.next();
                    break;
                case PRE_FLAG:
                    mc.pre();
                    break;
            }
        }
    }
    private void cancelNotification()
    {
        stopForeground(true);
        mNotificationManager.cancel(NOTIFICATION_ID);
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if(mControlBroadcast!=null)
        {
            unregisterReceiver(mControlBroadcast);
        }
    }
}
