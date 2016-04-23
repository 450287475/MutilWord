package com.wangdao.mutilword.bean;

import java.io.Serializable;

/**
 * Created by gourdboy on 2016/4/22.
 */
public class LrcContentBean implements Serializable
{
    public long milliseconds;
    public String content;
    public LrcContentBean(long milliseconds, String content)
    {
        this.milliseconds = milliseconds;
        this.content = content;
    }

    @Override
    public String toString()
    {
        return "LrcContentBean{" +
                "milliseconds=" + milliseconds +
                ", content='" + content + '\'' +
                '}';
    }
}
