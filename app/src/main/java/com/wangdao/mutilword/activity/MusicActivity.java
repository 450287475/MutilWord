package com.wangdao.mutilword.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.lidroid.xutils.BitmapUtils;

import com.wangdao.mutilword.R;
import com.wangdao.mutilword.adapter.MusicLrcAdapter;
import com.wangdao.mutilword.adapter.MusicViewPagerAdapter;
import com.wangdao.mutilword.application.ApplicationInfo;
import com.wangdao.mutilword.bean.LrcBean;
import com.wangdao.mutilword.bean.LrcLineContentBean;
import com.wangdao.mutilword.bean.MusicJsonBean;
import com.wangdao.mutilword.constant.IConstants;
import com.wangdao.mutilword.interfaces.DoAfterServiceConnectComplete;
import com.wangdao.mutilword.service.MusicService;
import com.wangdao.mutilword.service.MusicServiceManager;
import com.wangdao.mutilword.utils.MusicTimer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class MusicActivity extends Activity implements DoAfterServiceConnectComplete,IConstants
        ,ViewPager.OnPageChangeListener,SeekBar.OnSeekBarChangeListener
{
    private SeekBar sb_music_time;
    private TextView tv_music_time;
    private TextView tv_music_title;
    private ImageButton bt_music_play;
    private ImageButton bt_music_pause;
    private ViewPager vp_music_content;
    private MusicServiceManager msm;
    private MusicService.MusicBinder mService;
    private List<MusicJsonBean.MusicBeanContent> musicContentList;//每一首歌的信息，歌词，歌名等
    private List<LrcBean> lrcBeanList;//歌词文件
    private Map<Integer,LrcBean> lrcBeanMap;
    private List<ListView> showLrcViewList;
    private List<LrcLineContentBean> infos; //歌词每一句，用户获取歌词索引
    private int playerstate = -1;
    private MusicViewPagerAdapter mvpa;
    private MusicLrcAdapter mla;
    private int curPage = 0;
    private int totalPage = 0;//一共需要载入的页面
    private int curLoadPager = 0;//目前载入的页面
    private boolean isDownloadLrc = false;
    private MusicTimer mMusicTimer;
    private BitmapUtils bitmapUtils;
    private Handler serviceUIH = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case DOWNLOAD_MJSON_COMPLETE:
                    musicContentList = (List<MusicJsonBean.MusicBeanContent>) msg.obj;
                    totalPage = musicContentList.size();
                    initViewPager();
                    break;
                case DOWNLOAD_LRC_COMPLETE:
                    refreshLrcBeanMap();
                    if(lrcBeanList==null)
                    {
                        lrcBeanList = (List<LrcBean>) msg.obj;
                        mService.updateNotification(mService.getImage(musicContentList.get(0).imgurl),musicContentList.get(0).title,musicContentList.get(0).attr);
                        isDownloadLrc = true;
                    }
                    mla = new MusicLrcAdapter(MusicActivity.this);
                    if(lrcBeanMap.get(curPage)!=null)
                    {
                        infos = lrcBeanMap.get(curPage).infos;
                        mla.setLrc(infos);//初始化
                        showLrcViewList.get(curPage).setAdapter(mla);
                    }
                    break;
                case PLAYER_PREPARE:
                    playerstate = msg.arg1;
                    if(isDownloadLrc&&curPage!=msg.arg2)
                    {
                        lrcPrepare(msg.arg2);
                        sb_music_time.setProgress(0);
                        tv_music_title.setText(musicContentList.get(curPage).title);
                        vp_music_content.setCurrentItem(curPage);
                        mMusicTimer.startTimer();
                    }
                    else if(playerstate==PLAYER_PLAYING)
                    {
                        mMusicTimer.startTimer();
                    }
                    initSeekBarUI();
                    mService.updateNotification(mService.getImage(musicContentList.get(curPage).imgurl),musicContentList.get(curPage).title,musicContentList.get(curPage).attr);
                    break;
                case PLAYER_RESET:
                    mMusicTimer.stopTimer();
                    break;
                case PLAYER_COMPLETE:
                    playerstate = msg.arg1;
                    mMusicTimer.stopTimer();
                    break;
                case PLAYER_BUFFERED:
                    sb_music_time.setSecondaryProgress((int) msg.obj);
                    break;
                case REFRESH_SEEKBAR:
                    refreshSeekBar(mService.getPosition(), mService.getDuration());
                    int index = getLrcLineIndex(infos);
                    ListView lv =  showLrcViewList.get(curPage);
                    mla.setIndex(index);
                    mla.notifyDataSetChanged();
                    lv.smoothScrollToPositionFromTop(index,lv.getHeight()/2,500);
                    break;
                case PLAYER_STATE:
                    playerstate = msg.arg1;
                    if(playerstate==PLAYER_PAUSE)
                    {
                        showPlayButton(true);
                        mMusicTimer.stopTimer();
                    }
                    else if(playerstate==PLAYER_PLAYING)
                    {
                        showPlayButton(false);
                        mMusicTimer.startTimer();
                    }
                    mService.updateNotification(mService.getImage(musicContentList.get(curPage).imgurl), musicContentList.get(curPage).title, musicContentList.get(curPage).attr);
                    break;
                case DOWNLOAD_IMG_COMPLETE:
                    mService.updateNotification((Bitmap) msg.obj, musicContentList.get(curPage).title, musicContentList.get(curPage).attr);
                    break;
            }
        }
    };
    private void refreshLrcBeanMap()
    {
        if(lrcBeanList!=null)
        {
            Map<String,Integer> map = new HashMap<>(musicContentList.size());
            for(int i=0;i<musicContentList.size();i++)
            {
                map.put(musicContentList.get(i).lrcurl,i);
                Log.i("lrcBeanMap",musicContentList.get(i).lrcurl+"    index"+i);
            }
            for(LrcBean lb:lrcBeanList)
            {
                Log.i("lrcBeanMap","Lrc Bean's url is:"+lb.url);
                if(map.get(lb.url)!=null)
                {
                    int index = map.get(lb.url);
                    Log.i("lrcBeanMap","the same index is "+index);
                    lrcBeanMap.put(index,lb);
                }
            }
           /* Set<Map.Entry<Integer,LrcBean>> set = lrcBeanMap.entrySet();
            for(Map.Entry<Integer,LrcBean> entry:set)
            {
                Log.i("lrcBeanMap",entry.getKey()+"  :  "+entry.getValue());
            }*/
        }
    }
    private  void initButton()
    {
        bt_music_play.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mService.play(curPage))
                {
                    playerstate = PLAYER_PLAYING;
                    mMusicTimer.startTimer();
                    showPlayButton(false);
                }
            }
        });
        bt_music_pause.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mService.pause())
                {
                    playerstate = PLAYER_PAUSE;
                    showPlayButton(true);
                }
            }
        });
    }
    private void initViewPager()
    {
        mService.setMusicURL(musicContentList);
        mService.preparePlayer(curPage);
        String strt = musicContentList.get(curPage).title;
        tv_music_title.setText(strt);
        initLRCView();
    }
    //初始化歌曲名称等
    private void initSeekBarUI()
    {
        initButton();
        String str1 = formatTime(mService.getPosition());
        String str2 = formatTime(mService.getDuration());
        String str = str1+"/"+str2;
        tv_music_time.setText(str);
    }
    private void refreshSeekBar(int curtime, int totaltime)
    {
        String tol = formatTime(totaltime);
        String cur;
//        Log.i("refreshSeekBar",curtime+" : "+totaltime);
//        Log.i("refreshSeekBar",cur);
        if(Math.abs(totaltime-curtime)<1000||curtime>totaltime)
        {
            cur = tol;
        }
        else
        {
            cur = formatTime(curtime);
        }
        String str = cur+"/"+tol;
        tv_music_time.setText(str);
        int rate = 0;
        if (totaltime != 0)
        {
            rate = (int) ((float) curtime / totaltime * 100);
        }
        sb_music_time.setProgress(rate);
    }
    private String formatTime(int time)
    {
        String str = "00:00";
        if(time!=0)
        {
            time /= 1000;
            int min = time / 60;
            int sec = time % 60;
            str = String.format("%02d:%02d", min, sec);
        }
        return str;
    }
    private void initLRCView()
    {
        loadMoreLrc();
        mvpa = new MusicViewPagerAdapter(this,showLrcViewList);
        vp_music_content.setAdapter(mvpa);
    }
    //动态加载
    private void loadMoreLrc()
    {
        if(musicContentList!=null&&!musicContentList.isEmpty())
        {
            if(totalPage>curLoadPager+3)
            {
                for (int i = 0; i < 3; i++)
                {
                    ListView lv = new ListView(MusicActivity.this);
                    lv.setDescendantFocusability(ListView.FOCUS_BLOCK_DESCENDANTS);
                    lv.setDividerHeight(0);
                    lv.setVerticalScrollBarEnabled(false);
                    lv.setAnimation(AnimationUtils.loadAnimation(MusicActivity.this, R.anim.music_alpha_z));
                    showLrcViewList.add(lv);
                    curLoadPager ++;
                    mService.downLoadLRC(musicContentList.get(curLoadPager-1).lrcurl);
                }
            }
            else if(totalPage<=curLoadPager+3)
            {
                for (int i = curLoadPager; i < totalPage; i++)
                {
                    ListView lv = new ListView(MusicActivity.this);
                    lv.setDescendantFocusability(ListView.FOCUS_BLOCK_DESCENDANTS);
                    lv.setDividerHeight(0);
                    lv.setVerticalScrollBarEnabled(false);
                    lv.setAnimation(AnimationUtils.loadAnimation(MusicActivity.this,R.anim.music_alpha_z));
                    showLrcViewList.add(lv);
                    mService.downLoadLRC(musicContentList.get(i).lrcurl);
//                    Log.i("downloadImage",SERVER_ADDR+musicContentList.get(i).imgurl);
                }
                curLoadPager = totalPage;
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        Log.i("somtete",getCacheDir().getAbsolutePath());
        sb_music_time = (SeekBar) findViewById(R.id.sb_music_time);
        tv_music_time = (TextView) findViewById(R.id.tv_music_time);
        tv_music_title = (TextView) findViewById(R.id.tv_music_title);
        bt_music_pause = (ImageButton) findViewById(R.id.bt_music_pause);
        bt_music_play = (ImageButton) findViewById(R.id.bt_music_play);
        vp_music_content = (ViewPager) findViewById(R.id.vp_music_content);
        sb_music_time.setOnSeekBarChangeListener(this);
        vp_music_content.setOnPageChangeListener(this);
        mMusicTimer = new MusicTimer(serviceUIH);
        bitmapUtils = new BitmapUtils(this);
        msm = ApplicationInfo.mMusicServiceManager;
        msm.connectService();
        msm.setOnServiceConnectComplete(this);
        showLrcViewList = new ArrayList<>();
        lrcBeanMap = new HashMap<>();
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
    @Override
    protected void onDestroy()
    {
        msm.exit();
        super.onDestroy();
    }
    @Override
    public void doAfterServiceConnectComplete(MusicService.MusicBinder musicService)
    {
        mService = musicService;
        musicService.setHandler(serviceUIH);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
    {

    }
    @Override
    public void onPageSelected(int position)
    {
//        Log.i("onPageSelected","onPageSelected"+position);
        if(curPage!=position)
        {
            Log.i("onPageSelected","cur page index:  "+curPage+"     onPageSelected   "+position);
            lrcPrepare(position);
            mMusicTimer.stopTimer();
            sb_music_time.setProgress(0);
            tv_music_title.setText(musicContentList.get(position).title);
            if (playerstate == PLAYER_PLAYING)
            {
                mService.play(position);
            }
            else
            {
                mService.preparePlayer(position);
            }
        }
    }
    @Override
    public void onPageScrollStateChanged(int state)
    {
    }
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
    {

    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar)
    {
        mMusicTimer.stopTimer();
    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar)
    {
        mService.seekToPosition((int) ((float)seekBar.getProgress()/100*mService.getDuration()));
        mMusicTimer.startTimer();
    }
    private int getLrcLineIndex(List<LrcLineContentBean> infos)
    {
        int current;
        int index = 0;
        if(infos!=null)
        {
            current = mService.getPosition();
            for (int i = 0; i < infos.size(); i++)
            {
                if (i < infos.size() - 1)
                {
                    if (current < infos.get(i).milliseconds && i == 0)
                    {
                        index = i;
                    }
                    else if (current > infos.get(i).milliseconds && current < infos.get(i + 1).milliseconds)
                    {
                        index = i;
                    }
                }
                else if (i == infos.size() - 1 && current > infos.get(i).milliseconds)
                {
                    index = i;
                }
            }
        }
//        Log.i("getLrcLineIndex",index+"");
        return index;
    }
    private void lrcPrepare(int pageIndex)
    {
        curPage = pageIndex;
        if (curLoadPager != totalPage && curLoadPager == curPage + 1)
        {
            Log.i("loadMoreLrc","lrcPrepare   cur load page: "+curLoadPager+"   cur page:  "+curPage);
            loadMoreLrc();
            mvpa.notifyDataSetChanged();
        }
        if(lrcBeanMap.get(curPage)!=null)
        {
            infos = lrcBeanMap.get(curPage).infos;
            mla.setLrc(infos);
            mla.notifyDataSetChanged();
        }
        showLrcViewList.get(curPage).setAdapter(mla);
    }
}
