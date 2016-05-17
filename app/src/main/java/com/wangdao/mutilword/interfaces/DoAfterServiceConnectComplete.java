package com.wangdao.mutilword.interfaces;


import com.wangdao.mutilword.service.MusicService;

/**
 * Created by gourdboy on 2016/5/12.
 */
public interface DoAfterServiceConnectComplete
{
    void doAfterServiceConnectComplete(MusicService.MusicBinder musicService);
}
