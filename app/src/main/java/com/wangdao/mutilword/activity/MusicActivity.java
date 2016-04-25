package com.wangdao.mutilword.activity;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.wangdao.mutilword.R;
import com.wangdao.mutilword.bean.LrcBean;
import com.wangdao.mutilword.bean.LrcContentBean;
import com.wangdao.mutilword.bean.MusicJsonBean;
import com.wangdao.mutilword.constant.Constant;
import com.wangdao.mutilword.view.ShowLrcView;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MusicActivity extends Activity implements SeekBar.OnSeekBarChangeListener,
        MediaPlayer.OnPreparedListener,MediaPlayer.OnBufferingUpdateListener,MediaPlayer.OnCompletionListener,MediaPlayer.OnErrorListener
{
    //    需要INTERNET权限,gradle里android中加入：useLibrary 'org.apache.http.legacy'
    private SeekBar sb_music_time;
    private TextView tv_music_time;
    private TextView tv_music_title;
    private MediaPlayer player;
    private ImageButton bt_music_play;
    private ImageButton bt_music_pause;
    private List<MusicJsonBean.MusicBeanContent> musicContentList;
    private static final int PLAYER_PLAYING = 1;// 播放状态
    private static final int PLAYER_PAUSING = 2;// 暂停状态
    private static final int PLAYER_STOPPING = 3;// 停止状态
    private int PLAYER_CURRENT_STATE = 0;// 当前状态
    private String timeDuration;//一首歌的持续时间
    private boolean playerIsPrepared = false;//判断mediaplayer是否准备就绪
    private boolean isUpdatingSeekBar = false;//判断是否更新seekbar
    private boolean seekBarIsRunning = false;//判断seekbar是否运行
    private String musicURL;
    private int duration;//歌曲的毫秒数
    private List<LrcBean> lrcBeanList;
    private String title;
    private String errorTitle;
    private ViewPager vp_music_content;
    private List<ShowLrcView> showLrcViewList;
    //    private ShowLrcView slv_music_lrc;
    private ViewPagerAdapter viewPagerAdapter;
    private int vpIndex = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        lrcBeanList = new ArrayList<>();
        showLrcViewList = new ArrayList<>();
        bt_music_pause = (ImageButton) findViewById(R.id.bt_music_pause);
        bt_music_play = (ImageButton) findViewById(R.id.bt_music_play);
//        slv_music_lrc = (ShowLrcView) findViewById(R.id.slv_music_lrc);
        vp_music_content = (ViewPager) findViewById(R.id.vp_music_content);
        tv_music_title = (TextView) findViewById(R.id.tv_music_title);
        bt_music_play.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                play();
            }
        });
        bt_music_pause.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pause();
            }
        });
        musicContentList = new ArrayList<>();
        sb_music_time = (SeekBar) findViewById(R.id.sb_music_time);
        sb_music_time.setOnSeekBarChangeListener(this);
        tv_music_time = (TextView) findViewById(R.id.tv_music_time);
        String url = Constant.SERVER_ADDR+Constant.JSON_ADDR;
        new HttpUtils().send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                Gson gson = new Gson();
                MusicJsonBean musicJsonBean = gson.fromJson(responseInfo.result, MusicJsonBean.class);
                musicContentList = musicJsonBean.data;
                musicURL = Constant.SERVER_ADDR+musicContentList.get(0).musicurl;
                initPlayerView();
                initLrcList();
                title = musicContentList.get(0).title;
                tv_music_title.setText(title);
                for(int i=0;i<musicContentList.size();i++)
                {
                    ShowLrcView showLrcView = new ShowLrcView(MusicActivity.this);
                    showLrcView.setAnimation(AnimationUtils.loadAnimation(MusicActivity.this,R.anim.music_alpha_z));
                    showLrcViewList.add(showLrcView);
                }
                showLrcViewList.get(0).setStr(title);
                bt_music_pause.setClickable(true);
                bt_music_play.setClickable(true);
                player.setOnPreparedListener(MusicActivity.this);
                player.setOnCompletionListener(MusicActivity.this);
                player.setOnBufferingUpdateListener(MusicActivity.this);
                player.setOnErrorListener(MusicActivity.this);
            }
            @Override
            public void onFailure(HttpException e, String s)
            {

            }
        });
    }
    //    更新歌词文件并解析
    private void initLrcList()
    {
        for(int i=0;i<musicContentList.size();i++)
        {
            final String name = musicContentList.get(i).id+"";
            String url = Constant.SERVER_ADDR+musicContentList.get(i).lrcurl;
            new HttpUtils().send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>()
            {
                private LrcBean lrcBean;
                private List<LrcContentBean> lrc = new ArrayList<>();
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo)
                {
                    lrcBean = new LrcBean();
                    String str = responseInfo.result;
                    String[] str1 = str.split("\n");
                    File file = new File(getCacheDir(),name);
                    for(String s:str1)
                    {
                        parseLine(s);
                    }
                    lrcBean.infos = lrc;
         /*           for(int i=0;i<lrc.size();i++)
                    {
                        Log.i("initLrcList",lrc.get(i).milliseconds+"   "+lrc.get(i).content+"   "+i);
                    }*/
                    Message msg = MusicHandler.obtainMessage();
                    msg.what = 2;
                    msg.obj = lrcBean;
                    MusicHandler.sendMessage(msg);
                    try
                    {
                        FileOutputStream fos = new FileOutputStream(file);
                        ObjectOutputStream oos = new ObjectOutputStream(fos);
                        oos.writeObject(lrcBean);
                        oos.flush();
                        fos.close();
                        oos.close();
                    }
                    catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(HttpException e, String s)
                {

                }
                private void parseLine(String str)
                {
                    long milliseconds;
                    String currentContent = "";
                    if(str.startsWith("[ti:"))
                    {
                        lrcBean.title = str.substring(4,str.length()-1);
                    }
                    else if(str.startsWith("[ar:"))
                    {
                        lrcBean.singer = str.substring(4,str.length()-1);
                    }
                    else if(str.startsWith("[al:"))
                    {
                        lrcBean.album = str.substring(4,str.length()-1);
                    }
                    else
                    {
                        String reg = "\\[(\\d{2}:\\d{2}\\.\\d{2})\\]";
                        Pattern pattern = Pattern.compile(reg);
                        Matcher matcher = pattern.matcher(str);
                        while(matcher.find())
                        {
                            String msg = matcher.group(1);
                            milliseconds  = strToLong(msg);
                            String[] content = pattern.split(str);
                            for(int i=1;i<content.length;i++)
                            {
                                if(i==content.length-1)
                                {
                                    currentContent = content[i];
                                }
                            }
                            lrc.add(new LrcContentBean(milliseconds, currentContent));
                        }
                    }
                }
                private long strToLong(String msg)
                {
                    String[] s = msg.split(":");
                    int min = Integer.parseInt(s[0]);
                    String[] ss = s[1].split("\\.");
                    int sec = Integer.parseInt(ss[0]);
                    int mill = Integer.parseInt(ss[1]);
                    return min*60*1000+sec*1000+mill*10;
                }
            });
        }
    }
    private void showPlayButton(boolean flag)
    {
        if(flag)
        {
            bt_music_play.setVisibility(View.VISIBLE);
            bt_music_pause.setVisibility(View.GONE);
        }
        else
        {
            bt_music_play.setVisibility(View.GONE);
            bt_music_pause.setVisibility(View.VISIBLE);
        }
    }
    private void play()
    {
        if(player!=null)
        {
            errorTitle = "";
            if (playerIsPrepared)
            {
                player.start();
                if(!seekBarIsRunning)
                {
                    updateSeekBar();
                }
                else
                {
                    isUpdatingSeekBar = true;
                }
            }
            else
            {
                Toast.makeText(MusicActivity.this, "正在缓冲，请稍等", Toast.LENGTH_SHORT).show();
            }
            PLAYER_CURRENT_STATE = PLAYER_PLAYING;
            showPlayButton(false);
        }
        else
        {
            Toast.makeText(MusicActivity.this, "T.T无法播放", Toast.LENGTH_SHORT).show();
        }
    }
    private void pause()
    {
        if(player!=null&&PLAYER_CURRENT_STATE==PLAYER_PLAYING)
        {
            player.pause();
            PLAYER_CURRENT_STATE = PLAYER_PAUSING;
            isUpdatingSeekBar = false;
            showPlayButton(true);
        }
    }
    /*    Runnable mRunable = new Runnable()
        {
            @Override
            public void run()
            {
                showLrcViewList.get(vpIndex).setIndex(lrcIndex());
                showLrcViewList.get(vpIndex).invalidate();
                MusicHandler.postDelayed(mRunable,100);
            }
        };*/
    Handler MusicHandler = new Handler()
    {
        int i =1;
        int currentlrcBeanListSize;
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case 1:
                    String str = (String) msg.obj;
                    tv_music_time.setText(str);
//                    Log.i("timemmmmmm",str);
                    showLrcViewList.get(vpIndex).setIndex(lrcIndex());
                    showLrcViewList.get(vpIndex).invalidate();
//                    MusicHandler.post(mRunable);
                    break;
                case 2:
                    LrcBean lrcBean = (LrcBean) msg.obj;
                    lrcBeanList.add(lrcBean);
//                    Log.i("lrcBeanList",lrcBean.title+"   "+i++);
    /*                for(int i=0;i<lrcBeanList.size();i++)
                    {
                        List<LrcContentBean> list = lrcBeanList.get(i).infos;
                        for(int j=0;j<list.size();j++)
                        {
                            Log.i("initLrcList", list.get(j).milliseconds + "   " + list.get(j).content + "   " + i);
                        }
                    }*/
                    if(viewPagerAdapter==null)
                    {
                        currentlrcBeanListSize = lrcBeanList.size();
                        showLrcViewList.get(0).setLrclist(getCurrentLrcList(musicContentList.get(0).title));
                        showLrcViewList.get(0).invalidate();
                        viewPagerAdapter = new ViewPagerAdapter();
                        vp_music_content.setAdapter(viewPagerAdapter);
                        vp_music_content.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
                        {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
                            {

                            }
                            @Override
                            public void onPageSelected(int position)
                            {
                                musicURL = Constant.SERVER_ADDR+musicContentList.get(position).musicurl;
                                title = musicContentList.get(position).title;
                                tv_music_title.setText(title);
                                seekBarIsRunning = false;
                                sb_music_time.setProgress(0);
                                sb_music_time.setSecondaryProgress(0);
                                vpIndex = position;
                                showLrcViewList.get(position).setLrclist(getCurrentLrcList(title));
                                showLrcViewList.get(position).invalidate();
                                rePreparePlayer();
                                errorTitle = "";
                            }
                            @Override
                            public void onPageScrollStateChanged(int state)
                            {

                            }
                        });
                    }
                    else if(currentlrcBeanListSize!=lrcBeanList.size())
                    {
                        showLrcViewList.get(0).setLrclist(getCurrentLrcList(musicContentList.get(0).title));
                        currentlrcBeanListSize = lrcBeanList.size();
                        viewPagerAdapter.notifyDataSetChanged();
                    }
                    break;
                case 3:
