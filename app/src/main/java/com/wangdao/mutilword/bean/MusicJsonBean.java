package com.wangdao.mutilword.bean;

import java.util.List;

/**
 * Created by gourdboy on 2016/4/20.
 */
public class MusicJsonBean
{
    public int retcode;
    public List<MusicBeanContent> data;
    public class MusicBeanContent
    {
        public int id;
        public String musicurl;
        public String readingurl;
        public String title;
        public String lrcurl;
        @Override
        public String toString()
        {
            return "MusicBeanContent{" +
                    "id=" + id +
                    ", musicurl='" + musicurl + '\'' +
                    ", readingurl='" + readingurl + '\'' +
                    ", title='" + title + '\'' +
                    ", lrc='" + lrcurl + '\'' +
                    '}';
        }
    }
    @Override
    public String toString()
    {
        return "MusicJsonBean{" +
                "retcode=" + retcode +
                ", data=" + data +
                '}';
    }
}
