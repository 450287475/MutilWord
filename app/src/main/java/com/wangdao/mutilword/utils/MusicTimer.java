package com.wangdao.mutilword.utils;

import android.os.Handler;
import android.os.Message;

import com.wangdao.mutilword.constant.IConstants;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by gourdboy on 2016/5/13.
 */
public class MusicTimer implements IConstants
{
    private static final int INTERVAL_TIME = 1000;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private Handler mHandler;
    private boolean mTimerIsStart = false;
    public MusicTimer(Handler handler)
    {
        this.mHandler = handler;
        mTimer = new Timer();
    }
    public void startTimer()
    {
        if(mHandler==null||mTimerIsStart)
        {
            return;
        }
        mTimerTask = new MusicTimerTask();
        mTimer.schedule(mTimerTask,INTERVAL_TIME,INTERVAL_TIME);
        mTimerIsStart = true;
    }
    public void stopTimer()
    {
        if(!mTimerIsStart)
        {
            return;
        }
        mTimerIsStart = false;
        if(mTimerTask!=null)
        {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }
    private class MusicTimerTask extends TimerTask
    {
        @Override
        public void run()
        {
            if(mHandler!=null)
            {
                Message msg = mHandler.obtainMessage();
                msg.what = REFRESH_SEEKBAR;
                msg.sendToTarget();
            }
        }
    }
}
