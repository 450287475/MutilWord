package com.wangdao.mutilword.constant;

/**
 * Created by gourdboy on 2016/5/12.
 */
public interface IConstants
{
    //handler信号
    int DOWNLOAD_MJSON_COMPLETE = 1;
    int DOWNLOAD_LRC_COMPLETE = 2;
    int DOWNLOAD_IMG_COMPLETE = 7;
    int REFRESH_SEEKBAR = 3;
    int PLAYER_PREPARE = 0;
    int PLAYER_COMPLETE = 4;
    int PLAYER_BUFFERED = 5;
    int PLAYER_STATE = 6;
    int PLAYER_RESET = 8;

    int PLAYER_PLAYING  = 1;
    int PLAYER_PAUSE = 2;
    int PLAYER_INVALID = 3;
    String SERVER_ADDR = "http://bmob-cdn-418.b0.upaiyun.com";//听力页面
    String JSON_ADDR = "/2016/04/23/528acb65408d8bfe80024c2cd8854efe.json";//听力页面
}
