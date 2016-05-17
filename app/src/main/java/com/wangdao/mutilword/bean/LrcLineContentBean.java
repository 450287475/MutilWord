package com.wangdao.mutilword.bean;

import java.io.Serializable;

/**
 * Created by gourdboy on 2016/5/12.
 */
public class LrcLineContentBean implements Serializable
{
    public long milliseconds;
    public String content;
    public LrcLineContentBean(long milliseconds, String content)
    {
        this.milliseconds = milliseconds;
        this.content = content;
    }

    @Override
    public String toString()
    {
        return "LrcLineContentBean{" +
                "milliseconds=" + milliseconds +
                ", content='" + content + '\'' +
                '}';
    }
}
