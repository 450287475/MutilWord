package com.wangdao.mutilword.bean;
import java.io.Serializable;
import java.util.List;

/**
 * Created by gourdboy on 2016/4/21.
 */
public class LrcBean implements Serializable
{
    public LrcBean(String url)
    {
        this.url = url;
    }
    public String url;
    public String title;
    public String singer;
    public String album;
    public List<LrcLineContentBean> infos;

}
