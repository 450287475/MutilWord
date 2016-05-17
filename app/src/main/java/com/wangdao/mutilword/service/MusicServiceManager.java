package com.wangdao.mutilword.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.wangdao.mutilword.interfaces.DoAfterServiceConnectComplete;


/**
 * Created by gourdboy on 2016/5/12.
 */
public class MusicServiceManager
{
    private ServiceConnection mConn;
    public MusicService.MusicBinder musicService;
    private Context mContext;
    private DoAfterServiceConnectComplete mDoAfterServiceConnectComplete;
    public MusicServiceManager(Context context)
    {
        mContext = context;
        initConn();
    }
    private void initConn()
    {
        mConn = new ServiceConnection()
        {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service)
            {
                musicService = (MusicService.MusicBinder) service;
                if(service!=null)
                {
                    mDoAfterServiceConnectComplete.doAfterServiceConnectComplete(musicService);
                }
            }
            @Override
            public void onServiceDisconnected(ComponentName name)
            {

            }
        };
    }
    public void connectService()
    {
        Intent intent = new Intent(mContext,MusicService.class);
        mContext.bindService(intent,mConn, Context.BIND_AUTO_CREATE);
        Log.i("MusicServiceManager","bind to music service");
    }
    public void disConnextService()
    {
        mContext.unbindService(mConn);
    }
    //成功绑定service后调用该方法
    public void setOnServiceConnectComplete(DoAfterServiceConnectComplete doAfterServiceConnectComplete)
    {
        this.mDoAfterServiceConnectComplete = doAfterServiceConnectComplete;
    }
    public void exit()
    {
        if(musicService!=null)
        {
            musicService.exit();
        }
        mContext.unbindService(mConn);
    }
}