//                    Log.i("Musichandler",title);
//                    showLrcViewList.get(vpIndex).setIndex(lrcIndex());
//                    slv.setLrclist();
//                    showLrcViewList.get(vpIndex).invalidate();
                    break;
                default:
                    break;
            }
        }
    };
    private void updateSeekBar()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                isUpdatingSeekBar = true;
                synchronized (player)
                {
                    seekBarIsRunning = true;
                    while (isUpdatingSeekBar)
                    {
                        if (PLAYER_CURRENT_STATE == PLAYER_PLAYING && player != null && isUpdatingSeekBar)
                        {
                            int current = player.getCurrentPosition();
                            String currentTime = formatTime(current);
                            Log.i("currentTime",current+"   "+duration);
                            String str = currentTime + "/" + timeDuration;
                            if(playerIsPrepared&&current<duration)
                            {
                                sb_music_time.setProgress(current);
                                Message msg = MusicHandler.obtainMessage();
                                msg.what = 1;
                                msg.obj = str;
                                MusicHandler.sendMessage(msg);
//                                Log.i("isUpdatingSeekBar","yes");
                            }
                            else
                            {
                                isUpdatingSeekBar = false;
                            }
                        }
                        SystemClock.sleep(1000);
                    }
                    seekBarIsRunning = false;
                }
            }
        }).start();
    }
    private List<LrcContentBean> getCurrentLrcList(String title)
    {
        for(int i=0;i<lrcBeanList.size();i++)
        {
            if(title.equals(lrcBeanList.get(i).title))
            {
                return lrcBeanList.get(i).infos;
            }
        }
        return null;
    }
    private int lrcIndex()
    {
        int current = 0;
        int index = 0;
        List<LrcContentBean> list = getCurrentLrcList(title);
        if(list!=null)
        {
            if (PLAYER_CURRENT_STATE == PLAYER_PLAYING)
            {
                if (player != null)
                {
                    current = player.getCurrentPosition();
                    if (current < duration)
                    {
                        for (int i = 0; i < list.size(); i++)
                        {
                            if (i < list.size() - 1)
                            {
                                if (current < list.get(i).milliseconds && i == 0)
                                {
                                    index = i;
                                }
                                if (current > list.get(i).milliseconds
                                        && current < list.get(i + 1).milliseconds)
                                {
                                    index = i;
                                }
                            }
                            if (i == list.size() - 1
                                    && current > list.get(i).milliseconds)
                            {
                                index = i;
                            }
                        }
                    }
                }
       /* if(list.size()!=0)
        {
            Log.i("lrcIndex", list.get(index).milliseconds + "   " + list.get(index).content+"   "+current+"  "+duration+"   "   +index);
        *//*    for(int i=0;i<list.size();i++)
            {
                Log.i("lrcIndex",list.get(index).milliseconds + "   " + list.get(index).content+"   "+i);
            }*//*
        }*/
            }
        }
        return index;
    }
    private void initPlayerView()
    {
        if(player==null)
        {
            player = new MediaPlayer();
            setPlyerResource();
            player.prepareAsync();
            tv_music_title.setText("...");
            tv_music_time.setText("00:00/00:00");
        }
    }
    private void rePreparePlayer()
    {
        playerIsPrepared = false;
        if(player!=null)
        {
            player.reset();
            setPlyerResource();
            player.prepareAsync();
        }
    }
    private void setPlyerResource()
    {
        if(player!=null)
        {
            try
            {
                Uri uri = Uri.parse(musicURL);
                player.setDataSource(this, uri);
                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    private String formatTime(int duration)
    {
        int second = duration/1000;
        int minute = second/60;
        second = second - minute*60;
        StringBuilder sb = new StringBuilder();
        sb.append(minute>9?minute+"":"0"+minute);
        sb.append(":");
        sb.append(second>9?second:"0"+second);
        return sb.toString();
    }
    @Override
    public void onPrepared(MediaPlayer mp)
    {
        duration = player.getDuration();
        sb_music_time.setMax(duration);
        timeDuration = formatTime(duration);
        String str = "00:00/"+timeDuration;
        tv_music_time.setText(str);
        playerIsPrepared = true;
        if(PLAYER_CURRENT_STATE == PLAYER_PLAYING)
        {
            player.start();
            if(!seekBarIsRunning)
            {
                updateSeekBar();
            }
            else
            {
                isUpdatingSeekBar = true;
            }
        }
    }
    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent)
    {
        int progress = (duration*percent)/100;
        sb_music_time.setSecondaryProgress(progress);
    }
    @Override
    public void onCompletion(MediaPlayer mp)
    {
        if(title.equals(errorTitle))
        {
            if(player!=null)
            {
                player.reset();
                PLAYER_CURRENT_STATE = PLAYER_STOPPING;
            }
        }
        else
        {
            pause();
        }
        sb_music_time.setProgress(0);
        String str = "00:00/"+timeDuration;
        tv_music_time.setText(str);
    }
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
    {

    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar)
    {
        if(player!=null&&PLAYER_CURRENT_STATE==PLAYER_PLAYING)
        {
            isUpdatingSeekBar = false;
        }
    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar)
    {
        if(player!=null&&!title.equals(errorTitle))
        {
            player.seekTo(seekBar.getProgress());
            if(PLAYER_CURRENT_STATE==PLAYER_PLAYING)
            {
                updateSeekBar();
            }
        }
        else
        {
            Toast.makeText(MusicActivity.this, "出错啦！音频无法播放T.T", Toast.LENGTH_SHORT).show();
            sb_music_time.setProgress(0);
        }
    }
    @Override
    protected void onDestroy()
    {
        if(player!=null)
        {
            if(PLAYER_CURRENT_STATE==PLAYER_PLAYING)
            {
                player.stop();
            }
            player.reset();
            player.release();
            player = null;
        }
        super.onDestroy();
    }
    private class ViewPagerAdapter extends PagerAdapter
    {
        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view == object;
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
            ShowLrcView slv = showLrcViewList.get(position);
//            Log.i("instantiateItem",title);
            container.addView(slv);
            return slv;
        }
        @Override
        public int getCount()
        {
            return lrcBeanList.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            ShowLrcView slv = (ShowLrcView) object;
            container.removeView(slv);
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra)
    {
        if(player!=null)
        {
            Log.i("ON_ERROR", "ON ERROR"+"   "+what);
            Toast.makeText(MusicActivity.this, "获取歌曲失败T.T,下一首也许更好听哦^^", Toast.LENGTH_SHORT).show();
            errorTitle = title;
            if (PLAYER_CURRENT_STATE == PLAYER_PLAYING)
            {
                PLAYER_CURRENT_STATE = PLAYER_STOPPING;
                showPlayButton(true);
            }
        }
        return false;
    }
}
