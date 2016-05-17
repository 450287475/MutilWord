package com.wangdao.mutilword.service;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import com.wangdao.mutilword.bean.MusicJsonBean;
import com.wangdao.mutilword.constant.IConstants;

import java.io.IOException;
import java.util.List;

/**
 * Created by gourdboy on 2016/5/12.
 */
public class MusicController implements IConstants,MediaPlayer.OnPreparedListener,MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnCompletionListener
{
    private MediaPlayer mPlayer;
    private Context mContext;
    private int mPlayState = -1;
    private int mCurPlayIndex = -1;
    private List<MusicJsonBean.MusicBeanContent> musicList;
    private Handler musicHandler;
    public MusicController(Context context)
    {
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnBufferingUpdateListener(this);
        mPlayer.setOnCompletionListener(this);
        mContext = context;
    }
    public void setMusicHandler(Handler handler)
    {
        this.musicHandler = handler;
    }
    public void setMusicURL(List<MusicJsonBean.MusicBeanContent> musicList)
    {
        this.musicList = musicList;
    }
    public boolean playById(int id)
    {
        if(mCurPlayIndex==id||id==-1&&mCurPlayIndex>=0)
        {
            if(!mPlayer.isPlaying())
            {
                mPlayer.start();
                mPlayState = PLAYER_PLAYING;
                sendBroadCast();
            }
            return true;
        }
        if(preparePlayer(id))
        {
            mPlayState = PLAYER_PLAYING;
            return true;
        }
        return false;
    }
    public boolean pre()
    {
        if(!musicListIsEmpty())
        {
            --mCurPlayIndex;
            mCurPlayIndex = reviseIndex(mCurPlayIndex);
            Log.i("MusicController","preIndex"+mCurPlayIndex);
            if(!preparePlayer(mCurPlayIndex))
            {
                return false;
            }
            return true;
        }
        return false;
    }
    public boolean next()
    {
        if(!musicListIsEmpty())
        {
            ++mCurPlayIndex;
            mCurPlayIndex = reviseIndex(mCurPlayIndex);
            Log.i("mediaplayerrr",mCurPlayIndex+"   "+musicList.get(mCurPlayIndex).musicurl);
            Log.i("MusicController","nextIndex"+mCurPlayIndex);
            if(!preparePlayer(mCurPlayIndex))
            {
                return false;
            }
            return true;
        }
        return false;
    }
    public boolean pause()
    {
        if(mPlayState!=PLAYER_PLAYING)
        {
            Toast.makeText(mContext, "音乐未播放" , Toast.LENGTH_SHORT).show();
            return false;
        }
        mPlayer.pause();
        mPlayState = PLAYER_PAUSE;
        sendBroadCast();
        return true;
    }
    private int reviseIndex(int mCurPlayIndex)
    {
        if(mCurPlayIndex<=0||mCurPlayIndex>musicList.size()-1)
        {
            return 0;
        }
//        Log.i("MusicController","reviseIndex"+mCurPlayIndex);
        return mCurPlayIndex;
    }
    private boolean musicListIsEmpty()
    {
        if(musicList==null||musicList.isEmpty())
        {
            return true;
        }
        return false;
    }
    public boolean preparePlayer(int index)
    {
        if(musicListIsEmpty())
        {
            Toast.makeText(mContext, "获取歌曲文件中，播放器未准备好", Toast.LENGTH_SHORT).show();
            return false;
        }
        mCurPlayIndex = index;
        Log.i("MusicController","prepare reset");
        Message msg = musicHandler.obtainMessage();
        msg.what = PLAYER_RESET;
        msg.sendToTarget();
        mPlayer.reset();
        try
        {
            Log.i("MusicController","prepare setdatasource");
            mPlayer.setDataSource(SERVER_ADDR+musicList.get(index).musicurl);
            Log.i("MusicController","prepare async");
            mPlayer.prepareAsync();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            mPlayState = PLAYER_INVALID;
            index = reviseIndex(index);
            playById(index);
            return false;
        }
        return true;
    }
    public int getDuration()
    {
        if(mPlayState==PLAYER_PLAYING||mPlayState==PLAYER_PAUSE||mPlayState==PLAYER_PREPARE)
        {
            return mPlayer.getDuration();
        }
        return 0;
    }
    public int getPosition()
    {
        if(mPlayState==PLAYER_PLAYING||mPlayState==PLAYER_PAUSE||mPlayState==PLAYER_PREPARE)
        {
            return mPlayer.getCurrentPosition();
        }
        return 0;
    }
    public boolean seekToPosition(int position)
    {
        if(mPlayState==PLAYER_PLAYING||mPlayState==PLAYER_PAUSE||mPlayState==PLAYER_PREPARE)
        {
            mPlayer.seekTo(position);
            return true;
        }
        return false;
    }
    @Override
    public void onPrepared(MediaPlayer mp)
    {
        switch (mPlayState)
        {
            case PLAYER_PLAYING:
                Log.i("MusicController","playindex"+mCurPlayIndex);
                mPlayer.start();
                break;
            default:
                mPlayState = PLAYER_PREPARE;
                break;
        }
        Message msg = musicHandler.obtainMessage();
        msg.what = PLAYER_PREPARE;
        msg.arg1 = mPlayState;
        msg.arg2 = mCurPlayIndex;
        musicHandler.sendMessage(msg);
//        sendBroadCast();
    }
    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent)
    {
        Message msg = musicHandler.obtainMessage();
        msg.what = PLAYER_BUFFERED;
        msg.obj = percent;
        musicHandler.sendMessage(msg);
//        sendBroadCast();
    }
    @Override
    public void onCompletion(MediaPlayer mp)
    {
        Log.i("MusicController","on completion");
        Message msg = musicHandler.obtainMessage();
        msg.what = PLAYER_COMPLETE;
        msg.arg1 = mPlayState;
        musicHandler.sendMessage(msg);
        if(mPlayState==PLAYER_PLAYING)
            next();
    }
    public int getPlayState()
    {
        return mPlayState;
    }
    private void sendBroadCast()
    {
        Message msg = musicHandler.obtainMessage();
        msg.what = PLAYER_STATE;
        msg.arg1 = mPlayState;
        musicHandler.sendMessage(msg);
    }
}
